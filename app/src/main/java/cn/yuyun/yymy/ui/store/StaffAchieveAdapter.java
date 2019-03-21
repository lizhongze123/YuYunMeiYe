package cn.yuyun.yymy.ui.store;

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
import cn.lzz.utils.StringFormatUtils;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.http.result.SingleStaffInSpBean;

/**
 * @author lzz
 * @desc
 * @date 2018-01-25
 */
public class StaffAchieveAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int RESOURCE_ID = R.layout.item_staff_analysis;
    private Context context;
    private List<SingleStaffInSpBean> dataList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;
    private List<Integer> tagList = new ArrayList<>();

    public StaffAchieveAdapter(Context context) {
        this.context = context;
        tagList.add(0);
        tagList.add(1);
        tagList.add(2);
    }

    public void notifyDataSetChanged(List<SingleStaffInSpBean> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void notifyCustom(List<Integer> tagList) {
        this.tagList.clear();
        this.tagList.addAll(tagList);
        notifyDataSetChanged();
    }

    public void addAll(List<SingleStaffInSpBean> dataList) {
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

        private StaffAchieveAdapter.OnMyItemClickListener onItemClickListener;
        private LinearLayout ll;

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
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_num)
        TextView tvNum;

        ViewHolder(View view, StaffAchieveAdapter.OnMyItemClickListener onItemClickListener) {
            super(view);
            ButterKnife.bind(this, view);
            ll = (LinearLayout) itemView.findViewById(R.id.ll);
            this.onItemClickListener = onItemClickListener;
        }

        private void bindItem(final SingleStaffInSpBean bean, final int position) {
            ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, bean, position);
                    }
                }
            });

            tvOne.setVisibility(View.GONE);
            tvTwo.setVisibility(View.GONE);
            tvThree.setVisibility(View.GONE);
            tvFour.setVisibility(View.GONE);
            tvFive.setVisibility(View.GONE);
            tvSix.setVisibility(View.GONE);
            for (int i = 0; i < tagList.size(); i++) {
                if (tagList.get(i) == 0) {
                    tvOne.setVisibility(View.VISIBLE);
                } else if (tagList.get(i) == 1) {
                    tvTwo.setVisibility(View.VISIBLE);
                } else if (tagList.get(i) == 2) {
                    tvThree.setVisibility(View.VISIBLE);
                } else if (tagList.get(i) == 3) {
                    tvFour.setVisibility(View.VISIBLE);
                } else if (tagList.get(i) == 4) {
                    tvFive.setVisibility(View.VISIBLE);
                } else if (tagList.get(i) == 5) {
                    tvSix.setVisibility(View.VISIBLE);
                }
            }

            tvNum.setText(position + 1 + "");
            TextViewUtils.setTextOrEmpty(tvName, bean.staff_name);
            TextViewUtils.setTextOrEmpty(tvOne, StringFormatUtils.formatTwoDecimal(bean.handmake));
            TextViewUtils.setTextOrEmpty(tvTwo, StringFormatUtils.formatTwoDecimal(bean.sale));
            TextViewUtils.setTextOrEmpty(tvThree, StringFormatUtils.formatTwoDecimal(bean.consume));
            TextViewUtils.setTextOrEmpty(tvFour, StringFormatUtils.formatTwoDecimal(bean.person_times));
            TextViewUtils.setTextOrEmpty(tvFive, StringFormatUtils.formatTwoDecimal(bean.presale_refund));
            TextViewUtils.setTextOrEmpty(tvSix, StringFormatUtils.formatTwoDecimal(bean.sale_refund));
        }
    }

    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnMyItemClickListener {
        void onItemClick(View view, SingleStaffInSpBean bean, int position);
    }

}