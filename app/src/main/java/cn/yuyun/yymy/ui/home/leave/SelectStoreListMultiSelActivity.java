package cn.yuyun.yymy.ui.home.leave;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.example.http.ApiException;
import com.example.http.DataBean;
import com.example.http.ProgressSubscriber;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.lzz.utils.LogUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.event.NotifyEvent;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.result.ResultClassfiyStoreBean;
import cn.yuyun.yymy.http.result.ResultClassfiyStoreBean.OgServiceplacesRspListBean;
import cn.yuyun.yymy.sp.LoginInfoPrefs;
import cn.yuyun.yymy.sp.StorePeoplePrefs;
import cn.yuyun.yymy.ui.home.work.AlreadyStoreAdapter;
import cn.yuyun.yymy.utils.EvenManager;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;

/**
 * @author
 * @desc 门店列表
 * @date
 */

public class SelectStoreListMultiSelActivity extends BaseActivity {

    @BindView(R.id.rv_left)
    RecyclerView rvLeft;

    @BindView(R.id.rv_right)
    RecyclerView rvRight;
    @BindView(R.id.rv_top)
    RecyclerView rvTop;

    @BindView(R.id.et_inputStore)
    EditText etInputStore;

    private SelectStoreListMultiSelRightAdapter rightAdapter;
    private SelectStoreListLeftAdapter leftAdapter;
    private AlreadyStoreAdapter alreadyStoreAdapter;

    private List<OgServiceplacesRspListBean> intentData;
    private List<ResultClassfiyStoreBean> cacheData;

    public static Intent startMultiSelActivity(Context context, List<OgServiceplacesRspListBean> list) {
        return new Intent(context, SelectStoreListMultiSelActivity.class).putExtra(EXTRA_DATA, (Serializable) list);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_store);
        EvenManager.register(this);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
        initDatas();
    }

    private void initDatas() {
        String json = StorePeoplePrefs.getInstance(mContext).getJson();
        if (!TextUtils.isEmpty(json)) {
            Gson gson = new Gson();
            Type t = new TypeToken<List<ResultClassfiyStoreBean>>() {}.getType();
            cacheData = gson.fromJson(json, t);

            ResultClassfiyStoreBean bean = new ResultClassfiyStoreBean();
            bean.name = "总部";
            bean.group_id = LoginInfoPrefs.getInstance(mContext).getGroupId();
            ResultClassfiyStoreBean.OgServiceplacesRspListBean bean1 = new ResultClassfiyStoreBean.OgServiceplacesRspListBean();
            bean1.sp_id = LoginInfoPrefs.getInstance(mContext).getGroupId();
            bean1.group_id = LoginInfoPrefs.getInstance(mContext).getGroupId();
            bean1.sp_name = "总部";
            bean1.ogType = 1;
            List<ResultClassfiyStoreBean.OgServiceplacesRspListBean> ogServiceplacesRspList = new ArrayList<>();
            ogServiceplacesRspList.add(bean1);
            bean.ogServiceplacesRspList = ogServiceplacesRspList;
            cacheData.add(0, bean);

            leftAdapter.notifyDataSetChanged(cacheData);
            rightAdapter.notifyDataSetChanged(cacheData);
            classfiyStoreList.addAll(cacheData);
            LogUtils.e("我是读取缓存数据");
        }else {
            getClassifyStoreList();
            LogUtils.e("我是请求网络数据");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNotifyEvent(NotifyEvent notifyEvent) {
        if (notifyEvent.tag == NotifyEvent.DATA) {
            finish();
        }
    }

    private void initViews() {
        titleBar.setTilte("门店列表");
        titleBar.setTvRight("确定");
        titleBar.setRightOnClicker(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EvenManager.sendEvent(new NotifyEvent(rightAdapter.getSelectedItem(), NotifyEvent.DATA));
                onBackPressed();
            }
        });
        if(null != getIntent().getSerializableExtra(EXTRA_DATA)){
            intentData = (List<OgServiceplacesRspListBean>)getIntent().getSerializableExtra(EXTRA_DATA);
        }else{
            intentData = new ArrayList<>();
        }
        rvTop.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        alreadyStoreAdapter = new AlreadyStoreAdapter(mContext);
        rvTop.setAdapter(alreadyStoreAdapter);
        alreadyStoreAdapter.addAll(intentData);

        rvLeft.setLayoutManager(new LinearLayoutManager(this));
        rvRight.setLayoutManager(new LinearLayoutManager(this));
        rightAdapter = new SelectStoreListMultiSelRightAdapter(this, intentData);
        leftAdapter = new SelectStoreListLeftAdapter(this);
        rvLeft.setAdapter(leftAdapter);
        rvRight.setAdapter(rightAdapter);
        leftAdapter.setOnItemClickListener(new SelectStoreListLeftAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(View view, ResultClassfiyStoreBean bean, int position) {
                rightAdapter.setIndex(position);
                leftAdapter.setCheckedPosition(position);
            }
        });
        rightAdapter.setOnItemClickListener(new SelectStoreListMultiSelRightAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(View view, OgServiceplacesRspListBean bean, int position) {
                alreadyStoreAdapter.notifyDataSetChanged(rightAdapter.getSelectedItem());
                if(intentData.size() > 0){
                    rvTop.smoothScrollToPosition(intentData.size() - 1);
                }
            }
        });
        etInputStore.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() <= 0) {
                    leftAdapter.notifyDataSetChanged(classfiyStoreList);
                    rightAdapter.notifyDataSetChanged(classfiyStoreList);
                }else {
                    //本地搜索
                    localSearch(s.toString());
                }

            }
        });
    }

    /**门店数据*/
    private List<ResultClassfiyStoreBean> classfiyStoreList = new ArrayList<>();

    private void localSearch(String text) {
        //返回数据
        List<ResultClassfiyStoreBean> filterclassfiyStoreList = new ArrayList<>();
        for (ResultClassfiyStoreBean bean : classfiyStoreList) {
            ResultClassfiyStoreBean bean1 = new ResultClassfiyStoreBean();
            bean1.name = bean.name;
            bean1.group_id = bean.group_id;
            bean1.id = bean.id;
            List<ResultClassfiyStoreBean.OgServiceplacesRspListBean> ogList = new ArrayList<>();
            for (ResultClassfiyStoreBean.OgServiceplacesRspListBean ogServiceplacesRspListBean : bean.ogServiceplacesRspList) {
                if(ogServiceplacesRspListBean.sp_name.indexOf(text) >= 0){
                    ogList.add(ogServiceplacesRspListBean);
                }
            }
            if(ogList.size() > 0){
                bean1.ogServiceplacesRspList = ogList;
                filterclassfiyStoreList.add(bean1);
            }
        }
        leftAdapter.notifyDataSetChanged(filterclassfiyStoreList);
        rightAdapter.notifyDataSetChanged(filterclassfiyStoreList);
    }

    private void getClassifyStoreList() {
        AppModelFactory.model().getClassifyStoreList(LoginInfoPrefs.getInstance(this).getGroupId(), new ProgressSubscriber<DataBean<List<ResultClassfiyStoreBean>>>(mContext) {

            @Override
            public void onCompleted() {
                super.onCompleted();
            }

            @Override
            public void onNext(DataBean<List<ResultClassfiyStoreBean>> result) {
                if (null != result) {
                    if (null != result.data) {

                        //缓存门店列表数据
                        Gson gson = new Gson();
                        String json = gson.toJson(result.data);
                        StorePeoplePrefs.getInstance(mContext).saveInfo(json);

                        ResultClassfiyStoreBean bean = new ResultClassfiyStoreBean();
                        bean.name = "总部";
                        bean.group_id = LoginInfoPrefs.getInstance(mContext).getGroupId();
                        ResultClassfiyStoreBean.OgServiceplacesRspListBean bean1 = new ResultClassfiyStoreBean.OgServiceplacesRspListBean();
                        bean1.sp_id = LoginInfoPrefs.getInstance(mContext).getGroupId();
                        bean1.group_id = LoginInfoPrefs.getInstance(mContext).getGroupId();
                        bean1.sp_name = "总部";
                        bean1.ogType = 1;
                        List<ResultClassfiyStoreBean.OgServiceplacesRspListBean> ogServiceplacesRspList = new ArrayList<>();
                        ogServiceplacesRspList.add(bean1);
                        bean.ogServiceplacesRspList = ogServiceplacesRspList;
                        result.data.add(0, bean);

                        leftAdapter.notifyDataSetChanged(result.data);
                        rightAdapter.notifyDataSetChanged(result.data);
                        classfiyStoreList.addAll(result.data);

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
                showToast(mContext.getString(R.string.error_no_network));
            }

        });
    }

}
