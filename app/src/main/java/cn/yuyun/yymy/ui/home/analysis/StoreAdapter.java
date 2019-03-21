package cn.yuyun.yymy.ui.home.analysis;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import cn.yuyun.yymy.R;

/**
 * @author lzz
 * @desc
 * @date 2018-01-25
 */
public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.ViewHolder>{

    private int RESOURCE_ID = R.layout.item_store;
    private Context context;
    private List<StoreBean> list;
    OnMyItemClickListener onItemClickListener;

    public StoreAdapter(Context context, List<StoreBean> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context).inflate(RESOURCE_ID, parent, false);
        return new ViewHolder(rootView, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindItem(list.get(position), position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private StoreAdapter.OnMyItemClickListener onItemClickListener;
        private RelativeLayout rl;
        TextView tvStoreName, tvName;
        ImageView ivAvatar;

        ViewHolder(View view,  StoreAdapter.OnMyItemClickListener onItemClickListener) {
            super(view);
            ivAvatar = (ImageView) view.findViewById(R.id.iv_avatar);
            tvStoreName = (TextView) view.findViewById(R.id.tv_storeName);
            tvName = (TextView) view.findViewById(R.id.tv_name);
            rl = (RelativeLayout) itemView.findViewById(R.id.rl);
            this.onItemClickListener = onItemClickListener;
        }

        private void bindItem(final StoreBean bean, final int position) {
            rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, position);
                    }
                }
            });
        }
    }

    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnMyItemClickListener {
        void onItemClick(View view, int position);
    }

}