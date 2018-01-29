package com.example.xie.okhttpdemo.music;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by xie on 2018/1/26.
 */

public class MusicService extends Service {
    private static final String TAG = "MusicService";
    private static final int MUSIC_PLAY = 1;
    private static final int MUSIC_STOP = 2;
    Boolean isStop = true;

    private MediaPlayer mediaPlayer;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate: ");
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();

            //播放完成监听
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    Log.e(TAG, "onCompletion:播放完成 ");
                    Intent intent = new Intent();
                    intent.setAction("com.complete");
                    sendBroadcast(intent);
                }
            });
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int type = intent.getIntExtra("type", 0);

        switch (type) {
            case MUSIC_PLAY:
                if (isStop) {
                    mediaPlayer.reset();
                    mediaPlayer = MediaPlayer.create(this, Uri.parse(intent.getStringExtra("url")));
                    mediaPlayer.start();
                    isStop = false;
                }else if (!isStop&&mediaPlayer!=null){
                    mediaPlayer.start();
                }

                break;
            case MUSIC_STOP:
                if (mediaPlayer!=null&&mediaPlayer.isPlaying()){
                    mediaPlayer.pause();

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
}
