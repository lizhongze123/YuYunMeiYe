package cn.yuyun.yymy.ui.home.attendance;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.http.ApiException;
import com.example.http.ProgressSubscriber;

import java.util.ArrayList;
import java.util.List;

import cn.lzz.utils.DateTimeUtils;
import cn.lzz.utils.StringUtil;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.constan.Constans;
import cn.yuyun.yymy.event.NotifyEvent;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.result.ActionBean;
import cn.yuyun.yymy.http.result.ResultAttendanceWithTime;
import cn.yuyun.yymy.ui.home.train.TrainBean;
import cn.yuyun.yymy.ui.home.unboxing.FillContent;
import cn.yuyun.yymy.ui.other.ViewBigImageActivity;
import cn.yuyun.yymy.utils.EvenManager;
import cn.yuyun.yymy.utils.GlideHelper;
import cn.yuyun.yymy.view.WarnningDialog;
import cn.yuyun.yymy.view.bigimage.MediaBrowsePopupWindow;
import cn.yuyun.yymy.view.selectpic.SelectPicAdapter;
import cn.yuyun.yymy.view.selectpic.SelectPicView;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;
import static cn.yuyun.yymy.constan.Constans.EXTRA_TYPE;

/**
 * @author
 * @desc 考勤记录详情
 * @date
 */

public class SignRecordDetailActivity extends BaseActivity {

    private TextView tvTime, tvAddress, tvRemark;
    private ResultAttendanceWithTime.AppAttendanceExternalBean bean;
    private RecyclerView selectPicView;
    private List<String> list = new ArrayList<>();

    public static Intent startSignRecordDetailActivity(Context context, ResultAttendanceWithTime.AppAttendanceExternalBean bean) {
        return new Intent(context, SignRecordDetailActivity.class).putExtra(EXTRA_DATA, bean);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_record_detail);
    }

    @Override
    protected void setUpViewAndData() {
        initViews();
    }

    private void initViews() {
        bean = (ResultAttendanceWithTime.AppAttendanceExternalBean) getIntent().getSerializableExtra(EXTRA_DATA);
        titleBar.setTilte("外勤记录详情");
        if(null != bean.pictures){
            for (int i = 0; i < bean.pictures.size(); i++) {
                if(bean.pictures.get(i).startsWith(mContext.getString(R.string.HTTP))){
                    list.add(bean.pictures.get(i));
                }else{
                    list.add(mContext.getString(R.string.image_url_prefix) + bean.pictures.get(i));
                }
            }
        }
        selectPicView = (RecyclerView) findViewById(R.id.recyclerView);
        FillContent.fillNineImgList(list, this, selectPicView, true, true);
        tvTime = (TextView) findViewById(R.id.tv_time);
        tvAddress = (TextView) findViewById(R.id.tv_address);
        tvRemark = (TextView) findViewById(R.id.tv_remark);
        TextViewUtils.setTextOrEmpty(tvTime, DateTimeUtils.StringToDate(bean.create_time, DateTimeUtils.FORMAT_DATETIME_UI, DateTimeUtils.FORMAT_DATETIME_TWO));
        TextViewUtils.setTextOrEmpty(tvAddress, bean.place);
        TextViewUtils.setTextOrEmpty(tvRemark, bean.notes);
    }

}
