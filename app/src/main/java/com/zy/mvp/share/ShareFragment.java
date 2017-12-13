package com.zy.mvp.share;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.zy.mvp.R;
import com.zy.mvp.utils.DividerItemDecoration;
import com.zy.mvp.utils.touchhelper.ItemTouchHelperAdapter;
import com.zy.mvp.utils.touchhelper.SimpleItemTouchHelperCallback;

import java.util.ArrayList;
import java.util.List;

public class ShareFragment extends Fragment implements ShareContract.View {
    private static final String TOKEN = "token";
    private static final String ISSHOWFOOTER = "isShowFooter";
    private String token;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ShareRecyclerViewAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private int pageIndex = 1;
    private boolean isShowFooter = false;
    private List<String> mData;
    private RecyclerView mRecyclerView;
    public static final int PAGE_SIZE = 20;
    private ShareContract.Presenter mListPresenter;

    public static ShareFragment newInstance(String token, boolean isShowFooter) {
        ShareFragment fragment = new ShareFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TOKEN, token);
        bundle.putBoolean(ISSHOWFOOTER, isShowFooter);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            token = getArguments().getString(TOKEN);
            isShowFooter = getArguments().getBoolean(ISSHOWFOOTER);
        }
        mListPresenter = new SharePresenter(this);
        mAdapter = new ShareRecyclerViewAdapter(getContext());
        mAdapter.setOnItemClickListener(new ShareRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Snackbar.make(view, position + " Share", Snackbar.LENGTH_SHORT).show();
            }

//            @Override
//            public void onItemLongClick(View view, int position) {
//                mData.remove(position);
//                mAdapter.notifyItemRemoved(position);
//            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_share, container, false);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe);
        mSwipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);
        mRecyclerView = view.findViewById(R.id.recycler);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(view.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(view.getContext()));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(mOnScrollListener);
        mOnRefreshListener.onRefresh();
        //先实例化Callback
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mAdapter);
        //用Callback构造ItemtouchHelper
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        //调用ItemTouchHelper的attachToRecyclerView方法建立联系
        touchHelper.attachToRecyclerView(mRecyclerView);
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

    private static class ShareRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ItemTouchHelperAdapter {
        private List<String> mData;
        private final Context context;
        private static final int TYPE_ITEM = 0;
        private static final int TYPE_FOOTER = 1;
        private boolean mShowFooter = false;

        public ShareRecyclerViewAdapter(Context context) {
            this.context = context;
        }

        public void setmData(List<String> mData) {
            this.mData = mData;
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
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.recycler_item, parent, false);
                return new ViewHolder(view);
            } else {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.fragment_footer, null);
                view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                return new FooterViewHolder(view);
            }
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            if (holder instanceof ViewHolder) {
                final ViewHolder viewHolder = (ViewHolder) holder;
                viewHolder.mItemId.setText((position + 1) + "");
                viewHolder.mItemName.setText(mData.get(position));
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickListener.onItemClick(v, holder.getLayoutPosition());
                    }
                });
//            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    mOnItemClickListener.onItemLongClick(v, holder.getLayoutPosition());
//                    return false;
//                }
//            });
            }
        }

        @Override
        public int getItemCount() {
            int begin = mShowFooter ? 1 : 0;
            if (mData == null) {
                return begin;
            }
            return mData.size() + begin;
        }

        public void isShowFooter(boolean showFooter) {
            this.mShowFooter = showFooter;
        }

        public boolean isShowFooter() {
            return this.mShowFooter;
        }

        public class FooterViewHolder extends RecyclerView.ViewHolder {
            public FooterViewHolder(View view) {
                super(view);
            }
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView mItemId;
            public TextView mItemName;

            public ViewHolder(View view) {
                super(view);
                mItemId = view.findViewById(R.id.itemid);
                mItemName = view.findViewById(R.id.itemname);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mItemName.getText() + "'";
            }
        }

        public interface OnItemClickListener {
            void onItemClick(View view, int position);

//        void onItemLongClick(View view, int position);
        }

        private OnItemClickListener mOnItemClickListener;

        public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
            this.mOnItemClickListener = mOnItemClickListener;
        }
//    @Override
//    public void onItemMove(int fromPosition, int toPosition) {
//        Collections.swap(mData, fromPosition, toPosition);
//        notifyItemMoved(fromPosition, toPosition);
//    }

        @Override
        public void onItemDissmiss(int position) {
            mData.remove(position);
            notifyItemRemoved(position);
        }
    }
}