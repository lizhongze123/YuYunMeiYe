package cn.yuyun.yymy.ui.home.member;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.http.ApiException;
import com.example.http.DataBean;
import com.example.http.ProgressSubscriber;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.event.NotifyEvent;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.result.ResultLabel;
import cn.yuyun.yymy.http.result.ResultMemberBean;
import cn.yuyun.yymy.utils.EvenManager;
import cn.yuyun.yymy.view.BorderTextView;
import cn.yuyun.yymy.view.FlowLayout;
import cn.yuyun.yymy.view.WarnningDialog;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;

/**
 * @author
 * @desc
 * @date
 */

public class SelectMemberLabelActivity extends BaseActivity {

    @BindView(R.id.flowlayout)
    FlowLayout mFlowLayout;
    @BindView(R.id.tv_add)
    TextView tvAdd;
    @BindView(R.id.tv_public)
    TextView tvPublic;
    LayoutInflater mInflater;
    private ResultMemberBean memberBean;

    public static Intent startSelectMemberLabelActivity(Context context, ResultMemberBean bean) {
        return new Intent(context, SelectMemberLabelActivity.class).putExtra(EXTRA_DATA, bean);
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_select_member_label);
        EvenManager.register(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNotifyEvent(NotifyEvent notifyEvent) {
        if (notifyEvent.tag == NotifyEvent.RREFRESH) {
            getMemberLabel();
            setResult(RESULT_OK);
        }
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
        initData();
    }

    private void initData() {
        getMemberLabel();
    }

    private void initViews() {
        memberBean = (ResultMemberBean) getIntent().getSerializableExtra(EXTRA_DATA);
        titleBar.setTilte("添加标签");
        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(AddMemberLabelActivity.startAddMemberLabelActivity(mContext, memberBean));
            }
        });
        tvPublic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(PublicLabelActivity.startPublicLabelActivity(mContext, memberBean));
            }
        });
        mInflater = LayoutInflater.from(mContext);
        warnningDialog = new WarnningDialog(mContext, "确定解绑此标签？");
        warnningDialog.setOnPositiveListener(new WarnningDialog.OnDialogListener() {
            @Override
            public void onPositive() {
                delMemberLabel(labelList.get(tempPos).id, tempPos);
                warnningDialog.dismiss();
            }

            @Override
            public void onNegative() {

            }
        });
    }

    List<ResultLabel> labelList = new ArrayList<>();
    private WarnningDialog warnningDialog;
    private int tempPos;

    private void getMemberLabel() {
        AppModelFactory.model().getMemberLabel(2, memberBean.memberId, new ProgressSubscriber<DataBean<List<ResultLabel>>>(mContext) {

            @Override
            public void onNext(DataBean<List<ResultLabel>> result) {
                if (null != result) {
                    if(null != result.data){
                        labelList = result.data;
                        mFlowLayout.removeAllViewsInLayout();
                        for (int i = 0; i < result.data.size(); i++) {
                            ResultLabel labelBean = result.data.get(i);
                            BorderTextView rv = (BorderTextView) mInflater.inflate(R.layout.item_unboxing_label_add, mFlowLayout, false);
                            rv.setText(labelBean.kv_value);
                            final int finalI = i;
                            rv.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    tempPos = finalI;
                                    warnningDialog.show();
                                }
                            });
                            mFlowLayout.addView(rv);
                        }
                    }
                }
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
            }
        });
    }

    private void delMemberLabel(int id, final int position) {
        AppModelFactory.model().delMemberLabel(id, new ProgressSubscriber<Object>(mContext) {

            @Override
            public void onNext(Object result) {
                setResult(RESULT_OK);
                getMemberLabel();
                showToast("删除成功");
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
                showToast("删除失败，请重试");
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
            }
        });
    }

}
