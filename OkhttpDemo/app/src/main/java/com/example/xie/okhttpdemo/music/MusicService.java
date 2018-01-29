package com.example.xie.okhttpdemo.music;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by xie on 2018/1/26.
 */

public class MusicService extends Service {
    private static final String TAG = "MusicService";
    private static final int MUSIC_PLAY = 1;
    private static final int MUSIC_STOP = 2;
    //广播一 播放
    private static final String MUSIC_PLAYS ="MusicService.MUSIC_PLAYS" ;
    //广播二 暂停
    private static final String MUSIC_PAUSES ="MusicService.MUSIC_PAUSES" ;
    private Boolean isStop = true;
    private NotificationReceiver receiver;
    private MediaPlayer mediaPlayer;
    private ArrayList<MusicBean> musicList;
    private int id;
    @Override
    public void onCreate() {
        super.onCreate();
        registerListener();

        Log.e(TAG, "onCreate: ");
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        id = intent.getIntExtra("ID", 0);
        musicList = intent.getParcelableArrayListExtra("musicList");
        int type = intent.getIntExtra("type", 0);

        switch (type) {
            case MUSIC_PLAY:
                Log.e(TAG, "onStartCommand: "+musicList.get(id).getUrl());
                if (isStop) {
                    mediaPlayer.reset();
                    mediaPlayer = MediaPlayer.create(this, Uri.parse(musicList.get(id).getUrl()));
                    mediaPlayer.start();
                    isStop = false;
                    JudgeUtils.isStop = false;
                }else if (!isStop&&mediaPlayer!=null){
                    mediaPlayer.start();
                    JudgeUtils.isStop = false;
                }

                break;
            case MUSIC_STOP:
                if (mediaPlayer!=null&&mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    JudgeUtils.isStop = true;
                }
                break;
        }

        return super.onStartCommand(intent, flags, startId);
    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mediaPlayer !=null&&mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void registerListener() {
        receiver = new NotificationReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(MUSIC_PLAYS);
        filter.addAction(MUSIC_PAUSES);
        registerReceiver(receiver,filter);
    }

    public class NotificationReceiver extends BroadcastReceiver{
        Boolean isPlay = true;
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.e(TAG, "onReceive: "+action );
            if(action.equals(MUSIC_PLAYS)){
                isPlay = true;
                if (!isStop&&mediaPlayer!=null){
                    mediaPlayer.start();
                    NotificationUtils.startNotification(context,isPlay,musicList.get(id));
                }
            }else if (action.equals(MUSIC_PAUSES)){
                if (mediaPlayer!=null){
                    mediaPlayer.pause();
                    isPlay = false;
                    NotificationUtils.startNotification(context,isPlay,musicList.get(id));
                }
            }
        }
    }
}
