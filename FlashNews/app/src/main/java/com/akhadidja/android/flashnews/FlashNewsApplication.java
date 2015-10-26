package com.akhadidja.android.flashnews;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.akhadidja.android.flashnews.json.NprApiEndpoints;
import com.parse.Parse;
import com.parse.ParseInstallation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class FlashNewsApplication extends Application {

    private static FlashNewsApplication mInstance;
    public final static int NOTIFICATION_ID = 100;
    public static final String EXTRA_STORIES = "extra_stories";
    public final static String EXTRA_STORY = "extra_story";
    public final static String EXTRA_STORY_POSITION = "extra_story_pos";
    public final static String EXTRA_TOPIC = "extra_topic";
    public static final String EXTRA_IS_FAV = "extra_is_fav";
    public static final String EXTRA_FRAG_TYPE = "extra_fragment_type";
    public static final String FAVORITES_FRAG = "favorites_frag";
    public static final String STORIES_FRAG = "stories_frag";

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

    public static Context getAppContext() {
        return mInstance;
    }

    public static void saveToPreferences(Context context, String key, String value) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String readFromPreferences(Context context, String key, String defaultValue) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        return sharedPreferences.getString(key, defaultValue);
    }

    public static String formatDate(String storyDate) {
        try {
            SimpleDateFormat dateFormat =
                    new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.getDefault());
            return dateFormat.parse(storyDate).toString();

        } catch (ParseException e) {
            return storyDate;
        }
    }

    public static int getTextSize(String value){
        switch (value) {
            case "small":
                return R.dimen.body_regular_small;
            case "large":
                return R.dimen.body_regular_large;
            default:
                return R.dimen.body_regular_medium;
        }
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
            case FavoriteStoriesFragment.FAVORITE_STORIES:
                return FavoriteStoriesFragment.FAVORITE_STORIES;
            default:
                return "Flash News";
        }
    }
}
