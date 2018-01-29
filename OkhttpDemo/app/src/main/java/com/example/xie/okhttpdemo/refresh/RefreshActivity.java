package com.example.xie.okhttpdemo.refresh;

import android.os.Handler;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.xie.okhttpdemo.BaseActivity;
import com.example.xie.okhttpdemo.R;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class RefreshActivity extends BaseActivity {
    private static final String TAG = "RefreshActivity";
    @BindView(R.id.listName)
    ListView listView;

    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;

    String[] names = {"张三","李四","王五","高德"};

    List<String> list;
    @Override
    protected int getLayout() {
        return R.layout.activity_refresh;
    }

    @Override
    public void onAcCreate() {

        list = new ArrayList<>();

        for (int i = 0; i <names.length ; i++) {
            String name = names[i];
            list.add(name);
        }

        listView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                Log.e(TAG, "onRefresh: ");
                refreshlayout.finishRefresh(2000,true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        list.add("百度");
                        list.add("腾讯");
                        listView.setAdapter(new ArrayAdapter<String>(RefreshActivity.this,android.R.layout.simple_list_item_1,list));
                    }
                },2000);

            }
        });

        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                Log.e(TAG, "onLoadmore:");
                refreshlayout.finishLoadmore(2000,true);
            }
        });


    }
}
