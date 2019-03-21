package cn.yuyun.yymy.sp;


import android.content.Context;
import android.content.SharedPreferences;


public class StorePrefs {

    private static final String PREFERENCE_NAME = "store_list_sp";
    private final String STORE = "store";

    private static StorePrefs instance = null;

    public static synchronized StorePrefs getInstance(Context context) {
        if (instance == null) {
            instance = new StorePrefs(context);
        }
        return instance;
    }

    private final SharedPreferences prefs;

    private StorePrefs(Context context) {
        prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public void saveInfo(String json1) {
        prefs.edit().putString(STORE, json1).apply();
    }

    public String getJson() {
        return prefs.getString(STORE, "");
    }

    public void clearInfo() {
        prefs.edit().putString(STORE, "").apply();
    }
}
