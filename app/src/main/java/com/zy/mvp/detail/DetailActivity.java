package com.zy.mvp.detail;

import android.os.Bundle;
import android.widget.ProgressBar;

import com.zy.mvp.R;
import com.zy.mvp.utils.ToolsUtil;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class DetailActivity extends SwipeBackActivity implements DetailView {
    //    private NewsBean mNews;
//    private HtmlTextView mTVNewsContent;
//    private NewsDetailPresenter mNewsDetailPresenter;
    private ProgressBar mProgressBar;
    private SwipeBackLayout mSwipeBackLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        mProgressBar = (ProgressBar) findViewById(R.id.progress);
//        mTVNewsContent = (HtmlTextView) findViewById(R.id.htNewsContent);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onBackPressed();
//            }
//        });
        mSwipeBackLayout = getSwipeBackLayout();
        mSwipeBackLayout.setEdgeSize(ToolsUtil.getWidthInPx(this));
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
//        mNews = (NewsBean) getIntent().getSerializableExtra("news");
//        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
//        collapsingToolbar.setTitle(mNews.getTitle());
//        ImageLoaderUtils.display(getApplicationContext(), (ImageView) findViewById(R.id.ivImage), mNews.getImgsrc());
//        mNewsDetailPresenter = new NewsDetailPresenterImpl(getApplication(), this);
//        mNewsDetailPresenter.loadNewsDetail(mNews.getDocid());
    }

    @Override
    public void showDetialContent(String detailContent) {
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
