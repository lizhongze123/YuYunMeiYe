package cn.yuyun.yymy.ui.store.book;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ajguan.library.EasyRefreshLayout;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.http.ApiException;
import com.example.http.DataBean;
import com.example.http.PageInfo;
import com.example.http.Presenter2;
import com.example.http.ProgressSubscriber;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.lzz.utils.DateTimeUtils;
import cn.lzz.utils.ResoureUtils;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.constan.Constans;
import cn.yuyun.yymy.event.NotifyEvent;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestAppointmentList;
import cn.yuyun.yymy.http.result.AppointmentBean;
import cn.yuyun.yymy.http.result.AppointmentBean.AppointmentListBean;
import cn.yuyun.yymy.http.result.AppointmentBean.AppointmentListBean.ReservationBookRspListBean;
import cn.yuyun.yymy.http.result.ResultBook;
import cn.yuyun.yymy.sp.UserfoPrefs;
import cn.yuyun.yymy.ui.home.analysis.StoreBean;
import cn.yuyun.yymy.ui.home.appointment.AddAppointmentActivity;
import cn.yuyun.yymy.utils.EvenManager;
import cn.yuyun.yymy.utils.GlideHelper;
import cn.yuyun.yymy.view.EmptyLayout;
import cn.yuyun.yymy.view.MyHorizontalScrollView;
import cn.yuyun.yymy.view.dialog.BottomSelectDialogStaff;
import cn.yuyun.yymy.view.refresh.RefreshHeaderView;
import rx.Subscriber;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;
import static cn.yuyun.yymy.http.result.ResultBook.AppointmentType.CANCEL;
import static cn.yuyun.yymy.http.result.ResultBook.AppointmentType.REVIEWING;

/**
 * @author
 * @desc
 * @date
 */

public class AppointmentBookActivity extends BaseActivity {

    @BindView(R.id.tv_desc1)
    TextView tvDesc1;
    @BindView(R.id.tv_desc2)
    TextView tvDesc2;
    @BindView(R.id.tv_desc3)
    TextView tvDesc3;
    @BindView(R.id.cb_toggle)
    CheckBox cbToggle;
    @BindView(R.id.ll_filter)
    RelativeLayout llFilter;
    @BindView(R.id.ll_left)
    LinearLayout llLeft;
    @BindView(R.id.ll_topContainer)
    LinearLayout llTopContainer;
    @BindView(R.id.sv_top)
    MyHorizontalScrollView svTop;
    @BindView(R.id.content_container)
    LinearLayout contentContainer;
    @BindView(R.id.sv_right)
    MyHorizontalScrollView svRight;
    @BindView(R.id.rv_left)
    RecyclerView rvLeft;
    @BindView(R.id.rl_block)
    RelativeLayout rlBlock;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.easylayout)
    EasyRefreshLayout easyRefreshLayout;
    @BindView(R.id.rl_list)
    RelativeLayout rlList;

    @BindView(R.id.filter_block)
    LinearLayout filterBlock;
    @BindView(R.id.filter_list)
    LinearLayout filterList;

    @BindView(R.id.emptyLayout)
    EmptyLayout emptyLayout;
    @BindView(R.id.empty)
    EmptyLayout empty;
    @BindView(R.id.ll_book)
    LinearLayout llBook;

    private AppointmentBookListAdapter mListAdapter;
    private Presenter2<ResultBook> presenter;

    private LeftTimeAdapter adapter;

    private StoreBean storeBean;
    private String defaultTime = "09:00:00";

    private BottomSelectDialogStaff dialog;

    private int timeType = 3;
    private TimePickerView timePickerView;

    private RequestAppointmentList requestBean = new RequestAppointmentList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_book);
        EvenManager.register(this);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
        if(!hasInternet()){
            empty.setVisibility(View.VISIBLE);
            empty.setErrorType(EmptyLayout.NETWORK_ERROR);
            llBook.setVisibility(View.GONE);
            return;
        }
        initData();
        initDialog();
    }

    private void initData() {
        empty.setVisibility(View.GONE);
        llBook.setVisibility(View.VISIBLE);
        initBlockData();
        initListData();
    }

    @Override
    public void onEmptyRefresh() {
        super.onEmptyRefresh();
        if(!hasInternet()){
            empty.setVisibility(View.VISIBLE);
            llBook.setVisibility(View.GONE);
            return;
        }
        empty.setVisibility(View.GONE);
        llBook.setVisibility(View.VISIBLE);
        initBlockData();
        initListData();
    }

    private void initBlockData() {
        tvDesc3.setText(DateTimeUtils.getNowTime());
        blockTime = DateTimeUtils.getNowTimeStamp();
        getBlockData(blockTime);
    }

    private void initListData() {
        requestBean.group_id = storeBean.group_id;
        requestBean.sp_id = storeBean.spId;
        requestBean.start_date = DateTimeUtils.getTimesMonthMorning();
        requestBean.end_date = DateTimeUtils.getTimesMonthNight();
        tvDesc1.setText(DateTimeUtils.getDateTimeText(requestBean.start_date, DateTimeUtils.FORMAT_DATE_UI));
        tvDesc2.setText(DateTimeUtils.getDateTimeText(requestBean.end_date, DateTimeUtils.FORMAT_DATE_UI));
        configurePresenter();
        presenter.loadData(true);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNotifyEvent(NotifyEvent notifyEvent) {
        if (notifyEvent.tag == NotifyEvent.APPOINTMENT) {
            getBlockData(blockTime);
        }
    }

    public static Intent startAppointmentBookActivity(Context context, StoreBean storeBean) {
        return new Intent(context, AppointmentBookActivity.class).putExtra(EXTRA_DATA, storeBean);
    }

    private void initViews() {
        storeBean = (StoreBean) getIntent().getSerializableExtra(EXTRA_DATA);
        titleBar.setTilte("预约本");
        titleBar.setRightIcon(R.drawable.icon_add_two);
        titleBar.setRightOnClicker(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Constans.isStoreLoginer){
                    showSelectDialog();
                }else{
                    showToast(getString(R.string.PARTNER));
                }
            }
        });
        cbToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    rlBlock.setVisibility(View.VISIBLE);
                    rlList.setVisibility(View.GONE);
                    filterBlock.setVisibility(View.VISIBLE);
                    filterList.setVisibility(View.GONE);
                } else {
                    rlBlock.setVisibility(View.GONE);
                    rlList.setVisibility(View.VISIBLE);
                    filterBlock.setVisibility(View.GONE);
                    filterList.setVisibility(View.VISIBLE);
                }
            }
        });

        svTop.setScrollViewListener(new MyHorizontalScrollView.ScrollViewListener() {
            @Override
            public void onScrollChanged(MyHorizontalScrollView scrollView, int x, int y, int oldx, int oldy) {
                svRight.scrollTo(x, y);
            }
        });
        svRight.setScrollViewListener(new MyHorizontalScrollView.ScrollViewListener() {
            @Override
            public void onScrollChanged(MyHorizontalScrollView scrollView, int x, int y, int oldx, int oldy) {
                svTop.scrollTo(x, y);
            }
        });

        timePickerView = new TimePickerBuilder(mContext, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                if (timeType == 1) {
                    tvDesc1.setText(DateTimeUtils.getDateTimeText(date, DateTimeUtils.FORMAT_DATE_UI));
                    requestBean.start_date = date.getTime() / 1000;
                    if (requestBean.end_date - requestBean.start_date < 0) {
                        showTextDialog("结束时间不能小于开始时间", false);
                        return;
                    }
                    presenter.loadData(true);
                } else if (timeType == 2) {
                    tvDesc2.setText(DateTimeUtils.getDateTimeText(date, DateTimeUtils.FORMAT_DATE_UI));
                    requestBean.end_date = date.getTime() / 1000;
                    if (requestBean.end_date - requestBean.start_date < 0) {
                        showTextDialog("结束时间不能小于开始时间", false);
                        return;
                    }
                    presenter.loadData(true);
                } else {
                    tvDesc3.setText(DateTimeUtils.getDateTimeText(date, DateTimeUtils.FORMAT_DATE_UI));
                    blockTime = date.getTime() / 1000;
                    getBlockData(blockTime);
                }
            }
        }).build();

        initList();

    }

    private long blockTime;

    @OnClick({R.id.rl_desc2, R.id.rl_desc1, R.id.rl_desc3})
    public void selectTime(View view) {
        if (view.getId() == R.id.rl_desc3) {
            timeType = 3;
            timePickerView.show();
        } else if (view.getId() == R.id.rl_desc2) {
            timeType = 2;
            timePickerView.show();
        } else {
            timeType = 1;
            timePickerView.show();
        }
    }

    private void initDialog() {
        dialog = new BottomSelectDialogStaff(this, R.layout.dialog_bottom_select_appointment);
        dialog.setItemClickListener(new BottomSelectDialogStaff.ItemClickListener() {

            @Override
            public void oClick(int item) {
                switch (item) {
                    case 0:
                        dialog.dismiss();
                        break;
                    case 1:
                        //总部员工账号禁止预约
                        if (UserfoPrefs.getInstance(mContext).getBaseonType().equals("1")) {
                            showTextDialog("总部员工账号禁止个人预约", false);
                            return;
                        }
                        startActivity(new Intent(AppointmentBookActivity.this, AddAppointmentActivity.class));
                        dialog.dismiss();
                        break;
                    case 2:
                        startActivity(AddStoreAppointmentActivity.startAddStoreAppointmentActivity(mContext, storeBean));
                        dialog.dismiss();
                        break;
                    default:
                }
            }
        });
    }

    public void showSelectDialog() {
        if (dialog != null) {
            dialog.show();
        }
    }

    private void initList() {
        rvList.setLayoutManager(new LinearLayoutManager(this));
        mListAdapter = new AppointmentBookListAdapter(this);
        mListAdapter.setOnItemClickListener(new AppointmentBookListAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(View view, ResultBook bean, int position) {
                startActivity(AppointmentBookDetailActivity.startAppointmentBookDetailActivity(mContext, bean));
            }
        });
        rvList.setAdapter(mListAdapter);
        easyRefreshLayout.setRefreshHeadView(new RefreshHeaderView(mContext));
        easyRefreshLayout.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {
                presenter.loadData(false);
            }

            @Override
            public void onRefreshing() {
                presenter.loadData(true);
            }
        });
    }

    private int initLeft(String start, String end) {
        defaultTime = start;
        List<String> itemList = new ArrayList<>();
        String[] startAry = start.split(":");
        String[] endAry = end.split(":");
        int count = (Integer.valueOf(endAry[0]) - Integer.valueOf(startAry[0])) * 2 + 1;
        if (startAry[1].equals("30")) {
            count--;
        }
        if (endAry[1].equals("30")) {
            count++;
        }
        boolean isHalf = false;
        if (startAry[1].equals("30")) {
            isHalf = true;
        } else {
            isHalf = false;
        }
        int s = Integer.valueOf(startAry[0]);
        for (int i = 0; i < count; i++) {
            if (isHalf) {
                if (i % 2 == 0) {
                    itemList.add((s) + ":30");
                    s++;
                } else {
                    itemList.add((s) + ":00");
                }
            } else {
                if (i % 2 == 0) {
                    itemList.add((s) + ":00");
                } else {
                    itemList.add((s) + ":30");
                    s++;
                }
            }
        }
        adapter = new LeftTimeAdapter();
        adapter.addAll(itemList);
        rvLeft.setLayoutManager(new LinearLayoutManager(this));
        rvLeft.setAdapter(adapter);
        return itemList.size() - 1;
    }

    private List<List<List<ResultBook>>> setData(AppointmentBean appointmentBean, int col) {

        String[] startAry = appointmentBean.appointment_start.split(":");
        String[] endAry = appointmentBean.appointment_end.split(":");

        List<List<List<ResultBook>>> listOne = new ArrayList<>();

        for (int i = 0; i < appointmentBean.appointment_list.size(); i++) {

            AppointmentListBean outBean = appointmentBean.appointment_list.get(i);

            List<List<ResultBook>> listTwo = new ArrayList<>();

            for (int l = 0; l < col; l++) {

                List<ResultBook> listThree = new ArrayList<>();

                for (int j = 0; j < outBean.reservationBookRspList.size(); j++) {

                    if(outBean.reservationBookRspList.get(j).expired == 0 && outBean.reservationBookRspList.get(j).status == REVIEWING){

                    }else if(outBean.reservationBookRspList.get(j).status == CANCEL){

                    }else{
                        //没有过期
                        String[] startAryIn = outBean.reservationBookRspList.get(j).startTime.split(":");
                        String[] endAryIn = outBean.reservationBookRspList.get(j).endTime.split(":");
                        //要几个
                        int duration = (Integer.valueOf(endAryIn[0]) - Integer.valueOf(startAryIn[0])) * 2;
                        if (startAryIn[1].equals("30")) {
                            duration--;
                        }
                        if (endAryIn[1].equals("30")) {
                            duration++;
                        }

                        //起点
                        int topMarginCount = (Integer.valueOf(startAryIn[0]) - Integer.valueOf(startAry[0])) * 2;
                        if (startAry[1].equals("30")) {
                            topMarginCount--;
                        }
                        if (startAryIn[1].equals("30")) {
                            topMarginCount++;
                        }

                        if(l >= topMarginCount && l < duration + topMarginCount){

                            ReservationBookRspListBean bean = outBean.reservationBookRspList.get(j);

                            ResultBook book = new ResultBook();
                            book.memberName = bean.member_name;
                            book.member_level_name = bean.member_level_name;
                            book.mechanicStaffName = bean.mechanic_staff_name;
                            book.phone = bean.phone;
                            book.serviceName = bean.service_name;
                            book.spName = bean.sp_name;
                            book.submit_notes = bean.submit_notes;
                            book.date = bean.date;
                            book.startTime = bean.startTime;
                            book.endTime = bean.endTime;
                            book.status = bean.status;
                            book.id = bean.sp_reservation_id;
                            book.memberAvatar = bean.member_avatar;
                            book.expired = bean.expired;

                            listThree.add(book);

                        }
                    }

                }
                listTwo.add(listThree);
            }

            listOne.add(listTwo);
        }

        return listOne;
    }


    private void getBlockData(long startDate) {
        final RequestAppointmentList requestAppointmentList = new RequestAppointmentList();
        requestAppointmentList.start_date = startDate;
        requestAppointmentList.end_date = startDate;
        requestAppointmentList.group_id = storeBean.group_id;
        requestAppointmentList.sp_id = storeBean.spId;
        AppModelFactory.model().getAppointmentList(requestAppointmentList, new ProgressSubscriber<DataBean<AppointmentBean>>(mContext) {

            @Override
            public void onCompleted() {
                super.onCompleted();
            }

            @Override
            public void onNext(DataBean<AppointmentBean> result) {
                if (null != result && null != result.data) {

                    int col = initLeft(result.data.appointment_start, result.data.appointment_end);

                    if (null != result.data.appointment_list) {

                        llTopContainer.removeAllViews();
                        contentContainer.removeAllViews();
                        LayoutInflater inflater = LayoutInflater.from(mContext);
                        final List<List<List<ResultBook>>> listOne  = setData(result.data, col);

                        for (int i = 0; i < listOne.size(); i++) {
                            final AppointmentBean.AppointmentListBean resultAvailableStaff = result.data.appointment_list.get(i);
                            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
                            lp.rightMargin = ResoureUtils.getDimension(mContext, R.dimen.x1);
                            View view = inflater.inflate(R.layout.item_book_top, null);
                            TextView tv = (TextView) view.findViewById(R.id.tv_staffName);
                            tv.setText(resultAvailableStaff.staff_name);
                            view.setLayoutParams(lp);
                            llTopContainer.addView(view);

                            LinearLayout layoutRoot = new LinearLayout(mContext);
                            LinearLayout.LayoutParams s = new LinearLayout.LayoutParams(ResoureUtils.getDimension(mContext, R.dimen.x305), LinearLayout.LayoutParams.WRAP_CONTENT);
                            layoutRoot.setPadding(10, 0, 10, 0);
                            layoutRoot.setOrientation(LinearLayout.VERTICAL);
                            layoutRoot.setLayoutParams(s);

                            for (int j = 0; j < col; j++) {

                                final List<ResultBook> dataList = listOne.get(i).get(j);

                                RelativeLayout child = new RelativeLayout(mContext);
                                RelativeLayout.LayoutParams neiS = new RelativeLayout.LayoutParams(ResoureUtils.getDimension(mContext, R.dimen.x314), ResoureUtils.getDimension(mContext, R.dimen.item_appointment_height));
                                child.setBackground(ResoureUtils.getDrawable(mContext, R.drawable.shape_appointment_book_item));
                                child.setPadding(0, 0, 0, 4);
                                child.setLayoutParams(neiS);

                                if(dataList.size() == 0){

                                    final int finalJ = j;
                                    child.setOnLongClickListener(new View.OnLongClickListener() {
                                        @Override
                                        public boolean onLongClick(View v) {
                                            if (UserfoPrefs.getInstance(mContext).getBaseonType().equals("1")) {
                                                showTextDialog("总部员工账号禁止个人预约", false);

                                            }else {
                                                Bundle bundle = new Bundle();
                                                bundle.putString("start", adapter.getString(finalJ));
                                                bundle.putString("end", adapter.getString(finalJ + 1));
                                                bundle.putString("date", tvDesc1.getText().toString());
                                                bundle.putString("staffId", resultAvailableStaff.staff_id);
                                                bundle.putString("staffName", resultAvailableStaff.staff_name);
                                                startActivity(AddStoreAppointmentActivity.startAddStoreAppointmentActivity(mContext, storeBean, bundle));
                                            }
                                            return false;

                                        }
                                    });

                                }else{

                                    View viewIn = inflater.inflate(R.layout.item_book_member, null);
                                    ImageView ivAvatar = (ImageView) viewIn.findViewById(R.id.iv_avatar);
                                    TextView tvName = (TextView) viewIn.findViewById(R.id.tv_name);
                                    TextView tvProject = (TextView) viewIn.findViewById(R.id.tv_project);
                                    ImageView ivResult = (ImageView) viewIn.findViewById(R.id.iv_result);

                                    final ResultBook bean = dataList.get(0);

                                    if(dataList.size() > 1){
                                        tvProject.setVisibility(View.VISIBLE);
                                        TextViewUtils.setTextOrEmpty(tvProject, "当前预约有" + listOne.get(i).get(j).size() + "人");
                                        child.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                startActivity(CurrentAppointmentActivity.startCurrentAppointmentActivityFromBook(mContext, dataList));
                                            }
                                        });

                                           /* for (int k = 0; k < dataList.size(); k++) {
                                                if(dataList.get(k).status == ResultBook.AppointmentType.AGREE){
                                                    ivResult.setImageDrawable(ResoureUtils.getDrawable(mContext, ResultBook.AppointmentType.AGREE.imgId));
                                                    break;
                                                }else{
                                                    ivResult.setImageDrawable(ResoureUtils.getDrawable(mContext, ResultBook.AppointmentType.REVIEWING.imgId));
                                                }
                                            }*/

                                    }else{
                                        child.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                startActivity(AppointmentBookDetailActivity.startAppointmentBookDetailActivity(mContext, bean));
                                            }
                                        });
                                    }

                                    if (!TextUtils.isEmpty(bean.memberAvatar)) {
                                        if (bean.memberAvatar.startsWith(mContext.getString(R.string.HTTP))) {
                                            GlideHelper.displayImage(mContext, bean.memberAvatar, ivAvatar, R.drawable.avatar_default_female);
                                        } else {
                                            GlideHelper.displayImage(mContext, mContext.getString(R.string.image_url_prefix) + bean.memberAvatar, ivAvatar, R.drawable.avatar_default_female);
                                        }
                                    } else {
                                        GlideHelper.displayImage(mContext, R.drawable.avatar_default_female, ivAvatar);
                                    }

                                    TextViewUtils.setTextOrEmpty(tvName, bean.memberName);
                                    ivResult.setImageDrawable(ResoureUtils.getDrawable(mContext, bean.status.imgId));
                                    child.addView(viewIn);
                                }

                                layoutRoot.addView(child);
                            }

                            contentContainer.addView(layoutRoot);

                        }

                    }

                }
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
                showToast("加载失败");
            }

            @Override
            public void onNormalError(ApiException ex) {
                super.onNormalError(ex);
            }

            @Override
            public void onNoNetWorkError() {
                super.onNoNetWorkError();
                showToast(getString(R.string.error_no_network));
            }
        });
    }

    private void configurePresenter() {
        presenter = new Presenter2<>();
        presenter.setLoadDataListener(new Presenter2.OnPresenterLoadListener<ResultBook>() {

            @Override
            public void onSuccess(List<ResultBook> result, int total, boolean isRefresh) {
                emptyLayout.setVisibility(View.GONE);
                easyRefreshLayout.setVisibility(View.VISIBLE);

                if(null != result){
                    List<ResultBook> list = new ArrayList<>();
                    for (int i = 0; i < result.size(); i++) {
                        if(result.get(i).expired == 0 && result.get(i).status == REVIEWING){

                        }else{
                            list.add(result.get(i));
                        }
                    }
                    if (isRefresh) {
                        mListAdapter.notifyDataSetChanged(list);
                    } else {
                        mListAdapter.addAll(list);
                    }
                }
            }

            @Override
            public void onFailed(boolean isRefresh) {
                emptyLayout.setVisibility(View.VISIBLE);
                emptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
                easyRefreshLayout.setVisibility(View.GONE);
            }

            @Override
            public void onCompleted(boolean isRefresh) {
                easyRefreshLayout.refreshComplete();
                easyRefreshLayout.closeLoadView();
            }

            @Override
            public void onEmptyData() {
                emptyLayout.setVisibility(View.VISIBLE);
                emptyLayout.setErrorType(EmptyLayout.NODATA);
                easyRefreshLayout.setVisibility(View.GONE);
            }

            @Override
            public void onNoneMoreData() {
                showToast("没有更多数据了");
            }

            @Override
            public void onLoad(Subscriber<DataBean<PageInfo<ResultBook>>> subscriber, int pageIndex, int pageSize) {
                AppModelFactory.model().getStoreBookList(requestBean, pageIndex, pageSize, subscriber);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EvenManager.unregister(this);
    }
}
