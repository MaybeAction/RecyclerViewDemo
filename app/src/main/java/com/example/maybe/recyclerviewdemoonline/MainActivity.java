package com.example.maybe.recyclerviewdemoonline;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.util.List;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity implements MyRecyclerViewAdapter.OnItemClickListener, MyRecyclerViewAdapter.OnItemLongClickListener {
    private MyRecyclerViewAdapter adapter;
    private Unbinder unbind;
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

                adapter.add("" + (char) i);
            }
        }
    }

    private void initView() {
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
        adapter.setOnItemLongClickListener(this);
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
                    rv.setLayoutManager(new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL));

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
        Toast.makeText(this, position+"", Toast.LENGTH_SHORT).show();

                adapter.add("插入的"+position*199,position);



    }
}
