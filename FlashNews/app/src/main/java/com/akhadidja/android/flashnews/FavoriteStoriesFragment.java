package com.akhadidja.android.flashnews;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.akhadidja.android.flashnews.adapters.StoryAdapter;
import com.akhadidja.android.flashnews.callbacks.RecyclerStoryItemTouchListener;
import com.akhadidja.android.flashnews.data.FlashNewsSource;
import com.akhadidja.android.flashnews.pojos.Story;

import java.util.ArrayList;

public class FavoriteStoriesFragment extends Fragment
        implements RecyclerStoryItemTouchListener.OnStoryItemClickListener {

    private static final String LOG_TAG = FavoriteStoriesFragment.class.getSimpleName();
    public static final String FAVORITE_STORIES = "Favorite Stories";
    private ArrayList<Story> mStories;
    private StoryAdapter mStoryAdapter;

    public FavoriteStoriesFragment() {
    }

    public static FavoriteStoriesFragment newInstance(){
        FavoriteStoriesFragment fragment = new FavoriteStoriesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_favorite_stories, container, false);
        FlashNewsSource dataSource = new FlashNewsSource(getActivity());
        dataSource.open();
        mStories = dataSource.getFavoriteStories();
        dataSource.close();

        RecyclerView mRecyclerView = (RecyclerView) layout.findViewById(R.id.fav_stories_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addOnItemTouchListener(new RecyclerStoryItemTouchListener(getActivity(), this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL_LIST));
        mStoryAdapter = new StoryAdapter();
        mRecyclerView.setAdapter(mStoryAdapter);
        mStoryAdapter.setStories(mStories);
        return layout;
    }


    @Override
    public void onStoryItemClicked(int position) {
        Intent intent = new Intent(getActivity(), FullStoryActivity.class);
        intent.putExtra(FlashNewsApplication.EXTRA_STORY,
                mStoryAdapter.getStories());
        intent.putExtra(FlashNewsApplication.EXTRA_STORY_POSITION,
                position);
        intent.putExtra(FlashNewsApplication.EXTRA_TOPIC, FAVORITE_STORIES);
        intent.putExtra(FlashNewsApplication.EXTRA_FRAG_TYPE, FlashNewsApplication.FAVORITES_FRAG);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "Resuming");
        FlashNewsSource dataSource = new FlashNewsSource(getActivity());
        dataSource.open();
        mStories = dataSource.getFavoriteStories();
        dataSource.close();
        mStoryAdapter.setStories(mStories);
    }
}
