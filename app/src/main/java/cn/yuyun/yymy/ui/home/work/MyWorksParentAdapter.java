package cn.yuyun.yymy.ui.home.work;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.util.ArrayList;
import java.util.List;

import cn.lzz.utils.ResoureUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.constan.Constans;
import cn.yuyun.yymy.http.result.ResultWork;
import cn.yuyun.yymy.http.result.ResultWork2;
import cn.yuyun.yymy.http.result.StaffBean;
import cn.yuyun.yymy.sp.UserfoPrefs;
import cn.yuyun.yymy.ui.help.GlideImageLoader;
import cn.yuyun.yymy.utils.GlideHelper;

/**
 * @author
 * @desc
 * @date
 */
public class MyWorksParentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mContext;
    private List<ResultWork2> dataList = new ArrayList<>();
    AdapterListener2 onItemClickListener;
    private StaffBean staffBean;

    public MyWorksParentAdapter(Context context, StaffBean staffBean){
        this.mContext = context;
        this.staffBean = staffBean;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new HeaderViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_moments_header, parent, false));
        } else {
            return new MomentsHolder(LayoutInflater.from(mContext).inflate(R.layout.item_work_parent_my, parent, false), onItemClickListener);
        }
    }


    @Override
    public int getItemViewType(int position) {
        //0为头部布局 1为正常布局
        if (position == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            headerViewHolder.bindItem();
        } else {
            MomentsHolder headerViewHolder = (MomentsHolder) holder;
            headerViewHolder.bindItem(dataList.get(position - 1), position - 1);
        }
    }

    @Override
    public int getItemCount() {
        //因为第一个设置了header，所以总数必须+1
        return dataList.size() + 1;
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_avatar;
        RelativeLayout rl_empty;
        TextView tv_name;
        Banner banner;

        HeaderViewHolder(View view) {
            super(view);
            banner = view.findViewById(R.id.banner);
            iv_avatar = (ImageView) view.findViewById(R.id.iv_avatar);
            tv_name = view.findViewById(R.id.tv_name);
            rl_empty = (RelativeLayout) view.findViewById(R.id.rl_empty);
        }

        private void bindItem() {

            if (dataList.size() == 0) {
                rl_empty.setVisibility(View.VISIBLE);
            } else {
                rl_empty.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(staffBean.avatar)) {
                if (staffBean.avatar.startsWith(mContext.getString(R.string.HTTP))) {
                    GlideHelper.displayImage(mContext, staffBean.avatar, ResoureUtils.getDimension(mContext, R.dimen.sign_people_avatar), iv_avatar);
                } else {
                    GlideHelper.displayImage(mContext, mContext.getString(R.string.image_url_prefix) + staffBean.avatar, ResoureUtils.getDimension(mContext, R.dimen.sign_people_avatar), iv_avatar);
                }
            } else {
                if ("男".equals(staffBean.sex)) {
                    GlideHelper.displayImage(mContext, R.drawable.avatar_default_male, iv_avatar);
                } else {
                    GlideHelper.displayImage(mContext, R.drawable.avatar_default_female, iv_avatar);
                }
            }

            if (null != Constans.globalPic) {
                if (null != Constans.globalPic.resource.get(10).resourceRspList) {
                    if (Constans.globalPic.resource.get(10).resourceRspList.size() > 0) {
                        banner.startAutoPlay();
                        banner.setDelayTime(3000);
                        List<String> picList = new ArrayList<>();
                        for (int i = 0; i < Constans.globalPic.resource.get(10).resourceRspList.size(); i++) {
                            picList.add(Constans.globalPic.resource.get(10).resourceRspList.get(i).url);
                        }
                        banner.setBannerStyle(BannerConfig.NOT_INDICATOR);
                        banner.setImages(picList).setImageLoader(new GlideImageLoader()).start();
                    }
                }
            }
            tv_name.setText(staffBean.staffName);
        }
    }

     class MomentsHolder extends RecyclerView.ViewHolder {

        MyWorkAdapter adapter;
        private View line;
        private RelativeLayout rlParent;
        private RecyclerView rv;
        LinearLayoutManager mLayoutManager;
         private AdapterListener2 onItemClickListener;

        MomentsHolder(View view, AdapterListener2 onItemClickListener) {
            super(view);
            this.onItemClickListener = onItemClickListener;
            line =  view.findViewById(R.id.line);
            rlParent =  (RelativeLayout)view.findViewById(R.id.rl_parent);
            rv = (RecyclerView) view.findViewById(R.id.pull);
            mLayoutManager = new LinearLayoutManager(mContext){
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            };
            rv.setLayoutManager(mLayoutManager);
            adapter = new MyWorkAdapter(mContext);
            rv.setAdapter(adapter);
        }

        private void bindItem(final ResultWork2 bean, final int position) {
            rlParent.setVisibility(View.GONE);
            line.setVisibility(View.GONE);
            adapter.notifyDataSetChanged(bean.workVos);
            adapter.setListener(new AdapterListener() {
                @Override
                public void onUserClicked() {

                }

                @Override
                public void onFavorites(ResultWork bean) {

                }

                @Override
                public void onLike(ResultWork bean, int childPos) {

                }

                @Override
                public void onComment(ResultWork bean, int childPos) {

                }

                @Override
                public void onItemClicked(ResultWork bean, int childPos) {
                    onItemClickListener.onItemClicked(bean, position, childPos);
                }

            });
        }
    }

    public void setOnItemClickListener(AdapterListener2 onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void notifyDataSetChanged(List<ResultWork2> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addAll(List<ResultWork2> dataList) {
        int end = this.dataList.size() - 1;
        if(dataList.get(0).thisTime.equals(this.dataList.get(end).thisTime)){
            ResultWork2 bean = dataList.remove(0);
            this.dataList.get(end).workVos.addAll(bean.workVos);
            this.dataList.addAll(dataList);
        }else{
            this.dataList.addAll(dataList);
        }
        notifyDataSetChanged();
    }

    public void clear() {
        this.dataList.clear();
        notifyDataSetChanged();
    }

}