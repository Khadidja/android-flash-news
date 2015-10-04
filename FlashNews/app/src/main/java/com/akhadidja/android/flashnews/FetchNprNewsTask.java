package com.akhadidja.android.flashnews;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.akhadidja.android.flashnews.json.NprApiEndpoints;
import com.akhadidja.android.flashnews.json.StoryDeserializer;
import com.akhadidja.android.flashnews.json.Utility;
import com.akhadidja.android.flashnews.pojos.Story;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FetchNprNewsTask extends AsyncTask<String, Void, Story[]>{

    private static final String LOG_TAG = FetchNprNewsTask.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private String mApiKey;

    public FetchNprNewsTask(String apiKey, RecyclerView recyclerView){
        mApiKey = apiKey;
        mRecyclerView = recyclerView;
    }

    @Override
    protected Story[] doInBackground(String... params) {
        String topicId = params[0];
        String topicJsonStr = Utility.getJsonStringFromUrl(mApiKey, topicId);
        try {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(Story.class, new StoryDeserializer());
            Gson gson = gsonBuilder.create();
            JSONObject jsonObject = new JSONObject(topicJsonStr);
            JSONObject listObject = jsonObject.getJSONObject(NprApiEndpoints.LIST);
            JSONArray storyArray = listObject.getJSONArray(NprApiEndpoints.STORY);

            return gson.fromJson(storyArray.toString(), Story[].class);

        } catch (JSONException e) {
            Log.e(LOG_TAG, "JSON pb", e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Story[] stories) {
        if(stories != null){
            StoryAdapter mAdapter = new StoryAdapter(stories);
            mRecyclerView.setAdapter(mAdapter);
        }
    }
}
