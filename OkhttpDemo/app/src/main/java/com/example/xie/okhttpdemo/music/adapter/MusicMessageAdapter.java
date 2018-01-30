package com.example.xie.okhttpdemo.music.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xie.okhttpdemo.R;
import com.example.xie.okhttpdemo.music.MusicBean;

import java.util.List;

/**
 * Created by xie on 2018/1/30.
 */

public class MusicMessageAdapter extends RecyclerView.Adapter<MusicMessageAdapter.ViewHolder> {
    private static final String TAG = "MusicMessageAdapter";
    private Context mContext;
    private List<MusicBean> mList;

    public MusicMessageAdapter(Context context, List<MusicBean> list) {
        this.mContext = context;
        this.mList = list;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.music_gridview_item, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.grid_music_name.setText(mList.get(position).getTitle());
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

//    @Override
//    public View getView(int i, View view, ViewGroup viewGroup) {
//        ViewHolder viewHolder;
//        if (view == null) {
//            view = LayoutInflater.from(mContext).inflate(R.layout.music_gridview_item, null);
//            viewHolder = new ViewHolder(view);
//            view.setTag(viewHolder);
//        } else {
//            viewHolder = (ViewHolder) view.getTag();
//        }
//        viewHolder.grid_music_name.setText(mList.get(i).getTitle());
//        return view;
//    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView grid_music_name;

        public ViewHolder(View itemView) {
            super(itemView);
            grid_music_name = (TextView) itemView.findViewById(R.id.grid_music_name);
        }
    }
}
