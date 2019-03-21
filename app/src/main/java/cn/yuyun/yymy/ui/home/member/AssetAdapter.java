package cn.yuyun.yymy.ui.home.member;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.lzz.utils.ResoureUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.ui.me.entity.MemberBean;

/**
 * @author lzz
 * @desc
 * @date 2018-01-17
 */
public class AssetAdapter extends RecyclerView.Adapter<AssetAdapter.ViewHolder>{

    private int RESOURCE_ID = R.layout.item_asset;
    private Context mContext;
    private List<MemberBean> dataList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;

    public AssetAdapter(Context context){
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

    public void notifyDataSetChanged(List<MemberBean> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addAll(List<MemberBean> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void clear() {
        this.dataList.clear();
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private AssetAdapter.OnMyItemClickListener onItemClickListener;
        private RelativeLayout rl;
        TextView tvName, tvAddress, tvTime, tvStatus;
        int override;

        ViewHolder(View view,  AssetAdapter.OnMyItemClickListener onItemClickListener) {
            super(view);
            tvName = (TextView) view.findViewById(R.id.tv_name);
            tvAddress = (TextView) view.findViewById(R.id.tv_address);
            tvStatus = (TextView) view.findViewById(R.id.tv_status);
            tvTime = (TextView) view.findViewById(R.id.tv_time);
            this.onItemClickListener = onItemClickListener;
            override = ResoureUtils.getDimension(mContext,R.dimen.item_avatar_size);
        }

        private void bindItem(final MemberBean bean, final int position) {

            rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, bean, position);
                    }
                }
            });
        }
    }

    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnMyItemClickListener {
        void onItemClick(View view, MemberBean bean, int position);
    }

}