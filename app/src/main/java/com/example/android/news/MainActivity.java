package com.example.android.news;

import android.content.Intent;
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

public class MainActivity extends AppCompatActivity {

    private ArticleAdapter articleAdapter;

    /** Sample JSON response for a query from The Guardian*/
    private static final String SAMPLE_JSON_RESPONSE_URL = "http://content.guardianapis.com/search?q=debate&tag=politics/politics&from-date=2014-01-01&api-key=test";

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

        // Start the AsyncTask to fetch the article data
        ArticleAsyncTask articleTask = new ArticleAsyncTask();
        articleTask.execute(SAMPLE_JSON_RESPONSE_URL);

    }

    /**
     * {@link AsyncTask} to perform the network request on a background thread, and then
     * update the UI with the list of articles in the response.
     *
     * AsyncTask has three generic parameters: the input type, a type used for progress updates, and
     * an output type. Our task will take a String URL, and return an Article. We won't do
     * progress updates, so the second generic is just Void.
     *
     * We'll only override two of the methods of AsyncTask: doInBackground() and onPostExecute().
     * The doInBackground() method runs on a background thread, so it can run long-running code
     * (like network activity), without interfering with the responsiveness of the app.
     * Then onPostExecute() is passed the result of doInBackground() method, but runs on the
     * UI thread, so it can use the produced data to update the UI.
     */
    private class ArticleAsyncTask extends AsyncTask<String, Void, List<Article>>{


        /**
         * This method runs on a background thread and performs the network request.
         * We should not update the UI from a background thread, so we return a list of
         * {@link Article}s as the result.
         */
        @Override
        protected List<Article> doInBackground(String... urls) {
            // Don't perform the request if there are no URLs, or the first URL is null
            if(urls.length < 1 || urls[0] == null) {
                return null;
            }

            List<Article> articles = QueryUtils.fetchArticles(urls[0]);

            // Return the {@link List<Article>} object
            return articles;
        }

        /**
         * This method runs on the main UI thread after the background work has been
         * completed. This method receives as input, the return value from the doInBackground()
         * method. First we clear out the adapter, to get rid of article data from a previous
         * query to The Guardian. Then we update the adapter with the new list of articles,
         * which will trigger the ListView to re-populate its list items.
         */
        @Override
        protected void onPostExecute(List<Article> article) {
            // Clear the adapter of previous article data
            if(articleAdapter != null){
                articleAdapter.clear();
            }


            // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
            // data set. This will trigger the ListView to update.
            if (article != null && !article.isEmpty()) {
                articleAdapter.addAll(article);
            }

        }
    }
}
