package com.xuanluo.lkaleidoscope.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.xuanluo.lkaleidoscope.R;

import androidx.annotation.ColorRes;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

/**
 * Description：
 * Author：暄落丶
 * Date：2019/4/28
 */
public class FolderPopUpWindow extends PopupWindow {

    private RecyclerView rvFolder;

    public FolderPopUpWindow(Context context , RecyclerView.Adapter adapter) {
        super(context);

        View view = (View) View.inflate(context,R.layout.popup_sel_folder,null);
        rvFolder = view.findViewById(R.id.popup_selFolder_rv);
        rvFolder.setLayoutManager(new LinearLayoutManager(context));
        rvFolder.addItemDecoration(new DividerItemDecoration(context,DividerItemDecoration.VERTICAL));
        rvFolder.setAdapter(adapter);

        setContentView(view);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);  //如果不设置，就是 AnchorView 的宽度
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        setOutsideTouchable(true);


    }
}
