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
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.http.result.ResultListStaff;
import cn.yuyun.yymy.utils.GlideHelper;
import cn.yuyun.yymy.utils.MathUtils;

/**
 * @author
 * @desc
 * @date
 */
public class CashierBuyListAdapter extends RecyclerView.Adapter<CashierBuyListAdapter.ViewHolder> {

    private int RESOURCE_ID = R.layout.item_cashier_buy_list;
    private Context mContext;
    private List<CashierBuyBean> dataList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;

    private double storedValue;

    public CashierBuyListAdapter(Context context) {
        this.mContext = context;
    }

    public void setStoredValue(double storedValue){
        this.storedValue = storedValue;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(RESOURCE_ID, parent, false);
        return new ViewHolder(rootView, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        holder.bindItem(dataList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    public List<CashierBuyBean> getData(){
        return dataList;
    }

    public CashierBuyBean getData(int position){
        return dataList.get(position);
    }

    public void notifyStaffList(List<ResultListStaff> dataList, int position) {
        this.dataList.get(position).staffList = dataList;
        notifyDataSetChanged();
    }

    public void notifyDataSetChanged(List<CashierBuyBean> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addAll(List<CashierBuyBean> dataList) {
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
        @BindView(R.id.tv_guidePrice)
        TextView tvGuidePrice;
        @BindView(R.id.iv_del)
        ImageView ivDel;
        @BindView(R.id.et_pay)
        EditText etPay;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.rl)
        RelativeLayout rl;
        @BindView(R.id.tv_subtract)
        TextView tvSubtract;
        @BindView(R.id.et_total)
        TextView etTotal;
        @BindView(R.id.tv_add)
        TextView tvAdd;
        @BindView(R.id.tv_staff)
        TextView tvStaff;
        @BindView(R.id.et_transactionPrice)
        EditText etTransactionPrice;
        @BindView(R.id.et_storedValuePay)
        EditText etStoredValuePay;
        @BindView(R.id.et_arrears)
        EditText etArrears;
        @BindView(R.id.iv_more)
        ImageView ivMore;
        @BindView(R.id.rl_select)
        RelativeLayout rlSelect;
        int override;
        TextWatcher tw;

        ViewHolder(View view, OnMyItemClickListener onItemClickListener) {
            super(view);
            ButterKnife.bind(this, view);
            this.onItemClickListener = onItemClickListener;
            override = ResoureUtils.getDimension(mContext, R.dimen.item_avatar_size);
        }

        private void bindItem(final CashierBuyBean bean, final int position) {
            //默认两位小数
            etArrears.setFilters(new InputFilter[]{new MoneyValueFilter()});
            etStoredValuePay.setFilters(new InputFilter[]{new MoneyValueFilter()});
            etTransactionPrice.setFilters(new InputFilter[]{new MoneyValueFilter()});
            etTotal.setFilters( new InputFilter[]{new InputFilter.LengthFilter(6)});
            if (!TextUtils.isEmpty(bean.thumb_url)) {
                if (bean.thumb_url.startsWith(mContext.getString(R.string.HTTP))) {
                    GlideHelper.displayImage(mContext, bean.thumb_url, ivAvatar, R.color.loadding_img_bg);
                } else {
                    GlideHelper.displayImage(mContext, mContext.getString(R.string.image_url_prefix) + bean.thumb_url, ivAvatar, R.color.loadding_img_bg);
                }
            } else {
                ivAvatar.setImageResource(R.color.loadding_img_bg);
            }

            TextViewUtils.setTextOrEmpty(tvName, bean.name);
            TextViewUtils.setTextOrEmpty(tvGuidePrice, "指导价:¥" + bean.guideprice);
            etPay.setText("实付:¥"  + StringFormatUtils.formatTwoDecimal(bean.amount_realpay));
            etTransactionPrice.setText(StringFormatUtils.formatTwoDecimal(bean.transaction_price));
//            etTransactionPrice.setText("" + MathUtils.mul(bean.guideprice, bean.total_num));
            if(null != bean.staffList){
                StringBuilder sb = new StringBuilder();
                for (ResultListStaff staffBean : bean.staffList) {
                    sb.append(staffBean.staff_name + " ");
                }
                tvStaff.setText(sb.toString());
            }else{
                tvStaff.setText("");
            }
            ivDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.del(dataList.size(), bean);
                    dataList.remove(position);
                    notifyDataSetChanged();
                }
            });
            tvSubtract.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (bean.total_num == 1) {
                        bean.total_num = 1;
                        etTotal.setText("1");
                        etStoredValuePay.setText("");
                        etArrears.setText("");
                        bean.storedvalue_pay = 0;
                        bean.arrear_pay = 0;
                    } else {
                        bean.total_num--;
                        etTotal.setText(bean.total_num + "");
                        etStoredValuePay.setText("");
                        etArrears.setText("");
                        bean.storedvalue_pay = 0;
                        bean.arrear_pay = 0;
                    }
                }
            });
            tvAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bean.total_num++;
                    etTotal.setText(bean.total_num + "");
                    etStoredValuePay.setText("");
                    etArrears.setText("");
                    bean.storedvalue_pay = 0;
                    bean.arrear_pay = 0;
                }
            });
            rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {

                    }
                }
            });
            rlSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != onItemClickListener) {
                        onItemClickListener.onSelect(position, null, bean);
                    }
                }
            });
            etTotal.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String text = s.toString().trim();
                    int len = text.length();
                    if(len == 0){

                    }else{
                        if (len == 1 && text.equals("0")) {
                            etTotal.removeTextChangedListener(this);
                            etTotal.setText("1");
                            bean.total_num = 1;
                            bean.transaction_price = MathUtils.mul(bean.guideprice , bean.total_num);
//                            bean.amount_realpay = bean.transaction_price - bean.storedvalue_pay - bean.arrear_pay;
                            etTransactionPrice.setText(bean.transaction_price + "");
//                            tvPay.setText("实付:¥"  + StringFormatUtils.formatTwoDecimal(bean.amount_realpay));
                            etTotal.addTextChangedListener(this);
                        }else{
                            bean.total_num = Integer.valueOf(text);
//                            bean.amount_realpay = bean.transaction_price - bean.storedvalue_pay - bean.arrear_pay;
//                            tvPay.setText("实付:¥"  + StringFormatUtils.formatTwoDecimal(bean.amount_realpay));
//                            bean.transaction_price = bean.guideprice * bean.count;
                            etTransactionPrice.setText(StringFormatUtils.formatDecimal(MathUtils.mul(bean.guideprice , bean.total_num)) + "");
                        }
                    }
                }
            });
            etTransactionPrice.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(TextUtils.isEmpty(s.toString()) || s.toString().equals(".")){
                        bean.transaction_price = 0;
                    }else{
                        bean.transaction_price = Double.valueOf(s.toString());
                    }
                    if(bean.storedvalue_pay + bean.arrear_pay > bean.transaction_price){
                        onItemClickListener.toast("储值支付加上欠款不能大于成交价格");
                        etArrears.setText("");
                        etStoredValuePay.setText("");
                        bean.storedvalue_pay = 0;
                        bean.arrear_pay = 0;
                    }
                    bean.amount_realpay = MathUtils.sub(MathUtils.sub(bean.transaction_price ,bean.storedvalue_pay), bean.arrear_pay);
                    etPay.setText("实付:¥"  + StringFormatUtils.formatDecimal(bean.amount_realpay));

                }
            });
            etStoredValuePay.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(TextUtils.isEmpty(s.toString()) || s.toString().equals(".")){
                        bean.storedvalue_pay = 0;
                    }else{
                        bean.storedvalue_pay = Double.valueOf(s.toString());
                    }
                    if(bean.storedvalue_pay > storedValue){
                        onItemClickListener.toast("储值支付不能大于储值余额");
                        bean.storedvalue_pay = 0;
                        etStoredValuePay.setText("");
                    }
                    if(bean.storedvalue_pay + bean.arrear_pay > bean.transaction_price){
                        onItemClickListener.toast("储值支付加上欠款不能大于成交价格");
                        bean.storedvalue_pay = 0;
                        etStoredValuePay.setText("");
                    }
                    double total = 0;
                    for (int i = 0; i < dataList.size(); i++) {
                        total = total + dataList.get(i).storedvalue_pay;
                    }
                    if(total > storedValue){
                        onItemClickListener.toast("储值支付不能大于储值余额");
                        bean.storedvalue_pay = 0;
                        etStoredValuePay.setText("");
                    }
                    bean.amount_realpay = MathUtils.sub(MathUtils.sub(bean.transaction_price ,bean.storedvalue_pay), bean.arrear_pay);
                    etPay.setText("实付:¥"  + StringFormatUtils.formatDecimal(bean.amount_realpay));
                }
            });
            etArrears.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(TextUtils.isEmpty(s.toString()) || s.toString().equals(".")){
                        bean.arrear_pay = 0;
                    }else{
                        bean.arrear_pay = Double.valueOf(s.toString());
                    }
                    if(bean.storedvalue_pay + bean.arrear_pay > bean.transaction_price){
                        onItemClickListener.toast("储值支付加上欠款不能大于成交价格");
                        bean.arrear_pay = 0;
                        etArrears.setText("");
                    }
                    bean.amount_realpay = MathUtils.sub(MathUtils.sub(bean.transaction_price ,bean.storedvalue_pay), bean.arrear_pay);
                    etPay.setText("实付:¥"  + StringFormatUtils.formatDecimal(bean.amount_realpay));
                }
            });
            etPay.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    double total = 0;
                    for (int i = 0; i < dataList.size(); i++) {
                        total = total + dataList.get(i).amount_realpay;
                    }
                    onItemClickListener.onTotal(total);
                }
            });
            etStoredValuePay.setText(StringFormatUtils.formatTwoDecimal(bean.storedvalue_pay));
            etArrears.setText(StringFormatUtils.formatTwoDecimal(bean.arrear_pay));
        }
    }

    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnMyItemClickListener {
        void onSelect(int position, List<ResultListStaff> staffList, CashierBuyBean bean);
        void onTotal(double total);
        void del(int size, CashierBuyBean bean);
        void toast(String toast);
    }

}