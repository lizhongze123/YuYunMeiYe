package cn.yuyun.yymy.ui.home.analysis;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.lzz.utils.StringFormatUtils;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.http.result.ResultAnalysisTotal;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author lzz
 * @desc
 * @date 2018-01-25
 */
public class AnalysisDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE = -1;

    private int RESOURCE_ID_EMPTY = R.layout.item_list_empty;
    private int RESOURCE_ID = R.layout.item_analysis_detail;
    private Context context;
    private List<ResultAnalysisTotal> dataList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;
    int type;

    public AnalysisDetailAdapter(Context context) {
        this.context = context;
    }

    public void notifyDataSetChanged(List<ResultAnalysisTotal> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addAll(List<ResultAnalysisTotal> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == -1) {
            View emptyView = LayoutInflater.from(context).inflate(RESOURCE_ID_EMPTY, parent, false);
            return new EmptyHolder(emptyView);
        } else {
            View rootView = LayoutInflater.from(context).inflate(RESOURCE_ID, parent, false);
            return new ViewHolder(rootView, onItemClickListener);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.bindItem(dataList.get(position), position);
        } else {
            EmptyHolder emptyHolder = (EmptyHolder) holder;
            emptyHolder.bindItem();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (dataList.size() == 0) {
            return VIEW_TYPE;
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return dataList.size() > 0 ? dataList.size() : 1;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private OnMyItemClickListener onItemClickListener;
        @BindView(R.id.iv_avatar)
        CircleImageView ivAvatar;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.ll_user)
        LinearLayout llUser;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_typeDesc)
        TextView tvTypeDesc;
        @BindView(R.id.ll_top)
        LinearLayout llTop;
        @BindView(R.id.tv_type)
        TextView tvType;
        @BindView(R.id.tv_goodsName)
        TextView tvGoodsName;
        @BindView(R.id.ll_middle)
        RelativeLayout llMiddle;
        @BindView(R.id.tv_consumePrice)
        TextView tvConsumePrice;
        @BindView(R.id.ll_bottom)
        LinearLayout llBottom;
        @BindView(R.id.tv_totalPrice)
        TextView tvTotalPrice;
        @BindView(R.id.ll_store)
        LinearLayout llStore;
        @BindView(R.id.rl)
        RelativeLayout rl;

        ViewHolder(View view, OnMyItemClickListener onItemClickListener) {
            super(view);
            this.onItemClickListener = onItemClickListener;
            ButterKnife.bind(this, view);
        }

        private void bindItem(final ResultAnalysisTotal bean, final int position) {
            TextViewUtils.setTextOrEmpty(tvName, bean.member_name);
            TextViewUtils.setTextOrEmpty(tvTime, bean.consume_time);
            TextViewUtils.setTextOrEmpty(tvTypeDesc, bean.consume_type);
            TextViewUtils.setTextOrEmpty(tvType, "商品类型:" + bean.asset_type_name);
            TextViewUtils.setTextOrEmpty(tvGoodsName, "商品名称:" + bean.good_name);
            TextViewUtils.setTextOrEmpty(tvConsumePrice, "总计消耗:" + StringFormatUtils.formatTwoDecimal(bean.total_consume_amount));
            TextViewUtils.setTextOrEmpty(tvTotalPrice, "总计金额:" + StringFormatUtils.formatTwoDecimal(bean.total_pay_amount));
        }
    }

    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnMyItemClickListener {
        void onItemClick(ResultAnalysisTotal bean, int position);
    }

    class EmptyHolder extends RecyclerView.ViewHolder {

        EmptyHolder(View view) {
            super(view);
        }

        private void bindItem() {

        }
    }

}