package com.xuanluo.lkaleidoscope.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.xuanluo.lkaleidoscope.R;

import androidx.annotation.IdRes;
import androidx.appcompat.widget.AppCompatCheckBox;

/**
 * Description：
 * Author：暄落丶
 * Date：2019/4/28
 */
public class LKaleidoImageSelCheckBox extends AppCompatCheckBox {

    private static String TAG = "LKaleidoImageSelCheckBox";

    private Context mContext;

    private View borderView;

    public LKaleidoImageSelCheckBox(Context context) {
        this(context,null);
    }

    public LKaleidoImageSelCheckBox(Context context, AttributeSet attrs) {
        this(context, attrs,0);
        mContext = context;
        init(attrs);
    }

    public LKaleidoImageSelCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init(attrs);
    }

    private void init(AttributeSet attrs){

    }



}
