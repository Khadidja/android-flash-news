package com.akhadidja.android.flashnews.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.akhadidja.android.flashnews.pojos.Story;

import java.util.ArrayList;

public class FlashNewsSource {
    private static final String LOG_TAG = FlashNewsSource.class.getSimpleName();
    private FlashNewsHelper helper;
    private SQLiteDatabase db;

    private String[] storyProjection = {
            FlashNewsContract.StoryEntry._ID,
            FlashNewsContract.StoryEntry.COLUMN_STORY_ID,
            FlashNewsContract.StoryEntry.COLUMN_SHORT_LINK,
            FlashNewsContract.StoryEntry.COLUMN_HTML_LINK,
            FlashNewsContract.StoryEntry.COLUMN_TITLE,
            FlashNewsContract.StoryEntry.COLUMN_TEASER,
            FlashNewsContract.StoryEntry.COLUMN_DATE,
            FlashNewsContract.StoryEntry.COLUMN_TEXT,
            FlashNewsContract.StoryEntry.COLUMN_STORY_TOPIC
    };

    private String[] favProjection = {
            FlashNewsContract.FavoriteStoryEntry._ID,
            FlashNewsContract.FavoriteStoryEntry.COLUMN_STORY_ID};

    public FlashNewsSource(Context context) {
        helper = new FlashNewsHelper(context);
    }

    public void open() throws SQLException {
        db = helper.getWritableDatabase();
    }

    public void close() {
        helper.close();
    }

    public long addToFavorites(String storyId) {
        Log.d(LOG_TAG, "Adding " + storyId + " to favs");
        ContentValues values = new ContentValues();
        values.put(FlashNewsContract.FavoriteStoryEntry.COLUMN_STORY_ID, storyId);
        return db.insertOrThrow(FlashNewsContract.FavoriteStoryEntry.TABLE_NAME, null, values);
    }

    public long deleteFromFavorites(String storyId) {
        Log.d(LOG_TAG, "Deleting " + storyId + " from favs");
        return db.delete(
                FlashNewsContract.FavoriteStoryEntry.TABLE_NAME,
                FlashNewsContract.FavoriteStoryEntry.COLUMN_STORY_ID + " LIKE ?",
                new String[]{storyId});
    }

    public long deleteStory(String storyId) {
        Log.d(LOG_TAG, "Deleting " + storyId + " from stories");
        return db.delete(
                FlashNewsContract.StoryEntry.TABLE_NAME,
                FlashNewsContract.StoryEntry.COLUMN_STORY_ID + " LIKE ?",
                new String[]{storyId});
    }

    public ArrayList<Story> getFavoriteStories() {
        ArrayList<Story> stories = new ArrayList<>();
        Cursor favCursor = db.query(FlashNewsContract.FavoriteStoryEntry.TABLE_NAME,
                favProjection, null, null, null, null, null);

        if (favCursor.moveToFirst()) {
            Log.d(LOG_TAG, "There are " + favCursor.getCount() + " favs in table");
            while (!favCursor.isAfterLast()) {
                String id = favCursor.getString(
                        favCursor.getColumnIndex(FlashNewsContract.FavoriteStoryEntry.COLUMN_STORY_ID));
                Log.d(LOG_TAG, "id from cursor: " + id);
                Story story = selectStory(id);
                if (story != null) {
                    stories.add(story);
                }
                favCursor.moveToNext();
            }
        }
        favCursor.close();
        Log.d(LOG_TAG, "Found " + stories.size() + " fav stories");
        return stories;
    }

    public Story getFavoriteStory(String storyApiId) {
        Cursor cursor = db.query(
                FlashNewsContract.FavoriteStoryEntry.TABLE_NAME,
                favProjection,
                FlashNewsContract.FavoriteStoryEntry.COLUMN_STORY_ID + " LIKE ?",
                new String[]{storyApiId}, null, null, null);
        Story story = null;
        if (cursor.moveToFirst())
            story = selectStory(cursor.getString(
                    cursor.getColumnIndex(FlashNewsContract.FavoriteStoryEntry.COLUMN_STORY_ID)));
        cursor.close();
        return story;
    }

    public long insertStories(Story[] stories, String topic) {
        long storiesCount = 0;
        for (Story story : stories) {
            long id = insertStory(story, topic);
            if (id != -1)
                storiesCount += id;
        }
        return storiesCount;
    }

    public long insertStory(Story story, String topic) {
        if (storySaved(story.getId())){
            Log.d(LOG_TAG, "Story already saved");
            return -1;
        }

        ContentValues values = new ContentValues();
        values.put(FlashNewsContract.StoryEntry.COLUMN_STORY_ID, story.getId());
        values.put(FlashNewsContract.StoryEntry.COLUMN_SHORT_LINK, story.getShortLink());
        values.put(FlashNewsContract.StoryEntry.COLUMN_HTML_LINK, story.getHtmlLink());
        values.put(FlashNewsContract.StoryEntry.COLUMN_TITLE, story.getTitle());
        values.put(FlashNewsContract.StoryEntry.COLUMN_TEASER, story.getTeaser());
        values.put(FlashNewsContract.StoryEntry.COLUMN_DATE, story.getStoryDate());
        values.put(FlashNewsContract.StoryEntry.COLUMN_TEXT, story.getText());
        values.put(FlashNewsContract.StoryEntry.COLUMN_STORY_TOPIC, topic);

        Log.d(LOG_TAG, "Saving story");
        return db.insertOrThrow(FlashNewsContract.StoryEntry.TABLE_NAME, null, values);
    }

    private boolean storySaved(String storyApiID) {
        Cursor c = db.query(
                FlashNewsContract.StoryEntry.TABLE_NAME,
                storyProjection,
                FlashNewsContract.StoryEntry.COLUMN_STORY_ID + " LIKE ?",
                new String[]{storyApiID},
                null,
                null,
                null
        );

        boolean b = c.moveToFirst();
        c.close();
        return b;
    }

    public Story selectStory(String storyApiID) {
        Story story = null;
        Cursor c = db.query(
                FlashNewsContract.StoryEntry.TABLE_NAME,
                storyProjection,
                FlashNewsContract.StoryEntry.COLUMN_STORY_ID + " LIKE ?",
                new String[]{storyApiID}, null, null, null);

        if (c.moveToFirst()) {
            story = cursorToStory(c);
        }
        c.close();
        return story;
    }

    public Story[] getStoriesByTopic(String topic) {
        Story[] stories = new Story[0];
        Log.d(LOG_TAG, "Getting stories for topic " + topic);
        Cursor cursor = db.query(
                FlashNewsContract.StoryEntry.TABLE_NAME,
                storyProjection,
                FlashNewsContract.StoryEntry.COLUMN_STORY_TOPIC + " LIKE ?",
                new String[]{topic}, null, null, null);

        if (cursor.moveToFirst()) {
            stories = new Story[cursor.getCount()];
            Log.d(LOG_TAG, "Got " + stories.length + " stories");
            int counter = 0;
            while (!cursor.isAfterLast()) {
                Story story = cursorToStory(cursor);
                stories[counter] = story;
                counter++;
                cursor.moveToNext();
            }
            cursor.close();
        } else {
            Log.d(LOG_TAG, "No stories for topic " + topic);
        }
        return stories;
    }

    private Story cursorToStory(Cursor c) {
        Story story = new Story();

        story.setId(c.getString(c.getColumnIndex(FlashNewsContract.StoryEntry.COLUMN_STORY_ID)));
        story.setShortLink(c.getString(
                c.getColumnIndex(FlashNewsContract.StoryEntry.COLUMN_SHORT_LINK)));
        story.setHtmlLink(c.getString(
                c.getColumnIndex(FlashNewsContract.StoryEntry.COLUMN_HTML_LINK)));
        story.setTitle(c.getString(c.getColumnIndex(FlashNewsContract.StoryEntry.COLUMN_TITLE)));
        story.setTeaser(c.getString(c.getColumnIndex(FlashNewsContract.StoryEntry.COLUMN_TEASER)));
        story.setStoryDate(c.getString(c.getColumnIndex(FlashNewsContract.StoryEntry.COLUMN_DATE)));
        story.setText(c.getString(
                c.getColumnIndex(FlashNewsContract.StoryEntry.COLUMN_TEXT)));

        return story;
    }

    public int getFavoriteCount() {
        String countQuery = "SELECT  * FROM " + FlashNewsContract.FavoriteStoryEntry.TABLE_NAME;
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }
}
