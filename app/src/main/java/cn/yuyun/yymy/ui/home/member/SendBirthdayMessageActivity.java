package cn.yuyun.yymy.ui.home.member;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ajguan.library.EasyRefreshLayout;
import com.ajguan.library.LoadModel;
import com.example.http.ApiException;
import com.example.http.DataBean;
import com.example.http.PageInfo;
import com.example.http.Presenter2;
import com.example.http.ProgressSubscriber;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestSendMessage;
import cn.yuyun.yymy.http.request.RequestSms;
import cn.yuyun.yymy.http.result.ResultMemberBean;
import cn.yuyun.yymy.http.result.ResultMessageTemplate;
import cn.yuyun.yymy.http.result.ResultRandomMsg;
import cn.yuyun.yymy.sp.LoginInfoPrefs;
import cn.yuyun.yymy.sp.UserfoPrefs;
import cn.yuyun.yymy.ui.home.analysis.StoreBean;
import cn.yuyun.yymy.ui.store.member.MessageTemplateAdapter;
import cn.yuyun.yymy.ui.store.member.SendMemberAdapter;
import rx.Subscriber;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;

/**
 * @author
 * @desc 发送生日祝福
 * @date
 */
public class SendBirthdayMessageActivity extends BaseActivity {

    @BindView(R.id.rv_top)
    RecyclerView rvTop;
    private SendMemberAdapter sendMemberAdapter;

    @BindView(R.id.et_content)
    EditText etContent;

    private List<ResultMemberBean> memberBeanList;

    public static Intent startSendBirthdayMessageActivity(Context context, List<ResultMemberBean> memberList) {
        return new Intent(context, SendBirthdayMessageActivity.class).putExtra(EXTRA_DATA, (Serializable) memberList);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_birthday_message);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
        initDatas();
    }

    private void initDatas() {
        getRandomMessage();
    }

    private void initViews() {
        titleBar.setTilte("生日祝福");
        titleBar.setTvRight("发送");
        titleBar.setRightOnClicker(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send(etContent.getText().toString());
            }
        });
        memberBeanList = (List<ResultMemberBean>) getIntent().getSerializableExtra(EXTRA_DATA);

        LinearLayoutManager ms = new LinearLayoutManager(this);
        ms.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvTop.setLayoutManager(ms);
        sendMemberAdapter = new SendMemberAdapter(mContext);
        rvTop.setAdapter(sendMemberAdapter);
        sendMemberAdapter.addAll(memberBeanList);
        findViewById(R.id.tv_change).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRandomMessage();
            }
        });
    }

    private void send(String content) {
        if (TextUtils.isEmpty(content)) {
            showToast("内容不能为空");
            return;
        }
        RequestSendMessage requestSendMessage = new RequestSendMessage();
        List<RequestSendMessage.SendMsgListBean> list = new ArrayList<>();
        for (int i = 0; i < memberBeanList.size(); i++) {
            RequestSendMessage.SendMsgListBean bean = new RequestSendMessage.SendMsgListBean();
            ResultMemberBean memberBean = memberBeanList.get(i);
            bean.member_id = memberBean.memberId;
            bean.phone = memberBean.memberMobile;
            bean.content = content;
            list.add(bean);
        }
        requestSendMessage.smsSendDetailQoList = list;
        AppModelFactory.model().sendMessage(requestSendMessage, new ProgressSubscriber<Object>(mContext) {
            @Override
            public void onNext(Object o) {
                showToast("发送成功");
                finish();
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
                showToast("发送失败");
            }
        });

    }

    private void getRandomMessage(){
        AppModelFactory.model().getRandomMessage(new ProgressSubscriber<DataBean<ResultRandomMsg>>(mContext) {
            @Override
            public void onNext(DataBean<ResultRandomMsg> result) {
                if(null != result.data){
                    etContent.setText(result.data.content);
                }
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
                showToast("发送失败");
            }
        });
    }

}
