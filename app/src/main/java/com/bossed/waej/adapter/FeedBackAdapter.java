package com.bossed.waej.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bossed.waej.R;
import com.bossed.waej.base.OnItemClickListener;
import com.bossed.waej.javebean.FeedBackPicBean;
import com.bossed.waej.util.GlideUtils;

import java.util.ArrayList;


public class FeedBackAdapter extends RecyclerView.Adapter {
    private final ArrayList<FeedBackPicBean> arrayList;
    private final Context context;
    private static final int CONTENT_VIEW_TYPE = 1;
    private static final int FOOTER_VIEW_TYPE = 2;

    public FeedBackAdapter(ArrayList<FeedBackPicBean> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case FOOTER_VIEW_TYPE:
                return new FooterHolder(LayoutInflater.from(context).inflate(R.layout.layout_item_add_pic, parent, false));
            default:
                return new ContentHolder(LayoutInflater.from(context).inflate(R.layout.layout_item_shop_pic, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ContentHolder) {
            ContentHolder contentHolder = (ContentHolder) holder;
            if (arrayList != null && arrayList.size() > 0) {
                GlideUtils.Companion.get().loadShopPic(context, arrayList.get(position).getUrl(), contentHolder.imageView);
            }
        } else if (holder instanceof FooterHolder) {
            FooterHolder footerHolder = (FooterHolder) holder;
            footerHolder.imageView.setImageResource(R.mipmap.icon_camera_gray);
        }
    }

    @Override
    public int getItemCount() {
        if (arrayList != null && arrayList.size() > 0) {
            if (arrayList.size() == 3) {
                return arrayList.size();
            } else {
                return arrayList.size() + 1;
            }
        } else {
            return 1;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (arrayList != null && arrayList.size() > 0) {//如果有数据
            if (arrayList.size() == 3) {
                return CONTENT_VIEW_TYPE;
            } else if (position == arrayList.size()) {
                return FOOTER_VIEW_TYPE;
            } else {
                return CONTENT_VIEW_TYPE;
            }
        } else {//如果无数据
            return FOOTER_VIEW_TYPE;
        }
    }

    protected class FooterHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;

        private FooterHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_add_pic);
            imageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.iv_add_pic:
                    onItemclickListener.onClick(imageView, getAdapterPosition());
                    break;
                default:
                    break;
            }
        }
    }

    protected class ContentHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        ImageView imageView;
        ImageView delete;

        private ContentHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_shop_pic);
            delete = itemView.findViewById(R.id.iv_delete_pic);
            imageView.setOnClickListener(this);
            delete.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.iv_shop_pic:
                    onItemclickListener.onClick(imageView, getAdapterPosition());
                    break;
                case R.id.iv_delete_pic:
                    onItemclickListener.onClick(delete, getAdapterPosition());
                    break;
            }
        }

        @Override
        public boolean onLongClick(View view) {
            switch (view.getId()) {
                case R.id.iv_shop_pic:
                    break;
                default:
                    break;
            }
            return true;
        }
    }

    private OnItemClickListener onItemclickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemclickListener = onItemClickListener;
    }
}
