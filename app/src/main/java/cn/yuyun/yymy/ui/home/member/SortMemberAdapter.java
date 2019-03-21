package cn.yuyun.yymy.ui.home.member;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.lzz.utils.DateTimeUtils;
import cn.lzz.utils.ResoureUtils;
import cn.lzz.utils.StringFormatUtils;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.http.result.WarnningMemberBean;
import cn.yuyun.yymy.utils.GlideHelper;


public class SortMemberAdapter extends BaseAdapter {

	private int RESOURCE_ID = R.layout.item_member;
	private Context context;
	private List<WarnningMemberBean> dataList;
	private LayoutInflater inflater;
	private int override;

	public SortMemberAdapter(Context context) {
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		dataList = new ArrayList<>();
		override = ResoureUtils.getDimension(context,R.dimen.item_avatar_size);
	}

	public SortMemberAdapter(Context context,  List<WarnningMemberBean> list) {
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		dataList = list;
		override = ResoureUtils.getDimension(context,R.dimen.item_avatar_size);
	}

	/*public void notifyDataSetChanged(List<WarnningMemberBean> dataList) {
		this.dataList.clear();
		this.dataList.addAll(dataList);
		notifyDataSetChanged();
	}*/

	public void not(List<WarnningMemberBean> dataList) {
		this.dataList.clear();
		this.dataList.addAll(dataList);
		notifyDataSetChanged();
	}

	public void addAll(List<WarnningMemberBean> dataList) {
		this.dataList.addAll(dataList);
		notifyDataSetChanged();
	}

	public void clear() {
		this.dataList.clear();
		notifyDataSetChanged();
	}

	public void remove(int pos) {
		this.dataList.remove(pos);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return dataList.size();
	}

	@Override
	public Object getItem(int position) {
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}


	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewholder = null;
		WarnningMemberBean bean = dataList.get(position);
		if (convertView == null) {
			viewholder = new ViewHolder();
			convertView = inflater.inflate(RESOURCE_ID, null);
			viewholder.tv_tag = (TextView) convertView
					.findViewById(R.id.tv_lv_item_tag);
			viewholder.ivAvatar = (ImageView) convertView.findViewById(R.id.iv_avatar);
			viewholder.tvName = (TextView) convertView.findViewById(R.id.tv_userName);
			viewholder.tvLevel = (TextView) convertView.findViewById(R.id.tv_vip);
			viewholder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
			viewholder.ivSex = (ImageView) convertView.findViewById(R.id.iv_sex);
			viewholder.tvBirthday = (TextView) convertView.findViewById(R.id.tv_birthday);
			viewholder.ivBirthday = (ImageView) convertView.findViewById(R.id.iv_birthday);
			convertView.setTag(viewholder);
		} else {
			viewholder = (ViewHolder) convertView.getTag();
		}

		// 获取首字母的assii值
		int selection = bean.getFirstPinYin().charAt(0);
		// 通过首字母的assii值来判断是否显示字母
		int positionForSelection = getPositionForSelection(selection);
		if (position == positionForSelection) {
			// 相等说明需要显示字母
			viewholder.tv_tag.setVisibility(View.VISIBLE);
			viewholder.tv_tag.setText(bean.getFirstPinYin());
		} else {
			viewholder.tv_tag.setVisibility(View.GONE);
		}
		TextViewUtils.setTextOrEmpty(viewholder.tvName, bean.name);
		if(!TextUtils.isEmpty(bean.avatar)){
			if(bean.avatar.startsWith(context.getString(R.string.HTTP))){
				GlideHelper.displayImage(context, bean.avatar, viewholder.ivAvatar);
			}else{
				GlideHelper.displayImage(context, context.getString(R.string.image_url_prefix) + bean.avatar, viewholder.ivAvatar);
			}
		}else{
			GlideHelper.displayImage(context, R.drawable.avatar_default_female, viewholder.ivAvatar);
		}
		if(null != bean.sex){
			GlideHelper.displayImage(context, bean.sex.sexId, viewholder.ivSex);
		}

		if(!TextUtils.isEmpty(bean.level_name)){
			viewholder.tvLevel.setVisibility(View.VISIBLE);
			viewholder.tvLevel.setText("(" + bean.level_name + ")");
		}else{
			viewholder.tvLevel.setVisibility(View.GONE);
		}
		TextViewUtils.setTextOrEmpty(viewholder.tvTime, DateTimeUtils.StringToDate(bean.consumption_latest_time, DateTimeUtils.FORMAT_DATETIME_UI, DateTimeUtils.FORMAT_DATE_UI_TWO));
		if(null == bean.birthDayTillInfoRsp){
			viewholder.tvBirthday.setVisibility(View.GONE);
			viewholder.ivBirthday.setVisibility(View.GONE);
		}else {
			int warnningDay = Integer.valueOf(bean.birthDayTillInfoRsp.birth_date_till_days);
			if(warnningDay >= 0 && warnningDay <= 15){
				viewholder.tvBirthday.setText("距离生日" + warnningDay + "天");
				viewholder.tvBirthday.setVisibility(View.VISIBLE);
				viewholder.ivBirthday.setVisibility(View.VISIBLE);
			}else if(warnningDay > 15 && warnningDay <= 30){
				viewholder.tvBirthday.setText("距离生日" + warnningDay + "天");
				viewholder.tvBirthday.setVisibility(View.VISIBLE);
				viewholder.ivBirthday.setVisibility(View.GONE);
			}else{
				viewholder.tvBirthday.setVisibility(View.GONE);
				viewholder.ivBirthday.setVisibility(View.GONE);
			}
		}
		return convertView;
	}

	public int getPositionForSelection(int selection) {
		for (int i = 0; i < dataList.size(); i++) {
			String Fpinyin = dataList.get(i).getFirstPinYin();
			char first = Fpinyin.toUpperCase().charAt(0);
			if (first == selection) {
				return i;
			}
		}
		return -1;

	}

	class ViewHolder {
		TextView tv_tag;
		ImageView ivAvatar;
		TextView tvName;
		TextView tvLevel;
		TextView tvTime;
		ImageView ivSex;
		TextView tvBirthday;
		ImageView ivBirthday;
	}

	private OnCallListener onCallListener;

	public void setOnItemClickListener(OnCallListener onCallListener) {
		this.onCallListener = onCallListener;
	}

	public interface OnCallListener {
		void onCallClick(String mobile, int position);
	}

}
