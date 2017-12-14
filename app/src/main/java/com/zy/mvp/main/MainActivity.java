package com.zy.mvp.main;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.zy.mvp.R;
import com.zy.mvp.gallery.GalleryFragment;
import com.zy.mvp.send.SendFragment;
import com.zy.mvp.share.ShareFragment;
import com.zy.mvp.tab.TabFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MainContract.View {
    private MainContract.Presenter mMainPresenter;
    private Toolbar toolbar;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mMainPresenter = new MainPresenter(this);
        navigationView.setCheckedItem(R.id.nav_camera);
        switch2Camera(R.id.nav_camera);
    }

    @Override
    public void onBackPressed() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
    public boolean onNavigationItemSelected(MenuItem item) {
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
        getSupportFragmentManager().beginTransaction().replace(R.id.content_main, new TabFragment()).commit();
        toolbar.setTitle("Camera");
    }

    @Override
    public void switch2Gallery(int id) {
        getSupportFragmentManager().beginTransaction().replace(R.id.content_main, GalleryFragment.newInstance("", true)).commit();
        toolbar.setTitle("Gallery");
    }

    @Override
    public void switch2Share(int id) {
        getSupportFragmentManager().beginTransaction().replace(R.id.content_main, ShareFragment.newInstance("", true)).commit();
        toolbar.setTitle("Share");
    }

    @Override
    public void switch2Send(int id) {
        getSupportFragmentManager().beginTransaction().replace(R.id.content_main, SendFragment.newInstance("", true)).commit();
        toolbar.setTitle("Send");
    }

}