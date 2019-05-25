package com.xuanluo.lkaleidoscope.util;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import androidx.loader.content.CursorLoader;

/**
 * Description：文件处理工具类
 * Author：暄落丶
 * Date：2019/5/8
 */
public class LKaleidoFileUtils {

    /**
     * 从URI获取本地路径
     */
    public static  String getAbsoluteImagePath(Activity activity, Uri contentUri) {

        //如果是对媒体文件，在android开机的时候回去扫描，然后把路径添加到数据库中。
        //由打印的contentUri可以看到：2种结构。正常的是：content://那么这种就要去数据库读取path。
        //另外一种是Uri是 file:///那么这种是 Uri.fromFile(File file);得到的
        System.out.println(contentUri);

        String[] projection = { MediaStore.Images.Media.DATA };
        String urlpath;
        CursorLoader loader = new CursorLoader(activity,contentUri, projection, null, null, null);
        Cursor cursor = loader.loadInBackground();
        try
        {
            int column_index = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            urlpath =cursor.getString(column_index);
            //如果是正常的查询到数据库。然后返回结构
            return urlpath;
        }
        catch (Exception e)
        {

            e.printStackTrace();
        }finally{
            if(cursor != null){
                cursor.close();
            }
        }

        //如果是文件。Uri.fromFile(File file)生成的uri。那么下面这个方法可以得到结果
        urlpath = contentUri.getPath();
        return urlpath;
    }


    /**
     * 从path中获取文件名
     */
    public static String getFileName(String pathandname){

        int start=pathandname.lastIndexOf("/");
        int end=pathandname.lastIndexOf(".");
        if(start!=-1 && end!=-1){
            return pathandname.substring(start+1,end);
        }else{
            return null;
        }
    }


    /**
     * 从path中获取全部文件名
     */
    public static String getFileAllName(String pathandname){

        int start=pathandname.lastIndexOf("/");
        if(start!=-1){
            return pathandname.substring(start+1);
        }else{
            return null;
        }
    }

    public static String getFileAbsolutePath(String pathandname){

        int end=pathandname.lastIndexOf("/");
        if(end!=-1){
            return pathandname.substring(0,end+1);
        }else{
            return null;
        }
    }


}
