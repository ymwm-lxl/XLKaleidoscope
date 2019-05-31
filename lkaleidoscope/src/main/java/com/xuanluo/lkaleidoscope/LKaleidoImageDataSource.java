package com.xuanluo.lkaleidoscope;

import android.database.Cursor;
import android.icu.text.UFormat;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import com.xuanluo.lkaleidoscope.bean.LKaleidoImageBean;
import com.xuanluo.lkaleidoscope.bean.LKaleidoImageFolder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

/**
 * Description：加载手机图片实现类
 * Author：暄落丶
 * Date：2019/4/27
 */
    public class LKaleidoImageDataSource implements LoaderManager.LoaderCallbacks<Cursor>  {

    protected final String TAG = "LKaleidoImageDataSource";

    public static final int LOADER_ALL = 0;         //加载所有图片
    public static final int LOADER_CATEGORY = 1;    //分类加载图片
    private final String[] IMAGE_PROJECTION = {     //查询图片需要的数据列
            MediaStore.Images.Media.DISPLAY_NAME,   //图片的显示名称  aaa.jpg
            MediaStore.Images.Media.DATA,           //图片的真实路径  /storage/emulated/0/pp/downloader/wallpaper/aaa.jpg
            MediaStore.Images.Media.SIZE,           //图片的大小，long型  132492
            MediaStore.Images.Media.WIDTH,          //图片的宽度，int型  1920
            MediaStore.Images.Media.HEIGHT,         //图片的高度，int型  1080
            MediaStore.Images.Media.MIME_TYPE,      //图片的类型     image/jpeg
            MediaStore.Images.Media.DATE_ADDED};    //图片被添加的时间，long型  1450518608


    private LoaderManager loaderManager;
    private LKaleidoscope mLKaleidoscope;
    private FragmentActivity activity;
    private OnImagesLoadedListener loadedListener;
    private ArrayList<LKaleidoImageFolder> imageFolderList = new ArrayList<>();   //所有的图片文件夹

    /**
     * @param activity       用于初始化LoaderManager，需要兼容到2.3
     * @param path           指定扫描的文件夹目录，可以为 null，表示扫描所有图片
     * @param loadedListener 图片加载完成的监听
     */
    public LKaleidoImageDataSource(FragmentActivity activity,String path, OnImagesLoadedListener loadedListener) {
        this.activity = activity;
        this.loadedListener = loadedListener;
        mLKaleidoscope = LKaleidoscope.getInstance();

        loaderManager = activity.getSupportLoaderManager();
        if (path == null){
            //扫描所有图片
            loaderManager.initLoader(LOADER_ALL,null,this);
        }else {
            //加载指定目录的图片
            Bundle bundle = new Bundle();
            bundle.putString("path", path);
            loaderManager.initLoader(LOADER_CATEGORY, bundle, this);
        }
    }

    public void reLoader(){
        //扫描所有图片
        loaderManager.restartLoader(LOADER_ALL,null,this);
    }

    // 创建一个可查询ContentProvider的loader
    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        CursorLoader cursorLoader = null;
        //扫描所有图片
        if (id == LOADER_ALL)
            cursorLoader = new CursorLoader(activity, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION, null, null, IMAGE_PROJECTION[6] + " DESC");
        //扫描某个图片文件夹
        if (id == LOADER_CATEGORY)
            cursorLoader = new CursorLoader(activity, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION, IMAGE_PROJECTION[1] + " like '%" + args.getString("path") + "%'", null, IMAGE_PROJECTION[6] + " DESC");

        return cursorLoader;
    }

    // loader完成查询时调用，通常用于在查询到的cursor中提取数据
    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        imageFolderList.clear();

        if (data != null){

            ArrayList<LKaleidoImageBean> allImageList = new ArrayList<>();//全部图片的集合，不区分文件夹
            while (data.moveToNext()){
                //查询数据
                String imageName = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));//图片的显示名称
                String imagePath = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1])); //图片的真实路径

                File file = new File(imagePath);
                if (!file.exists() || file.length() <= 0) {
                    continue;
                }

                long imageSize = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));//图片的大小
                int imageWidth = data.getInt(data.getColumnIndexOrThrow(IMAGE_PROJECTION[3]));//图片的宽度
                int imageHeight = data.getInt(data.getColumnIndexOrThrow(IMAGE_PROJECTION[4]));//图片的高度
                String imageMimeType = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[5])); //图片的类型
                long imageAddTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[6])); //图片被添加的时间

                if (!mLKaleidoscope.isGifShow() && imageMimeType.equals("image/gif")){
                    continue;
                }

                //封装实体
                LKaleidoImageBean imageBean = new LKaleidoImageBean();
                imageBean.setName(imageName);
                imageBean.setPath(imagePath);
                imageBean.setSize(imageSize);
                imageBean.setWidth(imageWidth);
                imageBean.setHeight(imageHeight);
                imageBean.setMimeType(imageMimeType);
                imageBean.setAddTime(imageAddTime);
                allImageList.add(imageBean);//把当前图片放进所有图片的集合中

                //将图片再依据父路径分类存储
                File imageFile = new File(imagePath);//图片文件
                File imageParentFile = imageFile.getParentFile();//图片父路径文件
                LKaleidoImageFolder imageFolder = new LKaleidoImageFolder();
                imageFolder.setName(imageParentFile.getName());
                imageFolder.setPath(imageParentFile.getAbsolutePath());

                if (!imageFolderList.contains(imageFolder)){
                    //当前集合中还没有此父路径
                    ArrayList<LKaleidoImageBean> images = new ArrayList<>();

                    images.add(imageBean);
                    imageFolder.setCover(imageBean);
                    imageFolder.setImages(images);
                    imageFolderList.add(imageFolder);

                }else {
                    //当前集合中已经有此父路径
                    imageFolderList.get(imageFolderList.indexOf(imageFolder)).getImages().add(imageBean);
                }


            }

            //防止没有图片报异常
            if (data.getCount() > 0 && allImageList.size()>0){
                //构造所有图片的文件集合
                LKaleidoImageFolder allImagesFolder = new LKaleidoImageFolder();
                allImagesFolder.setName(activity.getString(R.string.all_image).toString());
                allImagesFolder.setPath("/");
                allImagesFolder.setCover(allImageList.get(0));
                allImagesFolder.setImages(allImageList);
                imageFolderList.add(0,allImagesFolder);//确保第一条是所有图片
            }


        }

        //回调接口，通知图片数据准备完成
        LKaleidoscope.getInstance().setImageFolders(imageFolderList);
        loadedListener.onImagesLoaded(imageFolderList);

    }

    //当一个已创建的加载器被重置从而使其数据无效时，此方法被调用。此回调使你能发现什么时候数据将被释放于是你可以释放对它的引用。
    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
    }

    /** 所有图片加载完成的回调接口 */
    public interface OnImagesLoadedListener {
        void onImagesLoaded(List<LKaleidoImageFolder> imageFolders);
    }
}
