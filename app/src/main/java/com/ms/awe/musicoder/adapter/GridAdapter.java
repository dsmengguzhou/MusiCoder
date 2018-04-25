package com.ms.awe.musicoder.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ms.awe.musicoder.R;
import com.ms.awe.musicoder.bean.Meizi;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by awe on 2018/4/25.
 * Grid布局的适配器
 */

public class GridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener,View.OnLongClickListener{

    private Context mContext;
    private List<Meizi> datas;

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view);
        void onItemLongClick(View view);
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public GridAdapter(Context context, List<Meizi> datas) {
        mContext=context;
        this.datas=datas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0){
            View view = LayoutInflater.from(mContext)
                    .inflate(R.layout.item_recycler_grid,parent,false);
            MsViewHolder holder = new MsViewHolder(view);

            view.setOnClickListener(this);
            view.setOnLongClickListener(this);

            return holder;
        }else {
            MyViewHolder mHolder = new MyViewHolder(LayoutInflater.from(mContext)
                    .inflate(R.layout.item_page,parent,false));
            return mHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MsViewHolder){
            Picasso.with(mContext)
                    .load(datas.get(position)
                    .getUrl())
                    .into(((MsViewHolder)holder).iv);
        }else if (holder instanceof MyViewHolder){
            ((MyViewHolder)holder).tv.setText(datas.get(position).getPage() + "页");
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    @Override
    public int getItemViewType(int position) {
        //判断item是图还是显示页数（图片有URL）
        if (!TextUtils.isEmpty(datas.get(position).getUrl())){
            return 0;
        }else {
            return 1;
        }
    }

    /**
     *  OnClick接口实现的点击事件
     */
    @Override
    public void onClick(View view) {
        if (mOnItemClickListener != null){
            mOnItemClickListener.onItemClick(view);
        }
    }

    /**
     * onLongClick接口实现的长按点击事件
     */
    @Override
    public boolean onLongClick(View view) {
        if (mOnItemClickListener != null){
            mOnItemClickListener.onItemLongClick(view);
        }
        return false;
    }

    class MsViewHolder extends RecyclerView.ViewHolder{

        private ImageButton iv;

        public MsViewHolder(View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.imageview);
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        private TextView tv;

        public MyViewHolder(View view)
        {
            super(view);
            tv = (TextView) view.findViewById(R.id.tv);
        }
    }
}
