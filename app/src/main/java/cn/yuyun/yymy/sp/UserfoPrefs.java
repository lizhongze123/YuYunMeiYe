package cn.yuyun.yymy.sp;


import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import cn.yuyun.yymy.constan.Constans;
import cn.yuyun.yymy.http.result.AccountInfoBean;
import cn.yuyun.yymy.http.result.ResultPermissionHq;
import cn.yuyun.yymy.http.result.UserInfoBean;


public class UserfoPrefs {

    private final String PREFERENCE_NAME = "user_info_prefs";
    private final String AVATAR = "avatar";
    private final String MOBILE = "mobile";
    private final String POSITION_ID = "position_id";
    private final String POSITION_NAME = "position_name";
    private final String STAFF_ID = "staff_id";
    private final String STAFF_NAME = "staffName";
    private final String STAFF_NUMBER = "staff_number";
    private final String OG_ID = "og_id";
    private final String OG_TYPE = "og_type";
    private final String BASEON_ID = "baseon_id";
    private final String BASEON_TYPE = "baseon_type";
    private final String SEX = "sex";
    private final String BASEON_TYPE_DESC = "baseon_type_desc";
    private final String BASEON_ID_DESC = "baseon_id_desc";
    private final String ACCOUNT_PERMISSION = "account_permission";
    private final String ACCOUNT_PERMISSION_HQ = "account_permission_hq";
    private final String ACCOUNT_PERMISSION_ALL = "account_permission_all";
    private final String MECHANIC = "mechanic";
    private final String GROUP_NAME = "group_name";

    private static UserfoPrefs instance = null;

    public static synchronized UserfoPrefs getInstance(Context context) {
        if (instance == null) {
            instance = new UserfoPrefs(context);
        }
        return instance;
    }

    private final SharedPreferences prefs;

    private UserfoPrefs(Context context) {
        prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public void saveUserInfo(AccountInfoBean.StaffInfoBean bean) {
        if(bean != null){
            if(TextUtils.isEmpty(bean.avatar)){
                prefs.edit().putString(AVATAR, "无").apply();
            }else{
                prefs.edit().putString(AVATAR, bean.avatar).apply();
            }
            prefs.edit().putString(MOBILE, bean.mobile).apply();
            prefs.edit().putString(POSITION_ID, bean.position_id).apply();
            prefs.edit().putString(POSITION_NAME, bean.position_name).apply();
            prefs.edit().putString(STAFF_ID, bean.staff_id).apply();
            prefs.edit().putString(STAFF_NAME, bean.staff_name).apply();
            prefs.edit().putString(STAFF_NUMBER, bean.staff_number).apply();
            prefs.edit().putString(BASEON_ID, bean.baseon_id).apply();
            prefs.edit().putString(BASEON_TYPE, bean.baseon_type).apply();
            prefs.edit().putString(SEX, bean.sex.desc).apply();
            prefs.edit().putString(BASEON_TYPE_DESC, bean.baseon_desc).apply();
            prefs.edit().putString(BASEON_ID_DESC, bean.baseon_desc).apply();
            prefs.edit().putInt(MECHANIC, bean.mechanic).apply();
            Constans.CURRENT_USER_NAME = bean.staff_name;
        }
    }

    public String getSEX() {
        return prefs.getString(SEX, "") ;
    }

    public String getAvatar() {
        return prefs.getString(AVATAR, "无") ;
    }

    public String getMobile() {
        return prefs.getString(MOBILE, "") ;
    }

    public String getPositionId() {
        return prefs.getString(POSITION_ID, "") ;
    }

    public String getPositionName() {
        return prefs.getString(POSITION_NAME, "") ;
    }

    public String getStaffName() {
        return prefs.getString(STAFF_NAME, "") ;
    }

    public String getStaffNumber() {
        return prefs.getString(STAFF_NUMBER, "") ;
    }

    public String getOgId() {
        return prefs.getString(OG_ID, "") ;
    }

    public String getOgType() {
        return prefs.getString(OG_TYPE, "") ;
    }

    public String getBaseonId() {
        return prefs.getString(BASEON_ID, "") ;
    }

    public String getBaseonType() {
        return prefs.getString(BASEON_TYPE, "") ;
    }

    public String getBaseonTypeDesc() {
        return prefs.getString(BASEON_TYPE_DESC, "") ;
    }

    public Integer getMechanic() {
        return prefs.getInt(MECHANIC, 0) ;
    }

    public void setMechanic(int value) {
         prefs.edit().putInt(MECHANIC, value).apply();
    }

    public String getBaseonIdDesc() {
        return prefs.getString(BASEON_ID_DESC, "") ;
    }

    public void setOg(String ogId, String ogType){
        prefs.edit().putString(OG_TYPE, ogType).apply();
        prefs.edit().putString(OG_ID, ogId).apply();
    }

    public void setGroupName(String groupName){
        prefs.edit().putString(GROUP_NAME, groupName).apply();
    }

    public String getGroupName(){
        return prefs.getString(GROUP_NAME, "") ;
    }

    public void setPermission(boolean isAppSet){
        prefs.edit().putBoolean(ACCOUNT_PERMISSION, isAppSet).apply();
    }

    public Integer getPermissionHq(){
        return prefs.getInt(ACCOUNT_PERMISSION_HQ, 0);
    }

    public Integer getPermissionAll(){
        return prefs.getInt(ACCOUNT_PERMISSION_ALL, 0);
    }

    public void setPermissionHq(ResultPermissionHq bean){
        prefs.edit().putInt(ACCOUNT_PERMISSION_HQ, bean.headquarters).apply();
        prefs.edit().putInt(ACCOUNT_PERMISSION_ALL, bean.headquarters).apply();
    }

    public boolean getPermission(){
        return prefs.getBoolean(ACCOUNT_PERMISSION, false);
    }


    public void clearUserInfo() {
        prefs.edit().putString(PREFERENCE_NAME, "").apply();
        prefs.edit().putString(AVATAR, "").apply();
        prefs.edit().putString(MOBILE, "").apply();
        prefs.edit().putString(POSITION_ID, "").apply();
        prefs.edit().putString(POSITION_NAME, "").apply();
        prefs.edit().putString(STAFF_ID, "").apply();
        prefs.edit().putString(STAFF_NAME, "").apply();
        prefs.edit().putString(STAFF_NUMBER, "").apply();
        prefs.edit().putString(OG_ID, "").apply();
        prefs.edit().putString(OG_TYPE, "").apply();
        prefs.edit().putString(BASEON_ID, "").apply();
        prefs.edit().putString(BASEON_TYPE, "").apply();
        prefs.edit().putString(SEX, "").apply();
        prefs.edit().putString(BASEON_TYPE_DESC, "").apply();
        prefs.edit().putString(BASEON_ID_DESC, "").apply();
        prefs.edit().putBoolean(ACCOUNT_PERMISSION, false).apply();
        prefs.edit().putBoolean(ACCOUNT_PERMISSION, false).apply();
        prefs.edit().putInt(ACCOUNT_PERMISSION_HQ, 0).apply();
        prefs.edit().putInt(ACCOUNT_PERMISSION_ALL, 0).apply();
    }
}
