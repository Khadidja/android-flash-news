package com.akhadidja.android.flashnews.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.akhadidja.android.flashnews.R;
import com.akhadidja.android.flashnews.pojos.Story;

import java.util.ArrayList;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.StoryHolder> {

    private static final String LOG_TAG = StoryAdapter.class.getSimpleName();
    private ArrayList<Story> mStories;

    public StoryAdapter() {
        mStories = new ArrayList<>();
    }

    public void setStories(ArrayList<Story> stories) {
        mStories = stories;
        notifyDataSetChanged();
        Log.d(LOG_TAG, "New set of stories");
    }

    @Override
    public StoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.story_item, parent, false);
        return new StoryHolder(v);
    }

    @Override
    public void onBindViewHolder(StoryHolder holder, int position) {
        holder.title.setText(mStories.get(position).getTitle());
        holder.date.setText(mStories.get(position).getStoryDate());
        holder.teaser.setText(mStories.get(position).getTeaser());
    }

    @Override
    public int getItemCount() {
        return mStories.size();
    }

    public Story getStory(int position){
        return mStories.get(position);
    }

    public ArrayList<Story> getStories() {
        return mStories;
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
