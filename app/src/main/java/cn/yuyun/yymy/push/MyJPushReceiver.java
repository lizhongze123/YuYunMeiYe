package cn.yuyun.yymy.push;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;
import cn.lzz.utils.DeviceUtils;
import cn.lzz.utils.LogUtils;
import cn.yuyun.yymy.main.TransitionActivity;
import cn.yuyun.yymy.sp.LoginInfoPrefs;
import cn.yuyun.yymy.sp.UserfoPrefs;
import cn.yuyun.yymy.ui.home.appointment.AppointmentActivity;
import cn.yuyun.yymy.ui.home.train.TrainListActivity;
import cn.yuyun.yymy.ui.home.work.MomentsDetailActivity;

/**
 * @author
 * @desc
 * @date
 */

public class MyJPushReceiver extends BroadcastReceiver {

    private static final String TAG = "MyJPushReceiver";
    private NotificationManager nm;
    /**
     * 预约
     */
    private final String BOOK = "book";
    /**
     * 通知公告
     */
    private final String ANNOUN = "announ";
    /**
     * 请假申请
     */
    private final String LEAVE = "leave";
    /**
     * 首页
     */
    private final String HOME = "home";
    /**
     * 工作汇报
     */
    private final String WORK = "work";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (null == nm) {
            nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        Bundle bundle = intent.getExtras();
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            LogUtils.d(TAG, "JPush用户注册成功");
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            LogUtils.d(TAG, "接收到推送下来的自定义消息");
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            LogUtils.d(TAG, "接收到推送下来的通知");
            receivingNotification(context, bundle);
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            LogUtils.d(TAG, "用户点击打开了通知");
            openNotification(context, bundle);
        } else {
            LogUtils.d(TAG, "Unhandled intent - " + intent.getAction());
        }
    }

    private void receivingNotification(Context context, Bundle bundle) {
        String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
        LogUtils.d(TAG, " title : " + title);
        String message = bundle.getString(JPushInterface.EXTRA_ALERT);
        LogUtils.d(TAG, "message : " + message);
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        LogUtils.d(TAG, "extras : " + extras);
        String myValue = "";
        try {
            JSONObject extrasJson = new JSONObject(extras);
            myValue = extrasJson.optString("type");
            if (myValue.equals(BOOK)) {
                LogUtils.d(TAG, "BOOK : " + extras);
            } else if (myValue.equals(ANNOUN)) {
                LogUtils.d(TAG, "ANNOUN : " + extras);
            } else if (myValue.equals(LEAVE)) {
                LogUtils.d(TAG, "LEAVE : " + extras);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openNotification(Context context, Bundle bundle) {
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        String myValue = "";
        int id ;
        try {
            JSONObject extrasJson = new JSONObject(extras);
            myValue = extrasJson.optString("type");
            id = extrasJson.optInt("id");
            if (DeviceUtils.isAppInForeground(context)) {
               /* if (myValue.equals(BOOK)) {
                    LogUtils.d(TAG, "BOOK : " + extras);
                    Intent mIntent = new Intent(context, AppointmentActivity.class);
                    mIntent.setAction(Intent.ACTION_MAIN);
                    mIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                    //关键的一步，设置启动模式
                    mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                    mIntent.putExtras(bundle);
                    context.startActivity(mIntent);
                } else if (myValue.equals(ANNOUN)) {
                    LogUtils.d(TAG, "ANNOUN : " + extras);
                    Intent mIntent = new Intent(context, TrainListActivity.class);
                    mIntent.putExtras(bundle);
                    context.startActivity(mIntent);
                } else if (myValue.equals(LEAVE)) {

                }else if(myValue.equals(WORK)){
                    context.startActivity(MomentsDetailActivity.startFromNotice(context, id, UserfoPrefs.getInstance(context).getBaseonId()));
                }*/
            } else {
                Log.e(TAG, "app--unalive");
                Intent mIntent = new Intent(context, TransitionActivity.class);
                mIntent.setAction(Intent.ACTION_VIEW);
                mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                mIntent.putExtra(EXTRA_DATA, "CUSTOM");
                context.startActivity(mIntent);
            }

        } catch (Exception e) {
            Logger.w(TAG, "Unexpected: extras is not a valid json", e);
            return;
        }

    }

}
