package com.xuanluo.lkaleidoscope.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xuanluo.lkaleidoscope.LKaleidoImageDataSource;
import com.xuanluo.lkaleidoscope.LKaleidoscope;
import com.xuanluo.lkaleidoscope.R;
import com.xuanluo.lkaleidoscope.adapter.LKaleidoGridShowAdapter;
import com.xuanluo.lkaleidoscope.adapter.LKaleidoImageFolderAdapter;
import com.xuanluo.lkaleidoscope.bean.LKaleidoImageBean;
import com.xuanluo.lkaleidoscope.bean.LKaleidoImageFolder;
import com.xuanluo.lkaleidoscope.loader.LKaleidoToastShow;
import com.xuanluo.lkaleidoscope.util.LKaleidoUtils;
import com.xuanluo.lkaleidoscope.view.FolderPopUpWindow;
import com.xuanluo.lkaleidoscope.view.GridSpacingItemDecoration;

import java.text.Format;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

/**
 * Description：图片展示暨选择页面
 * Author：暄落丶
 * Date：2019/4/27
 */
public class LKaleidoGridShowActivity extends LKaleidoBaseActivity
        implements LKaleidoImageDataSource.OnImagesLoadedListener, LKaleidoImageFolderAdapter.OnItemClicklistener
        , LKaleidoGridShowAdapter.OnItemClicklistener {

    private static final int JUMP_TAG_DISPLAY_IMG = 0xa0;//进入图片预览页面的标记
    private static final int JUMP_TAG_CAMERA = 0xa1;//进入拍摄预览页面的标记

    public static final String JUMP_DATA_SEL_OK_IMG = "jumpDataSelOkImg";//选择的图片
    public static final String LKALEIDO_DATA_IS_CAMERA = "LKaleidoDataIsCamera";//选择的图片

    private LKaleidoscope mLKaleidoscope;

    private LKaleidoGridShowAdapter mShowAdapter;
    private LKaleidoImageFolderAdapter mFolderAdapter;
    private LKaleidoImageDataSource lKaleidoImageDataSource;

    private ImageView mIvBack;
    private TextView tvSelFolder;
    private RecyclerView rvImage;
    private TextView tvOver;
    private FolderPopUpWindow folderPopUpWindow;
    private LinearLayout llTopBar;

    private List<LKaleidoImageFolder> imageFolderList = new ArrayList<>();
    private boolean isCamera = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lkaleido_grid_show);
        mLKaleidoscope = LKaleidoscope.getInstance();
        initView();
        initData();
        initAdapter();
        initClick();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case JUMP_TAG_CAMERA:
                if (resultCode == Activity.RESULT_OK){
                    if (data != null){

                        //拍照成功
                        ArrayList<LKaleidoImageBean> imageBeans = data.getExtras().getParcelableArrayList(LKaleidoCameraActivity.JUMP_DATA_CAMERA_IMG);

                        if (mLKaleidoscope.isDisplay()){
                            //预览
                            enterDisplay(imageBeans);//进入预览页面
                        }else {
                            //不预览 直接返回
                            selImgOkEsc(imageBeans);
                        }
                    }
                }else {
                    if (isCamera){
                        //只拍照
                        finish();
                    }
                }
                break;
            case JUMP_TAG_DISPLAY_IMG:
                if (resultCode == Activity.RESULT_OK){
                    //预览页面确定之后
                    if (data!= null){
                        ArrayList<LKaleidoImageBean> imageBeans = data.getExtras().getParcelableArrayList(LkaleidoDisplayActivity.JUMP_DATA_IMAGES);
                        selImgOkEsc(imageBeans);
                    }
                }else {
                    lKaleidoImageDataSource.reLoader();
                    if (isCamera){
                        //只拍照
                        finish();
                    }
                }

                break;
        }
        if (requestCode == JUMP_TAG_CAMERA ){


        }else if (requestCode == JUMP_TAG_DISPLAY_IMG && resultCode == Activity.RESULT_OK){

        }
    }

    private void initView() {
        //沉浸式

        mIvBack = findViewById(R.id.topBar_iv_back);
        tvSelFolder = findViewById(R.id.topBar_tv_folderSel);
        rvImage = findViewById(R.id.gridShow_rv_images);
        tvOver = findViewById(R.id.gridShow_tv_over);

        tvSelFolder.setVisibility(View.VISIBLE);

        llTopBar = findViewById(R.id.gridShow_topBar);

    }

    private void initClick() {
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvSelFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFolderWindow();
            }
        });
        tvOver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //必须选择图片
                if (mShowAdapter.getSelImages().size() < mLKaleidoscope.getSelectMinLimit()){
                    mLKaleidoscope.getToastShow().showLkToast(LKaleidoGridShowActivity.this
                            ,"请至少选择 "+mLKaleidoscope.getSelectMinLimit()+" 张图片");
                }else {
                    if (mLKaleidoscope.isDisplay()){
                        //预览
                        enterDisplay(mShowAdapter.getSelImages());//进入预览页面
                    }else {
                        //不预览
                        selImgOkEsc(mShowAdapter.getSelImages());
                    }
                }
            }
        });

    }

    private void initData() {
        //获取传递过来的数据
        if (getIntent() != null && getIntent().getExtras() != null){
            Bundle getBundle = getIntent().getExtras();
            isCamera = getBundle.getBoolean(LKALEIDO_DATA_IS_CAMERA,false);
        }

        //判断是不是只拍照
        if (isCamera){
            //进入拍照页面即可
            enterCamera();
        }else {
            //不是只需要拍照功能
            lKaleidoImageDataSource = new LKaleidoImageDataSource(this, null, this);
        }
    }

    private void initAdapter() {

        /***************** 图片展示 *********************/
        mShowAdapter = new LKaleidoGridShowAdapter(this, new ArrayList<LKaleidoImageBean>());
        mShowAdapter.setOnItemClicklistener(this);
        rvImage.setLayoutManager(new GridLayoutManager(this, mLKaleidoscope.getSpanCountLimit()));
        rvImage.addItemDecoration(new GridSpacingItemDecoration(mLKaleidoscope.getSpanCountLimit(), LKaleidoUtils.dp2px(this, 2), false));
        ((SimpleItemAnimator)rvImage.getItemAnimator()).setSupportsChangeAnimations(false);
        rvImage.setAdapter(mShowAdapter);
        /***************** 图片展示 *********************/


        /***************** 文件夹展示 *********************/
        mFolderAdapter = new LKaleidoImageFolderAdapter(this, imageFolderList);
        mFolderAdapter.setOnItemClicklistener(this);
        /***************** 文件夹展示 *********************/

    }

    //图片数据获取完成
    @Override
    public void onImagesLoaded(final List<LKaleidoImageFolder> imageFolders) {
        imageFolderList.clear();
        imageFolderList.addAll(imageFolders);

        if (mShowAdapter != null && imageFolderList.size() > 0) {

            //如果显示相机，则每个文件目录先增加一个空数据用于显示相机
            if (mLKaleidoscope.isShowCamera()){
                for (int i = 0; i < imageFolderList.size(); i++) {
                    //顯示相機
                    LKaleidoImageBean cameraImageBean = new LKaleidoImageBean();
                    imageFolderList.get(i).getImages().add(0,cameraImageBean);
                }
            }

            tvSelFolder.setText(imageFolderList.get(0).getName());
            mShowAdapter.refreshData(imageFolderList.get(0).getImages());
        }



    }

    private void setFolderWindow() {

        if (imageFolderList == null || imageFolderList.size() == 0) {
//            Toast.makeText(this,"您的手机没有图片",Toast.LENGTH_SHORT).show();
            Log.i(TAG, "您的手机没有图片");
            return;
        }

        VectorDrawableCompat folderBGDrawableCompat = VectorDrawableCompat.create(getResources(),R.drawable.bg_folder_sel_color,getTheme());
        folderBGDrawableCompat.setTint(two_color_transparent);
        folderPopUpWindow = new FolderPopUpWindow(this, mFolderAdapter);

        folderPopUpWindow.showAsDropDown(tvSelFolder, 0, 0);


    }

    //选择文件列表点击事件
    @Override
    public void OnItemClick(LKaleidoImageFolderAdapter.ViewHolder viewHolder, LKaleidoImageFolder lKaleidoImageFolder, int position) {
        mFolderAdapter.setSelectItem(position);
        if (mShowAdapter != null) {
            tvSelFolder.setText(imageFolderList.get(mFolderAdapter.getSelectItem()).getName());
            mShowAdapter.refreshData(imageFolderList.get(mFolderAdapter.getSelectItem()).getImages());
        }
        folderPopUpWindow.dismiss();
    }

    //选择图片点击事件
    @Override
    public void OnItemClick(LKaleidoImageBean lKaleidoImageBean, int position) {
        // 2019/5/8 如果需要裁剪，则直接进入裁剪页面，不需要的话进入预览页面
        if (mLKaleidoscope.isRadioMode()){
            //单选
            ArrayList<LKaleidoImageBean> imageBeans  = new ArrayList<>();
            imageBeans.add(lKaleidoImageBean);

            if (mLKaleidoscope.isDisplay()){
                //预览
                enterDisplay(imageBeans);//进入预览页面
            }else {
                //不预览
                selImgOkEsc(imageBeans);
            }
        }else {
            //多选
            mShowAdapter.setSelImages(lKaleidoImageBean,position);
        }

    }

    //點擊拍攝照片
    @Override
    public void OnItemCamera() {
        enterCamera();
    }

    //选择的图片数量
    @Override
    public void OnItemSelSize(int size) {

        if (size == 0) {
            tvOver.setText(getString(R.string.over));
        } else {
            String overTxt = String.format(getString(R.string.over_show_size), size + "");
            tvOver.setText(overTxt);
        }

    }

    //進入拍攝頁面
    private void enterCamera() {
        Intent intentCamera = new Intent(LKaleidoGridShowActivity.this, LKaleidoCameraActivity.class);
        startActivityForResult(intentCamera,JUMP_TAG_CAMERA);
    }

    //進入預覽頁面
    private void enterDisplay(ArrayList<LKaleidoImageBean> imageBeans){
        Intent intentDisplay = new Intent(LKaleidoGridShowActivity.this,LkaleidoDisplayActivity.class);
        Bundle bundleDisplay = new Bundle();
        bundleDisplay.putParcelableArrayList(LkaleidoDisplayActivity.JUMP_DATA_IMAGES,imageBeans);
        intentDisplay.putExtras(bundleDisplay);
        startActivityForResult(intentDisplay,JUMP_TAG_DISPLAY_IMG);
    }

    /**
     * 选择图片完成返回
     */
    private void selImgOkEsc(ArrayList<LKaleidoImageBean> selImageBeans){
        Intent intentGoHome = new Intent();
        Bundle bundleGoHome = new Bundle();
        bundleGoHome.putParcelableArrayList(JUMP_DATA_SEL_OK_IMG,selImageBeans);
        intentGoHome.putExtras(bundleGoHome);
        setResult(Activity.RESULT_OK,intentGoHome);
        finish();
    }

}
