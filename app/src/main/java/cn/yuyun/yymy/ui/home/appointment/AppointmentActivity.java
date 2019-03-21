package cn.yuyun.yymy.ui.home.appointment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.http.ApiException;
import com.example.http.DataBean;
import com.example.http.ProgressSubscriber;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.lzz.utils.DateTimeUtils;
import cn.lzz.utils.ResoureUtils;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.event.NotifyEvent;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestAppointmentList;
import cn.yuyun.yymy.http.result.AppointmentBean;
import cn.yuyun.yymy.http.result.ResultBook;
import cn.yuyun.yymy.sp.LoginInfoPrefs;
import cn.yuyun.yymy.sp.UserfoPrefs;
import cn.yuyun.yymy.ui.home.member.AppointmentDetailActivity;
import cn.yuyun.yymy.ui.store.book.CurrentAppointmentActivity;
import cn.yuyun.yymy.ui.store.book.LeftTimeAdapter;
import cn.yuyun.yymy.utils.EvenManager;
import cn.yuyun.yymy.utils.GlideHelper;
import cn.yuyun.yymy.view.DragRelativeLayout;
import cn.yuyun.yymy.view.EmptyLayout;

import static cn.yuyun.yymy.http.result.ResultBook.AppointmentType.CANCEL;
import static cn.yuyun.yymy.http.result.ResultBook.AppointmentType.REVIEWING;

/**
 * @author
 * @desc
 * @date
 */
public class AppointmentActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

//	private SwipeRefreshLayout srl;

    private String date;
    @BindView(R.id.rv_left)
    RecyclerView rvLeft;
    @BindView(R.id.rl_block)
    RelativeLayout rlBlock;
    @BindView(R.id.empty)
    EmptyLayout empty;
    @BindView(R.id.container_left)
    LinearLayout containerLeft;
    @BindView(R.id.container_right)
    LinearLayout containerRight;

    private String defaultTime = "09:00:00";

    private LeftTimeAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment2);
        EvenManager.register(this);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
    }

    private void initViews() {

        titleBar.setTilte("预约管理");
        titleBar.setRightIcon(R.drawable.icon_add_two);
        titleBar.setRightOnClicker(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //总部员工账号禁止预约
                if (UserfoPrefs.getInstance(mContext).getBaseonType().equals("1")) {
                    showTextDialog("总部员工账号禁止个人预约", false);
                } else {
                    startActivity(new Intent(mContext, AddAppointmentActivity.class));
                }
            }
        });

		/*
		srl = (SwipeRefreshLayout) findViewById(R.id.srl);
		srl.setColorSchemeResources(android.R.color.holo_green_light,android.R.color.holo_blue_bright,
				android.R.color.holo_orange_light, android.R.color.holo_red_light);
		srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				getData(DateTimeUtils.parseToStamp(date, DateTimeUtils.FORMAT_DATE_UI), DateTimeUtils.parseToStamp(date, DateTimeUtils.FORMAT_DATE_UI));
			}
		});*/

        initTools();
        if(!hasInternet()){
            empty.setVisibility(View.VISIBLE);
            empty.setErrorType(EmptyLayout.NETWORK_ERROR);
            rlBlock.setVisibility(View.GONE);
            return;
        }
        empty.setVisibility(View.GONE);
        rlBlock.setVisibility(View.VISIBLE);
        getData(DateTimeUtils.parseToStamp(date, DateTimeUtils.FORMAT_DATE_UI_TWO), DateTimeUtils.parseToStamp(date, DateTimeUtils.FORMAT_DATE_UI_TWO));

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

    private void initTools() {

        String oneDay = DateTimeUtils.getCustomDay2(2, DateTimeUtils.FORMAT_MD_EN_TWO);
        String twoDay = DateTimeUtils.getCustomDay2(3, DateTimeUtils.FORMAT_MD_EN_TWO);
        String threeDay = DateTimeUtils.getCustomDay2(4, DateTimeUtils.FORMAT_MD_EN_TWO);
        String fourDay = DateTimeUtils.getCustomDay2(5, DateTimeUtils.FORMAT_MD_EN_TWO);
        String fiveDay = DateTimeUtils.getCustomDay2(6, DateTimeUtils.FORMAT_MD_EN_TWO);

        ((RadioButton) findViewById(R.id.rb1)).setText("今天");
        ((RadioButton) findViewById(R.id.rb2)).setText("明天");
        ((RadioButton) findViewById(R.id.rb3)).setText("后天");
        ((RadioButton) findViewById(R.id.rb4)).setText(twoDay);
        ((RadioButton) findViewById(R.id.rb5)).setText(threeDay);
        ((RadioButton) findViewById(R.id.rb6)).setText(fourDay);
        ((RadioButton) findViewById(R.id.rb7)).setText(fiveDay);
        ((RadioGroup) findViewById(R.id.radioGroup)).setOnCheckedChangeListener(this);

        date = DateTimeUtils.getCustomDay2(0, DateTimeUtils.FORMAT_DATE_UI_TWO);

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb1:
                date = DateTimeUtils.getCustomDay(0, DateTimeUtils.FORMAT_DATE_UI_TWO);
                break;
            case R.id.rb2:
                date = DateTimeUtils.getCustomDay2(1, DateTimeUtils.FORMAT_DATE_UI_TWO);
                break;
            case R.id.rb3:
                date = DateTimeUtils.getCustomDay2(2, DateTimeUtils.FORMAT_DATE_UI_TWO);
                break;
            case R.id.rb4:
                date = DateTimeUtils.getCustomDay2(3, DateTimeUtils.FORMAT_DATE_UI_TWO);
                break;
            case R.id.rb5:
                date = DateTimeUtils.getCustomDay2(4, DateTimeUtils.FORMAT_DATE_UI_TWO);
                break;
            case R.id.rb6:
                date = DateTimeUtils.getCustomDay2(5, DateTimeUtils.FORMAT_DATE_UI_TWO);
                break;
            case R.id.rb7:
                date = DateTimeUtils.getCustomDay2(6, DateTimeUtils.FORMAT_DATE_UI_TWO);
                break;
            default:
        }

        getData(DateTimeUtils.parseToStamp(date, DateTimeUtils.FORMAT_DATE_UI_TWO), DateTimeUtils.parseToStamp(date, DateTimeUtils.FORMAT_DATE_UI_TWO));
    }

    private void getData(long startDate, long endDate) {
        RequestAppointmentList requestAppointmentList = new RequestAppointmentList();
        requestAppointmentList.start_date = startDate;
        requestAppointmentList.end_date = startDate;
        requestAppointmentList.group_id = LoginInfoPrefs.getInstance(this).getGroupId();
        requestAppointmentList.sp_id = UserfoPrefs.getInstance(this).getOgId();
        requestAppointmentList.staff_id = LoginInfoPrefs.getInstance(this).getStaffId();
        AppModelFactory.model().getAppointmentList(requestAppointmentList, new ProgressSubscriber<DataBean<AppointmentBean>>(mContext) {

            @Override
            public void onCompleted() {
                super.onCompleted();
				/*if (srl.isRefreshing()) {
					srl.setRefreshing(false);
				}*/
            }

            @Override
            public void onNext(DataBean<AppointmentBean> result) {
                if (null != result) {

                    if (null != result.data) {

                        int col = initLeft(result.data.appointment_start, result.data.appointment_end);

                        if (null != result.data.appointment_list) {

                            containerRight.removeAllViews();
                            containerLeft.removeAllViews();

                            final List<List<List<ResultBook>>> listOne = setData(result.data, col);
                            LayoutInflater inflater = LayoutInflater.from(mContext);

                            for (int i = 0; i < listOne.size(); i++) {

                                for (int j = 0; j < col; j++) {

                                    final List<ResultBook> dataList = listOne.get(i).get(j);

                                   RelativeLayout child = new RelativeLayout(mContext);
                                    RelativeLayout.LayoutParams neiS = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ResoureUtils.getDimension(mContext, R.dimen.item_appointment_height));
                                    child.setBackground(ResoureUtils.getDrawable(mContext, R.drawable.shape_appointment_book_item));
                                    child.setPadding(0, 0, 0, 4);
                                    child.setLayoutParams(neiS);

                                    if (dataList.size() == 0) {

                                        final int finalJ = j;
                                        child.setOnLongClickListener(new View.OnLongClickListener() {
                                            @Override
                                            public boolean onLongClick(View v) {
                                                if (UserfoPrefs.getInstance(mContext).getBaseonType().equals("1")) {
                                                    showTextDialog("总部员工账号禁止个人预约", false);
                                                }else{
                                                    Bundle bundle = new Bundle();
                                                    bundle.putString("start", adapter.getString(finalJ));
                                                    bundle.putString("end", adapter.getString(finalJ + 1));
                                                    bundle.putString("date", date);
                                                    startActivity(AddAppointmentActivity.startAddAppointmentActivity(mContext, bundle));
                                                }
                                                return false;
                                            }
                                        });

                                    } else {
                                        View viewIn = inflater.inflate(R.layout.item_book_member, null);
                                        ImageView ivAvatar = (ImageView) viewIn.findViewById(R.id.iv_avatar);
                                        TextView tvName = (TextView) viewIn.findViewById(R.id.tv_name);
                                        TextView tvProject = (TextView) viewIn.findViewById(R.id.tv_project);
                                        ImageView ivResult = (ImageView) viewIn.findViewById(R.id.iv_result);

                                        final ResultBook bean = dataList.get(0);

                                        if (dataList.size() > 1) {
                                            tvProject.setVisibility(View.VISIBLE);
                                            TextViewUtils.setTextOrEmpty(tvProject, "当前预约有" + listOne.get(i).get(j).size() + "人");
                                            child.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    startActivity(CurrentAppointmentActivity.startCurrentAppointmentActivity(mContext, dataList));
                                                }
                                            });

                                            for (int k = 0; k < dataList.size(); k++) {
                                                if (dataList.get(k).status == ResultBook.AppointmentType.AGREE) {
                                                    ivResult.setImageDrawable(ResoureUtils.getDrawable(mContext, ResultBook.AppointmentType.AGREE.imgId));
                                                    break;
                                                } else {
                                                    ivResult.setImageDrawable(ResoureUtils.getDrawable(mContext, ResultBook.AppointmentType.REVIEWING.imgId));
                                                }
                                            }

                                        } else {
                                            tvProject.setVisibility(View.VISIBLE);
                                            TextViewUtils.setTextOrEmpty(tvProject, "当前预约有" + listOne.get(i).get(j).size() + "人");
                                            child.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    startActivity(AppointmentDetailActivity.startAppointmentDetailActivity(mContext, bean));
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

                                    if (i == 0) {
                                        containerLeft.addView(child);
                                    } else {
                                        containerRight.addView(child);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onNormalError(ApiException ex) {
                super.onNormalError(ex);
                showToast(ex.message);
            }

            @Override
            public void onNoNetWorkError() {
                super.onNoNetWorkError();
                showToast(getString(R.string.error_no_network));
            }
        });
    }

    private List<List<List<ResultBook>>> setData(AppointmentBean appointmentBean, int col) {

        String[] startAry = appointmentBean.appointment_start.split(":");
        String[] endAry = appointmentBean.appointment_end.split(":");

        List<List<List<ResultBook>>> listOne = new ArrayList<>();

        for (int i = 0; i < 2; i++) {

            AppointmentBean.AppointmentListBean outBean = appointmentBean.appointment_list.get(0);

            List<List<ResultBook>> listTwo = new ArrayList<>();

            for (int l = 0; l < col; l++) {

                List<ResultBook> listThree = new ArrayList<>();

                for (int j = 0; j < outBean.reservationBookRspList.size(); j++) {

                    if(outBean.reservationBookRspList.get(j).expired == 0 && outBean.reservationBookRspList.get(j).status == REVIEWING){

                    }else if(outBean.reservationBookRspList.get(j).status == CANCEL){

                    }else{
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

                        if (i == 0) {
                            if (outBean.reservationBookRspList.get(j).status == ResultBook.AppointmentType.REVIEWING) {
                                if (l >= topMarginCount && l < duration + topMarginCount) {
                                    AppointmentBean.AppointmentListBean.ReservationBookRspListBean bean = outBean.reservationBookRspList.get(j);
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
                                    listThree.add(book);
                                }
                            }
                        } else {
                            if (outBean.reservationBookRspList.get(j).status == ResultBook.AppointmentType.AGREE) {
                                if (l >= topMarginCount && l < duration + topMarginCount) {
                                    AppointmentBean.AppointmentListBean.ReservationBookRspListBean bean = outBean.reservationBookRspList.get(j);
                                    ResultBook book = new ResultBook();
                                    book.memberName = bean.member_name;
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
                                    listThree.add(book);
                                }
                            }

                        }
                    }

                }

                listTwo.add(listThree);
            }

            listOne.add(listTwo);

        }

        return listOne;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNotifyEvent(NotifyEvent notifyEvent) {
        if (notifyEvent.tag == NotifyEvent.APPOINTMENT) {
            getData(DateTimeUtils.parseToStamp(date, DateTimeUtils.FORMAT_DATE_UI_TWO), DateTimeUtils.parseToStamp(date, DateTimeUtils.FORMAT_DATE_UI_TWO));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EvenManager.unregister(this);
    }

}
