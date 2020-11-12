package me.vinceh121.unofficialsouthpark.entities;

import java.io.Serializable;
import java.util.List;
import java.util.StringJoiner;

public class Episode implements Serializable {
	private String image, uuid, details, date, title, url, season, episode;
	private List<String> mediagen;

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getSeason() {
		return season;
	}

	public void setSeason(String season) {
		this.season = season;
	}

	public String getEpisode() {
		return episode;
	}

	public void setEpisode(String episode) {
		this.episode = episode;
	}

	public List<String> getMediagen() {
		return mediagen;
	}

	public void setMediagen(List<String> mediagen) {
		this.mediagen = mediagen;
	}

	public String toSearchableString() {
		return getUuid() + " " +
				getDetails() + " " +
				getDate() + " " +
				getTitle() + " " +
				"season:" + getSeason() + " " +
				"episode:" + getEpisode();
	}

	@Override
	public String toString() {
		return "Episode{" +
				"image='" + image + '\'' +
				", uuid='" + uuid + '\'' +
				", details='" + details + '\'' +
				", date='" + date + '\'' +
				", title='" + title + '\'' +
				", url='" + url + '\'' +
				", season='" + season + '\'' +
				", episode='" + episode + '\'' +
				", mediagen=" + mediagen +
				'}';
	}
}
