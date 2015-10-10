package com.akhadidja.android.flashnews;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.akhadidja.android.flashnews.pojos.Story;
import com.viewpagerindicator.UnderlinePageIndicator;

import java.util.ArrayList;

public class FullStoryActivity extends AppCompatActivity {

    private static final String LOG_TAG = FullStoryActivity.class.getSimpleName();
    private final static String FULL_STORY_FRAG = "full_story_frag";
    private ShareActionProvider mShareActionProvider;
    ArrayList<Story> mStories;
    int mPosition;
    String mTopic;
    private FullStoryFragment mFullStoryFragment = null;
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_story);
        Toolbar toolbar = (Toolbar) findViewById(R.id.full_story_toolbar);
        setSupportActionBar(toolbar);
        mFragmentManager = getFragmentManager();
            Intent intent = getIntent();
            if (intent != null) {
                mTopic = intent.getStringExtra(FlashNewsApplication.EXTRA_TOPIC);
                mPosition = intent.getIntExtra(FlashNewsApplication.EXTRA_STORY_POSITION, 0);
                mStories = intent.getParcelableArrayListExtra(FlashNewsApplication.EXTRA_STORY);
                mFullStoryFragment = FullStoryFragment.newInstance(mStories.get(mPosition));

                ViewPager viewPager = (ViewPager) findViewById(R.id.full_story_viewPager);
                FullStoryViewPagerAdapter pagerAdapter =
                        new FullStoryViewPagerAdapter(getSupportFragmentManager(), mStories);
                viewPager.setAdapter(pagerAdapter);
                viewPager.setCurrentItem(mPosition);
                UnderlinePageIndicator indicator =
                        (UnderlinePageIndicator) findViewById(R.id.full_story_indicator);
                indicator.setViewPager(viewPager);
                indicator.setFades(false);
            }
        setTitle(FlashNewsApplication.getTopicTitle(mTopic));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.full_story, menu);
        MenuItem itemShare = menu.findItem(R.id.action_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(itemShare);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                return true;

            case R.id.action_favorite: {
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(FlashNewsApplication.EXTRA_STORIES, mStories);
        outState.putInt(FlashNewsApplication.EXTRA_STORY_POSITION, mPosition);
        outState.putString(FlashNewsApplication.EXTRA_TOPIC, mTopic);
    }

    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

    @Override
    protected void onResume() {
        //dataSource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        //dataSource.close();
        super.onPause();
    }
}
