package com.xuanluo.lkaleidoscope;

import android.content.Context;
import android.os.Environment;
import android.util.TypedValue;

import com.xuanluo.lkaleidoscope.bean.LKaleidoImageBean;
import com.xuanluo.lkaleidoscope.bean.LKaleidoImageFolder;
import com.xuanluo.lkaleidoscope.loader.LKaleidoImageLoader;
import com.xuanluo.lkaleidoscope.loader.LKaleidoToastShow;

import java.io.File;
import java.util.List;

import androidx.annotation.StyleRes;

/**
 * Description：图片选择入口类
 * Author：暄落丶
 * Date：2019/4/27
 */
// TODO: 2019/4/27 改为链式调用
public class LKaleidoscope {
    public static final String TAG = LKaleidoscope.class.getSimpleName();

    public static final String DEFAULT_SD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() +"/LKaleidoscope/Camera/";

    private String SDPhth = DEFAULT_SD_PATH;//设置拍摄图片存储路径
    private int themeStyle = R.style.LkTheme;//默认属性
    private boolean radioMode = false; //是否单选
    private boolean gifShow = true; //是否显示gif
    private int selectLimit = 9;         //最大选择图片数量
    private int selectMinLimit = 1;         //最小选择图片数量 //如果显示相机，则无法控制
    private boolean crop = true;         //裁剪
    private boolean display = true;    //是否预览
    private int spanCountLimit = 3;         //圖片列數
    private boolean showCamera = true;   //显示相机
    private float outPutX = 3;//宽度比例
    private float outPutY = 4;//高度比例
    private boolean freeStyleCrop = true;//是否能够拖动裁剪框（比例自定）
    private int CompressionQuality = 100;// //设置裁剪的图片质量，取值0-100
    private boolean circleDimmed = false;//是否要圆形，默认不要
    private boolean rotate = false;//是否可以旋转，默认不可以

    private int icCameraBack = R.drawable.ic_camera_back;
    private int icCameraTake = R.drawable.ic_camera_take;
    private int icCameraFlip = R.drawable.ic_camera_flip;
    private int icCameraDisagree = R.drawable.ic_camera_disagree;
    private int icCameraAgree = R.drawable.ic_camera_agree;

    private LKaleidoImageLoader imageLoader;     //图片加载器
    private LKaleidoToastShow mToastShow;     //toast 显示仪

    private List<LKaleidoImageFolder> mImageFolders;      //所有的图片文件夹

    private static LKaleidoscope mInstance;

    public LKaleidoscope() {
    }

    public static LKaleidoscope getInstance(){
        if (mInstance == null){
            synchronized (LKaleidoscope.class){
                if (mInstance == null){
                    mInstance = new LKaleidoscope();
                }
            }
        }
        return mInstance;
    }

    public String getSDPhth() {
        //生成路径
        File appDir = new File(SDPhth);
        if (!appDir.exists()) {
            appDir.mkdirs();
        }
        return SDPhth;
    }

    public void setSDPhth(String SDPhth) {
        this.SDPhth = SDPhth;
    }

    public int getThemeStyle() {
        return themeStyle;
    }

    public void setThemeStyle(@StyleRes int themeStyle) {
        this.themeStyle = themeStyle;
    }

    public boolean isRadioMode() {
        //如果图片数量为1 则为单选
        if (getSelectLimit() == 1){
            radioMode =  true;
        }else {
            radioMode = false;
        }
        return radioMode;
    }

//    public void setRadioMode(boolean radioMode) {
//        this.radioMode = radioMode;
//    }


    public boolean isGifShow() {
        return gifShow;
    }

    public void setGifShow(boolean gifShow) {
        this.gifShow = gifShow;
    }

    public int getSelectLimit() {
        return selectLimit;
    }

    public void setSelectLimit(int selectLimit) {
        this.selectLimit = selectLimit;
    }

    public int getSelectMinLimit() {
        return selectMinLimit;
    }

    public void setSelectMinLimit(int selectMinLimit) {
        this.selectMinLimit = selectMinLimit;
    }

    public boolean getCrop() {
        return crop;
    }

    public void setCrop(boolean crop) {
        this.crop = crop;
    }

    public boolean isDisplay() {
        return display;
    }

    public void setDisplay(boolean display) {
        this.display = display;
    }

    public int getSpanCountLimit() {
        return spanCountLimit;
    }

    public void setSpanCountLimit(int spanCountLimit) {
        this.spanCountLimit = spanCountLimit;
    }

    public boolean isShowCamera() {
        // TODO: 2019/4/28 多選是否還顯示相機？ 
        return showCamera;
    }

    public void setShowCamera(boolean showCamera) {
        this.showCamera = showCamera;
    }

    public float getOutPutX() {
        return outPutX;
    }

    public void setOutPutX(float outPutX) {
        this.outPutX = outPutX;
    }

    public float getOutPutY() {
        return outPutY;
    }

    public void setOutPutY(float outPutY) {
        this.outPutY = outPutY;
    }

    public boolean isFreeStyleCrop() {
        return freeStyleCrop;
    }

    public void setFreeStyleCrop(boolean freeStyleCrop) {
        this.freeStyleCrop = freeStyleCrop;
    }

    public int getCompressionQuality() {
        //范围只能是0~100
        if (CompressionQuality <= 100 &&CompressionQuality >= 0){

            return CompressionQuality;
        }else {
            return 100;
        }
    }

    public void setCompressionQuality(int compressionQuality) {
        //范围只能是0~100
        if (compressionQuality <= 100 &&compressionQuality >= 0){

            CompressionQuality = compressionQuality;
        }else {
            CompressionQuality = 100;
        }
    }

    public boolean isCircleDimmed() {
        return circleDimmed;
    }

    public void setCircleDimmed(boolean circleDimmed) {
        this.circleDimmed = circleDimmed;
    }

    public boolean isRotate() {
        return rotate;
    }

    public void setRotate(boolean rotate) {
        this.rotate = rotate;
    }

    public int getIcCameraBack() {
        return icCameraBack;
    }

    public void setIcCameraBack(int icCameraBack) {
        this.icCameraBack = icCameraBack;
    }

    public int getIcCameraTake() {
        return icCameraTake;
    }

    public void setIcCameraTake(int icCameraTake) {
        this.icCameraTake = icCameraTake;
    }

    public int getIcCameraFlip() {
        return icCameraFlip;
    }

    public void setIcCameraFlip(int icCameraFlip) {
        this.icCameraFlip = icCameraFlip;
    }

    public int getIcCameraDisagree() {
        return icCameraDisagree;
    }

    public void setIcCameraDisagree(int icCameraDisagree) {
        this.icCameraDisagree = icCameraDisagree;
    }

    public int getIcCameraAgree() {
        return icCameraAgree;
    }

    public void setIcCameraAgree(int icCameraAgree) {
        this.icCameraAgree = icCameraAgree;
    }

    public LKaleidoImageLoader getImageLoader() {
        return imageLoader;
    }

    public void setImageLoader(LKaleidoImageLoader imageLoader) {
        this.imageLoader = imageLoader;
    }

    public LKaleidoToastShow getToastShow() {
        return mToastShow;
    }

    public void setToastShow(LKaleidoToastShow toastShow) {
        mToastShow = toastShow;
    }

    public List<LKaleidoImageFolder> getImageFolders() {
        return mImageFolders;
    }

    public void setImageFolders(List<LKaleidoImageFolder> imageFolders) {
        mImageFolders = imageFolders;
    }

}
