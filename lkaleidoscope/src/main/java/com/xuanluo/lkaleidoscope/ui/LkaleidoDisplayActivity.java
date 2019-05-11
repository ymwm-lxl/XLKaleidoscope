package com.xuanluo.lkaleidoscope.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xuanluo.lkaleidoscope.LKaleidoscope;
import com.xuanluo.lkaleidoscope.R;
import com.xuanluo.lkaleidoscope.adapter.LKaleidoViewPagerAdapter;
import com.xuanluo.lkaleidoscope.adapter.LkaleidoGalleryAdapter;
import com.xuanluo.lkaleidoscope.bean.LKaleidoImageBean;
import com.xuanluo.lkaleidoscope.util.LKaleidoFileUtils;
import com.xuanluo.lkaleidoscope.util.LKaleidoUtils;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import org.w3c.dom.Attr;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

/**
 * Description：图片展示页面
 * Author：暄落丶
 * Date：2019/4/30
 */
public class LkaleidoDisplayActivity extends LKaleidoBaseActivity implements LkaleidoGalleryAdapter.OnItemClicklistener {
    public static final String JUMP_DATA_IMAGES = "jumpDataImages";//跳转携带的照片们

    public static final int JUMP_TAG_CROP_IMG = 0xa0;//进入裁剪页面的标记

    private LKaleidoscope mLKaleidoscope;
    private LKaleidoViewPagerAdapter mViewPagerAdapter;
    private LkaleidoGalleryAdapter mGalleryAdapter;

    private ImageView mIvBack;
    private ViewPager mVpShow;
    private RecyclerView rvGallery;
    private TextView tvCrop;
    private TextView tvOk;

    private List<LKaleidoImageBean> imageList = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lkaleido_display);
        mLKaleidoscope = LKaleidoscope.getInstance();
        initView();
        initData();
        initClick();
        initAdapter();

        //如果不需要裁剪，则不显示裁剪按钮
        if (mLKaleidoscope.getCrop()){
            //需要裁剪
            imgType2ShowCrop(0);

            //如果需要裁剪，且只有一张照片，则直接进入裁剪页面
            if (mLKaleidoscope.isRadioMode()){
                //单选
                enterCrop();//进入裁剪功能
            }

        }else {
            //不需要裁剪
            tvCrop.setVisibility(View.GONE);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG,requestCode+"::");
        if (requestCode == UCrop.REQUEST_CROP){
            if (resultCode == Activity.RESULT_OK && data!= null){
                Uri resultUri = UCrop.getOutput(data);

                String resulePath = LKaleidoFileUtils.getAbsoluteImagePath(this,resultUri);
                String resuleAbsolutePath = LKaleidoFileUtils.getFileAbsolutePath(LKaleidoFileUtils.getAbsoluteImagePath(this,resultUri)) ;
                String resuleFileName = LKaleidoFileUtils.getFileAllName(resulePath);

                LKaleidoImageBean resuleImageBean = LKaleidoUtils.photo2Bean(this,resuleAbsolutePath
                        ,resuleFileName);

                Log.e(TAG,resuleImageBean.getPath());

                imageList.set(mVpShow.getCurrentItem(), resuleImageBean);

                mViewPagerAdapter.notifyDataSetChanged();
                mGalleryAdapter.notifyDataSetChanged();


                //如果单选，则直接返回
                if (mLKaleidoscope.isRadioMode()){
                    //携带选中的照片退出
                    selPhotoOver();
                }
            }else {
                //返回到这个页面，如果单张图片，因为单张图片不进入预览页面所以直接关闭本页面
                if (mLKaleidoscope.isRadioMode()){
                    finish();
                }


            }
        }
    }

    private void initView(){
        //沉浸式
        setTranslucentStatus();

        mIvBack = findViewById(R.id.topBar_iv_back);
        mVpShow = findViewById(R.id.display_vp_show);
        rvGallery = findViewById(R.id.display_rv_gallery);
        tvCrop = findViewById(R.id.display_tv_crop);
        tvOk = findViewById(R.id.display_tv_ok);
    }

    private void initData(){
        //接收数据
        if (getIntent().getExtras() != null){
            Bundle getBundle = getIntent().getExtras();
            imageList = getBundle.getParcelableArrayList(JUMP_DATA_IMAGES);
        }

    }

    private void initClick(){
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //裁剪
        tvCrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterCrop();//进入裁剪功能
            }
        });

        //选择完成
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //携带选中的照片退出
                selPhotoOver();
            }
        });

        //viewpage 滑动监听
        mVpShow.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mGalleryAdapter.setSELECT_ITEM(position);

                imgType2ShowCrop(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initAdapter() {
        /********* viewpager 适配器 **********/
        mViewPagerAdapter = new LKaleidoViewPagerAdapter(this,imageList);
        mVpShow.setAdapter(mViewPagerAdapter);
        /********* viewpager 适配器 **********/

        /********* 画廊 适配器 **********/
        mGalleryAdapter = new LkaleidoGalleryAdapter(this,imageList);
        mGalleryAdapter.setOnItemClicklistener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        rvGallery.setLayoutManager(linearLayoutManager);
        rvGallery.setAdapter(mGalleryAdapter);
        /********* 画廊 适配器 **********/

    }


    @Override
    public void OnItemClick(LkaleidoGalleryAdapter.ViewHolder lKaleidoImageBean, int position) {
        mGalleryAdapter.setSELECT_ITEM(position);
        mVpShow.setCurrentItem(position);
    }


    /**
     * 进入裁剪页面
     */
    private void enterCrop(){

        Uri suri = Uri.fromFile(new File(imageList.get(mVpShow.getCurrentItem()).getPath()));
        Uri euri = Uri.fromFile(new File(mLKaleidoscope.getSDPhth()
                +"new"+LKaleidoUtils.makeRandom()+"_"+imageList.get(mVpShow.getCurrentItem()).getName()));

        UCrop.Options options = new UCrop.Options();
        //下面参数分别是缩放,旋转,裁剪框的比例
        //一共三个参数，分别对应裁剪功能页面的“缩放”，“旋转”，“裁剪”界面，对应的传入NONE，就表示关闭了其手势操作，比如这里我关闭了缩放和旋转界面的手势，只留了裁剪页面的手势操作
        if (mLKaleidoscope.isRotate()){
            //需要旋转
            options.setAllowedGestures(UCropActivity.ALL, UCropActivity.ALL, UCropActivity.ALL);
        }else {
            //不旋转
//            options.setAllowedGestures(UCropActivity.ALL, UCropActivity.NONE, UCropActivity.ALL);
        }

        options.setToolbarColor(main_color);// 设置标题栏颜色
        options.setToolbarWidgetColor(font_main_color);//标题字的颜色以及按钮颜色
        options.setToolbarCancelDrawable(R.drawable.ic_topbar_back);
        options.setFreeStyleCropEnabled(mLKaleidoscope.isFreeStyleCrop());////是否能调整裁剪框
        options.setHideBottomControls(true);//隐藏下边控制栏
        options.setCompressionQuality(mLKaleidoscope.getCompressionQuality());///设置裁剪的图片质量，取值0-100

        // TODO: 2019/5/8 圆形这里最后截取的依旧是放行，待解决
        if (mLKaleidoscope.isCircleDimmed()){
            //圆形
            options.setShowCropFrame(false); //设置是否显示裁剪边框(true为方形边框)
            options.setShowCropGrid(false); ////设置是否显示裁剪网格
            options.setCircleDimmedLayer(true);////设置是否为圆形裁剪框
        }else {
            //方形
            options.setShowCropFrame(true); //设置是否显示裁剪边框(true为方形边框)
            options.setShowCropGrid(true); ////设置是否显示裁剪网格
            options.setCircleDimmedLayer(false);////设置是否为圆形裁剪框
        }

        UCrop.of(suri, euri)
                .withAspectRatio(mLKaleidoscope.getOutPutX(), mLKaleidoscope.getOutPutY())
                .withMaxResultSize(1440, 2960)
                .withOptions(options)

                .start(LkaleidoDisplayActivity.this);

    }


    /**
     * 携带选中的照片退出当前页面
     */
    private void selPhotoOver(){
        //携带选中的照片退出
        Intent intentSelOk = new Intent();
        Bundle bundleSelOk = new Bundle();
        bundleSelOk.putParcelableArrayList(JUMP_DATA_IMAGES, (ArrayList<? extends Parcelable>) imageList);
        intentSelOk.putExtras(bundleSelOk);
        setResult(Activity.RESULT_OK,intentSelOk);
        finish();
    }


    /**
     * 根据图片类型判断是否显示裁剪
     */
    private void imgType2ShowCrop(int position){


        //如果是gif 则不显示裁剪
        if (imageList.get(position).getMimeType().equals("image/gif")){
            Log.e(TAG,"隐藏裁剪");
            tvCrop.setVisibility(View.GONE);
        }else {
            Log.e(TAG,"显示裁剪");
            tvCrop.setVisibility(View.VISIBLE);
        }
    }



}
