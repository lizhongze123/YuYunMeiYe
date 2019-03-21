package cn.yuyun.yymy.utils;

import android.content.Context;
import android.provider.Settings;

/**
 * @author
 * @desc 屏幕旋转工具类
 * @date
 */
public class ScreenRotateUtils {

    /**判断手机是否打开了重力感应*/
    public static Boolean isOpenGravity(Context mContext){
        int screenChange;
        try {
            screenChange = Settings.System.getInt(mContext.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        if(screenChange == 1){
            return true;
        }else{
            return false;
        }
    }
}
