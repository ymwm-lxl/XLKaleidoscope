package com.xuanluo.lkaleidoscope.view;

import android.content.Context;
import android.util.AttributeSet;

import com.xuanluo.lkaleidoscope.LKaleidoscope;

/**
 * Description：
 * Author：暄落丶
 * Date：2019/4/30
 */
public class LKaleidoPhotoView extends com.github.chrisbanes.photoview.PhotoView {

    private Context mContext;

    public LKaleidoPhotoView(Context context) {
        this(context,null);
    }

    public LKaleidoPhotoView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LKaleidoPhotoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
    }

    /**
     * glide 加载图片
     * @param url
     */
    public void loadGlideImage(String url){

//        Glide.with(mContext)
//                .load(url)
//                .thumbnail(0.25f)
//                .apply(MyUtils.mRequestOptions())
//                .into(this);

        LKaleidoscope.getInstance().getImageLoader().displayImage(mContext,url,this,0,0);
    }


    public void loadGlideImagePreview(String url){
        LKaleidoscope.getInstance().getImageLoader().displayImagePreview(mContext,url,this,0,0);
    }
}
