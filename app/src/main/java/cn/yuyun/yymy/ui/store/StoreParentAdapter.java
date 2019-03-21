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
import cn.yuyun.yymy.ui.store.attendance.AttendanceStaff;
import cn.yuyun.yymy.ui.store.attendance.AttendanceStatisticsChildAdapter;

/**
 * @author
 * @desc
 * @date
 */
public class StoreParentAdapter extends RecyclerView.Adapter<StoreParentAdapter.ViewHolder>{

    private int RESOURCE_ID = R.layout.item_store_parent;
    private Context mContext;
    private List<ResultClassfiyStoreBean> dataList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;

    public StoreParentAdapter(Context context){
        this.mContext = context;
    }

    @Override
    public StoreParentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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

    class ViewHolder extends RecyclerView.ViewHolder {

        public RelativeLayout rl;
        public LinearLayout ll;
        public TextView tv_status;
        public TextView tv_number;
        public ImageView iv_expand;
        public RecyclerView recyclerView;
        public StoreChildAdapter adapter;
        private OnMyItemClickListener onItemClickListener;

        ViewHolder(View v, OnMyItemClickListener onItemClickListener) {
            super(v);
            rl = (RelativeLayout) v.findViewById(R.id.rl);
            ll = (LinearLayout) v.findViewById(R.id.ll);
            tv_status = (TextView) v.findViewById(R.id.tv_status);
            tv_number = (TextView) v.findViewById(R.id.tv_number);
            iv_expand = (ImageView) v.findViewById(R.id.iv_expand);
            recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
            this.onItemClickListener = onItemClickListener;
            adapter = new StoreChildAdapter(mContext);

        }

        private void bindItem(final ResultClassfiyStoreBean bean, final int position) {
            tv_status.setText(bean.name);
            if(null != bean.ogServiceplacesRspList){
                if(bean.ogServiceplacesRspList.size() == 0){
                    ll.setVisibility(View.GONE);
                }else{
                    ll.setVisibility(View.VISIBLE);
                    tv_number.setText("所属门店（" +  bean.ogServiceplacesRspList.size() + "）" );
                }
            }

            rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(recyclerView.getVisibility() == View.VISIBLE){
                        recyclerView.setVisibility(View.GONE);
                    }else{
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                }
            });
            recyclerView.setLayoutManager(new GridLayoutManager(mContext, 5){
                @Override
                public boolean canScrollHorizontally() {
                    return false;
                }

                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            });
            adapter.setOnItemClickListener(new StoreChildAdapter.OnMyItemClickListener() {
                @Override
                public void onItemClick(ResultClassfiyStoreBean.OgServiceplacesRspListBean bean, int pos) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(bean);
                    }
                }
            });
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged(bean.ogServiceplacesRspList);
        }
    }

    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnMyItemClickListener {
        void onItemClick(ResultClassfiyStoreBean.OgServiceplacesRspListBean bean);
    }

}
