package com.akhadidja.android.flashnews;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;

import com.akhadidja.android.flashnews.json.NprApiEndpoints;

// TODO link back to https://icons8.com & https://www.iconfinder.com in About

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, RecyclerView.OnItemTouchListener {

    private RecyclerView mRecyclerView;
    private String mApiKey;
    private GestureDetectorCompat mDetectorCompat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.stories_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addOnItemTouchListener(this);
        mDetectorCompat = new GestureDetectorCompat(this, new StoryListener(this, mRecyclerView));
        mApiKey = getString(R.string.NPR_API_KEY);

        FetchNprNewsTask fetchNprNewsTask = new FetchNprNewsTask(mApiKey, mRecyclerView);
        fetchNprNewsTask.execute(NprApiEndpoints.TOPIC_NEWS);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_news:{
                setTitle(R.string.news);
                FetchNprNewsTask newsTask =
                        new FetchNprNewsTask(mApiKey, mRecyclerView);
                newsTask.execute(NprApiEndpoints.TOPIC_NEWS);
                break;
            }
            case R.id.nav_sports:{
                setTitle(R.string.sports);
                FetchNprNewsTask sportsTask =
                        new FetchNprNewsTask(mApiKey, mRecyclerView);
                sportsTask.execute(NprApiEndpoints.TOPIC_SPORTS);
                break;
            }
            case R.id.nav_science:{
                setTitle(R.string.science);
                FetchNprNewsTask scienceTask =
                        new FetchNprNewsTask(mApiKey, mRecyclerView);
                scienceTask.execute(NprApiEndpoints.TOPIC_SCIENCE);
                break;
            }
            case R.id.nav_tech:{
                setTitle(R.string.technology);
                FetchNprNewsTask techTask =
                        new FetchNprNewsTask(mApiKey, mRecyclerView);
                techTask.execute(NprApiEndpoints.TOPIC_TECH);
                break;
            }
            case R.id.nav_world:{
                setTitle(R.string.world);
                FetchNprNewsTask worldTask =
                        new FetchNprNewsTask(mApiKey, mRecyclerView);
                worldTask.execute(NprApiEndpoints.TOPIC_WORLD);
                break;
            }
            case R.id.nav_politics:{
                setTitle(R.string.politics);
                FetchNprNewsTask politicsTask =
                        new FetchNprNewsTask(mApiKey, mRecyclerView);
                politicsTask.execute(NprApiEndpoints.TOPIC_POLITICS);
                break;
            }
            default:
                setTitle(R.string.app_name);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        mDetectorCompat.onTouchEvent(e);
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
