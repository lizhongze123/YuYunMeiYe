package cn.yuyun.yymy.ui.home.work;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cn.yuyun.yymy.R;
import cn.yuyun.yymy.http.result.ResultWork;
import cn.yuyun.yymy.ui.me.entity.MemberBean;
import cn.yuyun.yymy.view.moments.MomentsItemView;

/**
 * @author
 * @desc
 * @date
 */
public class ResultWorksAdapter extends RecyclerView.Adapter<ResultWorksAdapter.MomentsHolder>{

    private Context mContext;
    private List<ResultWork> dataList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;


    public ResultWorksAdapter(Context context){
        this.mContext = context;
    }


    @Override
    public MomentsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MomentsHolder(LayoutInflater.from(mContext).inflate(R.layout.item_moments, parent, false),onItemClickListener);
    }


    @Override
    public void onBindViewHolder(MomentsHolder holder, int position) {
        MomentsHolder headerViewHolder = holder;
        headerViewHolder.bindItem(dataList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

     class MomentsHolder extends RecyclerView.ViewHolder {

        private ResultWorksAdapter.OnMyItemClickListener onItemClickListener;
        private MomentsItemView momentsItemView;

        MomentsHolder(View view,  ResultWorksAdapter.OnMyItemClickListener onItemClickListener) {
            super(view);
            this.onItemClickListener = onItemClickListener;
            momentsItemView = (MomentsItemView) view.findViewById(R.id.main);
        }

        private void bindItem(final ResultWork b, final int position) {
            momentsItemView.initView(b);
            momentsItemView.setOnMenuClickListener(new MomentsItemView.OnMenuClickListener() {
                @Override
                public void onItemClick(View view, ResultWork bean) {

                }

                @Override
                public void onFavorites(ResultWork bean) {
                    adapterListener.onFavorites(b);
                }

                @Override
                public void onLike(ResultWork bean) {
                    adapterListener.onLike(b, position);
                }

                @Override
                public void onComment(ResultWork bean) {
                    adapterListener.onComment(b, position);
                }
            });
            momentsItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapterListener.onItemClicked(b, position);
                }
            });
        }
    }

    private AdapterListener adapterListener;
    public void setListener(AdapterListener adapterListener) {
        this.adapterListener = adapterListener;
    }


    public void notifyDataSetChanged(List<ResultWork> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addAll(List<ResultWork> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void clear() {
        this.dataList.clear();
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnMyItemClickListener {
        void onItemClick(View view, MemberBean bean, int position);
    }

    public void refreshLike(int position, ResultWork bean) {
        dataList.set(position, bean);
//        notifyItemChanged(position, "lzz");
        notifyDataSetChanged();
    }

}