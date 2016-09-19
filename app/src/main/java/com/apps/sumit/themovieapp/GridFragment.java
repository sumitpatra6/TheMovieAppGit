package com.apps.sumit.themovieapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.sumit.data.Movies;
import com.apps.sumit.utility.StringResources;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sumit on 9/8/2016.
 */
public class GridFragment extends Fragment {

    GridView gridView;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
       update();

    }


    protected void update(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String choice_key = sharedPreferences.getString(getString(R.string.user_choice_key),getString(R.string.user_choice_default));
        Log.v(StringResources.INFO_LOG,choice_key);
        new FetchMovieDetailsTask().execute(choice_key,StringResources.api__key);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.v("The Movie App:","OncreateViewApp called");
       View rootView = inflater.inflate(R.layout.grid_layout,container,false);
        gridView = (GridView)rootView.findViewById(R.id.gridView);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView)view.findViewById(R.id.grid_single_text);
                Intent intent = new Intent(getActivity(),MovieDetails_Activity.class);
                intent.putExtra("EXTRA_MESSAGE", String.valueOf(textView.getText()));
                startActivity(intent);

            }
        });
        return rootView;
    }

    public class FetchMovieDetailsTask extends AsyncTask<String,Void,List<Movies>>{

        protected List<Movies> getImageList(StringBuffer buffer) throws JSONException {
            JSONObject jsonObject = new JSONObject(String.valueOf(buffer));
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            List<Movies> moviesList = new ArrayList<>();
            for(int i=0;i<jsonArray.length();i++){
                JSONObject object = (JSONObject) jsonArray.get(i);
                String originlTitle = (String) object.getString("original_title");
                String imageUrl = (String) object.getString("poster_path");
                Bitmap mIcon = getBitmapImage(imageUrl);
                if(mIcon !=null)
                     moviesList.add(new Movies(originlTitle,mIcon));
            }

            return moviesList;
        }
        protected Bitmap getBitmapImage(String imageUrl){
            System.out.println("BitMap Image created! "+imageUrl);
            Uri uri = Uri.parse(StringResources.imageBaseUrl+imageUrl);
            Bitmap mIcon = null;
            try{
              if(imageUrl!=null) {
                  URL url = new URL(uri.toString());
                  HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                  httpURLConnection.connect();
                  InputStream in = httpURLConnection.getInputStream();
                  mIcon = BitmapFactory.decodeStream(in);
              }
            }catch(Exception e){
               System.out.println(e.toString());
            }
            return mIcon;
        }
        @Override
        protected List<Movies> doInBackground(String... params) {
             List<Movies> moviesList = new ArrayList<>();
            System.out.println(StringResources.INFO_LOG+" "+"execute called!");
            InputStream inputStream;
            BufferedReader reader;
            if(params.length==0){
                Log.e(StringResources.ERROR_LOG,"No parameteres Passed!");
                return null;
            }
            String sortOrder=params[0];
            String apiKey=params[1];
            try {
            Uri.Builder uri = Uri.parse(StringResources.baseUrl).buildUpon()
                    .appendQueryParameter("sort_by",sortOrder)
                    .appendQueryParameter("api_key",apiKey);

                URL url = new URL(uri.toString());
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();
                inputStream = urlConnection.getInputStream();
                Log.v(StringResources.INFO_LOG,"Connections opened!");
                if(inputStream == null){
                    Log.e(StringResources.ERROR_LOG,"No data received!");
                    return null;
                }
                Log.v(StringResources.INFO_LOG,"input stream received!");
                reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer buffer = new StringBuffer();
                String line;
                while((line = reader.readLine())!=null){
                    buffer.append(line+"\n");
                }
                Log.v(StringResources.INFO_LOG, String.valueOf(buffer));
                System.out.println(buffer);
                moviesList = getImageList(buffer);
                for(int i=0;i<moviesList.size();i++){
                    System.out.println(moviesList.get(i));
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }catch(Exception e){
                Log.v(StringResources.ERROR_LOG,e.toString());
            }
            return moviesList;
        }

        @Override
        protected void onPostExecute(List<Movies> moviesList) {
            super.onPostExecute(moviesList);
            gridView.setAdapter(new ImageAdapter(getActivity(),moviesList));
        }
    }
}
