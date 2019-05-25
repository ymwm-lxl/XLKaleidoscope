package com.xuanluo.lkaleidoscope.ui;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.hardware.Camera.CameraInfo;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.xuanluo.lkaleidoscope.LKaleidoscope;
import com.xuanluo.lkaleidoscope.R;
import com.xuanluo.lkaleidoscope.bean.LKaleidoImageBean;
import com.xuanluo.lkaleidoscope.util.LKaleidoUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Policy;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

/**
 * Description：
 * Author：暄落丶
 * Date：2019/4/28
 */
public class LKaleidoCameraActivity extends LKaleidoBaseActivity implements SurfaceHolder.Callback, View.OnClickListener {

    public static final String JUMP_DATA_CAMERA_IMG = "jumpDataCameraImg";//

    private Camera mCamera;
    private SurfaceHolder mSurfaceHolder;
    private LKaleidoscope mLKaleidoscope;

    private SurfaceView mSurfaceView;
    private CardView mLlShow;
    private ImageView imgBack;
    private ImageView imgSwitch;
    private ImageView imgTakeCamera;
    private ImageView imgShow;
    private ImageView mImgShowClose;
    private ImageView mImgShowSave;

    private boolean isOpenCamera;
    private int cameraPosition = 1;//0代表前置摄像头，1代表后置摄像头
    private Bitmap mBitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lkaleido_camera);
        mLKaleidoscope = LKaleidoscope.getInstance();
        initView();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isOpenCamera){
            initCamera();
        }else {
            Log.e(TAG,"没有相机");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initView() {

        imgBack = findViewById(R.id.camera_img_back);
        imgSwitch = findViewById(R.id.camera_img_switch);
        imgTakeCamera = findViewById(R.id.camera_img_takeCamera);

        mLlShow = findViewById(R.id.camera_cv_show);
        imgShow = findViewById(R.id.camera_img_show);
        mImgShowClose = findViewById(R.id.camera_img_closeShow);
        mImgShowSave = findViewById(R.id.camera_img_saveShow);

        imgBack.setOnClickListener(this);
        imgSwitch.setOnClickListener(this);
        imgTakeCamera.setOnClickListener(this);
        mImgShowClose.setOnClickListener(this);
        mImgShowSave.setOnClickListener(this);
        mLlShow.setOnClickListener(this);

        //更换图标
        imgBack.setImageResource(mLKaleidoscope.getIcCameraBack());
        imgTakeCamera.setImageResource(mLKaleidoscope.getIcCameraTake());
        imgSwitch.setImageResource(mLKaleidoscope.getIcCameraFlip());
        mImgShowClose.setImageResource(mLKaleidoscope.getIcCameraDisagree());
        mImgShowSave.setImageResource(mLKaleidoscope.getIcCameraAgree());

    }

    private void initData() {
        isOpenCamera = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    //相机初始化
    private void initCamera() {
        mSurfaceView = findViewById(R.id.camera_surfaceview);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);//surfaceview不维护自己的缓冲区，等待屏幕渲染引擎将内容推送到用户面前
    }


    /**
     * 切换前后摄像头
     */
    public void switchCamera() {

        if (mCamera == null){
            mLKaleidoscope.getToastShow().showLkToast(this,getString(R.string.camera_error));
            return;
        }

        if(cameraPosition == 1) {
            //现在是后置，变更为前置
            cameraStopPreview(false);
            cameraPosition = 0;
            cameraSelectPreview();
        } else {
            //现在是前置， 变更为后置
            cameraStopPreview(false);
            cameraPosition = 1;
            cameraSelectPreview();
        }
    }

    //SurfaceHolder.Callback,这是个holder用来显示surfaceView 数据的接口,他必须实现以下3个方法
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        cameraSelectPreview();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        if (mCamera != null){
            mCamera.autoFocus(null);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.e(TAG,"释放相机");
        cameraStopPreview(true);
    }

    @Override
    public void onClick(View v) {
        int vId = v.getId();
        if (vId == R.id.camera_img_back) {
            //返回按钮
            finish();
        }else if (vId == R.id.camera_img_switch){
            //摄像头切换
            switchCamera();
        }else if (vId == R.id.camera_img_takeCamera){
            //快门
            if (!isFastClick()){
                cameraTakePicture();
            }
        }else if (vId == R.id.camera_img_closeShow){
            //重新拍摄
            mLlShow.setVisibility(View.GONE);
            cameraSelectPreview();
            mBitmap.recycle();
        }else if (vId == R.id.camera_img_saveShow){
            savePhoto();
        }else if (vId == R.id.camera_cv_show){
            //拦截卡片上面的点击事件，避免让用户点击到页面
        }

    }

    private void savePhoto(){
        //确定保存
        String imgFileName = LKaleidoUtils.savePhoto2SD(this,mBitmap,mLKaleidoscope.getSDPhth());//存储并获取文件名

        if (!imgFileName.isEmpty()){
            Intent intentSave = new Intent();
            Bundle bundleSave = new Bundle();
            ArrayList<LKaleidoImageBean> imageBeans = new ArrayList<>();
            LKaleidoImageBean imageBean = LKaleidoUtils.photo2Bean(this,mLKaleidoscope.getSDPhth(),imgFileName);
            imageBeans.add(imageBean);

            bundleSave.putParcelableArrayList(JUMP_DATA_CAMERA_IMG,imageBeans);
            intentSave.putExtras(bundleSave);
            setResult(Activity.RESULT_OK,intentSave);
            finish();
        }
    }

    /**
     * 选择相机
     */
    private void cameraSelectPreview(){
        //切换前后摄像头
        int cameraCount = 0;
        CameraInfo cameraInfo = new CameraInfo();
        cameraCount = Camera.getNumberOfCameras();//得到摄像头的个数
        for(int i = 0; i < cameraCount; i++){
            Camera.getCameraInfo(i, cameraInfo);//得到每一个摄像头的信息

            if (cameraPosition == 1){
                //打开后置
                if(cameraInfo.facing  == Camera.CameraInfo.CAMERA_FACING_BACK) {//代表摄像头的方位，CAMERA_FACING_FRONT前置      CAMERA_FACING_BACK后置
                    cameraStartPreview(i,90);
                }
            }else {
                //打开前置
                if(cameraInfo.facing  == Camera.CameraInfo.CAMERA_FACING_FRONT) {//代表摄像头的方位，CAMERA_FACING_FRONT前置      CAMERA_FACING_BACK后置
                    cameraStartPreview(i,270);
                }
            }

        }
    }

    /**
     * 创建相机
     * @param cameraId 相机id
     * @param rotation 旋转角度
     */
    private void cameraStartPreview(int cameraId,int rotation){

        //当surfaceview创建时开启相机
        mCamera = Camera.open(cameraId);

        Camera.Parameters parameters = mCamera.getParameters();
        // 选择合适的预览尺寸
        int PreviewWidth = 0;
        int PreviewHeight = 0;
        List<Camera.Size> sizeList = parameters.getSupportedPreviewSizes();
//        List<Camera.Size> sizeList = parameters.getSupportedPreviewSizes();

        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(outMetrics);
        int widthPixel = outMetrics.widthPixels;
        int heightPixel = outMetrics.heightPixels;

        Camera.Size cur2 = getBestSupportedSize(sizeList,heightPixel,widthPixel);
                PreviewWidth = cur2.width;
                PreviewHeight = cur2.height;



        //设置
        parameters.setPreviewSize(PreviewWidth, PreviewHeight);
        parameters.setPictureSize(PreviewWidth, PreviewHeight);
        parameters.setRotation(rotation);

        Log.e(TAG,"相机尺寸：PreviewWidth"+PreviewWidth +"，PreviewHeight"+PreviewHeight);

        mCamera.setParameters(parameters);

        try {
            mCamera.setPreviewDisplay(mSurfaceHolder);

            mCamera.setDisplayOrientation(90);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 自动对焦
        mCamera.startPreview();
        mCamera.autoFocus(null);
    }

    /**
     * 获取最合适的分辨率
     */
    private Size getBestSupportedSize(List<Size> sizes, int width, int height) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) width / height;
        if (sizes == null) return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = height;

        // Try to find an size match aspect ratio and size
        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        // Cannot find the one match the aspect ratio, ignore the requirement
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

    /**
     * 释放资源
     */
    private void cameraStopPreview(boolean isAllStop){

        if (mCamera != null){
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }


        if (isAllStop){
            mSurfaceHolder = null;
            mSurfaceView = null;
        }
    }

    //拍照结束
    private void cameraTakePicture(){
        if (mCamera == null){
            mLKaleidoscope.getToastShow().showLkToast(this,getString(R.string.camera_error));
            return;
        }

        mCamera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {

                //旋转后再展示
                mBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

                //耗时操作在子线程中进行
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (mBitmap.getHeight() < mBitmap.getWidth()){
                            if (cameraPosition == 0){

                                mBitmap = rotaingImageView(270,mBitmap);
                            }else {

                                mBitmap = rotaingImageView(90,mBitmap);
                            }
                        }

                        //如果是前置，则翻转照片
                        if (cameraPosition == 0){
                            mBitmap = turnCurrentLayer(mBitmap,-1,1);
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mLlShow.setVisibility(View.VISIBLE);
                                imgShow.setImageBitmap(mBitmap);
                            }
                        });


                    }
                }).start();




//                bitmap.recycle();//回收bitmap空间


            }
        });
    }


    /**
     * 翻转bitmap (-1,1)左右翻转  (1,-1)上下翻转
     * @param srcBitmap
     * @param sx
     * @param sy
     * @return
     */
    public Bitmap turnCurrentLayer(Bitmap srcBitmap,float sx,float sy){
        Bitmap cacheBitmap = Bitmap.createBitmap(srcBitmap.getWidth(), srcBitmap.getHeight(), Bitmap.Config.ARGB_8888);// 创建缓存像素的位图
        int w = cacheBitmap .getWidth();
        int h=cacheBitmap.getHeight();

        Canvas cv = new Canvas(cacheBitmap );//使用canvas在bitmap上面画像素

        Matrix mMatrix = new Matrix();//使用矩阵 完成图像变换

        mMatrix.postScale(sx, sy);//重点代码，记住就ok

        Bitmap resultBimtap= Bitmap.createBitmap(srcBitmap, 0, 0, w, h, mMatrix, true);
        cv.drawBitmap(resultBimtap,
                new Rect(0, 0, srcBitmap.getWidth(), srcBitmap.getHeight()),
                new Rect(0, 0, w, h), null);
        return resultBimtap;
    }


    public  Bitmap rotaingImageView(int angle , Bitmap bitmap) {
        //旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }

}
