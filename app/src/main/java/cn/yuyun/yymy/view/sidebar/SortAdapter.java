package cn.yuyun.yymy.view.sidebar;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.lzz.utils.ResoureUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.http.result.StaffBean;
import cn.yuyun.yymy.utils.GlideHelper;


public class SortAdapter extends BaseAdapter {

	private Context context;
	private List<StaffBean> list;
	private LayoutInflater inflater;
	private int override;

	public SortAdapter(Context context, List<StaffBean> list) {
		this.context = context;
		this.list = list;
		this.inflater = LayoutInflater.from(context);
		override = ResoureUtils.getDimension(context,R.dimen.item_avatar_size);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewholder = null;
		final StaffBean bean = list.get(position);
		if (convertView == null) {
			viewholder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_contact, null);
			viewholder.tv_tag = (TextView) convertView
					.findViewById(R.id.tv_lv_item_tag);
			viewholder.ivAvatar = (ImageView) convertView.findViewById(R.id.iv_avatar);
			viewholder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			viewholder.tv_position = (TextView) convertView.findViewById(R.id.tv_positions);
			viewholder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
			viewholder.ivCall = (ImageView) convertView.findViewById(R.id.iv_call);
			viewholder.ll_item = (LinearLayout) convertView.findViewById(R.id.ll_item);
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
		viewholder.tv_position.setText(bean.positionName);
		viewholder.tv_name.setText(bean.staffName);
		viewholder.tvTime.setText(bean.entryTime);
		if(!TextUtils.isEmpty(bean.avatar)){
			if (bean.avatar.startsWith("http")) {
				GlideHelper.displayImage(context, bean.avatar, override, viewholder.ivAvatar);
			}else{
				GlideHelper.displayImage(context, context.getString(R.string.image_url_prefix) + bean.avatar, override, viewholder.ivAvatar);
			}
		}else{
			GlideHelper.displayImage(context, bean.sex.resId, override, viewholder.ivAvatar);
		}
		viewholder.ivCall.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onCallListener.onCallClick(bean.mobile, position);
			}
		});
		return convertView;
	}

	public int getPositionForSelection(int selection) {
		for (int i = 0; i < list.size(); i++) {
			String Fpinyin = list.get(i).getFirstPinYin();
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
		ImageView ivCall;
		TextView tv_name;
		TextView tv_position;
		TextView tvTime;
		LinearLayout ll_item;
	}

	private OnCallListener onCallListener;

	public void setOnItemClickListener(OnCallListener onCallListener) {
		this.onCallListener = onCallListener;
	}

	public interface OnCallListener {
		void onCallClick(String mobile, int position);
	}

}
