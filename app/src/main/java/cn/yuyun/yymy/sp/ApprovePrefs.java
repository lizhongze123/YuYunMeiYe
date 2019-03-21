package cn.yuyun.yymy.sp;


import android.content.Context;
import android.content.SharedPreferences;

import cn.yuyun.yymy.constan.Constans;
import cn.yuyun.yymy.http.result.UserInfoBean;


public class ApprovePrefs {

    private static final String PREFERENCE_NAME = "approve_prefs";
    private final String WORK = "work";
    private final String WORK_AVATAR = "work_avatar";

    private static ApprovePrefs instance = null;

    public static synchronized ApprovePrefs getInstance(Context context) {
        if (instance == null) {
            instance = new ApprovePrefs(context);
        }
        return instance;
    }

    private final SharedPreferences prefs;

    private ApprovePrefs(Context context) {
        prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public void saveInfo(String json1) {
        prefs.edit().putString(WORK, json1).apply();
    }

    public String getJson1() {
        return prefs.getString(WORK, "");
    }

    public String getJson2() {
        return prefs.getString(WORK_AVATAR, "");
    }

    public void clearInfo() {
        prefs.edit().putString(WORK, "").apply();
        prefs.edit().putString(WORK_AVATAR, "").apply();
    }
}
