package com.akhadidja.android.flashnews.callbacks;

import com.akhadidja.android.flashnews.pojos.Story;

import java.util.ArrayList;

public interface StoriesLoadedListener {
    void onStoriesLoadedListener(ArrayList<Story> stories);
}
