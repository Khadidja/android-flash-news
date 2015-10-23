package com.akhadidja.android.flashnews.pojos;

import android.os.Parcel;
import android.os.Parcelable;

public class Story implements Parcelable {

    private String id;
    private String shortLink;
    private String htmlLink;
    private String title;
    private String teaser;
    private String storyDate;
    private String text;

    public Story() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShortLink() {
        return shortLink;
    }

    public void setShortLink(String shortLink) {
        this.shortLink = shortLink;
    }

    public String getHtmlLink() {
        return htmlLink;
    }

    public void setHtmlLink(String htmlLink) {
        this.htmlLink = htmlLink;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTeaser() {
        return teaser;
    }

    public void setTeaser(String teaser) {
        this.teaser = teaser;
    }

    public String getStoryDate() {
        return storyDate;
    }

    public void setStoryDate(String storyDate) {
        this.storyDate = storyDate;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Story{" +
                "id='" + id + '\'' +
                ", shortLink='" + shortLink + '\'' +
                ", htmlLink='" + htmlLink + '\'' +
                ", title='" + title + '\'' +
                ", teaser='" + teaser + '\'' +
                ", storyDate='" + storyDate + '\'' +
                ", text=" + text +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.shortLink);
        dest.writeString(this.htmlLink);
        dest.writeString(this.title);
        dest.writeString(this.teaser);
        dest.writeString(this.storyDate);
        dest.writeString(this.text);
    }

    protected Story(Parcel in) {
        this.id = in.readString();
        this.shortLink = in.readString();
        this.htmlLink = in.readString();
        this.title = in.readString();
        this.teaser = in.readString();
        this.storyDate = in.readString();
        this.text = in.readString();
    }

    public static final Parcelable.Creator<Story> CREATOR = new Parcelable.Creator<Story>() {
        public Story createFromParcel(Parcel source) {
            return new Story(source);
        }

        public Story[] newArray(int size) {
            return new Story[size];
        }
    };
}
