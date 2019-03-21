package cn.yuyun.yymy.ui.store.staff;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.http.DataBean;
import com.example.http.PageInfo;
import com.example.http.ProgressSubscriber;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestStaffList;
import cn.yuyun.yymy.http.result.StaffBean;
import cn.yuyun.yymy.sp.LoginInfoPrefs;
import cn.yuyun.yymy.ui.home.analysis.StoreBean;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;
import static cn.yuyun.yymy.constan.Constans.EXTRA_TYPE;

/**
 * @author lzz
 * @desc 选择员工
 * @date 2018/5/21
 */
public class SelectStaffListActivity extends BaseActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private SelectStaffAdapter selectStaffAdapter;

    private StoreBean storeBean;
    private List<StaffBean> mIntentResult;

    public static Intent startSelectStaffListActivity(Context context, StoreBean bean, List<StaffBean>list) {
        return new Intent(context, SelectStaffListActivity.class).putExtra(EXTRA_DATA, bean).putExtra(EXTRA_TYPE, (Serializable) list);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_member);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
        initDatas();
    }

    private void initDatas() {
        getData();
    }

    private void initViews() {
        titleBar.setTilte("选择员工");
        titleBar.setTvRight("确定");
        titleBar.setRightOnClicker(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
        storeBean = (StoreBean) getIntent().getSerializableExtra(EXTRA_DATA);
        if(getIntent().getSerializableExtra(EXTRA_TYPE) != null){
            mIntentResult = (List<StaffBean>)getIntent().getSerializableExtra(EXTRA_TYPE);
        }else{
            mIntentResult = new ArrayList<>();
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        selectStaffAdapter = new SelectStaffAdapter(mContext, mIntentResult);
        recyclerView.setAdapter(selectStaffAdapter);
    }


    private void getData() {
        RequestStaffList body = new RequestStaffList();
        body.is_mechanic = 1;
        body.baseon_type = 2;
        body.status = 1;
        List<String>list = new ArrayList<>();
        list.add(storeBean.spId);
        body.sp_id_list = list;
        AppModelFactory.model().getMechanicInSp(LoginInfoPrefs.getInstance(this).getGroupId(), 1, Integer.MAX_VALUE, body, new ProgressSubscriber<DataBean<PageInfo<StaffBean>>>(mContext) {

            @Override
            public void onCompleted() {
                super.onCompleted();
            }

            @Override
            public void onNext(DataBean<PageInfo<StaffBean>> result) {
                if (null != result) {

                    if(null != result.data){

                        if(null != result.data.dataLsit){

                            selectStaffAdapter.addAll(result.data.dataLsit);

                        }

                    }

                }
            }
        });
    }

    public void save() {
       List<StaffBean> list =  selectStaffAdapter.getSelectedItem();
        if(list.size() == 0){
            showToast("请选择员工");
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATA, (Serializable) list);
        setResult(RESULT_OK, intent);
        finish();
    }
}
