package com.example.xie.okhttpdemo.music;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.EventLogTags;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by xie on 2018/1/29.
 */

public class NotificationBroadCast extends BroadcastReceiver {
    private static final String TAG = "NotificationBroadCast";
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals("music.NotificationBroadCast.music_play")){

            Log.e(TAG, "onReceive: 播放" );

        }else if (action.equals("music.NotificationBroadCast.music_pause")){
            Log.e(TAG, "onReceive: 暂停" );

        }
    }
}
