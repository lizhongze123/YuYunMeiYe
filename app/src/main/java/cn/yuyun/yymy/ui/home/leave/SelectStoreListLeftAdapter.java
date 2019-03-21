package cn.yuyun.yymy.ui.home.leave;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.lzz.utils.ResoureUtils;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.event.NotifyEvent;
import cn.yuyun.yymy.http.result.ResultClassfiyStoreBean;
import cn.yuyun.yymy.http.result.ResultGroupBean;
import cn.yuyun.yymy.ui.home.analysis.StoreBean;
import cn.yuyun.yymy.utils.EvenManager;
import cn.yuyun.yymy.utils.GlideHelper;

/**
 * @author
 * @desc 门店列表
 * @date
 */

public class SelectStoreListLeftAdapter extends RecyclerView.Adapter<SelectStoreListLeftAdapter.ViewHolder>{

    private int checkedPosition;
    private int RESOURCE_ID = R.layout.item_store_left;
    private Context mContext;
    private List<ResultClassfiyStoreBean> dataList = new ArrayList<>();
    SelectStoreListLeftAdapter.OnMyItemClickListener onItemClickListener;

    public SelectStoreListLeftAdapter(Context context){
        this.mContext = context;
    }

    @Override
    public SelectStoreListLeftAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(RESOURCE_ID, parent, false);
        return new ViewHolder(rootView, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(SelectStoreListLeftAdapter.ViewHolder holder, int position) {
        holder.bindItem(dataList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void notifyDataSetChanged(List<ResultClassfiyStoreBean> dataList) {
        this.dataList.clear();
        checkedPosition = 0;
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void setCheckedPosition(int checkedPosition) {
        this.checkedPosition = checkedPosition;
        notifyDataSetChanged();
    }

    public void addAll(List<ResultClassfiyStoreBean> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void clear() {
        this.dataList.clear();
        checkedPosition = 0;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private SelectStoreListLeftAdapter.OnMyItemClickListener onItemClickListener;
        private RelativeLayout rl;
        TextView tvName;
        int override;
        private View line;

        ViewHolder(View view,  SelectStoreListLeftAdapter.OnMyItemClickListener onItemClickListener) {
            super(view);
            tvName = (TextView) view.findViewById(R.id.tv_name);
            rl = (RelativeLayout) view.findViewById(R.id.rl);
            line =  view.findViewById(R.id.line);
            this.onItemClickListener = onItemClickListener;
            override = ResoureUtils.getDimension(mContext, R.dimen.item_avatar_size);
        }

        private void bindItem(final ResultClassfiyStoreBean bean, final int position) {
            TextViewUtils.setTextOrEmpty(tvName, bean.name);
            rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, bean, position);
                    }
                }
            });
            if (position == checkedPosition) {
                line.setVisibility(View.VISIBLE);
                rl.setBackgroundColor(Color.parseColor("#FFFFFF"));
                tvName.setTextColor(Color.parseColor("#303030"));
                tvName.getPaint().setFakeBoldText(true);
            } else {
                line.setVisibility(View.GONE);
                rl.setBackgroundColor(Color.parseColor("#F5F5F5"));
                tvName.setTextColor(Color.parseColor("#7A7A7A"));
                tvName.getPaint().setFakeBoldText(false);
            }
        }
    }

    public void setOnItemClickListener(SelectStoreListLeftAdapter.OnMyItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnMyItemClickListener {
        void onItemClick(View view, ResultClassfiyStoreBean bean, int position);
    }

}
