package cn.yuyun.yymy.ui.home.work;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.lzz.utils.DateTimeUtils;
import cn.lzz.utils.LogUtils;
import cn.lzz.utils.ResoureUtils;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.constan.Constans;
import cn.yuyun.yymy.http.result.PageInfoWork;
import cn.yuyun.yymy.http.result.ResultWork;
import cn.yuyun.yymy.http.result.ResultWork2;
import cn.yuyun.yymy.sp.UserfoPrefs;
import cn.yuyun.yymy.ui.help.GlideImageLoader;
import cn.yuyun.yymy.ui.me.entity.MemberBean;
import cn.yuyun.yymy.utils.GlideHelper;
import cn.yuyun.yymy.view.moments.MomentsItemView;

/**
 * @author
 * @desc
 * @date
 */
public class MomentsParentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<ResultWork2> dataList = new ArrayList<>();
    AdapterListener2 onItemClickListener;

    public MomentsParentAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MomentsHolder(LayoutInflater.from(mContext).inflate(R.layout.item_work_parent, parent, false), onItemClickListener);
    }

  /*  @Override
    public void onBindViewHolder(RecyclerView.ViewHolder  holder, int position, List<Object> payloads) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {
            MomentsHolder momentsHolder = (MomentsHolder) holder;
            momentsHolder.adapter.re;
        }

    }*/

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MomentsHolder momentsHolder = (MomentsHolder) holder;
        momentsHolder.bindItem(dataList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class MomentsHolder extends RecyclerView.ViewHolder {

        private AdapterListener2 onItemClickListener;
        MomentsAdapter adapter;
        private TextView tvDate;
        private TextView tvRead;
        private RecyclerView rv;
        LinearLayoutManager mLayoutManager;

        MomentsHolder(View view, AdapterListener2 onItemClickListener) {
            super(view);
            this.onItemClickListener = onItemClickListener;
            tvDate = (TextView) view.findViewById(R.id.tv_date);
            tvRead = (TextView) view.findViewById(R.id.tv_read);
            rv = (RecyclerView) view.findViewById(R.id.pull);
            mLayoutManager = new LinearLayoutManager(mContext) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            };
            rv.setLayoutManager(mLayoutManager);
            adapter = new MomentsAdapter(mContext);
            rv.setAdapter(adapter);
        }

        private void bindItem(final ResultWork2 bean, final int position) {
            tvRead.setText("已读(" + bean.readTotal + ")   全部(" + bean.allTotal + ")");
            tvDate.setText(DateTimeUtils.StringToDate(bean.thisTime, DateTimeUtils.FORMAT_DATE_UI, DateTimeUtils.FORMAT_DATETIME_YEAR_MONTH_DAY));
            adapter.notifyDataSetChanged(bean.workVos);
            adapter.setListener(new AdapterListener() {
                @Override
                public void onUserClicked() {


                }

                @Override
                public void onFavorites(ResultWork bean) {
                    onItemClickListener.onFavorites(bean);
                }

                @Override
                public void onLike(ResultWork bean, int childPos) {
                    onItemClickListener.onLike(bean, position, childPos);
                }

                @Override
                public void onComment(ResultWork bean, int childPos) {
                    onItemClickListener.onComment(bean, position, childPos);
                }

                @Override
                public void onItemClicked(ResultWork bean, int childPos) {
                    onItemClickListener.onItemClicked(bean, position, childPos);
                }

            });
        }
    }

    public void setOnItemClickListener(AdapterListener2 onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void notifyDataSetChanged(List<ResultWork2> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addAll(List<ResultWork2> dataList) {
        int end = this.dataList.size() - 1;
        if (dataList.get(0).thisTime.equals(this.dataList.get(end).thisTime)) {
            ResultWork2 bean = dataList.remove(0);
            this.dataList.get(end).workVos.addAll(bean.workVos);
            this.dataList.addAll(dataList);
        } else {
            this.dataList.addAll(dataList);
        }
        notifyDataSetChanged();
    }

    public void clear() {
        this.dataList.clear();
        notifyDataSetChanged();
    }

    public void setDot(ResultWork bean) {
        notifyDataSetChanged();
    }

    public void refreshLike(int parentPos, int childPos, ResultWork bean) {
        dataList.get(parentPos).workVos.set(childPos, bean);
//        notifyItemChanged(parentPos, "lzz");
        notifyDataSetChanged();
    }

    public void refreshLike(int parentPos, int childPos, ResultWork bean, RecyclerView rv) {
        dataList.get(parentPos).workVos.set(childPos, bean);
        MomentsHolder momentsHolder = (MomentsHolder) rv.findViewHolderForAdapterPosition(parentPos);
        if(null != momentsHolder){
            momentsHolder.adapter.notifyItemChanged(childPos, "lzz");
        }
//        notifyItemChanged(parentPos, "lzz");
//        notifyDataSetChanged();
    }

    public void refreshRead(int parentPos, int childPos, ResultWork bean) {
//        dataList.get(parentPos).workVos.set(childPos, bean);
//        notifyItemChanged(parentPos);
        dataList.get(parentPos).readTotal++;
        notifyDataSetChanged();
    }

    public void update(){

    }

}