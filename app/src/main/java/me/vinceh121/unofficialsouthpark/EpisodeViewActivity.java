package me.vinceh121.unofficialsouthpark;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

import me.vinceh121.unofficialsouthpark.entities.Episode;
import me.vinceh121.unofficialsouthpark.entities.MediaInfo;

public class EpisodeViewActivity extends AbstractSPActivity {
	private Episode episode;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_episode_view);

		this.episode = (Episode) getIntent().getSerializableExtra("episode");

		final Toolbar toolbar = findViewById(R.id.toolbar);
		toolbar.setTitle(this.episode.getTitle());
		setSupportActionBar(toolbar);

		final TextView textDesc = findViewById(R.id.textEpDesc);
		textDesc.setText(this.episode.getDetails());

		final FloatingActionButton fab = findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View view) {
				final LoadMediaInfoTask task = new LoadMediaInfoTask();
				final List<String> streams = SPManager.getStreams(episode);
				if (streams.size() == 0) {
					Snackbar.make(fab, "This episode doesn't have any streams ;-;", Snackbar.LENGTH_LONG);
					return;
				}
				task.execute(streams.get(0));
			}
		});
	}

	private class LoadMediaInfoTask extends AsyncTask<String, Void, MediaInfo> { // todo make static

		@Override
		protected MediaInfo doInBackground(final String... strings) {
			try {
				final MediaInfo info = SPManager.getInstance().loadMediaInfo(strings[0]);
				return info;
			} catch (final IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(final MediaInfo mediaInfo) {
			final Uri uri = Uri.parse(mediaInfo.getRendition().get(0).getSrc());
			Log.d("EpisodeViewActivity", "URL for " + episode.getTitle() + " : " + uri);
			if (PreferenceManager.getDefaultSharedPreferences(EpisodeViewActivity.this).getBoolean("episode-send", false)) {
				final Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType("application/vnd.apple.mpegurl");
				intent.putExtra(Intent.EXTRA_STREAM, uri);
				final Intent shareIntent = Intent.createChooser(intent, null);
				startActivity(shareIntent);
			} else {
				final Intent intent = new Intent(EpisodeViewActivity.this, PlayerActivity.class);
				intent.putExtra("mediaInfo", mediaInfo);
				startActivity(intent);
			}
		}
	}


}
