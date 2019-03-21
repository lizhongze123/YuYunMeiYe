package cn.yuyun.yymy.ui.home.other;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.example.http.ApiException;
import com.example.http.DataBean;
import com.example.http.NoneProgressSubscriber;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.lzz.utils.ResoureUtils;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseNoTitleActivity;
import cn.yuyun.yymy.db.RecordSQLiteOpenHelper;
import cn.yuyun.yymy.db.RecordsDao;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestGlobalSearch;
import cn.yuyun.yymy.http.result.ResultClassfiyStoreBean;
import cn.yuyun.yymy.http.result.ResultGlobalSearchMember;
import cn.yuyun.yymy.http.result.ResultGlobalSearchStaff;
import cn.yuyun.yymy.http.result.ResultGlobalSearchStore;
import cn.yuyun.yymy.http.result.ResultMemberBean;
import cn.yuyun.yymy.http.result.StaffBean;
import cn.yuyun.yymy.sp.LoginInfoPrefs;
import cn.yuyun.yymy.sp.UserfoPrefs;
import cn.yuyun.yymy.ui.home.analysis.StoreBean;
import cn.yuyun.yymy.ui.home.attendance.SearchLocationActivity2;
import cn.yuyun.yymy.ui.home.member.memberdata.MemberDetailActivity;
import cn.yuyun.yymy.ui.store.StaffDetailActivity;
import cn.yuyun.yymy.ui.store.StoreChildAdapter;
import cn.yuyun.yymy.ui.store.StoreDetailActivity;
import cn.yuyun.yymy.ui.store.StoreStaffAdapter;
import cn.yuyun.yymy.view.FlowLayout;

/**
 * @author
 * @desc 全局搜索
 * @date
 */

public class SearchGlobalActivity extends BaseNoTitleActivity implements RadioGroup.OnCheckedChangeListener {


    @BindView(R.id.keyWord)
    EditText etInput;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.rl_top)
    RelativeLayout rlTop;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.rb1)
    RadioButton rb1;
    @BindView(R.id.rb2)
    RadioButton rb2;
    @BindView(R.id.rb3)
    RadioButton rb3;
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;
    @BindView(R.id.iv_del)
    ImageView ivDel;
    @BindView(R.id.ll_record)
    LinearLayout llRecord;
    @BindView(R.id.rl_content)
    RelativeLayout rlContent;
    @BindView(R.id.fl_store)
    FlowLayout flStore;
    @BindView(R.id.fl_staff)
    FlowLayout flStaff;
    @BindView(R.id.fl_member)
    FlowLayout flMember;
    @BindView(R.id.ll_empty)
    LinearLayout llEmpty;

    private RequestGlobalSearch requestBody;

    private StoreChildAdapter storeChildAdapter;
    private SearchMemberListAdapter searchMemberListAdapter;
    private StoreStaffAdapter storeStaffAdapter;

    private RecordsDao recordsDao;
    private LayoutInflater mInflater;

    private List<String> storeList;
    private List<String> storeTempList;
    private List<String> staffList;
    private List<String> staffTempList;
    private List<String> memberList;
    private List<String> memberTempList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_global);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
        initData();
    }

    private void initData() {
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        requestBody = new RequestGlobalSearch();
        recordsDao = new RecordsDao(this);
        storeList = new ArrayList<>();
        storeTempList = new ArrayList<>();
        staffList = new ArrayList<>();
        staffTempList = new ArrayList<>();
        memberList = new ArrayList<>();
        memberTempList = new ArrayList<>();
        storeTempList.addAll(recordsDao.getRecordsList(RecordSQLiteOpenHelper.TABLE_STORE));
        staffTempList.addAll(recordsDao.getRecordsList(RecordSQLiteOpenHelper.TABLE_STAFF));
        memberTempList.addAll(recordsDao.getRecordsList(RecordSQLiteOpenHelper.TABLE_MEMBER));
        reversedList(storeTempList, storeList);
        reversedList(staffTempList, staffList);
        reversedList(memberTempList, memberList);
        //第一次进入判断数据库中是否有历史记录，没有则不显示
        checkRecordsSize(flStore, storeList);
        checkRecordsSize(flStaff, staffList);
        checkRecordsSize(flMember, memberList);
    }

    private void initViews() {
        etInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (!IsEmptyOrNullString(etInput.getText().toString())) {
                        recyclerView.setVisibility(View.VISIBLE);
                        rlContent.setVisibility(View.GONE);
                        //请求接口搜索
                        requestBody.search_text = etInput.getText().toString();
                        if (requestBody.serviceplaces == 1) {
                            globalSearchStore();
                        } else if (requestBody.member == 1) {
                            globalSearchMember();
                        } else if (requestBody.staff == 1) {
                            globalSearchStaff();
                        }
                    } else {
                        recyclerView.setVisibility(View.GONE);
                        rlContent.setVisibility(View.VISIBLE);
                        llEmpty.setVisibility(View.GONE);
                    }
                }
                return false;
            }
        });
        etInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(TextUtils.isEmpty(s.toString())){
                    recyclerView.setVisibility(View.GONE);
                    rlContent.setVisibility(View.VISIBLE);
                    llEmpty.setVisibility(View.GONE);
                }
            }
        });
        findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        findViewById(R.id.tv_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeTempList.clear();
                staffTempList.clear();
                memberTempList.clear();
                reversedList(storeTempList, storeList);
                reversedList(staffTempList, staffList);
                reversedList(memberTempList, memberList);
                recordsDao.deleteAllRecords(RecordSQLiteOpenHelper.TABLE_STORE);
                recordsDao.deleteAllRecords(RecordSQLiteOpenHelper.TABLE_STAFF);
                recordsDao.deleteAllRecords(RecordSQLiteOpenHelper.TABLE_MEMBER);
                flStore.removeAllViewsInLayout();
                flStaff.removeAllViewsInLayout();
                flMember.removeAllViewsInLayout();
            }
        });
        ((RadioGroup) findViewById(R.id.radioGroup)).setOnCheckedChangeListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        storeChildAdapter = new StoreChildAdapter(mContext);
        searchMemberListAdapter = new SearchMemberListAdapter(mContext);
        storeStaffAdapter = new StoreStaffAdapter(mContext);
        recyclerView.setAdapter(storeChildAdapter);

        storeChildAdapter.setOnItemClickListener(new StoreChildAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(ResultClassfiyStoreBean.OgServiceplacesRspListBean bean, int pos) {
                if (!TextUtils.isEmpty(bean.sp_name)) {
                    //判断数据库中是否存在该记录
                    if (!recordsDao.isHasRecord(RecordSQLiteOpenHelper.TABLE_STORE, bean.sp_name)) {
                        storeTempList.add(bean.sp_name);
                    }
                    //将搜索记录保存至数据库中
                    recordsDao.addRecords(RecordSQLiteOpenHelper.TABLE_STORE, bean.sp_name);
                    reversedList(storeTempList, storeList);
                    checkRecordsSize(flStore, storeList);
                }
                StoreBean storeBean = new StoreBean();
                storeBean.spId = bean.sp_id;
                storeBean.group_id = bean.group_id;
                storeBean.spName = bean.sp_name;
                storeBean.thumb_url = bean.thumb_url;
                storeBean.chairmantel = bean.chairmantel;
                storeBean.chairman = bean.chairman;
                startActivity(StoreDetailActivity.startStoreDetailActivity(mContext, storeBean));
            }
        });
        searchMemberListAdapter.setOnItemClickListener(new SearchMemberListAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(View view, ResultMemberBean bean, int position) {
                if (!TextUtils.isEmpty(bean.memberName)) {
                    //判断数据库中是否存在该记录
                    if (!recordsDao.isHasRecord(RecordSQLiteOpenHelper.TABLE_MEMBER, bean.memberName)) {
                        memberTempList.add(bean.memberName);
                    }
                    //将搜索记录保存至数据库中
                    recordsDao.addRecords(RecordSQLiteOpenHelper.TABLE_MEMBER, bean.memberName);
                    reversedList(memberTempList, memberList);
                    checkRecordsSize(flMember, memberList);
                }
                StoreBean storeBean = new StoreBean();
                storeBean.spId = UserfoPrefs.getInstance(mContext).getOgId();
                storeBean.group_id = LoginInfoPrefs.getInstance(mContext).getGroupId();
                startActivity(MemberDetailActivity.startMemberDetailActivityWithId(mContext, bean.memberId, storeBean));
            }
        });
        storeStaffAdapter.setOnItemClickListener(new StoreStaffAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(View view, StaffBean bean, int position) {
                //保存搜索记录到数据库
                if (!TextUtils.isEmpty(bean.staffName)) {
                    //判断数据库中是否存在该记录
                    if (!recordsDao.isHasRecord(RecordSQLiteOpenHelper.TABLE_STAFF, bean.staffName)) {
                        staffTempList.add(bean.staffName);
                    }
                    //将搜索记录保存至数据库中
                    recordsDao.addRecords(RecordSQLiteOpenHelper.TABLE_STAFF, bean.staffName);
                    reversedList(staffTempList, staffList);
                    checkRecordsSize(flStaff, staffList);
                }
                StoreBean storeBean = new StoreBean();
                storeBean.spId = UserfoPrefs.getInstance(mContext).getOgId();
                storeBean.group_id = LoginInfoPrefs.getInstance(mContext).getGroupId();
                startActivity(StaffDetailActivity.startStaffDetailActivity(mContext, bean, storeBean));
            }
        });
    }


    private void setHistoryLayout(FlowLayout flowlayout, List<String> searchRecordsList) {
        flowlayout.removeAllViewsInLayout();
        for (int i = 0; i < searchRecordsList.size(); i++) {
            RelativeLayout rv = (RelativeLayout) mInflater.inflate(R.layout.item_search_record, flowlayout, false);
            final TextView tv = (TextView) rv.findViewById(R.id.tv_label);
            tv.setText(searchRecordsList.get(i));
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    etInput.setText(tv.getText().toString());
                    if (!IsEmptyOrNullString(etInput.getText().toString())) {
                        recyclerView.setVisibility(View.VISIBLE);
                        rlContent.setVisibility(View.GONE);
                        //请求接口搜索
                        requestBody.search_text = etInput.getText().toString();
                        if (requestBody.serviceplaces == 1) {
                            globalSearchStore();
                        } else if (requestBody.member == 1) {
                            globalSearchMember();
                        } else if (requestBody.staff == 1) {
                            globalSearchStaff();
                        }
                    } else {
                        recyclerView.setVisibility(View.GONE);
                        rlContent.setVisibility(View.VISIBLE);
                        llEmpty.setVisibility(View.GONE);
                    }
                }
            });
            flowlayout.addView(rv);
        }
    }

    /**
     * 颠倒list顺序，用户输入的信息会从上依次往下显示
     */
    private void reversedList(List<String> tempList, List<String> resultList) {
        resultList.clear();
        for (int i = tempList.size() - 1; i >= 0; i--) {
            resultList.add(tempList.get(i));
        }
    }

    /**
     * 当没有匹配的搜索数据的时候不显示历史记录栏
     */
    private void checkRecordsSize(FlowLayout flowlayout, List<String> searchRecordsList) {
        if (searchRecordsList.size() == 0) {

        } else {
            setHistoryLayout(flowlayout, searchRecordsList);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb1:
                requestBody.serviceplaces = 1;
                requestBody.staff = 0;
                requestBody.member = 0;
                recyclerView.setAdapter(storeChildAdapter);
                flStore.setVisibility(View.VISIBLE);
                flStaff.setVisibility(View.GONE);
                flMember.setVisibility(View.GONE);
                break;
            case R.id.rb2:
                requestBody.serviceplaces = 0;
                requestBody.staff = 1;
                requestBody.member = 0;
                recyclerView.setAdapter(storeStaffAdapter);
                flStore.setVisibility(View.GONE);
                flStaff.setVisibility(View.VISIBLE);
                flMember.setVisibility(View.GONE);
                break;
            case R.id.rb3:
                requestBody.serviceplaces = 0;
                requestBody.staff = 0;
                requestBody.member = 1;
                recyclerView.setAdapter(searchMemberListAdapter);
                flStore.setVisibility(View.GONE);
                flStaff.setVisibility(View.GONE);
                flMember.setVisibility(View.VISIBLE);
                break;
            default:
        }
    }


    private void globalSearchStore() {

        AppModelFactory.model().globalSearchStore(requestBody, LoginInfoPrefs.getInstance(mContext).getGroupId(), new NoneProgressSubscriber<DataBean<ResultGlobalSearchStore>>(mContext) {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(DataBean<ResultGlobalSearchStore> result) {

                if (null != result.data) {

                    List<ResultClassfiyStoreBean.OgServiceplacesRspListBean> dataList = new ArrayList<>();

                    for (int i = 0; i < result.data.serviceplaces.size(); i++) {
                        for (int j = 0; j < result.data.serviceplaces.get(i).ogServiceplacesRspList.size(); j++) {
                            dataList.add(result.data.serviceplaces.get(i).ogServiceplacesRspList.get(j));
                        }
                    }

                    if (dataList.size() == 0) {
                        llEmpty.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        llEmpty.setVisibility(View.GONE);
                        storeChildAdapter.notifyDataSetChanged(dataList);
                        recyclerView.setVisibility(View.VISIBLE);
                    }

                } else {
                    llEmpty.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
            }


        });
    }

    private void globalSearchMember() {

        AppModelFactory.model().globalSearchMember(requestBody, LoginInfoPrefs.getInstance(mContext).getGroupId(), new NoneProgressSubscriber<DataBean<ResultGlobalSearchMember>>(mContext) {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(DataBean<ResultGlobalSearchMember> result) {
                if (null != result.data) {
                    if (null != result.data.member && null != result.data.member.records) {
                        if (result.data.member.records.size() == 0) {
                            llEmpty.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        } else {
                            llEmpty.setVisibility(View.GONE);
                            searchMemberListAdapter.notifyDataSetChanged(result.data.member.records);
                            recyclerView.setVisibility(View.VISIBLE);
                        }
                    } else {
                        llEmpty.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }
                } else {
                    llEmpty.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
            }


        });
    }

    private void globalSearchStaff() {

        AppModelFactory.model().globalSearchStaff(requestBody, LoginInfoPrefs.getInstance(mContext).getGroupId(), new NoneProgressSubscriber<DataBean<ResultGlobalSearchStaff>>(mContext) {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(DataBean<ResultGlobalSearchStaff> result) {
                if (null != result.data) {
                    if (null != result.data.staff && null != result.data.staff.records) {
                        if (result.data.staff.records.size() == 0) {
                            llEmpty.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        } else {
                            llEmpty.setVisibility(View.GONE);
                            storeStaffAdapter.notifyDataSetChanged(result.data.staff.records);
                            recyclerView.setVisibility(View.VISIBLE);
                        }
                    } else {
                        llEmpty.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }
                } else {
                    llEmpty.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
            }


        });
    }

    public boolean IsEmptyOrNullString(String s) {
        return (s == null) || (s.trim().length() == 0);
    }

}
