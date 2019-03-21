package cn.yuyun.yymy.sp;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import cn.lzz.utils.AESCipherUtil;


public class LoginInfoPrefs {

    private static final String PREFERENCE_NAME = "login_info_prefs";
    private static final String LOGIN_USERNAME = "login_username";
    private static final String LOGIN_PWD = "login_pwd";
    private static final String TOKEN_TYPE = "token_type";
    private static final String TOKEN_VALUE = "token_value";
    private static final String STAFF_ID = "staff_id";
    private static final String GROUP_ID = "group_id";
    private static final String LOGIN_TYPE = "login_type";
    private static final String LAUCH_PIC = "lauch_pic";

    private static LoginInfoPrefs instance = null;

    public static synchronized LoginInfoPrefs getInstance(Context context) {
        if (instance == null) {
            instance = new LoginInfoPrefs(context);
        }
        return instance;
    }

    private final SharedPreferences prefs;

    private LoginInfoPrefs(Context context) {
        prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public void saveToken(String tokenType, String tokenValue) {
        prefs.edit().putString(TOKEN_TYPE, tokenType).apply();
        prefs.edit().putString(TOKEN_VALUE, tokenValue).apply();
    }

    public String getToken() {
        return prefs.getString(TOKEN_TYPE, "") + prefs.getString(TOKEN_VALUE, "") ;
    }

    public void saveStaffId(String staffId) {
        prefs.edit().putString(STAFF_ID, staffId).apply();
    }

    public void saveGroupId(String groupId) {
        prefs.edit().putString(GROUP_ID, groupId).apply();
    }

    public String getGroupId() {
        return prefs.getString(GROUP_ID, "" ) ;
    }

    public String getStaffId() {
        return prefs.getString(STAFF_ID, "" ) ;
    }

   /* public void saveLoginType(boolean isStoreLogin) {
        prefs.edit().putBoolean(LOGIN_TYPE, isStoreLogin).apply();
    }

    public Boolean getLoginType() {
        return prefs.getBoolean(LOGIN_TYPE, true) ;
    }*/

    public void saveLoginerType(boolean isStoreLogin) {
        prefs.edit().putBoolean(LOGIN_TYPE, isStoreLogin).apply();
    }

    public Boolean getLoginerType() {
        return prefs.getBoolean(LOGIN_TYPE, true) ;
    }

    public void saveLoginInfo(String userName, String password) {
        try {
            prefs.edit().putString(LOGIN_PWD, password).apply();
        } catch (Exception e) {
            Log.e("lzz","AESCipherUtil::Encrypt::Exception::" + e.getMessage());
        }
        prefs.edit().putString(LOGIN_USERNAME, userName).apply();
    }

    public void clearLoginInfo() {
        prefs.edit().putString(LOGIN_PWD, "").apply();
        prefs.edit().putString(LOGIN_USERNAME, "").apply();
        prefs.edit().putString(TOKEN_TYPE, "").apply();
        prefs.edit().putString(TOKEN_VALUE, "").apply();
        prefs.edit().putString(STAFF_ID, "").apply();
        prefs.edit().putString(GROUP_ID, "").apply();
        prefs.edit().putString(LAUCH_PIC, "").apply();
    }

    public String getUserName() {
        return prefs.getString(LOGIN_USERNAME, "");
    }

    public String getPassword() {
        String pwd = prefs.getString(LOGIN_PWD, "");
        try {
        } catch (Exception e) {
            Log.e("lzz", "AESCipherUtil::Decrypt::Exception::" + e.getMessage());
            pwd = "";
        }
        return pwd;
    }

    public void clearPassword() {
        prefs.edit().putString(LOGIN_PWD, "").apply();
    }

    public void saveLauchPic(String json) {
        prefs.edit().putString(LAUCH_PIC, json).apply();
    }

    public String getLauchPic() {
        return prefs.getString(LAUCH_PIC, "");
    }


}
