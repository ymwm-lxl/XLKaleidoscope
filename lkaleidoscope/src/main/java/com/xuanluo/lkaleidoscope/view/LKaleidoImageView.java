package com.xuanluo.lkaleidoscope.view;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;

import com.xuanluo.lkaleidoscope.LKaleidoscope;
import com.xuanluo.lkaleidoscope.R;
import com.xuanluo.lkaleidoscope.util.LKaleidoAspectRatioUtil;

import androidx.appcompat.widget.AppCompatImageView;

/**
 * Description：
 * Author：暄落丶
 * Date：2019/4/27
 */
public class LKaleidoImageView extends AppCompatImageView {

    private static final String TAG = "LKaleidoImageView";

    private Context mContext;

    private float heightRatio = -1;
    private float weightRatio = -1;

    public LKaleidoImageView(Context context) {
        super(context);
        this.mContext = context;
    }

    public LKaleidoImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView(attrs);
    }

    public LKaleidoImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
    }

    private void initView(AttributeSet attrs){
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.LKaleidoImageView);


        weightRatio = typedArray.getFloat(R.styleable.LKaleidoImageView_weight_ratio_iv,-1);
        heightRatio = typedArray.getFloat(R.styleable.LKaleidoImageView_height_ratio_iv,-1);

        typedArray.recycle();//回收

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {


        super.onMeasure(LKaleidoAspectRatioUtil.height2weight(widthMeasureSpec,heightMeasureSpec,weightRatio)
                , LKaleidoAspectRatioUtil.weight2height(widthMeasureSpec, heightMeasureSpec,heightRatio));
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

        if (mContext != null && url != null){
            LKaleidoscope.getInstance().getImageLoader().displayImage(mContext,url,this,0,0);
        }else {
            Log.e(TAG,"图片为空无法显示");
        }


    }




    /**
     * 更改 weightRatio
     */
    public void setWeightRatio(float weightRatio){
        this.weightRatio = weightRatio;
    }

    /**
     * 更改 heightRatio
     */
    public void setHeightRatio(float heightRatio){
        this.heightRatio = heightRatio;
    }


}
