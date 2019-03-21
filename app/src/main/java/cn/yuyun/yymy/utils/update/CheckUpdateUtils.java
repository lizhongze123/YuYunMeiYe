package cn.yuyun.yymy.utils.update;

import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.example.http.ApiException;
import com.example.http.ProgressSubscriber;

import cn.lzz.utils.LogUtils;
import cn.lzz.utils.ToastUtils;
import cn.lzz.utils.VersionUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestCheckAppVersion;
import cn.yuyun.yymy.http.result.ResultVersion;
import cn.yuyun.yymy.view.dialog.VersionDialog;

/**
 * @author lzz
 * @desc lzz
 * @date 2018/4/11
 */
public class CheckUpdateUtils {

    private Context mContext;

    public CheckUpdateUtils(Context context) {
        mContext = context;
    }

    public void check() {

        final VersionDialog dialog = new VersionDialog(mContext);
        dialog.setOnPositiveListener(new VersionDialog.OnPositiveListener() {
            @Override
            public void onPositive() {
                if (!TextUtils.isEmpty("https://yuyunrj.oss-cn-shenzhen.aliyuncs.com/apk/yyrj_v2.1.1_2018-04-11.apk")) {
                    String apkUrl = "https://yuyunrj.oss-cn-shenzhen.aliyuncs.com/apk/yyrj_v2.1.1_2018-04-11.apk";
                    LogUtils.e(apkUrl);
                    FileDownloadManager manager = FileDownloadManager.getInstance(mContext);
                    long id = manager.startDownload(apkUrl, mContext.getString(R.string.app_name), "正在下载中", "");
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
                    sp.edit().putLong(DownloadManager.EXTRA_DOWNLOAD_ID, id).commit();
                }
                dialog.dismiss();
            }
        });
        dialog.show();



        /*AppModelFactory.model().checkAppVersion(VersionUtils.getAppVersionName(mContext), new ProgressSubscriber<ResultVersion>(mContext) {

            @Override
            public void onNext(final ResultVersion result) {
                if (null != result) {
                    final VersionDialog dialog = new VersionDialog(mContext);
                    dialog.setOnPositiveListener(new VersionDialog.OnPositiveListener() {
                        @Override
                        public void onPositive() {
                            if (!TextUtils.isEmpty(result.url)) {
                                String apkUrl = result.url;
                                LogUtils.e(apkUrl);
                                FileDownloadManager manager = FileDownloadManager.getInstance(mContext);
                                long id = manager.startDownload(apkUrl, mContext.getString(R.string.app_name), "正在下载中", result.app_name);
                                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
                                sp.edit().putLong(DownloadManager.EXTRA_DOWNLOAD_ID, id).commit();
                            }
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                    if (TextUtils.isEmpty(result.desc)) {
                        dialog.setTips("新版本V" + result.version);
                    } else {
                        dialog.setTips(result.desc);
                    }
                }
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
            }
        });*/
    }


}
