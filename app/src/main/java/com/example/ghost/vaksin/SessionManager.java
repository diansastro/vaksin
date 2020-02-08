package com.example.ghost.vaksin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;

/**
 * Created by ghost on 25/05/16.
 */
public class SessionManager {
    public void setPreferences(Context context, String key, String value) {

        SharedPreferences.Editor editor = context.getSharedPreferences("Vaksin", Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.commit();

    }
    public  String getPreferences(Context context, String key) {

        SharedPreferences prefs = context.getSharedPreferences("Vaksin",	Context.MODE_PRIVATE);
        String position = prefs.getString(key, "");
        return position;
    }

    public void logout(Context context){
        SharedPreferences.Editor editor = context.getSharedPreferences("Vaksin", Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();
    }
}
