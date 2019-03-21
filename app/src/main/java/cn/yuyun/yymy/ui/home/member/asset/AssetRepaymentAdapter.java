package cn.yuyun.yymy.ui.home.member.asset;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.lzz.utils.DensityUtils;
import cn.lzz.utils.StringFormatUtils;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.http.result.ResultAssetDetail.MemberAssetsRepaymentBean;
import cn.yuyun.yymy.http.result.ResultBillManagerTypeDetail.BillAllInfoBean;
import cn.yuyun.yymy.view.ShadowDrawable;

/**
 * @author
 * @desc
 * @date
 */
public class AssetRepaymentAdapter extends RecyclerView.Adapter<AssetRepaymentAdapter.ViewHolder> {

    private int RESOURCE_ID = R.layout.item_asset_repayment;
    private Context mContext;
    private List<MemberAssetsRepaymentBean> dataList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;

    public AssetRepaymentAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(RESOURCE_ID, parent, false);
        return new ViewHolder(rootView, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindItem(dataList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void notifyDataSetChanged(List<MemberAssetsRepaymentBean> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addAll(List<MemberAssetsRepaymentBean> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void clear() {
        this.dataList.clear();
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_one)
        TextView tvOne;
        @BindView(R.id.tv_two)
        TextView tvTwo;
        @BindView(R.id.tv_three)
        TextView tvThree;
        @BindView(R.id.tv_four)
        TextView tvFour;
        @BindView(R.id.rl)
        RelativeLayout rl;

        ViewHolder(View v, OnMyItemClickListener onItemClickListener) {
            super(v);
            ButterKnife.bind(this, v);
        }

        private void bindItem(final MemberAssetsRepaymentBean bean, final int position) {
            TextViewUtils.setText(tvOne, "还款门店：", bean.cashier_sp_name);
            tvTwo.setText("还款日期：" + bean.consume_time);
            tvThree.setText("还款金额：" + StringFormatUtils.formatDecimal(bean.amount_realpay));
            if(TextUtils.isEmpty( bean.description)) {
                tvFour.setText("备注：无");
            }else{
                tvFour.setText("备注：" + bean.description);
            }
            ShadowDrawable.setShadowDrawable(rl, Color.parseColor("#ffffff"), DensityUtils.dip2px(mContext, 6),
                    Color.parseColor("#22000000"),
                    DensityUtils.dip2px(mContext, 3), 0, 0);
        }

    }

    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnMyItemClickListener {
        void onItemClick(View view, BillAllInfoBean bean, int position);
    }

}
