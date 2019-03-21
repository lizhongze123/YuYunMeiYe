package cn.yuyun.yymy.ui.store.report;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.http.result.ResultStock;
import cn.yuyun.yymy.http.result.ResultStorehouseDetail;

/**
 * @author lzz
 * @desc
 * @date 2018-05-25
 */
public class StockDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int RESOURCE_ID = R.layout.item_stock_detail;

    private Context context;
    private List<ResultStorehouseDetail> dataList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;

    public StockDetailAdapter(Context context) {
        this.context = context;
    }

    public void notifyDataSetChanged(List<ResultStorehouseDetail> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addAll(List<ResultStorehouseDetail> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
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

        private StockDetailAdapter.OnMyItemClickListener onItemClickListener;
        private LinearLayout ll;

        @BindView(R.id.tv_type)
        TextView tvType;
        @BindView(R.id.tv_num)
        TextView tvNum;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_code)
        TextView tvCode;


        ViewHolder(View view, StockDetailAdapter.OnMyItemClickListener onItemClickListener) {
            super(view);
            ButterKnife.bind(this, view);
            ll = (LinearLayout) itemView.findViewById(R.id.ll);
            this.onItemClickListener = onItemClickListener;
        }

        private void bindItem(final ResultStorehouseDetail bean, final int position) {
           /* ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != onItemClickListener) {
                        onItemClickListener.onItemClick(v, bean, position);
                    }
                }
            });*/

            TextViewUtils.setTextOrEmpty(tvType, "商品类型:" + bean.type);
            TextViewUtils.setTextOrEmpty(tvNum, "数量:" + bean.numbers);
            TextViewUtils.setTextOrEmpty(tvTime, "日期:" + bean.date);
            TextViewUtils.setTextOrEmpty(tvCode, "单据编号:" + bean.orderId);
        }
    }

    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnMyItemClickListener {
        void onItemClick(View view, ResultStock bean, int position);
    }

}