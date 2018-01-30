package com.example.xie.okhttpdemo.music;

import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.xie.okhttpdemo.eventbus.MessageEvent;
import com.example.xie.okhttpdemo.music.evenbusBean.MusicMagess;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by xie on 2018/1/26.
 */

public class MusicService extends Service {
    private static final String TAG = "MusicService";
    private static final int MUSIC_PLAY = 1;
    private static final int MUSIC_PAUSE = 2;
    //广播一 播放
    private static final String MUSIC_PLAYS = "MusicService.MUSIC_PLAYS";
    //广播二 暂停
    private static final String MUSIC_PAUSES = "MusicService.MUSIC_PAUSES";
    //广播三 上一曲
    private static final String MUSIC_UP = "MusicService.MUSIC_UP";
    //广播四 下一曲
    private static final String MUSIC_DOWN = "MusicService.MUSIC_DOWN";
    //广播五 关闭
    private static final String MUSIC_STOP ="MusicService.MUSIC_STOP" ;

    private static final int MAX_MUSIC = 1;
    private static final int MIN_MUSIC = 2;
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
        Log.e(TAG, "onStartCommand: jici");
        if (intent != null) {
            id = intent.getIntExtra("ID", 0);
            musicList = intent.getParcelableArrayListExtra("musicList");
            int type = intent.getIntExtra("type", 0);

            switch (type) {
                case MUSIC_PLAY:
//                    Log.e(TAG, "onStartCommand: " + musicList.get(id).getUrl());
                    if (isStop) {
                        mediaPlayer.reset();
                        mediaPlayer = MediaPlayer.create(this, Uri.parse(musicList.get(id).getUrl()));
                        mediaPlayer.start();
                        isStop = false;
                    } else if (!isStop && mediaPlayer != null) {
                        mediaPlayer.start();
                    }

                    break;
                case MUSIC_PAUSE:
                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                    }
                    break;
            }
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
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            unregisterReceiver(receiver);
        }
    }

    private void registerListener() {
        receiver = new NotificationReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(MUSIC_PLAYS);
        filter.addAction(MUSIC_PAUSES);
        filter.addAction(MUSIC_UP);
        filter.addAction(MUSIC_DOWN);
        filter.addAction(MUSIC_STOP);
        registerReceiver(receiver, filter);
    }

    public class NotificationReceiver extends BroadcastReceiver {
        Boolean isPlay = true;

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
//            Log.e(TAG, "onReceive: " + action);
            if (action.equals(MUSIC_PLAYS)) {
                isPlay = true;
                if (isStop) {
                    mediaPlayer.reset();
                    mediaPlayer = MediaPlayer.create(MusicService.this, Uri.parse(musicList.get(id).getUrl()));
                    mediaPlayer.start();
                    isStop = false;
                    NotificationUtils.startNotification(context, isPlay, musicList.get(id));
                } else if (!isStop && mediaPlayer != null) {
                    mediaPlayer.start();
                    NotificationUtils.startNotification(context, isPlay, musicList.get(id));
                }
            } else if (action.equals(MUSIC_PAUSES)) {
                if (mediaPlayer != null) {
                    mediaPlayer.pause();
                    isPlay = false;
                    NotificationUtils.startNotification(context, isPlay, musicList.get(id));
                }
            } else if (action.equals(MUSIC_UP)) {
                Log.e(TAG, "onReceive: =================" + id);
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    isStop = true;
                    mediaPlayer.stop();
                    if (id == 0) {
                        int max_i = musicList.size();
                        mediaPlayer = MediaPlayer.create(MusicService.this, Uri.parse(musicList.get(max_i - 1).getUrl()));
                        isPlay = true;
                        NotificationUtils.startNotification(context, isPlay, musicList.get(max_i - 1));
                        EventBus.getDefault().postSticky(new MusicMagess(max_i - 1));
                    } else {
                        mediaPlayer = MediaPlayer.create(MusicService.this, Uri.parse(musicList.get(id - 1).getUrl()));
                        isPlay = true;
                        NotificationUtils.startNotification(context, isPlay, musicList.get(id - 1));
                        EventBus.getDefault().post(new MusicMagess(id - 1));
                    }
                    mediaPlayer.start();
                }
            } else if (action.equals(MUSIC_DOWN)) {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    isStop = true;
                    mediaPlayer.stop();
                    if (id == (musicList.size()-1)) {
                        int min_i = 0;
                        mediaPlayer = MediaPlayer.create(MusicService.this, Uri.parse(musicList.get(min_i).getUrl()));
                        isPlay = true;
                        NotificationUtils.startNotification(context, isPlay, musicList.get(min_i));
                        EventBus.getDefault().postSticky(new MusicMagess(min_i));
                    }else {
                        mediaPlayer = MediaPlayer.create(MusicService.this, Uri.parse(musicList.get(id + 1).getUrl()));
                        isPlay = true;
                        NotificationUtils.startNotification(context, isPlay, musicList.get(id + 1));
                        EventBus.getDefault().post(new MusicMagess(id + 1));
                    }
                    mediaPlayer.start();

                }
            }else if (action.equals(MUSIC_STOP)){
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    isStop=true;
                    mediaPlayer.stop();
                }
                NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                manager.cancel(1);
            }
        }
    }



}
