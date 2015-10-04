package com.akhadidja.android.flashnews.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.akhadidja.android.flashnews.pojos.Story;

public class FlashNewsSource {
    private static final String LOG_TAG = FlashNewsSource.class.getSimpleName();
    private FlashNewsHelper helper;
    private SQLiteDatabase db;

    private String [] storyProjection = {
            FlashNewsContract.StoryEntry._ID,
            FlashNewsContract.StoryEntry.COLUMN_STORY_ID,
            FlashNewsContract.StoryEntry.COLUMN_SHORT_LINK,
            FlashNewsContract.StoryEntry.COLUMN_HTML_LINK,
            FlashNewsContract.StoryEntry.COLUMN_TITLE,
            FlashNewsContract.StoryEntry.COLUMN_TEASER,
            FlashNewsContract.StoryEntry.COLUMN_DATE,
            FlashNewsContract.StoryEntry.COLUMN_TEXT_WITH_HTML,
            FlashNewsContract.StoryEntry.COLUMN_STORY_TOPIC
    };

    public FlashNewsSource(Context context){
        helper = new FlashNewsHelper(context);
    }

    public void open() throws SQLException {
        db = helper.getWritableDatabase();
    }

    public void close() {
        helper.close();
    }

    public long insertStories(Story [] stories, String topic){
        Log.d(LOG_TAG, "Adding " + stories.length + " " + topic + " stories");
        long storiesCount = 0;
        for (Story story : stories) {
            long id = insertStory(story, topic);
            if (id != -1)
                storiesCount += id;
        }
        return storiesCount;
    }

    public long insertStory(Story story, String topic){
        if(storySaved(story.getId()))
            return -1;

        ContentValues values = new ContentValues();
        values.put(FlashNewsContract.StoryEntry.COLUMN_STORY_ID, story.getId());
        values.put(FlashNewsContract.StoryEntry.COLUMN_SHORT_LINK, story.getShortLink());
        values.put(FlashNewsContract.StoryEntry.COLUMN_HTML_LINK, story.getHtmlLink());
        values.put(FlashNewsContract.StoryEntry.COLUMN_TITLE, story.getTitle());
        values.put(FlashNewsContract.StoryEntry.COLUMN_TEASER, story.getTeaser());
        values.put(FlashNewsContract.StoryEntry.COLUMN_DATE, story.getStoryDate());
        values.put(FlashNewsContract.StoryEntry.COLUMN_TEXT_WITH_HTML, story.getTextWithHtml());
        values.put(FlashNewsContract.StoryEntry.COLUMN_STORY_TOPIC, topic);

        return db.insertOrThrow(FlashNewsContract.StoryEntry.TABLE_NAME, null, values);
    }

    private boolean storySaved(String storyApiID){
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

    public Story selectStory(String storyApiID){
        Story story = null;
        Cursor c = db.query(
                FlashNewsContract.StoryEntry.TABLE_NAME,
                storyProjection,
                FlashNewsContract.StoryEntry.COLUMN_STORY_ID + " LIKE ?",
                new String[]{storyApiID}, null, null, null);

        if(c.moveToFirst())
            story = cursorToStory(c);

        c.close();
        return story;
    }

    public Story [] getStoriesByTopic(String topic){
        Story [] stories = new Story[0];
        Log.d(LOG_TAG, "Getting stories for topic "+topic);
        Cursor cursor = db.query(
                FlashNewsContract.StoryEntry.TABLE_NAME,
                storyProjection,
                FlashNewsContract.StoryEntry.COLUMN_STORY_TOPIC + " LIKE ?",
                new String[]{topic}, null, null, null);

        if(cursor.moveToFirst()){
            stories = new Story[cursor.getCount()];
            Log.d(LOG_TAG, "Got "+stories.length+" stories");
            int counter = 0;
            while (!cursor.isAfterLast()) {
                Story story = cursorToStory(cursor);
                stories[counter] = story;
                counter++;
                cursor.moveToNext();
            }
            cursor.close();
        } else {
            Log.d(LOG_TAG, "No stories for topic "+topic);
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
        story.setTextWithHtml(c.getString(
                c.getColumnIndex(FlashNewsContract.StoryEntry.COLUMN_TEXT_WITH_HTML)));

        return story;
    }
}
