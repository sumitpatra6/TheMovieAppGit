package com.apps.sumit.themovieapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.apps.sumit.data.MovieDetails;
import com.apps.sumit.utility.StringResources;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MovieDetails_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details_);
        update();
    }

    protected void update(){
        Intent intent = getIntent();
        String movieName = intent.getStringExtra("EXTRA_MESSAGE");
        System.out.println("Move Name"+movieName);
        new FetchMovieDetails().execute(movieName);
    }
    private class FetchMovieDetails extends AsyncTask<String, Void, MovieDetails>{

        protected Bitmap bitmapCreator(String uri){
            Uri urii = Uri.parse(StringResources.imageBaseUrl+uri);
            Bitmap mIcon = null;
            try{
                if(uri!=null) {
                    URL url = new URL(urii.toString());
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.connect();
                    InputStream in = httpURLConnection.getInputStream();
                    mIcon = BitmapFactory.decodeStream(in);
                }
            }catch(Exception e){
                System.out.println(e.toString());
            }
            return mIcon;
        }
        protected MovieDetails movieMaker(StringBuffer buffer){
            MovieDetails movieDetails= null;
            String original_title;
            String release_date;
            String voteAverage;
            String synopsis;
            Bitmap mIcon;
            try {
                JSONObject object = new JSONObject(String.valueOf(buffer));
                JSONArray array = object.getJSONArray("results");
                JSONObject result = (JSONObject)(array.get(0));
                original_title = result.getString("original_title");
                release_date = result.getString("release_date");
                voteAverage = result.getString("vote_average");
                synopsis = result.getString("overview");
                mIcon = bitmapCreator(result.getString("poster_path"));
                movieDetails = new MovieDetails(original_title, release_date,voteAverage,synopsis,mIcon);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return movieDetails;
        }
        @Override
        protected MovieDetails doInBackground(String... params) {
            System.out.println("Execute called!");
            MovieDetails movieDetails = null;
            BufferedReader reader;
            InputStream inputStream;
            if(params.length ==0){
                System.out.println("No Value Passed!");
            }
            String movieName = params[0];
            System.out.println(movieName);
            try {
                Uri uri= Uri.parse(StringResources.movieFetchUrl).buildUpon().
                    appendQueryParameter("query",movieName).build();


                URL url = new URL(uri.toString());
                HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.connect();
                inputStream = urlConnection.getInputStream();
                if(inputStream == null){
                    Log.e(StringResources.INFO_LOG,"No data received");
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer buffer = new StringBuffer();
                String line;
                while((line = reader.readLine())!=null){
                    buffer.append(line+"\n");
                }
                System.out.println(buffer);
                movieDetails = movieMaker(buffer);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return movieDetails;
        }

        @Override
        protected void onPostExecute(MovieDetails movieDetails) {
            super.onPostExecute(movieDetails);
            ImageView imageView = (ImageView)findViewById(R.id.details_poster);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setImageBitmap(movieDetails.getmIcon());
            TextView textView = (TextView)findViewById(R.id.details_title);
            textView.setText(movieDetails.getMovieName());
            TextView releaseDate = (TextView)findViewById(R.id.details_release_date);
            releaseDate.setText("Release Date:"+movieDetails.getReleaseDate());
            TextView voteAverage = (TextView)findViewById(R.id.details_vote_average);
            voteAverage.setText("Vote Average:"+movieDetails.getVoteAverage());
            TextView synopsis = (TextView)findViewById(R.id.details_synosys);
            synopsis.setText(movieDetails.getSynopsys());
        }
    }
}
