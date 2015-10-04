package com.akhadidja.android.flashnews.data;

import android.provider.BaseColumns;

public final class FlashNewsContract {

    public FlashNewsContract(){}

    public static abstract class StoryEntry implements BaseColumns {
        public static final String TABLE_NAME = "stories";
        public static final String COLUMN_STORY_ID = "story_id";
        public static final String COLUMN_SHORT_LINK = "short_link";
        public static final String COLUMN_HTML_LINK = "html_link";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_TEASER = "teaser";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_TEXT_WITH_HTML = "text_with_html";
        public static final String COLUMN_STORY_TOPIC = "story_topic";
    }
}
