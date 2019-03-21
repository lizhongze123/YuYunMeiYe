package cn.yuyun.yymy.app;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.stat.StatConfig;
import com.tencent.stat.StatService;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import cn.jpush.android.api.JPushInterface;
import cn.yuyun.yymy.BuildConfig;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.utils.CommonUtils;

import static cn.lzz.utils.LogUtils.isDebug;

/**
 * @desc
 * @author
 * @date
 */
public class MyApplication extends Application{

    private static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder().tag("lzz").build();
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));
        if(BuildConfig.LOG_DEBUG){
            isDebug = true;
        }else{
            isDebug = false;
        }
        AppModelFactory.singleton = new AppModelFactory(this);
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        instance = this;
        Context context = getApplicationContext();
        // 获取当前包名
        String packageName = context.getPackageName();
        // 获取当前进程名
        String processName = CommonUtils.getProcessName(android.os.Process.myPid());
        // 设置是否为上报进程
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
        strategy.setUploadProcess(processName == null || processName.equals(packageName));
        // 初始化Bugly
//        CrashReport.initCrashReport(context, "806bcdf5fa", isDebug, strategy);
        // 打开Logcat输出，上线时，一定要关闭
        StatConfig.setDebugEnable(false);
        // 注册activity生命周期，统计时长
//        StatService.registerActivityLifecycleCallbacks(this);
        fix();
    }

    /**处理oppo手机timeout问题*/
    public static void fix() {
        try {
            Class clazz = Class.forName("java.lang.Daemons$FinalizerWatchdogDaemon");
            Method method = clazz.getSuperclass().getDeclaredMethod("stop");
            method.setAccessible(true);
            Field field = clazz.getDeclaredField("INSTANCE");
            field.setAccessible(true);
            method.invoke(field.get(null));
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * 使其系统更改字体大小无效
     */
    private void initTextSize() {
        Resources res = getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
    }

    /**
     * 单例获取application实例
     * @return MyApplication
     */
    public static MyApplication getInstance() {
        return instance;
    }

}
