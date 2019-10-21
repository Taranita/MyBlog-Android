package com.taranita.myblog;

import android.app.Application;
import android.content.Context;

public class MyBlogApplication extends Application {
    private static MyBlogApplication sInstance;

        @Override
    public void onCreate(){
        super.onCreate();
        sInstance = this;
    }

    public static MyBlogApplication getInstance(){
            return sInstance;
    }

    public static Context getAppContext(){
            return sInstance.getApplicationContext();
    }
}
