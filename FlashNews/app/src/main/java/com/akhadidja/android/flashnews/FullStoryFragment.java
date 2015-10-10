package com.akhadidja.android.flashnews;


import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.akhadidja.android.flashnews.pojos.Story;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class FullStoryFragment extends Fragment {

    private static final String LOG_TAG = FullStoryFragment.class.getSimpleName();
    //private FlashNewsSource dataSource;
    ArrayList<Story> mStories;
    int mPosition;
    TextView mTitle, mDate, mText, mLink;

    public FullStoryFragment() {
    }

    public static FullStoryFragment newInstance(ArrayList<Story> stories, int position){
        FullStoryFragment fragment = new FullStoryFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(FlashNewsApplication.EXTRA_STORY, stories);
        args.putInt(FlashNewsApplication.EXTRA_STORY_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    private ArrayList<Story> getArgStories(){
        return getArguments().getParcelableArrayList(FlashNewsApplication.EXTRA_STORY);
    }

    private int getArgPosition(){
        return getArguments().getInt(FlashNewsApplication.EXTRA_STORY_POSITION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_full_story, container, false);
        //dataSource = new FlashNewsSource(this);
        //dataSource.open();
        mTitle = (TextView) layout.findViewById(R.id.full_story_title_textView);
        mDate = (TextView) layout.findViewById(R.id.full_story_date_textView);
        mText = (TextView) layout.findViewById(R.id.full_story_text_textView);
        mLink = (TextView) layout.findViewById(R.id.full_story_website_link);
        mPosition = getArgPosition();
        mStories = getArgStories();

        initFullStory();
        return layout;
    }

    private void initFullStory() {
        if (mStories != null) {
            mLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(
                            Intent.ACTION_VIEW, Uri.parse(mStories.get(mPosition).getHtmlLink()));
                    startActivity(browserIntent);
                }
            });
            mTitle.setText(mStories.get(mPosition).getTitle());
            mDate.setText(formatDate());
            mText.setText(mStories.get(mPosition).getText());
        }
    }

    private String formatDate() {
        try {
            SimpleDateFormat dateFormat =
                    new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
            return dateFormat.parse(mStories.get(mPosition).getStoryDate()).toString();

        } catch (ParseException e) {
            return mStories.get(mPosition).getStoryDate();
        }
    }

}
