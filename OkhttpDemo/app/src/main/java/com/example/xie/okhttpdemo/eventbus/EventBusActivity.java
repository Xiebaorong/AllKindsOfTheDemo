package com.example.xie.okhttpdemo.eventbus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.xie.okhttpdemo.BaseActivity;
import com.example.xie.okhttpdemo.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

public class EventBusActivity extends BaseActivity {

    @BindView(R.id.eventbus_btn_send)
    Button eventBus_send;
    @BindView(R.id.eventbus_btn_stickiness)
    Button eventBus_stick;
    @BindView(R.id.event_text)
    TextView eventBus_text;


    @Override
    protected int getLayout() {
        return R.layout.activity_event_bus;
    }

    @Override
    public void onAcCreate() {
        //1.先注册
        EventBus.getDefault().register(this);

        eventBus_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EventBusActivity.this,ToMessageActivity.class));
            }
        });

        eventBus_stick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().postSticky(new StickEvent("粘性事件"));
                startActivity(new Intent(EventBusActivity.this,ToMessageActivity.class));
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //2.解注册
        EventBus.getDefault().unregister(this);
    }

    //5.接收信息
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(MessageEvent event){
        eventBus_text.setText(event.msg);

    }
}
