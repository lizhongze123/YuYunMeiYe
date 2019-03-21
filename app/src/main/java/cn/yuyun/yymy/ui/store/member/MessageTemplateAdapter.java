package cn.yuyun.yymy.ui.store.member;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.lzz.utils.DensityUtils;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.http.result.ResultMessageTemplate;
import cn.yuyun.yymy.view.ShadowDrawable;

/**
 * @author lzz
 * @desc
 * @date 2018-05-28
 */
public class MessageTemplateAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private int RESOURCE_ID = R.layout.item_message;
    private Context mContext;
    private List<ResultMessageTemplate> dataList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;

    public MessageTemplateAdapter(Context context){
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(RESOURCE_ID, parent, false);
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

    public void notifyDataSetChanged(List<ResultMessageTemplate> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addAll(List<ResultMessageTemplate> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void clear() {
        this.dataList.clear();
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private MessageTemplateAdapter.OnMyItemClickListener onItemClickListener;
        private RelativeLayout rl;
        private TextView tvName;

        ViewHolder(View view,  MessageTemplateAdapter.OnMyItemClickListener onItemClickListener) {
            super(view);
            rl = (RelativeLayout) view.findViewById(R.id.rl);
            this.onItemClickListener = onItemClickListener;
            tvName = (TextView) view.findViewById(R.id.tv_name);

        }

        private void bindItem(final ResultMessageTemplate bean, final int position) {
            ShadowDrawable.setShadowDrawable(tvName, Color.parseColor("#ffffff"), DensityUtils.dip2px(mContext, 6),
                    Color.parseColor("#22000000"),
                    DensityUtils.dip2px(mContext, 3), 0, 0);
            TextViewUtils.setTextOrEmpty(tvName, bean.title);

            rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(itemView, bean, position);
                }
            });
        }
    }

    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnMyItemClickListener {
        void onItemClick(View view, ResultMessageTemplate bean, int position);
    }

}