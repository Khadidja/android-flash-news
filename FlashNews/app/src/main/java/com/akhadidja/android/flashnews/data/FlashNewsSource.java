package com.akhadidja.android.flashnews.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.akhadidja.android.flashnews.pojos.Story;

public class FlashNewsSource {
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
            FlashNewsContract.StoryEntry.COLUMN_TEXT_WITH_HTML
    };

    public FlashNewsSource(Context context){
        helper = new FlashNewsHelper(context);
        db = helper.getWritableDatabase();
    }

    public long insertStory(Story story){
        ContentValues values = new ContentValues();
        values.put(FlashNewsContract.StoryEntry.COLUMN_STORY_ID, story.getId());
        values.put(FlashNewsContract.StoryEntry.COLUMN_SHORT_LINK, story.getShortLink());
        values.put(FlashNewsContract.StoryEntry.COLUMN_HTML_LINK, story.getHtmlLink());
        values.put(FlashNewsContract.StoryEntry.COLUMN_TITLE, story.getTitle());
        values.put(FlashNewsContract.StoryEntry.COLUMN_TEASER, story.getTeaser());
        values.put(FlashNewsContract.StoryEntry.COLUMN_DATE, story.getStoryDate());
        values.put(FlashNewsContract.StoryEntry.COLUMN_TEXT_WITH_HTML, story.getTextWithHtml());

        return db.insert(FlashNewsContract.StoryEntry.TABLE_NAME, null, values);
    }

    public Story selectStory(String storyApiID){
        Story story = null;
        Cursor c = db.query(
                FlashNewsContract.StoryEntry.TABLE_NAME,
                storyProjection,
                FlashNewsContract.StoryEntry.COLUMN_STORY_ID + " LIKE ?",
                new String[]{storyApiID},
                null,
                null,
                null
        );

        if(c.moveToFirst())
            story = cursorToStory(c);

        return story;
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
