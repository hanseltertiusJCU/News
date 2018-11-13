package com.example.android.news;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ArticleAdapter extends ArrayAdapter<Article> {

    // Create a constructor for giving access to create an object
    public ArticleAdapter (Activity context, ArrayList<Article> articles) {
        super(context, 0, articles);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        // Check if listItemView is null
        if (listItemView == null) {
            // Inflate the listItemView based on list_item.xml
            listItemView = LayoutInflater.from(getContext()).inflate
                    (R.layout.list_item, parent, false);
        }

        // Get the {@link Article} object located at this position in the list
        Article currentArticle = getItem(position);

        // Find the ImageView in the list_item.xml layout with the ID article_image
        ImageView articleImageView = (ImageView) listItemView.findViewById(R.id.article_image);

        // Check if the article object has valid image
        if(currentArticle.hasArticleImage()){
            // Get image resource ID of the article and set the image resource into ImageView
            articleImageView.setImageResource(currentArticle.getmArticleImage());
            articleImageView.setVisibility(View.VISIBLE);
        } else {
            // Set the ImageView visibility to GONE if there is no image exists.
            articleImageView.setVisibility(View.GONE);
        }

        // Find the TextView in the list_item.xml layout with the ID author_text
        TextView authorTextView = (TextView) listItemView.findViewById(R.id.author_text);

        // Check if the article object has author
        if(currentArticle.hasArticleAuthor()){
            // Get article author and set it into TextView
            authorTextView.setText(currentArticle.getArticleAuthor());
            authorTextView.setVisibility(View.VISIBLE);
        } else {
            // Set the ImageView visibility to INVISIBLE if there is no author exists.
            authorTextView.setVisibility(View.INVISIBLE);
        }

        // Find the TextView in the list_item.xml layout with the ID date_text
        TextView topicTextView = (TextView) listItemView.findViewById(R.id.topic_text);
        // Get article topic and set it into TextView
        topicTextView.setText(currentArticle.getArticleTopic());

        // Find the TextView in the list_item.xml layout with the ID date_text
        TextView dateTextView = (TextView) listItemView.findViewById(R.id.date_text);

        // Check if the article object has date
        if(currentArticle.hasArticleDate()){
            // Get article date and set it into TextView
            dateTextView.setText(currentArticle.getArticleDate());
            dateTextView.setVisibility(View.VISIBLE);
        } else {
            // Set the TextView visibility to INVISIBLE if there is no date exists.
            dateTextView.setVisibility(View.INVISIBLE);
        }

        // Find the TextView in the list_item.xml layout with the ID time_text
        TextView timeTextView = (TextView) listItemView.findViewById(R.id.time_text);

        // Check if the article object has time
        if(currentArticle.hasArticleTime()){
            // Get article time and set it into TextView
            timeTextView.setText(currentArticle.getArticleTime());
            timeTextView.setVisibility(View.VISIBLE);
        } else {
            // Set the TextView visibility to INVISIBLE if there is no time exists.
            timeTextView.setVisibility(View.INVISIBLE);
        }

        // Find the TextView in the list_item.xml layout with the ID news_title
        TextView titleTextView = (TextView) listItemView.findViewById(R.id.news_title);
        // Get article title and set it into TextView
        titleTextView.setText(currentArticle.getArticleTitle());

        return listItemView;
    }
}
