package com.example.maybe.recyclerviewdemoonline;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by maybe on 2017/3/10.
 */

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHoler> implements View.OnClickListener, View.OnLongClickListener {
    private List<String> list;
    private Context context;
    private LayoutInflater inflater;
    private Unbinder unbinder;
    private View view;
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public MyRecyclerViewAdapter(Context context) {
        this.context=context;
        inflater=inflater.from(context);
        list=new ArrayList();
    }
    public void add(String str){
        list.add(str);

    }
    @Override
    public MyViewHoler onCreateViewHolder(ViewGroup parent, int viewType) {
        view=inflater.inflate(R.layout.item_rv,parent,false);
        MyViewHoler myViewHoler=new MyViewHoler(view);
        return myViewHoler;
    }

    @Override
    public void onBindViewHolder(MyViewHoler holder, int position) {
                holder.rc_tv.setText(list.get(position));
                holder.rc_tv.setTag(position);
                holder.rc_tv.setOnClickListener(this);
                holder.rc_tv.setOnLongClickListener(this);
                holder.rc_tv.setTag(position);
                holder.rc_tv.setWidth((int) (position%3+Math.random()*100)+1);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * 插入数据
     * @param str
     * @param position
     */
    public void add(String str,int position){
        list.add(position,str);
        //调用才会有动画效果
        notifyItemInserted(position);
        //刷新数据和数据结构
        notifyItemRangeChanged(position,list.size()-position);
    }
    /**
     * 删除数据
     * @param position
     */
    public void delete(int position){
        list.remove(position);
        //伴有动画效果
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,list.size()-position);
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        onItemClickListener.OnItemClick((Integer) view.getTag());
    }

    @Override
    public boolean onLongClick(View view) {
        onItemLongClickListener.onItemLongClick((Integer) view.getTag());
        //不设置为true不会触发点击
        return true;
    }


    class MyViewHoler extends RecyclerView.ViewHolder{
        @BindView(R.id.rv_tv)TextView rc_tv;
        public MyViewHoler(View itemView) {
            super(itemView);
            unbinder=ButterKnife.bind(this,itemView);

        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public interface OnItemClickListener{
        void OnItemClick(int position);
    }
    public interface OnItemLongClickListener{
        void onItemLongClick(int position);
    }
}
