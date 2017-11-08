package org.dragonsbaneproject.android.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Brian on 11/6/17.
 */

public class HTTPHelper {

    public static String GET(String stringURL){

        InputStream in = null;
        HttpsURLConnection connection = null;
        String result = null;
        try {
            URL url = new URL(stringURL);
            connection = (HttpsURLConnection)url.openConnection();
            in = new BufferedInputStream(connection.getInputStream());
            // convert inputstream to string
            result = readInputStream(in);
        } catch (Exception e) {
            Log.d(HTTPHelper.class.getName(), e.getLocalizedMessage());
        } finally {
            if(connection != null) connection.disconnect();
            try {
                if(in != null) in.close();
            } catch (IOException e) {}
        }
        return result;
    }

    public static String GET(String stringURL, long waitTimeMS, int attempts, Context context){
        InputStream in = null;
        HttpsURLConnection connection = null;
        String result = null;
        int tries = 0;
        while(attempts > tries++) {
            if(isConnected(context)) {
                try {
                    URL url = new URL(stringURL);
                    connection = (HttpsURLConnection) url.openConnection();
                    in = new BufferedInputStream(connection.getInputStream());
                    // convert inputstream to string
                    result = readInputStream(in);
                } catch (Exception e) {
                    Log.d(HTTPHelper.class.getName(), e.getLocalizedMessage());
                } finally {
                    if (connection != null) connection.disconnect();
                    try {
                        if (in != null) in.close();
                    } catch (IOException e) {
                    }
                }
            } else {
                try {
                    Thread.sleep(waitTimeMS);
                } catch (InterruptedException e) {

                }
            }
        }
        return result;
    }

    public static String readInputStream(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;
        inputStream.close();
        return result;

    }

    // check network connection
    public static boolean isConnected(Context context){
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
