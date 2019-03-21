package cn.yuyun.yymy.ui.me;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.http.ApiException;
import com.example.http.ProgressSubscriber;

import java.util.Date;

import butterknife.BindView;
import cn.lzz.utils.DateTimeUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.event.NotifyEvent;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestAddCallback;
import cn.yuyun.yymy.http.request.RequestAddCommunication;
import cn.yuyun.yymy.http.request.RequestEmail;
import cn.yuyun.yymy.http.request.RequestFeedback;
import cn.yuyun.yymy.http.result.ResultMemberBean;
import cn.yuyun.yymy.sp.LoginInfoPrefs;
import cn.yuyun.yymy.sp.UserfoPrefs;
import cn.yuyun.yymy.utils.EvenManager;
import cn.yuyun.yymy.utils.InputLengthFilter;
import cn.yuyun.yymy.view.TitleBar;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;
import static cn.yuyun.yymy.constan.Constans.EXTRA_TYPE;

/**
 * @author lzz
 * @desc 总裁信箱Activity
 * @date 2018/1/17
 */

public class EmailActivity extends BaseActivity{

    @BindView(R.id.et_content)
    EditText etContent;
    public int type;
    public static final int COMMUNICATION = 1;
    public static final int CALLBACK = 2;
    public static final int FEEDBACK = 3;
    private String memberId;
    private ResultMemberBean memberBean;

    public static Intent startFromCommunication(Context context, ResultMemberBean resultMemberBean) {
        return new Intent(context, EmailActivity.class).putExtra(EXTRA_DATA, resultMemberBean).putExtra(EXTRA_TYPE, COMMUNICATION);
    }

    public static Intent startFromCallback(Context context, ResultMemberBean resultMemberBean) {
        return new Intent(context, EmailActivity.class).putExtra(EXTRA_DATA, resultMemberBean).putExtra(EXTRA_TYPE, CALLBACK);
    }

    public static Intent startFromFeedBack(Context context, String memberId) {
        return new Intent(context, EmailActivity.class).putExtra(EXTRA_DATA, memberId).putExtra(EXTRA_TYPE, FEEDBACK);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
    }

    private void initViews() {
        type = getIntent().getIntExtra(EXTRA_TYPE, 0);
        if(type == COMMUNICATION) {
            titleBar.setTilte("添加记录");
            titleBar.setTvRight("完成");
            memberBean = (ResultMemberBean) getIntent().getSerializableExtra(EXTRA_DATA);
        }else if(type == CALLBACK){
            titleBar.setTilte("添加记录");
            titleBar.setTvRight("完成");
            memberBean = (ResultMemberBean) getIntent().getSerializableExtra(EXTRA_DATA);
        }else if(type == FEEDBACK){
            titleBar.setTilte("添加心得");
            titleBar.setTvRight("完成");
            memberId = getIntent().getStringExtra(EXTRA_DATA);
        }else{
            titleBar.setTilte("总裁信箱");
            titleBar.setTvRight("发送");
            memberId = getIntent().getStringExtra(EXTRA_DATA);
        }

        titleBar.setRightOnClicker(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(etContent.getText().toString())){
                    showToast("请输入内容");
                    return;
                }
                if(type == COMMUNICATION){
                    addCommunication(etContent.getText().toString());
                } else if(type == CALLBACK){
                    addCallback(etContent.getText().toString());
                } else if(type == FEEDBACK){
                    addFeedback(etContent.getText().toString());
                } else{
                    send(etContent.getText().toString());
                }

            }
        });
    }

    private void send(String content) {
        RequestEmail requestBean = new RequestEmail();
        requestBean.content = content;
        AppModelFactory.model().sendEmail(requestBean ,new ProgressSubscriber<Object>(mContext) {
            @Override
            public void onNext(Object result) {
                showToast("发送成功");
                onBackPressed();
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
                showToast("发送失败");
            }

        });
    }

    private void addCommunication(String content) {
        RequestAddCommunication requestBean = new RequestAddCommunication();
        requestBean.content = content;
        requestBean.comTime = System.currentTimeMillis() / 1000;
        requestBean.memberId = memberBean.memberId;
        requestBean.groupId = LoginInfoPrefs.getInstance(mContext).getGroupId();
        requestBean.staffId = LoginInfoPrefs.getInstance(mContext).getStaffId();
        requestBean.spName = UserfoPrefs.getInstance(mContext).getBaseonTypeDesc();
        AppModelFactory.model().addCommunication(requestBean ,new ProgressSubscriber<Object>(mContext) {
            @Override
            public void onNext(Object result) {
                showToast("添加成功");
                setResult(RESULT_OK);
                onBackPressed();
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
                showToast("添加失败");
            }

        });
    }

    private void addCallback(String content) {
        RequestAddCallback requestBean = new RequestAddCallback();
        requestBean.content = content;
        requestBean.visTime = System.currentTimeMillis() / 1000;
        requestBean.memberId = memberBean.memberId;
        requestBean.staffId = LoginInfoPrefs.getInstance(mContext).getStaffId();
        requestBean.spId = UserfoPrefs.getInstance(mContext).getBaseonId();
        AppModelFactory.model().addCallback(requestBean ,new ProgressSubscriber<Object>(mContext) {
            @Override
            public void onNext(Object result) {
                showToast("添加成功");
                setResult(RESULT_OK);
                onBackPressed();
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
                showToast("添加失败");
            }

        });
    }

    private void addFeedback(String content) {
        RequestFeedback requestBean = new RequestFeedback();
        requestBean.content = content;
        requestBean.bill_consume_id = memberId;
        AppModelFactory.model().addFeedback(requestBean ,new ProgressSubscriber<Object>(mContext) {
            @Override
            public void onNext(Object result) {
                showToast("添加成功");
                //通知界面刷新
                EvenManager.sendEvent(new NotifyEvent(NotifyEvent.RREFRESH));
                onBackPressed();
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
                showToast("添加失败");
            }

        });
    }

}
