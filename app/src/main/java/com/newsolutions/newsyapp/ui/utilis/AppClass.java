package com.newsolutions.newsyapp.ui.utilis;

import android.app.Application;
import android.content.Context;
import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class AppClass extends Application {
    public static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
     //   context=getApplicationContext();
    }

}
