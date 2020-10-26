package me.vinceh121.unofficialsouthpark;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.vinceh121.unofficialsouthpark.entities.Episode;

public class SeasonsActivity extends AppCompatActivity {
	private List<List<Episode>> seasons;
	private RecyclerView recycler;
	private RecyclerView.LayoutManager layoutManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_seasons);

		this.seasons = (List<List<Episode>>) getIntent().getSerializableExtra("seasons");

		this.recycler = this.findViewById(R.id.seasonList);
		this.layoutManager = new LinearLayoutManager(this);
		this.recycler.setLayoutManager(this.layoutManager);

		final SeasonAdapter adapter = new SeasonAdapter(seasons);
		this.recycler.setAdapter(adapter);
	}

	private class SeasonAdapter extends RecyclerView.Adapter<SeasonAdapter.ViewHolder> {
		private List<List<Episode>> seasonList;

		private class ViewHolder extends RecyclerView.ViewHolder {
			private View v;

			public ViewHolder(final View v) {
				super(v);
				this.v = v;
			}
		}

		public SeasonAdapter(final List<List<Episode>> seasonList) {
			this.seasonList = seasonList;
		}

		@NonNull
		@Override
		public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
			final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_season_view, parent, false);
			v.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					final Intent intent = new Intent(SeasonsActivity.this, EpisodesActivity.class);
					final List<Episode> s = seasonList.get(recycler.getChildLayoutPosition(v));
					intent.putExtra("season", new ArrayList<>(s));
					startActivity(intent);
				}
			});

			final ViewHolder vh = new ViewHolder(v);
			return vh;
		}

		@Override
		public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
			final List<Episode> season = seasonList.get(position);

			final TextView title = holder.v.findViewById(R.id.textTitle);
			final TextView epCount = holder.v.findViewById(R.id.textEpCount);
			final ImageView img = holder.v.findViewById(R.id.imageSeason);

			title.setText("Season " + (position + 1));
			epCount.setText(Integer.toString(season.size()));
			img.setImageURI(Uri.parse(SPManager.getSeasonImage(position + 1)));
		}

		@Override
		public int getItemCount() {
			if (seasonList == null)
				return 0;
			return seasonList.size();
		}
	}
}
