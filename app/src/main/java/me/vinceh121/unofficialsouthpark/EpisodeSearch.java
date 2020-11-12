package me.vinceh121.unofficialsouthpark;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import me.vinceh121.unofficialsouthpark.entities.Episode;

public class EpisodeSearch extends AppCompatActivity {
	private String search = null;
	private RecyclerView recycler;
	private RecyclerView.LayoutManager layoutManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_episode_search);
		final Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		this.recycler = this.findViewById(R.id.episodeSearchList);
		this.layoutManager = new LinearLayoutManager(this);
		this.recycler.setLayoutManager(this.layoutManager);

		this.updateAdapter();
	}

	@Override
	public boolean onOptionsItemSelected(@NonNull final MenuItem item) {
		if (item.getItemId() == R.id.action_search) return true;
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		getMenuInflater().inflate(R.menu.menu_search, menu);
		final View searchView = menu.findItem(R.id.action_search).getActionView();
		if (searchView instanceof SearchView) {
			prepareSearchView((SearchView) searchView);
		}
		return true;
	}

	private void prepareSearchView(final SearchView view) {
		view.setOnCloseListener(new SearchView.OnCloseListener() {
			@Override
			public boolean onClose() {
				EpisodeSearch.this.search = null;
				return false;
			}
		});
		view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(final String query) {
				return false;
			}

			@Override
			public boolean onQueryTextChange(final String newText) {
				EpisodeSearch.this.search = newText;
				updateAdapter();
				return false;
			}
		});
	}

	private void updateAdapter() {
		final List<Episode> episodeList = new ArrayList<>();
		if (search != null) {
			for (final Episode e : SPManager.getInstance().getData().getAllEpisodes()) {
				if (e.toSearchableString().toLowerCase().contains(this.search)) {
					episodeList.add(e);
				}
			}
		} else {
			episodeList.addAll(SPManager.getInstance().getData().getAllEpisodes());
		}

		final EpisodeAdapter adapter = new EpisodeAdapter(episodeList, this);
		this.recycler.setAdapter(adapter);
	}
}
