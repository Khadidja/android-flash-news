package com.akhadidja.android.flashnews;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.akhadidja.android.flashnews.adapters.StoryAdapter;
import com.akhadidja.android.flashnews.callbacks.OnVolleyErrorListener;
import com.akhadidja.android.flashnews.callbacks.RecyclerStoryItemTouchListener;
import com.akhadidja.android.flashnews.callbacks.StoriesLoadedListener;
import com.akhadidja.android.flashnews.network.FetchNprNewsTask;
import com.akhadidja.android.flashnews.pojos.Story;
import com.android.volley.VolleyError;

import java.util.ArrayList;


public class StoriesFragment extends Fragment implements
        RecyclerStoryItemTouchListener.OnStoryItemClickListener,
        StoriesLoadedListener, SwipeRefreshLayout.OnRefreshListener, OnVolleyErrorListener {

    private static final String STATE_STORIES = "state_stories";
    private static final String TOPIC_KEY = "topic_key";
    private static final String LOG_TAG = StoriesFragment.class.getSimpleName();
    private ArrayList<Story> mStories;
    private StoryAdapter mStoryAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView mLoadingTextView;

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
        swipeRefreshLayout = (SwipeRefreshLayout) layout.findViewById(R.id.stories_swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.accent, R.color.primaryDark);
        swipeRefreshLayout.setOnRefreshListener(this);
        mLoadingTextView = (TextView) layout.findViewById(R.id.loading_textView);

        init(savedInstanceState, layout);

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
            mStories = savedInstanceState.getParcelableArrayList(STATE_STORIES);
            mStoryAdapter.setStories(mStories);
        }else{
            new FetchNprNewsTask(mLoadingTextView,getString(R.string.NPR_API_KEY),
                    getTopicArg(), this, this, false).execute();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
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
        intent.putExtra(FlashNewsApplication.EXTRA_FRAG_TYPE, FlashNewsApplication.STORIES_FRAG);
        startActivity(intent);
    }

    @Override
    public void onStoriesLoadedListener(ArrayList<Story> stories) {
        mStories = stories;
        mStoryAdapter.setStories(mStories);
    }

    @Override
    public void onRefresh() {
        new FetchNprNewsTask( mLoadingTextView, getString(R.string.NPR_API_KEY),
                getTopicArg(), this, this, true).execute();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onVolleyError(VolleyError error) {
        Toast.makeText(getActivity(), R.string.volley_error, Toast.LENGTH_LONG).show();
    }
}
