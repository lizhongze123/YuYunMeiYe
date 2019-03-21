package cn.yuyun.yymy.ui.home.member;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.http.result.ResultDepositAsset;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;

/**
 * @author 会员资产-寄存详情
 * @desc
 * @date 2018/1/15
 */

public class AssetDepositDetailActivity extends BaseActivity {

    @BindView(R.id.tv_one)
    TextView tvOne;
    @BindView(R.id.tv_two)
    TextView tvTwo;
    @BindView(R.id.tv_three)
    TextView tvThree;
    @BindView(R.id.tv_four)
    TextView tvFour;
    @BindView(R.id.tv_five)
    TextView tvFive;
    @BindView(R.id.tv_six)
    TextView tvSix;
    @BindView(R.id.tv_seven)
    TextView tvSeven;
    @BindView(R.id.tv_twelve)
    TextView tvTwelve;
    @BindView(R.id.tv_result)
    TextView tvResult;
    private ResultDepositAsset bean;

    public static Intent startAssetDepositDetailActivity(Context context, ResultDepositAsset bean) {
        return new Intent(context, AssetDepositDetailActivity.class).putExtra(EXTRA_DATA, bean);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
        initDatas();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset_deposit_detail);
    }

    private void initViews() {
        titleBar.setTilte("寄存详情");
        bean = (ResultDepositAsset) getIntent().getSerializableExtra(EXTRA_DATA);
    }

    private void initDatas() {

        TextViewUtils.setTextOrEmpty(tvOne, bean.depositId);
        TextViewUtils.setTextOrEmpty(tvTwo, bean.memberName);
        TextViewUtils.setTextOrEmpty(tvThree, bean.name);
        TextViewUtils.setTextOrEmpty(tvFour, bean.type.desc);
        TextViewUtils.setTextOrEmpty(tvFive, bean.createTime);
        TextViewUtils.setTextOrEmpty(tvSix, bean.fetchTime);
        TextViewUtils.setTextOrEmpty(tvSeven, bean.modifyTime);
        TextViewUtils.setTextOrEmpty(tvTwelve, bean.description);
        tvResult.setText(bean.status.desc);
    }

}
