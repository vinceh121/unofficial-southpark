package me.vinceh121.unofficialsouthpark;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.StyledPlayerControlView;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.android.exoplayer2.util.MimeTypes;

import java.util.ArrayList;
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
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		final StyledPlayerView playerView = findViewById(R.id.player_view);

		playerView.setControllerOnFullScreenModeChangedListener(new StyledPlayerControlView.OnFullScreenModeChangedListener() {
			@Override
			public void onFullScreenModeChanged(final boolean isFullScreen) {
				if (isFullScreen) {
					playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE);
				} else {
					playerView.setSystemUiVisibility(0);
				}
			}
		});

		player = new SimpleExoPlayer.Builder(this).build();
		playerView.setPlayer(player);

		final ArrayList<MediaInfo> infos = (ArrayList<MediaInfo>) getIntent().getSerializableExtra("mediaInfo");

		for (final MediaInfo mediaInfo : infos) {
			final Uri uri = Uri.parse(mediaInfo.getRendition().get(0).getSrc());
			final MediaItem.Builder build = new MediaItem.Builder()
					.setMimeType(MimeTypes.APPLICATION_M3U8)
					.setUri(uri);

			if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("subtitles", true)) {
				final List<MediaItem.SubtitleConfiguration> subs = new Vector<>();
				for (final MediaInfo.Transcript trans : mediaInfo.getTranscript()) {
					for (final MediaInfo.Typographic typo : trans.getTypographic()) {
						subs.add(new MediaItem.SubtitleConfiguration.Builder(Uri.parse(typo.getSrc()))
								.setSelectionFlags(C.SELECTION_FLAG_FORCED)
								.setMimeType(getSubMimetype(typo.getFormat()))
								.build());
					}
				}
				build.setSubtitleConfigurations(subs);
			}

			final MediaItem item = build.build();

			player.addMediaItem(item);
		}

		player.prepare();
		player.play();
	}

	@Override
	protected void onPause() {
		super.onPause();
		player.pause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		player.release();
	}

	public static String getSubMimetype(final String apiType) {
		switch (apiType) {
			case "ttml":
				return MimeTypes.APPLICATION_TTML;
			case "vtt":
				return MimeTypes.TEXT_VTT;
			case "cea-608":
				return MimeTypes.APPLICATION_CEA608;
		}
		return null;
	}
}