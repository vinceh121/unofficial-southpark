package me.vinceh121.unofficialsouthpark;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

import me.vinceh121.unofficialsouthpark.entities.Geolocation;
import me.vinceh121.unofficialsouthpark.entities.SPData;

public class MainActivity extends AppCompatActivity {
	private TextView textProgress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		this.textProgress = findViewById(R.id.textProgress);

		final Geolocation geo = Geolocation.valueOf(getSharedPreferences("southparkcontent", MODE_PRIVATE).getString("geolocation", "EN"));

		final LoadingTask task = new LoadingTask();
		task.execute(geo);
	}

	private class LoadingTask extends AsyncTask<Geolocation, String, SPData> {

		@Override
		protected SPData doInBackground(Geolocation... geos) {
			final SPManager manager = new SPManager();
			final Geolocation geo = geos[0];
			try {
				this.publishProgress("Loading geolocation " + geo.name());
				manager.loadData(geo);
				this.publishProgress("Loading succcess!");
				return manager.getData();
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}

		@Override
		protected void onPostExecute(SPData spData) {
			final Intent intent = new Intent(MainActivity.this, SeasonsActivity.class);
			intent.putExtra("seasons", new ArrayList<>(spData.getSeasons()));
			startActivity(intent);
		}

		@Override
		protected void onProgressUpdate(String... values) {
			textProgress.setText(values[0]);
		}
	}
}
