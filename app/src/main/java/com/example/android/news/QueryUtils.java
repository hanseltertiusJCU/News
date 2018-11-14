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

    /** Sample JSON response for a query from The Guardian*/
    private static final String SAMPLE_JSON_RESPONSE = "{\"response\":{\"status\":\"ok\",\"userTier\":\"developer\",\"total\":14062,\"startIndex\":1,\"pageSize\":10,\"currentPage\":1,\"pages\":1407,\"orderBy\":\"relevance\",\"results\":[{\"id\":\"education/2018/nov/05/lets-have-perspective-in-tuition-fees-debate\",\"type\":\"article\",\"sectionId\":\"education\",\"sectionName\":\"Education\",\"webPublicationDate\":\"2018-11-05T18:20:40Z\",\"webTitle\":\"Let’s have perspective in tuition fees debate | Letters\",\"webUrl\":\"https://www.theguardian.com/education/2018/nov/05/lets-have-perspective-in-tuition-fees-debate\",\"apiUrl\":\"https://content.guardianapis.com/education/2018/nov/05/lets-have-perspective-in-tuition-fees-debate\",\"isHosted\":false,\"pillarId\":\"pillar/news\",\"pillarName\":\"News\"},{\"id\":\"politics/2018/oct/31/debate-final-brexit-deal-could-use-rare-commons-procedure\",\"type\":\"article\",\"sectionId\":\"politics\",\"sectionName\":\"Politics\",\"webPublicationDate\":\"2018-10-31T06:00:12Z\",\"webTitle\":\"Debate on final Brexit deal could use rare Commons procedure\",\"webUrl\":\"https://www.theguardian.com/politics/2018/oct/31/debate-final-brexit-deal-could-use-rare-commons-procedure\",\"apiUrl\":\"https://content.guardianapis.com/politics/2018/oct/31/debate-final-brexit-deal-could-use-rare-commons-procedure\",\"isHosted\":false,\"pillarId\":\"pillar/news\",\"pillarName\":\"News\"},{\"id\":\"news/2018/jul/18/debate-continues-over-labours-code-on-antisemitism\",\"type\":\"article\",\"sectionId\":\"news\",\"sectionName\":\"News\",\"webPublicationDate\":\"2018-07-18T17:10:48Z\",\"webTitle\":\"Debate continues over Labour’s code on antisemitism | Letters\",\"webUrl\":\"https://www.theguardian.com/news/2018/jul/18/debate-continues-over-labours-code-on-antisemitism\",\"apiUrl\":\"https://content.guardianapis.com/news/2018/jul/18/debate-continues-over-labours-code-on-antisemitism\",\"isHosted\":false,\"pillarId\":\"pillar/news\",\"pillarName\":\"News\"},{\"id\":\"politics/2018/sep/19/momentum-wont-block-second-brexit-vote-debate-at-labour-conference\",\"type\":\"article\",\"sectionId\":\"politics\",\"sectionName\":\"Politics\",\"webPublicationDate\":\"2018-09-19T16:00:22Z\",\"webTitle\":\"Momentum won't block second Brexit vote debate at Labour conference\",\"webUrl\":\"https://www.theguardian.com/politics/2018/sep/19/momentum-wont-block-second-brexit-vote-debate-at-labour-conference\",\"apiUrl\":\"https://content.guardianapis.com/politics/2018/sep/19/momentum-wont-block-second-brexit-vote-debate-at-labour-conference\",\"isHosted\":false,\"pillarId\":\"pillar/news\",\"pillarName\":\"News\"},{\"id\":\"politics/2018/sep/13/labour-conference-must-debate-brexit-deal-vote-say-constituencies\",\"type\":\"article\",\"sectionId\":\"politics\",\"sectionName\":\"Politics\",\"webPublicationDate\":\"2018-09-13T17:54:54Z\",\"webTitle\":\"Labour conference must debate Brexit deal vote, say local groups\",\"webUrl\":\"https://www.theguardian.com/politics/2018/sep/13/labour-conference-must-debate-brexit-deal-vote-say-constituencies\",\"apiUrl\":\"https://content.guardianapis.com/politics/2018/sep/13/labour-conference-must-debate-brexit-deal-vote-say-constituencies\",\"isHosted\":false,\"pillarId\":\"pillar/news\",\"pillarName\":\"News\"},{\"id\":\"politics/2018/jun/22/working-class-concerns-ignored-in-brexit-debate\",\"type\":\"article\",\"sectionId\":\"politics\",\"sectionName\":\"Politics\",\"webPublicationDate\":\"2018-06-22T15:45:21Z\",\"webTitle\":\"Working-class concerns ignored in Brexit debate | Letters\",\"webUrl\":\"https://www.theguardian.com/politics/2018/jun/22/working-class-concerns-ignored-in-brexit-debate\",\"apiUrl\":\"https://content.guardianapis.com/politics/2018/jun/22/working-class-concerns-ignored-in-brexit-debate\",\"isHosted\":false,\"pillarId\":\"pillar/news\",\"pillarName\":\"News\"},{\"id\":\"politics/2018/sep/12/mps-to-propose-allowing-proxy-votes-for-members-absent-for-family-reasons\",\"type\":\"article\",\"sectionId\":\"politics\",\"sectionName\":\"Politics\",\"webPublicationDate\":\"2018-09-11T23:00:57Z\",\"webTitle\":\"MPs to debate allowing proxy votes for members with babies\",\"webUrl\":\"https://www.theguardian.com/politics/2018/sep/12/mps-to-propose-allowing-proxy-votes-for-members-absent-for-family-reasons\",\"apiUrl\":\"https://content.guardianapis.com/politics/2018/sep/12/mps-to-propose-allowing-proxy-votes-for-members-absent-for-family-reasons\",\"isHosted\":false,\"pillarId\":\"pillar/news\",\"pillarName\":\"News\"},{\"id\":\"politics/2018/jun/08/theresa-may-enters-northern-ireland-abortion-debate\",\"type\":\"article\",\"sectionId\":\"politics\",\"sectionName\":\"Politics\",\"webPublicationDate\":\"2018-06-08T15:06:45Z\",\"webTitle\":\"Theresa May enters Northern Ireland abortion debate\",\"webUrl\":\"https://www.theguardian.com/politics/2018/jun/08/theresa-may-enters-northern-ireland-abortion-debate\",\"apiUrl\":\"https://content.guardianapis.com/politics/2018/jun/08/theresa-may-enters-northern-ireland-abortion-debate\",\"isHosted\":false,\"pillarId\":\"pillar/news\",\"pillarName\":\"News\"},{\"id\":\"news/2018/apr/26/putting-the-antisemitism-debate-in-perspective\",\"type\":\"article\",\"sectionId\":\"news\",\"sectionName\":\"News\",\"webPublicationDate\":\"2018-04-26T16:31:31Z\",\"webTitle\":\"Putting the antisemitism debate in perspective | Letters\",\"webUrl\":\"https://www.theguardian.com/news/2018/apr/26/putting-the-antisemitism-debate-in-perspective\",\"apiUrl\":\"https://content.guardianapis.com/news/2018/apr/26/putting-the-antisemitism-debate-in-perspective\",\"isHosted\":false,\"pillarId\":\"pillar/news\",\"pillarName\":\"News\"},{\"id\":\"business/2018/jun/26/brexit-businesses-david-blanchflower-andrew-sentance\",\"type\":\"article\",\"sectionId\":\"business\",\"sectionName\":\"Business\",\"webPublicationDate\":\"2018-06-26T12:12:37Z\",\"webTitle\":\"'Brexit is scaring businesses to death' –  experts debate the data\",\"webUrl\":\"https://www.theguardian.com/business/2018/jun/26/brexit-businesses-david-blanchflower-andrew-sentance\",\"apiUrl\":\"https://content.guardianapis.com/business/2018/jun/26/brexit-businesses-david-blanchflower-andrew-sentance\",\"isHosted\":false,\"pillarId\":\"pillar/news\",\"pillarName\":\"News\"}]}}";

    /** Tag for the log messages */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Return a list of {@link Article} objects that has been built up from
     * parsing the given JSON response.
     */
    public static ArrayList<Article> extractFeatureFromJson() {

        // Create an empty ArrayList that we can start adding articles to
        ArrayList<Article> articles = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(SAMPLE_JSON_RESPONSE);

            JSONObject jsonObject = baseJsonResponse.getJSONObject("response");

            // Extract the JSONArray associated with the key called "results",
            // which represents a list of features (or articles).
            JSONArray articleArray = jsonObject.getJSONArray("results");

            // For each earthquake in the articleArray, create an {@link Article} object
            for (int i = 0; i < articleArray.length(); i++) {

                // Get a single article at position i within the list of articles
                JSONObject currentArticle = articleArray.getJSONObject(i);

                // Extract the value for the key called "mag"
                String topic = currentArticle.getString("sectionName");

                // Extract the value for the key called "webPublicationDate"
                String dateTime = currentArticle.getString("webPublicationDate");

                // Create a StringArray that split into two parts: webDate and webTime
                String[] webTimePublished = dateTime.split("T");

                // Initiate two String values to be the part of webTimePublished
                String webDate;
                String webTime;

                // Assign the value of webDate and webTime
                webDate = webTimePublished[0];
                webTime = webTimePublished[1];

                // Remove an unwanted character on webTime
                webTime = webTime.replace("Z", "");

                // Extract the value for the key called "webTitle"
                String title = currentArticle.getString("webTitle");

                //todo: put the url for intent

                // Create a new {@link Article} object with the magnitude, location, time,
                // and title from the JSON response.
                Article article = new Article(null, topic, webDate, webTime, title);

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

}
