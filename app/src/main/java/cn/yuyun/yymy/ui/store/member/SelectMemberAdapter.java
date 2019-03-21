package cn.yuyun.yymy.ui.store.member;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.lzz.utils.StringFormatUtils;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.http.result.ResultMemberBean;
import cn.yuyun.yymy.utils.GlideHelper;

/**
 * @author lzz
 * @desc lzz
 * @date 2018/5/28
 */
public class SelectMemberAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private boolean[] isCheckedArray;
    private Bitmap[] bitmaps;
    private List<ResultMemberBean> list = new ArrayList<>();
    private Context mContext;
    private List<String> exitedMembers;

    public SelectMemberAdapter(Context mContext, List<ResultMemberBean> users, List<String> exitedMembers) {
        layoutInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.list = users;
        bitmaps = new Bitmap[list.size()];
        isCheckedArray = new boolean[list.size()];
        this.exitedMembers = exitedMembers;
    }

    public Bitmap getBitmap(int position) {
        return bitmaps[position];
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_select_member, parent, false);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        if (holder == null) {
            holder = new ViewHolder();
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.cb);
            holder.ivAvatar = (ImageView) convertView.findViewById(R.id.iv_avatar);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tvLevel = (TextView) convertView.findViewById(R.id.tv_vip);
            holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
            holder.ivSex = (ImageView) convertView.findViewById(R.id.iv_sex);
            convertView.setTag(holder);
        }
        final ResultMemberBean memberBean = list.get(position);
        String hxid = memberBean.memberName;
        if(!TextUtils.isEmpty(memberBean.memberAvatar)){
            if(memberBean.memberAvatar.startsWith(mContext.getString(R.string.HTTP))){
                GlideHelper.displayImage(mContext, memberBean.memberAvatar, holder.ivAvatar);
            }else{
                GlideHelper.displayImage(mContext, mContext.getString(R.string.image_url_prefix) + memberBean.memberAvatar, holder.ivAvatar);
            }
        }else{
            GlideHelper.displayImage(mContext, memberBean.member_sex.resId, holder.ivAvatar);
        }
        GlideHelper.displayImage(mContext, memberBean.member_sex.sexId, holder.ivSex);
        TextViewUtils.setTextOrEmpty(holder.tvName, memberBean.memberName);
        holder.tvLevel.setVisibility(View.VISIBLE);
        TextViewUtils.setTextOrEmpty(holder.tvLevel, memberBean.memberLevelName);
        TextViewUtils.setTextOrEmpty(holder.tvTime, StringFormatUtils.getUserDate(memberBean.memberConsumptionLatestTime));
//        holder.checkBox.setButtonDrawable(R.drawable.bg_checkbox_blue);
        holder.checkBox.setOnCheckedChangeListener(new MyOnCheckedChangeListener(position, memberBean));

        if (exitedMembers != null && exitedMembers.contains(hxid)) {
        } else {
            holder.checkBox.setChecked(isCheckedArray[position]);
        }
        return convertView;
    }


    //获得选中条目的结果
    public List<ResultMemberBean> getSelectedItem() {
        List<ResultMemberBean> selectList = new ArrayList<>();
        for (int i = 0, len = list.size(); i < len; i++) {
            if (isCheckedArray[i]) {
                selectList.add(list.get(i));
            }
        }
        return selectList;
    }

    class MyOnCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {
        private int positon;
        private ResultMemberBean memberBean;

        public MyOnCheckedChangeListener(int positon, ResultMemberBean memberBean){
            this.positon = positon;
            this.memberBean = memberBean;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            if (exitedMembers.contains(memberBean.memberName)) {
                return;
            }
            //                mContext.showCheckImage(getBitmap(positon), memberBean);
//                activity.deleteImage(user);
            isCheckedArray[positon] = isChecked;
        }
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public ResultMemberBean getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    private static class ViewHolder {
        private CheckBox checkBox;
        private ImageView ivAvatar;
        private TextView tvName;
        private TextView tvLevel;
        private TextView tvTime;
        private ImageView ivSex;
    }
}