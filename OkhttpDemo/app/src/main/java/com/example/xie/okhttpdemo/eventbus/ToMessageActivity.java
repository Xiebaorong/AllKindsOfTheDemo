package com.example.xie.okhttpdemo.eventbus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.xie.okhttpdemo.BaseActivity;
import com.example.xie.okhttpdemo.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

public class ToMessageActivity extends BaseActivity {

    @BindView(R.id.to_message_edit)
    EditText messageEdit;
    @BindView(R.id.to_message_btn)
    Button messageBtn;
    @BindView(R.id.to_stick_btn)
    Button stickBtn;

    Boolean flag = true;
    @Override
    protected int getLayout() {
        return R.layout.activity_to_message;
    }

    @Override
    public void onAcCreate() {
        messageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = messageEdit.getText().toString();
                //4.获得信息将信息发送....
                EventBus.getDefault().post(new MessageEvent(s));
                finish();
            }
        });

        stickBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag){
                    flag = false;
                    //直接this不等同于ToMessageActivity,需要加ToMessageActivity.this,切记
                    EventBus.getDefault().register(ToMessageActivity.this);
                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onEvent(StickEvent event){
        messageEdit.setText(event.msg);
    }

}
