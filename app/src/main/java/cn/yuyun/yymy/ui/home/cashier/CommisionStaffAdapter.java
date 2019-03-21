package cn.yuyun.yymy.ui.home.cashier;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.lzz.utils.ResoureUtils;
import cn.lzz.utils.StringFormatUtils;
import cn.lzz.utils.StringUtil;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.http.result.ResultListStaff;
import cn.yuyun.yymy.http.result.ResultProject;
import cn.yuyun.yymy.utils.GlideHelper;
import cn.yuyun.yymy.view.bannerview.RoundAngleImageView;

/**
 * @author
 * @desc
 * @date
 */
public class CommisionStaffAdapter extends RecyclerView.Adapter<CommisionStaffAdapter.ViewHolder> {

    private int RESOURCE_ID = R.layout.item_commision_staff;
    private Context mContext;
    private List<ResultListStaff> dataList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;
    int type;
    double amount;

    public CommisionStaffAdapter(Context context, int type) {
        this.mContext = context;
        this.type = type;
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

    public void setAmount(double amount){
        this.amount = amount;
    }

    public List<ResultListStaff> getData(){
        return dataList;
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void notifyDataSetChanged(List<ResultListStaff> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addAll(List<ResultListStaff> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void clear() {
        this.dataList.clear();
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private OnMyItemClickListener onItemClickListener;
        @BindView(R.id.iv_avatar)
        ImageView ivAvatar;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_position)
        TextView tvPosition;
        @BindView(R.id.tv_entryTime)
        TextView tvEntryTime;
        @BindView(R.id.iv_del)
        ImageView ivDel;
        @BindView(R.id.et_sale)
        EditText etSale;
        @BindView(R.id.et_before)
        EditText etBefore;
        @BindView(R.id.rl)
        RelativeLayout rl;
        @BindView(R.id.tv_saleDesc)
        TextView tvSaleDesc;
        @BindView(R.id.tv_beforeDesc)
        TextView tvBeforeDesc;
        int override;

        ViewHolder(View view, OnMyItemClickListener onItemClickListener) {
            super(view);
            ButterKnife.bind(this, view);
            this.onItemClickListener = onItemClickListener;
            override = ResoureUtils.getDimension(mContext, R.dimen.item_avatar_size);
        }

        private void bindItem(final ResultListStaff bean, final int position) {
            if (null != bean.avatar) {
                if (!TextUtils.isEmpty(bean.avatar)) {
                    if (bean.avatar.startsWith(mContext.getString(R.string.HTTP))) {
                        GlideHelper.displayImage(mContext, bean.avatar, ivAvatar, R.color.loadding_img_bg);
                    } else {
                        GlideHelper.displayImage(mContext, mContext.getString(R.string.image_url_prefix) + bean.avatar, ivAvatar, R.color.loadding_img_bg);
                    }
                } else {
                    GlideHelper.displayImage(mContext, R.color.loadding_img_bg, ivAvatar);
                }
            }
            if(StringUtil.isEmpty(bean.position_name)){
                tvPosition.setVisibility(View.GONE);
            }else{
                tvPosition.setVisibility(View.VISIBLE);
                TextViewUtils.setTextOrEmpty(tvPosition, "(" + bean.position_name + ")");
            }
            TextViewUtils.setTextOrEmpty(tvEntryTime, "入职时间:" + bean.entry_time);
            ivDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dataList.remove(position);
                    onItemClickListener.onDel(position, dataList.size());
                    /*if(dataList.size() == 0){
                        onItemClickListener.onDel(position);
                    }else{

                    }*/
                }
            });

            TextViewUtils.setTextOrEmpty(tvName, bean.staff_name);
            if(type == 0 || type == 2){
                tvSaleDesc.setText("销售¥");
                tvBeforeDesc.setText("售前¥");
            }else{
                tvSaleDesc.setText("消耗¥");
                tvBeforeDesc.setText("手工费¥");
            }
            etSale.setText(StringFormatUtils.formatTwoDecimal(bean.sale));
            etBefore.setText(StringFormatUtils.formatTwoDecimal(bean.before));
            etSale.setFilters(new InputFilter[]{new MoneyValueFilter()});
            etBefore.setFilters(new InputFilter[]{new MoneyValueFilter()});
            etSale.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(TextUtils.isEmpty(s.toString()) || s.toString().equals(".")){
                        bean.sale = 0;
                    }else{
                        bean.sale = Double.valueOf(s.toString());
                    }
                }
            });
            etBefore.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(TextUtils.isEmpty(s.toString()) || s.toString().equals(".")){
                        bean.before = 0;
                    }else{
                        bean.before = Double.valueOf(s.toString());
                    }
                }
            });
        }
    }

    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnMyItemClickListener {
        void onSelect(int position);

        void onDel(int position, int size);
    }

}