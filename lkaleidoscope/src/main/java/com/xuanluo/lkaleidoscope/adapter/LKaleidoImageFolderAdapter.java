package com.xuanluo.lkaleidoscope.adapter;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.xuanluo.lkaleidoscope.R;
import com.xuanluo.lkaleidoscope.bean.LKaleidoImageFolder;
import com.xuanluo.lkaleidoscope.view.LKaleidoImageView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Description：选择文件夹列表的适配器
 * Author：暄落丶
 * Date：2019/4/28
 */
public class LKaleidoImageFolderAdapter extends RecyclerView.Adapter<LKaleidoImageFolderAdapter.ViewHolder> {
    private String TAG = "LKaleidoImageFolderAdapter";

    private Context mcontext;
    private LayoutInflater layoutInflater;

    private static int SELECT_ITEM = 0;
    private List<LKaleidoImageFolder> folderList;

    public LKaleidoImageFolderAdapter(Context mcontext, List<LKaleidoImageFolder> folderList) {
        this.mcontext = mcontext;
        this.folderList = folderList;
        layoutInflater = LayoutInflater.from(mcontext);



    }

    public interface OnItemClicklistener{
        void OnItemClick(ViewHolder viewHolder,LKaleidoImageFolder lKaleidoImageFolder, int position);
    }

    private OnItemClicklistener onItemClicklistener;

    public void setOnItemClicklistener(OnItemClicklistener onItemClicklistener){
        this.onItemClicklistener=onItemClicklistener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=layoutInflater.inflate(R.layout.item_image_folder,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.imgPreview.loadGlideImage(folderList.get(position).getCover().getPath());
        holder.tvPathName.setText(folderList.get(position).getName());
        holder.tvPathSize.setText("共 "+folderList.get(position).getImages().size()+" 张");

        if (position == SELECT_ITEM){
            holder.rbSel.setChecked(true);
        }else {
            holder.rbSel.setChecked(false);
        }

        if (onItemClicklistener != null){
            holder.rbSel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClicklistener.OnItemClick(holder,getItem(position),holder.getLayoutPosition());
                }
            });
        }
    }

    public LKaleidoImageFolder getItem(int position) {

        return folderList.get(position);
    }

    @Override
    public int getItemCount() {
        return folderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LKaleidoImageView imgPreview;
        private CheckBox rbSel;
        private TextView tvPathName ;
        private TextView tvPathSize ;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPreview = itemView.findViewById(R.id.item_imageFolder_img_preview);
            rbSel = itemView.findViewById(R.id.item_imageFolder_rb_sel);
            tvPathName = itemView.findViewById(R.id.item_imageFolder_tv_pathName);
            tvPathSize = itemView.findViewById(R.id.item_imageFolder_tv_pathSize);
        }
    }

    //设置SELECT_ITEM
    public void setSelectItem(int selPostion){
        SELECT_ITEM = selPostion;
        notifyDataSetChanged();
    }


    //获取SELECT_ITEM
    public int getSelectItem(){
        return SELECT_ITEM;
    }
}
