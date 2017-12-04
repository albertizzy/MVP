package com.zy.mvp.camera.widget;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zy.mvp.R;
import com.zy.mvp.camera.presenter.CameraPresenter;
import com.zy.mvp.camera.presenter.CameraPresenterImpl;
import com.zy.mvp.camera.view.CameraView;
import com.zy.mvp.detail.widget.DetailActivity;
import com.zy.mvp.utils.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class CameraFragment extends Fragment implements CameraView {
    private static final String TOKEN = "token";
    private static final String ISSHOWFOOTER = "isShowFooter";
    private String token;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private CameraRecyclerViewAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private int pageIndex = 1;
    private boolean isShowFooter = false;
    private List<String> mData;
    private RecyclerView mRecyclerView;
    public static final int PAGE_SIZE = 20;
    private CameraPresenter mListPresenter;

    public static CameraFragment newInstance(String token, boolean isShowFooter) {
        CameraFragment fragment = new CameraFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TOKEN, token);
        bundle.putBoolean(ISSHOWFOOTER, isShowFooter);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListPresenter = new CameraPresenterImpl(this);
        if (getArguments() != null) {
            token = getArguments().getString(TOKEN);
            isShowFooter = getArguments().getBoolean(ISSHOWFOOTER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, container, false);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe);
        mSwipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);
        mRecyclerView = view.findViewById(R.id.recycler);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(view.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(view.getContext()));
        mAdapter = new CameraRecyclerViewAdapter(view.getContext());
        mAdapter.setOnItemClickLitener(new CameraRecyclerViewAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
//                Toast.makeText(context, holder.getLayoutPosition() + " Camera", Toast.LENGTH_SHORT).show();
//                Snackbar.make(view, position + " Camera", Snackbar.LENGTH_SHORT).show();
                Intent intent = new Intent(view.getContext(), DetailActivity.class);
//                    View transitionView = view.findViewById(R.id.ivNews);
//                    ActivityOptionsCompat options =
//                            ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
//                                    transitionView, getString(R.string.transition_news_img));
                ActivityCompat.startActivity(view.getContext(), intent, null);
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
        //通过onCreateOptionsMenu()，fragment可以为activity的Options Menu提供菜单项。
        // 为了确保这一方法成功实现回调。必须在onCreate()期间调用setHasOptionsMenu()告知Options Menu fragment要添加菜单项。
        setHasOptionsMenu(true);
        return view;
    }

    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            pageIndex = 1;
            if (mData != null) {
                mData.clear();
            }
            mListPresenter.loadData(token, pageIndex);
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
            //如果数据量小于分页条数了,说明没有数据,则隐藏footer布局
            if (dataList.size() < PAGE_SIZE) {
                mAdapter.isShowFooter(false);
            }
            if (mData == null) {
                mData = new ArrayList<>();
            }
            mData.addAll(dataList);
            mAdapter.setmData(mData);
            mAdapter.notifyDataSetChanged();
            pageIndex++;
        }
//        if (pageIndex == 1) {
//            mAdapter.setmData(mData);
//            if (mData == null || mData.size() < PAGE_SIZE) {
//                mAdapter.isShowFooter(false);
//            }
//        } else {
//            mAdapter.notifyDataSetChanged();
//        }
//        pageIndex++;
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

    @Override
    public void onPause() {
        super.onPause();
        mListPresenter.unsubscribe();
    }
}