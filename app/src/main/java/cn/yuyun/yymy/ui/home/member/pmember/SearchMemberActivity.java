package cn.yuyun.yymy.ui.home.member.pmember;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.http.PageInfo;
import com.example.http.Presenter;

import java.util.List;

import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.http.request.RequestPublicMember;
import cn.yuyun.yymy.http.result.ResultMemberBean;
import cn.yuyun.yymy.ui.home.analysis.StoreBean;
import cn.yuyun.yymy.ui.home.member.memberdata.MemberDetailActivity;
import rx.Subscriber;

/**
 * @author lzz
 * @desc 搜索会员
 * @date 2018/3/20
 */

public class SearchMemberActivity extends BaseActivity implements TextWatcher{

    private EditText editText;

    private Presenter<ResultMemberBean> presenter;
    private PublicMemberAdapter mAdapter;
    private RecyclerView recyclerView;
    private RequestPublicMember requestBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_member);

    }

    @Override
    protected void setUpViewAndData() {
        initViews();
        configurePresenter();
    }

    private void initViews() {
        requestBean = new RequestPublicMember();
        editText = (EditText) findViewById(R.id.et_search);
        editText.addTextChangedListener(this);
        editText.setHint(TextViewUtils.getIconText(mContext, R.drawable.common_icon_search, (int) editText.getTextSize(), "请输入会员姓名"));
        editText.setOnEditorActionListener(actionListener);
        titleBar.setTilte("搜索");
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        mAdapter = new PublicMemberAdapter(mContext);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new PublicMemberAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(View view, ResultMemberBean bean, int position) {
                StoreBean storeBean = new StoreBean();
                storeBean.spId = bean. memberInSpId+ "";
                storeBean.group_id = cn.yuyun.yymy.sp.LoginInfoPrefs.getInstance(mContext).getGroupId();
                startActivity(MemberDetailActivity.startMemberDetailActivityFromPublic(mContext, bean.memberId, storeBean));
            }
        });
    }


    private void configurePresenter() {
        presenter = new Presenter<>();
        presenter.setLoadDataListener(new Presenter.OnPresenterLoadListener<ResultMemberBean>() {

            @Override
            public void onSuccess(List<ResultMemberBean> result, int total, final boolean isRefresh) {
                if (result != null) {
                    mAdapter.notifyDataSetChanged(result);
                }
            }

            @Override
            public void onFailed(boolean isRefresh) {
                showToast("加载失败，请稍候重试");
            }

            @Override
            public void onCompleted(boolean isRefresh) {

            }

            @Override
            public void onEmptyData() {

            }

            @Override
            public void onNoneMoreData() {
                showToast("没有更多数据了");
            }

            @Override
            public void onLoad(Subscriber<PageInfo<ResultMemberBean>> subscriber, int pageIndex, int pageSize) {
//                AppModelFactory.model().getPublicMemberList(requestBean, LoginInfoPrefs.getInstance(mContext).getGroupId(), pageIndex, 1000, subscriber);
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.toString().length() <= 0) {
            mAdapter.clear();
            return;
        }else {
            requestBean.searchText = s.toString().trim();
            presenter.loadData(true);
        }
    }


    private TextView.OnEditorActionListener actionListener =  new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(textView.getWindowToken(),0);
            return keyEvent != null && keyEvent.getAction() == KeyEvent.ACTION_DOWN;
        }
    };
}
