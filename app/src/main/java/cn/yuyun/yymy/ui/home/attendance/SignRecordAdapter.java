package cn.yuyun.yymy.ui.home.attendance;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.lzz.utils.ResoureUtils;
import cn.lzz.utils.StringUtil;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.http.result.ActionBean;
import cn.yuyun.yymy.http.result.ResultAttendanceWithTime;
import cn.yuyun.yymy.utils.GlideHelper;

/**
 * @author lzz
 * @desc
 * @date 2018-01-29
 */
public class SignRecordAdapter extends RecyclerView.Adapter<SignRecordAdapter.ViewHolder>{

    private int RESOURCE_ID = R.layout.item_sign_record_on;
    private Context mContext;
    public List<ResultAttendanceWithTime.AppAttendanceExternalBean> dataList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;

    public SignRecordAdapter(Context context){
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

    public void notifyDataSetChanged(List<ResultAttendanceWithTime.AppAttendanceExternalBean> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addAll(List<ResultAttendanceWithTime.AppAttendanceExternalBean> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void clear() {
        this.dataList.clear();
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private SignRecordAdapter.OnMyItemClickListener onItemClickListener;
        private LinearLayout rl;
        ImageView ivPic;
        TextView tv_signOnTime, tv_signOnAddress, tvDesc;
        int override;

        ViewHolder(View view,  SignRecordAdapter.OnMyItemClickListener onItemClickListener) {
            super(view);
            ivPic = (ImageView) view.findViewById(R.id.iv_pic);
            tv_signOnTime = (TextView) view.findViewById(R.id.tv_signOnTime);
            tv_signOnAddress = (TextView) view.findViewById(R.id.tv_signOnAddress);
            tvDesc = (TextView) view.findViewById(R.id.tv_desc);
            this.onItemClickListener = onItemClickListener;
            override = ResoureUtils.getDimension(mContext, R.dimen.item_pic);
            rl = (LinearLayout) view.findViewById(R.id.rl);
        }

        private void bindItem(final ResultAttendanceWithTime.AppAttendanceExternalBean bean, final int position) {
            TextViewUtils.setTextOrEmpty(tv_signOnTime, bean.create_time);
            TextViewUtils.setTextOrEmpty(tv_signOnAddress, bean.place);
            if(null == bean.pictures || bean.pictures.size() == 0){
                ivPic.setVisibility(View.GONE);
            }else{
                ivPic.setVisibility(View.VISIBLE);
                if(bean.pictures.get(0).startsWith(mContext.getString(R.string.HTTP))){
                    GlideHelper.displayOverrideImage(mContext, bean.pictures.get(0), override, override, ivPic, R.color.loadding_img_bg);
                }else{
                    GlideHelper.displayOverrideImage(mContext, mContext.getString(R.string.image_url_prefix) + bean.pictures.get(0), override, override, ivPic, R.color.loadding_img_bg);
                }
            }
            rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, bean, position);
                    }
                }
            });
        }
    }

    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnMyItemClickListener {
        void onItemClick(View view, ResultAttendanceWithTime.AppAttendanceExternalBean bean, int position);
    }

}