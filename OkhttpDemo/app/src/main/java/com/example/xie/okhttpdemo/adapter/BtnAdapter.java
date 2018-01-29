package com.example.xie.okhttpdemo.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.xie.okhttpdemo.MainActivity;
import com.example.xie.okhttpdemo.R;

/**
 * Created by xie on 2018/1/26.
 */

public class BtnAdapter extends BaseAdapter {
    private static final String TAG = "BtnAdapter";
    private Context mContext;
    private String[] mName;
    public BtnAdapter(Context context, String[] name) {
        this.mContext = context;
        this.mName = name;
    }

    @Override
    public int getCount() {
        return mName.length;
    }

    @Override
    public Object getItem(int i) {
        return mName[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(mContext).inflate(R.layout.mian_list_item,null);
        TextView itemBtn = (TextView) view.findViewById(R.id.item_btn);
        itemBtn.setText(mName[i]);
        return view;
    }
}
