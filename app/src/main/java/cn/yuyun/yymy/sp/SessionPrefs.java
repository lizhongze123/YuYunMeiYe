package cn.yuyun.yymy.sp;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.lzz.utils.LogUtils;

/**
 * @author
 * @desc
 * @date
 */
public class SessionPrefs {

    private static final String PREFERENCE_NAME = "session_sp";
    private final String USERNAME = "username";

    private final SharedPreferences prefs;

    private static SessionPrefs instance = null;

    public static synchronized SessionPrefs getInstance(Context context) {
        if (instance == null) {
            instance = new SessionPrefs(context);
        }
        return instance;
    }

    private SessionPrefs(Context context) {
        prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public void delAccount(String userName){
        Set<String> set = new HashSet<>();
        set = prefs.getStringSet(USERNAME, null);
        SharedPreferences.Editor editor = prefs.edit();
        set.remove(userName);
        editor.remove(USERNAME);
        //清除后必须再提交一下，否则没有效果
        editor.commit();
        editor.putStringSet(USERNAME, set);
        editor.commit();
    }

    public void saveLoginInfo(String userName, String passWord) {
        prefs.edit().putString(userName, passWord).apply();
        Set<String> set = new HashSet<>();
        set = prefs.getStringSet(USERNAME, null);
        SharedPreferences.Editor editor = prefs.edit();
        if(set != null){
            if(set.contains(userName)){
                LogUtils.e("已存在此账号");
                return;
            }

            set.add(userName);
            //清除必须要有
            editor.remove(USERNAME);
            //清除后必须再提交一下，否则没有效果
            editor.commit();
            editor.putStringSet(USERNAME, set);
            editor.commit();
        }else{
            set = new HashSet<>();
            set.add(userName);
            editor.putStringSet(USERNAME, set);
            editor.commit();
        }
    }

    public void saveAvatar(String account, String avatar) {
        if(null != avatar){
            prefs.edit().putString(account + "0", avatar).apply();
        }
    }

    public void saveStaffName(String account, String staffName) {
        if(null != staffName){
            prefs.edit().putString(account + "1", staffName).apply();
        }
    }

    public List<String> getAccount() {
        Set<String> set = new HashSet<>();
        set = prefs.getStringSet(USERNAME, null);
        List<String> user_list  = new ArrayList<>();
        if(set != null){
            //转化存储到list集合中
             user_list = new ArrayList(set);
        }
        return user_list;
    }

    public String getPassword(String key) {
        String pwd = prefs.getString(key, "");
        try {
        } catch (Exception e) {
            pwd = "";
        }
        return pwd;
    }

    public String getAvatar(String key) {
        if(null != key){
            key = key + "0";
        }
        String avatar = prefs.getString(key, "");
        try {

        } catch (Exception e) {
            avatar = "";
        }
        return avatar;
    }

    public String getStaffName(String key) {
        if(null != key){
            key = key + "1";
        }
        String staffName = prefs.getString(key, "");
        try {

        } catch (Exception e) {
            staffName = "";
        }
        return staffName;
    }

}
