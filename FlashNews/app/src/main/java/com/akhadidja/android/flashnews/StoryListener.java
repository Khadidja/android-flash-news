package com.akhadidja.android.flashnews;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class StoryListener extends GestureDetector.SimpleOnGestureListener {

    private Context mContext;
    private RecyclerView mRecyclerView;

    public StoryListener (Context context, RecyclerView recyclerView){
        mContext = context;
        mRecyclerView = recyclerView;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        View view = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
        int position = mRecyclerView.getChildLayoutPosition(view);
        Intent intent = new Intent(mContext, FullStoryActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, position);
        mContext.startActivity(intent);
        return super.onSingleTapConfirmed(e);
    }
}
