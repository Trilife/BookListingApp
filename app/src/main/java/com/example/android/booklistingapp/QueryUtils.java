package com.example.android.booklistingapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JRStrele on 16/11/2016.
 */
//
//public class QueryUtils {
//
//    /** Tag for the log messages */
//    public static final String LOG_TAG = QueryUtils.class.getSimpleName();
//
//    /**
//     * Query the Book Dataset and return a list of {@link Book} objects.
//     */
//    public static List<Book> fetchBookData(String requestUrl) {
//        // Create URL object
//        URL url = createUrl(requestUrl);
//
//        // Perform HTTP request to the URL and receive a JSON response back
//        String jsonResponse = null;
//        try {
//            jsonResponse = makeHttpRequest(url);
//        } catch (IOException e) {
//            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
//        }
//
//        // Extract relevant fields from the JSON response and create a list of {@link Earthquake}s
//        List<Book> earthquakes = extractItemsFromJson(jsonResponse);
//
//        // Return the list of {@link Earthquake}s
//        return books;
//    }
//    /**
//     * Return a list of {@link Book} objects that has been built up from
//     * parsing the given JSON response.
//     */
//    private static List<Book> extractItemsFromJson(String bookJSON) {
//        // If the JSON string is empty or null, then return early.
//        if (TextUtils.isEmpty(bookJSON)) {
//            return null;
//        }
//
//        // Create an empty ArrayList that we can start adding earthquakes to
//        List<Book> earthquakes = new ArrayList<>();
//
//        // Try to parse the JSON response string. If there's a problem with the way the JSON
//        // is formatted, a JSONException exception object will be thrown.
//        // Catch the exception so the app doesn't crash, and print the error message to the logs.
//        try {
//
//            // Create a JSONObject from the JSON response string
//            JSONObject baseJsonResponse = new JSONObject(bookJSON);
//
//            // Extract the JSONArray associated with the key called "features",
//            // which represents a list of features (or earthquakes).
//            JSONArray bookArray = baseJsonResponse.getJSONArray("items");
//
//            // For each earthquake in the earthquakeArray, create an {@link Earthquake} object
//            for (int i = 0; i < bookArray.length(); i++) {
//
//                // Get a single earthquake at position i within the list of earthquakes
//                JSONObject currentEarthquake = bookArray.getJSONObject(i);
//
//                // For a given earthquake, extract the JSONObject associated with the
//                // key called "properties", which represents a list of all properties
//                // for that earthquake.
//                JSONObject volumeInfo = currentEarthquake.getJSONObject("volumeInfo");
//
//                // Extract the value for the key called "title"
//                String title = volumeInfo.getDouble("title");
//
//                // Extract the value for the key called "authors"
//                Array authors = volumeInfo.getJSONArray("authors");
//
//                // Create a new {@link Earthquake} object with the magnitude, location, time,
//                // and url from the JSON response.
//                Book book = new Book(title, authors);
//
//                // Add the new {@link Earthquake} to the list of earthquakes.
//                books.add(book);
//            }
//
//        } catch (JSONException e) {
//            // If an error is thrown when executing any of the above statements in the "try" block,
//            // catch the exception here, so the app doesn't crash. Print a log message
//            // with the message from the exception.
//            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
//        }
//
//        // Return the list of earthquakes
//        return earthquakes;
//    }
//
//}
