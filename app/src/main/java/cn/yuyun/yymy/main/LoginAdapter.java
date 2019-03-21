package cn.yuyun.yymy.main;

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

import cn.yuyun.yymy.R;
import cn.yuyun.yymy.bean.Sex;
import cn.yuyun.yymy.http.result.ActionBean;
import cn.yuyun.yymy.ui.home.ItemBean;
import cn.yuyun.yymy.utils.GlideHelper;
import cn.yuyun.yymy.view.ItemButton;

/**
 * @author
 * @desc
 * @date
 */
public class LoginAdapter extends RecyclerView.Adapter<LoginAdapter.ItemHolder>{

    private int RESOURCE_ID = R.layout.item_login;
    private Context context;
    private List<LoginAccountBean> dataList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;

    public LoginAdapter(Context context){
        this.context = context;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context).inflate(RESOURCE_ID, parent, false);
        return new ItemHolder(rootView, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        holder.bindItem(dataList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class ItemHolder extends RecyclerView.ViewHolder {

        private LoginAdapter.OnMyItemClickListener onItemClickListener;
        private LinearLayout ll;
        private TextView tvName;
        private ImageView ivAvatar;
        private ImageView ivDel;

        ItemHolder(View view,  LoginAdapter.OnMyItemClickListener onItemClickListener) {
            super(view);
            ll = (LinearLayout) itemView.findViewById(R.id.ll);
            ivAvatar = (ImageView) view.findViewById(R.id.iv_avatar);
            ivDel = (ImageView) view.findViewById(R.id.iv_del);
            tvName = (TextView) view.findViewById(R.id.tv_name);
            this.onItemClickListener = onItemClickListener;
        }

        private void bindItem(final LoginAccountBean bean, final int position) {
            ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, position);
                    }
                }
            });
            ivDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dataList.remove(position);

                    if (onItemClickListener != null) {
                        onItemClickListener.del(position);
                    }

                    notifyDataSetChanged();
                }
            });
            tvName.setText(bean.account);
            if (!TextUtils.isEmpty(bean.avatar)) {
                if (bean.avatar.startsWith(context.getString(R.string.HTTP))) {
                    GlideHelper.displayImage(context, bean.avatar, ivAvatar);
                } else {
                    GlideHelper.displayImage(context, context.getString(R.string.image_url_prefix) + bean.avatar, ivAvatar);
                }
            }
        }
    }

    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnMyItemClickListener {
        void onItemClick(View view, int position);
        void del(int position);
    }

    public void notifyDataSetChanged(List<LoginAccountBean> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addAll(List<LoginAccountBean> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void clear() {
        this.dataList.clear();
        notifyDataSetChanged();
    }

}