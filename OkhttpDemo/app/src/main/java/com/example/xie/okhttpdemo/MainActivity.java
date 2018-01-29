package com.example.xie.okhttpdemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.xie.okhttpdemo.adapter.BtnAdapter;
import com.example.xie.okhttpdemo.eventbus.EventBusActivity;
import com.example.xie.okhttpdemo.music.MusicActivity;
import com.example.xie.okhttpdemo.phone.PhoneActivity;
import com.example.xie.okhttpdemo.refresh.RefreshActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private static final String TAG = "MainActivity";

    //动态申请权限所需
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE};

    @BindView(R.id.main_list)
    ListView listView;

    String[] name = {"刷新","EventBus","仿微信照片排列","音乐播放器"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        verifyStoragePermissions();
        listView.setAdapter(new BtnAdapter(this,name));
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Log.e(TAG, "onItemClick: "+i );
        switch (i){
            case 0:
                //刷新用例
                startActivity(new Intent(MainActivity.this, RefreshActivity.class));
                break;
            case 1:
                //EventBus练习
                startActivity(new Intent(MainActivity.this, EventBusActivity.class));
                break;
            case 2:
                //防微信拍照排列
                startActivity(new Intent(MainActivity.this, PhoneActivity.class));
                break;
            case 3:
                //通知栏音乐播放器
                startActivity(new Intent(MainActivity.this, MusicActivity.class));
                break;

        }
    }

    private void verifyStoragePermissions() {
        try {
            int permission = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE);
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(MainActivity.this,PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "verifyStoragePermissions: " + e.getMessage());
        }
    }
}
