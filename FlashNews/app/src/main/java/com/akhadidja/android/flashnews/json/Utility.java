package com.akhadidja.android.flashnews.json;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Utility {

    private static final String NPR_BASE_URL = "http://api.npr.org/query?";

    private static String nprTopicUriBuilder(String apiKey, String topicId){
        Uri uri = Uri.parse(NPR_BASE_URL).buildUpon()
                .appendQueryParameter(NprApiEndpoints.TOPIC, topicId)
                .appendQueryParameter(NprApiEndpoints.FIELDS,
                        NprApiEndpoints.FIELD_TITLE +","+ NprApiEndpoints.FIELD_TEASER+","+
                        NprApiEndpoints.FIELD_STORY_DATE+","+ NprApiEndpoints.FIELD_TEXT+","+
                        NprApiEndpoints.FIELD_IMAGE+","+ NprApiEndpoints.FIELD_CORRECTION)
                .appendQueryParameter(NprApiEndpoints.OUTPUT, NprApiEndpoints.OUTPUT_JSON)
                .appendQueryParameter(NprApiEndpoints.API_KEY, apiKey)
                .build();

        return uri.toString();
    }

    public static String getJsonStringFromUrl(String apiKey, String topicId){

        String urlStr = nprTopicUriBuilder(apiKey, topicId);
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String jsonStr = null;

        try {
            URL url = new URL(urlStr);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }

            if (buffer.length() == 0) {
                return null;
            }
            jsonStr = buffer.toString();

        } catch (IOException e) {
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(Utility.class.getSimpleName(), "Error closing stream");
                }
            }
        }
        return jsonStr;
    }
}
