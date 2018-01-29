package com.example.xie.okhttpdemo.phone;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.xie.okhttpdemo.BaseActivity;
import com.example.xie.okhttpdemo.R;

import butterknife.BindView;

/**
 * Created by xie on 2018/1/25.
 * 仿微信中放大单张大图显示
 */

public class ImageActivity extends BaseActivity {
    @BindView(R.id.image_back)
    Button back;

    @BindView(R.id.image_delete)
    Button delete;

    @BindView(R.id.image_phone)
    ImageView imageView;

    private static final int SHOWPHONE = 4402;

    @Override
    protected int getLayout() {
        return R.layout.activity_image;
    }

    @Override
    public void onAcCreate() {
        Bitmap phone = getIntent().getExtras().getParcelable("phone");
        imageView.setImageBitmap(phone);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("imageID",getIntent().getExtras().getInt("ID"));
                setResult(SHOWPHONE,intent);
                finish();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


}
