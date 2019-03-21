package cn.yuyun.yymy.ui.home.member;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.example.http.ApiException;
import com.example.http.ProgressSubscriber;

import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.event.NotifyEvent;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestAddUnboxingLabel;
import cn.yuyun.yymy.http.request.RequestLabel;
import cn.yuyun.yymy.http.result.ResultMemberBean;
import cn.yuyun.yymy.utils.EvenManager;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;

/**
 * @author
 * @desc 填写会员标签
 * @date
 */
public class AddMemberLabelActivity extends BaseActivity {

    private EditText etContent;
    private ResultMemberBean memberBean;

    public static Intent startAddMemberLabelActivity(Context context, ResultMemberBean bean) {
        return new Intent(context, AddMemberLabelActivity.class).putExtra(EXTRA_DATA, bean);
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_add_unboxing_label);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
    }

    private void initViews() {
        memberBean = (ResultMemberBean) getIntent().getSerializableExtra(EXTRA_DATA);
        titleBar.setTilte("填写标签");
        titleBar.setTvRight("完成");
        titleBar.setRightOnClicker(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(etContent.getText().toString())){
                    showToast("请输入内容");
                    return;
                }
                addMemberLabel(etContent.getText().toString());
            }
        });
        etContent = (EditText) findViewById(R.id.et_content);

    }


    private void addMemberLabel(final String content) {
        RequestLabel requestLabel = new RequestLabel();
        requestLabel.kv_type = 2;
        requestLabel.kv_key = "";
        requestLabel.kv_value = content;
        requestLabel.sm_id = memberBean.memberId;
        AppModelFactory.model().addMemberLabel(requestLabel, new ProgressSubscriber<Object>(mContext) {

            @Override
            public void onNext(Object result) {
                showToast("添加成功");
                EvenManager.sendEvent(new NotifyEvent(NotifyEvent.RREFRESH));
                finish();
            }

            @Override
            public void onNormalError(ApiException ex) {
                super.onNormalError(ex);
                showToast("添加失败，请稍后重试");
            }

            @Override
            public void onNoNetWorkError() {
                super.onNoNetWorkError();
                showToast(mContext.getString(R.string.error_no_network));
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
            }
        });
    }

}
