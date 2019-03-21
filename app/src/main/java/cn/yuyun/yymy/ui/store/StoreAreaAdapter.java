package cn.yuyun.yymy.ui.store;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.yuyun.yymy.R;
import cn.yuyun.yymy.http.result.ResultClassfiyStoreBean;

/**
 * @author
 * @desc
 * @date
 */
public class StoreAreaAdapter extends RecyclerView.Adapter<StoreAreaAdapter.ViewHolder> {

    private int RESOURCE_ID = R.layout.item_store_area;
    private Context mContext;
    private List<ResultClassfiyStoreBean> dataList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;

    private int selectedPos = 0;

    public StoreAreaAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public StoreAreaAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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

    public void notifyDataSetChanged(List<ResultClassfiyStoreBean> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addAll(List<ResultClassfiyStoreBean> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void clear() {
        this.dataList.clear();
        notifyDataSetChanged();
    }

    public void setPosition(int position) {
        notifyItemChanged(selectedPos);
        selectedPos = position;
        notifyItemChanged(selectedPos);
    }

    public int getPosition(){
        return selectedPos;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout ll;
        public TextView tv_name;

        private OnMyItemClickListener onItemClickListener;

        ViewHolder(View v, OnMyItemClickListener onItemClickListener) {
            super(v);
            ll = (LinearLayout) v.findViewById(R.id.ll);
            tv_name = (TextView) v.findViewById(R.id.tv_name);
            this.onItemClickListener = onItemClickListener;

        }

        private void bindItem(final ResultClassfiyStoreBean bean, final int position) {
            tv_name.setText(bean.name);
            if (selectedPos == position) {
                tv_name.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
                tv_name.setBackground(mContext.getResources().getDrawable(R.drawable.store_area_bg));
            } else {
                tv_name.setTextColor(mContext.getResources().getColor(R.color.gray_88));
                tv_name.setBackground(mContext.getResources().getDrawable(R.color.white));
            }
            tv_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(selectedPos != position){
                        notifyItemChanged(selectedPos);
                        selectedPos = position;
                        notifyItemChanged(selectedPos);
                        if (null != onItemClickListener) {
                            onItemClickListener.onItemClick(position);
                        }
                    }

                }
            });
        }

        public void autoClick(){
            tv_name.performClick();
        }
    }

    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnMyItemClickListener {
        void onItemClick(int position);
    }

}
