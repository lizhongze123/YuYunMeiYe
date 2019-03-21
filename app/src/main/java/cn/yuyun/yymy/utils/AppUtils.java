package cn.yuyun.yymy.utils;

import android.content.Context;

import cn.yuyun.yymy.main.GuideActivity;
import cn.yuyun.yymy.sp.ApprovePrefs;
import cn.yuyun.yymy.sp.LoginInfoPrefs;
import cn.yuyun.yymy.sp.StorePeoplePrefs;
import cn.yuyun.yymy.sp.StorePrefs;
import cn.yuyun.yymy.sp.UserfoPrefs;

/**
 * @author
 * @desc
 * @date
 */

public class AppUtils {

    public static void clearThisUserInfo(Context context){
        LoginInfoPrefs.getInstance(context).clearLoginInfo();
        UserfoPrefs.getInstance(context).clearUserInfo();
        ApprovePrefs.getInstance(context).clearInfo();
        StorePrefs.getInstance(context).clearInfo();
        StorePeoplePrefs.getInstance(context).clearInfo();
    }

}
