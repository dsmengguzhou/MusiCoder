package com.ms.awe.musicoder.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
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
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ms.awe.musicoder.R;
import com.ms.awe.musicoder.bean.Meizi;
import com.ms.awe.musicoder.okhttp.MyOkhttp;
import com.ms.awe.musicoder.util.SnackbarUtil;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.List;

/**
 * Created by awe on 2018/4/24.
 */

public class SmoothLineActivity extends AppCompatActivity {

    private CoordinatorLayout coordinatorLayout;            //根布局
    private static RecyclerView recyclerView;
    private MsAdapter msAdapter;                            //RecyclerView适配器
    private LinearLayoutManager mLayoutManager;             //RecyclerView布局管理器
    private SwipeRefreshLayout swipeRefreshLayout;          //刷新布局
    private List<Meizi> meizis;
    private ItemTouchHelper itemTouchHelper;
    private int page = 1;                                     //首页
    private int lastVisibleItem;                           //最后一个显示的Item
    private int screenwidth;
    private boolean remove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line);

        initViews();
        setListener();
        new GetData().execute("http://gank.io/api/data/福利/10/1");
    }

    private void initViews() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("好看的小姐姐");                           //设置主标题
        toolbar.inflateMenu(R.menu.base_toolbar_menu);              //设置Menu
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_item1:
                        Toast.makeText(SmoothLineActivity.this,"好看的小姐姐Item1",Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                }
                return true;
            }
        });

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.line_coordinatorLayout);

        recyclerView = (RecyclerView) findViewById(R.id.line_recycler);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.line_swipe_refresh);
        swipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
    }

    private void setListener() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                new GetData().execute("http://gank.io/api/data/福利/10/1");
            }
        });

        itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int dragFlags = 0, swipeFlags = 0;
                if (recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
                    dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                } else if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                    dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                    //设置侧滑方向为从左到右和从右到左都可以
                    swipeFlags = ItemTouchHelper.LEFT;
                }
                return makeMovementFlags(dragFlags, swipeFlags);
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
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        viewHolder.itemView.setElevation(100);
                    }
                }
            }

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    viewHolder.itemView.setElevation(0);
                }

            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                viewHolder.itemView.scrollTo(-(int) dX, -(int) dY);//根据item的滑动偏移修改HorizontalScrollView的滚动
                if (Math.abs(dX) > screenwidth / 5 && !remove && isCurrentlyActive) {
                    //用户收滑动item超过屏幕5分之1，标记为要删除
                    remove = true;
                } else if (Math.abs(dX) < screenwidth / 5 && remove && !isCurrentlyActive) {
                    //用户收滑动item没有超过屏幕5分之1，标记为不删除
                    remove = false;
                }
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE && remove == true && !isCurrentlyActive) {
                    //当用户滑动tem超过屏幕5分之1，并且松手时，执行删除item
                    if (viewHolder != null && viewHolder.getAdapterPosition() >= 0) {
                        msAdapter.removeItem(viewHolder.getAdapterPosition());
                        remove = false;
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            //  0：当前屏幕停止滚动；1时：屏幕在滚动 且 用户仍在触碰或手指还在屏幕上；2时：随用户的操作，屏幕上产生的惯性滑动；

                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem + 2 >= mLayoutManager.getItemCount()) {
                    new GetData().execute("http://gank.io/api/data/福利/10/" + (++page));
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();

            }

        });
    }

    private class GetData extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            swipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected String doInBackground(String... params) {

            return MyOkhttp.get(params[0]);
        }

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
                    List<Meizi> more = gson.fromJson(jsonData, new TypeToken<List<Meizi>>() {
                    }.getType());
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

    class MsAdapter extends RecyclerView.Adapter<MsAdapter.MsViewHolder> {

        @Override
        public MsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(
                    SmoothLineActivity.this).inflate(R.layout.item_recycler_two, parent,
                    false);
            MsViewHolder holder = new MsViewHolder(view);

            WindowManager wm = (WindowManager) SmoothLineActivity.this
                    .getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics outMetrics = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(outMetrics);
            screenwidth = outMetrics.widthPixels;
            return holder;

        }

        @Override
        public void onBindViewHolder(MsViewHolder holder, final int position) {
            if (holder.itemView.getScrollX() != 0) {
                ((HorizontalScrollView) holder.itemView).fullScroll(View.FOCUS_UP);//如果item的HorizontalScrollView没在初始位置，则滚动回顶部
            }
            holder.ll.setMinimumWidth(screenwidth);//设置LinearLayout宽度为屏幕宽度
            holder.tv.setText("图" + position);
            Picasso.with(SmoothLineActivity.this).load(meizis.get(position).getUrl()).into(holder.iv);

            holder.ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SnackbarUtil.ShortSnackbar(coordinatorLayout, "点击第" + position + "个", SnackbarUtil.Info).show();
                    Intent intent = new Intent(SmoothLineActivity.this,RecyclerDetailActivity.class);
                    intent.putExtra("url",meizis.get(position).getUrl());
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return meizis.size();
        }

        class MsViewHolder extends RecyclerView.ViewHolder {
            private ImageView iv;
            private TextView tv;
            private LinearLayout ll;

            public MsViewHolder(View view) {
                super(view);
                iv = (ImageView) view.findViewById(R.id.line_item_iv);
                tv = (TextView) view.findViewById(R.id.line_item_tv);
                ll = (LinearLayout) view.findViewById(R.id.ll);
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
                public void onClick(View v) {
                    addItem(removed, position);
                    SnackbarUtil.ShortSnackbar(coordinatorLayout, "撤销了删除第" + position + "个item", SnackbarUtil.Confirm).show();
                }
            }).setActionTextColor(Color.WHITE).show();
        }

        public void removeItem(Meizi meizi) {
            int position = meizis.indexOf(meizi);
            meizis.remove(position);
            notifyItemRemoved(position);
        }

    }
}
