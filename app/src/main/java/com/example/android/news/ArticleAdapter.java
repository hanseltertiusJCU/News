package com.example.android.news;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
        // Get image resource ID of the article and set the image resource into ImageView
        articleImageView.setImageBitmap(formatImageFromBitmap(currentArticle.getmArticleImage()));

        // Find the TextView in the list_item.xml layout with the ID author_text
        TextView authorTextView = (TextView) listItemView.findViewById(R.id.author_text);

        // Check if the article object has author
        if(currentArticle.getArticleAuthor() != null){
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
        if(currentArticle.getArticleDate() != null){
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
        if(currentArticle.getArticleTime() != null){
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

    // Get the thumbnail image
    private Bitmap formatImageFromBitmap(Bitmap articleThumbnail) {
        // Bitmap for image
        Bitmap bitmapResult;
        // Check if the thumbnail is valid
        if (articleThumbnail == null) {
            // If not valid return default image
            bitmapResult = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.no_image_available);
        } else {
            // If valid return image based on article thumbnail
            bitmapResult = articleThumbnail;
        }
        // Return bitmap
        return bitmapResult;
    }
}
