package com.example.maybe.recyclerviewdemoonline;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity implements MyRecyclerViewAdapter.OnItemClickListener, MyRecyclerViewAdapter.OnItemLongClickListener {
    private MyRecyclerViewAdapter adapter;
    private Unbinder unbind;
    private List<String> mData;
    private List<Integer> mDatai;
    @BindView(R.id.rv)RecyclerView rv;
    @BindViews({R.id.rv_bt1,R.id.rv_bt2})List<Button> buttons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //绑定视图
        unbind=ButterKnife.bind(this);
        initView();
        initEvent();
        initData();

    }

    private void initData() {
        for(int j=0;j<5;j++) {
            for (int i = 'A'; i < 'Z' + 1; i++) {
                mData.add(""+(char)i);
            }
        }
        adapter.add(mData);
    }

    private void initView() {
        mData=new ArrayList<>();
        adapter=new MyRecyclerViewAdapter(this);
        //设置布局管理器：让他展示的样式是什么
        //ListView
        rv.setLayoutManager(new LinearLayoutManager(this));

        //2如果添加或删除item。可以设置动画,他为我们提供一个可以直接使用的动画defaultitemAnimator
        rv.setItemAnimator(new DefaultItemAnimator());

        //3.添加分割线:默认提供的一个DividerItemDecoration。都可以自己定义可以在item中去设置他的分割线
        rv.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

        rv.setAdapter(adapter);

        //设置item的点击事件
        adapter.setOnItemClickListener(this);
        //为了拖动，先禁用
        adapter.setOnItemLongClickListener(this);

        //item拖动
//        ItemTouchHelper.Callback callback= new ItemTouchHelper.Callback() {
//            /**
//             * 方向：
//             * dragDirs:拖动：LEFT、RIGHT、START、END、UP、DOWN
//             * swipeDirs:滑动：LEFT、RIGHT、START、END、UP、DOWN
//             * 设计滑动 模式及方向要考虑好当前recyclerView样式
//             *如果设置为0的时候，表示没有此项功能
//             */
//
//
//
//            //拿到设置的移动的方向设置
//            @Override
//            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
//                int dragFlags=0;
//                int swipeFlags=0;
//                //设置的方向（拖动、滑动）
//                if(rv.getLayoutManager() instanceof LinearLayoutManager){
//                     dragFlags=ItemTouchHelper.UP|ItemTouchHelper.LEFT|ItemTouchHelper.DOWN;
//                    swipeFlags=ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT;
//                }else{
//                     dragFlags=ItemTouchHelper.UP|ItemTouchHelper.DOWN;
//                     swipeFlags=0;
//            }
//
//                //将我们设置的Flags设置给ItemTouchHelper
//                return makeMovementFlags(dragFlags,swipeFlags);
//            }
//
//
//            //拖动的事件处理
//            @Override
//            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//
//
//                return true;
//            }
//            //滑动
//            @Override
//            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//
//            }
//
//        };
        ItemTouchHelper.Callback callback= new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                int fromPosition=viewHolder.getAdapterPosition();//得到拖动的viewHolder的position
                int toPosition=target.getAdapterPosition();// 得到目标的viewHolder的potision

                //向下移动
                if(fromPosition<toPosition){
                    for(int i=fromPosition;i<toPosition;i++){
                        //数据集合：通过一个集合的工具类collections
                        Collections.swap(mData,i,i+1);
                    }
                }else{
                    //向上
                    for(int i=fromPosition;i>toPosition;i--){
                        Collections.swap(mData,i,i-1);
                    }
                }
                adapter.itemMoved(fromPosition,toPosition,mData);
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                //滑动的时候删除
                int position=viewHolder.getAdapterPosition();
                adapter.delete(position);
            }
        };
        //滑动拖动的帮助类
        ItemTouchHelper itemTouchHelper=new ItemTouchHelper(callback);
        //跟recyclerView关联起来
        itemTouchHelper.attachToRecyclerView(rv);
    }



    private void initEvent() {
        ButterKnife.apply(buttons, new ButterKnife.Action<Button>() {
            @Override
            public void apply(@NonNull Button view, int index) {
                    switch (index){
                        case 1:
                            view.setText("瀑布流");

                            break;
                        case 0:
                            view.setText("普通");
                            break;
                    }

            }
        });
    }


    @OnClick({R.id.rv_bt1,R.id.rv_bt2})
    public void btClick(View view){
            switch (view.getId()){
                case R.id.rv_bt2:
                    rv.setLayoutManager(new StaggeredGridLayoutManager(5,StaggeredGridLayoutManager.VERTICAL));

                    break;
                case R.id.rv_bt1:
                    rv.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    break;
            }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        unbind.unbind();
    }


    /**
     * Item点击事件
     * @param position
     */
    @Override
    public void OnItemClick(int position) {
            adapter.delete(position);
    }

    /**
     * Item的长按点击事件
     * @param position
     */
    @Override
    public void onItemLongClick(int position) {
//        Toast.makeText(this, position+"", Toast.LENGTH_SHORT).show();
//
//                adapter.add("插入的"+position*199,position);
    }
}
