package com.zy.mvp.camera;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.zy.mvp.R;
import com.zy.mvp.detail.DetailActivity;
import com.zy.mvp.divider.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class CameraFragment extends Fragment implements CameraContract.View {
    private static final String TAG = "CameraFragment";
    private static final String TOKEN = "token";
    private static final String IS_SHOW_FOOTER = "isShowFooter";
    private String token;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private CameraRecyclerViewAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private int pageIndex = 1;
    public static final int PAGE_SIZE = 20;
    private CameraContract.Presenter mListPresenter;
    private boolean isFirstVisibleToUser = true;

    public static CameraFragment newInstance(String token, Boolean isShowFooter) {
        CameraFragment fragment = new CameraFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TOKEN, token);
        bundle.putBoolean(IS_SHOW_FOOTER, isShowFooter);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach");
        //当Fragment与Activity发生关联时调用
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        boolean isShowFooter = false;
        if (getArguments() != null) {
            token = getArguments().getString(TOKEN);
            isShowFooter = getArguments().getBoolean(IS_SHOW_FOOTER);
        }
        mListPresenter = new CameraPresenter(this);
        mAdapter = new CameraRecyclerViewAdapter(getContext());
        mAdapter.mShowFooter = isShowFooter;
        mAdapter.setOnItemClickListener(mOnItemClickListener);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_camera, container, false);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe);
        mSwipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);
        RecyclerView mRecyclerView = view.findViewById(R.id.recycler);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(view.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(view.getContext()));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(mOnScrollListener);
        //通过onCreateOptionsMenu()，fragment可以为activity的Options Menu提供菜单项。
        // 为了确保这一方法成功实现回调。必须在onCreate()期间调用setHasOptionsMenu()告知Options Menu fragment要添加菜单项。
        setHasOptionsMenu(true);
        if (savedInstanceState == null) {
            mOnRefreshListener.onRefresh();
        } else {//屏幕旋转
            mAdapter.mShowFooter = savedInstanceState.getBoolean("isShowFooter");
            mAdapter.data = savedInstanceState.getStringArrayList("data");
            pageIndex = savedInstanceState.getInt("pageIndex");
            isFirstVisibleToUser = savedInstanceState.getBoolean("isFirstVisibleToUser");
            addData(mAdapter.data);
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated");
        //当Activity的onCreate方法返回时调用
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
        mListPresenter.unSubscribe();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView");
        //与onCreateView相对应，当该Fragment的视图被移除时调用
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach");
        //与onAttach相对应，当Fragment与Activity关联被取消时调用
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isShowFooter", mAdapter.mShowFooter);
        outState.putStringArrayList("data", mAdapter.data);
        outState.putInt("pageIndex", pageIndex);
        outState.putBoolean("isFirstVisibleToUser", isFirstVisibleToUser);
    }

    /**
     * 懒加载
     *
     * @param isVisibleToUser 用户是否可见
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (getView() != null && isFirstVisibleToUser) {
                mOnRefreshListener.onRefresh();
            }
        }
    }

    private final CameraRecyclerViewAdapter.OnItemClickListener mOnItemClickListener = new CameraRecyclerViewAdapter.OnItemClickListener() {
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
            mAdapter.data.remove(position);
            mAdapter.notifyItemRemoved(position);
        }
    };
    private final SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            if (getUserVisibleHint()) {
                pageIndex = 1;
                mAdapter.data.clear();
                mListPresenter.loadData(token, pageIndex);
                isFirstVisibleToUser = false;
            }
        }
    };
    private final RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        private int lastVisibleItem;

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE
                    && lastVisibleItem + 1 == mAdapter.getItemCount()
                    && mAdapter.mShowFooter) {
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
        //如果没有更多数据了,则隐藏footer布局
        if (dataList == null || dataList.isEmpty()) {
            mAdapter.mShowFooter = false;
        } else {
            //如果数据量小于分页条数了,说明没有数据,则隐藏footer布局
            if (dataList.size() < PAGE_SIZE) {
                mAdapter.mShowFooter = false;
            }
            mAdapter.setData(dataList);
            mAdapter.notifyDataSetChanged();
            pageIndex++;
        }
//        if (pageIndex == 1) {
//            mAdapter.setData(dataList);
//            if (mAdapter.data.size() < PAGE_SIZE) {
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
            mAdapter.mShowFooter = false;
            mAdapter.notifyDataSetChanged();
        }
//                View view = getActivity() == null ? mRecyclerView.getRootView() : getActivity().findViewById(R.id.drawer_layout);
//                Snackbar.make(view, "加载失败！", Snackbar.LENGTH_SHORT).show();
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    private static class CameraRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private ArrayList<String> data;
        private final Context context;
        private static final int TYPE_ITEM = 0;
        private static final int TYPE_FOOTER = 1;
        private boolean mShowFooter = false;
        private final LayoutInflater mLayoutInflater;

        public CameraRecyclerViewAdapter(Context context) {
            this.context = context;
            this.mLayoutInflater = LayoutInflater.from(context);
            this.data = new ArrayList<>();
        }

        public void setData(List<String> data) {
            this.data.addAll(data);
            this.notifyDataSetChanged();
        }

        @Override
        public int getItemViewType(int position) {
            // 最后一个item设置为footerView
            if (!mShowFooter) {
                return TYPE_ITEM;
            }
            if (position + 1 == getItemCount()) {
                return TYPE_FOOTER;
            } else {
                return TYPE_ITEM;
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_ITEM) {
                View view = mLayoutInflater.inflate(R.layout.recycler_item, parent, false);
                final MyViewHolder myViewHolder = new MyViewHolder(view);
                myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickListener.onItemClick(v, myViewHolder.getLayoutPosition());
                    }
                });
                myViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        mOnItemClickListener.onItemLongClick(v, myViewHolder.getLayoutPosition());
                        return false;
                    }
                });
                return myViewHolder;
            } else {
                View view = mLayoutInflater.inflate(R.layout.fragment_footer, null);
                view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                return new FooterViewHolder(view);
            }
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            if (getItemViewType(position) == TYPE_ITEM) {
                MyViewHolder myViewHolder = (MyViewHolder) holder;
                myViewHolder.mItemId.setText(String.valueOf(position + 1));
                myViewHolder.mItemName.setText(data.get(position));
            }
        }

        @Override
        public int getItemCount() {
            if (data.isEmpty()) {//数据empty时，不显示footer
                return 0;
            } else {
                return data.size() + (mShowFooter ? 1 : 0);
            }
        }

        private class FooterViewHolder extends RecyclerView.ViewHolder {
            private FooterViewHolder(View view) {
                super(view);
            }
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            private final TextView mItemId;
            private final TextView mItemName;

            private MyViewHolder(View view) {
                super(view);
                mItemId = view.findViewById(R.id.item_id);
                mItemName = view.findViewById(R.id.item_name);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mItemName.getText() + "'";
            }
        }

        public interface OnItemClickListener {
            void onItemClick(View view, int position);

            void onItemLongClick(View view, int position);
        }

        private OnItemClickListener mOnItemClickListener;

        private void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
            this.mOnItemClickListener = mOnItemClickListener;
        }
    }
}