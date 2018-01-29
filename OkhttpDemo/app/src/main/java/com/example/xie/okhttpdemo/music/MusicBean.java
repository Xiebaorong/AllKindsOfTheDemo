package com.example.xie.okhttpdemo.music;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by xie on 2018/1/29.
 */

public class MusicBean implements Parcelable {
    private String url;
    private String title;
    private String author;
    private Long size;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeString(this.title);
        dest.writeString(this.author);
        dest.writeValue(this.size);
    }

    public MusicBean() {
    }

    protected MusicBean(Parcel in) {
        this.url = in.readString();
        this.title = in.readString();
        this.author = in.readString();
        this.size = (Long) in.readValue(Long.class.getClassLoader());
    }

    public static final Parcelable.Creator<MusicBean> CREATOR = new Parcelable.Creator<MusicBean>() {
        @Override
        public MusicBean createFromParcel(Parcel source) {
            return new MusicBean(source);
        }

        @Override
        public MusicBean[] newArray(int size) {
            return new MusicBean[size];
        }
    };
}
