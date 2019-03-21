package cn.yuyun.yymy.ui.home.member.memberdata;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.http.result.ResultLabel;

/**
 * @author lzz
 * @desc
 * @date 2018-01-17
 */
public class LabelAdapter extends RecyclerView.Adapter<LabelAdapter.ViewHolder>{

    private int RESOURCE_ID = R.layout.item_label_del;
    private Context mContext;
    private List<ResultLabel> dataList = new ArrayList<>();
    private List<Integer> colors;

    public LabelAdapter(Context context){
        this.mContext = context;
        initColors(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(RESOURCE_ID, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindItem(dataList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void notifyDataSetChanged(List<ResultLabel> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addAll(List<ResultLabel> dataList) {
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

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvLabel, tvDel;
        private RelativeLayout rl;

        ViewHolder(View view) {
            super(view);
            tvLabel = (TextView) view.findViewById(R.id.tv_label);
            tvDel = (TextView) view.findViewById(R.id.tv_del);
            rl = (RelativeLayout) view.findViewById(R.id.rl);
        }

        private void bindItem(final ResultLabel bean, final int position) {
            tvLabel.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            tvLabel.setSingleLine(true);
            tvLabel.setSelected(true);
            tvLabel.setFocusable(true);
            tvLabel.setFocusableInTouchMode(true);
            TextViewUtils.setTextOrEmpty(tvLabel, bean.kv_value);
            rl.setBackgroundColor(getColor(position));
            tvDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(null != onDelListener){
                        onDelListener.onDel(bean.id, position);
                    }
                }
            });
        }
    }

    interface OnDelListener{
        void onDel(int id, int position);
    }

    OnDelListener onDelListener;

    public void setOnDelListener(OnDelListener onDelListener){
        this.onDelListener = onDelListener;
    }

    private int getRandomColor() {
        int color;
        color = colors.get(new Random().nextInt(13));
        return color;
    }

    public int getColor(int pos){
       return colors.get(pos % colors.size());
    }

    private void initColors(Context context) {
        if(colors == null){
            colors = new ArrayList<>();
            int color1 = context.getResources().getColor(R.color.index_colors_1);
            int color2 = context.getResources().getColor(R.color.index_colors_2);
            int color3 = context.getResources().getColor(R.color.index_colors_3);
            int color4 = context.getResources().getColor(R.color.index_colors_4);
            int color5 = context.getResources().getColor(R.color.index_colors_5);
            int color6 = context.getResources().getColor(R.color.index_colors_6);
            int color7 = context.getResources().getColor(R.color.index_colors_7);
            int color8 = context.getResources().getColor(R.color.index_colors_8);
            int color9 = context.getResources().getColor(R.color.index_colors_9);
            int color10 = context.getResources().getColor(R.color.index_colors_10);
            int color11 = context.getResources().getColor(R.color.index_colors_11);
            int color12= context.getResources().getColor(R.color.index_colors_12);
            int color13 = context.getResources().getColor(R.color.index_colors_13);
            colors.add(color1);
            colors.add(color2);
            colors.add(color3);
            colors.add(color4);
            colors.add(color5);
            colors.add(color6);
            colors.add(color7);
            colors.add(color8);
            colors.add(color9);
            colors.add(color10);
            colors.add(color11);
            colors.add(color12);
            colors.add(color13);
        }
    }

}