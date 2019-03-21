package cn.yuyun.yymy.ui.store.book;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ajguan.library.EasyRefreshLayout;
import com.ajguan.library.LoadModel;
import com.example.http.ApiException;
import com.example.http.DataBean;
import com.example.http.PageInfo;
import com.example.http.Presenter2;
import com.example.http.ProgressSubscriber;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.lzz.utils.ResoureUtils;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.event.NotifyEvent;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestAppointmentList;
import cn.yuyun.yymy.http.result.AppointmentBean;
import cn.yuyun.yymy.http.result.AppointmentBean.AppointmentListBean;
import cn.yuyun.yymy.http.result.AppointmentBean.AppointmentListBean.ReservationBookRspListBean;
import cn.yuyun.yymy.http.result.ResultBook;
import cn.yuyun.yymy.ui.home.member.AppointmentDetailActivity;
import cn.yuyun.yymy.utils.EvenManager;
import cn.yuyun.yymy.utils.GlideHelper;
import cn.yuyun.yymy.view.refresh.RefreshHeaderView;
import rx.Subscriber;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;
import static cn.yuyun.yymy.constan.Constans.EXTRA_TYPE;

/**
 * @author
 * @desc 当前预约列表
 * @date
 */

public class CurrentAppointmentActivity extends BaseActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.easylayout)
    EasyRefreshLayout easylayout;

    private String type;

    private AppointmentBookListAdapter mListAdapter;
    private List<ResultBook> dataList;

    public static Intent startCurrentAppointmentActivity(Context context, List<ResultBook> bean) {
        return new Intent(context, CurrentAppointmentActivity.class).putExtra(EXTRA_DATA, (Serializable) bean).putExtra(EXTRA_TYPE, "normal");
    }

    public static Intent startCurrentAppointmentActivityFromBook(Context context, List<ResultBook> bean) {
        return new Intent(context, CurrentAppointmentActivity.class).putExtra(EXTRA_DATA, (Serializable) bean);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_list);
        EvenManager.register(this);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNotifyEvent(NotifyEvent notifyEvent) {
        if (notifyEvent.tag == NotifyEvent.RREFRESH) {
            finish();
        }
    }

    private void initViews() {
        dataList = (List<ResultBook>) getIntent().getSerializableExtra(EXTRA_DATA);
        type = getIntent().getStringExtra(EXTRA_TYPE);
        titleBar.setTilte("当前预约列表");
        easylayout.setLoadMoreModel(LoadModel.NONE);
        easylayout.setEnablePullToRefresh(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mListAdapter = new AppointmentBookListAdapter(this);
        mListAdapter.addAll(dataList);
        mListAdapter.setOnItemClickListener(new AppointmentBookListAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(View view, ResultBook bean, int position) {
                if(null == type){
                    startActivity(AppointmentBookDetailActivity.startAppointmentBookDetailActivity(mContext, bean));
                }else{
                    startActivity(AppointmentDetailActivity.startAppointmentDetailActivity(mContext, bean));
                }
            }
        });
        recyclerView.setAdapter(mListAdapter);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EvenManager.unregister(this);
    }
}
