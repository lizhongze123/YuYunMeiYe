package cn.yuyun.yymy.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import cn.jpush.android.api.JPushInterface;
import cn.lzz.utils.LogUtils;
import cn.yuyun.yymy.main.LoginActivity;
import cn.yuyun.yymy.main.MainActivity;
import cn.yuyun.yymy.ui.me.SetActivity;

/**
 * @author
 * @desc
 * @date
 */
public class MyReceive extends BroadcastReceiver{


    @Override
    public void onReceive(Context context, Intent intent) {

        LogUtils.e("//////////////////////////************/////////////////");

        Bundle bundle = intent.getExtras();

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {

            //官网提供根据Registration ID 进行推送 此方法用于处理该类推送消息

        }else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {

            // 自定义消息不会展示在通知栏，完全要开发者写代码去处理
            // 处理自定义消息

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {

            //接收到通知会走的方法

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {

            //用户点击通知会走的方法

            //获取推送消息的方法
            String content = bundle.getString(JPushInterface.EXTRA_ALERT);

            // 在这里可以自己写代码去定义用户点击后的行为
            if(context != null){

                //例如 如果推送内容以【消息】开头 则点击后跳转到消息的Activity 否则跳转到主页面
                if(content.startsWith("【消息】")){//判断内容的条件

                    Intent i = new Intent(context, SetActivity.class);  //打开消息界面
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);

                }else{

                    Intent i = new Intent(context, SetActivity.class); //打开主界面
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                }

            }else{
                Log.d("-------","null");
            }
        } else {
            //...
        }
    }
}
