package cn.yuyun.yymy.ui.store.report;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
import cn.yuyun.yymy.http.result.ResultStock;

/**
 * @author lzz
 * @desc
 * @date 2018-05-25
 */
public class StockAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int RESOURCE_ID = R.layout.item_stock;
    private Context context;
    private boolean isHq = false;
    private List<ResultStock> dataList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;

    public StockAdapter(Context context) {
        this.context = context;
    }

    public void notifyDataSetChanged(List<ResultStock> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addAll(List<ResultStock> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void setHq(boolean isHq) {
        this.isHq = isHq;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context).inflate(RESOURCE_ID, parent, false);
        return new ViewHolder(rootView, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.bindItem(dataList.get(position), position);
    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private StockAdapter.OnMyItemClickListener onItemClickListener;
        @BindView(R.id.ll)
        LinearLayout ll;
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
        @BindView(R.id.rl)
        RelativeLayout rl;

        ViewHolder(View view, StockAdapter.OnMyItemClickListener onItemClickListener) {
            super(view);
            ButterKnife.bind(this, view);
            this.onItemClickListener = onItemClickListener;
        }

        private void bindItem(final ResultStock bean, final int position) {
            if (position % 2 == 0) {
                rl.setBackgroundResource(R.color.white);
            } else {
                rl.setBackgroundResource(R.color.item_analysis);
            }
            ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != onItemClickListener) {
                        onItemClickListener.onItemClick(v, bean, position);
                    }
                }
            });

            TextViewUtils.setTextOrEmpty(tvOne, bean.productBrand, "无品牌");
            TextViewUtils.setTextOrEmpty(tvTwo, bean.productType, "无类别");
            TextViewUtils.setTextOrEmpty(tvThree, bean.productName, "无名称");
            TextViewUtils.setTextOrEmpty(tvFour, bean.stockNumber + "");
            if(isHq){
                TextViewUtils.setTextOrEmpty(tvFive, StringFormatUtils.formatTwoDecimal(bean.stockNumber * bean.purchasePrice));
            }else{
                TextViewUtils.setTextOrEmpty(tvFive, StringFormatUtils.formatTwoDecimal(bean.stockNumber * bean.spMoney));
            }
        }
    }

    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnMyItemClickListener {
        void onItemClick(View view, ResultStock bean, int position);
    }

}