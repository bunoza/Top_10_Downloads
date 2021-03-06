package com.bunoza.top10downloads;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ItemClickListener {

    private static final String TAG = "MainActivity";
    private List<FeedEntry> applications;
    private RecyclerView recycler;
    private RecyclerAdapter adapter;
    private Spinner spinner;
    private String source;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recycler = findViewById(R.id.recyclerView);
        spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                source = getResources().getStringArray(R.array.spinner_array_rss)[spinner.getSelectedItemPosition()];
                Log.d(TAG, "onCreate: starting AsyncTask");
                DownloadData downloadData = new DownloadData();

                downloadData.execute(source);
                Log.d(TAG, "onCreate: done");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        source = getResources().getStringArray(R.array.spinner_array_rss)[spinner.getSelectedItemPosition()];
        applications = new ArrayList<>();

        Log.d(TAG, "onCreate: starting AsyncTask");
        DownloadData downloadData = new DownloadData();

        downloadData.execute(source);
        Log.d(TAG, "onCreate: done");

    }

    private void setupRecycler(){
        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerAdapter(this);
        recycler.setAdapter(adapter);
    }

    private void setupRecyclerData(){
        adapter.setApplications(applications);
    }

    @Override
    public void onItemClick(int position) {
//        try {
            String query = applications.get(position).getName();
            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
            intent.putExtra(SearchManager.QUERY, query); // query contains search string
            startActivity(intent);

//        }catch (UnsupportedEncodingException e){
//
//        }
    }

    private class DownloadData extends AsyncTask<String, Void, String>{

        private static final String TAG = "DownloadData";

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(TAG, "onPostExecute: parameter is " + s);
            ParseApplications parseApplications = new ParseApplications();
            parseApplications.parse(s);
            applications = parseApplications.getApplications();
            setupRecycler();
            setupRecyclerData();
        }

        @Override
        protected String doInBackground(String... strings) {
            Log.d(TAG, "doInBackground: starts with " + strings[0]);
            String rssFeed = downloadXML(strings[0]);
            if(rssFeed == null){
                Log.e(TAG, "doInBackground: Error downloading");
            }
            return rssFeed;
        }

        private String downloadXML(String urlpath){
            StringBuilder xmlResult = new StringBuilder();
            try{
                URL url = new URL(urlpath);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                int response = connection.getResponseCode();
                Log.d(TAG, "downloadXML: The response code was" + response);
//                InputStream inputStream = connection.getInputStream();
//                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//                BufferedReader reader = new BufferedReader(inputStreamReader);
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                int charsRead;
                char[] inputBuffer = new char[500];
                while(true){
                    charsRead = reader.read(inputBuffer);
                    if(charsRead < 0){
                        break;
                    }
                    if(charsRead >0){
                        xmlResult.append(String.copyValueOf(inputBuffer,0,charsRead));
                    }
                }
                reader.close();

                return xmlResult.toString();
            }catch (MalformedURLException e){
                Log.e(TAG, "downloadXML: Invalid URL" + e.getMessage());
            }catch (IOException e){
                Log.e(TAG, "downloadXML: IO exception reading data" + e.getMessage());
            }catch (SecurityException e){
                Log.e(TAG, "downloadXML: Security exception. Needs permission? " + e.getMessage());
                e.printStackTrace();
            }
            return null;

        }


    }

}