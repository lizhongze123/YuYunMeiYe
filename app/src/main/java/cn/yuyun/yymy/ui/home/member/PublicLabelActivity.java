package cn.yuyun.yymy.ui.home.member;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.LoginInfoPrefs;
import com.example.http.ApiException;
import com.example.http.DataBean;
import com.example.http.ProgressSubscriber;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.event.NotifyEvent;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestLabel;
import cn.yuyun.yymy.http.request.RequestPublicLabel;
import cn.yuyun.yymy.http.result.ResultLabel;
import cn.yuyun.yymy.http.result.ResultMemberBean;
import cn.yuyun.yymy.http.result.ResultUnboxingLabel;
import cn.yuyun.yymy.ui.home.unboxing.FilterUnboxingActivity;
import cn.yuyun.yymy.utils.EvenManager;
import cn.yuyun.yymy.view.BorderTextView;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;

/**
 * @author
 * @desc 公共标签
 * @date
 */
public class PublicLabelActivity extends BaseActivity {

    @BindView(R.id.flowLayout)
    TagFlowLayout mFlowLayout;
    private LayoutInflater mInflater;
    private ResultMemberBean memberBean;

    public static Intent startPublicLabelActivity(Context context, ResultMemberBean bean) {
        return new Intent(context, PublicLabelActivity.class).putExtra(EXTRA_DATA, bean);
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_public_label);
        EvenManager.register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNotifyEvent(NotifyEvent notifyEvent) {
        if (notifyEvent.tag == NotifyEvent.RREFRESH_LABEL) {

        }
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
        initData();
    }

    private void initData() {
        getData();
    }

    private void initViews() {
        memberBean = (ResultMemberBean) getIntent().getSerializableExtra(EXTRA_DATA);
        titleBar.setTilte("公共标签");
        titleBar.setTvRight("完成");
        titleBar.setRightOnClicker(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Set<Integer> selectedList = mFlowLayout.getSelectedList();
                List<RequestLabel> mValsSelected = new ArrayList<>();
                if(selectedList.size() == 0){
                    showToast("请选择标签");
                    return;
                }
                Iterator<Integer> iterator = selectedList.iterator();
                while (iterator.hasNext()) {
                    RequestLabel requestLabel = new RequestLabel();
                    requestLabel.kv_type = 2;
                    requestLabel.kv_key = "";
                    ResultLabel bean = dataList.get(iterator.next());
                    requestLabel.kv_value = bean.kv_value;
                    requestLabel.sm_id = memberBean.memberId;
                    mValsSelected.add(requestLabel);
                }
                addMemberLabel(mValsSelected);
//                startActivity(FilterUnboxingActivity.startFilterUnboxingActivity(mContext, mValsSelected));
            }
        });
        mInflater = LayoutInflater.from(mContext);
    }

    List<ResultLabel> dataList;


    private void getData() {
        AppModelFactory.model().getPublicLabel(LoginInfoPrefs.getInstance(mContext).getGroupId(), new ProgressSubscriber<DataBean<List<ResultLabel>>>(mContext) {

            @Override
            public void onNext(DataBean<List<ResultLabel>> result) {
                if (null != result.data) {
                    dataList = result.data;

                    TagAdapter adapter;
                    mFlowLayout.setAdapter(adapter = new TagAdapter<ResultLabel>(result.data) {
                        @Override
                        public View getView(FlowLayout parent, int position, ResultLabel s) {
                            BorderTextView tv = (BorderTextView) mInflater.inflate(R.layout.item_unboxing_label_add, mFlowLayout, false);
                            tv.setText(s.kv_value);
                            return tv;
                        }

                        @Override
                        public void onSelected(int position, View view) {
                            super.onSelected(position, view);
                            ((BorderTextView) view).setSelected(true);

                        }

                        @Override
                        public void unSelected(int position, View view) {
                            super.unSelected(position, view);
                            ((BorderTextView) view).setSelected(false);
                        }
                    });
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

    private void addMemberLabel(List<RequestLabel> list) {
        RequestPublicLabel body = new RequestPublicLabel();
        body.addKeyValueQoList = list;
        AppModelFactory.model().addMulti(body, new ProgressSubscriber<Object>(mContext) {

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EvenManager.unregister(this);
    }
}
