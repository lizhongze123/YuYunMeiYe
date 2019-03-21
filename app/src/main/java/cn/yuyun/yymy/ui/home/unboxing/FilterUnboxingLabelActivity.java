package cn.yuyun.yymy.ui.home.unboxing;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.http.ApiException;
import com.example.http.DataBean;
import com.example.http.ProgressSubscriber;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
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
import cn.yuyun.yymy.http.result.ResultUnboxingLabel;
import cn.yuyun.yymy.utils.EvenManager;
import cn.yuyun.yymy.view.BorderTextView;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;

/**
 * @author
 * @desc 筛选标签
 * @date
 */
public class FilterUnboxingLabelActivity extends BaseActivity {

    @BindView(R.id.flowLayout)
    TagFlowLayout mFlowLayout;
    @BindView(R.id.tv_count)
    TextView tvCount;
    @BindView(R.id.tv_positive)
    TextView tvPositive;
    private LayoutInflater mInflater;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_filter_unboxing_label);
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
        titleBar.setTilte("标签筛选");
        titleBar.setTitleBarBackground(Color.parseColor("#f8f8f8"));
        tvPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Set<Integer> selectedList = mFlowLayout.getSelectedList();
                List<ResultUnboxingLabel.UnboxingLabelBean> mValsSelected = new ArrayList<>();
                if(selectedList.size() == 0){
                    showToast("请选择标签");
                    return;
                }
                Iterator<Integer> iterator = selectedList.iterator();
                while (iterator.hasNext()) {
                    mValsSelected.add(dataList.get(iterator.next()));
                }
                startActivity(FilterUnboxingActivity.startFilterUnboxingActivity(mContext, mValsSelected));
            }
        });
        mInflater = LayoutInflater.from(mContext);
        setDesc(mFlowLayout.getSelectedList().size());
    }

    List<ResultUnboxingLabel.UnboxingLabelBean> dataList;


    private void getData() {
        AppModelFactory.model().getUnboxingLabel(1, new ProgressSubscriber<DataBean<ResultUnboxingLabel>>(mContext) {

            @Override
            public void onNext(DataBean<ResultUnboxingLabel> result) {
                if (null != result.data) {
                    if (null != result.data.record) {
                        dataList = result.data.record;

                        TagAdapter adapter;
                        mFlowLayout.setAdapter(adapter = new TagAdapter<ResultUnboxingLabel.UnboxingLabelBean>(result.data.record) {
                            @Override
                            public View getView(FlowLayout parent, int position, ResultUnboxingLabel.UnboxingLabelBean s) {
                                BorderTextView tv = (BorderTextView) mInflater.inflate(R.layout.item_unboxing_label_add, mFlowLayout, false);
                                tv.setText(s.labelName);
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
                                setDesc(mFlowLayout.getSelectedList().size());
                            }
                        });
                        mFlowLayout.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
                            @Override
                            public void onSelected(Set<Integer> selectPosSet) {
                                setDesc(mFlowLayout.getSelectedList().size());
                            }
                        });
                        Set<Integer> selectedList = new HashSet<>();
                        List<ResultUnboxingLabel.UnboxingLabelBean> labelList = (List<ResultUnboxingLabel.UnboxingLabelBean>) getIntent().getSerializableExtra(EXTRA_DATA);
                        for (int i = 0; i < dataList.size(); i++) {
                            for (int j = 0; j < labelList.size(); j++) {
                                if(labelList.get(j).labelId == dataList.get(i).labelId){
                                    selectedList.add(i);
                                }
                            }
                        }
                        adapter.setSelectedList(selectedList);
                        setDesc(mFlowLayout.getSelectedList().size());

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

    private void setDesc(int selectSize) {
       tvCount.setText("已选择" + selectSize + "个标签");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EvenManager.unregister(this);
    }
}
