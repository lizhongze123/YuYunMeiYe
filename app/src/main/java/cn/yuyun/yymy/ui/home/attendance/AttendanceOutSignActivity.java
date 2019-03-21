package cn.yuyun.yymy.ui.home.attendance;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.http.ApiException;
import com.example.http.DataBean;
import com.example.http.ProgressSubscriber;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import cn.example.takephoto.CustomHelper;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestOutSign;
import cn.yuyun.yymy.http.request.RequestUploadPic;
import cn.yuyun.yymy.sp.UserfoPrefs;
import cn.yuyun.yymy.ui.other.ViewBigImageActivity;
import cn.yuyun.yymy.view.selectpic.SelectPicAdapter;
import cn.yuyun.yymy.view.selectpic.SelectPicView;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;

/**
 * @author
 * @desc
 * @date
 */

public class AttendanceOutSignActivity extends BaseActivity {

    @BindView(R.id.et_remark)
    EditText etRemark;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.selectPicView)
    SelectPicView selectPicView;
    private List<String> list = new ArrayList<>();
    private CustomHelper customHelper;
    RequestOutSign requestOutSign;

    public static Intent startAttendanceOutSignActivity(Context context, RequestOutSign bean) {
        return new Intent(context, AttendanceOutSignActivity.class).putExtra(EXTRA_DATA, bean);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_outsign);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
        customHelper = new CustomHelper(this).setCrop(false).setLimit(9);
    }

    private void initViews() {
        refreshThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    getTimeDegree();
                    SystemClock.sleep((long) refresh_time);
                }
            }
        });
        refreshThread.start();
        requestOutSign = (RequestOutSign) getIntent().getSerializableExtra(EXTRA_DATA);
        titleBar.setTilte("外勤打卡");
        TextViewUtils.setTextOrEmpty(tvLocation, requestOutSign.place);
        selectPicView.setMax(9);
        selectPicView.setAdapter(new SelectPicAdapter.OnClickCallBack() {
            @Override
            public void onAdd() {
                customHelper.onClick(getTakePhoto());
            }

            @Override
            public void onDel(int pos) {
                list.remove(pos);
                selectPicView.notifyDataSetChanged(list);
            }

            @Override
            public void onBrowser(int pos) {
                Bundle bundle = new Bundle();
                bundle.putInt("code", pos);
                bundle.putBoolean("isShow", true);
                bundle.putStringArrayList("imageList", (ArrayList<String>) list);
                startActivity(ViewBigImageActivity.startViewBigImageActivity(mContext, bundle));
            }
        });
        selectPicView.notifyDataSetChanged(list);
        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.size() > 0) {
                    uploadPic();
                } else {
                    outSign(null);
                }
            }
        });
    }

    @Override
    public void takeCancel() {
        super.takeCancel();
    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        showImg(result.getImages());
    }

    private void showImg(ArrayList<TImage> images) {
        for (int i = 0; i < images.size(); i++) {
            if (list.size() < 9) {
                list.add(images.get(i).getCompressPath());
            }
        }
        selectPicView.notifyDataSetChanged(list);
    }

    private void outSign(List<String> pictures) {
        requestOutSign.pictures = pictures;
        requestOutSign.baseon_id = UserfoPrefs.getInstance(mContext).getOgId();
        requestOutSign.baseon_type = UserfoPrefs.getInstance(mContext).getOgType();
        if (!TextUtils.isEmpty(etRemark.getText().toString())) {
            requestOutSign.notes = etRemark.getText().toString();
        }
        AppModelFactory.model().outsideSign(requestOutSign, new ProgressSubscriber<DataBean>(mContext) {

            @Override
            public void onCompleted() {
                super.onCompleted();
            }

            @Override
            public void onNext(DataBean result) {
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onNormalError(ApiException ex) {
                super.onNormalError(ex);
                showToast(ex.message);
            }

            @Override
            public void onNoNetWorkError() {
                super.onNoNetWorkError();
                showToast(mContext.getString(R.string.error_no_network));
            }

        });
    }

    private void uploadPic() {
        RequestUploadPic requestUploadPic = new RequestUploadPic();
        requestUploadPic.type = RequestUploadPic.UploadPicType.OUTSIGN;
        List<File> fileList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            File file = new File(list.get(i));
            fileList.add(file);
        }
        requestUploadPic.file = fileList;
        AppModelFactory.model().uploadPic(requestUploadPic, new ProgressSubscriber<DataBean<List<String>>>(mContext) {
            @Override
            public void onNext(DataBean<List<String>> o) {
                if (null != o.data) {
                    outSign(o.data);
                }
            }

            @Override
            public void onNormalError(ApiException ex) {
                super.onNormalError(ex);
                showToast("请求失败，请稍后重试");
            }

            @Override
            public void onNoNetWorkError() {
                super.onNoNetWorkError();
                showToast("网络异常，请检查网络");
            }
        });
    }

    String minute;
    String hour;
    boolean isFlash;

    /**
     * 刷新时间线程
     */
    private Thread refreshThread;
    /**
     * 秒针刷新的时间
     */
    private float refresh_time = 1000;

    private void getTimeDegree() {
        Calendar calendar = Calendar.getInstance();
        minute = String.valueOf(calendar.get(Calendar.MINUTE));
        hour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
        if (minute.length() != 2) {
            minute = "0" + minute;
        }
        if (hour.length() != 2) {
            hour = "0" + hour;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isFlash) {
                    tvSubmit.setText(hour + " " + minute + " 外勤打卡");
                } else {
                    tvSubmit.setText(hour + ":" + minute + " 外勤打卡");
                }
                isFlash = !isFlash;
            }
        });
    }
}
