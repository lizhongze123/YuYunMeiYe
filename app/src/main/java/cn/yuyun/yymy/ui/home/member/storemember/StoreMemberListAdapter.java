package cn.yuyun.yymy.ui.home.member.storemember;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.http.result.ResultMemberBean;
import cn.yuyun.yymy.utils.GlideHelper;
import cn.yuyun.yymy.view.EmptyLayout;

public class StoreMemberListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

	private static final int VIEW_TYPE = -1;
	private int RESOURCE_ID = R.layout.item_member;
	private int RESOURCE_ID_EMPTY = R.layout.item_list_empty_pic;
	private Context mContext;
	private List<ResultMemberBean> dataList = new ArrayList<>();
	OnMyItemClickListener onItemClickListener;

	public StoreMemberListAdapter(Context context){
		this.mContext = context;
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

		if (viewType == -1) {
			View emptyView = LayoutInflater.from(mContext).inflate(RESOURCE_ID_EMPTY, parent, false);
			return new EmptyHolder(emptyView);
		}else{
			View rootView = LayoutInflater.from(mContext).inflate(RESOURCE_ID, parent, false);
			return new ViewHolder(rootView, onItemClickListener);
		}
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		if(holder instanceof ViewHolder){
			ViewHolder viewHolder = (ViewHolder) holder;
			viewHolder.bindItem(dataList.get(position), position);
		}else{
			EmptyHolder emptyHolder = (EmptyHolder) holder;
			emptyHolder.bindItem();
		}
	}

	@Override
	public int getItemViewType(int position) {
		if(dataList.size() == 0){
			return VIEW_TYPE;
		}
		return super.getItemViewType(position);
	}

	@Override
	public int getItemCount() {
		return dataList.size() > 0 ? dataList.size() : 1;
	}

	public void notifyDataSetChanged(List<ResultMemberBean> dataList) {
		this.dataList.clear();
		this.dataList.addAll(dataList);
		notifyDataSetChanged();
	}

	public void addAll(List<ResultMemberBean> dataList) {
		this.dataList.addAll(dataList);
		notifyDataSetChanged();
	}

	public void clear() {
		this.dataList.clear();
		notifyDataSetChanged();
	}

	class EmptyHolder extends RecyclerView.ViewHolder {

		private EmptyLayout emptyLayout;

		EmptyHolder(View view) {
			super(view);
			emptyLayout = view.findViewById(R.id.img_error_layout);
		}

		private void bindItem() {
			emptyLayout.setErrorType(EmptyLayout.NODATA_ENABLE_CLICK);
		}
	}

	class ViewHolder extends RecyclerView.ViewHolder {

		private OnMyItemClickListener onItemClickListener;
		private RelativeLayout rl;
		private ImageView ivAvatar;
		private TextView tvUserName, tvVip, tv_address, tvTime, tvTag;

		ViewHolder(View view, OnMyItemClickListener onItemClickListener) {
			super(view);
			rl = (RelativeLayout) view.findViewById(R.id.rl);
			this.onItemClickListener = onItemClickListener;
			ivAvatar = (ImageView) view.findViewById(R.id.iv_avatar);
			tvUserName = (TextView) view.findViewById(R.id.tv_userName);
			tvVip = (TextView) view.findViewById(R.id.tv_vip);
			tv_address = (TextView) view.findViewById(R.id.tv_address);
			tvTime = (TextView) view.findViewById(R.id.tv_time);
			tvTag = (TextView) view.findViewById(R.id.tv_lv_item_tag);
		}

		private void bindItem(final ResultMemberBean bean, final int position) {
			tvTag.setVisibility(View.GONE);
			if(!TextUtils.isEmpty(bean.memberAvatar)){
				if(bean.memberAvatar.startsWith(mContext.getString(R.string.HTTP))){
					GlideHelper.displayImage(mContext, bean.memberAvatar, ivAvatar);
				}else{
					GlideHelper.displayImage(mContext, mContext.getString(R.string.image_url_prefix) + bean.memberAvatar, ivAvatar);
				}
			}else{
				GlideHelper.displayImage(mContext, bean.member_sex.resId, ivAvatar);
			}
			TextViewUtils.setTextOrEmpty(tvUserName, bean.memberName);
			if(TextUtils.isEmpty(bean.memberLevelName)){
				tvVip.setVisibility(View.GONE);
			}else{
				tvVip.setVisibility(View.VISIBLE);
				TextViewUtils.setTextOrEmpty(tvVip, "(" + bean.memberLevelName + ")");
			}
			TextViewUtils.setTextOrEmpty(tvTime, bean.memberConsumptionLatestTime);
			rl.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					onItemClickListener.onItemClick(v, bean, position);
				}
			});

		}
	}

	public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}

	public interface OnMyItemClickListener {
		void onItemClick(View view, ResultMemberBean bean, int position);
	}

}
