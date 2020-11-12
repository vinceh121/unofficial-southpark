package me.vinceh121.unofficialsouthpark;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import me.vinceh121.unofficialsouthpark.entities.Episode;

public class EpisodesActivity extends AbstractSPActivity {
	private RecyclerView recycler;
	private RecyclerView.LayoutManager layoutManager;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_seasons);

		final int seasonIndex = getIntent().getIntExtra("season", -2);

		final ActionBar toolbar = getSupportActionBar();
		toolbar.setTitle("Season " + (seasonIndex + 1));

		final List<Episode> season = SPManager.getInstance().getData().getSeasons().get(seasonIndex);

		this.recycler = this.findViewById(R.id.seasonList);
		this.layoutManager = new LinearLayoutManager(this);
		this.recycler.setLayoutManager(this.layoutManager);

		final EpisodeAdapter adapter = new EpisodeAdapter(season, this);
		this.recycler.setAdapter(adapter);
	}
}
