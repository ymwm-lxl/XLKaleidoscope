package com.xuanluo.kaleidoscope;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.xuanluo.lkaleidoscope.LKaleidoscope;
import com.xuanluo.lkaleidoscope.adapter.LKaleidoGridShowAdapter;
import com.xuanluo.lkaleidoscope.bean.LKaleidoImageBean;
import com.xuanluo.lkaleidoscope.bean.LKaleidoImageFolder;
import com.xuanluo.lkaleidoscope.ui.LKaleidoCameraActivity;
import com.xuanluo.lkaleidoscope.ui.LKaleidoGridShowActivity;
import com.xuanluo.lkaleidoscope.ui.LkaleidoDisplayActivity;
import com.xuanluo.lkaleidoscope.util.LKaleidoUtils;
import com.xuanluo.lkaleidoscope.view.GridSpacingItemDecoration;
import com.xuanluo.lkaleidoscope.view.LKaleidoImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private static final int JUMP_TAG_SEL_IMG = 0xa0;

    private Button btnKaleidoscope;
    private TextView mTvImgSize;
    private RecyclerView mRvImgShow;
    private ImageShowAdapter mShowAdapter;
    private LKaleidoscope mLKaleidoscope;

    private Timer timer;

    private ArrayList<LKaleidoImageBean> mImageBeans = new ArrayList<>();
    private int showImgPosition = 0 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initLKaleidoscope();
        btnKaleidoscope = findViewById(R.id.main_btn_kaleidoscope);
        mTvImgSize = findViewById(R.id.main_tv_imgSize);
        mRvImgShow = findViewById(R.id.main_rv_imgShow);

        btnKaleidoscope.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LKaleidoGridShowActivity.class);
//                Intent intent = new Intent(MainActivity.this, LKaleidoCameraActivity.class);
                Bundle bundle = new Bundle();
//                bundle.putBoolean(LKaleidoGridShowActivity.LKALEIDO_DATA_IS_CAMERA,true);
                intent.putExtras(bundle);
                startActivityForResult(intent,JUMP_TAG_SEL_IMG);


            }
        });




        mShowAdapter = new ImageShowAdapter(this, mImageBeans);
        mLKaleidoscope = LKaleidoscope.getInstance();
        mRvImgShow.setLayoutManager(new GridLayoutManager(this, mLKaleidoscope.getSpanCountLimit()));
        mRvImgShow.addItemDecoration(new GridSpacingItemDecoration(mLKaleidoscope.getSpanCountLimit(), LKaleidoUtils.dp2px(this, 2), false));
        mRvImgShow.setAdapter(mShowAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == JUMP_TAG_SEL_IMG && resultCode == Activity.RESULT_OK){
            if (data!= null){
                mImageBeans = data.getExtras().getParcelableArrayList(LKaleidoGridShowActivity.JUMP_DATA_SEL_OK_IMG);
                mTvImgSize.setText("选择了 "+mImageBeans.size()+" 张");
                mShowAdapter.refreshData(mImageBeans);

            }

        }
    }

    private void initLKaleidoscope(){
        LKaleidoscope lKaleidoscope = LKaleidoscope.getInstance()
                .setImageLoader(new GlideImageLoader())//设置图片加载器
                .setThemeStyle(R.style.mainTheme)//设置样式
//                .setToastShow(new ToastShow())//toast 显示仪
                .setSelectLimit(3)//图片最多数量//如果設置為1 則為單選
                .setGifShow(true)//是否显示gif 默认显示
                .setSelectMinLimit(1)//最小选择图片数量 //如果显示相机，则无法控制
                .setSpanCountLimit(3)//設置圖片列數
                .setShowCamera(true)//是否显示相机
                .setCrop(true)//是否裁剪
                .setDisplay(true)//是否预览
                .setOutPutX(1)//宽高比
                .setOutPutY(1)//宽高比
                .setFreeStyleCrop(false)//是否能够拖动裁剪框（比例自定）
                .setCompressionQuality(100)//设置裁剪的图片质量，取值0-100
                .setCircleDimmed(true)//是否要圆形
                .setRotate(false);//是否支持旋转

//                .setIcCameraBack(R.mipmap.ic_launcher)//相机页面-退出拍照页面按钮
//                .setIcCameraTake(R.mipmap.ic_launcher)//相机页面-拍照按钮
//                .setIcCameraFlip(R.mipmap.ic_launcher)//相机页面-切换前后摄像头按钮
//                .setIcCameraDisagree(R.mipmap.ic_launcher)//相机页面-预览页面-重新拍摄按钮
//                .setIcCameraAgree(R.mipmap.ic_launcher);//相机页面-预览页面-保存按钮

//        lKaleidoscope.setImageLoader(new GlideImageLoader());//设置图片加载器
////        lKaleidoscope.setThemeStyle(R.style.mainTheme);//设置样式
//        lKaleidoscope.setToastShow(new ToastShow());//toast 显示仪
//        lKaleidoscope.setSelectLimit(3);//图片最多数量//如果設置為1 則為單選
//        lKaleidoscope.setGifShow(true); //是否显示gif 默认显示
//        lKaleidoscope.setSelectMinLimit(1); //最小选择图片数量 //如果显示相机，则无法控制
//        lKaleidoscope.setSpanCountLimit(3);//設置圖片列數
//        lKaleidoscope.setShowCamera(true);//是否显示相机
//        lKaleidoscope.setCrop(true);//是否裁剪
//        lKaleidoscope.setDisplay(true); //是否预览
//        lKaleidoscope.setOutPutX(1);//宽高比
//        lKaleidoscope.setOutPutY(1);//宽高比
//        lKaleidoscope.setFreeStyleCrop(false);//是否能够拖动裁剪框（比例自定）
//        lKaleidoscope.setCompressionQuality(100); //设置裁剪的图片质量，取值0-100
//        lKaleidoscope.setCircleDimmed(false);//是否要圆形
//        lKaleidoscope.setRotate(false);//是否支持旋转

//        lKaleidoscope.setIcCameraBack(R.mipmap.ic_launcher);//相机页面-退出拍照页面按钮
//        lKaleidoscope.setIcCameraTake(R.mipmap.ic_launcher);//相机页面-拍照按钮
//        lKaleidoscope.setIcCameraFlip(R.mipmap.ic_launcher);//相机页面-切换前后摄像头按钮
//        lKaleidoscope.setIcCameraDisagree(R.mipmap.ic_launcher);//相机页面-预览页面-重新拍摄按钮
//        lKaleidoscope.setIcCameraAgree(R.mipmap.ic_launcher);//相机页面-预览页面-保存按钮

    }


}
