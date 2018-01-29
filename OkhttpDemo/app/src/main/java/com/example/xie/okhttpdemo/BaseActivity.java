package com.example.xie.okhttpdemo;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getLayout()!=0){
            setContentView(getLayout());
            ButterKnife.bind(this);
        }

        onAcCreate();
    }

    protected abstract int getLayout();

    public abstract void onAcCreate();


}
