package me.vinceh121.unofficialsouthpark;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import me.vinceh121.unofficialsouthpark.entities.Episode;

public class EpisodeAdapter extends RecyclerView.Adapter<EpisodeAdapter.ViewHolder> { // this needs to be moved somewhere else to be more pretty
	private final List<Episode> season;
	private final Context ctx;

	public class ViewHolder extends RecyclerView.ViewHolder {
		private final View v;

		public ViewHolder(final View v) {
			super(v);
			this.v = v;
		}
	}

	public EpisodeAdapter(final List<Episode> season, final Context ctx) {
		this.season = season;
		this.ctx = ctx;
	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
		final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_episode_view, parent, false);
		v.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				final Intent intent = new Intent(ctx, EpisodeViewActivity.class);
				final Episode s = season.get(((RecyclerView) parent).getChildLayoutPosition(v));
				intent.putExtra("episode", s);
				ctx.startActivity(intent);
			}
		});

		final ViewHolder vh = new ViewHolder(v);
		return vh;
	}

	@Override
	public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
		final Episode episode = season.get(position);

		final TextView title = holder.v.findViewById(R.id.textTitle);
		final TextView epCount = holder.v.findViewById(R.id.textEpDesc);
		final ImageView img = holder.v.findViewById(R.id.imageEpisode);

		title.setText(episode.getTitle() + (episode.getMediagen().size() <= 0 ? " [UNAVAILABLE]" : ""));
		epCount.setText(episode.getDetails());
		Picasso.get().load(episode.getImage()).resize(768, 480).onlyScaleDown().into(img);
	}

	@Override
	public int getItemCount() {
		if (season == null)
			return 0;
		return season.size();
	}
}
