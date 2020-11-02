package me.vinceh121.unofficialsouthpark;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.android.exoplayer2.util.MimeTypes;

import java.util.List;
import java.util.Vector;

import me.vinceh121.unofficialsouthpark.entities.MediaInfo;

public class PlayerActivity extends AppCompatActivity {
	private SimpleExoPlayer player;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player);
		this.getSupportActionBar().hide();

		final StyledPlayerView playerView = findViewById(R.id.player_view);

		final MediaInfo mediaInfo = (MediaInfo) getIntent().getSerializableExtra("mediaInfo");

		final Uri uri = Uri.parse(mediaInfo.getRendition().get(0).getSrc());

		player = new SimpleExoPlayer.Builder(this).build();
		playerView.setPlayer(player);
		final MediaItem.Builder build = new MediaItem.Builder()
				.setMimeType(MimeTypes.APPLICATION_M3U8)
				.setUri(uri);

		final List<MediaItem.Subtitle> subs = new Vector<>();
		for (final MediaInfo.Transcript trans : mediaInfo.getTranscript()) {
			for (final MediaInfo.Typographic typo : trans.getTypographic()) {
				subs.add(new MediaItem.Subtitle(Uri.parse(typo.getSrc()), getSubMimetype(typo.getFormat()), trans.getKind()));
			}
		}
		build.setSubtitles(subs);

		final MediaItem item = build.build();

		player.setMediaItem(item);
		player.prepare();
		player.play();
	}

	@Override
	protected void onPause() {
		super.onPause();
		player.pause();
	}

	public static String getSubMimetype(final String apiType) {
		switch (apiType) {
			case "ttml":
				return MimeTypes.APPLICATION_TTML;
			case "vtt":
				return MimeTypes.TEXT_VTT;
		}
		return null;
	}
}