package me.vinceh121.unofficialsouthpark;

import android.content.Intent;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public abstract class AbstractSPActivity extends AppCompatActivity {
	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		final MenuInflater inf = new MenuInflater(this);
		inf.inflate(R.menu.menu_general, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(@NonNull final MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_settings:
				startActivity(new Intent(this, SettingsActivity.class));
				return true;
			case R.id.app_bar_search:
				startActivity(new Intent(this, EpisodeSearch.class));
				return true;
		}
		return false;
	}
}
