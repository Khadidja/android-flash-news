package com.akhadidja.android.flashnews;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.akhadidja.android.flashnews.adapters.FullStoryViewPagerAdapter;
import com.akhadidja.android.flashnews.data.FlashNewsSource;
import com.akhadidja.android.flashnews.pojos.Story;
import com.viewpagerindicator.UnderlinePageIndicator;

import java.util.ArrayList;

public class FullStoryActivity extends AppCompatActivity {

    private static final String LOG_TAG = FullStoryActivity.class.getSimpleName();
    private final static String FULL_STORY_FRAG = "full_story_frag";
    private ShareActionProvider mShareActionProvider;
    private ArrayList<Story> mStories;
    private int mPosition;
    private String mTopic;
    private FlashNewsSource dataSource;
    private boolean isFav;
    private FullStoryViewPagerAdapter pagerAdapter;
    private Menu mOptionsMenu;
    private String mFragmentType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_story);
        Toolbar toolbar = (Toolbar) findViewById(R.id.full_story_toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        dataSource = new FlashNewsSource(this);
        dataSource.open();

        if(savedInstanceState != null){
            mFragmentType = savedInstanceState.getString(FlashNewsApplication.EXTRA_FRAG_TYPE);
            mTopic = savedInstanceState.getString(FlashNewsApplication.EXTRA_TOPIC);
            mPosition = savedInstanceState.getInt(FlashNewsApplication.EXTRA_STORY_POSITION);
            mStories = savedInstanceState.getParcelableArrayList(FlashNewsApplication.EXTRA_STORIES);
            isFav = savedInstanceState.getBoolean(FlashNewsApplication.EXTRA_IS_FAV);
        } else {
            initFromIntent();
        }
        initViewPager();
        setTitle(FlashNewsApplication.getTopicTitle(mTopic));
    }

    private void initViewPager() {
        ViewPager viewPager = (ViewPager) findViewById(R.id.full_story_viewPager);
        pagerAdapter =
                new FullStoryViewPagerAdapter(getSupportFragmentManager(), mStories);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(mPosition);
        UnderlinePageIndicator indicator =
                (UnderlinePageIndicator) findViewById(R.id.full_story_indicator);
        indicator.setViewPager(viewPager);
        indicator.setFades(false);
        indicator.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mPosition = position;
                isFav = (dataSource.getFavoriteStory(mStories.get(mPosition).getId()) != null);
                if (mOptionsMenu != null) {
                    updateFavoriteIcon();
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(FlashNewsApplication.EXTRA_STORIES, mStories);
        outState.putInt(FlashNewsApplication.EXTRA_STORY_POSITION, mPosition);
        outState.putString(FlashNewsApplication.EXTRA_TOPIC, mTopic);
        outState.putBoolean(FlashNewsApplication.EXTRA_IS_FAV, isFav);
        outState.putString(FlashNewsApplication.EXTRA_FRAG_TYPE, mFragmentType);
    }

    private void initFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            mFragmentType = intent.getStringExtra(FlashNewsApplication.EXTRA_FRAG_TYPE);
            mTopic = intent.getStringExtra(FlashNewsApplication.EXTRA_TOPIC);
            mPosition = intent.getIntExtra(FlashNewsApplication.EXTRA_STORY_POSITION, 0);
            mStories = intent.getParcelableArrayListExtra(FlashNewsApplication.EXTRA_STORY);
        }
        isFav = (dataSource.getFavoriteStory(mStories.get(mPosition).getId()) != null);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        mOptionsMenu = menu;
        getMenuInflater().inflate(R.menu.full_story, menu);
        MenuItem itemShare = mOptionsMenu.findItem(R.id.action_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(itemShare);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(isFav){
            MenuItem item = menu.findItem(R.id.action_favorite);
            item.setIcon(R.mipmap.ic_favorite);
        }
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String text = "";
        if (mStories != null)
            text = mStories.get(mPosition).getTitle() + " -> " +
                    mStories.get(mPosition).getShortLink();
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        setShareIntent(shareIntent);
        return super.onPrepareOptionsMenu(menu);
    }

    private void updateFavoriteIcon(){
        MenuItem item = mOptionsMenu.findItem(R.id.action_favorite);
        if(isFav){
            item.setIcon(R.mipmap.ic_favorite);
        } else {
            item.setIcon(R.mipmap.ic_add_to_favs);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                return true;

            case R.id.action_favorite: {
                isFav = (dataSource.getFavoriteStory(mStories.get(mPosition).getId()) != null);
                if(isFav){
                    removeFromFavorites(item);
                } else {
                    addToFavorites(item);
                }
                return true;
            }
            case android.R.id.home:{
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void addToFavorites(MenuItem item){
        Story story = mStories.get(mPosition);
        if(addedToDatabase(story, mTopic)){
            long id = dataSource.addToFavorites(story.getId());
            if(id == -1){
                Toast.makeText(this, R.string.cannot_add_to_fav, Toast.LENGTH_LONG).show();
            } else {
                item.setIcon(R.mipmap.ic_favorite);
                Toast.makeText(this, R.string.story_saved, Toast.LENGTH_LONG).show();
                isFav = true;
            }
        } else {
            Toast.makeText(this, R.string.error_occurred, Toast.LENGTH_LONG).show();
        }
    }

    private boolean addedToDatabase(Story story, String topic) {
        return (dataSource.insertStory(story, topic) != -1);
    }

    private void removeFromFavorites(MenuItem item){
        String id = mStories.get(mPosition).getId();
        long countFav = dataSource.deleteFromFavorites(id);
        long countStory = dataSource.deleteStory(id);
        Log.d(LOG_TAG, "Removed " + countFav + " stories");
        if(countFav == 1 && countStory == 1){
            item.setIcon(R.mipmap.ic_add_to_favs);
            Toast.makeText(this, R.string.story_removed_from_favs, Toast.LENGTH_LONG).show();
            isFav = false;
            updateActivity();
        } else {
            Toast.makeText(this, R.string.error_removing_fav, Toast.LENGTH_LONG).show();
        }
    }

    private void updateActivity() {
        if(mFragmentType.equals(FlashNewsApplication.FAVORITES_FRAG)){
            Toast.makeText(this, "Removed from Favorites", Toast.LENGTH_SHORT).show();
            mStories = dataSource.getFavoriteStories();
            pagerAdapter.setStories(mStories);
        } else if(mFragmentType.equals(FlashNewsApplication.STORIES_FRAG)){
            Toast.makeText(this, "Removed from Stories", Toast.LENGTH_SHORT).show();
        }
    }

    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
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
}
