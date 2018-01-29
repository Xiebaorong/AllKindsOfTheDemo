package com.example.xie.okhttpdemo.music;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.example.xie.okhttpdemo.R;

import java.util.Map;

/**
 * Created by xie on 2018/1/29.
 */

public class NotificationUtils {

    public static void startNotification(Context context, Boolean isStop, Map<String, Object> musicMap) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.notification_item);
        builder .setOngoing(true)//不被滑动取消
                .setCustomBigContentView(remoteViews)//显示大布局
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setWhen(System.currentTimeMillis());
        Notification notification = builder.build();
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1, notification);

    }
}
