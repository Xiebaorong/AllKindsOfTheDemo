package com.example.xie.okhttpdemo.music;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.example.xie.okhttpdemo.R;

import java.util.List;
import java.util.Map;

/**
 * Created by xie on 2018/1/26.
 */

class MusicAdapter extends BaseAdapter {
    private static final String TAG = "MusicAdapter";
    private static final int MUSIC_PLAY = 1;
    private static final int MUSIC_STOP = 2;
    private Context context;
    private List<Map<String, Object>> musicList;
    Boolean isStop = true;

    public MusicAdapter(Context context, List<Map<String, Object>> musicList) {
        this.context = context;
        this.musicList = musicList;
    }

    @Override
    public int getCount() {
        return musicList.size();
    }

    @Override
    public Object getItem(int i) {
        return musicList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final ViewHolder holder;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.music_item, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.music_item_title.setText(musicList.get(i).get("musicTitle") + "");
        holder.music_item_author.setText(musicList.get(i).get("music_author") + "");
        holder.item_music_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int type;
                if (isStop) {
                    NotificationUtils.startNotification(context,isStop, musicList.get(i));
                    holder.item_music_play.setText("暂停");
                    type = MUSIC_PLAY;
                    isStop = false;
                } else {
                    NotificationUtils.startNotification(context,isStop, musicList.get(i));

                    holder.item_music_play.setText("播放");
                    type = MUSIC_STOP;
                    isStop = true;
                }
                Intent intent = new Intent(context, MusicService.class);
                intent.putExtra("type", type);
                intent.putExtra("url", musicList.get(i).get("musicFileUrl") + "");
                context.startService(intent);

            }
        });
        return view;
    }




    class ViewHolder {
        TextView music_item_title;
        TextView music_item_author;
        TextView item_music_play;

        public ViewHolder(View view) {
            music_item_title = (TextView) view.findViewById(R.id.music_item_title);
            music_item_author = (TextView) view.findViewById(R.id.music_item_author);
            item_music_play = (TextView) view.findViewById(R.id.item_music_play);

        }
    }
}
