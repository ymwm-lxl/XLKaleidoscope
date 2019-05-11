package com.xuanluo.lkaleidoscope.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.xuanluo.lkaleidoscope.LKaleidoscope;
import com.xuanluo.lkaleidoscope.bean.LKaleidoImageBean;
import com.xuanluo.lkaleidoscope.view.LKaleidoPhotoView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

/**
 * Description：图片展示适配器
 * Author：暄落丶
 * Date：2019/4/30
 */
public class LKaleidoViewPagerAdapter extends PagerAdapter {

    private Context mContext;

    private List<LKaleidoImageBean> imageList;

    private LKaleidoscope mLKaleidoscope;
    private PhotoViewClickListener mPhotoViewClickListener;

    public LKaleidoViewPagerAdapter(Context context, List<LKaleidoImageBean> imageList) {
        mContext = context;
        this.imageList = imageList;

        mLKaleidoscope = LKaleidoscope.getInstance();

    }

    private void setData(List<LKaleidoImageBean> imageList){
        this.imageList = imageList;
    }

    public void setPhotoViewClickListener(PhotoViewClickListener photoViewClickListener) {
        mPhotoViewClickListener = photoViewClickListener;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LKaleidoPhotoView LKaleidoPhotoView = new LKaleidoPhotoView(mContext);
        LKaleidoImageBean imageBean = imageList.get(position);
        LKaleidoPhotoView.loadGlideImagePreview(imageBean.getPath());

        container.addView(LKaleidoPhotoView);

        return LKaleidoPhotoView;
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    //监听
    public interface PhotoViewClickListener {
        void OnPhotoTapListener(View view, float v, float v1);
    }
}
