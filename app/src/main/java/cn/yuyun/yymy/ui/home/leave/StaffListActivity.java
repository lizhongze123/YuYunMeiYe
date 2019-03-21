package cn.yuyun.yymy.ui.home.leave;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.ajguan.library.EasyRefreshLayout;
import com.example.http.ApiException;
import com.example.http.ProgressSubscriber;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.event.NotifyEvent;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestStoreStaff;
import cn.yuyun.yymy.http.result.StaffBean;
import cn.yuyun.yymy.sp.UserfoPrefs;
import cn.yuyun.yymy.utils.EvenManager;
import cn.yuyun.yymy.view.refresh.RefreshHeaderView;
import cn.yuyun.yymy.view.sidebar.PinyinComparator;
import cn.yuyun.yymy.view.sidebar.PinyinUtils;
import cn.yuyun.yymy.view.sidebar.SideBar;
import cn.yuyun.yymy.view.sidebar.SortAdapter;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;
import static cn.yuyun.yymy.constan.Constans.EXTRA_TYPE;

/**
 * @author lzz
 * @desc lzz
 * @date 2018/2/3
 */

public class StaffListActivity extends BaseActivity {

    private EasyRefreshLayout easyRefreshLayout;

    private SideBar sidebar;
    private TextView dialog;
    private ListView listView;
    private SortAdapter sortadapter;
    private List<StaffBean> data;
    private String spId;
    private static String TYPE_GROUP = "type_group";
    private static String TYPE_GROUP_CHECK = "type_group_check";
    private static String TYPE_LIST = "type_list";
    private String type = "";

    public static Intent startContactsListActivity(Context context, String spId) {
        return new Intent(context, StaffListActivity.class).putExtra(EXTRA_DATA, spId);
    }

    public static Intent startContactsListActivityFromGroup(Context context) {
        return new Intent(context, StaffListActivity.class).putExtra(EXTRA_TYPE, TYPE_GROUP);
    }

    public static Intent startContactsListActivityFromGroupWithCheck(Context context) {
        return new Intent(context, StaffListActivity.class).putExtra(EXTRA_TYPE, TYPE_GROUP_CHECK);
    }

    public static Intent startContactsListActivityWithCheck(Context context, String spId) {
        return new Intent(context, StaffListActivity.class).putExtra(EXTRA_DATA, spId).putExtra(EXTRA_TYPE,TYPE_LIST);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

    }

    @Override
    protected void setUpViewAndData() {
        spId = getIntent().getStringExtra(EXTRA_DATA);
        type = getIntent().getStringExtra(EXTRA_TYPE);
        initViews();
    }

    private void initViews() {
        titleBar.setTilte("通讯录");
        sidebar = (SideBar) findViewById(R.id.sidebar);
        listView = (ListView) findViewById(R.id.listview);
        dialog = (TextView) findViewById(R.id.dialog);
        sidebar.setTextView(dialog);
        // 设置字母导航触摸监听
        sidebar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                // 该字母首次出现的位置
                int position = sortadapter.getPositionForSelection(s.charAt(0));
                if (position != -1) {
                    listView.setSelection(position);
                }
            }
        });
        easyRefreshLayout = (EasyRefreshLayout) findViewById(R.id.easylayout);
        easyRefreshLayout.setRefreshHeadView(new RefreshHeaderView(this));
        easyRefreshLayout.autoRefresh();
        easyRefreshLayout.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {}

            @Override
            public void onRefreshing() {
                if(TYPE_GROUP.equals(type) || TYPE_GROUP_CHECK.equals(type)){
                    getGroupData();
                }else{
                    getStoreData();
                }
            }
        });

        if(TYPE_GROUP_CHECK.equals(type) || TYPE_LIST.equals(type)){

        }else{
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(data.get(position).staffName.equals(UserfoPrefs.getInstance(mContext).getStaffName())){
                        showToast("不可选择自己作为审批人");
                    }else{
                        EvenManager.sendEvent(new NotifyEvent(data.get(position), NotifyEvent.DATA));
                        onBackPressed();
                    }
                }
            });
        }

    }

    private List<StaffBean> setData(List<StaffBean> list) {
        List<StaffBean> newList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            //0和-1的员工不显示
            if(list.get(i).status != RequestStoreStaff.JobStatus.OUT && list.get(i).status != RequestStoreStaff.JobStatus.RECYCLED){
                StaffBean staffBean = list.get(i);
                String pinyin = PinyinUtils.getPingYin(staffBean.staffName);
                String fPinyin = pinyin.substring(0, 1).toUpperCase();

                staffBean.setPinYin(pinyin);
                if (fPinyin.matches("[A-Z]")) {
                    staffBean.setFirstPinYin(fPinyin);
                } else {
                    staffBean.setFirstPinYin("#");
                }
                newList.add(staffBean);
            }
        }
        return newList;

    }

    private void getGroupData() {
        AppModelFactory.model().getGroupStaffList(new ProgressSubscriber<List<StaffBean>>(mContext) {

            @Override
            public void onCompleted() {
                super.onCompleted();
                easyRefreshLayout.refreshComplete();
            }

            @Override
            public void onNext(List<StaffBean> result) {
                if(result != null){
                    if(result.size() == 0){
                        showEmpty();
                    }else{
                        // 数据在放在adapter之前需要排序
                        data = setData(result);
                        Collections.sort(data, new PinyinComparator());
                        sortadapter = new SortAdapter(mContext, data);
                        sortadapter.setOnItemClickListener(new SortAdapter.OnCallListener() {
                            @SuppressLint("MissingPermission")
                            @Override
                            public void onCallClick(String mobile, int position) {
                                if(null != mobile){
                                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+ mobile));
                                    startActivity(intent);
                                }
                            }
                        });
                        listView.setAdapter(sortadapter);
                    }
                }else{
                    showEmpty();
                }
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
                showToast(ex.message);
            }

        });
    }

    private void getStoreData() {
       /* AppModelFactory.model().getStaffList(spId, new ProgressSubscriber<List<StaffBean>>(mContext) {

            @Override
            public void onCompleted() {
                super.onCompleted();
                easyRefreshLayout.refreshComplete();
            }

            @Override
            public void onNext(List<StaffBean> result) {
                if(result != null){
                    // 数据在放在adapter之前需要排序
                    data = setData(result);
                    Collections.sort(data, new PinyinComparator());
                    sortadapter = new SortAdapter(mContext, data);
                    sortadapter.setOnItemClickListener(new SortAdapter.OnCallListener() {
                        @SuppressLint("MissingPermission")
                        @Override
                        public void onCallClick(String mobile, int position) {
                            if(null != mobile){
                                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+ mobile));
                                startActivity(intent);
                            }
                        }
                    });
                    listView.setAdapter(sortadapter);
                }
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
                showToast(ex.message);
            }

        });*/
    }

}
