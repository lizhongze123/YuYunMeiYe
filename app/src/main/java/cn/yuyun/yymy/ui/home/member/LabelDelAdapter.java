package cn.yuyun.yymy.ui.home.member;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.yuyun.yymy.R;
import cn.yuyun.yymy.http.result.ResultLabel;

/**
 * @author lzz
 * @desc
 * @date 2018-01-17
 */
public class LabelDelAdapter extends RecyclerView.Adapter<LabelDelAdapter.ViewHolder>{

    private int RESOURCE_ID = R.layout.item_label_del;
    private Context mContext;
    private List<ResultLabel> dataList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;

    public LabelDelAdapter(Context context){
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

    public void notifyDataSetChanged(List<ResultLabel> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addAll(List<ResultLabel> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void clear() {
        this.dataList.clear();
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private LabelDelAdapter.OnMyItemClickListener onItemClickListener;
        private TextView tvLabel;

        ViewHolder(View view, LabelDelAdapter.OnMyItemClickListener onItemClickListener) {
            super(view);
            this.onItemClickListener = onItemClickListener;
            tvLabel = (TextView) view.findViewById(R.id.tv_label);
        }

        private void bindItem(final ResultLabel bean, final int position) {
          /*  rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, bean, position);
                    }
                }
            });*/
            tvLabel.setText(bean.kv_value);
        }
    }

    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnMyItemClickListener {
        void onItemClick(View view, ResultLabel bean, int position);
    }

}