package com.akhadidja.android.flashnews.network;

import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

import com.akhadidja.android.flashnews.callbacks.OnVolleyErrorListener;
import com.akhadidja.android.flashnews.callbacks.StoriesLoadedListener;
import com.akhadidja.android.flashnews.json.NprApiEndpoints;
import com.akhadidja.android.flashnews.json.StoryDeserializer;
import com.akhadidja.android.flashnews.json.Utility;
import com.akhadidja.android.flashnews.pojos.Story;
import com.android.volley.RequestQueue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FetchNprNewsTask extends AsyncTask<Void, Integer, ArrayList<Story>>{

    private static final String LOG_TAG = FetchNprNewsTask.class.getSimpleName();

    private String mApiKey;
    private String mTopic;
    private StoriesLoadedListener mListener;
    private OnVolleyErrorListener mVolleyErrorListener;
    private TextView mLoadingTextView;
    private boolean mIsRefresh;
    private RequestQueue mRequestQueue;

    public FetchNprNewsTask(TextView loadingTextView, String apiKey, String topic,
                            StoriesLoadedListener storiesListener,
                            OnVolleyErrorListener volleyListener, boolean isRefresh){
        mApiKey = apiKey;
        mTopic = topic;
        mListener = storiesListener;
        mVolleyErrorListener = volleyListener;
        mLoadingTextView = loadingTextView;
        mIsRefresh = isRefresh;
        VolleySingleton mVolleySingleton = VolleySingleton.getInstance();
        mRequestQueue = mVolleySingleton.getRequestQueue();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(!mIsRefresh){
            mLoadingTextView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected ArrayList<Story> doInBackground(Void... params) {
        String url = Utility.nprTopicUriBuilder(mApiKey, mTopic);
        ArrayList<Story> stories = new ArrayList<>();
        try{
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(Story.class, new StoryDeserializer());
            Gson gson = gsonBuilder.create();
            JSONObject jsonObject =
                    VolleyJSONRequest.response(mVolleyErrorListener, mRequestQueue, url);
            JSONObject listObject = jsonObject.getJSONObject(NprApiEndpoints.LIST);
            JSONArray storyArray = listObject.getJSONArray(NprApiEndpoints.STORY);

            for (int i = 0; i < storyArray.length(); i++) {
                stories.add(gson.fromJson(storyArray.get(i).toString(), Story.class));
                publishProgress(i, storyArray.length());
            }
        } catch (JSONException e) {
            //Log.d(LOG_TAG, "JSon response issue", e);
        }
        return stories;
    }

    @Override
    protected void onPostExecute(ArrayList<Story> stories) {
        if(stories != null){
            mListener.onStoriesLoadedListener(stories);
            mLoadingTextView.setVisibility(View.GONE);
        }
    }
}
