package com.akhadidja.android.flashnews;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.akhadidja.android.flashnews.data.FlashNewsSource;
import com.akhadidja.android.flashnews.json.NprApiEndpoints;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String STORIES_FRAGMENT_KEY = "stories_fragment";
    private static final String NEWS_CATEGORY_KEY = "news_category";
    private static final String ACTIVITY_TITLE = "activity_title";

    private Fragment mFragment = null;
    private FragmentManager mFragmentManager;
    private DrawerLayout drawer;
    private NavigationView mNavigationView;
    private int mDrawerCheckedItemId;

    private FlashNewsSource mDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initLayoutFeatures();
        mDataSource = new FlashNewsSource(this);
        mDataSource.open();

        mFragmentManager = getFragmentManager();
        if(savedInstanceState != null){
            mFragment = mFragmentManager.getFragment(savedInstanceState, STORIES_FRAGMENT_KEY);
            mDrawerCheckedItemId = savedInstanceState.getInt(NEWS_CATEGORY_KEY);
            mNavigationView.setCheckedItem(mDrawerCheckedItemId);
            setTitle(savedInstanceState.getString(ACTIVITY_TITLE));

            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.stories_fragment_container, mFragment);
            fragmentTransaction.commit();
        } else {
            drawer.openDrawer(GravityCompat.START);
        }
    }

    private void initLayoutFeatures() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(mFragment != null)
            mFragmentManager.putFragment(outState, STORIES_FRAGMENT_KEY, mFragment);
        outState.putInt(NEWS_CATEGORY_KEY, mDrawerCheckedItemId);
        outState.putString(ACTIVITY_TITLE, String.valueOf(getTitle()));
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
            setTitle(R.string.title_activity_settings);
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            mFragment = SettingsFragment.newInstance();
            fragmentTransaction.replace(R.id.stories_fragment_container, mFragment);
            fragmentTransaction.commit();
            mDrawerCheckedItemId = R.id.nav_settings;
            mNavigationView.setCheckedItem(mDrawerCheckedItemId);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        mDrawerCheckedItemId = item.getItemId();

        switch (mDrawerCheckedItemId) {
            case R.id.nav_news:{
                setTitle(R.string.news);
                mFragment = StoriesFragment.newInstance(NprApiEndpoints.TOPIC_NEWS);
                break;
            }
            case R.id.nav_sports:{
                setTitle(R.string.sports);
                mFragment = StoriesFragment.newInstance(NprApiEndpoints.TOPIC_SPORTS);
                break;
            }
            case R.id.nav_science:{
                setTitle(R.string.science);
                mFragment = StoriesFragment.newInstance(NprApiEndpoints.TOPIC_SCIENCE);
                break;
            }
            case R.id.nav_tech:{
                setTitle(R.string.technology);
                mFragment = StoriesFragment.newInstance(NprApiEndpoints.TOPIC_TECH);
                break;
            }
            case R.id.nav_world:{
                setTitle(R.string.world);
                mFragment = StoriesFragment.newInstance(NprApiEndpoints.TOPIC_WORLD);
                break;
            }
            case R.id.nav_politics:{
                setTitle(R.string.politics);
                mFragment = StoriesFragment.newInstance(NprApiEndpoints.TOPIC_POLITICS);
                break;
            }
            case R.id.nav_favorites:{
                setTitle(R.string.favorites);
                mFragment = FavoriteStoriesFragment.newInstance();
                break;
            }
            case R.id.nav_settings:{
                setTitle(getString(R.string.action_settings));
                mFragment = SettingsFragment.newInstance();
            }
            default:
                setTitle(R.string.app_name);
                break;
        }
        fragmentTransaction.replace(R.id.stories_fragment_container, mFragment);
        fragmentTransaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        mDataSource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mDataSource.close();
        super.onPause();
    }
}
