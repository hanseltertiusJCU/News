package com.example.android.news;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods related to requesting and receiving articles from The Guardian.
 */
public final class QueryUtils {



    /** Tag for the log messages */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Initiate number of pages
     */
    private static int pagesCount = 1;

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Query the USGS dataset and return a list of {@link Article} objects.
     */
    public static List<Article> fetchArticles(String requestURL){

        // Create an URL object
        URL url = createURL(requestURL);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHTTPRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem on making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and
        // create an {@link List<Article>} object
        List<Article> articles = extractFeatureFromJson(jsonResponse);

        Log.e(LOG_TAG, "Fetching the data from List<Article> object");

        // Return the {@link List<Article>} object
        return articles;
    }

    /**
     * Returns a new URL object from the given String URL
     */
    private static URL createURL(String stringURL) {
        URL url = null;
        try{
            url = new URL(stringURL);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL", e);
        }
        return url;
    }

    /**
     * Make an HTTP request from the given URL and return a String object as the response.
     */
    private static String makeHTTPRequest(URL url) throws IOException {

        // Initialize a String object that represents jsonResponse
        String jsonResponse = "";

        // If an URL object on the parameter is null, we return empty jsonResponse
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            // Open the URL connection based on the given URL object
            urlConnection = (HttpURLConnection) url.openConnection();
            // Set the URL connection timeout to avoid on taking too long
            urlConnection.setReadTimeout(10000 /*ms*/);
            urlConnection.setConnectTimeout(15000 /*ms*/);
            // Set the request method for an HTTPURLConnection object
            urlConnection.setRequestMethod("GET");
            // Connect the HTTPURLConnection into the Internet
            urlConnection.connect();

            // Check if the request was successful (response code 200)
            if (urlConnection.getResponseCode() == 200) {
                // If the request was successful, read the input stream and parse
                // the response based on the given InputStream object
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error Response Code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e){
            Log.e(LOG_TAG, "Problem retrieving JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            } if (inputStream != null){
                // Closing the input stream could throw an IOException, which is why
                // the makeHTTPRequest(URL url) method signature specifies an IOException
                // could be thrown.
                inputStream.close();
            }
        }

        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        // Create a StringBuilder object
        StringBuilder output = new StringBuilder();
        if(inputStream != null){
            // Create a new InputStreamReader object
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            // Create a BufferedReader object that takes an InputStreamReader object
            BufferedReader reader = new BufferedReader(inputStreamReader);
            // Create a string object that reads line from a BufferedReader object
            String line = reader.readLine();
            // Check if line is not null and loop it until line is null
            while (line != null) {
                // If so, add the StringBuilder based on line
                output.append(line);
                line = reader.readLine();
            }
        }

        return output.toString();
    }

    /**
     * Return a list of {@link Article} objects that has been built up from
     * parsing the given JSON response.
     */
    private static List<Article> extractFeatureFromJson(String articlesJSON) {

        // If the JSON string is empty or null, then return early
        if (TextUtils.isEmpty(articlesJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding articles to
        List<Article> articles = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(articlesJSON);

            // Create a JSONObject based on baseJSONResponse
            JSONObject jsonObject = baseJsonResponse.getJSONObject("response");

            // Get the number of pages
            pagesCount = jsonObject.getInt("pages");

            // Extract the JSONArray associated with the key called "results",
            // which represents a list of results (or articles).
            JSONArray articleArray = jsonObject.getJSONArray("results");

            // For each earthquake in the articleArray, create an {@link Article} object
            for (int i = 0; i < articleArray.length(); i++) {

                // Get a single article at position i within the list of articles
                JSONObject currentArticle = articleArray.getJSONObject(i);

                // Get a JSONObject based on key "fields"
                JSONObject fields = currentArticle.getJSONObject("fields");

                // Declare String variable to hold thumbnail
                String thumbnail = null;

                // Check if fields exist, if so then get the object and assign the
                // thumbnail variable from "thumbnail" tag in "fields"
                if (fields.length() == 1) {
                    thumbnail = fields.getString("thumbnail");
                }

                // Extract the value for the key called "sectionName"
                String topic = currentArticle.getString("sectionName");

                // Extract the value for the key called "webPublicationDate"
                String dateTime = currentArticle.getString("webPublicationDate");

                // Extract the JSONArray with the key "tags"
                JSONArray tagsArray = currentArticle.getJSONArray("tags");

                // Declare String variable to hold author name
                String authorName = null;

                // Check if tagsArray exist, if so then get the array and assign the
                // authorName variable from "webTitle" tag in "tags"
                if (tagsArray.length() == 1) {
                    JSONObject contributorTag = (JSONObject) tagsArray.get(0);
                    authorName = contributorTag.getString("webTitle");
                }

                // Create a StringArray that split into two parts: webDate and webTime
                String[] webTimePublished = dateTime.split("T");

                // Initiate two String values to be the part of webTimePublished
                String webDate;
                String webTime;

                // Assign the value of webDate and webTime
                webDate = webTimePublished[0];
                webTime = webTimePublished[1];

                // Remove an unwanted character on webTime
                webTime = webTime.replace("Z", " GMT");

                // Extract the value for the key called "webTitle"
                String title = currentArticle.getString("webTitle");

                // Extract the value for the key called "webUrl"
                String url = currentArticle.getString("webUrl");

                // Create a new {@link Article} object with the magnitude, location, time,
                // title and url from the JSON response.
                Article article = new Article(thumbnail, authorName, topic, webDate, webTime, title, url);

                // Add the new {@link Article} to the list of articles.
                articles.add(article);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the article JSON results", e);
        }

        // Return the list of articles
        return articles;
    }

    /**
     * Return number of pages count
     * @return pagesCount
     */
    public static int getPagesCount() {
        return pagesCount;
    }

}
