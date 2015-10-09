package com.akhadidja.android.flashnews;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

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

public class FetchNprNewsTask extends AsyncTask<Void, Void, ArrayList<Story>>{

    private static final String LOG_TAG = FetchNprNewsTask.class.getSimpleName();

    private String mApiKey;
    private String mTopic;
    private StoriesLoadedListener mListener;

    public FetchNprNewsTask(Context context, String apiKey, String topic,
                            StoriesLoadedListener listener){
        mApiKey = apiKey;
        mTopic = topic;
        mListener = listener;
    }

    @Override
    protected ArrayList<Story> doInBackground(Void... params) {
        Log.d(LOG_TAG, "Fetching stories in background");
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
            }
            Log.d(LOG_TAG, "Fetched "+stories.size()+" stories");
            return stories;

        } catch (JSONException e) {
            Log.e(LOG_TAG, "JSON pb", e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Story> stories) {
        if(stories != null){
            Log.d(LOG_TAG, "onPostExecute Downloaded " + stories.size() + " for " + mTopic);
            mListener.onStoriesLoadedListener(stories);
        }
    }
}
