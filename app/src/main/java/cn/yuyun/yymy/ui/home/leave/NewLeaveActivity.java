package cn.yuyun.yymy.ui.home.leave;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.http.ApiException;
import com.example.http.DataBean;
import com.example.http.ProgressSubscriber;
import com.google.gson.Gson;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.example.takephoto.CustomHelper;
import cn.lzz.utils.DateTimeUtils;
import cn.lzz.utils.DeviceUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.bean.LeaveReason;
import cn.yuyun.yymy.event.NotifyEvent;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestLeave;
import cn.yuyun.yymy.http.request.RequestUploadPic;
import cn.yuyun.yymy.http.result.ResultListStaff;
import cn.yuyun.yymy.sp.UserfoPrefs;
import cn.yuyun.yymy.ui.home.work.SelectBean;
import cn.yuyun.yymy.ui.home.work.SelectPeopleAdapter;
import cn.yuyun.yymy.ui.home.work.SelectPeopleView;
import cn.yuyun.yymy.ui.other.ViewBigImageActivity;
import cn.yuyun.yymy.utils.EvenManager;
import cn.yuyun.yymy.view.selectpic.SelectPicAdapter;
import cn.yuyun.yymy.view.selectpic.SelectPicView;

/**
 * @author
 * @desc
 * @date
 */
public class NewLeaveActivity extends BaseActivity {

    @BindView(R.id.tv_reason)
    TextView tvReason;
    @BindView(R.id.tv_timeStart)
    TextView tvTimeStart;
    @BindView(R.id.tv_timeEnd)
    TextView tvTimeEnd;
    @BindView(R.id.tv_timesLong)
    TextView tvTimesLong;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.selectPicView)
    SelectPicView selectPicView;
    @BindView(R.id.recyclerView)
    SelectPeopleView selectPeopleView;
    /**
     * 请假事由选择器
     */
    private OptionsPickerView<LeaveReason> levelPickerView;

    private int timeType = 0;
    private RequestLeave requestBean;
    private UserfoPrefs userfoPrefs;

    private CustomHelper customHelper;

    private TimePickerView timePickerView;
    /**
     * 审批人的头像
     */
    private List<SelectBean> dataList = new ArrayList<>();
    /**
     * 选择的审批人
     */
    private List<RequestLeave.ApprovePeopleBean> personList = new ArrayList<>();

    private List<String> picList = new ArrayList<>();

    private OptionsPickerView<String> timesLongPickerView;

    private List<ResultListStaff> intentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_leave);
        EvenManager.register(this);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
        initData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNotifyEvent(NotifyEvent notifyEvent) {
        if (notifyEvent.tag == NotifyEvent.DATA) {
            intentList = (List<ResultListStaff>) notifyEvent.value;
            dataList.clear();
            personList.clear();
            for (int i = 0; i < intentList.size(); i++) {
                SelectBean selectBean = new SelectBean();
                ResultListStaff bean = intentList.get(i);
                if (!TextUtils.isEmpty(intentList.get(i).avatar)) {
                    if (bean.avatar.startsWith(getString(R.string.HTTP))) {
                        selectBean.pic_url = intentList.get(i).avatar;
                    } else {
                        selectBean.pic_url = getString(R.string.image_url_prefix) + intentList.get(i).avatar;
                    }
                }
                if (null != bean.sex) {
                    selectBean.type = bean.sex.value;
                }
                selectBean.name = bean.staff_name;
                dataList.add(selectBean);
                RequestLeave.ApprovePeopleBean a = new RequestLeave.ApprovePeopleBean();
                a.approvePerson = bean.staff_id;
                personList.add(a);
            }
            selectPeopleView.notifyDataSetChanged(dataList);
        }
    }

    protected void initViews() {
        titleBar.setTilte("请假申请");
        titleBar.setTvRight("提交");
        titleBar.setRightOnClicker(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();
            }
        });
        customHelper = new CustomHelper(this).setCrop(false).setLimit(9);
        initPickerView();
        selectPeopleView.setAdapter(new SelectPeopleAdapter.OnClickCallBack() {
            @Override
            public void onAdd() {
                startActivity(SelectStoreListActivity.startTypeSelectStaff(mContext, intentList));
            }

            @Override
            public void onDel(int pos) {
                dataList.remove(pos);
                intentList.remove(pos);
                selectPeopleView.notifyDataSetChanged(dataList);
                personList.remove(pos);
            }
        });
        selectPicView.setMax(9);
        selectPicView.isDel(true);
        selectPicView.setAdapter(new SelectPicAdapter.OnClickCallBack() {
            @Override
            public void onAdd() {
                customHelper.onClick(getTakePhoto());
            }

            @Override
            public void onDel(int pos) {
                picList.remove(pos);
                selectPicView.notifyDataSetChanged(picList);
            }

            @Override
            public void onBrowser(int pos) {
                Bundle bundle = new Bundle();
                bundle.putInt("code", pos);
                bundle.putBoolean("isShow", false);
                bundle.putStringArrayList("imageList", (ArrayList<String>) picList);
                startActivity(ViewBigImageActivity.startViewBigImageActivity(mContext, bundle));
            }
        });
    }

    private void initData() {
        requestBean = new RequestLeave();
        userfoPrefs = UserfoPrefs.getInstance(mContext);
    }

  /*  private void initDialog() {
        dialog = new BottomSelectDialogStaff(this, R.layout.dialog_bottom_select_appointment);
        dialog.setItemClickListener(new BottomSelectDialogStaff.ItemClickListener() {

            @Override
            public void oClick(int item) {
                switch (item) {
                    case 0:
                        dialog.dismiss();
                        break;
                    case 1:
                        startActivity(new Intent(NewLeaveActivity.this, MyReviewedFragment.class));
                        dialog.dismiss();
                        break;
                    case 2:
                        startActivity(MyApplyFragment.startMyApplyActivityFromMysellf(mContext, null));
                        dialog.dismiss();
                        break;
                    default:
                }
            }
        });
    }*/


    private void initPickerView() {
        final ArrayList<LeaveReason> mReasonDatas = new ArrayList<>();
        for (int i = 0; i < LeaveReason.values().length; i++) {
            mReasonDatas.add(LeaveReason.values()[i]);
        }

        levelPickerView = new OptionsPickerBuilder(mContext, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                tvReason.setText(mReasonDatas.get(options1).desc);
                requestBean.reason = mReasonDatas.get(options1);
            }
        }).build();
        levelPickerView.setPicker(mReasonDatas);

        timePickerView = new TimePickerBuilder(mContext, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                if (timeType == 0) {
                    requestBean.startSpan = 1;
                    tvTimeStart.setText(DateTimeUtils.getDateTimeText(date, DateTimeUtils.FORMAT_DATETIME_TWO));
                    requestBean.startTime = DateTimeUtils.parseToStamp(tvTimeStart.getText().toString(), DateTimeUtils.FORMAT_DATETIME_TWO);
                } else {
                    requestBean.endSpan = 2;
                    tvTimeEnd.setText(DateTimeUtils.getDateTimeText(date, DateTimeUtils.FORMAT_DATETIME_TWO));
                    requestBean.endTime = DateTimeUtils.parseToStamp(tvTimeEnd.getText().toString(), DateTimeUtils.FORMAT_DATETIME_TWO);
                }

            }
        })
                // 默认全部显示
                .setType(new boolean[]{true, true, true, true, true, false})
                .build();

        final ArrayList<String> options1Items = new ArrayList<>();
        final ArrayList<String> options1Items2 = new ArrayList<>();
        options1Items2.add("0.0");
        options1Items2.add("0.5");
        for (int i = 0; i < 31; i++) {
            options1Items.add(i + "");
        }
        timesLongPickerView = new OptionsPickerBuilder(mContext, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                if(options1 == 0 && options2 == 0){
                    showToast("请假天数不能为0天,请重新选择");
                    return;
                }
                tvTimesLong.setText((Double.valueOf(options1Items.get(options1)) + Double.valueOf(options1Items2.get(options2))) + "");
                requestBean.timesLong = tvTimesLong.getText().toString();
            }
        }).build();

        timesLongPickerView.setNPicker(options1Items, options1Items2, null);
        timesLongPickerView.setSelectOptions(0, 1);
        levelPickerView.setPicker(mReasonDatas);
    }

    private void showReasonPickerDialog() {
        levelPickerView.show();
    }

    private void showDateTimePickerDialog() {
        timePickerView.show();
    }

    @OnClick({R.id.tv_reason, R.id.tv_timeStart, R.id.tv_timeEnd, R.id.tv_timesLong})
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.tv_reason) {
            showReasonPickerDialog();
        } else if (viewId == R.id.tv_timeStart) {
            timeType = 0;
            showDateTimePickerDialog();
        } else if (viewId == R.id.tv_timeEnd) {
            timeType = 1;
            showDateTimePickerDialog();
        } else if(viewId == R.id.tv_timesLong){
            if (null != timesLongPickerView) {
                DeviceUtils.hideSoftKeyboard(view, mContext);
                timesLongPickerView.show();
            }
        }
    }

    private void check(){
        if (TextUtils.isEmpty(tvReason.getText().toString())) {
            showToast("请选择请假类型");
            return;
        } else if (TextUtils.isEmpty(tvTimeStart.getText().toString())) {
            showToast("请选择开始时间");
            return;
        } else if (TextUtils.isEmpty(tvTimeEnd.getText().toString())) {
            showToast("请选择结束时间");
            return;
        } else if (TextUtils.isEmpty(tvTimesLong.getText().toString())) {
            showToast("请选择请假时长");
            return;
        } else if (TextUtils.isEmpty(etContent.getText().toString())) {
            showToast("请输入请假说明");
            return;
        } else if (personList.size() == 0) {
            showToast("请选择审批人");
            return;
        }else if(requestBean.endTime - requestBean.startTime < 0){
            showTextDialog("结束时间不能小于开始时间", false);
            return;
        }

        //如果有图片先上传图片
        if (picList.size() > 0) {
            submitPic();
        } else {
            submit(null);
        }
    }

    private void submit(List<String> pictures) {
        requestBean.content = etContent.getText().toString();
        requestBean.timesLong = tvTimesLong.getText().toString();
        requestBean.approvePeopleBeanList = new Gson().toJson(personList);
        if(null != pictures){
            requestBean.imgUrls = pictures;
        }
        AppModelFactory.model().submitLeaveApply(requestBean, new ProgressSubscriber<DataBean>(mContext) {
            @Override
            public void onNext(DataBean result) {
                EvenManager.sendEvent(new NotifyEvent(NotifyEvent.RREFRESH));
                showToast("已提交申请");
                finish();
            }

            @Override
            public void onNormalError(ApiException ex) {
                super.onNormalError(ex);
                showToast("请求失败，请稍后重试");
            }

            @Override
            public void onNoNetWorkError() {
                super.onNoNetWorkError();
                showToast(mContext.getString(R.string.error_no_network));
            }
        });

    }

    private void submitPic() {
        RequestUploadPic requestUploadPic = new RequestUploadPic();
        requestUploadPic.type = RequestUploadPic.UploadPicType.LEAVE;
        List<File> fileList = new ArrayList<>();
        for (int i = 0; i < picList.size(); i++) {
            File file = new File(picList.get(i));
            fileList.add(file);
        }
        requestUploadPic.file = fileList;
        AppModelFactory.model().uploadPic(requestUploadPic, new ProgressSubscriber<DataBean<List<String>>>(mContext) {
            @Override
            public void onNext(DataBean<List<String>> o) {
                if (null != o.data) {
                    submit(o.data);
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
                showToast(mContext.getString(R.string.error_no_network));
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
            if(picList.size() < 9){
                picList.add(images.get(i).getCompressPath());
            }
        }
        selectPicView.notifyDataSetChanged(picList);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EvenManager.unregister(this);
    }

}
