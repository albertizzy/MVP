package com.zy.mvp.gallery.widget;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zy.mvp.R;
import com.zy.mvp.gallery.presenter.GalleryPresenter;
import com.zy.mvp.gallery.presenter.GalleryPresenterImpl;
import com.zy.mvp.gallery.view.GalleryView;
import com.zy.mvp.utils.DividerItemDecoration;
import com.zy.mvp.utils.touchhelper.SimpleItemTouchHelperCallback;

import java.util.ArrayList;
import java.util.List;

public class GalleryFragment extends Fragment implements GalleryView {
    private static final String TOKEN = "token";
    private static final String ISSHOWFOOTER = "isShowFooter";
    private String token;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private GalleryRecyclerViewAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private int pageIndex = 1;
    private boolean isShowFooter = false;
    private List<String> mData;
    private RecyclerView mRecyclerView;
    public static final int PAGE_SIZE = 20;
    private GalleryPresenter mListPresenter;

    public static GalleryFragment newInstance(String token, boolean isShowFooter) {
        GalleryFragment fragment = new GalleryFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TOKEN, token);
        bundle.putBoolean(ISSHOWFOOTER, isShowFooter);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListPresenter = new GalleryPresenterImpl(this);
        if (getArguments() != null) {
            token = getArguments().getString(TOKEN);
            isShowFooter = getArguments().getBoolean(ISSHOWFOOTER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe);
        mSwipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);
        mRecyclerView = view.findViewById(R.id.recycler);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(view.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(view.getContext()));
        mAdapter = new GalleryRecyclerViewAdapter(view.getContext());
        mAdapter.setOnItemClickLitener(new GalleryRecyclerViewAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Snackbar.make(view, position + " Gallery", Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {
                mData.remove(position);
                mAdapter.notifyItemRemoved(position);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(mOnScrollListener);
        mOnRefreshListener.onRefresh();
        //先实例化Callback
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mAdapter);
        //用Callback构造ItemtouchHelper
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        //调用ItemTouchHelper的attachToRecyclerView方法建立联系
        touchHelper.attachToRecyclerView(mRecyclerView);
        return view;
    }

    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            new Handler().postDelayed(new Thread() {
                @Override
                public void run() {
                    pageIndex = 1;
                    if (mData != null) {
                        mData.clear();
                    }
                    mListPresenter.loadData(token, pageIndex);
                }
            }, 1000);
        }
    };
    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        private int lastVisibleItem;

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE
                    && lastVisibleItem + 1 == mAdapter.getItemCount()
                    && mAdapter.isShowFooter() && isShowFooter) {
                //加载更多
                mListPresenter.loadData(token, pageIndex);
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
        }
    };

    @Override
    public void hideProgress() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showProgress() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void addData(final List<String> dataList) {
        if (isShowFooter) {
            mAdapter.isShowFooter(true);
        }
        //如果没有更多数据了,则隐藏footer布局
        if (dataList == null || dataList.isEmpty()) {
            mAdapter.isShowFooter(false);
        } else {
            if (mData == null) {
                mData = new ArrayList<>();
            }
            mData.addAll(dataList);
        }
        if (pageIndex == 1) {
            mAdapter.setmData(mData);
            if (mData == null || mData.size() < PAGE_SIZE) {
                mAdapter.isShowFooter(false);
            }
        } else {
            mAdapter.notifyDataSetChanged();
        }
        pageIndex++;
    }

    @Override
    public void showLoadFailMsg(final String message) {
        if (pageIndex == 1) {
            mAdapter.isShowFooter(false);
            mAdapter.notifyDataSetChanged();
        }
//                View view = getActivity() == null ? mRecyclerView.getRootView() : getActivity().findViewById(R.id.drawer_layout);
//                Snackbar.make(view, "加载失败！", Snackbar.LENGTH_SHORT).show();
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}