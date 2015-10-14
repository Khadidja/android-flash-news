package com.akhadidja.android.flashnews;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.akhadidja.android.flashnews.json.NprApiEndpoints;
import com.parse.Parse;
import com.parse.ParseInstallation;

public class FlashNewsApplication extends Application {

    private static FlashNewsApplication mInstance;
    public final static int NOTIFICATION_ID = 100;
    public static final String EXTRA_STORIES = "extra_stories";
    public final static String EXTRA_STORY = "extra_story";
    public final static String EXTRA_STORY_POSITION = "extra_story_pos";
    public final static String EXTRA_TOPIC = "extra_topic";
    public static final String EXTRA_IS_FAV = "is_fav";

    public static synchronized FlashNewsApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        Parse.initialize(this, getString(R.string.Application_ID), getString(R.string.Client_Key));
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }

    public static void saveToPreferences(Context context, String key, boolean value) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static boolean readFromPreferences(Context context, String key, boolean defaultValue) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    public static String getTopicTitle(String topic){
        switch (topic){
            case NprApiEndpoints.TOPIC_NEWS:
                return "News";
            case NprApiEndpoints.TOPIC_SPORTS:
                return "Sports";
            case NprApiEndpoints.TOPIC_SCIENCE:
                return "Science";
            case NprApiEndpoints.TOPIC_POLITICS:
                return "Politics";
            case NprApiEndpoints.TOPIC_TECH:
                return "Technology";
            case NprApiEndpoints.TOPIC_WORLD:
                return "World";
            default:
                return "Flash News";
        }
    }
}
