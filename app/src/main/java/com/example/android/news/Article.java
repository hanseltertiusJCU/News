package com.example.android.news;

public class Article {

    // Initiate variables for the custom class
    private String mArticleImage;
    private String mAuthor;
    private String mTopic;
    private String mDate;
    private String mTime;
    private String mArticleTitle;
    private String mArticleURL;

    // Set the constants for no image and no text found
    private static final String NO_TEXT_FOUND = null;
    private static final int NO_IMAGE_FOUND = -1;

    public Article(String author, String topic, String date, String time, String articleTitle, String articleURL){
        mAuthor = author;
        mTopic = topic;
        mDate = date;
        mTime = time;
        mArticleTitle = articleTitle;
        mArticleURL = articleURL;
    }


    public Article(String articleImage, String author, String topic, String date, String time, String articleTitle, String articleURL){
        mArticleImage = articleImage;
        mAuthor = author;
        mTopic = topic;
        mDate = date;
        mTime = time;
        mArticleTitle = articleTitle;
        mArticleURL = articleURL;
    }

    public String getmArticleImage() {
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

    public String getArticleURL() {
        return mArticleURL;
    }

}
