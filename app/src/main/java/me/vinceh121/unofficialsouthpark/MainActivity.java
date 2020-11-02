package me.vinceh121.unofficialsouthpark;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

import me.vinceh121.unofficialsouthpark.entities.Geolocation;
import me.vinceh121.unofficialsouthpark.entities.SPData;

public class MainActivity extends AbstractSPActivity {
	private TextView textProgress;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		this.textProgress = findViewById(R.id.textProgress);

		final Geolocation geo = Geolocation.valueOf(PreferenceManager.getDefaultSharedPreferences(this).getString("geolocation", "EN"));

		final LoadingTask task = new LoadingTask();
		task.execute(geo);
	}

	private class LoadingTask extends AsyncTask<Geolocation, String, SPData> {

		@Override
		protected SPData doInBackground(final Geolocation... geos) {
			final SPManager manager = SPManager.getInstance();
			final Geolocation geo = geos[0];
			try {
				this.publishProgress("Loading geolocation " + geo.name());
				manager.loadData(geo);
				this.publishProgress("Loading succcess!");
				return manager.getData();
			} catch (final IOException e) {
				e.printStackTrace();
				return null;
			}
		}

		@Override
		protected void onPostExecute(final SPData spData) {
			final Intent intent = new Intent(MainActivity.this, SeasonsActivity.class);
			startActivity(intent);
		}

		@Override
		protected void onProgressUpdate(final String... values) {
			textProgress.setText(values[0]);
		}
	}
}
