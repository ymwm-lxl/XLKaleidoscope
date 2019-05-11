package com.xuanluo.lkaleidoscope.loader;

import android.app.Activity;
import android.content.Context;
import android.widget.ImageView;

/**
 * Description：ImageLoader抽象类，外部需要实现这个类去加载图片， 尽力减少对第三方库的依赖，所以这么干了
 * 这样在外部可自由使用图片加载框架。
 * Author：暄落丶
 * Date：2019/4/27
 */
public interface LKaleidoImageLoader {

    //图片缩略图加载
    void displayImage(Context context, String path, ImageView imageView, int width, int height);

    //预览图片加载
    void displayImagePreview(Context context, String path, ImageView imageView, int width, int height);

//    void clearMemoryCache();
}
