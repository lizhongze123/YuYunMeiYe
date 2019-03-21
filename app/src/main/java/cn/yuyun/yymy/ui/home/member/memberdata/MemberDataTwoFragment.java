package cn.yuyun.yymy.ui.home.member.memberdata;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.http.ApiException;
import com.example.http.ProgressSubscriber;

import java.util.ArrayList;
import java.util.List;

import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseNoTitleFragment;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestLabel;
import cn.yuyun.yymy.http.result.ResultLabel;
import cn.yuyun.yymy.http.result.ResultMemberBean;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;
import static cn.yuyun.yymy.constan.Constans.EXTRA_TYPE;
import static cn.yuyun.yymy.ui.home.member.memberdata.MemberDetailActivity.TYPE_PUBLIC;

/**
 * @author 详细资料-会员标签Fragment
 * @desc
 * @date 2018/1/15
 */
public class MemberDataTwoFragment extends BaseNoTitleFragment {

    private ResultMemberBean memberBean;
    private String type;
    private LabelAdapter mAdapter;
    private RecyclerView rv;
    private EditText etContent;
    private Button btnAdd;
    private InputMethodManager inputMethodManager;

    public static MemberDataTwoFragment newInstance(ResultMemberBean memberBean, String type) {
        MemberDataTwoFragment fragment = new MemberDataTwoFragment();
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_DATA, memberBean);
        args.putSerializable(EXTRA_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initBundle(Bundle bundle) {
        super.initBundle(bundle);
        if (getArguments() != null) {
            memberBean = (ResultMemberBean) getArguments().getSerializable(EXTRA_DATA);
            type = getArguments().getString(EXTRA_TYPE);
        }
    }

    @Override
    protected void onBindViewBefore(View root) {
        super.onBindViewBefore(root);
        mContext = getContext();
    }

    @Override
    protected int getTheLayoutId() {
        return R.layout.fragment_label;
    }

    @Override
    protected void initViews(View root) {
        super.initViews(root);
        if(null == type){
            inputMethodManager = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN |
                    WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }else if(type.equals(TYPE_PUBLIC)){
            root.findViewById(R.id.re_edittext).setVisibility(View.GONE);
        }
        rv = (RecyclerView) root.findViewById(R.id.recyclerView);
        rv.setLayoutManager(new GridLayoutManager(mContext, 2));
        mAdapter = new LabelAdapter(mContext);
        mAdapter.setOnDelListener(new LabelAdapter.OnDelListener() {
            @Override
            public void onDel(int id, int position) {
//                delMemberLabel(id, position);
            }
        });
        rv.setAdapter(mAdapter);
        etContent = (EditText) root.findViewById(R.id.et_content);
        btnAdd = (Button) root.findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(etContent.getText().toString().trim())){
                    showToast("请输入内容");
                    return;
                }
                hideInputMenu();
                addMemberLabel(etContent.getText().toString().trim());
            }
        });
    }

    public void showInputMenu() {
        etContent.requestFocus();
        etContent.postDelayed(new Runnable() {
            @Override
            public void run() {
                inputMethodManager.showSoftInput(etContent, 0);
            }
        }, 400);

    }

    public void hideInputMenu() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }, 300);

    }

    @Override
    protected void initData() {
        getMemberLabel();
    }

    private void getMemberLabel() {
       /* AppModelFactory.model().getMemberLabel(2, memberBean.memberId, new ProgressSubscriber<List<ResultLabel>>(mContext) {

            @Override
            public void onNext(List<ResultLabel> result) {
                if (result != null) {
                    mAdapter.notifyDataSetChanged(result);
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
        });*/
    }

    private void addMemberLabel(String content) {
        RequestLabel requestLabel = new RequestLabel();
        requestLabel.kv_type = 2;
        requestLabel.kv_key = "";
        requestLabel.kv_value = content;
        requestLabel.sm_id = memberBean.memberId;
        AppModelFactory.model().addMemberLabel(requestLabel, new ProgressSubscriber<Object>(mContext) {

            @Override
            public void onNext(Object result) {
                getMemberLabel();
                etContent.setText("");
                showToast("添加成功");
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
                showToast("添加失败，请重试");
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
            }
        });
    }



}

