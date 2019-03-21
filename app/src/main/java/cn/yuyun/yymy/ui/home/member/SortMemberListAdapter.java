package cn.yuyun.yymy.ui.home.member;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.lzz.utils.ResoureUtils;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.bean.Sex;
import cn.yuyun.yymy.http.result.ResultMemberBean;
import cn.yuyun.yymy.http.result.StaffBean;
import cn.yuyun.yymy.utils.GlideHelper;

/***
 * 排序的会员列表adapter
 */
public class SortMemberListAdapter extends BaseAdapter {

	private Context context;
	private List<ResultMemberBean> list;
	private LayoutInflater inflater;
	private int override;

	public SortMemberListAdapter(Context context, List<ResultMemberBean> list) {
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
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewholder = null;
		ResultMemberBean bean = list.get(position);
		if (convertView == null) {
			viewholder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_member, null);
			viewholder.tv_tag = (TextView) convertView
					.findViewById(R.id.tv_lv_item_tag);
			viewholder.ivAvatar = (ImageView) convertView.findViewById(R.id.iv_avatar);
			viewholder.tv_name = (TextView) convertView.findViewById(R.id.tv_userName);
			viewholder.tvLevel = (TextView) convertView.findViewById(R.id.tv_vip);
			viewholder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
			viewholder.ivCall = (ImageView) convertView.findViewById(R.id.iv_call);
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
		viewholder.tv_name.setText(bean.memberName);

		if(!TextUtils.isEmpty(bean.memberAvatar)){
			if(bean.memberAvatar.startsWith("http")){
				GlideHelper.displayImage(context, bean.memberAvatar, viewholder.ivAvatar);
			}else{
				GlideHelper.displayImage(context, context.getString(R.string.image_url_prefix) + bean.memberAvatar, viewholder.ivAvatar);
			}
		}else{
			if(bean.member_sex == Sex.MALE || bean.member_sex == Sex.MALE2){
				GlideHelper.displayImage(context, R.drawable.avatar_default_male, viewholder.ivAvatar);
			}else{
				GlideHelper.displayImage(context, R.drawable.avatar_default_female, viewholder.ivAvatar);
			}
		}

		if(!TextUtils.isEmpty(bean.memberLevelName)){
			viewholder.tvLevel.setVisibility(View.VISIBLE);
			viewholder.tvLevel.setText(bean.memberLevelName);
		}else{
			viewholder.tvLevel.setVisibility(View.GONE);
		}
		TextViewUtils.setTextOrEmpty(viewholder.tvTime, bean.memberConsumptionLatestTime);
		viewholder.ivCall.setVisibility(View.GONE);
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
		TextView tv_name;
		TextView tvLevel;
		TextView tvTime;
		ImageView ivCall;
	}

}
