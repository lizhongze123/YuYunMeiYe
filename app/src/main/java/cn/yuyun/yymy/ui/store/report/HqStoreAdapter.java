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
import cn.yuyun.yymy.http.result.ResultLevel;

/**
 * @author lzz
 * @desc
 * @date
 */
public class HqStoreAdapter extends RecyclerView.Adapter<HqStoreAdapter.ViewHolder> {

    private int RESOURCE_ID = R.layout.item_filter2;
    private Context context;
    private List<ResultClassfiyStoreBean.OgServiceplacesRspListBean> dataList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;

    public HqStoreAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context).inflate(RESOURCE_ID, parent, false);
        return new ViewHolder(rootView);
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

    public void clearAllSelection() {
        for (int i = 0, len = dataList.size(); i < len; i++) {
            dataList.get(i).isChecked = false;
        }
        notifyDataSetChanged();
    }

    /**
     * 获得选中条目的结果
     */
    public List<ResultClassfiyStoreBean.OgServiceplacesRspListBean> getSelectedItem() {
        List<ResultClassfiyStoreBean.OgServiceplacesRspListBean> selectList = new ArrayList<>();
        for (int i = 0, len = dataList.size(); i < len; i++) {
            if (dataList.get(i).isChecked) {
                selectList.add(dataList.get(i));
            }
        }
        return selectList;
    }

    public boolean getSelectedItem(int pos) {
        if (dataList.get(pos).isChecked) {
            return true;
        } else {
            return false;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private CheckBox checkBox;

        ViewHolder(View view) {
            super(view);
            checkBox = (CheckBox) view.findViewById(R.id.tv_name);
        }

        private void bindItem(final ResultClassfiyStoreBean.OgServiceplacesRspListBean bean, final int position) {
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        bean.isChecked = true;
                        if (null != onItemClickListener) {
                            onItemClickListener.onItemClick();
                        }
                    } else {
                        bean.isChecked = false;
                    }
                }
            });

            checkBox.setText(bean.sp_name);
        }
    }

    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnMyItemClickListener {
        void onItemClick();
    }

}