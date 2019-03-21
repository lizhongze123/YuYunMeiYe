package cn.yuyun.yymy.ui.me;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.http.ApiException;
import com.example.http.DataBean;
import com.example.http.ProgressSubscriber;

import java.util.ArrayList;
import java.util.List;

import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.event.NotifyEvent;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestDelEmail;
import cn.yuyun.yymy.http.result.ResultEmailList;
import cn.yuyun.yymy.utils.EvenManager;
import cn.yuyun.yymy.view.WarnningDialog;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;

/**
 * @author lzz
 * @desc 总裁信箱Activity
 * @date 2018/1/17
 */

public class EmailDetailActivity extends BaseActivity {

    private TextView tvContent, tvName, tvTime;
    private WarnningDialog warnningDialog;
    private ResultEmailList bean;

    public static Intent startEmailDetailActivity(Context context, ResultEmailList bean) {
        return new Intent(context, EmailDetailActivity.class).putExtra(EXTRA_DATA, bean);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_detail);
    }

    @Override
    protected void setUpViewAndData() {
        initViews();
    }

    private void initViews() {
        titleBar.setTilte("信箱详情");
        titleBar.setTvRight("删除");
        titleBar.setRightOnClicker(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                warnningDialog.show();
            }
        });
        tvContent = (TextView) findViewById(R.id.tv_content);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvTime = (TextView) findViewById(R.id.tv_time);
        bean = (ResultEmailList) getIntent().getSerializableExtra(EXTRA_DATA);
        if(null != bean){
            TextViewUtils.setTextOrEmpty(tvName, bean.createPersonName, "未知");
            TextViewUtils.setTextOrEmpty(tvContent, bean.content);
            TextViewUtils.setTextOrEmpty(tvTime, bean.createTime);
        }
        warnningDialog = new WarnningDialog(mContext, "确定删除？");
        warnningDialog.setOnPositiveListener(new WarnningDialog.OnDialogListener() {
            @Override
            public void onPositive() {
                del();
                warnningDialog.dismiss();
            }

            @Override
            public void onNegative() {

            }
        });
    }

    private void del() {
        RequestDelEmail body = new RequestDelEmail();
        body.status = -1;
        List<Integer> list = new ArrayList<>();
        list.add(bean.presidentMailboxId);
        body.presidentMailboxIdList = list;
        AppModelFactory.model().delEmail(body, new ProgressSubscriber<DataBean<Object>>(mContext) {
            @Override
            public void onCompleted() {
                super.onCompleted();
            }

            @Override
            public void onNext(DataBean<Object> result) {
                EvenManager.sendEvent(new NotifyEvent(NotifyEvent.RREFRESH_EMAIL));
                finish();
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
                showToast(ex.message);
            }
        });
    }
}
