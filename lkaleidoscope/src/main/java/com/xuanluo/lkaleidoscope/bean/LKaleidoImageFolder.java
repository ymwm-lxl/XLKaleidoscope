package com.xuanluo.lkaleidoscope.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import androidx.annotation.Nullable;

/**
 * Description：存放图片的文件夹
 * Author：暄落丶
 * Date：2019/4/27
 */
public class LKaleidoImageFolder implements Parcelable {
    private String name;  //当前文件夹的名字
    private String path;  //当前文件夹的路径
    private LKaleidoImageBean cover;   //当前文件夹需要要显示的缩略图，默认为最近的一次图片
    private ArrayList<LKaleidoImageBean> images;  //当前文件夹下所有图片的集合

    /** 只要文件夹的路径和名字相同，就认为是相同的文件夹 */
    @Override
    public boolean equals(@Nullable Object obj) {
        try {
            LKaleidoImageFolder other = (LKaleidoImageFolder) obj;
            return this.getPath().equalsIgnoreCase(other.getPath()) && this.getName().equalsIgnoreCase(other.getName());
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return super.equals(obj);
    }

    public LKaleidoImageFolder() {
        this.path = path;
    }

    protected LKaleidoImageFolder(Parcel in) {
        name = in.readString();
        path = in.readString();
        cover = in.readParcelable(LKaleidoImageBean.class.getClassLoader());
        images = in.createTypedArrayList(LKaleidoImageBean.CREATOR);
    }

    public static final Creator<LKaleidoImageFolder> CREATOR = new Creator<LKaleidoImageFolder>() {
        @Override
        public LKaleidoImageFolder createFromParcel(Parcel in) {
            return new LKaleidoImageFolder(in);
        }

        @Override
        public LKaleidoImageFolder[] newArray(int size) {
            return new LKaleidoImageFolder[size];
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

    public LKaleidoImageBean getCover() {
        return cover;
    }

    public void setCover(LKaleidoImageBean cover) {
        this.cover = cover;
    }

    public ArrayList<LKaleidoImageBean> getImages() {
        return images;
    }

    public void setImages(ArrayList<LKaleidoImageBean> images) {
        this.images = images;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(path);
        dest.writeParcelable(cover, flags);
        dest.writeTypedList(images);
    }
}
