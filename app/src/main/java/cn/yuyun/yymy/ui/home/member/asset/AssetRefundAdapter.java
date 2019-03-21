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
import cn.yuyun.yymy.http.result.ResultAssetDetail.MemberAssetsRefundBean;
import cn.yuyun.yymy.http.result.ResultBillManagerTypeDetail.BillAllInfoBean;
import cn.yuyun.yymy.view.ShadowDrawable;

/**
 * @author
 * @desc
 * @date
 */
public class AssetRefundAdapter extends RecyclerView.Adapter<AssetRefundAdapter.ViewHolder> {

    private int RESOURCE_ID = R.layout.item_asset_refund;
    private Context mContext;
    private List<MemberAssetsRefundBean> dataList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;

    public AssetRefundAdapter(Context context) {
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

    public void notifyDataSetChanged(List<MemberAssetsRefundBean> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addAll(List<MemberAssetsRefundBean> dataList) {
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
        @BindView(R.id.tv_five)
        TextView tvFive;
        @BindView(R.id.tv_six)
        TextView tvSix;
        @BindView(R.id.rl)
        RelativeLayout rl;

        ViewHolder(View v, OnMyItemClickListener onItemClickListener) {
            super(v);
            ButterKnife.bind(this, v);
        }

        private void bindItem(final MemberAssetsRefundBean bean, final int position) {
            TextViewUtils.setText(tvOne, "退款门店：", bean.cashier_sp_name);
            tvTwo.setText("退款日期：" + bean.create_time);
            tvThree.setText("退款金额：" + StringFormatUtils.formatDecimal(bean.refund_amount_realpay) + "元");
            tvFour.setText("退至储值：" + StringFormatUtils.formatDecimal(bean.refund_amount_to_storedvalue) + "元");
            tvFive.setText("退款次数：" + bean.refund_times + "次");
            if(TextUtils.isEmpty( bean.description)) {
                tvSix.setText("备注：无");
            }else{
                tvSix.setText("备注：" + bean.description);
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
