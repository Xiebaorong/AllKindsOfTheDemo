package com.example.xie.okhttpdemo.music;

import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xie.okhttpdemo.BaseActivity;
import com.example.xie.okhttpdemo.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by xie on 2018/1/26.
 */

public class MusicActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "MusicActivity";
    private static final int MUSIC_PLAY = 1;
    private static final int MUSIC_STOP = 2;
    @BindView(R.id.music_find)
    Button music_find;
    @BindView(R.id.music_up)
    Button music_up;
    @BindView(R.id.music_down)
    Button music_down;
    @BindView(R.id.music_play)
    Button music_play;
    @BindView(R.id.music_time)
    TextView music_time;
    @BindView(R.id.music_message)
    TextView music_message;
    @BindView(R.id.music_all)
    ListView music_all;

    Boolean isStop = true;
    private MyBroadCastReceiver receiver;

    private List<Map<String, Object>> musicList = new ArrayList<>();

    @Override
    protected int getLayout() {
        return R.layout.activity_music;
    }

    @Override
    public void onAcCreate() {
        receiver = new MyBroadCastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.complete");
        registerReceiver(receiver,filter);
        music_find.setOnClickListener(this);
        music_play.setOnClickListener(this);
//
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.music_find:
                if (musicList.size() == 0) {
                    musicList = findMusic();
                    if (!musicList.get(0).equals("null")) {
                        music_all.setAdapter(new MusicAdapter(MusicActivity.this, musicList));
                    }
                }
                break;
            case R.id.music_up:

                break;
            case R.id.music_play:
                musicList = findMusic();
                Log.e(TAG, "onClick: " + musicList.size());
                if (musicList.size() == 0) {
                    Toast.makeText(MusicActivity.this, "本地没有歌曲", Toast.LENGTH_SHORT).show();
                } else {
                    int type;
                    if (isStop) {
                        music_play.setText("暂停");
                        type = MUSIC_PLAY;
                        isStop = false;
                    } else {
                        music_play.setText("播放");
                        type = MUSIC_STOP;
                        isStop = true;
                    }
                    Intent intent = new Intent(MusicActivity.this, MusicService.class);
                    intent.putExtra("type", type);
//                    intent.putExtra("url", musicList.get(i).get("musicFileUrl") + "");
                    startService(intent);

                }
                break;
        }
    }

    private List<Map<String, Object>> findMusic() {
        List list = new ArrayList<>();
        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                int musicId = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
                String musicTitle = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                String author = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                String url = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
                if (size > 1024 * 1000) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("musicTitle", musicTitle);
                    map.put("musicFileUrl", url);
                    map.put("music_author", author);
                    list.add(map);
                }
                cursor.moveToNext();
            }
//            Log.e(TAG, "findMusic: \nid:"+musicId+"\nmusicTitle:"+musicTitle+"\nauthor:"+author+"\nurl:"+url+"\nsize:"+size);
        } else {
            list.add("null");
        }
        return list;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
