package com.xuanluo.kaleidoscope;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.xuanluo.lkaleidoscope.bean.LKaleidoImageBean;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Description：
 * Author：暄落丶
 * Date：2019/5/9
 */
public class ImageShowAdapter extends RecyclerView.Adapter<ImageShowAdapter.ViewHolder> {
    private Context mcontext;
    private LayoutInflater layoutInflater;

    private List<LKaleidoImageBean> imageList;

    public ImageShowAdapter(Context mcontext , List<LKaleidoImageBean> imageList) {
        this.mcontext = mcontext;
        this.imageList = imageList;
        layoutInflater = LayoutInflater.from(mcontext);
    }

    public void refreshData(ArrayList<LKaleidoImageBean> images) {
        if (images == null || images.size() == 0){
            this.imageList = new ArrayList<>();
        } else {

            this.imageList = images;

        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=layoutInflater.inflate(R.layout.item_show,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RequestOptions requestOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)   //缓存全尺寸;
//                .error(R.drawable.)           //设置错误图片
                .placeholder(R.drawable.ic_default_image) ;   //设置占位图片
        Glide.with(mcontext)
                .load(Uri.fromFile(new File(imageList.get(position).getPath())))
                .apply(requestOptions)
                .into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.item_show_img);
        }
    }
}
