package cn.yuyun.yymy.sp;


import android.content.Context;
import android.content.SharedPreferences;


public class StorePeoplePrefs {

    private static final String PREFERENCE_NAME = "store_people_sp";
    private final String STORE_PEOPLE = "store_people";

    private static StorePeoplePrefs instance = null;

    public static synchronized StorePeoplePrefs getInstance(Context context) {
        if (instance == null) {
            instance = new StorePeoplePrefs(context);
        }
        return instance;
    }

    private final SharedPreferences prefs;

    private StorePeoplePrefs(Context context) {
        prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public void saveInfo(String json1) {
        prefs.edit().putString(STORE_PEOPLE, json1).apply();
    }

    public String getJson() {
        return prefs.getString(STORE_PEOPLE, "");
    }

    public void clearInfo() {
        prefs.edit().putString(STORE_PEOPLE, "").apply();
    }
}
