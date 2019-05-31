package com.xuanluo.lkaleidoscope;

import android.content.Context;
import android.os.Environment;
import android.util.TypedValue;

import com.xuanluo.lkaleidoscope.bean.LKaleidoImageBean;
import com.xuanluo.lkaleidoscope.bean.LKaleidoImageFolder;
import com.xuanluo.lkaleidoscope.loader.LKaleidoImageLoader;
import com.xuanluo.lkaleidoscope.loader.LKaleidoToastShow;
import com.xuanluo.lkaleidoscope.loader.ToastShow;

import java.io.File;
import java.util.List;

import androidx.annotation.StyleRes;

/**
 * Description：图片选择入口类
 * Author：暄落丶
 * Date：2019/4/27
 */
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
    private LKaleidoToastShow mToastShow = new ToastShow();     //toast 显示仪

    private List<LKaleidoImageFolder> mImageFolders;      //所有的图片文件夹

    private static LKaleidoscope mInstance;

    public LKaleidoscope() {
    }

    /**
     * 获取配置器
     * @return 万花筒 配置器
     */
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

    /**
     * 获取 sd 卡存储路径
     * @return SD卡存储路径
     */
    public String getSDPhth() {
        //生成路径
        File appDir = new File(SDPhth);
        if (!appDir.exists()) {
            appDir.mkdirs();
        }
        return SDPhth;
    }

    /**
     * 设置 SD 卡存储路径
     * @param SDPhth SD卡存储路径
     * @return 万花筒 配置器
     */
    public LKaleidoscope setSDPhth(String SDPhth) {
        this.SDPhth = SDPhth;
        return this;
    }

    /**
     * 获取风格
     * @return
     */
    public int getThemeStyle() {
        return themeStyle;
    }

    /**
     * 设置风格
     * @param themeStyle style
     * @return
     */
    public LKaleidoscope setThemeStyle(@StyleRes int themeStyle) {
        this.themeStyle = themeStyle;
        return this;
    }

    /**
     * 获取是否为单选
     * @return
     */
    public boolean isRadioMode() {
        //如果图片数量为1 则为单选
//        if (getSelectLimit() == 1){
//            radioMode =  true;
//        }else {
//            radioMode = false;
//        }
        return radioMode;
    }

    /**
     * 设置是否单选（如果图片数量为1 则为单选）
     * @param radioMode
     * @return
     */
    public LKaleidoscope setRadioMode(boolean radioMode) {
        this.radioMode = radioMode;
        return this;
    }


    /**
     * 是否显示gif
     * @return
     */
    public boolean isGifShow() {
        return gifShow;
    }

    /**
     * 设置是否显示gif
     * @param gifShow 是否显示gif
     * @return
     */
    public LKaleidoscope setGifShow(boolean gifShow) {
        this.gifShow = gifShow;
        return this;
    }

    /**
     * 获取最大选择图片数量
     * @return
     */
    public int getSelectLimit() {
        return selectLimit;
    }

    /**
     * 设置最大选择图片数量
     * @param selectLimit 最大选择图片数量
     * @return
     */
    public LKaleidoscope setSelectLimit(int selectLimit) {
        this.selectLimit = selectLimit;
        return this;
    }

    /**
     * 获取最小选择图片数量 //如果显示相机，则无法控制
     * @return
     */
    public int getSelectMinLimit() {
        return selectMinLimit;
    }

    /**
     * 设置最小选择图片数量 //如果显示相机，则无法控制
     * @param selectMinLimit
     * @return
     */
    public LKaleidoscope setSelectMinLimit(int selectMinLimit) {
        this.selectMinLimit = selectMinLimit;
        return this;
    }

    /**
     * 获取是否裁剪
     * @return
     */
    public boolean getCrop() {
        return crop;
    }

    /**
     * 设置是否裁剪
     * @param crop
     * @return
     */
    public LKaleidoscope setCrop(boolean crop) {
        this.crop = crop;
        return this;
    }

    /**
     * 是否进入预览页面
     * @return
     */
    public boolean isDisplay() {
        return display;
    }

    /**
     * 设置是否进入预览页面
     * @param display
     * @return
     */
    public LKaleidoscope setDisplay(boolean display) {
        this.display = display;
        return this;
    }

    /**
     * 获取相册展示的图片列数
     * @return
     */
    public int getSpanCountLimit() {
        return spanCountLimit;
    }

    /**
     * 设置相册展示的图片列数
     * @param spanCountLimit
     * @return
     */
    public LKaleidoscope setSpanCountLimit(int spanCountLimit) {
        this.spanCountLimit = spanCountLimit;
        return this;
    }

    /**
     * 是否显示拍照按钮
     * @return
     */
    public boolean isShowCamera() {
        return showCamera;
    }

    /**
     * 设置是否显示拍照按钮
     * @param showCamera
     * @return
     */
    public LKaleidoscope setShowCamera(boolean showCamera) {
        this.showCamera = showCamera;
        return this;
    }

    /**
     * 获取宽度比例
     * @return
     */
    public float getOutPutX() {
        return outPutX;
    }

    /**
     * 设置宽度比例
     * @param outPutX
     * @return
     */
    public LKaleidoscope setOutPutX(float outPutX) {
        this.outPutX = outPutX;
        return this;
    }

    /**
     * 设置高度比例
     * @return
     */
    public float getOutPutY() {
        return outPutY;
    }

    /**
     * 设置高度比例
     * @param outPutY
     * @return
     */
    public LKaleidoscope setOutPutY(float outPutY) {
        this.outPutY = outPutY;
        return this;
    }

    /**
     * 是否能够拖动裁剪框（比例自定）
     * @return
     */
    public boolean isFreeStyleCrop() {
        return freeStyleCrop;
    }

    /**
     * 设置是否能够拖动裁剪框（比例自定）
     * @param freeStyleCrop
     * @return
     */
    public LKaleidoscope setFreeStyleCrop(boolean freeStyleCrop) {
        this.freeStyleCrop = freeStyleCrop;
        return this;
    }

    /**
     * 获取裁剪的图片质量，取值0-100
     * @return
     */
    public int getCompressionQuality() {
        //范围只能是0~100
        if (CompressionQuality <= 100 &&CompressionQuality >= 0){

            return CompressionQuality;
        }else {
            return 100;
        }
    }

    /**
     * 设置裁剪的图片质量，取值0-100
     * @param compressionQuality 裁剪的图片质量
     * @return
     */
    public LKaleidoscope setCompressionQuality(int compressionQuality) {
        //范围只能是0~100
        if (compressionQuality <= 100 &&compressionQuality >= 0){

            CompressionQuality = compressionQuality;
        }else {
            CompressionQuality = 100;
        }

        return this;
    }

    /**
     * 是否要圆形，默认不要
     * @return
     */
    public boolean isCircleDimmed() {
        return circleDimmed;
    }

    /**
     * 设置是否要圆形，默认不要
     * @param circleDimmed
     * @return
     */
    public LKaleidoscope setCircleDimmed(boolean circleDimmed) {
        this.circleDimmed = circleDimmed;
        return this;
    }

    /**
     * 是否可以旋转，默认不可以
     * @return
     */
    public boolean isRotate() {
        return rotate;
    }


    /**
     * 设置是否可以旋转，默认不可以
     * @param rotate
     * @return
     */
    public LKaleidoscope setRotate(boolean rotate) {
        this.rotate = rotate;
        return this;
    }

    /**
     * 获取相机返回按钮图片
     * @return
     */
    public int getIcCameraBack() {
        return icCameraBack;
    }

    /**
     * 设置相机返回按钮图片
     * @param icCameraBack
     * @return
     */
    public LKaleidoscope setIcCameraBack(int icCameraBack) {
        this.icCameraBack = icCameraBack;
        return this;
    }

    /**
     * 获取快门按钮图片
     * @return
     */
    public int getIcCameraTake() {
        return icCameraTake;
    }

    /**
     * 设置快门按钮图片
     * @param icCameraTake
     * @return
     */
    public LKaleidoscope setIcCameraTake(int icCameraTake) {
        this.icCameraTake = icCameraTake;
        return this;
    }

    /**
     * 获取切换相机图片
     * @return
     */
    public int getIcCameraFlip() {
        return icCameraFlip;
    }

    /**
     * 设置切换相机图片
     * @param icCameraFlip
     * @return
     */
    public LKaleidoscope setIcCameraFlip(int icCameraFlip) {
        this.icCameraFlip = icCameraFlip;
        return this;
    }

    /**
     * 获取重新拍照图片
     * @return
     */
    public int getIcCameraDisagree() {
        return icCameraDisagree;
    }

    /**
     * 设置重新拍照图片
     * @param icCameraDisagree
     * @return
     */
    public LKaleidoscope setIcCameraDisagree(int icCameraDisagree) {
        this.icCameraDisagree = icCameraDisagree;
        return this;
    }

    /**
     * 获取保存相片图片
     * @return
     */
    public int getIcCameraAgree() {
        return icCameraAgree;
    }

    /**
     * 设置保存相片图片
     * @param icCameraAgree
     * @return
     */
    public LKaleidoscope setIcCameraAgree(int icCameraAgree) {
        this.icCameraAgree = icCameraAgree;
        return this;
    }

    /**
     * 获取图片加载器
     * @return
     */
    public LKaleidoImageLoader getImageLoader() {
        return imageLoader;
    }

    /**
     * 设置图片加载器
     * @param imageLoader
     * @return
     */
    public LKaleidoscope setImageLoader(LKaleidoImageLoader imageLoader) {
        this.imageLoader = imageLoader;
        return this;
    }

    /**
     * 获取toast 显示器
     * @return
     */
    public LKaleidoToastShow getToastShow() {
        return mToastShow;
    }

    /**
     * 设置toast 显示器
     * @param toastShow
     * @return
     */
    public LKaleidoscope setToastShow(LKaleidoToastShow toastShow) {
        mToastShow = toastShow;
        return this;
    }

    /**
     *
     * @return
     */
    public List<LKaleidoImageFolder> getImageFolders() {
        return mImageFolders;
    }

    /**
     *
     * @param imageFolders
     * @return
     */
    public LKaleidoscope setImageFolders(List<LKaleidoImageFolder> imageFolders) {
        mImageFolders = imageFolders;
        return this;
    }

}
