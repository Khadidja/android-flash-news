package com.akhadidja.android.flashnews;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.akhadidja.android.flashnews.callbacks.StoriesLoadedListener;
import com.akhadidja.android.flashnews.data.FlashNewsSource;
import com.akhadidja.android.flashnews.pojos.Story;


public class StoriesFragment extends Fragment implements
        RecyclerStoryItemTouchListener.OnStoryItemClickListener,
        StoriesLoadedListener {

    // TODO change methods to return ArrayList instead of arrays

    private static final String STATE_STORIES = "state_stories";
    private static final String TOPIC_KEY = "topic_key";
    private static final String LOG_TAG = StoriesFragment.class.getSimpleName();
    private Story [] mStories;
    private StoryAdapter mStoryAdapter;

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
        FlashNewsSource mDataSource = new FlashNewsSource(getActivity());
        mDataSource.open();

        RecyclerView mRecyclerView = (RecyclerView) layout.findViewById(R.id.stories_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addOnItemTouchListener(new RecyclerStoryItemTouchListener(getActivity(), this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL_LIST));
        mStoryAdapter = new StoryAdapter();
        mRecyclerView.setAdapter(mStoryAdapter);

        if(savedInstanceState != null){
            Log.d(LOG_TAG, "savedInstanceState NOT null");
            Parcelable [] parcelables = savedInstanceState.getParcelableArray(STATE_STORIES);
            mStories = new Story[parcelables.length];
            for (int i = 0; i < parcelables.length; i++) {
                mStories[i] = (Story) parcelables[i];
            }
        }else{
            Log.d(LOG_TAG, "savedInstance NULL, get stories from DB");
            mStories = mDataSource.getStoriesByTopic(getTopicArg());
            mDataSource.close();
            if (mStories.length == 0){
                new FetchNprNewsTask(getActivity(), getString(R.string.NPR_API_KEY),
                        getTopicArg(), this).execute();
            }
        }
        mStoryAdapter.setStories(mStories);
        return layout;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArray(STATE_STORIES, mStories);
    }

    @Override
    public void onStoryItemClicked(int position) {
        Intent intent = new Intent(getActivity(), FullStoryActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT,
                mStoryAdapter.getStoryApiIDAtPosition(position));
        startActivity(intent);
    }

    @Override
    public void onStoriesLoadedListener(Story[] stories) {
        mStoryAdapter.setStories(stories);
    }
}
