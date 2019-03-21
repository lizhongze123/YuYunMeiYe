package cn.yuyun.yymy.utils.update;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.Settings;
import android.text.TextUtils;

import cn.lzz.utils.LogUtils;
import cn.lzz.utils.ToastUtils;
import cn.yuyun.yymy.app.MyApplication;

/**
 * @author lzz
 * @desc lzz
 * @date 2018/4/11
 */
public class FileDownloadManager {

    private DownloadManager downloadManager;
    private Context context;
    private static FileDownloadManager instance;

    private FileDownloadManager(Context context) {
        downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        this.context = context.getApplicationContext();
    }

    public static FileDownloadManager getInstance(Context context) {
        if (instance == null) {
            instance = new FileDownloadManager(context);
        }
        return instance;
    }

    private void check(){

    }


    public long startDownload(String uri, String title, String description, String appName) {

        final String packageName = "com.android.providers.downloads";
        int state = context.getPackageManager().getApplicationEnabledSetting(packageName); //检测下载管理器是否被禁用
        if (state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED) {
            LogUtils.e("关闭了下载管理器");
            AlertDialog.Builder builder = new AlertDialog.Builder(context).setTitle("温馨提示").setMessage("系统下载管理器被禁止，需手动打开").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    try {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.parse("package:" + packageName));
                        context.startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
                        context.startActivity(intent);
                    }
                }
            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
            return 0;
        }else{
            ToastUtils.toastInBottom(MyApplication.getInstance().getApplicationContext(), "开始下载更新...");
            DownloadManager.Request req = new DownloadManager.Request(Uri.parse(uri));
            req.setAllowedOverRoaming(true); //通知栏显示
//        req.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
            //req.setAllowedOverRoaming(false);
            req.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            //设置文件的保存的位置[三种方式]
            //第一种
            //file:///storage/emulated/0/Android/data/your-package/files/Download/update.apk
//        req.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, appName+".apk");
            //第二种
            //file:///storage/emulated/0/Download/update.apk
            if(!TextUtils.isEmpty(appName)){
                req.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, appName + ".apk");
            }else{
                req.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "update.apk");
            }

            //第三种 自定义文件路径
            //req.setDestinationUri()
            // 设置一些基本显示信息
            req.setTitle(title);
            req.setDescription(description);
            req.setMimeType("application/vnd.android.package-archive");
            return downloadManager.enqueue(req);//异步
            //dm.openDownloadedFile()
        }



    }


    public static void down(Context mContext, String apkurl){
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(apkurl));
        //设置在什么网络情况下进行下载
//        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        //设置通知栏标题
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setTitle("御韵美业");
        request.setDescription("apk正在下载");
        request.setAllowedOverRoaming(false);
        //设置文件存放目录
        request.setDestinationInExternalFilesDir(mContext, Environment.DIRECTORY_DOWNLOADS, "mydown");

        DownloadManager downManager = (DownloadManager)mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        long id = downManager.enqueue(request);
    }

    /**
     * 获取文件保存的路径
     *
     * @param downloadId an ID for the download, unique across the system.
     *                   This ID is used to make future calls related to this download.
     * @return file path
     * @see FileDownloadManager#getDownloadUri(long)
     */
    public String getDownloadPath(long downloadId) {
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(downloadId);
        Cursor c = downloadManager.query(query);
        if (c != null) {
            try {
                if (c.moveToFirst()) {
                    return c.getString(c.getColumnIndexOrThrow(DownloadManager.COLUMN_LOCAL_URI));
                }
            } finally {
                c.close();
            }
        }
        return null;
    }

    /**
     * 获取保存文件的地址
     *
     * @param downloadId an ID for the download, unique across the system.
     *                   This ID is used to make future calls related to this download.
     * @see FileDownloadManager#getDownloadPath(long)
     */
    public Uri getDownloadUri(long downloadId) {
        return downloadManager.getUriForDownloadedFile(downloadId);
    }

    public DownloadManager getDownloadManager() {
        return downloadManager;
    }




}
