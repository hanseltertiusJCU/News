package com.example.android.news;

public class Article {

    // Initiate variables for the custom class
    private int mArticleImage;
    private String mAuthor;
    private String mTopic;
    private String mDate;
    private String mTime;
    private String mArticleTitle;

    // Set the constants for no image and no text found
    private static final String NO_TEXT_FOUND = null;
    private static final int NO_IMAGE_FOUND = -1;

    public Article(String author, String topic, String date, String time, String articleTitle){
        mAuthor = author;
        mTopic = topic;
        mDate = date;
        mTime = time;
        mArticleTitle = articleTitle;
    }


    public Article(int articleImage, String author, String topic, String date, String time, String articleTitle){
        mArticleImage = articleImage;
        mAuthor = author;
        mTopic = topic;
        mDate = date;
        mTime = time;
        mArticleTitle = articleTitle;
    }

    public int getmArticleImage() {
        return mArticleImage;
    }

    public String getArticleAuthor() {
        return mAuthor;
    }

    public String getArticleTopic() {
        return mTopic;
    }

    public String getArticleDate() {
        return mDate;
    }

    public String getArticleTime() {
        return mTime;
    }

    public String getArticleTitle() {
        return mArticleTitle;
    }

    public boolean hasArticleAuthor() {
        return mAuthor != NO_TEXT_FOUND;
    }

    public boolean hasArticleDate() {
        return mDate != NO_TEXT_FOUND;
    }

    public boolean hasArticleTime() {
        return mTime != NO_TEXT_FOUND;
    }

    public boolean hasArticleImage() {
        return mArticleImage != NO_IMAGE_FOUND;
    }
}
