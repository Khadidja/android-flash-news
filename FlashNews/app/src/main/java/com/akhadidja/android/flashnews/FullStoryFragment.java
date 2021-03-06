package com.akhadidja.android.flashnews;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.akhadidja.android.flashnews.pojos.Story;

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
        int textSize = Integer.parseInt(FlashNewsApplication.readFromPreferences(getActivity(),
                SettingsFragment.PREF_TEXT_SIZE_VALUE,
                getString(R.string.pref_text_size_default_value)));
        mText.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
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
            mDate.setText(FlashNewsApplication.formatDate(mStory.getStoryDate()));
            mText.setText(mStory.getText());
        }
    }
}
