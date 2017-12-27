package com.zy.mvp.send;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
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

public class SendFragment extends Fragment implements SendContract.View {
    private static final String TAG = "SendFragment";
    private static final String TOKEN = "token";
    private String token;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private SendRecyclerViewAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private int pageIndex = 1;
    private boolean isShowFooter;
    private ArrayList<String> data;
    public static final int PAGE_SIZE = 20;
    private SendContract.Presenter mListPresenter;

    public static SendFragment newInstance(String token) {
        SendFragment fragment = new SendFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TOKEN, token);
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
        if (getArguments() != null) {
            token = getArguments().getString(TOKEN);
        }
        mListPresenter = new SendPresenter(this);
        mAdapter = new SendRecyclerViewAdapter(getContext());
        mAdapter.setOnItemClickListener(mOnItemClickListener);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_send, container, false);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe);
        mSwipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);
        RecyclerView mRecyclerView = view.findViewById(R.id.recycler);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(view.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(view.getContext()));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(mOnScrollListener);
        //先实例化Callback
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mAdapter);
        //用Callback构造ItemTouchHelper
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        //调用ItemTouchHelper的attachToRecyclerView方法建立联系
        touchHelper.attachToRecyclerView(mRecyclerView);
        //通过onCreateOptionsMenu()，fragment可以为activity的Options Menu提供菜单项。
        // 为了确保这一方法成功实现回调。必须在onCreate()期间调用setHasOptionsMenu()告知Options Menu fragment要添加菜单项。
        setHasOptionsMenu(true);
        if (savedInstanceState == null) {
            mOnRefreshListener.onRefresh();
        } else {//屏幕旋转
            isShowFooter = savedInstanceState.getBoolean("isShowFooter");
            data = savedInstanceState.getStringArrayList("data");
            pageIndex = savedInstanceState.getInt("pageIndex");
            addData(data);
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
        outState.putBoolean("isShowFooter", isShowFooter);
        outState.putStringArrayList("data", data);
        outState.putInt("pageIndex", pageIndex);
    }

    private final SendRecyclerViewAdapter.OnItemClickListener mOnItemClickListener = new SendRecyclerViewAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            Snackbar.make(view, position + " Send", Snackbar.LENGTH_SHORT).show();
        }
//            @Override
//            public void onItemLongClick(View view, int position) {
//                data.remove(position);
//                mAdapter.notifyItemRemoved(position);
//            }
    };
    private final SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            pageIndex = 1;
            if (data != null) {
                data.clear();
            }
            mListPresenter.loadData(token, pageIndex);
        }
    };
    private final RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
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
            if (data == null) {
                data = new ArrayList<>();
            }
            data.addAll(dataList);
            mAdapter.setData(data);
            mAdapter.notifyDataSetChanged();
            pageIndex++;
        }
//        if (pageIndex == 1) {
//            mAdapter.setData(data);
//            if (data == null || data.size() < PAGE_SIZE) {
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

    public void isShowFooter(boolean showFooter) {
        isShowFooter = showFooter;
    }

    private static class SendRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ItemTouchHelperAdapter {
        private List<String> data;
        private final Context context;
        private static final int TYPE_ITEM = 0;
        private static final int TYPE_FOOTER = 1;
        private boolean mShowFooter = false;
        private final LayoutInflater mLayoutInflater;

        public SendRecyclerViewAdapter(Context context) {
            this.context = context;
            this.mLayoutInflater = LayoutInflater.from(context);
        }

        public void setData(List<String> data) {
            this.data = data;
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
//            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    mOnItemClickListener.onItemLongClick(viewHolder, holder.getLayoutPosition());
//                    return false;
//                }
//            });
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
            if (holder instanceof SendRecyclerViewAdapter.MyViewHolder) {
                SendRecyclerViewAdapter.MyViewHolder myViewHolder = (SendRecyclerViewAdapter.MyViewHolder) holder;
                myViewHolder.mItemId.setText(String.valueOf(position + 1));
                myViewHolder.mItemName.setText(data.get(position));
            }
        }

        @Override
        public int getItemCount() {
            int begin = mShowFooter ? 1 : 0;
            if (data == null) {
                return begin;
            }
            return data.size() + begin;
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

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public final TextView mItemId;
            public final TextView mItemName;

            public MyViewHolder(View view) {
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
//        Collections.swap(data, fromPosition, toPosition);
//        notifyItemMoved(fromPosition, toPosition);
//    }
        @Override
        public void onItemDismiss(int position) {
            data.remove(position);
            notifyItemRemoved(position);
        }
    }
}