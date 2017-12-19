package com.servicehub.Controller;

import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Admin on 8/29/2016.
 */
public class Configr {

    //public static String app_url = "http://76.74.170.179/servicehub/api/index.php/";
    // public static String app_url = "http://5.189.141.180/76/servicehub/api/index.php/";
    // public static String app_url = "http://servicehub.net.in/api/index.php/";
    public static String app_url = "https://live.parshwatechnologies.in/107/parshwa/servicehub-php/api/index.php/";
    public static String headunm = "admin";
    public static String headkey = "8q3P499DVq6VTJ7Uc9voQD";


    // code to define map header
    public static Map<String, String> getHeaderParam() {
        Map<String, String> header = new HashMap<>();
        //header.put("apiuser", Configr.headunm);
        //header.put("apikey", Configr.headkey);

        return header;
    }

    //Date Formate
    public static SimpleDateFormat df1 = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
    public static SimpleDateFormat df2 = new SimpleDateFormat("dd MMM yyyy , EEEE", Locale.getDefault());
    public static SimpleDateFormat df3 = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
    public static SimpleDateFormat df4 = new SimpleDateFormat("HH");
    public static SimpleDateFormat df5 = new SimpleDateFormat("hh:mm a");
    public static SimpleDateFormat outFormat = new SimpleDateFormat("EEEE", Locale.getDefault());

    public static boolean isValidEmailAddress(String emailAddress) {
        String emailRegEx;
        Pattern pattern;
        // Regex for a valid email address
        emailRegEx = "^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,4}$";
        // Compare the regex with the email address
        pattern = Pattern.compile(emailRegEx);
        Matcher matcher = pattern.matcher(emailAddress);
        if (!matcher.find()) {
            return false;
        }
        return true;
    }

    public static int Screen(Context context) {
        int i = 1;
        Configuration config = context.getResources().getConfiguration();
        if (config.smallestScreenWidthDp >= 720 || config.smallestScreenWidthDp >= 600) {
            i = 0;
        } else {
            i = 1;
        }
        return i;
    }

    public static String getPath(Uri uri, Context context) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();
        cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();
        return path;
    }

    public static String getVedioPath(Uri uri, Context context) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();
        cursor = context.getContentResolver().query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Video.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
        cursor.close();
        return path;
    }

}
