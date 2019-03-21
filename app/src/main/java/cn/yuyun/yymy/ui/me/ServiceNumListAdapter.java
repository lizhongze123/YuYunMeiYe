package cn.yuyun.yymy.ui.me;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.lzz.utils.DateTimeUtils;
import cn.lzz.utils.StringFormatUtils;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.http.result.ResultServiceNum;
import cn.yuyun.yymy.ui.me.entity.PeopleNumberBean;
import cn.yuyun.yymy.utils.GlideHelper;
import de.hdodenhof.circleimageview.CircleImageView;

/***
 *
 */
public class ServiceNumListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int RESOURCE_ID = R.layout.item_servicenum;
    private Context mContext;
    private List<ResultServiceNum> dataList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;

    public ServiceNumListAdapter(Context context) {
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

    public void notifyDataSetChanged(List<ResultServiceNum> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addAll(List<ResultServiceNum> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void clear() {
        this.dataList.clear();
        notifyDataSetChanged();
    }

     class ViewHolder extends RecyclerView.ViewHolder {

        private OnMyItemClickListener onItemClickListener;

        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_card)
        TextView tvCard;
        @BindView(R.id.tv_mobile)
        TextView tvMobile;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_service)
        TextView tvService;
         @BindView(R.id.tv_serviceNum)
         TextView tvServiceNum;

        ViewHolder(View view, OnMyItemClickListener onItemClickListener) {
            super(view);
            ButterKnife.bind(this, view);
            this.onItemClickListener = onItemClickListener;
        }

        private void bindItem(final ResultServiceNum bean, final int position) {
            TextViewUtils.setTextOrEmpty(tvName, bean.member_name);
            TextViewUtils.setTextOrEmpty(tvCard, bean.related_consumption_id);
            TextViewUtils.setTextOrEmpty(tvMobile, "手机号：" + bean.member_mobile);
            TextViewUtils.setTextOrEmpty(tvTime, "消费时间：" + DateTimeUtils.StringToDate(bean.consume_time, DateTimeUtils.FORMAT_DATETIME_UI,  DateTimeUtils.FORMAT_DATETIME_TWO));
            TextViewUtils.setTextOrEmpty(tvService, "消费项目：" + bean.product_name);
            TextViewUtils.setTextOrEmpty(tvServiceNum, "项目数：" + bean.service_items);
        }
    }

    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnMyItemClickListener {
        void onItemClick(View view, ResultServiceNum bean, int position);
    }

}
