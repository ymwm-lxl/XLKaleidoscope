package com.xuanluo.lkaleidoscope.ui;

import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.xuanluo.lkaleidoscope.LKaleidoscope;
import com.xuanluo.lkaleidoscope.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

/**
 * Description：
 * Author：暄落丶
 * Date：2019/4/27
 */
public class LKaleidoBaseActivity extends AppCompatActivity {


    /** 日志输出标志 **/
    protected final String TAG = this.getClass().getSimpleName();

    protected int main_color;
    protected int main_color_transparent;
    protected int two_color;
    protected int two_color_transparent;

    protected int card_color;
    protected int grid_bg_color;

    protected int font_main_color;
    protected int font_black_color;
    protected int font_gray_color;

    protected int sel_transparent;

    private LKaleidoscope mBaseLKaleidoscope;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBaseLKaleidoscope = LKaleidoscope.getInstance();
        setTheme(mBaseLKaleidoscope.getThemeStyle());
        initAttr();

        initDrawable();

    }


     void setTranslucentStatus() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
            Window window = getWindow();
            View decorView = window.getDecorView();
            //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            //导航栏颜色也可以正常设置
//            window.setNavigationBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            WindowManager.LayoutParams attributes = window.getAttributes();
            int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            attributes.flags |= flagTranslucentStatus;
            //int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
            //attributes.flags |= flagTranslucentNavigation;
            window.setAttributes(attributes);
        }
    }


    private void initAttr(){
        TypedArray typedValue = this.getTheme().obtainStyledAttributes(new int[]{
                R.attr.Lkaleido_main_color ,
                R.attr.Lkaleido_main_color_transparent ,
                R.attr.Lkaleido_two_color ,
                R.attr.Lkaleido_two_color_transparent ,

                R.attr.Lkaleido_card_color ,
                R.attr.Lkaleido_grid_bg_color ,

                R.attr.Lkaleido_font_main_color ,
                R.attr.Lkaleido_font_black_color ,
                R.attr.Lkaleido_font_gray_color ,
                R.attr.Lkaleido_sel_transparent
        });

        main_color = typedValue.getColor(0,getResources().getColor(R.color.main_color));
        main_color_transparent  = typedValue.getColor(1,getResources().getColor(R.color.main_color_transparent));
        two_color = typedValue.getColor(2,getResources().getColor(R.color.two_color));
        two_color_transparent = typedValue.getColor(3,getResources().getColor(R.color.two_color_transparent));

        card_color = typedValue.getColor(4,getResources().getColor(R.color.card_color));
        grid_bg_color = typedValue.getColor(5,getResources().getColor(R.color.grid_bg_color));

        font_main_color = typedValue.getColor(6,getResources().getColor(R.color.font_main_color));
        font_black_color = typedValue.getColor(7,getResources().getColor(R.color.font_black_color));
        font_gray_color = typedValue.getColor(8,getResources().getColor(R.color.font_gray_color));

        sel_transparent = typedValue.getColor(9,getResources().getColor(R.color.sel_transparent));


        typedValue.recycle();
    }

    private void initDrawable() {
        //返回按钮
        VectorDrawableCompat mIvBackDrawableCompat = VectorDrawableCompat.create(getResources(),R.drawable.ic_topbar_back,getTheme());
        mIvBackDrawableCompat.setTint(font_main_color);

        //下拉按钮
        VectorDrawableCompat selFolderDrawableCompat = VectorDrawableCompat.create(getResources(),R.drawable.ic_arrow_drop_down,getTheme());
        selFolderDrawableCompat.setTint(font_main_color);


        //添加照片
        VectorDrawableCompat addphotoDrawableCompat = VectorDrawableCompat.create(getResources(),R.drawable.ic_add_a_photo,getTheme());
        addphotoDrawableCompat.setTint(two_color);

        //图片选择标记- 选中
        VectorDrawableCompat checkyesDrawableCompat = VectorDrawableCompat.create(getResources(),R.drawable.ic_check_yes,getTheme());
        checkyesDrawableCompat.setTint(two_color);

        //图片选择标记- 未选中
        VectorDrawableCompat checknoDrawableCompat = VectorDrawableCompat.create(getResources(),R.drawable.ic_cleck_no,getTheme());
        checknoDrawableCompat.setTint(two_color);

        //图片选择边框
        VectorDrawableCompat checkBorderDrawableCompat = VectorDrawableCompat.create(getResources(),R.drawable.bg_image_sel_border,getTheme());
        checkBorderDrawableCompat.setTint(two_color);

        //文件夹选择背景
        VectorDrawableCompat folderBGDrawableCompat = VectorDrawableCompat.create(getResources(),R.drawable.bg_folder_sel_color,getTheme());
        folderBGDrawableCompat.setTint(two_color_transparent);

        //点击背景
        VectorDrawableCompat clickBGDrawableCompat = VectorDrawableCompat.create(getResources(),R.drawable.bg_item_click_bg,getTheme());
        clickBGDrawableCompat.setTint(sel_transparent);

        //相机图标
        VectorDrawableCompat cameraBackDrawableCompat = VectorDrawableCompat.create(getResources(),R.drawable.ic_camera_back,getTheme());
        cameraBackDrawableCompat.setTint(two_color);

        VectorDrawableCompat cameraTakeDrawableCompat = VectorDrawableCompat.create(getResources(),R.drawable.ic_camera_take,getTheme());
        cameraTakeDrawableCompat.setTint(two_color);

        VectorDrawableCompat cameraflipDrawableCompat = VectorDrawableCompat.create(getResources(),R.drawable.ic_camera_flip,getTheme());
        cameraflipDrawableCompat.setTint(two_color);

        VectorDrawableCompat cameraAgereeDrawableCompat = VectorDrawableCompat.create(getResources(),R.drawable.ic_camera_agree,getTheme());
        cameraAgereeDrawableCompat.setTint(two_color);

        VectorDrawableCompat cameraDisagDrawableCompat = VectorDrawableCompat.create(getResources(),R.drawable.ic_camera_disagree,getTheme());
        cameraDisagDrawableCompat.setTint(two_color);

    }

    private static long lastClickTime;
    public synchronized static boolean isFastClick() {
        long time = System.currentTimeMillis();
        if ( time - lastClickTime < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

}
