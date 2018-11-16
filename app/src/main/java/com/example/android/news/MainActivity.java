package com.example.android.news;

import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<Article>>{

    private ArticleAdapter articleAdapter;

    /** Sample JSON response for a query from The Guardian*/
    private static final String SAMPLE_JSON_RESPONSE_URL = "http://content.guardianapis.com/search?q=debate&tag=politics/politics&from-date=2014-01-01&api-key=test";

    /**
     * Constant value for the article loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int ARTICLE_LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Search for ListView
        ListView listView = (ListView) findViewById(R.id.news_list);

        // Create a new ArticleAdapter object
        articleAdapter = new ArticleAdapter(this, new ArrayList<Article>());

        // Set the content of ArticleAdapter into the ListView
        listView.setAdapter(articleAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Find the current Article object
                Article currentArticle = articleAdapter.getItem(position);

                // Convert the String URL into a URI object
                Uri articleUri = Uri.parse(currentArticle.getArticleURL());

                // Create a new intent to view the article URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, articleUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

        // Get a reference to the LoaderManager, in order to interact with loaders.
        LoaderManager loaderManager = getLoaderManager();


        // Initialize the loader. Pass in the int ID constant defined above and pass in null for
        // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
        // because this activity implements the LoaderCallbacks interface).
        loaderManager.initLoader(ARTICLE_LOADER_ID, null, this);

    }

    /**
     * Create a new Loader by returning a class that extends from Loader
     * @param i
     * @param bundle
     * @return ArticleLoader
     */
    @Override
    public Loader<List<Article>> onCreateLoader(int i, Bundle bundle) {

        Uri baseUri = Uri.parse(SAMPLE_JSON_RESPONSE_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        return new ArticleLoader(this, uriBuilder.toString());
    }

    /**
     * Display the result when the loader finished on loading
     * @param loader
     * @param articles
     */
    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> articles) {
        // Clear the adapter of previous article data
        if(articleAdapter != null){
            articleAdapter.clear();
        }


        // If there is a valid list of {@link Article}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (articles != null && !articles.isEmpty()) {
            articleAdapter.addAll(articles);
        }
    }

    /**
     * Clear an existing data when the loader is being reset (e.g. rotating the device)
     * @param loader
     */
    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {
        articleAdapter.clear();
    }
}
