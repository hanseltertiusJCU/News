package com.example.android.news;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.widget.TextView;

import static com.example.android.news.QueryUtils.getPagesCount;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<Article>>{

    /** Adapter for the list of articles */
    private ArticleAdapter articleAdapter;

    /** TextView that is displayed when the list is empty */
    private TextView emptyStateTextView;

    /** Sample JSON response for a query from The Guardian*/
    private static final String SAMPLE_JSON_RESPONSE_URL = "http://content.guardianapis.com/";

    private static final String API_KEY = "c61b895c-df8d-444f-a371-6c3715bf8c9c";

    /**
     * Constant value for the article loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int ARTICLE_LOADER_ID = 1;

    /**
     * Set the variable into global variable in order to be able to used in multiple places
     */
    private TextView fromDate;
    private TextView toDate;

    private String current_from_date;
    private String current_to_date;

    /**
     * Initialize variable from and to dates in order to be used in multiple places
     */
    private int mFromYear;
    private int mFromMonth;
    private int mFromDate;

    private int mToYear;
    private int mToMonth;
    private int mToDate;

    // Tracking for the current article page
    private int currentPage = 1;

    // Number of article pages
    private int totalPages = 0;

    // Change article page Up/Down
    private Button prevPage = null;
    private Button nextPage = null;

    // Create a new variable that returns ListView, which is used for retrieving article list
    private ListView listView;

    // Create a LinearLayout for finding the pageLayout menu
    private LinearLayout pageLayout;

    // Drawing the current and all article pages number
    private TextView currentPageText;
    private TextView allPagesText;

    // View for the loading spinner
    private View loadingIndicator;

    // Set the LoaderManager to be able to used in multiple occasions
    private LoaderManager loaderManager;

    // Setup the maximum number of pages
    private String maxPages;

    /**
     * Create a listener that change date in from section
     */
    private DatePickerDialog.OnDateSetListener mFromDateSetListener = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker datePicker, int year, int month, int date) {
            month = month + 1;

            current_from_date = String.format("%d-%02d-%02d", year, month, date);

            fromDate.setText(current_from_date);
        }



    };

    /**
     * Create a listener that change date in to section
     */
    private DatePickerDialog.OnDateSetListener mToDateSetListener = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker datePicker, int year, int month, int date) {
            month = month + 1;

            current_to_date = String.format("%d-%02d-%02d", year, month, date);

            toDate.setText(current_to_date);
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get Current Date in From section
        final Calendar fromCalendar = Calendar.getInstance();
        mFromYear = fromCalendar.get(Calendar.YEAR);
        mFromMonth = fromCalendar.get(Calendar.MONTH);
        mFromDate = fromCalendar.get(Calendar.DAY_OF_MONTH);

        // Format the date to be 'YYYY-MM-DD'
        current_from_date = String.format("%d-%02d-%02d", mFromYear, (mFromMonth + 1), mFromDate);

        // Get Current Date in To section
        final Calendar toCalendar = Calendar.getInstance();
        mToYear = toCalendar.get(Calendar.YEAR);
        mToMonth = toCalendar.get(Calendar.MONTH);
        mToDate = toCalendar.get(Calendar.DAY_OF_MONTH);

        // Format the date to be 'YYYY-MM-DD'
        current_to_date = String.format("%d-%02d-%02d", mToYear, (mToMonth + 1), mToDate);

        // Search for ListView
        listView = (ListView) findViewById(R.id.news_list);

        emptyStateTextView = (TextView) findViewById(R.id.empty_view);
        listView.setEmptyView(emptyStateTextView);

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

        // Search the LinearLayout with id
        pageLayout = (LinearLayout) findViewById(R.id.pageMenu);

        // Search the TextView with id fromDate
        fromDate = (TextView) findViewById(R.id.fromDate);
        // Set text in TextView to current date
        fromDate.setText(current_from_date);
        // Pop up the dialog when the TextView is being clicked
        fromDate.setOnClickListener(new View.OnClickListener() {

            public void onClick(View activity) {
                // Create a new Dialog object that shows the current date
                Dialog mFromDialog = new DatePickerDialog(MainActivity.this,
                        mFromDateSetListener, mFromYear,
                        mFromMonth, mFromDate);

                mFromDialog.show();
            }
        });

        // Search the TextView with id toDate
        toDate = (TextView) findViewById(R.id.toDate);
        // Set text in TextView to current date
        toDate.setText(current_to_date);
        // Pop up the dialog when the TextView is being clicked
        toDate.setOnClickListener(new View.OnClickListener() {

            public void onClick(View activity) {
                // Create a new Dialog object that shows the current date
                Dialog mToDialog = new DatePickerDialog(MainActivity.this,
                        mToDateSetListener, mToYear,
                        mToMonth, mToDate);

                mToDialog.show();
            }
        });

        // Find the specific Button that refresh the content
        Button refresh = (Button) findViewById(R.id.refreshButton);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Show progressbar, hide list view while loading
                listView.setVisibility(View.GONE);
                loadingIndicator.setVisibility(View.VISIBLE);
                // Empty the emptyStateTextView if there is text in there
                if(emptyStateTextView.getText() != null) {
                    emptyStateTextView.setText(null);
                }

                // Get a reference to the ConnectivityManager to check state of network connectivity
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

                // Get details on the currently active default data network
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

                //Refresh list view by restart loader
                if (networkInfo != null && networkInfo.isConnected()) {
                    // Get a reference to the LoaderManager, in order to interact with loaders.
                    loaderManager = getLoaderManager();
                    // Restart the loader when clicking the button to retrieve new from-date and to-date query parameter
                    loaderManager.restartLoader(ARTICLE_LOADER_ID, null, MainActivity.this);
                    //Set start position
                    currentPage = 1;
                    // Show page navigation as well as listView
                    prevPage.setVisibility(View.INVISIBLE);
                    nextPage.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.VISIBLE);
                } else {
                    if (articleAdapter != null) {
                        articleAdapter.clear();
                    }
                    // Hide loading indicator as well as page navigation LinearLayout and show listView
                    // in order to enable emptyStateTextView text into the center
                    loadingIndicator.setVisibility(View.GONE);
                    pageLayout.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                    // Set the message no internet connection when there is no internet connectivity
                    emptyStateTextView.setText(R.string.no_internet_connection);
                }
            }
        });

        // Find reference for page down button
        prevPage = (Button) findViewById(R.id.previousPageButton);
        prevPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Show progressbar, hide list view while loading
                loadingIndicator.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
                // Handle min page
                if (currentPage > 1) {
                    currentPage--;
                }
                // Handle page down button visibility
                if (currentPage < 2) {
                    prevPage.setVisibility(View.INVISIBLE);
                }
                // Handle page up button visibility
                if (currentPage < totalPages) {
                    nextPage.setVisibility(View.VISIBLE);
                }

                // Get a reference to the ConnectivityManager to check state of network connectivity
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

                // Get details on the currently active default data network
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

                //Refresh list view
                if (networkInfo != null && networkInfo.isConnected()) {
                    // Get a reference to the LoaderManager, in order to interact with loaders.
                    loaderManager = getLoaderManager();
                    // Restart the loader when clicking the button to retrieve new from-date and to-date query parameter
                    loaderManager.restartLoader(ARTICLE_LOADER_ID, null, MainActivity.this);
                } else {
                    if (articleAdapter != null) {
                        articleAdapter.clear();
                    }
                    // Hide loading indicator as well as page navigation LinearLayout and show listView
                    // in order to enable emptyStateTextView text into the center
                    loadingIndicator.setVisibility(View.GONE);
                    pageLayout.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                    // Set the message no internet connection when there is no internet connectivity
                    emptyStateTextView.setText(R.string.no_internet_connection);
                }
            }
        });

        // Find reference for page up button
        nextPage = (Button) findViewById(R.id.nextPageButton);
        nextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Show progressbar, hide list view while loading
                loadingIndicator.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
                // Handle max page
                if (currentPage < totalPages) {
                    currentPage++;
                }
                // Handle page down button visibility
                if (currentPage > 1) {
                    prevPage.setVisibility(View.VISIBLE);
                }
                // Handle page up button visibility
                if (currentPage == totalPages) {
                    nextPage.setVisibility(View.INVISIBLE);
                }

                // Get a reference to the ConnectivityManager to check state of network connectivity
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

                // Get details on the currently active default data network
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

                //Refresh list view by restart loader
                if (networkInfo != null && networkInfo.isConnected()) {
                    // Get a reference to the LoaderManager, in order to interact with loaders.
                    loaderManager = getLoaderManager();
                    // Restart the loader when clicking the button to retrieve new from-date and to-date query parameter
                    loaderManager.restartLoader(ARTICLE_LOADER_ID, null, MainActivity.this);
                } else {
                    if (articleAdapter != null) {
                        articleAdapter.clear();
                    }
                    // Hide loading indicator as well as page navigation LinearLayout and show listView
                    // in order to enable emptyStateTextView text into the center
                    loadingIndicator.setVisibility(View.GONE);
                    pageLayout.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                    // Set the message no internet connection when there is no internet connectivity
                    emptyStateTextView.setText(R.string.no_internet_connection);
                }
            }
        });

        // Search for TextView that retrieve current page and all pages
        currentPageText = (TextView) findViewById(R.id.currentPage);
        allPagesText = (TextView) findViewById(R.id.pagesCount);

        // Set the text for current page and all pages
        currentPageText.setText(String.valueOf(currentPage));
        allPagesText.setText(String.valueOf(totalPages));

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(ARTICLE_LOADER_ID, null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator and page navigation layout so error message will be visible
            loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
            pageLayout.setVisibility(View.GONE);

            // Then, show the list view
            listView.setVisibility(View.VISIBLE);

            // Update empty state with no connection error message
            emptyStateTextView.setText(R.string.no_internet_connection);
        }

    }

    /**
     * Create a new Loader by returning a class that extends from Loader
     * @param i
     * @param bundle
     * @return ArticleLoader
     */
    @Override
    public Loader<List<Article>> onCreateLoader(int i, Bundle bundle) {

        // Create a SharedPreferences object
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Get a String value based on SharedPreferences object
        String topic = sharedPreferences.getString(getString(R.string.settings_topic_key), getString(R.string.settings_topic_default));
        String displayPages = sharedPreferences.getString(getString(R.string.settings_page_size_key), getString(R.string.settings_page_size_default));
        String sortBy = sharedPreferences.getString(getString(R.string.settings_order_by_key), getString(R.string.settings_order_by_default));
        maxPages = sharedPreferences.getString(getString(R.string.settings_max_pages_key), getString(R.string.settings_max_pages_default));


        // Create an Uri object based on topic values
        Uri baseUri = Uri.parse(SAMPLE_JSON_RESPONSE_URL + topic + "?");
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("page", String.valueOf(currentPage));
        uriBuilder.appendQueryParameter("page-size", displayPages);
        uriBuilder.appendQueryParameter("from-date", current_from_date);
        uriBuilder.appendQueryParameter("to-date", current_to_date);
        uriBuilder.appendQueryParameter("order-by", sortBy);
        uriBuilder.appendQueryParameter("show-fields", "thumbnail");
        uriBuilder.appendQueryParameter("show-tags", "contributor");
        uriBuilder.appendQueryParameter("api-key", API_KEY);

        return new ArticleLoader(this, uriBuilder.toString());
    }

    /**
     * Display the result when the loader finished on loading
     * @param loader
     * @param articles
     */
    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> articles) {

        // Hide loading indicator because the data has been loaded
        loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // Hide page navigation LinearLayout when we are going to set the empty data
        pageLayout.setVisibility(View.GONE);

        // Set empty state text to display "No articles found."
        emptyStateTextView.setText(R.string.no_articles);

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        // Check if there is a network connection, which affects the empty state text view output
        if (networkInfo != null && networkInfo.isConnected()) {
            // Hide the page navigation LinearLayout and show the listView to enable the emptyStateTextView
            // to be in the center
            pageLayout.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            // Set empty state text to display "There are no books to display."
            emptyStateTextView.setText(R.string.no_articles);
        } else {
            // Hide the page navigation LinearLayout and show the listView to enable the emptyStateTextView
            // to be in the center
            pageLayout.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            // Update empty state with no connection error message
            emptyStateTextView.setText(R.string.no_internet_connection);
        }

        // Clear the adapter of previous article data
        if(articleAdapter != null){
            articleAdapter.clear();
        }

        // If there is a valid list of {@link Article}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (articles != null && !articles.isEmpty()) {
            articleAdapter.addAll(articles);
        }

        // Check if maxPages based on SharedPreferences does not equal to all
        if(!(maxPages.equals("all"))) {
            // Get number of pages
            totalPages = getPagesCount();
            // Check if totalPages is more than maxPages
            if(totalPages > Integer.parseInt(maxPages)){
                totalPages = Integer.parseInt(maxPages);
            }
        } else {
            // Get number of pages
            totalPages = getPagesCount();
        }

        // If no article
        if (totalPages == 0) {
            if (articleAdapter != null) {
                articleAdapter.clear();
            }
            // Show list view, hide progress bar while loading in order for text in emptyStateTextView
            // to be in center
            loadingIndicator.setVisibility(View.GONE);
            pageLayout.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            emptyStateTextView.setText(getString(R.string.no_articles));
        } else {
            //Show list view, hide progress bar while loading
            loadingIndicator.setVisibility(View.GONE);
            pageLayout.setVisibility(View.VISIBLE);
            listView.setVisibility(View.VISIBLE);
        }

        // Set pages text
        currentPageText.setText(String.valueOf(currentPage));
        allPagesText.setText(String.valueOf(totalPages));

        // Handle one page up down button visibility
        if (totalPages == 1) {
            nextPage.setVisibility(View.INVISIBLE);
            prevPage.setVisibility(View.INVISIBLE);
        }
        if ((totalPages > 1) && (currentPage != 1)) {
            prevPage.setVisibility(View.VISIBLE);
            nextPage.setVisibility(View.VISIBLE);
        }
        if ((totalPages > 1) && (currentPage == totalPages)) {
            prevPage.setVisibility(View.VISIBLE);
            nextPage.setVisibility(View.INVISIBLE);
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

    /**
     * Inflate the main.xml under the menu directory
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * When the item is selected, go to another activity called SettingsActivity
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
