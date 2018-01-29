package com.example.xie.okhttpdemo.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.xie.okhttpdemo.R;

import java.util.List;

/**
 * Created by xie on 2018/1/24.
 */

public class PhoneAdapter extends BaseAdapter{
    private static final String TAG = "PhoneAdapter";
    private Context mContext;
    private List<Bitmap> mList;
    public PhoneAdapter(Context context, List<Bitmap> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public int getCount() {
        int num = 0;
        if(mList.size()==0){
            num = mList.size() + 1;
        }else {
            num = mList.size()+1;
        }
        return num;
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if(view ==null){
            view = LayoutInflater.from(mContext).inflate(R.layout.gridview_item,null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }

        if (i==mList.size()){
            viewHolder.imageView.setImageResource(R.drawable.icon_addpic_unfocused);
                if (i==8){
                    viewHolder.imageView.setVisibility(View.GONE);
                }
        }else {
            viewHolder.imageView.setImageBitmap(mList.get(i));

        }
        return view;
    }

    class ViewHolder{
        private ImageView imageView;
        public ViewHolder(View view){
            imageView = (ImageView) view.findViewById(R.id.phoneImage);
        }
    }
}
