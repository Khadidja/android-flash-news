package com.akhadidja.android.flashnews.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.akhadidja.android.flashnews.FullStoryFragment;
import com.akhadidja.android.flashnews.pojos.Story;

import java.util.ArrayList;

public class FullStoryViewPagerAdapter extends FragmentPagerAdapter {

    private static final String LOG_TAG = FullStoryViewPagerAdapter.class.getSimpleName();
    ArrayList<Story> mStories;

    public FullStoryViewPagerAdapter(FragmentManager fm, ArrayList<Story> stories) {
        super(fm);
        mStories = stories;
    }

    @Override
    public Fragment getItem(int position) {
        return FullStoryFragment.newInstance(mStories.get(position), position);
    }

    @Override
    public int getCount() {
        return mStories.size();
    }

    public void setStories(ArrayList<Story> stories) {
        Log.d(LOG_TAG, "New list of stories");
        mStories = stories;
        notifyDataSetChanged();
    }
}
