package com.example.xie.okhttpdemo;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

/**
 * Created by xie on 2018/1/26.
 */

public class MyApplication extends Application {
    private static final String TAG = "MyApplication";

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("application","=======onCr");
        context = getApplicationContext();


    }



    public static Context getContext(){
        return context;
    }

}
