package com.rorysoft.lyricgrab;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rorysoft.lyricgrab.Adapter.LyricAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText search;
    private Button searchButton;
    private Lyric lyric = null;
    private ArrayList<Lyric> songList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private LyricAdapter lyricAdapter;
    private Button nextButton;
    private Button prevButton;
    private Elements navigationNext;
    private Element hrefNext;
    private String nextLink;
    private Elements navigationPrev;
    private Element hrefPrev;
    private String prevLink;
    private String searchBox;
    private String searchBoxLast;
    private String baseUrl;
    private int pageNumber = 1;
    private boolean haveResults = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        search = (EditText) findViewById(R.id.search);
        searchButton = (Button) findViewById(R.id.searchButton);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        lyricAdapter = new LyricAdapter(MainActivity.this, songList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(lyricAdapter);

        nextButton = (Button) findViewById(R.id.next_button);
        prevButton = (Button) findViewById(R.id.prev_button);
        baseUrl = "http://www.songlyrics.com";

        initButtons();
        checkPrevVisibility();
        checkNextVisibility();
        search();
    }

    private void search() {

        searchButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                searchBox = search.getText().toString();
                String url = getString(R.string.song_lyrics_url) + searchBox;
                startDownload(url);
                searchButton.setEnabled(false);

            }
        });
    }

    private void setLyricAdapter() {
        lyricAdapter = new LyricAdapter(MainActivity.this, songList);
        mRecyclerView.setAdapter(lyricAdapter);
    }

    private void startDownload(String url) {
        DownloadData downloadData = new DownloadData();
        downloadData.execute(url);
    }


    private void initButtons() {
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = baseUrl + nextLink;
                startDownload(url);
                pageNumber++;
                Log.d("LyricsActivity", "Next Link: " + url);
                checkPrevVisibility();
                nextButton.setEnabled(false);

            }
        });

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = baseUrl + prevLink;
                startDownload(url);
                pageNumber--;
                Log.d("LyricsActivity", "Prev Link: " + url);
                checkPrevVisibility();
                prevButton.setEnabled(false);

            }
        });
    }

    private void checkPrevVisibility() {
        if (pageNumber == 1) {
            prevButton.setVisibility(View.GONE);
        } else if (pageNumber > 1) {
            prevButton.setVisibility(View.VISIBLE);
        }
    }

    private void checkNextVisibility() {
        if (!haveResults) {
            nextButton.setVisibility(View.GONE);
        } else {
            nextButton.setVisibility(View.VISIBLE);
        }
    }

    private void showToast() {

        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), R.string.toast_no_match_message, Toast.LENGTH_LONG).show();
            }
        });
    }


    private class DownloadData extends AsyncTask<String, Void, ArrayList<Lyric>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<Lyric> songList) {
            super.onPostExecute(songList);
            if(!searchBox.equals(searchBoxLast)) {
                pageNumber = 1;
                checkPrevVisibility();
            }
            checkNextVisibility();
            nextButton.setEnabled(true);
            prevButton.setEnabled(true);
            searchButton.setEnabled(true);
            searchBoxLast = searchBox;
            setLyricAdapter();
        }

        @Override
        protected ArrayList<Lyric> doInBackground(String... params) {

            try {
                Document document = Jsoup.connect(params[0]).get();
                Elements results = document.select("div[class=serpresult]");
                navigationNext = document.select("li[class=li_pagination next]");
                hrefNext = navigationNext.select("a[href]").first();
                nextLink = hrefNext.attr("href");
                songList.clear();

                Log.d("DownloadData", "page: " + pageNumber + " " + searchBox + searchBoxLast);


                if (pageNumber > 1 && (searchBox.equals(searchBoxLast))) {
                    navigationPrev = document.select("li[class=li_pagination previous]");
                    hrefPrev = navigationPrev.select("a[href]").first();
                    prevLink = hrefPrev.attr("href");
                }


                for (Element e : results) {

                    Elements href = e.select("a[href]");
                    lyric = new Lyric();
                    String lyricsLink = href.first().attr("abs:href");
                    lyric.setLyricsPath(lyricsLink);
                    Element imageSource = href.select("[class=imgserp]").first();
                    String url = imageSource.absUrl("src");
                    lyric.setImagePath(url);
                    Elements h3 = e.select("h3");
                    Elements links = h3.select("a[href]");
                    String songTitle = links.attr("title");
                    lyric.setSongTitle(songTitle);
                    Elements results2 = e.select("div[class=serpdesc-2]");
                    Elements p = results2.select("p");
                    Elements href2 = p.select("a[href]");
                    String artistName = href2.first().text();
                    lyric.setArtistName(artistName);
                    String albumName = href2.last().text();
                    lyric.setAlbumName(albumName);

                    songList.add(lyric);

                    Log.d("DownloadData", "Song name: " + lyric.getSongTitle());
                    Log.d("DownloadData", "Artist name: " + lyric.getArtistName());
                    Log.d("DownloadData", "Album name: " + lyric.getAlbumName());
                    Log.d("DownloadData", "Album Art Source: " + lyric.getImagePath());
                    Log.d("DownloadData", "Lyrics Source: " + lyric.getLyricsPath());
                    Log.d("DownloadData", "**************************");

                }
                haveResults = true;
                return songList;

            } catch (IOException e) {
                Log.d("DownloadData", "The exception reported is: " + e);
            } catch (NullPointerException e) {
                showToast();
            }
            return null;
        }
    }

}

