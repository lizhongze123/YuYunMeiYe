package cn.yuyun.yymy.ui.home.work;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.http.ApiException;
import com.example.http.DataBean;
import com.example.http.ProgressSubscriber;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.example.takephoto.CustomHelper;
import cn.lzz.utils.DateTimeUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.event.NotifyEvent;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestFilterWork;
import cn.yuyun.yymy.http.request.RequestSubmitWork;
import cn.yuyun.yymy.http.request.RequestUploadPic;
import cn.yuyun.yymy.http.result.ResultListStaff;
import cn.yuyun.yymy.sp.ApprovePrefs;
import cn.yuyun.yymy.sp.LoginInfoPrefs;
import cn.yuyun.yymy.sp.UserfoPrefs;
import cn.yuyun.yymy.ui.home.leave.SelectStoreListActivity;
import cn.yuyun.yymy.ui.other.ViewBigImageActivity;
import cn.yuyun.yymy.utils.EvenManager;
import cn.yuyun.yymy.view.selectpic.SelectPicAdapter;

/**
 * @author
 * @desc
 * @date
 */

public class FilterWorkActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.rb_day)
    RadioButton rbDay;
    @BindView(R.id.rb_week)
    RadioButton rbWeek;
    @BindView(R.id.rb_month)
    RadioButton rbMonth;
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;
    @BindView(R.id.selectPeopleView)
    SelectPeopleView selectPeopleView;
    @BindView(R.id.tv_start)
    TextView tvStart;
    @BindView(R.id.tv_end)
    TextView tvEnd;

    RequestFilterWork body = new RequestFilterWork();

    private List<ResultListStaff> intentList = new ArrayList<>();

    private List<SelectBean> dataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_work);
        EvenManager.register(this);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
    }

    private void initViews() {
        titleBar.setTilte("查看汇报");
        tvStart.setText(DateTimeUtils.getNowTime());
        tvEnd.setText(DateTimeUtils.getNowTime());
        start = DateTimeUtils.getNowTimeStamp();
        end = DateTimeUtils.getNowTimeStamp();
        radioGroup.setOnCheckedChangeListener(this);
        selectPeopleView.setAdapter(new SelectPeopleAdapter.OnClickCallBack() {
            @Override
            public void onAdd() {
                startActivity(SelectStoreListActivity.startTypeSelectStaff(mContext, intentList));
            }

            @Override
            public void onDel(int pos) {
                intentList.remove(pos);
                dataList.remove(pos);
                selectPeopleView.notifyDataSetChanged(dataList);

            }
        });
        selectPeopleView.setMax(Integer.MAX_VALUE);
        for (int i = 0; i < intentList.size(); i++) {
            SelectBean bean = new SelectBean();
            if (!TextUtils.isEmpty(intentList.get(i).avatar)) {
                if (intentList.get(i).avatar.startsWith(getString(R.string.HTTP))) {
                    bean.pic_url = intentList.get(i).avatar;
                } else {
                    bean.pic_url = getString(R.string.image_url_prefix) + intentList.get(i).avatar;
                }
            }
            if (null != intentList.get(i).sex) {
                bean.type = intentList.get(i).sex.value;
            }
            bean.name = intentList.get(i).staff_name;
            dataList.add(bean);
        }
        selectPeopleView.addAll(dataList);

        timePickerView = new TimePickerBuilder(mContext, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                if (timeType == 0) {
                    tvStart.setText(DateTimeUtils.getDateTimeText(date, DateTimeUtils.FORMAT_DATE_UI));
                    start = date.getTime() / 1000;
                } else {
                    tvEnd.setText(DateTimeUtils.getDateTimeText(date, DateTimeUtils.FORMAT_DATE_UI));
                    end = date.getTime() / 1000;
                }
            }
        }).build();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNotifyEvent(NotifyEvent notifyEvent) {
        if (notifyEvent.tag == NotifyEvent.DATA) {
            intentList = (List<ResultListStaff>) notifyEvent.value;
            dataList.clear();
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
            }
            selectPeopleView.notifyDataSetChanged(dataList);
        }
    }

    private long start;
    private long end;

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        switch (checkedId) {
            case R.id.rb_day:
                body.type = 1;
                break;
            case R.id.rb_week:
                body.type = 2;
                break;
            case R.id.rb_month:
                body.type = 3;
                break;
            default:
        }
    }

    private int timeType = 0;
    private TimePickerView timePickerView;

    @OnClick(R.id.ll_start)
    public void start(View v) {
        timeType = 0;
        showDateTimePickerDialog();
    }

    @OnClick(R.id.ll_end)
    public void end(View v) {
        timeType = 1;
        showDateTimePickerDialog();
    }

    private void showDateTimePickerDialog() {
        timePickerView.show();
    }

    @OnClick(R.id.tv_agree)
    public void agree(View v) {
        if(end - start < 0){
            showTextDialog("结束时间不能小于开始时间", false);
            return;
        }
        body.startTime = tvStart.getText().toString();
        body.endTime = tvEnd.getText().toString();
        //选择的审批人
        List<String> personList = new ArrayList<>();
        for (int i = 0; i < intentList.size(); i++) {
            personList.add(intentList.get(i).staff_id);
        }
        body.personId = personList;
        startActivity(ResultWorkActivity.startResultWorkActivity(mContext, body));
    }

    @OnClick(R.id.tv_refuse)
    public void refuse(View v) {
        finish();
    }

}
