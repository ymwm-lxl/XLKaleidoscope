package com.xuanluo.kaleidoscope;

import android.content.Context;
import android.widget.Toast;

import com.xuanluo.lkaleidoscope.loader.LKaleidoToastShow;

/**
 * Description：
 * Author：暄落丶
 * Date：2019/4/28
 */
public class ToastShow implements LKaleidoToastShow {
    @Override
    public void showLkToast(Context context, String str) {
        Toast.makeText(context,str+"",Toast.LENGTH_SHORT).show();
    }
}
