package com.apps.sumit.data;

import android.graphics.Bitmap;

/**
 * Created by Sumit on 9/14/2016.
 */
public class Movies {
    private String original_title;
    private Bitmap mIcon;

    public Movies(String original_title,Bitmap mIcon){
        this.original_title = original_title;
        this.mIcon=mIcon;
    }

    public String getOriginal_title(){
        return this.original_title;

    }

    public Bitmap getmIcon(){
        return this.mIcon;
    }

    @Override
    public String toString() {
        return ("Movie Name-> "+original_title+" Image Url->"+mIcon);

    }
}
