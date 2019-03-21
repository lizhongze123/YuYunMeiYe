package cn.yuyun.yymy.ui.store.book;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.yuyun.yymy.R;


public class LeftTimeAdapter extends RecyclerView.Adapter<LeftTimeAdapter.ViewHolder> {

    public List<String> datas ;

    public LeftTimeAdapter() {
        datas = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_left_time, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(datas.get(position), position);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public void addAll(List<String> dataList) {
        this.datas.addAll(dataList);
        notifyDataSetChanged();
    }

    public void clear() {
        this.datas.clear();
        notifyDataSetChanged();
    }

    public String getString(int pos) {
        return this.datas.get(pos);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTime;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
        }

        public void setData(String bean, int num) {
            tvTime.setText(bean);
        }
    }


}
