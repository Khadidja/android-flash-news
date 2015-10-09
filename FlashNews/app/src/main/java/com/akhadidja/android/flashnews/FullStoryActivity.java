package com.akhadidja.android.flashnews;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.akhadidja.android.flashnews.pojos.Story;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class FullStoryActivity extends AppCompatActivity {

    private static final String LOG_TAG = FullStoryActivity.class.getSimpleName();
    private ShareActionProvider mShareActionProvider;
    //private FlashNewsSource dataSource;
    Story mStory;
    String mTopic;
    // TODO: replace isFav with appropriate database calls

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_story);
        Toolbar toolbar = (Toolbar) findViewById(R.id.full_story_toolbar);
        setSupportActionBar(toolbar);
        //dataSource = new FlashNewsSource(this);
        //dataSource.open();
        if(savedInstanceState != null){
            mStory = savedInstanceState.getParcelable(FlashNewsApplication.EXTRA_STORY);
            mTopic = savedInstanceState.getString(FlashNewsApplication.EXTRA_TOPIC);
        } else {
            Intent intent = getIntent();
            if (intent != null) {
                mTopic = intent.getStringExtra(FlashNewsApplication.EXTRA_TOPIC);
                mStory = intent.getParcelableExtra(FlashNewsApplication.EXTRA_STORY);
            }
        }
        initFullStory();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.full_story, menu);
        MenuItem itemShare = menu.findItem(R.id.action_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(itemShare);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String text = "";
        if(mStory != null)
            text = mStory.getTitle() + " -> " + mStory.getShortLink();
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        setShareIntent(shareIntent);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                return true;

            case R.id.action_favorite:{
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(FlashNewsApplication.EXTRA_STORY, mStory);
        outState.putString(FlashNewsApplication.EXTRA_TOPIC, mTopic);
    }

    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

    private void initFullStory() {
        if (mStory != null) {
            TextView title = (TextView) findViewById(R.id.full_story_title_textView);
            TextView date = (TextView) findViewById(R.id.full_story_date_textView);
            TextView text = (TextView) findViewById(R.id.full_story_text_textView);
            TextView link = (TextView) findViewById(R.id.full_story_website_link);
            link.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mStory.getHtmlLink()));
                    startActivity(browserIntent);
                }
            });
            title.setText(mStory.getTitle());
            date.setText(formatDate());
            text.setText(mStory.getText());
        }
    }

    private String formatDate() {
        try {
            SimpleDateFormat dateFormat =
                    new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
            return dateFormat.parse(mStory.getStoryDate()).toString();

        } catch (ParseException e) {
            return mStory.getStoryDate();
        }
    }

    @Override
    protected void onResume() {
        //dataSource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        //dataSource.close();
        super.onPause();
    }
}
