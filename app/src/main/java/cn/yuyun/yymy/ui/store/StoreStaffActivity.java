package cn.yuyun.yymy.ui.store;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ajguan.library.EasyRefreshLayout;
import com.ajguan.library.LoadModel;
import com.example.http.ApiException;
import com.example.http.NoneProgressSubscriber;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.result.WarnningMemberBean;
import cn.yuyun.yymy.sp.LoginInfoPrefs;
import cn.yuyun.yymy.ui.home.analysis.StoreBean;
import cn.yuyun.yymy.ui.home.member.SortMemberAdapter;
import cn.yuyun.yymy.view.refresh.RefreshHeaderView;
import cn.yuyun.yymy.view.sidebar.PinyinComparator;
import cn.yuyun.yymy.view.sidebar.SideBar;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;

/**
 * @author
 * @desc
 * @date
 */

public class StoreStaffActivity extends BaseActivity {

    private RelativeLayout rlOne;
    private RelativeLayout rlTwo;

    private EasyRefreshLayout easyRefreshLayout1;
    private ListView listView;
    private TextView dialog;
    private SideBar sidebar;
    private SortMemberAdapter mAdapter;
    private List<WarnningMemberBean> data = new ArrayList<>();
    private final int REQUEST_CODE = 1111;
    private RelativeLayout rlEmpty;

    private StoreBean storeBean;

    public static Intent startStoreStaffActivity (Context context, StoreBean bean){
        return new Intent(context, StoreStaffActivity.class).putExtra(EXTRA_DATA, bean);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_staff);
    }

    @Override
    protected void setUpViewAndData() {
        initViews();
    }

    private void initViews() {
        storeBean = (StoreBean) getIntent().getSerializableExtra(EXTRA_DATA);
        rlEmpty = (RelativeLayout) findViewById(R.id.rl_empty);
        listView = (ListView) findViewById(R.id.listview);
        dialog = (TextView) findViewById(R.id.dialog);
        sidebar = (SideBar) findViewById(R.id.sidebar);
        sidebar.setTextView(dialog);
        // 设置字母导航触摸监听
        sidebar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                // 该字母首次出现的位置
                int position = mAdapter.getPositionForSelection(s.charAt(0));
                if (position != -1) {
                    listView.setSelection(position);
                }
            }
        });
        easyRefreshLayout1 = (EasyRefreshLayout) findViewById(R.id.easylayout);
        easyRefreshLayout1.setRefreshHeadView(new RefreshHeaderView(this));
        easyRefreshLayout1.autoRefresh();
        easyRefreshLayout1.setLoadMoreModel(LoadModel.NONE);
        easyRefreshLayout1.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {
            }

            @Override
            public void onRefreshing() {
                getMyMember();
            }
        });
    }

    private void getMyMember() {
        AppModelFactory.model().getMyMember(LoginInfoPrefs.getInstance(mContext).getStaffId(), new NoneProgressSubscriber<List<WarnningMemberBean>>(mContext) {
            @Override
            public void onNext(final List<WarnningMemberBean> result) {
                if (result != null) {
                    if (result.size() == 0) {
                        rlEmpty.setVisibility(View.VISIBLE);
                    } else {
                        rlEmpty.setVisibility(View.GONE);
                        // 数据在放在adapter之前需要排序

                        Collections.sort(data, new PinyinComparator());
                        mAdapter = new SortMemberAdapter(mContext, data);
                        listView.setAdapter(mAdapter);
                    }
                } else {
                    rlEmpty.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCompleted() {
                easyRefreshLayout1.refreshComplete();
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
                showToast("获取数据失败，请重试");
                easyRefreshLayout1.refreshComplete();
                rlEmpty.setVisibility(View.VISIBLE);
            }
        });
    }


}
