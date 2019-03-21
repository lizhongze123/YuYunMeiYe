package cn.yuyun.yymy.ui.store.member;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

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
import cn.yuyun.yymy.event.NotifyEvent;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestSendMessage;
import cn.yuyun.yymy.http.request.RequestSms;
import cn.yuyun.yymy.http.result.ResultMemberBean;
import cn.yuyun.yymy.http.result.ResultMessageTemplate;
import cn.yuyun.yymy.sp.LoginInfoPrefs;
import cn.yuyun.yymy.sp.UserfoPrefs;
import cn.yuyun.yymy.utils.EvenManager;
import rx.Subscriber;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;

/**
 * @author lzz
 * @desc 群发信息
 * @date
 */
public class SendMessageActivity extends BaseActivity {

    @BindView(R.id.rv_top)
    RecyclerView rvTop;
    private SendMemberAdapter sendMemberAdapter;

    @BindView(R.id.et_content)
    EditText etContent;

    @BindView(R.id.rl1)
    RelativeLayout rl1;
    @BindView(R.id.rv1)
    RecyclerView rv1;
    @BindView(R.id.easylayout1)
    EasyRefreshLayout easyRefreshLayout1;
    private MessageTemplateAdapter messageTemplateAdapter1;

    private Presenter2<ResultMessageTemplate> presenter1;

    private List<ResultMemberBean> memberBeanList;

    public static Intent startSendMessageActivity(Context context, List<ResultMemberBean> memberList) {
        return new Intent(context, SendMessageActivity.class).putExtra(EXTRA_DATA, (Serializable) memberList);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
        initDatas();
    }
    private String contentHead = "";

    private void initDatas() {
        configurePresenter1();
        presenter1.loadData(true);
    }

    private void initViews() {
        titleBar.setTilte("群发信息");
        titleBar.setTvRight("发送");
        titleBar.setRightOnClicker(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send(etContent.getText().toString());
            }
        });
        memberBeanList = (List<ResultMemberBean>) getIntent().getSerializableExtra(EXTRA_DATA);
        contentHead = "【" +  UserfoPrefs.getInstance(mContext).getGroupName() + "_" + UserfoPrefs.getInstance(mContext).getBaseonIdDesc() + "_" + UserfoPrefs.getInstance(mContext).getStaffName() + "】";
        LinearLayoutManager ms = new LinearLayoutManager(this);
        ms.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvTop.setLayoutManager(ms);
        sendMemberAdapter = new SendMemberAdapter(mContext);
        rvTop.setAdapter(sendMemberAdapter);
        sendMemberAdapter.addAll(memberBeanList);

        messageTemplateAdapter1 = new MessageTemplateAdapter(mContext);
        rv1.setLayoutManager(new LinearLayoutManager(this));
        easyRefreshLayout1.setEnablePullToRefresh(false);
        easyRefreshLayout1.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {
                presenter1.loadData(false);
            }

            @Override
            public void onRefreshing() {
                presenter1.loadData(true);
            }
        });
        rv1.setAdapter(messageTemplateAdapter1);

        messageTemplateAdapter1.setOnItemClickListener(new MessageTemplateAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(View view, ResultMessageTemplate bean, int position) {
                etContent.setText("");
                etContent.setText(bean.content);
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
            bean.content = contentHead + content;
            list.add(bean);
        }
        requestSendMessage.smsSendDetailQoList = list;
        AppModelFactory.model().sendMessage(requestSendMessage, new ProgressSubscriber<Object>(mContext) {
            @Override
            public void onNext(Object o) {
                showToast("发送成功");
                EvenManager.sendEvent(new NotifyEvent());
                finish();
            }

            @Override
            public void onNormalError(ApiException ex) {
                super.onNormalError(ex);
                showToast("发送失败");
            }

            @Override
            public void onNoNetWorkError() {
                super.onNoNetWorkError();
                showToast(getString(R.string.error_no_network));
            }
        });

    }

    private void configurePresenter1() {
        presenter1 = new Presenter2<>();
        presenter1.setLoadDataListener(new Presenter2.OnPresenterLoadListener<ResultMessageTemplate>() {

            @Override
            public void onSuccess(List<ResultMessageTemplate> result, int total, final boolean isRefresh) {
                if (result != null) {
                   /* List<ResultMessageTemplate> newResult = new ArrayList<>();
                    for (int i = 0; i < result.size(); i++) {
                        if(result.get(i).og_type == 2){
                            newResult.add(result.get(i));
                        }
                    }*/
                    messageTemplateAdapter1.addAll(result);
                }
            }

            @Override
            public void onFailed(boolean isRefresh) {
                showToast("加载失败，请稍候重试");
            }

            @Override
            public void onCompleted(boolean isRefresh) {
                easyRefreshLayout1.loadMoreComplete();
            }

            @Override
            public void onEmptyData() {
            }

            @Override
            public void onNoneMoreData() {
                showToast("没有更多数据了");
                easyRefreshLayout1.setLoadMoreModel(LoadModel.NONE);
            }

            @Override
            public void onLoad(Subscriber<DataBean<PageInfo<ResultMessageTemplate>>> subscriber, int pageIndex, int pageSize) {
                AppModelFactory.model().getMsgTemplateWithGroup(LoginInfoPrefs.getInstance(mContext).getGroupId(), pageIndex, pageSize, new RequestSms(), subscriber);
            }
        });
    }


}
