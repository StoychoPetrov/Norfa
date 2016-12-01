package com.example.stoycho.norfa.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

/**
 * Created by stoycho on 10/28/16.
 */

public class SharedPref {

    private final static String APP_KEY = "app_key";
    private final static String API_KEY = "api_key";


    public static void setApiKey(Context context, String apiKey)
    {
        SharedPreferences sharedPref = context.getSharedPreferences(APP_KEY,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(API_KEY, apiKey);
        editor.apply();
    }

    public static String getAppKey(Context context)
    {
        SharedPreferences sharedPref = context.getSharedPreferences(APP_KEY,Context.MODE_PRIVATE);
        return sharedPref.getString(API_KEY, null);
    }


}
