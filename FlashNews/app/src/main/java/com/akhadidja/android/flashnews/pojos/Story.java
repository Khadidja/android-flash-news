package com.akhadidja.android.flashnews.pojos;

public class Story {
    private String id;
    private String shortLink;
    private String htmlLink;
    private String title;
    private String teaser;
    private String storyDate;
    private String textWithHtml;

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

    public String getTextWithHtml() {
        return textWithHtml;
    }

    public void setTextWithHtml(String textWithHtml) {
        this.textWithHtml = textWithHtml;
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
                ", textWithHtml=" + textWithHtml +
                '}';
    }
}
