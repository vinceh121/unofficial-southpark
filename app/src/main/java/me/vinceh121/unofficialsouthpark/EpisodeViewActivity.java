package me.vinceh121.unofficialsouthpark;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.TextView;

import java.io.IOException;

import me.vinceh121.unofficialsouthpark.entities.Episode;
import me.vinceh121.unofficialsouthpark.entities.MediaInfo;

public class EpisodeViewActivity extends AppCompatActivity {
	private SPManager manager;
	private Episode episode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_episode_view);

		this.manager = new SPManager();
		this.episode = (Episode) getIntent().getSerializableExtra("episode");

		final Toolbar toolbar = findViewById(R.id.toolbar);
		toolbar.setTitle(this.episode.getTitle());
		setSupportActionBar(toolbar);

		final TextView textDesc = findViewById(R.id.textEpDesc);
		textDesc.setText(this.episode.getDetails());

		final FloatingActionButton fab = findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//final AlertDialog dialog = new AlertDialog.Builder(getBaseContext()).setMessage("Loading episode...").create();
				//dialog.show();
				final LoadMediaInfoTask task = new LoadMediaInfoTask();
				task.execute(SPManager.getStreams(episode).get(0));
			}
		});
	}

	private class LoadMediaInfoTask extends AsyncTask<String, Void, MediaInfo> {

		@Override
		protected MediaInfo doInBackground(String... strings) {
			try {
				final MediaInfo info = manager.loadMediaInfo(strings[0]);
				return info;
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(MediaInfo mediaInfo) {
			final Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("application/vnd.apple.mpegurl");
			intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(mediaInfo.getRendition().get(0).getSrc()));
			final Intent shareIntent = Intent.createChooser(intent, null);
			startActivity(shareIntent);
		}
	}
}
