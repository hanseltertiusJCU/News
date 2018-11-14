package com.example.android.news;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create a new ArrayList object
        ArrayList<Article> articles = QueryUtils.extractFeatureFromJson();

        // Create a new ArticleAdapter object
        final ArticleAdapter articleAdapter = new ArticleAdapter(this, articles);

        // Search for ListView
        ListView listView = (ListView) findViewById(R.id.news_list);

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

    }
}
