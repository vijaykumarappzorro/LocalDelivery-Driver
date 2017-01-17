package com.localdelivery.driver.model;

import android.content.Context;
import android.content.SharedPreferences;

public class LDPreferences {

    private static SharedPreferences getPreference(Context context) {
        return context.getSharedPreferences("LD_PREF", Context.MODE_PRIVATE);
    }

    public static String readString(Context context, String key) {
        return getPreference(context).getString(key, "");
    }

    public static void putString(Context context,String key,String value){
        getPreference(context).edit().putString(key,value).apply();
    }

    public static void getString(Context context , String key , String value){
        getPreference(context).getString(key,value);
    }

}
