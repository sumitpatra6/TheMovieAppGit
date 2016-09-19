package com.apps.sumit.themovieapp;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;

public class SetingActivity extends PreferenceActivity {

    @Nullable
    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {

        addPreferencesFromResource(R.xml.preference_general);
        return super.onCreateView(name, context, attrs);
    }
}
