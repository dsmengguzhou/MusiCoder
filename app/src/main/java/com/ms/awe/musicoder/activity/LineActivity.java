package com.ms.awe.musicoder.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import com.ms.awe.musicoder.R;
import com.ms.awe.musicoder.bean.Meizi;
import com.ms.awe.musicoder.okhttp.MyOkhttp;
import com.ms.awe.musicoder.util.SnackbarUtil;

import java.util.Collections;
import java.util.List;

/**
 * Created by awe on 2018/4/24.
 * RecyclerView展示线性布局
 */

public class LineActivity extends AppCompatActivity{

    private CoordinatorLayout coordinatorLayout;            //跟布局
    private static RecyclerView recyclerView;
    private MsAdapter msAdapter;                            //RecyclerView适配器
    private LinearLayoutManager mLayoutManager;             //RecyclerView布局管理器
    private SwipeRefreshLayout swipeRefreshLayout;          //刷新布局
    private List<Meizi> meizis;
    private ItemTouchHelper itemTouchHelper;
    private int page = 1;                                     //首页
    private int lastVisibleItem;                           //最后一个显示的Item
    private int screenwidth;                                //屏幕宽度

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().hide();
//        }
        setContentView(R.layout.activity_line);

        initViews();
        setListener();

        new GetData().execute("http://gank.io/api/data/福利/10/1");

        initScreenWidth();
    }

    private void initViews() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setNavigationIcon(R.mipmap.icon_butterfly);         //设置导航栏图标
//        toolbar.setLogo(R.mipmap.icon_cat);                         //设置app logo
        toolbar.setTitle("好看的小姐姐");                           //设置主标题
//        toolbar.setSubtitle("真的是好看的小姐姐");                  //设置子标题
        toolbar.inflateMenu(R.menu.base_toolbar_menu);              //设置Menu
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_item1:
                        Toast.makeText(LineActivity.this,"好看的小姐姐Item1",Toast.LENGTH_SHORT).show();
                        finish();
                        break;
//                    case R.id.action_item2:
//                        Toast.makeText(LineActivity.this,"好看的小姐姐Item2",Toast.LENGTH_SHORT).show();
//                        break;
//                    case R.id.action_item3:
//                        Toast.makeText(LineActivity.this,"好看的小姐姐Item3",Toast.LENGTH_SHORT).show();
//                        break;
//                    case R.id.action_item4:
//                        Toast.makeText(LineActivity.this,"好看的小姐姐Item4",Toast.LENGTH_SHORT).show();
//                        break;
                }
                return true;
            }
        });


        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.line_coordinatorLayout);

        recyclerView = (RecyclerView) findViewById(R.id.line_recycler);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.line_swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorAccent);
        swipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
    }

    private void setListener() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //下拉刷新第一页
                page = 1;
                new GetData().execute("http://gank.io/api/data/福利/10/1");
            }
        });

        itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int drawFlags = 0;
                int swipeFlags = 0;
                if (recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
                    drawFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                } else if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                    drawFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                    //设置侧滑方向为从左到右和从右到左都可以
                    swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                }
                return makeMovementFlags(drawFlags, swipeFlags);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                int from = viewHolder.getAdapterPosition();
                int to = target.getAdapterPosition();
                Collections.swap(meizis, from, to);
                msAdapter.notifyItemMoved(from, to);
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                msAdapter.removeItem(viewHolder.getAdapterPosition());
            }

            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                super.onSelectedChanged(viewHolder, actionState);
                if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
                    viewHolder.itemView.setBackgroundColor(Color.LTGRAY);
                }
            }

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                viewHolder.itemView.setBackgroundColor(Color.WHITE);
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                viewHolder.itemView.setAlpha(1 - Math.abs(dX) / screenwidth);

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //  0：当前屏幕停止滚动；1时：屏幕在滚动 且 用户仍在触碰或手指还在屏幕上；2时：随用户的操作，屏幕上产生的惯性滑动；
                //  滑动状态停止并且剩余两个item时自动加载
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem + 2 >= mLayoutManager.getItemCount()) {
                    new GetData().execute("http://gank.io/api/data/福利/10/" + (++page));
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //  获取加载的最后一个可见视图在适配器的位置。
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
            }
        });
    }

    private void initScreenWidth() {
        //获取屏幕宽度
        WindowManager wm = (WindowManager) LineActivity.this
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        screenwidth = outMetrics.widthPixels;
    }



    private class GetData extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            swipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected String doInBackground(String... strings) {
            return MyOkhttp.get(strings[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (!TextUtils.isEmpty(result)) {
                JSONObject jsonObject;
                Gson gson = new Gson();
                String jsonData = null;

                try {
                    jsonObject = new JSONObject(result);
                    jsonData = jsonObject.getString("results");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (meizis == null || meizis.size() == 0) {
                    meizis = gson.fromJson(jsonData, new TypeToken<List<Meizi>>() {
                    }.getType());
                    Meizi pages = new Meizi();
                    pages.setPage(page);
                    meizis.add(pages);
                } else {
                    List<Meizi> more = gson.fromJson(jsonData, new TypeToken<List<Meizi>>() {}.getType());
                    meizis.addAll(more);
                    Meizi pages = new Meizi();
                    pages.setPage(page);
                    meizis.add(pages);
                }

                if (msAdapter == null) {
                    recyclerView.setAdapter(msAdapter = new MsAdapter());
                    itemTouchHelper.attachToRecyclerView(recyclerView);
                } else {
                    msAdapter.notifyDataSetChanged();
                }
            }
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    /**
     * RecyclerView的适配器
     */
    class MsAdapter extends RecyclerView.Adapter<MsAdapter.MsViewHolder> implements View.OnClickListener {


        @Override
        public MsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(LineActivity.this)
                    .inflate(R.layout.item_recycler, parent, false);
            MsViewHolder holder = new MsViewHolder(view);
            view.setOnClickListener(this);
            return holder;
        }

        @Override
        public void onBindViewHolder(MsViewHolder holder, int position) {
            holder.tv.setText("妹纸" + position);
            Picasso.with(LineActivity.this)
                    .load(meizis.get(position).getUrl())
                    .into(holder.iv);

        }

        @Override
        public int getItemCount() {
            return meizis.size();
        }

        @Override
        public void onClick(View view) {
            int position = recyclerView.getChildAdapterPosition(view);
            SnackbarUtil.ShortSnackbar(coordinatorLayout, "点击第" + position + "个", SnackbarUtil.Info).show();
            Intent intent = new Intent(LineActivity.this,RecyclerDetailActivity.class);
            intent.putExtra("url",meizis.get(position).getUrl());
            startActivity(intent);
        }

        /**
         * ViewHolder
         */
        class MsViewHolder extends RecyclerView.ViewHolder {
            private ImageView iv;
            private TextView tv;

            public MsViewHolder(View itemView) {
                super(itemView);
                iv = (ImageView)itemView.findViewById(R.id.line_item_iv);
                tv = (TextView)itemView.findViewById(R.id.line_item_tv);
            }
        }

        public void addItem(Meizi meizi, int position) {
            meizis.add(position, meizi);
            notifyItemInserted(position);
            recyclerView.scrollToPosition(position);
        }

        public void removeItem(final int position) {
            final Meizi removed = meizis.get(position);
            meizis.remove(position);
            notifyItemRemoved(position);
            SnackbarUtil.ShortSnackbar(coordinatorLayout, "你删除了第" + position + "个item", SnackbarUtil.Warning).setAction("撤销", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addItem(removed, position);
                    SnackbarUtil.ShortSnackbar(coordinatorLayout, "撤销了删除第" + position + "个item", SnackbarUtil.Confirm).show();
                }
            }).setActionTextColor(Color.WHITE).show();
        }
    }
}
