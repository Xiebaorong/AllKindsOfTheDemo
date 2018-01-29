package com.example.xie.okhttpdemo.phone;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.example.xie.okhttpdemo.BaseActivity;
import com.example.xie.okhttpdemo.R;
import com.example.xie.okhttpdemo.adapter.PhoneAdapter;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class PhoneActivity extends BaseActivity {
    private static final String TAG = "PhoneActivity";
    private static final int TAKEPHOTO = 3306;
    private static final int PHOTORESULT = 4405;
    private static final int REQUEST_ALBUM_OK = 2929;
    private static final String IMAGE_UNSPECIFIED ="image/*" ;
    private static final int SHOWPHONE = 4402;
    private File phoneFile;
    private String phoneName;
    @BindView(R.id.phoneGrid)
    GridView phoneGrid;
    DisplayMetrics dm;
    private List<Bitmap> list = new ArrayList();
    @Override
    protected int getLayout() {
        return R.layout.activity_phone;
    }

    @Override
    public void onAcCreate() {
        ininPhone();
        dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        PhoneAdapter adapter = new PhoneAdapter(this,list);
        phoneGrid.setAdapter(adapter);
        phoneGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==list.size()){
                    new PopupWindows(PhoneActivity.this,phoneGrid);
                }else {
                    Log.e(TAG, "onItemClick: "+i);
                    Intent intent = new Intent(PhoneActivity.this,ImageActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("phone",list.get(i));
                    bundle.putInt("ID",i);
                    intent.putExtras(bundle);
                    startActivityForResult(intent,SHOWPHONE);
                }



            }
        });
    }

    private void ininPhone() {

    }

    public class PopupWindows extends PopupWindow {
        public PopupWindows(Context context, View phoneGrid){
            View view = LayoutInflater.from(context).inflate(R.layout.popupwindows_item,null);
            view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.pop_view_alpha));
            LinearLayout pop_phone_lin = (LinearLayout) view.findViewById(R.id.pop_phone_linear);
            pop_phone_lin.startAnimation(AnimationUtils.loadAnimation(context,R.anim.pop_view_translate));
            setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
            setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            setFocusable(true);
            setOutsideTouchable(false);
            setContentView(view);
            showAtLocation(phoneGrid, Gravity.BOTTOM,0,0);
            Button paizhao= (Button) view.findViewById(R.id.pop_phone_paizhao);
            Button xuanqu= (Button) view.findViewById(R.id.pop_phone_xuanqu);
            Button back= (Button) view.findViewById(R.id.pop_phone_back);

            paizhao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    phone();
                    dismiss();
                }
            });

            xuanqu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent openAlbumIntent = new Intent(Intent.ACTION_PICK);
//                    Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    openAlbumIntent.setType("image/*");
                    startActivityForResult(openAlbumIntent, REQUEST_ALBUM_OK);
                    dismiss();
                }
            });
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
        }
    }

    private void phone() {

        phoneName = System.currentTimeMillis()+".jpg";
        phoneFile = new File(getExternalCacheDir(), phoneName);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){//判断版本是否为7.0以上
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(this,getPackageName()+".fileprovider",phoneFile));
        }else{
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(phoneFile));
        }
        startActivityForResult(intent,TAKEPHOTO);

    }

    //照相所用
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case TAKEPHOTO:
                //设置文件保存路径这里放在跟目录下
                File picture = new File(getExternalCacheDir() + "/"+phoneName);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                    Uri uri = FileProvider.getUriForFile(this, getPackageName()+".fileprovider", picture);
                    //裁剪照片
                    startPhotoZoom(uri);
                }else {
                    //裁剪照片
                    startPhotoZoom(Uri.fromFile(picture));
                }
                break;
            case PHOTORESULT:
                if (data == null) {
                    return;
                }
                // 裁剪照片的处理结果，看完后面的代码再看这里
                if (requestCode == PHOTORESULT) {

                    Bundle extras = data.getExtras();
                    File file = new File(getExternalCacheDir() + "/head.jpg");
                    Log.e(TAG, "onActivityResult: "+getExternalCacheDir() );
                    if (extras != null) {
                        Bitmap photo = extras.getParcelable("data");
                        try {
                            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                            photo.compress(Bitmap.CompressFormat.JPEG, 70, bos);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if(list.size()<8){
                            list.add(photo);
                            PhoneAdapter adapter = new PhoneAdapter(PhoneActivity.this,list);
                            phoneGrid.setAdapter(adapter);

                        }

                    }
                }
                break;
            case REQUEST_ALBUM_OK:
                //裁剪照片
                if (data == null) {
                    return;
                }
                startPhotoZoom(data.getData());
                break;
            case SHOWPHONE:
                if (data == null) {
                    return;
                }
                Log.e(TAG, "onActivityResult: "+data.getExtras().getInt("imageID"));
                list.remove(data.getExtras().getInt("imageID"));
                PhoneAdapter adapter = new PhoneAdapter(PhoneActivity.this,list);
                phoneGrid.setAdapter(adapter);
                break;

    }
   }

    private void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        //7.0判断
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
        intent.putExtra("circleCrop", "true");
        // aspectX aspectY 是宽高的比例
        //将大小定为9998与9999为防止圆形截图的出现
        intent.putExtra("aspectY", 1.1);
        intent.putExtra("aspectX", 1);

        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX",dm.widthPixels/4);
//        intent.putExtra("outputX",80);
        intent.putExtra("outputY", dm.heightPixels/4);
//        intent.putExtra("outputY", 80);
//        intent.putExtra("scale",true);//自由截取
        intent.putExtra("circleCrop", true);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PHOTORESULT);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        list.clear();
    }
}



