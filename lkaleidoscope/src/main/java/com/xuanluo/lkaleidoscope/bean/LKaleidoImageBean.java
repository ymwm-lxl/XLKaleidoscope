package com.xuanluo.lkaleidoscope.bean;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

/**
 * Description：
 * Author：暄落丶
 * Date：2019/4/27
 */
public class LKaleidoImageBean implements Parcelable {

    private String name;       //图片的名字
    private String path;       //图片的路径
    private long size;         //图片的大小
    private int width;         //图片的宽度
    private int height;        //图片的高度
    private String mimeType;   //图片的类型
    private long addTime;      //图片的创建时间

    /** 图片的路径和创建时间相同就认为是同一张图片 */
    @Override
    public boolean equals(@Nullable Object obj) {

        if (obj instanceof LKaleidoImageBean) {
            LKaleidoImageBean item = (LKaleidoImageBean) obj;
            return this.getPath().equalsIgnoreCase(item.getPath()) && this.getAddTime() == item.getAddTime();
        }

        return super.equals(obj);
    }

    public LKaleidoImageBean() {

    }

    protected LKaleidoImageBean(Parcel in) {
        name = in.readString();
        path = in.readString();
        size = in.readLong();
        width = in.readInt();
        height = in.readInt();
        mimeType = in.readString();
        addTime = in.readLong();
    }

    public static final Creator<LKaleidoImageBean> CREATOR = new Creator<LKaleidoImageBean>() {
        @Override
        public LKaleidoImageBean createFromParcel(Parcel in) {
            return new LKaleidoImageBean(in);
        }

        @Override
        public LKaleidoImageBean[] newArray(int size) {
            return new LKaleidoImageBean[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public long getAddTime() {
        return addTime;
    }

    public void setAddTime(long addTime) {
        this.addTime = addTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(path);
        dest.writeLong(size);
        dest.writeInt(width);
        dest.writeInt(height);
        dest.writeString(mimeType);
        dest.writeLong(addTime);
    }
}
