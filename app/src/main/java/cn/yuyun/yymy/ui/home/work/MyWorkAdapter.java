package cn.yuyun.yymy.ui.home.work;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.yuyun.yymy.R;
import cn.yuyun.yymy.http.result.ResultWork;
import cn.yuyun.yymy.view.moments.MomentsItemMineView;

/**
 * @author
 * @desc
 * @date
 */

public class MyWorkAdapter extends RecyclerView.Adapter<MyWorkAdapter.ViewHolder>{

    private Context mContext;
    private List<ResultWork> dataList = new ArrayList<>();

    public MyWorkAdapter(Context context){
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_moments_mine, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ViewHolder headerViewHolder = holder;
        headerViewHolder.bindItem(dataList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private MomentsItemMineView momentsItemView;

        ViewHolder(View view) {
            super(view);
            momentsItemView = (MomentsItemMineView) view.findViewById(R.id.main);
        }

        private void bindItem(final ResultWork bean, final int position) {
            momentsItemView.initView(bean);
            momentsItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapterListener.onItemClicked(bean, position);
                }
            });
            if(position == 0){
                momentsItemView.setTimeVisibility(true);
            }else{
                momentsItemView.setTimeVisibility(false);
            }
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

    public void refreshLike(int position, ResultWork bean) {
        dataList.set(position, bean);
//        notifyItemChanged(position, "lzz");
        notifyDataSetChanged();
    }

}
