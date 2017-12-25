package com.zy.mvp.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.zy.mvp.R;
import com.zy.mvp.gallery.GalleryFragment;
import com.zy.mvp.send.SendFragment;
import com.zy.mvp.share.ShareFragment;
import com.zy.mvp.tab.TabFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MainContract.View {
    private static final String TAG = "MainActivity";
    private MainContract.Presenter mMainPresenter;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private TabFragment tabFragment;
    private GalleryFragment galleryFragment;
    private ShareFragment shareFragment;
    private SendFragment sendFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        //创建Activity
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);//让原始的toolbar的内容不现实，使用setTitle的内容
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mMainPresenter = new MainPresenter(this);
        navigationView.setCheckedItem(R.id.nav_camera);
        if (savedInstanceState == null) {
            switch2Camera(R.id.nav_camera);
        } else {//屏幕旋转
            toolbar.setTitle(savedInstanceState.getString("title"));
        }
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("title", toolbar.getTitle().toString());
    }

    @Override
    public void onBackPressed() {
        drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        mMainPresenter.switchNavigation(id);
        item.setChecked(true);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void switch2Camera(int id) {
//        getSupportFragmentManager().beginTransaction().replace(R.id.content_main, CameraFragment.newInstance("", true)).commit();
        if (tabFragment == null) {
            tabFragment = TabFragment.newInstance();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.content_main, tabFragment).commit();
        toolbar.setTitle("Camera");
    }

    @Override
    public void switch2Gallery(int id) {
        if (galleryFragment == null) {
            galleryFragment = GalleryFragment.newInstance("");
            galleryFragment.isShowFooter(true);
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.content_main, galleryFragment).commit();
        toolbar.setTitle("Gallery");
    }

    @Override
    public void switch2Share(int id) {
        if (shareFragment == null) {
            shareFragment = ShareFragment.newInstance("");
            shareFragment.isShowFooter(true);
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.content_main, shareFragment).commit();
        toolbar.setTitle("Share");
    }

    @Override
    public void switch2Send(int id) {
        if (sendFragment == null) {
            sendFragment = SendFragment.newInstance("");
            sendFragment.isShowFooter(true);
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.content_main, sendFragment).commit();
        toolbar.setTitle("Send");
    }
}