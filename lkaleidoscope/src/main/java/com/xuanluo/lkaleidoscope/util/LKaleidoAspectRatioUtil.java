package com.xuanluo.lkaleidoscope.util;

import android.util.Log;
import android.view.View;

/**
 * Description：
 * Author：暄落丶
 * Date：2019/4/27
 */
public final class LKaleidoAspectRatioUtil {

    private static final String TAG = "LKaleidoAspectRatioUtil";

    /**
     * 调整高度
     * @param widthMeasureSpec 宽
     * @param heightMeasureSpec 高
     * @return 高
     */
    public static int weight2height(int widthMeasureSpec, int heightMeasureSpec,float heightRatio){
        int widthSize = View.MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);

        int heightSize = View.MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);

        //是否指定了宽高比,如果指定了，则进行计算
        if (heightRatio > 0){
            //如果当前宽高不是写死的
            if (heightMode != View.MeasureSpec.EXACTLY ){

                double snapheight = widthSize/heightRatio;

                heightMeasureSpec = View.MeasureSpec.makeMeasureSpec((int)snapheight, View.MeasureSpec.EXACTLY);
            } else {
                Log.e(TAG,"无法指定宽高比");
            }
        }

        return heightMeasureSpec;
    }


    /**
     * 调整宽度
     * @param widthMeasureSpec 宽
     * @param heightMeasureSpec 高
     * @return 高
     */
    public static int height2weight(int widthMeasureSpec, int heightMeasureSpec ,float weightRatio){
        int widthSize = View.MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);

        int heightSize = View.MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);

        //是否指定了宽高比,如果指定了，则进行计算
        if (weightRatio > 0){
            //如果当前宽高不是写死的
            if (widthMode != View.MeasureSpec.EXACTLY ){

                double snapWeight = heightSize/weightRatio;

                widthMeasureSpec = View.MeasureSpec.makeMeasureSpec((int)snapWeight, View.MeasureSpec.EXACTLY);
            } else {
                Log.e(TAG,"无法指定宽高比");
            }
        }

        return widthMeasureSpec;
    }

}
