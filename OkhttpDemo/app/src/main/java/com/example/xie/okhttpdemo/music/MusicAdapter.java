package com.example.xie.okhttpdemo.music;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.util.ArraySet;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.example.xie.okhttpdemo.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by xie on 2018/1/26.
 */

class MusicAdapter extends BaseAdapter {
    private static final String TAG = "MusicAdapter";
    private static final int MUSIC_PLAY = 1;
    private static final int MUSIC_STOP = 2;
    private Context context;
    private List<MusicBean> musicList;
    Boolean isStop = true;
    Boolean isNow = true;
    private Set<Integer> set = new HashSet<Integer>();
    public MusicAdapter(Context context, List<MusicBean> musicList) {
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
        Log.e(TAG, "getView: ========" );
        final ViewHolder holder;
        final MusicBean musicBean = new MusicBean();
        musicBean.setId(-1);
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.music_item, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.music_item_title.setText(musicList.get(i).getTitle());
        holder.music_item_author.setText(musicList.get(i).getAuthor());

        holder.item_music_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int type;
                Log.e(TAG, "onClick: qqqqqqqq"+set.size() );
                if (set.size()==0){
                    Log.e(TAG, "onClick: ================" );
                    set.add(i);return;
                }else if (set.size()==1){
                    set.add(i);
                    Log.e(TAG, "onClick: ==="+set.size() );
                    Log.e(TAG, "onClick: 一致" );
                    return;
                }else if (set.size()==2){
                    set.remove(0);
                    Log.e(TAG, "onClick: +++"+set.size() );
                    Log.e(TAG, "onClick: 不一致" );return;
                }
                if (isStop) {
                    NotificationUtils.startNotification(context, isStop, musicList.get(i));
                    holder.item_music_play.setText("暂停");
                    type = MUSIC_PLAY;

                } else {
                    NotificationUtils.startNotification(context, isStop, musicList.get(i));
                    holder.item_music_play.setText("播放");
                    type = MUSIC_STOP;
                    isStop = true;
                }
                Intent intent = new Intent(context, MusicService.class);
                intent.putExtra("type", type);
                intent.putParcelableArrayListExtra("musicList", (ArrayList<? extends Parcelable>) musicList);
                intent.putExtra("ID", i);
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
