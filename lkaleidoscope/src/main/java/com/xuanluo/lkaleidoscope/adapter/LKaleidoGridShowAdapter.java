package com.xuanluo.lkaleidoscope.adapter;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.xuanluo.lkaleidoscope.LKaleidoscope;
import com.xuanluo.lkaleidoscope.R;
import com.xuanluo.lkaleidoscope.bean.LKaleidoImageBean;
import com.xuanluo.lkaleidoscope.view.LKaleidoImageSelCheckBox;
import com.xuanluo.lkaleidoscope.view.LKaleidoImageView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Description：显示相册图片列表的适配器
 * Author：暄落丶
 * Date：2019/4/27
 */
public class LKaleidoGridShowAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static String TAG = "LKaleidoGridShowAdapter";//

    private static final int ITEM_TYPE_CAMERA = 0;  //相机
    private static final int ITEM_TYPE_NORMAL = 1;  //不是相机

    private LKaleidoscope mLKaleidoscope;

    private Context mcontext;
    private LayoutInflater layoutInflater;

    private List<LKaleidoImageBean> imageList;
    private ArrayList<LKaleidoImageBean> mSelImageList;


    public LKaleidoGridShowAdapter(Context mcontext, List<LKaleidoImageBean> imageList) {
        this.mcontext = mcontext;
        this.imageList = imageList;
        layoutInflater = LayoutInflater.from(mcontext);
        mLKaleidoscope = LKaleidoscope.getInstance();
        mSelImageList = new ArrayList<>();


    }

    public void refreshData(ArrayList<LKaleidoImageBean> images) {
        if (images == null || images.size() == 0){
            this.imageList = new ArrayList<>();
        } else {

            this.imageList = images;

//            //如果显示相机，则每个文件目录先增加一个空数据用于显示相机
//            if (mLKaleidoscope.isShowCamera()){
//                //顯示相機
//                LKaleidoImageBean cameraImageBean = new LKaleidoImageBean();
//                images.add(cameraImageBean);
//            }

        }
        notifyDataSetChanged();
    }




    public interface OnItemClicklistener{
        void OnItemClick(LKaleidoImageBean lKaleidoImageBean, int position);
        void OnItemCamera();
        void OnItemSelSize(int size);
    }

    private OnItemClicklistener onItemClicklistener;

    public void setOnItemClicklistener(OnItemClicklistener onItemClicklistener){
        this.onItemClicklistener=onItemClicklistener;
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0 && mLKaleidoscope.isShowCamera()){
            //顯示相機
            return ITEM_TYPE_CAMERA;
        }

        return ITEM_TYPE_NORMAL;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_CAMERA){
            //第一個條目是相機
            View view=layoutInflater.inflate(R.layout.item_lkaleido_grid_camera,parent,false);
            return new CameraViewHolder(view);
        }
        View view=layoutInflater.inflate(R.layout.item_lkaleido_grid_show,parent,false);
        return new photoViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof CameraViewHolder){
            ((CameraViewHolder)holder).onBind();
        }else if (holder instanceof photoViewHolder){
            ((photoViewHolder)holder).onBind(getItem(position));
        }

    }


    public LKaleidoImageBean getItem(int position) {
        if (mLKaleidoscope.isShowCamera()){
            //拍照
        }else {
            //不拍照
        }
        return imageList.get(position);
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }


    /**
     * 添加图片布局
     */
    public class CameraViewHolder extends RecyclerView.ViewHolder{
        private FrameLayout fmAll;
        public CameraViewHolder(@NonNull View itemView) {
            super(itemView);

            fmAll = itemView.findViewById(R.id.item_gridCamera_fmAll);
        }


        public void onBind(){

            if (onItemClicklistener != null){
                fmAll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClicklistener.OnItemCamera();
                    }
                });
            }

        }
    }

    /**
     * 图片布局
     */
    public class photoViewHolder extends RecyclerView.ViewHolder {
        private LKaleidoImageView ivPhoto;
        private View vBorder;
        private LKaleidoImageSelCheckBox cbImageSel;
        public photoViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPhoto = itemView.findViewById(R.id.item_gridShow_iv_photo);
            vBorder = itemView.findViewById(R.id.item_gridShow_view_imageSelBorder);
            cbImageSel = itemView.findViewById(R.id.item_gridShow_cb_imageSel);
        }

        public void onBind(final LKaleidoImageBean imageBean){

            ivPhoto.loadGlideImage(imageBean.getPath());


            if (!mSelImageList.contains(imageBean)){
                cbImageSel.setChecked(false);
                vBorder.setVisibility(View.GONE);
            }else {
                cbImageSel.setChecked(true);
                vBorder.setVisibility(View.VISIBLE);
            }

            //如果单选模式，则不显示右上角选择框
            if (mLKaleidoscope.isRadioMode()){
                //单选
                cbImageSel.setVisibility(View.GONE);
            }else {
                cbImageSel.setVisibility(View.VISIBLE);
            }

            if (onItemClicklistener != null){
                ivPhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClicklistener.OnItemClick(imageBean,getLayoutPosition());

                    }
                });
            }
        }
    }

    //选择了图片
    public void setSelImages(LKaleidoImageBean lKaleidoImageBean,int position){
        if (mSelImageList.contains(lKaleidoImageBean)){
            mSelImageList.remove(lKaleidoImageBean);
        }else {
            if (mSelImageList.size() <mLKaleidoscope.getSelectLimit()){
                mSelImageList.add(lKaleidoImageBean);
            }else {
                mLKaleidoscope.getToastShow().showLkToast(mcontext,"您最多可以选择 "+mLKaleidoscope.getSelectLimit()+ " 张照片");
            }
        }
        if (onItemClicklistener!= null){
            onItemClicklistener.OnItemSelSize(mSelImageList.size());
        }
//        notifyDataSetChanged();
        notifyItemChanged(position);
//        notifyItemRangeChanged(position,1);
    }

    //获取选择的图片
    public ArrayList<LKaleidoImageBean> getSelImages(){
        return mSelImageList;
    }




}
