package me.vinceh121.unofficialsouthpark;

import android.util.Log;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.codec.binary.Base64;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.vinceh121.unofficialsouthpark.entities.Episode;
import me.vinceh121.unofficialsouthpark.entities.Geolocation;
import me.vinceh121.unofficialsouthpark.entities.MediaInfo;
import me.vinceh121.unofficialsouthpark.entities.SPData;

public class SPManager {
	public static final String DATA_URL = "https://raw.githubusercontent.com/wargio/plugin.video.southpark_unofficial/addon-data/addon-data-%s.json";
	public static final String SEASON_THUMBNAIL_URL = "https://raw.githubusercontent.com/wargio/plugin.video.southpark_unofficial/master/imgs/%s.jpg";
	public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; rv:25.0) Gecko/20100101 Firefox/25.0";
	private final HttpClient client;
	private final ObjectMapper mapper;
	private SPData data;

	public SPManager() {
		this(HttpClients.custom().setUserAgent(USER_AGENT).build(), new ObjectMapper());
		this.mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	}

	public SPManager(final HttpClient client, final ObjectMapper mapper) {
		this.client = client;
		this.mapper = mapper;
	}

	public void loadData(final Geolocation geo) throws IOException {
		final HttpGet jsonGet = new HttpGet(String.format(DATA_URL, geo.name().toLowerCase()));
		final SPData data = this.client.execute(jsonGet, new HttpClientResponseHandler<SPData>() {
			@Override
			public SPData handleResponse(final ClassicHttpResponse response) throws HttpException, IOException {
				return mapper.readValue(response.getEntity().getContent(), SPData.class);
			}
		});
		this.setData(data);
	}

	public MediaInfo loadMediaInfo(final String mediaGenUrl) throws IOException {
		final HttpGet jsonGet = new HttpGet(mediaGenUrl.replaceAll("\\{", "%7B").replaceAll("\\}", "%7D"));
		final MediaInfo info = this.client.execute(jsonGet, new HttpClientResponseHandler<MediaInfo>() {
			@Override
			public MediaInfo handleResponse(ClassicHttpResponse response) throws HttpException, IOException {
				final JsonNode node = mapper.readTree(response.getEntity().getContent());
				final JsonNode nodeInfo = node.at("/package/video/item/0"); // TODO maybe have better control over tree traversal
				Log.d("SPManager", "nodeInfo: " + nodeInfo);
				return mapper.convertValue(nodeInfo, MediaInfo.class);
			}
		});
		return info;
	}

	public SPData getData() {
		return data;
	}

	public void setData(SPData data) {
		this.data = data;
	}

	public static List<String> getStreams(final Episode ep) {
		final List<String> streams = new ArrayList<>();
		for (final String b64url : ep.getMediagen()) {
			final String url = new String(Base64.decodeBase64(b64url));
			streams.add(url);
		}
		return streams;
	}

	public static String getSeasonImage(final int seasonNbr) {
		return String.format(SEASON_THUMBNAIL_URL, Integer.toString(seasonNbr));
	}
}
