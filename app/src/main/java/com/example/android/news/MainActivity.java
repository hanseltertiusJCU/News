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
        ArrayList<Article> articles = new ArrayList<Article>();

        articles.add(new Article(null, "Education", "2018-11-05", "18:20:40", "Let’s have perspective in tuition fees debate | Letters"));
        articles.add(new Article(null, "Politics", "2018-10-31", "06:00:12", "Debate on final Brexit deal could use rare Commons procedure"));

        // Create a new ArticleAdapter object
        ArticleAdapter articleAdapter = new ArticleAdapter(this, articles);

        // Search for ListView
        ListView listView = (ListView) findViewById(R.id.news_list);

        // Set the content of ArticleAdapter into the ListView
        listView.setAdapter(articleAdapter);
    }
}
