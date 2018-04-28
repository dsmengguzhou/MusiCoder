package com.ms.awe.musicoder.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ms.awe.musicoder.R;
import com.ms.awe.musicoder.adapter.GridAdapter;
import com.ms.awe.musicoder.bean.Meizi;
import com.ms.awe.musicoder.okhttp.MyOkhttp;
import com.ms.awe.musicoder.util.SnackbarUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by awe on 2018/4/25.
 */

public class StaggeredActivity extends AppCompatActivity {

    private CoordinatorLayout coordinatorLayout;
    private RecyclerView recyclerview;
    private GridAdapter mAdapter;
    private List<Meizi> meizis;
    private StaggeredGridLayoutManager mLayoutManager;

    private int page = 1;
    private int lastVisibleItem;
    private ItemTouchHelper itemTouchHelper;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staggered);

        initView();
        setListener();

        new GetData().execute("http://gank.io/api/data/福利/10/1");

    }

    private void initView() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("好看的小姐姐");                           //设置主标题
        toolbar.inflateMenu(R.menu.base_toolbar_menu);              //设置Menu
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_item1:
                        Toast.makeText(StaggeredActivity.this,"好看的小姐姐Item1",Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                }
                return true;
            }
        });

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.staggered_coordinatorLayout);

        recyclerview = (RecyclerView) findViewById(R.id.staggered_recycler);
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(mLayoutManager);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.staggered_swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorAccent);
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
                int dragFlags = 0;
                if (recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
                    dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                }
                return makeMovementFlags(dragFlags, 0);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                int from = viewHolder.getAdapterPosition();
                int to = target.getAdapterPosition();
                Meizi moveItem = meizis.get(from);
                meizis.remove(from);
                meizis.add(to, moveItem);
                mAdapter.notifyItemMoved(from, to);
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

            }

            @Override
            public boolean isLongPressDragEnabled() {
                return false;
            }
        });

        recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
//                0：当前屏幕停止滚动；1时：屏幕在滚动 且 用户仍在触碰或手指还在屏幕上；2时：随用户的操作，屏幕上产生的惯性滑动；
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem + 2 >= mLayoutManager.getItemCount()) {
                    new GetData().execute("http://gank.io/api/data/福利/10/" + (++page));
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int[] positions = mLayoutManager.findLastVisibleItemPositions(null);
                lastVisibleItem = Math.max(positions[0], positions[1]);
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

                if (mAdapter == null) {
                    recyclerview.setAdapter(mAdapter = new GridAdapter(StaggeredActivity.this, meizis));

                    //设置点击监听
                    mAdapter.setOnItemClickListener(new GridAdapter.OnRecyclerViewItemClickListener() {
                        @Override
                        public void onItemClick(View view) {
                            int position = recyclerview.getChildAdapterPosition(view);
                            SnackbarUtil.ShortSnackbar(coordinatorLayout, "点击第" + position + "个", SnackbarUtil.Info).show();
                            Intent intent = new Intent(StaggeredActivity.this,RecyclerDetailActivity.class);
                            intent.putExtra("url",meizis.get(position).getUrl());
                            startActivity(intent);
                        }

                        @Override
                        public void onItemLongClick(View view) {
                            itemTouchHelper.startDrag(recyclerview.getChildViewHolder(view));
                        }
                    });

                    itemTouchHelper.attachToRecyclerView(recyclerview);
                } else {
                    mAdapter.notifyDataSetChanged();
                }
            }
            swipeRefreshLayout.setRefreshing(false);
        }
    }
}
