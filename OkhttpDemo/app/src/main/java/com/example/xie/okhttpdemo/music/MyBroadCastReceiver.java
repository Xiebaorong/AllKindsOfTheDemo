package com.example.xie.okhttpdemo.music;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by xie on 2018/1/26.
 */

class MyBroadCastReceiver extends BroadcastReceiver{
    private static final String TAG = "MyBroadCastReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(TAG, "onReceive: 播放完成");
    }
}
