package com.xuanluo.lkaleidoscope.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xuanluo.lkaleidoscope.LKaleidoscope;
import com.xuanluo.lkaleidoscope.R;
import com.xuanluo.lkaleidoscope.bean.LKaleidoImageBean;
import com.xuanluo.lkaleidoscope.view.LKaleidoImageView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Description：
 * Author：暄落丶
 * Date：2019/4/30
 */
public class LkaleidoGalleryAdapter extends RecyclerView.Adapter<LkaleidoGalleryAdapter.ViewHolder> {

    private LKaleidoscope mLKaleidoscope;

    private Context mcontext;
    private LayoutInflater layoutInflater;

    private List<LKaleidoImageBean> imageList;
    private int SELECT_ITEM = 0;

    public LkaleidoGalleryAdapter(Context mcontext, List<LKaleidoImageBean> imageList) {
        this.mcontext = mcontext;
        this.imageList = imageList;
        layoutInflater = LayoutInflater.from(mcontext);
    }

    public interface OnItemClicklistener{
        void OnItemClick(ViewHolder lKaleidoImageBean, int position);
    }

    private OnItemClicklistener onItemClicklistener;

    public void setOnItemClicklistener(OnItemClicklistener onItemClicklistener){
        this.onItemClicklistener=onItemClicklistener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=layoutInflater.inflate(R.layout.item_lkaleido_gallery,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        LKaleidoImageBean imageBean = imageList.get(position);
        holder.ivShow.loadGlideImage(imageBean.getPath());

        if (position == SELECT_ITEM){
            holder.vBorder.setVisibility(View.VISIBLE);
        }else {
            holder.vBorder.setVisibility(View.GONE);
        }

        if (onItemClicklistener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    onItemClicklistener.OnItemClick(holder,holder.getLayoutPosition());
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private LKaleidoImageView ivShow;
        private View vBorder;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivShow = itemView.findViewById(R.id.item_gallery_iv_show);
            vBorder = itemView.findViewById(R.id.item_gallery_v_border);
        }
    }

    public void setSELECT_ITEM(int position){
        SELECT_ITEM = position;
        notifyDataSetChanged();
    }
}
