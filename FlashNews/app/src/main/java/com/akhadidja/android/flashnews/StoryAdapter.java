package com.akhadidja.android.flashnews;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.akhadidja.android.flashnews.pojos.Story;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.joda.time.Seconds;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.StoryHolder> {

    private static final String LOG_TAG = StoryAdapter.class.getSimpleName();
    private Story [] mStories;

    public StoryAdapter() {
        mStories = new Story[0];
    }

    public void setStories(Story[] stories) {
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
        holder.title.setText(mStories[position].getTitle());
        holder.date.setText(formatDateAndTime(mStories[position].getStoryDate()));
        holder.teaser.setText(mStories[position].getTeaser());
    }

    private String formatDateAndTime(String storyDate) {
        String elapsedTime = null;
        Date currentDate;
        Date passedDate;
        SimpleDateFormat dateFormat =
                new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
        try {
            currentDate = new Date();
            passedDate = dateFormat.parse(storyDate);

            DateTime currentDateTime = new DateTime(currentDate);
            DateTime passedDateTime = new DateTime(passedDate);

            int days = Days.daysBetween(passedDateTime, currentDateTime).getDays();
            int hours = Hours.hoursBetween(passedDateTime, currentDateTime).getHours();
            int minutes = Minutes.minutesBetween(passedDateTime, currentDateTime).getMinutes();
            int seconds = Seconds.secondsBetween(passedDateTime, currentDateTime).getSeconds();

            elapsedTime = getElapsedTime (days, hours, minutes, seconds);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return elapsedTime;
    }

    private String getElapsedTime(int days, int hours, int minutes, int seconds) {
        if(seconds < 60)
            return seconds + " seconds ago";
        else if (minutes < 60)
            return minutes + " minutes ago";
        else if (hours < 24)
            return hours + " hours ago";
        else if(days == 1)
            return "Yesterday";
        else
            return days + " days ago";
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
