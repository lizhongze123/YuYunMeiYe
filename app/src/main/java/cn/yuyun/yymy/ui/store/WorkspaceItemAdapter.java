package cn.yuyun.yymy.ui.store;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cn.yuyun.yymy.R;
import cn.yuyun.yymy.ui.home.ItemBean;

/**
 * @author lzz
 * @desc
 * @date
 */
public class WorkspaceItemAdapter extends RecyclerView.Adapter<WorkspaceItemAdapter.ItemHolder>{

    /**
     * 标题
     */
    final static int TYPE_TITLE = 0;

    /**
     * 内容
     */
    final static int TYPE_VALUE = 1;

    private int RESOURCE_ID = R.layout.item_workspace;
    private Context context;
    private List<ItemBean> list;
    OnMyItemClickListener onItemClickListener;

    public WorkspaceItemAdapter(Context context, List<ItemBean> list){
        this.context = context;
        this.list = list;

    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context).inflate(RESOURCE_ID, parent, false);
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

        private WorkspaceItemAdapter.OnMyItemClickListener onItemClickListener;
        private LinearLayout ll;
        private ImageView ivIcon;
        private TextView tvDesc;


        ItemHolder(View view,  WorkspaceItemAdapter.OnMyItemClickListener onItemClickListener) {
            super(view);
            ll = (LinearLayout) itemView.findViewById(R.id.ll);
            ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
            tvDesc = (TextView) view.findViewById(R.id.tv_desc);
            this.onItemClickListener = onItemClickListener;
        }

        private void bindItem(final ItemBean bean, final int position) {
            ivIcon.setImageResource(bean.iconResId);
            tvDesc.setText(bean.iconName);

            ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, position);
                    }
                }
            });
        }
    }

    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnMyItemClickListener {
        void onItemClick(View view, int position);
    }

}