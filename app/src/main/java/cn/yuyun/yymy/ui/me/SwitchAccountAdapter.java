package cn.yuyun.yymy.ui.me;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.lzz.utils.LogUtils;
import cn.lzz.utils.ToastUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.app.MyApplication;
import cn.yuyun.yymy.main.LoginAccountBean;
import cn.yuyun.yymy.utils.GlideHelper;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author
 * @desc
 * @date
 */
public class SwitchAccountAdapter extends RecyclerView.Adapter<SwitchAccountAdapter.ViewHolder> {

    private int RESOURCE_ID = R.layout.item_switch_account;
    private Context mContext;
    private List<LoginAccountBean> dataList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;

    private int selectedPos = 0;

    public SwitchAccountAdapter(Context context) {
        this.mContext = context;
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

    @Override
    public int getItemCount() {
        return dataList.size();
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

    public void setPosition(int position) {
        notifyItemChanged(selectedPos);
        selectedPos = position;
        notifyItemChanged(selectedPos);
    }

    public int getPosition() {
        return selectedPos;
    }

    public boolean isDel;

    public void setDelVisiable(boolean isDel){
        this.isDel = isDel;
        if(!isDel){
            for (int i = 0, len = dataList.size(); i < len; i++) {
                dataList.get(i).isChecked = false;
            }
        }
        notifyDataSetChanged();
    }

    public boolean getDelVisiable(){
        return isDel;
    }


    /**获得选中条目的结果*/
   public List<LoginAccountBean> getSelectedItem() {
        List<LoginAccountBean> selectList = new ArrayList<>();
        for (int i = 0, len = dataList.size(); i < len; i++) {
            if (dataList.get(i).isChecked) {
                selectList.add(dataList.get(i));
            }
        }
        return selectList;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_avatar)
        CircleImageView ivAvatar;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_account)
        TextView tvAccount;
        @BindView(R.id.iv_select)
        ImageView ivSelect;
        @BindView(R.id.rl)
        RelativeLayout rl;
        @BindView(R.id.cb)
        CheckBox checkBox;

        private OnMyItemClickListener onItemClickListener;

        ViewHolder(View v, OnMyItemClickListener onItemClickListener) {
            super(v);
            ButterKnife.bind(this, v);
            this.onItemClickListener = onItemClickListener;
        }

        private void bindItem(final LoginAccountBean bean, final int position) {

            if(isDel){
                checkBox.setVisibility(View.VISIBLE);
                ivSelect.setVisibility(View.GONE);
            }else{
                checkBox.setVisibility(View.GONE);
                if(position == 0){
                    ivSelect.setVisibility(View.VISIBLE);
                }else{
                    ivSelect.setVisibility(View.INVISIBLE);
                }
            }

            checkBox.setChecked(bean.isChecked);

            if (!TextUtils.isEmpty(bean.avatar)) {
                if (bean.avatar.startsWith(mContext.getString(R.string.HTTP))) {
                    GlideHelper.displayImage(mContext, bean.avatar, ivAvatar);
                } else {
                    GlideHelper.displayImage(mContext, mContext.getString(R.string.image_url_prefix) + bean.avatar, ivAvatar);
                }
            }
            tvName.setText(bean.staffName);
            tvAccount.setText("账号：" + bean.account);

            /*if (selectedPos == position) {
                ivSelect.setVisibility(View.VISIBLE);
            } else {
                ivSelect.setVisibility(View.INVISIBLE);
            }*/
            rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isDel){
                        checkBox.toggle();
                        bean.isChecked = checkBox.isChecked();
                        if (null != onItemClickListener) {
                            onItemClickListener.onItemClick(bean, position);
                        }
                    }else{
                       if(selectedPos == position){
                            selectedPos = position;
                           ToastUtils.toastInBottom(MyApplication.getInstance().getApplicationContext(), "该账号已经登录");
                        }else{
                           notifyItemChanged(selectedPos);
                           selectedPos = position;
                           notifyItemChanged(selectedPos);
                           if (null != onItemClickListener) {
                               onItemClickListener.onItemClick(bean, position);
                           }
                       }

                    }
                }
            });
        }

    }

    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnMyItemClickListener {
        void onItemClick(LoginAccountBean bean, int position);
    }

}
