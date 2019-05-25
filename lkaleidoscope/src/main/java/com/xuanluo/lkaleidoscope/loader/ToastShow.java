package com.xuanluo.lkaleidoscope.loader;

import android.content.Context;
import android.widget.Toast;

/**
 * Description：
 * Author：暄落丶
 * Date：2019/5/24
 */
public class ToastShow implements LKaleidoToastShow {
    @Override
    public void showLkToast(Context context, String str) {
        Toast.makeText(context,str+"",Toast.LENGTH_SHORT).show();
    }
}
