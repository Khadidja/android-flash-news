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
import android.widget.Toast;

import com.akhadidja.android.flashnews.data.FlashNewsSource;
import com.akhadidja.android.flashnews.pojos.Story;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class FullStoryActivity extends AppCompatActivity {

    private ShareActionProvider mShareActionProvider;
    private FlashNewsSource dataSource;
    Story mStory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_story);
        Toolbar toolbar = (Toolbar) findViewById(R.id.full_story_toolbar);
        setSupportActionBar(toolbar);

        dataSource = new FlashNewsSource(this);
        dataSource.open();
        Intent intent = getIntent();
        if (intent != null) {
            String storyApiId = intent.getStringExtra(Intent.EXTRA_TEXT);
            mStory = dataSource.selectStory(storyApiId);
            if (mStory != null) {
                initFullStory();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.full_story, menu);
        MenuItem item = menu.findItem(R.id.action_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String text = mStory.getTitle() + " -> " + mStory.getShortLink();
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        setShareIntent(shareIntent);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_share) {
            Toast.makeText(this, "Sharing...", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            Toast.makeText(this, "Provider not null", Toast.LENGTH_SHORT).show();
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

    private void initFullStory() {
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
        dataSource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        dataSource.close();
        super.onPause();
    }
}
