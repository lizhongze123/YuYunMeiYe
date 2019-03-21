package cn.yuyun.yymy.ui.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.List;

import cn.yuyun.yymy.R;
import cn.yuyun.yymy.ui.home.ItemBean;
import cn.yuyun.yymy.view.ItemButton;

/**
 * @author lzz
 * @desc
 * @date
 */
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemHolder>{

    private int RESOURCE_ID = R.layout.item_data;
    private int RESOURCE_ID_TOP = R.layout.item_data_top;
    private Context context;
    private List<ItemBean> list;
    OnMyItemClickListener onItemClickListener;
    private String type;

    public ItemAdapter(Context context, List<ItemBean> list, String type){
        this.context = context;
        this.list = list;
        this.type = type;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = null;
        if(type.equals("top")){
            rootView = LayoutInflater.from(context).inflate(RESOURCE_ID_TOP, parent, false);
        }else{
            rootView = LayoutInflater.from(context).inflate(RESOURCE_ID, parent, false);
        }
        return new ItemHolder(rootView, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        holder.bindItem(list.get(position), position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ItemHolder extends RecyclerView.ViewHolder {

        private ItemAdapter.OnMyItemClickListener onItemClickListener;
        private RelativeLayout rl;
        private ItemButton nav_item_tweet;


        ItemHolder(View view,  ItemAdapter.OnMyItemClickListener onItemClickListener) {
            super(view);
            rl = (RelativeLayout) itemView.findViewById(R.id.rl);
            nav_item_tweet = (ItemButton) view.findViewById(R.id.nav_item_tweet);
            this.onItemClickListener = onItemClickListener;
        }

        private void bindItem(final ItemBean bean, final int position) {
            nav_item_tweet.init(bean.iconResId, bean.iconName);
            if(position == 6){
                nav_item_tweet.showRedDot(bean.notice, true);
            }else{
                nav_item_tweet.showRedDot(bean.notice, false);
            }
            rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, position);
                    }
                }
            });
        }
    }

    public void notifyDataChanged(int approve, int announce, int train){
        for (int i = 0; i < list.size(); i++) {
           if(i == 2){
                list.get(i).notice = approve;
            }else if(i == 3){
                list.get(i).notice = announce;
            }else if(i == 4){
               list.get(i).notice = train;
           }
        }
        notifyDataSetChanged();
    }

    public void setTrainDot(){
        list.get(4).notice = 0;
        notifyDataSetChanged();
    }

    public void setApproveDot(){
        list.get(2).notice = 0;
        notifyDataSetChanged();
    }

    public void setWorkDot(int value){
        list.get(2).notice = value;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnMyItemClickListener {
        void onItemClick(View view, int position);
    }

}