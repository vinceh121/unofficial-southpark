package me.vinceh121.unofficialsouthpark;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ShareCompat;
import androidx.preference.PreferenceManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.vinceh121.unofficialsouthpark.entities.Episode;
import me.vinceh121.unofficialsouthpark.entities.MediaInfo;

public class EpisodeViewActivity extends AbstractSPActivity {
    private Episode episode;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuInflater inf = new MenuInflater(this);
        inf.inflate(R.menu.menu_episode, menu);
        inf.inflate(R.menu.menu_general, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_share_link) {
            this.play(LoadMode.SHARE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episode_view);

        this.episode = (Episode) getIntent().getSerializableExtra("episode");

        final Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(this.episode.getTitle());
        setSupportActionBar(toolbar);

        final TextView textDesc = findViewById(R.id.textEpDesc);
        textDesc.setText(this.episode.getDate() + "\n" + this.episode.getDetails());

        final ImageView image = findViewById(R.id.imageEpisode);
        Picasso.get().load(episode.getImage()).resize(768, 480).onlyScaleDown().into(image);

        final FloatingActionButton fab = findViewById(R.id.fab);

        if (episode.getMediagen().isEmpty()) {
            fab.setImageResource(android.R.drawable.ic_menu_info_details);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (episode.getMediagen().isEmpty()) {
                    Snackbar.make(fab, "Oopsie woopsie fucky wucky, this episode isn't available ;-;", Snackbar.LENGTH_LONG);
                    return;
                }
                play();
            }
        });
    }

    private void play() {
        this.play(
                PreferenceManager.getDefaultSharedPreferences(EpisodeViewActivity.this)
                        .getBoolean("episode-send", false)
                        ? LoadMode.SEND : LoadMode.PLAY);
    }

    private void play(LoadMode loadMode) {
        final LoadMediaInfoTask task = new LoadMediaInfoTask(loadMode);
        final List<String> streams = SPManager.getStreams(episode);
        task.execute(streams.toArray(new String[0]));
    }

    private class LoadMediaInfoTask extends AsyncTask<String, Void, ArrayList<MediaInfo>> {
        private final LoadMode loadMode;

        public LoadMediaInfoTask(LoadMode loadMode) {
            this.loadMode = loadMode;
        }

        @Override
        protected ArrayList<MediaInfo> doInBackground(final String... strings) {
            final ArrayList<MediaInfo> infos = new ArrayList<>();
            for (final String s : strings) {
                try {
                    final MediaInfo info = SPManager.getInstance().loadMediaInfo(s);
                    infos.add(info);
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
            return infos;
        }

        @Override
        protected void onPostExecute(final ArrayList<MediaInfo> mediaInfo) {
            switch (this.loadMode) {
                case SEND:
                    final Uri uri = Uri.parse(mediaInfo.get(0).getRendition().get(0).getSrc());
                    Log.d("EpisodeViewActivity", "URL for " + episode.getTitle() + " : " + uri);
                    final Intent sendIntent = new Intent(Intent.ACTION_SEND);
                    sendIntent.setType("application/vnd.apple.mpegurl");
                    sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
                    final Intent shareIntent = Intent.createChooser(sendIntent, null);
                    startActivity(shareIntent);
                    break;
                case PLAY:
                    final Intent intent = new Intent(EpisodeViewActivity.this, PlayerActivity.class);
                    intent.putExtra("mediaInfo", mediaInfo);
                    startActivity(intent);
                    break;
                case SHARE:
                    StringBuilder sb = new StringBuilder();
                    for (MediaInfo i : mediaInfo) {
                        for (MediaInfo.Rendition r : i.getRendition()) {
                            sb.append(r.getSrc());
                            sb.append("\n");
                        }
                    }
                    ShareCompat.IntentBuilder.from(EpisodeViewActivity.this)
                            .setType("text/plain")
                            .setChooserTitle("Share playlist links")
                            .setText(sb.toString())
                            .startChooser();
                    break;
            }
        }
    }

    private enum LoadMode {
        SEND, PLAY, SHARE;
    }

}
