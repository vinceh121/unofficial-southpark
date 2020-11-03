package me.vinceh121.unofficialsouthpark;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
					Snackbar.make(fab, "Oopsie woopsie fucky wucky, this episode isn't available ;-;", Snackbar.LENGTH_LONG);
					return;
				}
				task.execute(streams.toArray(new String[0]));
			}
		});
	}

	private class LoadMediaInfoTask extends AsyncTask<String, Void, ArrayList<MediaInfo>> { // todo make static

		@Override
		protected ArrayList<MediaInfo> doInBackground(final String... strings) {
			final ArrayList<MediaInfo> infos = new ArrayList<>();
			for (final String s : strings) {
				try {
					final MediaInfo info = SPManager.getInstance().loadMediaInfo(s);
					infos.add(info);
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}
			return infos;
		}

		@Override
		protected void onPostExecute(final ArrayList<MediaInfo> mediaInfo) {
			if (PreferenceManager.getDefaultSharedPreferences(EpisodeViewActivity.this).getBoolean("episode-send", false)) {
				final Uri uri = Uri.parse(mediaInfo.get(0).getRendition().get(0).getSrc());
				Log.d("EpisodeViewActivity", "URL for " + episode.getTitle() + " : " + uri);
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
