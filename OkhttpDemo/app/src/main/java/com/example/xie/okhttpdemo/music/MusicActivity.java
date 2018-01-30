package com.example.xie.okhttpdemo.music;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xie.okhttpdemo.BaseActivity;
import com.example.xie.okhttpdemo.R;
import com.example.xie.okhttpdemo.music.adapter.MusicMessageAdapter;
import com.example.xie.okhttpdemo.music.evenbusBean.MusicMagess;
import com.scwang.smartrefresh.layout.util.DensityUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by xie on 2018/1/26.
 */

public class MusicActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private static final String TAG = "MusicActivity";
    private static final int MUSIC_PLAY = 1;
    private static final int MUSIC_PAUSE = 2;
    private static int code = -1;
    @BindView(R.id.music_find)
    Button music_find;
    @BindView(R.id.music_recycler_list)
    RecyclerView music_recycler_list;
    @BindView(R.id.music_play)
    ImageView music_play;
    @BindView(R.id.music_down)
    ImageView music_down;
    @BindView(R.id.music_time)
    TextView music_time;
    @BindView(R.id.music_message)
    TextView music_message;
    @BindView(R.id.music_all)
    ListView music_all;

    Boolean isStop = true;

    private List<MusicBean> musicList = new ArrayList<>();

    @Override
    protected int getLayout() {
        return R.layout.activity_music;
    }

    @Override
    public void onAcCreate() {
        EventBus.getDefault().register(MusicActivity.this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        music_recycler_list.setLayoutManager(manager);
        music_find.setOnClickListener(this);
        music_play.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.music_find:
                if (musicList.size() == 0) {
                    musicList = findMusic();
                        music_all.setAdapter(new MusicAdapter(MusicActivity.this, musicList));
                        music_all.setOnItemClickListener(this);


                }
                break;
            case R.id.music_down:

                break;
            case R.id.music_play:
                musicList = findMusic();
                Log.e(TAG, "onClick: " + musicList.size());
                if (musicList.size() == 0) {
                    Toast.makeText(MusicActivity.this, "本地没有歌曲", Toast.LENGTH_SHORT).show();
                } else {
                    int type;
                    if (isStop) {
                        music_play.setImageResource(R.drawable.zanting);
                        type = MUSIC_PLAY;
                        isStop = false;
                    } else {
                        music_play.setImageResource(R.drawable.bofang);
                        type = MUSIC_PAUSE;
                        isStop = true;
                    }
                    Intent intent = new Intent(MusicActivity.this, MusicService.class);
                    intent.putExtra("type", type);
                    intent.putParcelableArrayListExtra("musicList", (ArrayList<? extends Parcelable>) musicList);
//                    intent.putExtra("ID", i);
                    startService(intent);
                }
                break;
        }
    }

    private List<MusicBean> findMusic() {
        List<MusicBean> list = new ArrayList<>();

        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                int musicId = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                String author = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                String url = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
                if (size > 1024 * 1000) {
                    MusicBean musicBean = new MusicBean();
                    musicBean.setAuthor(author);
                    musicBean.setTitle(title);
                    musicBean.setUrl(url);
                    musicBean.setSize(size);

                    list.add(musicBean);
                }
                cursor.moveToNext();
            }
        }
        return list;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(receiver);
        EventBus.getDefault().unregister(MusicActivity.this);

    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onMessage(MusicMagess event) {
        int id = event.id;
        Intent musicIntent = new Intent(MusicActivity.this,MusicService.class);
        musicIntent.putExtra("type", MUSIC_PLAY);
        musicIntent.putParcelableArrayListExtra("musicList", (ArrayList<? extends Parcelable>) musicList);
        musicIntent.putExtra("ID", id);
        startService(musicIntent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMusicMessage(MusicMagess event) {
        int id = event.id;
        Log.e(TAG, "onMessage: EventBus:" + id);
        MusicMessageAdapter adapter = new MusicMessageAdapter(MusicActivity.this,musicList);
        music_recycler_list.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(code ==-1){
            musicPlay(position,isStop);
        } else {
            if (code == position) {
                musicPlay(position,isStop);
            } else {
                isStop = true;
                stopService(new Intent(MusicActivity.this, MusicService.class));
                musicPlay(position, isStop);
            }
        }
        code = position;
    }

    public void musicPlay(int i, Boolean isMusicStop){
        int type = 0;
        if (isMusicStop) {
            NotificationUtils.startNotification(MusicActivity.this, isStop, musicList.get(i));
            type = MUSIC_PLAY;
            isStop = false;
            EventBus.getDefault().post(new MusicMagess(i));
        }
        Intent intent = new Intent(MusicActivity.this, MusicService.class);
        intent.putExtra("type", type);
        intent.putParcelableArrayListExtra("musicList", (ArrayList<? extends Parcelable>) musicList);
        intent.putExtra("ID", i);
        startService(intent);
    }

}
