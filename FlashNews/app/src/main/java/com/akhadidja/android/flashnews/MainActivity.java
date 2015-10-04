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

import com.akhadidja.android.flashnews.callbacks.StoriesLoadedListener;
import com.akhadidja.android.flashnews.data.FlashNewsSource;
import com.akhadidja.android.flashnews.json.NprApiEndpoints;
import com.akhadidja.android.flashnews.pojos.Story;

// TODO link back to https://icons8.com & https://www.iconfinder.com in About

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, RecyclerView.OnItemTouchListener, StoriesLoadedListener {

    private String mApiKey;
    private GestureDetectorCompat mDetectorCompat;
    private StoryAdapter mStoryAdapter;
    private FlashNewsSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataSource = new FlashNewsSource(this);
        dataSource.open();

        initLayoutFeatures();

        initRecycler();
    }

    private void initLayoutFeatures() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initRecycler() {
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.stories_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addOnItemTouchListener(this);
        mStoryAdapter = new StoryAdapter();
        mRecyclerView.setAdapter(mStoryAdapter);
        mDetectorCompat = new GestureDetectorCompat(this, new StoryListener(this, mRecyclerView));
        mApiKey = getString(R.string.NPR_API_KEY);
    }

    @Override
    protected void onResume() {
        dataSource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        dataSource.close();
        super.onPause();
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
                        new FetchNprNewsTask(this, mApiKey, NprApiEndpoints.TOPIC_NEWS, this);
                newsTask.execute(NprApiEndpoints.TOPIC_NEWS);
                break;
            }
            case R.id.nav_sports:{
                setTitle(R.string.sports);
                FetchNprNewsTask sportsTask =
                        new FetchNprNewsTask(this, mApiKey, NprApiEndpoints.TOPIC_SPORTS, this);
                sportsTask.execute(NprApiEndpoints.TOPIC_SPORTS);
                break;
            }
            case R.id.nav_science:{
                setTitle(R.string.science);
                FetchNprNewsTask scienceTask =
                        new FetchNprNewsTask(this, mApiKey, NprApiEndpoints.TOPIC_SCIENCE, this);
                scienceTask.execute(NprApiEndpoints.TOPIC_SCIENCE);
                break;
            }
            case R.id.nav_tech:{
                setTitle(R.string.technology);
                FetchNprNewsTask techTask =
                        new FetchNprNewsTask(this, mApiKey, NprApiEndpoints.TOPIC_TECH, this);
                techTask.execute(NprApiEndpoints.TOPIC_TECH);
                break;
            }
            case R.id.nav_world:{
                setTitle(R.string.world);
                FetchNprNewsTask worldTask =
                        new FetchNprNewsTask(this, mApiKey, NprApiEndpoints.TOPIC_WORLD, this);
                worldTask.execute(NprApiEndpoints.TOPIC_WORLD);
                break;
            }
            case R.id.nav_politics:{
                setTitle(R.string.politics);
                FetchNprNewsTask politicsTask =
                        new FetchNprNewsTask(this, mApiKey, NprApiEndpoints.TOPIC_POLITICS, this);
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

    @Override
    public void onStoriesLoadedListener(Story[] stories) {
        mStoryAdapter.setStories(stories);
    }
}
