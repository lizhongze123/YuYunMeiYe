package cn.yuyun.yymy.ui.store.report;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.yuyun.yymy.R;
import cn.yuyun.yymy.http.result.ResultClassfiyStoreBean;

/**
 * @author lzz
 * @desc
 * @date
 */
public class HqStoreRadioAdapter extends RecyclerView.Adapter<HqStoreRadioAdapter.ViewHolder> {

    private int RESOURCE_ID = R.layout.item_filter;
    private Context context;
    private List<ResultClassfiyStoreBean.OgServiceplacesRspListBean> dataList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;

    public HqStoreRadioAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context).inflate(RESOURCE_ID, parent, false);
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

    public void notifyDataSetChanged(List<ResultClassfiyStoreBean.OgServiceplacesRspListBean> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addAll(List<ResultClassfiyStoreBean.OgServiceplacesRspListBean> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void clear() {
        this.dataList.clear();
        notifyDataSetChanged();
    }

    public void setSelectPos(int pos) {
        selectedPos = pos;
        notifyDataSetChanged();
    }

    private int selectedPos = 0;

    public void clearSelection(int pos) {
        selectedPos = pos;
    }

    public void notifySelectedChange(int position){
        clearSelection(position);
        notifyDataSetChanged();
    }

    /**获得选中条目的结果*/
    public List<ResultClassfiyStoreBean.OgServiceplacesRspListBean> getSelectedItem() {
        List<ResultClassfiyStoreBean.OgServiceplacesRspListBean> selectList = new ArrayList<>();
        for (int i = 0, len = dataList.size(); i < len; i++) {
            if (dataList.get(i).isChecked) {
                selectList.add(dataList.get(i));
            }
        }
        return selectList;
    }

    public ResultClassfiyStoreBean.OgServiceplacesRspListBean getItem(int pos){
        return dataList.get(pos);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private OnMyItemClickListener onItemClickListener;
        private RelativeLayout rl;
        TextView tvTime;

        ViewHolder(View view, OnMyItemClickListener onItemClickListener) {
            super(view);
            tvTime = (TextView) view.findViewById(R.id.tv_name);
            rl = (RelativeLayout) itemView.findViewById(R.id.rl);
            this.onItemClickListener = onItemClickListener;
        }

        private void bindItem(final ResultClassfiyStoreBean.OgServiceplacesRspListBean bean, final int position) {
            tvTime.setText(bean.sp_name);
            if(position == selectedPos){
                tvTime.setSelected(true);
            }else{
                tvTime.setSelected(false);
            }
            rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notifySelectedChange(getAdapterPosition());
                    onItemClickListener.onItemClick(v, bean, position);
                }
            });
        }
    }
    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnMyItemClickListener {
        void onItemClick(View view, ResultClassfiyStoreBean.OgServiceplacesRspListBean bean, int position);
    }


}