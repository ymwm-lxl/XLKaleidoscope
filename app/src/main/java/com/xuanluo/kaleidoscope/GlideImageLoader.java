package com.xuanluo.kaleidoscope;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.xuanluo.lkaleidoscope.loader.LKaleidoImageLoader;

import java.io.File;

/**
 * Description：
 * Author：暄落丶
 * Date：2019/4/27
 */
public class GlideImageLoader implements LKaleidoImageLoader {
    @Override
    public void displayImage(Context context, String path, ImageView imageView, int width, int height) {
        RequestOptions requestOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL);   //缓存全尺寸;
//                .error(R.drawable.)           //设置错误图片
//                .placeholder(R.drawable.ic_default_image)    //设置占位图片
        Glide.with(context)
                .load(Uri.fromFile(new File(path)))
                .apply(requestOptions)
                .thumbnail(0.25f)
                .into(imageView);
    }

    @Override
    public void displayImagePreview(Context context, String path, ImageView imageView, int width, int height) {
        RequestOptions requestOptions = new RequestOptions()
//                .error(R.drawable.)           //设置错误图片
//                .placeholder(R.drawable.ic_default_image)    //设置占位图片
                .diskCacheStrategy(DiskCacheStrategy.ALL);   //缓存全尺寸
        Glide.with(context)
                .load(Uri.fromFile(new File(path)))
                .thumbnail(0.25f)
                .apply(requestOptions)
                .into(imageView);
    }

}
