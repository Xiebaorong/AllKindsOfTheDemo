package com.example.xie.okhttpdemo.music;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.xie.okhttpdemo.R;

import java.util.Map;

/**
 * Created by xie on 2018/1/29.
 */

public class NotificationUtils {
    private static final String TAG = "NotificationUtils";
    //广播一 播放
    private static final String MUSIC_PLAYS ="MusicService.MUSIC_PLAYS" ;
    //广播二 暂停
    private static final String MUSIC_PAUSES ="MusicService.MUSIC_PAUSES" ;
    //广播三 上一曲
    private static final String MUSIC_UP ="MusicService.MUSIC_UP" ;
    //广播二 下一曲
    private static final String MUSIC_DOWN ="MusicService.MUSIC_DOWN" ;


    public static void startNotification(Context context, Boolean isStop, MusicBean musicMap) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.notification_item);
        String type;


        if (isStop){
            type = MUSIC_PAUSES;
            remoteViews.setImageViewResource(R.id.notification_music_play,R.drawable.zanting);
        }else {
            type = MUSIC_PLAYS;
            remoteViews.setImageViewResource(R.id.notification_music_play,R.drawable.bofang);
        }

        remoteViews.setTextViewText(R.id.notification_music_author,musicMap.getAuthor());
        remoteViews.setTextViewText(R.id.notification_music_name,musicMap.getTitle());
        PendingIntent playIntent = PendingIntent.getBroadcast(context,1,new Intent(type),PendingIntent.FLAG_CANCEL_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.notification_music_play,playIntent);


        PendingIntent upIntent = PendingIntent.getBroadcast(context,2,new Intent(MUSIC_UP),PendingIntent.FLAG_CANCEL_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.notification_music_up,playIntent);
        PendingIntent downIntent = PendingIntent.getBroadcast(context,3,new Intent(MUSIC_DOWN),PendingIntent.FLAG_CANCEL_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.notification_music_down,playIntent);

        builder .setOngoing(true)//不被滑动取消
                .setCustomBigContentView(remoteViews)//显示大布局
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setWhen(System.currentTimeMillis());
        Notification notification = builder.build();
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1, notification);

    }
}
