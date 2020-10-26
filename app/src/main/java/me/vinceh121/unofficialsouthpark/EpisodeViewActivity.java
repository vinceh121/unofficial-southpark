package me.vinceh121.unofficialsouthpark;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.TextView;

import me.vinceh121.unofficialsouthpark.entities.Episode;

public class EpisodeViewActivity extends AppCompatActivity {
	private Episode episode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_episode_view);

		this.episode = (Episode) getIntent().getSerializableExtra("episode");

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setTitle(this.episode.getTitle());
		setSupportActionBar(toolbar);

		final TextView textDesc = findViewById(R.id.textEpDesc);
		textDesc.setText(this.episode.getDetails());

		final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				final Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse(SPManager.getStreams(episode).get(0)));
				startActivity(intent);
			}
		});
	}
}
