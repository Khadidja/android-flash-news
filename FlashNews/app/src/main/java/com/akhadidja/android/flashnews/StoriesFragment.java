package com.akhadidja.android.flashnews;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.akhadidja.android.flashnews.callbacks.StoriesLoadedListener;
import com.akhadidja.android.flashnews.pojos.Story;

import java.util.ArrayList;


public class StoriesFragment extends Fragment implements
        RecyclerStoryItemTouchListener.OnStoryItemClickListener,
        StoriesLoadedListener, SwipeRefreshLayout.OnRefreshListener {

    // TODO change methods to return ArrayList instead of arrays

    private static final String STATE_STORIES = "state_stories";
    private static final String TOPIC_KEY = "topic_key";
    private static final String LOG_TAG = StoriesFragment.class.getSimpleName();
    private ArrayList<Story> mStories;
    private StoryAdapter mStoryAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    public StoriesFragment() {
    }

    public static StoriesFragment newInstance(String topic){
        StoriesFragment fragment = new StoriesFragment();
        Bundle args = new Bundle();
        args.putString(TOPIC_KEY, topic);
        fragment.setArguments(args);
        return fragment;
    }

    private String getTopicArg(){
        return getArguments().getString(TOPIC_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_stories, container, false);
        init(savedInstanceState, layout);

        swipeRefreshLayout = (SwipeRefreshLayout) layout.findViewById(R.id.stories_swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.accent, R.color.primary, R.color.primaryLight);
        swipeRefreshLayout.setOnRefreshListener(this);
        return layout;
    }

    private void init(Bundle savedInstanceState, View layout) {
        RecyclerView mRecyclerView = (RecyclerView) layout.findViewById(R.id.stories_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addOnItemTouchListener(new RecyclerStoryItemTouchListener(getActivity(), this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL_LIST));
        mStoryAdapter = new StoryAdapter();
        mRecyclerView.setAdapter(mStoryAdapter);
        mStories = new ArrayList<>();
        if(savedInstanceState != null){
            Log.d(LOG_TAG, "savedInstanceState NOT null");
            mStories = savedInstanceState.getParcelableArrayList(STATE_STORIES);
            if (mStories != null)
                Log.d(LOG_TAG, "Saved stories NULL");
            mStoryAdapter.setStories(mStories);
        }else{
            Log.d(LOG_TAG, "savedInstance NULL, get stories from Api");
                new FetchNprNewsTask(getActivity(), getString(R.string.NPR_API_KEY),
                        getTopicArg(), this).execute();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(LOG_TAG, "Saving state");
        super.onSaveInstanceState(outState);
        Log.d(LOG_TAG, "Saving "+mStories.size()+" stories");
        outState.putParcelableArrayList(STATE_STORIES, mStories);
    }


    @Override
    public void onStoryItemClicked(int position) {
        Intent intent = new Intent(getActivity(), FullStoryActivity.class);
        intent.putExtra(FlashNewsApplication.EXTRA_STORY,
                mStoryAdapter.getStories());
        intent.putExtra(FlashNewsApplication.EXTRA_STORY_POSITION,
                position);
        intent.putExtra(FlashNewsApplication.EXTRA_TOPIC, getTopicArg());
        startActivity(intent);
    }

    @Override
    public void onStoriesLoadedListener(ArrayList<Story> stories) {
        mStories = stories;
        mStoryAdapter.setStories(mStories);
    }

    @Override
    public void onRefresh() {
        new FetchNprNewsTask(getActivity(), getString(R.string.NPR_API_KEY), getTopicArg(), this)
                .execute();
        swipeRefreshLayout.setRefreshing(false);
    }
}
