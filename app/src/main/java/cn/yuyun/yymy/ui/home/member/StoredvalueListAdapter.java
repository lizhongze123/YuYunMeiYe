package cn.yuyun.yymy.ui.home.member;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.lzz.utils.StringFormatUtils;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.http.result.ResultStoredvalueBean;
import cn.yuyun.yymy.ui.home.unboxing.FillContent;
import cn.yuyun.yymy.utils.GlideHelper;

/**
 * @author lzz
 * @desc
 * @date
 */
public class StoredvalueListAdapter extends RecyclerView.Adapter<StoredvalueListAdapter.ViewHolder> {

    private int RESOURCE_ID = R.layout.item_storedvalue_list;
    private Context mContext;
    private List<ResultStoredvalueBean> dataList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;

    public StoredvalueListAdapter(Context context) {
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

    public void notifyDataSetChanged(List<ResultStoredvalueBean> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addAll(List<ResultStoredvalueBean> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void clear() {
        this.dataList.clear();
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private OnMyItemClickListener onItemClickListener;
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

        ViewHolder(View view, OnMyItemClickListener onItemClickListener) {
            super(view);
            ButterKnife.bind(this, view);
            this.onItemClickListener = onItemClickListener;
        }

        private void bindItem(final ResultStoredvalueBean bean, final int position) {
            TextViewUtils.setTextOrEmpty(tvOne, "原有储值余额:" + StringFormatUtils.formatTwoDecimal(bean.getPrevious()));
            TextViewUtils.setTextOrEmpty(tvTwo, "现有储值余额:" + StringFormatUtils.formatTwoDecimal(bean.getCurrent()));

            if(bean.getCurrent() - bean.getPrevious() > 0){
                TextViewUtils.setTextOrEmpty(tvThree, "储值变更:+" + StringFormatUtils.formatTwoDecimal(bean.getCurrent() - bean.getPrevious()));
            }else{
                TextViewUtils.setTextOrEmpty(tvThree, "储值变更:" + StringFormatUtils.formatTwoDecimal(bean.getCurrent() - bean.getPrevious()));
            }
            TextViewUtils.setTextOrEmpty(tvFour, "类型描述:" + bean.getRelated_consumption_type().desc);
            TextViewUtils.setTextOrEmpty(tvFive, "消费时间:" + bean.getConsume_time());
            TextViewUtils.setTextOrEmpty(tvSix, "消费单号:" + bean.getStoredvalue_id());
            /*rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, bean, position);
                    }
                }
            });*/
        }
    }

    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnMyItemClickListener {
        void onItemClick(View view, ResultStoredvalueBean bean, int position);
    }

}