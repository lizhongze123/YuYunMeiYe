package cn.yuyun.yymy.ui.home.appointment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import cn.yuyun.yymy.R;

/**
 * @author lzz
 * @desc
 * @date 2018-01-17
 */
public class SelectTimeAdapter2 extends RecyclerView.Adapter<SelectTimeAdapter2.ViewHolder> {

    private int RESOURCE_ID = R.layout.item_time2;
    private Context context;
    private List<String> dataList = new ArrayList<>();
    private List<Integer> resultList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;

    public SelectTimeAdapter2(Context context, List<String> list) {
        this.context = context;
        this.dataList = list;
    }

    public SelectTimeAdapter2(Context context) {
        this.context = context;
        isSelected = new LinkedHashMap<>();

    }

    private void initDate() {
        for (int i = 0; i < dataList.size(); i++) {
            isSelected.put(i, false);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context).inflate(RESOURCE_ID, parent, false);
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

    public void notifyDataSetChanged(List<String> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addAll(List<String> dataList) {
        this.dataList.addAll(dataList);
        initDate();
        notifyDataSetChanged();
    }

    public void clear() {
        this.dataList.clear();
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private SelectTimeAdapter2.OnMyItemClickListener onItemClickListener;
        private RelativeLayout rl;
        private CheckBox cbTime;

        ViewHolder(View view, SelectTimeAdapter2.OnMyItemClickListener onItemClickListener) {
            super(view);
            cbTime = (CheckBox) view.findViewById(R.id.cb_name);
            rl = (RelativeLayout) itemView.findViewById(R.id.rl);
            this.onItemClickListener = onItemClickListener;
        }

        private void bindItem(final String bean, final int position) {

            cbTime.setChecked(isSelected.get(position));
            cbTime.setText(bean);
            //动态添加上午和下午中间的间隔
            if (position == 8 || position == 9) {
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) rl.getLayoutParams();
                lp.setMargins(0, 20, 0, 0);
                rl.setLayoutParams(lp);
            }
            cbTime.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        resultList.add(position);
                        isSelected.put(position, true);
                    } else {
                        resultList.remove(position);
                        isSelected.put(position, false);
                    }
                }
            });
        }
    }

    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnMyItemClickListener {
        void onItemClick(View view, String bean, int position);
    }

    // 用来控制CheckBox的选中状况
    private LinkedHashMap<Integer, Boolean> isSelected;


    //供activity获取选中的数据
    public List<Integer> getSelectedData() {
        List<Integer> mSelectedData = new ArrayList<>();
        for (Integer key : isSelected.keySet()) {
            if(isSelected.get(key)){
                mSelectedData.add(key);
            }
        }
        return mSelectedData;

    }

}