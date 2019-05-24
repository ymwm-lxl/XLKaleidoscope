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

    public LKaleidoscope setSDPhth(String SDPhth) {
        this.SDPhth = SDPhth;
        return this;
    }

    public int getThemeStyle() {
        return themeStyle;
    }

    public LKaleidoscope setThemeStyle(@StyleRes int themeStyle) {
        this.themeStyle = themeStyle;
        return this;
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

    public LKaleidoscope setRadioMode(boolean radioMode) {
        this.radioMode = radioMode;
        return this;
    }


    public boolean isGifShow() {
        return gifShow;
    }

    public LKaleidoscope setGifShow(boolean gifShow) {
        this.gifShow = gifShow;
        return this;
    }

    public int getSelectLimit() {
        return selectLimit;
    }

    public LKaleidoscope setSelectLimit(int selectLimit) {
        this.selectLimit = selectLimit;
        return this;
    }

    public int getSelectMinLimit() {
        return selectMinLimit;
    }

    public LKaleidoscope setSelectMinLimit(int selectMinLimit) {
        this.selectMinLimit = selectMinLimit;
        return this;
    }

    public boolean getCrop() {
        return crop;
    }

    public LKaleidoscope setCrop(boolean crop) {
        this.crop = crop;
        return this;
    }

    public boolean isDisplay() {
        return display;
    }

    public LKaleidoscope setDisplay(boolean display) {
        this.display = display;
        return this;
    }

    public int getSpanCountLimit() {
        return spanCountLimit;
    }

    public LKaleidoscope setSpanCountLimit(int spanCountLimit) {
        this.spanCountLimit = spanCountLimit;
        return this;
    }

    public boolean isShowCamera() {
        // TODO: 2019/4/28 多選是否還顯示相機？ 
        return showCamera;
    }

    public LKaleidoscope setShowCamera(boolean showCamera) {
        this.showCamera = showCamera;
        return this;
    }

    public float getOutPutX() {
        return outPutX;
    }

    public LKaleidoscope setOutPutX(float outPutX) {
        this.outPutX = outPutX;
        return this;
    }

    public float getOutPutY() {
        return outPutY;
    }

    public LKaleidoscope setOutPutY(float outPutY) {
        this.outPutY = outPutY;
        return this;
    }

    public boolean isFreeStyleCrop() {
        return freeStyleCrop;
    }

    public LKaleidoscope setFreeStyleCrop(boolean freeStyleCrop) {
        this.freeStyleCrop = freeStyleCrop;
        return this;
    }

    public int getCompressionQuality() {
        //范围只能是0~100
        if (CompressionQuality <= 100 &&CompressionQuality >= 0){

            return CompressionQuality;
        }else {
            return 100;
        }
    }

    public LKaleidoscope setCompressionQuality(int compressionQuality) {
        //范围只能是0~100
        if (compressionQuality <= 100 &&compressionQuality >= 0){

            CompressionQuality = compressionQuality;
        }else {
            CompressionQuality = 100;
        }

        return this;
    }

    public boolean isCircleDimmed() {
        return circleDimmed;
    }

    public LKaleidoscope setCircleDimmed(boolean circleDimmed) {
        this.circleDimmed = circleDimmed;
        return this;
    }

    public boolean isRotate() {
        return rotate;
    }

    public LKaleidoscope setRotate(boolean rotate) {
        this.rotate = rotate;
        return this;
    }

    public int getIcCameraBack() {
        return icCameraBack;
    }

    public LKaleidoscope setIcCameraBack(int icCameraBack) {
        this.icCameraBack = icCameraBack;
        return this;
    }

    public int getIcCameraTake() {
        return icCameraTake;
    }

    public LKaleidoscope setIcCameraTake(int icCameraTake) {
        this.icCameraTake = icCameraTake;
        return this;
    }

    public int getIcCameraFlip() {
        return icCameraFlip;
    }

    public LKaleidoscope setIcCameraFlip(int icCameraFlip) {
        this.icCameraFlip = icCameraFlip;
        return this;
    }

    public int getIcCameraDisagree() {
        return icCameraDisagree;
    }

    public LKaleidoscope setIcCameraDisagree(int icCameraDisagree) {
        this.icCameraDisagree = icCameraDisagree;
        return this;
    }

    public int getIcCameraAgree() {
        return icCameraAgree;
    }

    public LKaleidoscope setIcCameraAgree(int icCameraAgree) {
        this.icCameraAgree = icCameraAgree;
        return this;
    }

    public LKaleidoImageLoader getImageLoader() {
        return imageLoader;
    }

    public LKaleidoscope setImageLoader(LKaleidoImageLoader imageLoader) {
        this.imageLoader = imageLoader;
        return this;
    }

    public LKaleidoToastShow getToastShow() {
        return mToastShow;
    }

    public LKaleidoscope setToastShow(LKaleidoToastShow toastShow) {
        mToastShow = toastShow;
        return this;
    }

    public List<LKaleidoImageFolder> getImageFolders() {
        return mImageFolders;
    }

    public LKaleidoscope setImageFolders(List<LKaleidoImageFolder> imageFolders) {
        mImageFolders = imageFolders;
        return this;
    }

}
