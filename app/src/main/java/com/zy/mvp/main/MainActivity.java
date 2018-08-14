package com.zy.mvp.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.zy.mvp.R;
import com.zy.mvp.base.BaseActivity;
import com.zy.mvp.gallery.GalleryFragment;
import com.zy.mvp.send.SendFragment;
import com.zy.mvp.share.ShareFragment;
import com.zy.mvp.tab.TabFragment;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, MainContract.View {
    private MainContract.Presenter mMainPresenter;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //创建Activity
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);//让原始的toolbar使用的app_name的内容不显示，使用setTitle的内容
        }
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
            switch2Camera();
        } else {//屏幕旋转
            toolbar.setTitle(savedInstanceState.getString("title"));
        }
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
    public void switch2Camera() {
//        getSupportFragmentManager().beginTransaction().replace(R.id.content_main, CameraFragment.newInstance("", true)).commit();
        String tag = TabFragment.class.getSimpleName();
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment == null) {
            fragment = TabFragment.newInstance();
        }
        hideAndShowFragment(fragment, tag);
        toolbar.setTitle("Camera");
    }

    @Override
    public void switch2Gallery() {
        String tag = GalleryFragment.class.getSimpleName();
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment == null) {
            fragment = GalleryFragment.newInstance("", true);
        }
        hideAndShowFragment(fragment, tag);
        toolbar.setTitle("Gallery");
    }

    @Override
    public void switch2Share() {
        String tag = ShareFragment.class.getSimpleName();
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment == null) {
            fragment = ShareFragment.newInstance("", true);
        }
        hideAndShowFragment(fragment, tag);
        toolbar.setTitle("Share");
    }

    @Override
    public void switch2Send() {
        String tag = SendFragment.class.getSimpleName();
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment == null) {
            fragment = SendFragment.newInstance("", true);
        }
        hideAndShowFragment(fragment, tag);
        toolbar.setTitle("Send");
    }

    private void hideAndShowFragment(Fragment fragment, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (currentFragment != null) {
            fragmentTransaction.hide(currentFragment);
        }
        if (fragment.isAdded()) {
            fragmentTransaction.show(fragment);
        } else {
            fragmentTransaction.add(R.id.content_main, fragment, tag);
        }
        fragmentTransaction.commit();
        currentFragment = fragment;
    }
}