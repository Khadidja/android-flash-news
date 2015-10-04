package com.akhadidja.android.flashnews;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.akhadidja.android.flashnews.pojos.Story;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.StoryHolder> {

    private Story [] mStories;

    public StoryAdapter(Story[] stories) {
        mStories = stories;
    }

    @Override
    public StoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.story_item, parent, false);
        return new StoryHolder(v);
    }

    @Override
    public void onBindViewHolder(StoryHolder holder, int position) {
        holder.title.setText(mStories[position].getTitle());
        holder.date.setText(mStories[position].getStoryDate());
        holder.teaser.setText(mStories[position].getTeaser());
    }

    @Override
    public int getItemCount() {
        return mStories.length;
    }

    public class StoryHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView date;
        TextView teaser;

        public StoryHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.story_title_textView);
            date = (TextView) itemView.findViewById(R.id.story_date_textView);
            teaser = (TextView) itemView.findViewById(R.id.story_teaser_textView);
        }
    }
}
