package com.zy.mvp.detail;

import android.os.Bundle;
import android.util.Log;

import com.zy.mvp.R;
import com.zy.mvp.utils.ToolsUtil;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class DetailActivity extends SwipeBackActivity implements DetailView {
    private static final String TAG = "DetailActivity";

    //    private NewsBean mNews;
//    private HtmlTextView mTVNewsContent;
//    private NewsDetailPresenter mNewsDetailPresenter;
//    private ProgressBar mProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        //创建Activity
        setContentView(R.layout.activity_detail);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        mProgressBar = (ProgressBar) findViewById(R.id.progress);
//        mTVNewsContent = (HtmlTextView) findViewById(R.id.htNewsContent);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });
        SwipeBackLayout mSwipeBackLayout = getSwipeBackLayout();
        mSwipeBackLayout.setEdgeSize(ToolsUtil.getWidthInPx(this));
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
//        mNews = (NewsBean) getIntent().getSerializableExtra("news");
//        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
//        collapsingToolbar.setTitle(mNews.getTitle());
//        ImageLoaderUtils.display(getApplicationContext(), (ImageView) findViewById(R.id.ivImage), mNews.getImgSrc());
//        mNewsDetailPresenter = new NewsDetailPresenterImpl(getApplication(), this);
//        mNewsDetailPresenter.loadNewsDetail(mNews.getDocId());
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
        //Activity快要变成可见的
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        //Activity变成可见的，处于运行状态
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
        //其他Activity获得用户焦点，（Activity快要暂停了）
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
        //Activity不再可见，处于停止状态
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        ////Activity快要被销毁了
    }

    @Override
    public void showDetailContent(String detailContent) {
//        mTVNewsContent.setHtmlFromString(detailContent, new HtmlTextView.LocalImageGetter());
    }

    @Override
    public void showProgress() {
//        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
//        mProgressBar.setVisibility(View.GONE);
    }
}
