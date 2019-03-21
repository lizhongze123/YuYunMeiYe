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

import cn.lzz.utils.StringFormatUtils;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.http.result.SingleStaffInSpBean;

/**
 * @author lzz
 * @desc
 * @date 2018-01-25
 */
public class MemberAchieveAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int VIEW_TYPE = -1;
    private int RESOURCE_ID = R.layout.item_analysis;
    private int RESOURCE_ID_EMPTY = R.layout.item_list_empty;
    private Context context;
    private List<SingleStaffInSpBean> dataList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;

    public MemberAchieveAdapter(Context context){
        this.context = context;
    }

    public void notifyDataSetChanged(List<SingleStaffInSpBean> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addAll(List<SingleStaffInSpBean> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == -1) {
            View emptyView = LayoutInflater.from(context).inflate(RESOURCE_ID_EMPTY, parent, false);
            return new EmptyHolder(emptyView);
        }else{
            View rootView = LayoutInflater.from(context).inflate(RESOURCE_ID, parent, false);
            return new ViewHolder(rootView, onItemClickListener);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ViewHolder){
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.bindItem(dataList.get(position), position);
        }else{
            EmptyHolder emptyHolder = (EmptyHolder) holder;
            emptyHolder.bindItem();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(dataList.size() == 0){
            return VIEW_TYPE;
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return dataList.size() > 0 ? dataList.size() : 1;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private MemberAchieveAdapter.OnMyItemClickListener onItemClickListener;
        private LinearLayout ll;
        TextView tvOne, tvTwo, tvThree, tvFour;

        ViewHolder(View view,  MemberAchieveAdapter.OnMyItemClickListener onItemClickListener) {
            super(view);
            tvOne = (TextView) view.findViewById(R.id.tv_one);
            tvTwo = (TextView) view.findViewById(R.id.tv_two);
            tvThree = (TextView) view.findViewById(R.id.tv_three);
            tvFour = (TextView) view.findViewById(R.id.tv_four);
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

            TextViewUtils.setTextOrEmpty(tvOne, bean.staff_name);
            TextViewUtils.setTextOrEmpty(tvTwo,  StringFormatUtils.formatTwoDecimal(bean.presale));
            TextViewUtils.setTextOrEmpty(tvThree,  StringFormatUtils.formatTwoDecimal(bean.sale));
            TextViewUtils.setTextOrEmpty(tvFour,  StringFormatUtils.formatTwoDecimal(bean.consume));
        }
    }

    class EmptyHolder extends RecyclerView.ViewHolder {

        EmptyHolder(View view) {
            super(view);
        }

        private void bindItem() {

        }
    }

    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnMyItemClickListener {
        void onItemClick(View view, SingleStaffInSpBean bean, int position);
    }

}