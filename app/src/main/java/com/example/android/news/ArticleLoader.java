package com.example.android.news;

import android.content.AsyncTaskLoader;

import java.util.List;

public class ArticleLoader extends AsyncTaskLoader<List<Article>> {

    /**
     * Tag for log messages
     */
    private static final String LOG_TAG = ArticleLoader.class.getName();

    /**
     * Query URL
     */
    private String queryURL;

    /**
     * Constructor for loader
     */
    public ArticleLoader(MainActivity context, String url){
        super(context);
        queryURL = url;
    }

    /**
     * Force the loader to load when the loader started to load
     */
    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * Load the loader and do the task (retrieve a list of articles) in background thread
     * @return articles
     */
    @Override
    public List<Article> loadInBackground() {
        if(queryURL == null){
            return null;
        }
        // Perform the network request, parse the response, and extract a list of articles.
        List<Article> articles = QueryUtils.fetchArticles(queryURL);
        return articles;
    }
}
