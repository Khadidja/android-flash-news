package com.akhadidja.android.flashnews.network;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.akhadidja.android.flashnews.callbacks.StoriesLoadedListener;
import com.akhadidja.android.flashnews.json.NprApiEndpoints;
import com.akhadidja.android.flashnews.json.StoryDeserializer;
import com.akhadidja.android.flashnews.json.Utility;
import com.akhadidja.android.flashnews.pojos.Story;
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
    private ProgressBar mProgressBar;
    private TextView mLoadingTextView;
    private boolean mIsRefresh;

    public FetchNprNewsTask(ProgressBar progressBar, TextView loadingTextView, String apiKey,
                            String topic, StoriesLoadedListener listener, boolean isRefresh){
        mApiKey = apiKey;
        mTopic = topic;
        mListener = listener;
        mProgressBar = progressBar;
        mLoadingTextView = loadingTextView;
        mIsRefresh = isRefresh;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(!mIsRefresh){
            mProgressBar.setVisibility(View.VISIBLE);
            mLoadingTextView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected ArrayList<Story> doInBackground(Void... params) {
        String topicJsonStr = Utility.getJsonStringFromUrl(mApiKey, mTopic);
        try {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(Story.class, new StoryDeserializer());
            Gson gson = gsonBuilder.create();
            JSONObject jsonObject = new JSONObject(topicJsonStr);
            JSONObject listObject = jsonObject.getJSONObject(NprApiEndpoints.LIST);
            JSONArray storyArray = listObject.getJSONArray(NprApiEndpoints.STORY);

            ArrayList<Story> stories = new ArrayList<>();

            for (int i = 0; i < storyArray.length(); i++) {
                stories.add(gson.fromJson(storyArray.get(i).toString(), Story.class));
                publishProgress(i, storyArray.length());
            }
            return stories;

        } catch (JSONException e) {
            Log.e(LOG_TAG, "JSON pb", e);
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        int counter = values[0];
        int max = values[1];
        mProgressBar.setMax(max);
        mProgressBar.setProgress(counter);
    }

    @Override
    protected void onPostExecute(ArrayList<Story> stories) {
        if(stories != null){
            mListener.onStoriesLoadedListener(stories);
            mProgressBar.setVisibility(View.GONE);
            mLoadingTextView.setVisibility(View.GONE);
        }
    }
}
