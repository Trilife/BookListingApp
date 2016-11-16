package com.example.android.booklistingapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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


public class MainActivity extends AppCompatActivity {

    //Tag for log messages
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    // book loader ID
    private static final int BOOK_LOADER_ID = 1;

    // Store url variable here
    public String finalUrl;

    //Adapter for the list in books
    // TODO private BookAdapter mAdapter;

    Button mSearchButton;
    EditText mSearchTerm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchButton = (Button) findViewById(R.id.search_button);
        mSearchTerm = (EditText) findViewById(R.id.search_term);

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchTerm = mSearchTerm.getText().toString();
                finalUrl = getString(R.string.google_books_api) + searchTerm;
                Log.v("EditText", mSearchTerm.getText().toString());
                displayTestSearchTerm("final URL: " + finalUrl);
            }
        });
        //Kick off a new AsyncTask to perform the network request
        BookAsyncTask task = new BookAsyncTask();
        task.execute();
    }
    /**
     * Update the screen to display information from the given {@link Book}.
     */
    private void updateUi(Book book) {
        setContentView(R.layout.list_item);

        // Display the book title in the UI
        TextView titleTextView = (TextView) findViewById(R.id.title);
        titleTextView.setText(book.getTitle());

        // Display the book author in the UI
        TextView dateTextView = (TextView) findViewById(R.id.author);
        dateTextView.setText(book.getAuthors());
    }
    /**
     * {@link AsyncTask} to perform the network request on a background thread, and then
     * update the UI with the books in the response.
     */
    private class BookAsyncTask extends AsyncTask<URL, Void, Book> {

        @Override
        protected Book doInBackground(URL... urls) {
            // Create URL object
            URL url = createUrl(finalUrl);

            // Perform HTTP request to the URL and receive a JSON response back
            String jsonResponse = "";
            try {
                jsonResponse = makeHttpRequest(url);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
            }

            // Extract relevant fields from the JSON response and create an {@link Event} object
            Book book = extractItemsFromJson(jsonResponse);

            // Return the {@link Event} object as the result fo the {@link TsunamiAsyncTask}
            return book;
        }

        /**
         * Update the screen with the given book (which was the result of the
         * {@link BookAsyncTask}).
         */
        @Override
        protected void onPostExecute(Book book) {
            if (book == null) {
                return;
            }

            updateUi(book);
        }

        /**
         * Returns new URL object from the given string URL.
         */
        private URL createUrl(String stringUrl) {
            URL url = null;
            try {
                url = new URL(stringUrl);
            } catch (MalformedURLException exception) {
                Log.e(LOG_TAG, "Error with creating URL", exception);
                return null;
            }
            return url;
        }

        /**
         * Make an HTTP request to the given URL and return a String as the response.
         */
        private String makeHttpRequest(URL url) throws IOException {
            String jsonResponse = "";
            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(15000 /* milliseconds */);
                urlConnection.connect();

                // check for a good response of 200, and read the response
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = urlConnection.getInputStream();
                    jsonResponse = readFromStream(inputStream);
                } else {
                    Log.e(LOG_TAG, "error code: " + urlConnection.getResponseCode());
                }
            } catch (IOException e) {
                Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (inputStream != null) {
                    // function must handle java.io.IOException here
                    inputStream.close();
                }
            }
            return jsonResponse;
        }

        /**
         * Convert the {@link InputStream} into a String which contains the
         * whole JSON response from the server.
         */
        private String readFromStream(InputStream inputStream) throws IOException {
            StringBuilder output = new StringBuilder();
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
            }
            return output.toString();
        }

        /**
         * //     * Return a list of {@link Book} objects that has been built up from
         * //     * parsing the given JSON response.
         * //
         */
//        private List<Book> extractItemsFromJson(String bookJSON) {
//            // If the JSON string is empty or null, then return early.
//            if (TextUtils.isEmpty(bookJSON)) {
//                return null;
//            }
//
//            // Create an empty ArrayList that we can start adding books to
//            List<Book> books = new ArrayList<>();
//
//            // Try to parse the JSON response string. If there's a problem with the way the JSON
//            // is formatted, a JSONException exception object will be thrown.
//            // Catch the exception so the app doesn't crash, and print the error message to the logs.
//            try {
//
//                // Create a JSONObject from the JSON response string
//                JSONObject baseJsonResponse = new JSONObject(bookJSON);
//
//                // Extract the JSONArray associated with the key called "features",
//                // which represents a list of features (or earthquakes).
//                JSONArray bookArray = baseJsonResponse.getJSONArray("items");
//
//                // For each book in the bookArray, create an {@link Book} object
//                for (int i = 0; i < bookArray.length(); i++) {
//
//                    // Get a single earthquake at position i within the list of earthquakes
//                    JSONObject currentBook = bookArray.getJSONObject(i);
//
//                    // For a given book, extract the JSONObject associated with the
//                    // key called "volumeInfo", which represents a list of all properties
//                    // for that book.
//                    JSONObject volumeInfo = currentBook.getJSONObject("volumeInfo");
//
//                    // Extract the value for the key called "title"
//                    String title = volumeInfo.getString("title");
//
//                    // Extract the value for the key called "authors"
//                    String authors = volumeInfo.getString("authors");
//
//                    // and url from the JSON response.
//                    Book book = new Book(title, authors);
//
//                    // Add the new {@link Earthquake} to the list of earthquakes.
//                    books.add(book);
//                }
//
//            } catch (JSONException e) {
//                // If an error is thrown when executing any of the above statements in the "try" block,
//                // catch the exception here, so the app doesn't crash. Print a log message
//                // with the message from the exception.
//                Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
//            }
//
//            // Return the list of books
//            return books;
//        }
        /**
         * //     * Return a list of {@link Book} objects that has been built up from
         * //     * parsing the given JSON response.
         * //
         */
        private Book extractItemsFromJson(String bookJSON) {

            // If the JSON string is empty or null, then return early.
            if (TextUtils.isEmpty(bookJSON)) {
                return null;
            }

            // Create an empty ArrayList that we can start adding books to
           // List<Book> books = new ArrayList<>();

            // Try to parse the JSON response string. If there's a problem with the way the JSON
            // is formatted, a JSONException exception object will be thrown.
            // Catch the exception so the app doesn't crash, and print the error message to the logs.
            try {

                // Create a JSONObject from the JSON response string
                JSONObject baseJsonResponse = new JSONObject(bookJSON);

                // Extract the JSONArray associated with the key called "features",
                // which represents a list of features (or earthquakes).
                JSONArray bookArray = baseJsonResponse.getJSONArray("items");

                    // Get a single earthquake at position i within the list of earthquakes
                    JSONObject currentBook = bookArray.getJSONObject(0);

                    // For a given book, extract the JSONObject associated with the
                    // key called "volumeInfo", which represents a list of all properties
                    // for that book.
                    JSONObject volumeInfo = currentBook.getJSONObject("volumeInfo");

                    // Extract the value for the key called "title"
                    String title = volumeInfo.getString("title");
                    Log.v(LOG_TAG, "parsed title: " + title);
                    // Extract the value for the key called "authors"
                    String authors = volumeInfo.getString("authors");
                    Log.v(LOG_TAG, "parsed authors: " + authors);

                    // and url from the JSON response.
                    return new Book(title, authors);


            } catch (JSONException e) {
                // If an error is thrown when executing any of the above statements in the "try" block,
                // catch the exception here, so the app doesn't crash. Print a log message
                // with the message from the exception.
                Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
            }

            // Return the list of books
            return null;
        }

    }
    /** Define the Book Object that holds the details of the book*/
    public class Book {
        /** Title of the Book*/
        private String mTitle;

        /** Author of the book */
        //TODO might need to turn this in to an array to catch more authors
        private String mAuthors;

        /** Construt a new Book object*/
        public Book(String title, String authors) {
            title = mTitle;
            authors = mAuthors;
        }
        /**define the constructors for the new object*/
        public String getTitle() {return mTitle; }
        public String getAuthors() {return mAuthors; }



    }

    private void displayTestSearchTerm(String searchTerm) {
        setContentView(R.layout.list_item);
        Log.v("searchTerm", searchTerm);
        TextView testTextView = (TextView) findViewById(R.id.author);
        testTextView.setText("result" + searchTerm);
    }
}
