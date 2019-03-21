package cn.yuyun.yymy.constan;

import cn.yuyun.yymy.http.result.ResultGlobalPic;

/**
 * @author
 * @desc
 * @date
 */

public class Constans {

    public static String CURRENT_USER_NAME;
    public static String CURRENT_USER_AVATAR;

    public static String EXTRA_TYPE = "EXTRA_TYPE";
    public static String EXTRA_DATA = "EXTRA_DATA";
    public static String EXTRA_DATA2 = "EXTRA_DATA2";

    /**应用放在后台被强杀了*/
//    public static final int STATUS_FORCE_KILLED = -1;
    /**APP正常态//intent到MainActivity区分跳转目的*/
//    public static final int STATUS_NORMAL = 2;
    /**返回到主页面*/
//    public static final String KEY_HOME_ACTION = "key_home_action";
    /**默认值*/
//    public static final int ACTION_BACK_TO_HOME = 0;
    /**被强杀*/
//    public static final int ACTION_RESTART_APP = 1;

    /**文件路径管理类*/
    public static class FilePath {
        public static final String ROOT_PATH = "wxr/";
        public static final String RECORD_DIR = "record/";
        public static final String RECORD_PATH = ROOT_PATH + RECORD_DIR;
    }


    public static ResultGlobalPic globalPic;

//    public static boolean isStoreLogin = true;

    public static boolean isStoreLoginer = true;
}
