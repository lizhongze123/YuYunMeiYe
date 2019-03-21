package cn.yuyun.yymy.ui.store.member;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.ArrayList;
import java.util.List;

import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.bean.ConsumeType;
import cn.yuyun.yymy.http.result.ActionBean;
import cn.yuyun.yymy.http.result.ResultBillManage;
import cn.yuyun.yymy.http.result.ResultBillManagerTypeDetail;
import cn.yuyun.yymy.view.rv.PullListener;
import cn.yuyun.yymy.view.rv.PullRecyclerView;
import cn.yuyun.yymy.view.rv.SimpleRefreshHeadView;
import cn.yuyun.yymy.view.rv.SimpleRefreshMoreView;

/**
 * @author lzz
 * @desc lzz
 * @date 2018/10/5
 */
public class MemberBillAdapter extends RecyclerView.Adapter<MemberBillAdapter.ViewHolder>{

    private Context mContext;
    private List<ResultBillManage> dataList = new ArrayList<>();
    /**footer类型 Item*/
    private final int TYPE_FOOTER_VIEW = 100002;
    /**普通类型 Item*/
    private final int TYPE_COMMON_VIEW = 100001;

    public MemberBillAdapter(Context context){
        mContext = context;
    }

    public void notifyDataSetChanged(List<ResultBillManage> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addAll(List<ResultBillManage> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void clear() {
        this.dataList.clear();
        notifyDataSetChanged();
    }

    public void refresh(int position, List<ResultBillManagerTypeDetail.BillAllInfoBean> list) {
        dataList.get(position).detailList = list;
        notifyItemChanged(position);
    }

    private boolean isFooterView(int position) {
        return  position >= getItemCount() - 1;
    }

    public ResultBillManage getItem(int pos){
        return dataList.get(pos);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_member_consume_bill,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        if (isFooterView(position)) {
            return TYPE_FOOTER_VIEW;
        }else{
            return TYPE_COMMON_VIEW;
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindItem(dataList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvOne;
        TextView tvTwo;
        TextView tvThree;
        TextView tvFour;
        PullRecyclerView rv;
        MemberBillChildAdapter memberBillChildAdapter;

        public ViewHolder(View itemView) {
            super(itemView);
            tvOne = itemView.findViewById(R.id.tv_one);
            tvTwo = itemView.findViewById(R.id.tv_two);
            tvThree = itemView.findViewById(R.id.tv_three);
            tvFour = itemView.findViewById(R.id.tv_four);
            rv = itemView.findViewById(R.id.pull);
        }

        private void bindItem(final ResultBillManage bean, final int position) {
            tvOne.setText("消费门店:" + bean.cashier_sp_name);
            tvTwo.setText("消费类型:" + bean.containTypeDesc);
            tvThree.setText("消费时间:" + bean.consume_time);
            tvFour.setText("单据编号:" + bean.record_id);
            memberBillChildAdapter = new MemberBillChildAdapter(mContext);
            memberBillChildAdapter.addAll(bean.detailList);
            rv.setHeadRefreshView(new SimpleRefreshHeadView(mContext))
                    .setMoreRefreshView(new SimpleRefreshMoreView(mContext))
                    .setUseLoadMore(true)
                    .setUseRefresh(true)
                    .setPullLayoutManager(new LinearLayoutManager(mContext))
                    .setPullListener(new PullListener() {
                        @Override
                        public void onRefresh() {
                            rv.onComplete(true);
                            onScrollToBottomListener.onTop(position, bean);
                        }

                        @Override
                        public void onLoadMore() {
                            rv.onComplete(true);
                            onScrollToBottomListener.onBottom(position, bean);
                        }
                    })
                    .setPullItemAnimator(null)
                    .build(memberBillChildAdapter);
            rv.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
                @Override
                public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent e) {
                    recyclerView.requestDisallowInterceptTouchEvent(true);
                    return false;
                }

                @Override
                public void onTouchEvent(RecyclerView rv, MotionEvent e) {

                }

                @Override
                public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

                }
            });

        }
    }

    OnScrollToBottomListener onScrollToBottomListener;

    public void setScrollToBottomListener(OnScrollToBottomListener onItemClickListener) {
        this.onScrollToBottomListener = onItemClickListener;
    }

    public interface OnScrollToBottomListener {
        void onBottom(int position, ResultBillManage bean);
        void onTop(int position, ResultBillManage bean);
    }

}
