package com.example.android.news;

import android.graphics.Bitmap;

public class Article {

    // Initiate variables for the custom class
    private Bitmap mArticleImage;
    private String mAuthor;
    private String mTopic;
    private String mDate;
    private String mTime;
    private String mArticleTitle;
    private String mArticleURL;


    public Article(Bitmap articleImage, String author, String topic, String date, String time, String articleTitle, String articleURL){
        mArticleImage = articleImage;
        mAuthor = author;
        mTopic = topic;
        mDate = date;
        mTime = time;
        mArticleTitle = articleTitle;
        mArticleURL = articleURL;
    }

    public Bitmap getmArticleImage() {
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
