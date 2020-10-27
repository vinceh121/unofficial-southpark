package me.vinceh121.unofficialsouthpark;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import me.vinceh121.unofficialsouthpark.entities.Episode;

public class EpisodesActivity extends AppCompatActivity {
	private RecyclerView recycler;
	private RecyclerView.LayoutManager layoutManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_seasons);

		@SuppressWarnings("unchecked")
		final List<Episode> season = (List<Episode>) getIntent().getSerializableExtra("season");

		this.recycler = this.findViewById(R.id.seasonList);
		this.layoutManager = new LinearLayoutManager(this);
		this.recycler.setLayoutManager(this.layoutManager);

		final EpisodeAdapter adapter = new EpisodeAdapter(season);
		this.recycler.setAdapter(adapter);
	}

	private class EpisodeAdapter extends RecyclerView.Adapter<EpisodeAdapter.ViewHolder> {
		private List<Episode> season;

		private class ViewHolder extends RecyclerView.ViewHolder {
			private View v;

			public ViewHolder(final View v) {
				super(v);
				this.v = v;
			}
		}

		public EpisodeAdapter(final List<Episode> season) {
			this.season = season;
		}

		@NonNull
		@Override
		public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
			final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_episode_view, parent, false);
			v.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					final Intent intent = new Intent(EpisodesActivity.this, EpisodeViewActivity.class);
					final Episode s = season.get(recycler.getChildLayoutPosition(v));
					intent.putExtra("episode", s);
					startActivity(intent);
				}
			});

			final ViewHolder vh = new ViewHolder(v);
			return vh;
		}

		@Override
		public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
			final Episode episode = season.get(position);

			final TextView title = holder.v.findViewById(R.id.textTitle);
			final TextView epCount = holder.v.findViewById(R.id.textEpDesc);
			final ImageView img = holder.v.findViewById(R.id.imageEpisode);

            title.setText(episode.getTitle());
            epCount.setText(episode.getDetails());
            img.setImageURI(Uri.parse(episode.getImage()));
        }

		@Override
		public int getItemCount() {
			if (season == null)
				return 0;
			return season.size();
		}
	}
}
