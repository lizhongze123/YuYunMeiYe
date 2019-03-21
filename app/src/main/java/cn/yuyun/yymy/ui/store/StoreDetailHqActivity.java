package cn.yuyun.yymy.ui.store;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.githang.statusbar.StatusBarCompat;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseNoTitleActivity;
import cn.yuyun.yymy.ui.home.analysis.StoreBean;
import cn.yuyun.yymy.ui.store.attendance.AttendanceStatisticsActivity;
import cn.yuyun.yymy.ui.store.report.ReportHqOutputActivity;
import cn.yuyun.yymy.ui.store.report.StaffAnalysisActivity;
import cn.yuyun.yymy.ui.store.report.StockActivity;
import cn.yuyun.yymy.ui.store.staff.StoreStaffListHqActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;
import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA2;
import static cn.yuyun.yymy.constan.Constans.EXTRA_TYPE;

/**
 * @author
 * @desc 门店详情 -- 总部
 * @date
 */

public class StoreDetailHqActivity extends BaseNoTitleActivity {

    @BindView(R.id.iv_avatar)
    CircleImageView ivAvatar;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_charge)
    TextView tvCharge;
    @BindView(R.id.tv_mobile)
    TextView tvMobile;
    private String groupId;
    private List<String> tempSpIdList;
    private StoreBean storeBean;

    public static Intent startStoreDetailActivity(Context context, List<String> tempSpIdList, String groupId) {
        return new Intent(context, StoreDetailHqActivity.class).putExtra(EXTRA_TYPE, groupId).putExtra(EXTRA_DATA, (Serializable) tempSpIdList);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.title_mine));
        setContentView(R.layout.activity_store_hq_detail);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
    }

    private void initDatas() {

    }

    private void initViews() {
        titleBar.setTilte("门店详情");
        groupId = getIntent().getStringExtra(EXTRA_TYPE);
        tempSpIdList = (List<String>) getIntent().getSerializableExtra(EXTRA_DATA);
        TextViewUtils.setTextOrEmpty(tvCharge, "负责人:无");
        TextViewUtils.setTextOrEmpty(tvMobile,"未填写门店地址");
        TextViewUtils.setTextOrEmpty(tvName, "总部");
        storeBean = new StoreBean();
        storeBean.spId = groupId;
        storeBean.ogType = 1;
        storeBean.spName = "总部";
        storeBean.group_id = groupId;
    }

    @OnClick({R.id.iv_back, R.id.ll_staff, R.id.ll_attendance, R.id.ll_staffAnalysis, R.id.ll_stock, R.id.ll_hqOutput})
    public void onClickItem(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_staff:
                //本店员工
                startActivity(StoreStaffListHqActivity.storeStaffListHqActivity(mContext, groupId));
                break;
            case R.id.ll_attendance:
                //考勤汇总
                startActivity(AttendanceStatisticsActivity.startAttendanceStatisticsActivity(mContext, storeBean));
                break;
            case R.id.ll_staffAnalysis:
                //员工业绩表
                startActivity(StaffAnalysisActivity.startStaffAnalysisActivity(mContext, storeBean));
                break;
            case R.id.ll_hqOutput:
                //总部出库表
                startActivity(ReportHqOutputActivity.startReportHqOutputActivity(mContext, tempSpIdList, storeBean));
                break;
            case R.id.ll_stock:
                //库存管理
                startActivity(StockActivity.startStockActivity(mContext, storeBean));
                break;
            default:
        }
    }


}
