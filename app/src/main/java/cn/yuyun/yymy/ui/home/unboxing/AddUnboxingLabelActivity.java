package cn.yuyun.yymy.ui.home.unboxing;

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
import cn.yuyun.yymy.utils.EvenManager;

/**
 * @author
 * @desc 填写标签
 * @date
 */
public class AddUnboxingLabelActivity extends BaseActivity {

    private EditText etContent;

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
        titleBar.setTilte("填写标签");
        titleBar.setTvRight("完成");
        titleBar.setRightOnClicker(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(etContent.getText().toString())){
                    showToast("请输入内容");
                    return;
                }
                submit(etContent.getText().toString());
            }
        });
        etContent = (EditText) findViewById(R.id.et_content);

    }

    private void submit(final String content) {
        RequestAddUnboxingLabel requestBody = new RequestAddUnboxingLabel();
        requestBody.labelName = content;
        AppModelFactory.model().addUnboxingLabel(requestBody, new ProgressSubscriber<Object>(mContext) {

            @Override
            public void onNext(Object result) {
                EvenManager.sendEvent(new NotifyEvent(NotifyEvent.RREFRESH_LABEL));
                showToast("添加成功");
                finish();
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
                showToast("添加失败，请重试");
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
            }
        });
    }


}
