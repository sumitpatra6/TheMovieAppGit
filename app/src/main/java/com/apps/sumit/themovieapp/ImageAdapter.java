package com.apps.sumit.themovieapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apps.sumit.data.Movies;
import com.apps.sumit.utility.StringResources;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by Sumit on 9/9/2016.
 */
public class ImageAdapter extends BaseAdapter {

    Context c;
    List<Movies> moviesList;
     Bitmap mIcon;
    public ImageAdapter(Context c){
        this.c = c;
    }

    public ImageAdapter(Context c, List<Movies> moviesList){
        this.c =c;
        this.moviesList = moviesList;

    }
    @Override
    public int getCount() {
        return moviesList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.v("The Movie App","View method called! "+position);
        View grid;
        LayoutInflater inflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView == null){
            Movies movies = moviesList.get(position);
            grid = new View(c);
            grid = inflater.inflate(R.layout.grid_single,null);
            TextView textView = (TextView)grid.findViewById(R.id.grid_single_text);
            ImageView imageView = (ImageView)grid.findViewById(R.id.grid_single_image);

            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setPadding(8,8,8,8);
            textView.setText(movies.getOriginal_title());
            imageView.setImageBitmap(movies.getmIcon());
        }else{
            grid = (View)convertView;
        }

        return grid;
    }

    private Integer mThumbIds[] = {R.drawable.sample_0,R.drawable.sample_1,R.drawable.sample_2,R.drawable.sample_3,
                                    R.drawable.sample_4,R.drawable.sample_5,R.drawable.sample_6,R.drawable.sample_7};




}
