package com.xuanluo.lkaleidoscope.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;

import com.xuanluo.lkaleidoscope.bean.LKaleidoImageBean;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Description：需要用的简单的工具们
 * Author：暄落丶
 * Date：2019/4/27
 */
public class LKaleidoUtils {
    private static final String TAG = "LKaleidoUtils";//

    /** dp转px */
    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, context.getResources().getDisplayMetrics());
    }

    /** 保存图片 **/
    public static String savePhoto2SD(Context context,Bitmap mBitmap,String path){
        //生成路径
        File appDir = new File(path);
        if (!appDir.exists()) {
            appDir.mkdirs();
        }

        //文件名为时间
        long timeStamp = System.currentTimeMillis();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
        String sd = sdf.format(new Date(timeStamp));
        String fileName = "LK"+sd + ".jpg";

        //获取文件
        File file = new File(appDir, fileName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            //通知系统相册刷新
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                    Uri.fromFile(new File(file.getPath()))));
            return fileName;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    /** 图片转换实体类 **/
    public static LKaleidoImageBean photo2Bean(Context context,String filePath,String fileName){
        String path = filePath+fileName;

        LKaleidoImageBean imageBean = new LKaleidoImageBean();
        imageBean.setName(fileName);
        imageBean.setPath(path);
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(path);
            imageBean.setSize(Long.parseLong(exif.getAttribute(ExifInterface.TAG_IMAGE_LENGTH)));
            imageBean.setWidth(Integer.parseInt(exif.getAttribute(ExifInterface.TAG_IMAGE_WIDTH)));
            imageBean.setHeight(Integer.parseInt(exif.getAttribute(ExifInterface.TAG_IMAGE_LENGTH)));

            String time = exif.getAttribute(ExifInterface.TAG_DATETIME);
            if (time != null && !time.isEmpty()){
                long longTime = getStringToDate(time,"yyyy:MM:dd HH:mm:ss");
                imageBean.setAddTime(longTime);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(path);
            imageBean.setSize(fis.available());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }

        imageBean.setMimeType(getFormatName(fileName));
        return imageBean;
    }

    /**
     * 获取文件格式名
     */
    public static String getFormatName(String fileName) {
        //去掉首尾的空格
        fileName = fileName.trim();
        String s[] = fileName.split("\\.");
        if (s.length >= 2) {
            return s[s.length - 1];
        }
        return "";
    }

    public static long getStringToDate(String dateString, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        Date date = new Date();
        try{
            date = dateFormat.parse(dateString);
        } catch(ParseException e) {
            e.printStackTrace();
        }
        return date.getTime()/1000;
    }

    /** 生成随机数 **/
    public static int makeRandom(){

        Random rand = new Random();
        int i = rand.nextInt(100);
        return i;
    }

}
