package cn.yuyun.yymy.ui.home.member.asset;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import cn.yuyun.yymy.http.result.ResultAssetDetail.MemberAssetsConsumeBean;
import cn.yuyun.yymy.http.result.ResultBillManagerTypeDetail.BillAllInfoBean;
import cn.yuyun.yymy.view.ShadowDrawable;

/**
 * @author
 * @desc
 * @date
 */
public class AssetConsumeAdapter extends RecyclerView.Adapter<AssetConsumeAdapter.ViewHolder> {

    private int RESOURCE_ID = R.layout.item_asset_consume;
    private Context mContext;
    private List<MemberAssetsConsumeBean> dataList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;

    public AssetConsumeAdapter(Context context) {
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

    public void notifyDataSetChanged(List<MemberAssetsConsumeBean> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addAll(List<MemberAssetsConsumeBean> dataList) {
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
        @BindView(R.id.rv)
        RecyclerView rv;
        @BindView(R.id.ll_more)
        LinearLayout llMore;
        @BindView(R.id.iv_expand)
        ImageView ivExpand;
        AssetConsumeAssetAdapter adapter;
        LinearLayoutManager linearLayoutManager;

        ViewHolder(View v, OnMyItemClickListener onItemClickListener) {
            super(v);
            ButterKnife.bind(this, v);
            linearLayoutManager = new LinearLayoutManager(mContext);
            rv.setLayoutManager(linearLayoutManager);
            adapter = new AssetConsumeAssetAdapter(mContext);
            rv.setAdapter(adapter);
        }

        private void bindItem(final MemberAssetsConsumeBean bean, final int position) {
            TextViewUtils.setText(tvOne, "消耗门店：", bean.cashier_sp_name);
            tvTwo.setText("消耗次数：" + bean.consume_num_now);
            tvThree.setText("消耗金额：" + StringFormatUtils.formatDecimal(bean.consume_amount_now));
            tvFour.setText("消耗日期：" + bean.consume_time);
            tvFive.setText("消耗时长：" + bean.consume_time_now + "分钟");
            if(TextUtils.isEmpty( bean.description)) {
                tvSix.setText("备注：无");
            }else{
                tvSix.setText("备注：" + bean.description);
            }
            ShadowDrawable.setShadowDrawable(rl, Color.parseColor("#ffffff"), DensityUtils.dip2px(mContext, 6),
                    Color.parseColor("#22000000"),
                    DensityUtils.dip2px(mContext, 3), 0, 0);
            if(null != bean.consumptionBillsConsumeRspList){
                adapter.notifyDataSetChanged(bean.consumptionBillsConsumeRspList);
                llMore.setVisibility(View.VISIBLE);
                llMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(rv.getVisibility() == View.VISIBLE){
                            rv.setVisibility(View.GONE);
                            ivExpand.setRotation(360);
                        }else{
                            ivExpand.setRotation(180);
                            rv.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }else{
                llMore.setVisibility(View.GONE);
            }
        }

    }

    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnMyItemClickListener {
        void onItemClick(View view, BillAllInfoBean bean, int position);
    }

}
