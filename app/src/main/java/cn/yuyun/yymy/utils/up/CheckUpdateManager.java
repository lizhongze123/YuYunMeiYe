package cn.yuyun.yymy.utils.up;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;

import com.example.http.ApiException;
import com.example.http.DataBean;
import com.example.http.ProgressSubscriber;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;

import java.util.List;

import cn.lzz.utils.LogUtils;
import cn.lzz.utils.ToastUtils;
import cn.lzz.utils.VersionUtils;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.result.ResultVersion;
import cn.yuyun.yymy.view.dialog.VersionDialog;


/**版本更新*/
public class CheckUpdateManager {

    private Context mContext;

    public CheckUpdateManager(Context context) {
        this.mContext = context;

    }

    public void checkUpdate() {
        AppModelFactory.model().checkAppVersion(1, VersionUtils.getAppVersionName(mContext), new ProgressSubscriber<DataBean<ResultVersion>>(mContext) {

            @Override
            public void onNext(final DataBean<ResultVersion> result) {

                if (null != result.data) {
                    final VersionDialog dialog = new VersionDialog(mContext);
                    dialog.setOnPositiveListener(new VersionDialog.OnPositiveListener() {
                        @Override
                        public void onPositive() {
                            if (!TextUtils.isEmpty(result.data.url)) {
                                String apkUrl = result.data.url;
                                requestExternalStorage(apkUrl);
                            }
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                    if (!TextUtils.isEmpty(result.data.describes)) {
                        dialog.setTips(result.data.describes);
                    }
                    if (!TextUtils.isEmpty(result.data.version)) {
                        dialog.setCode(result.data.describes);
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
        });
    }

    public void requestExternalStorage(final String apkUrl) {
        if (hasPermissions(mContext, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            DownloadService.startService(mContext, apkUrl);
        } else {
            AndPermission.with(mContext)
                    .permission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    .onGranted(new Action() {
                        @Override
                        public void onAction(List<String> permissions) {
                            DownloadService.startService(mContext, apkUrl);
                        }
                    })
                    .onDenied(new Action() {
                        @Override
                        public void onAction(@NonNull List<String> permissions) {
                            LogUtils.e("onDenied");
                            if (AndPermission.hasAlwaysDeniedPermission(mContext, permissions)) {
                                ToastUtils.toastInCenter(mContext, "权限被禁用,可能会导致功能失效");
                            }
                        }
                    })
                    .start();
        }

    }


    public static boolean hasPermissions(@NonNull Context context, @NonNull String... perms) {
        // Always return true for SDK < M, let the system deal with the permissions
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            Log.w("lzz", "hasPermissions: API version < M, returning true by default");

            // DANGER ZONE!!! Changing this will break the library.
            return true;
        }

        for (String perm : perms) {
            boolean hasPerm = (ContextCompat.checkSelfPermission(context, perm) ==
                    PackageManager.PERMISSION_GRANTED);
            if (!hasPerm) {
                return false;
            }
        }

        return true;
    }
}
