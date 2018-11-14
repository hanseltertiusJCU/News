package com.example.android.news;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
        ArticleAdapter articleAdapter = new ArticleAdapter(this, articles);

        // Search for ListView
        ListView listView = (ListView) findViewById(R.id.news_list);

        // Set the content of ArticleAdapter into the ListView
        listView.setAdapter(articleAdapter);
    }
}
