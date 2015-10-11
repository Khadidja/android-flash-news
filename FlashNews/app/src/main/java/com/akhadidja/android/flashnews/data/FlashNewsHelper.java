package com.akhadidja.android.flashnews.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FlashNewsHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "flash_news.db";

    private static final String SQL_CREATE_STORIES =
            "CREATE TABLE " + FlashNewsContract.StoryEntry.TABLE_NAME + " (" +
                    FlashNewsContract.StoryEntry._ID + " INTEGER PRIMARY KEY," +
                    FlashNewsContract.StoryEntry.COLUMN_STORY_ID + " TEXT UNIQUE," +
                    FlashNewsContract.StoryEntry.COLUMN_SHORT_LINK + " TEXT," +
                    FlashNewsContract.StoryEntry.COLUMN_HTML_LINK + " TEXT," +
                    FlashNewsContract.StoryEntry.COLUMN_TITLE + " TEXT," +
                    FlashNewsContract.StoryEntry.COLUMN_TEASER + " TEXT," +
                    FlashNewsContract.StoryEntry.COLUMN_DATE + " TEXT," +
                    FlashNewsContract.StoryEntry.COLUMN_TEXT + " TEXT," +
                    FlashNewsContract.StoryEntry.COLUMN_STORY_TOPIC + " TEXT);";
    private static final String SQL_DELETE_STORIES =
            "DROP TABLE IF EXISTS " + FlashNewsContract.StoryEntry.TABLE_NAME + ";";

    private static final String SQL_CREATE_FAV_STORIES =
            "CREATE TABLE " + FlashNewsContract.FavoriteStoryEntry.TABLE_NAME + " (" +
                    FlashNewsContract.FavoriteStoryEntry._ID +" INTEGER PRIMARY KEY," +
                    FlashNewsContract.FavoriteStoryEntry.COLUMN_STORY_ID + " TEXT UNIQUE," +
                    "FOREIGN KEY (" + FlashNewsContract.FavoriteStoryEntry.COLUMN_STORY_ID +
                        ") REFERENCES " + FlashNewsContract.StoryEntry.TABLE_NAME +
                        " (" + FlashNewsContract.StoryEntry.COLUMN_STORY_ID + "));";
    private static final String SQL_DELETE_FAV_STORIES =
            "DROP TABLE IF EXISTS " + FlashNewsContract.FavoriteStoryEntry.TABLE_NAME + ";";

    public FlashNewsHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_STORIES);
        db.execSQL(SQL_CREATE_FAV_STORIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_STORIES);
        db.execSQL(SQL_DELETE_FAV_STORIES);
        onCreate(db);
    }
}
