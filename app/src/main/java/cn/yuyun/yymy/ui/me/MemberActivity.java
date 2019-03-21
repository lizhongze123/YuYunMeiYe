package cn.yuyun.yymy.ui.me;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.ui.me.adapter.MemberAdapter;
import cn.yuyun.yymy.ui.me.entity.MemberBean;
import cn.yuyun.yymy.view.TitleBar;

import static cn.yuyun.yymy.constan.Constans.EXTRA_TYPE;

/**
 * @author 新增会员/会员失客
 * @desc
 * @date 2018/1/17
 */

public class MemberActivity extends BaseActivity {

    public String MEMBER_TYPE = "";
    public static final String MEMBER_NEW = "新增会员";
    public static final String MEMBER_LOST = "会员失客";

    private RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private List<MemberBean> dataList = new ArrayList<>();

    public static Intent startNewMemberActivity(Context context) {
        return new Intent(context, MemberActivity.class).putExtra(EXTRA_TYPE, MEMBER_NEW);
    }
    public static Intent startLostMemberActivity(Context context) {
        return new Intent(context, MemberActivity.class).putExtra(EXTRA_TYPE, MEMBER_LOST);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

    }

    @Override
    protected void setUpViewAndData() {
        initViews();
    }

    private void initViews() {
        MEMBER_TYPE = getIntent().getStringExtra("EXTRA_TYPE");
        titleBar.setTilte(MEMBER_TYPE);
        testData();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        MemberAdapter mAdapter = new MemberAdapter(this, dataList);
        mAdapter.setOnItemClickListener(new MemberAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
            }
        });
        recyclerView.setAdapter(mAdapter);
    }

    private void testData() {
        for (int i = 0; i < 10; i++) {
            MemberBean bean = new MemberBean();
            bean.memberName = "会员" + i;
            bean.lastTime = "2018年01月18日 12:35";
            dataList.add(bean);
        }
    }


}
