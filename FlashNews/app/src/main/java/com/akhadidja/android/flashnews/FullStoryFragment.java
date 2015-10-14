package com.akhadidja.android.flashnews;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.akhadidja.android.flashnews.pojos.Story;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class FullStoryFragment extends Fragment {

    private static final String LOG_TAG = FullStoryFragment.class.getSimpleName();
    private Story mStory;
    TextView mTitle, mDate, mText, mLink;

    public FullStoryFragment() {
    }

    public static FullStoryFragment newInstance(Story story, int position){
        FullStoryFragment fragment = new FullStoryFragment();
        Bundle args = new Bundle();
        args.putParcelable(FlashNewsApplication.EXTRA_STORY, story);
        args.putInt(FlashNewsApplication.EXTRA_STORY_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    private Story getArgStory(){
        return getArguments().getParcelable(FlashNewsApplication.EXTRA_STORY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_full_story, container, false);
        mTitle = (TextView) layout.findViewById(R.id.full_story_title_textView);
        mDate = (TextView) layout.findViewById(R.id.full_story_date_textView);
        mText = (TextView) layout.findViewById(R.id.full_story_text_textView);
        mLink = (TextView) layout.findViewById(R.id.full_story_website_link);
        mStory = getArgStory();

        initFullStory();
        return layout;
    }

    private void initFullStory() {
        if (mStory != null) {
            mLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(
                            Intent.ACTION_VIEW, Uri.parse(mStory.getHtmlLink()));
                    startActivity(browserIntent);
                }
            });
            mTitle.setText(mStory.getTitle());
            mDate.setText(formatDate());
            mText.setText(mStory.getText());
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
}
