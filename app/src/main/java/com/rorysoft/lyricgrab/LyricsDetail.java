package com.rorysoft.lyricgrab;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;


public class LyricsDetail extends AppCompatActivity {

    private Lyric lyric;
    private String lyrics;
    private TextView lyricView;
    private String url;
    private String artistName;
    private String albumName;
    private String songTitle;
    private String albumArtPath;
    private TextView artistView;
    private TextView albumView;
    private TextView songView;
    private TextView artistLabel;
    private TextView albumLabel;
    private TextView songLabel;
    private ImageView albumArt;
    private ImageView border;
    private ImageView youtubeButton;
    private ImageView facebookButton;
    private ImageView twitterButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lyrics_detail);
        lyricView = (TextView) findViewById(R.id.lyricView);
        artistView = (TextView) findViewById(R.id.artist_name);
        albumView = (TextView) findViewById(R.id.album_name);
        songView = (TextView) findViewById(R.id.song_name);
        artistLabel = (TextView) findViewById(R.id.artist_label);
        albumLabel = (TextView) findViewById(R.id.album_label);
        songLabel = (TextView) findViewById(R.id.song_label);
        albumArt = (ImageView) findViewById(R.id.album_art);
        border = (ImageView) findViewById(R.id.border);
        youtubeButton = (ImageView) findViewById(R.id.youtube);
        facebookButton = (ImageView) findViewById(R.id.facebook);
        twitterButton = (ImageView) findViewById(R.id.twitter);


        Intent intent = getIntent();
        lyric = intent.getParcelableExtra(getString(R.string.lyric));
        url = lyric.getLyricsPath();
        albumName = lyric.getAlbumName();
        artistName = lyric.getArtistName();
        songTitle = lyric.getSongTitle();
        albumArtPath = lyric.getImagePath();
        downloadLyrics downloadLyrics = new downloadLyrics();
        downloadLyrics.execute(url);
        setVisibility();
        initButtons();
    }

    public void initButtons() {
        youtubeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.youtube_url) + songTitle));
                startActivity(intent);
            }
        });

        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.facebook_url) + artistName + getString(R.string.facebook_url2) +
                        getString(R.string.facebook_url3)));
                startActivity(intent);
            }
        });

        twitterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.twitter_url) + artistName + getString(R.string.twitter_url2)));
                startActivity(intent);
            }
        });
    }

    public void setVisibility() {
        border.setVisibility(View.GONE);
        artistLabel.setVisibility(View.GONE);
        songLabel.setVisibility(View.GONE);
        albumLabel.setVisibility(View.GONE);
        youtubeButton.setVisibility(View.GONE);
        facebookButton.setVisibility(View.GONE);
        twitterButton.setVisibility(View.GONE);
    }

    private void setDetails() {
        artistView.setText(artistName);
        songView.setText(songTitle);
        albumView.setText(albumName);
        Picasso.with(LyricsDetail.this)
                .load(albumArtPath)
                .into(albumArt);
        Log.d("LyricsDetail", " Here is image path: " + albumArtPath);
    }

    private void setLyrics(String result) {
        this.lyrics = result;
        lyricView.setText(lyrics);
        Log.d("LyricsDetail", " Here is dump: " + lyrics);

    }

    private class downloadLyrics extends AsyncTask<String, Void, String> {
        private String lyrics;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            setLyrics(result);
            setDetails();
            border.setVisibility(View.VISIBLE);
            artistLabel.setVisibility(View.VISIBLE);
            songLabel.setVisibility(View.VISIBLE);
            albumLabel.setVisibility(View.VISIBLE);
            youtubeButton.setVisibility(View.VISIBLE);
            facebookButton.setVisibility(View.VISIBLE);
            twitterButton.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                Document document = Jsoup.connect(params[0]).get();
                Elements songLyricsOuter = document.select("div [id = songLyricsDiv-outer]");

                String html = songLyricsOuter.html();
                String replaceAll = html.replaceAll("(?i)<br[^>]*>", "<pre>\n</pre>");
                lyrics = " " + Jsoup.parse(replaceAll).text();

                return lyrics;
            } catch (IOException e) {
                Log.d("LyricDetail", " The exception encountered is: " + e);
            }
            return null;
        }
    }
}
