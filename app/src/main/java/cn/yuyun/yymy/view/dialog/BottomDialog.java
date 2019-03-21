package cn.yuyun.yymy.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.lzz.utils.LogUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.http.result.ResultClassfiyStoreBean;

/**
 * @author lzz
 * @desc lzz
 * @date 2018/1/31
 */

public class BottomDialog extends Dialog {

    private Context mContext;
    private RecyclerView rv;
    private List<ResultClassfiyStoreBean> mList;
    private StoreAreaDialogAdapter adapter;
    private int mPosition;

    public BottomDialog(Context context, List<ResultClassfiyStoreBean> list) {
        super(context, R.style.tip_dialog_no_status);
        mContext = context;
        mList = list;
    }

    public void notifyDataSetChanged(int position){
        adapter.notifyItemChanged(adapter.selectedPos);
        adapter.selectedPos = position;
        adapter.notifyItemChanged(adapter.selectedPos);
    }

    public void setPosition(int position){
        mPosition = position;
    }

    @Override
    public void show() {
        if(null != adapter){
            notifyDataSetChanged(mPosition);
        }
        super.show();
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.dialog_select_area);
        init();
        initView();
    }

    private void initView() {
        LogUtils.e("dialog----" + mPosition);
        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setLayoutManager(new GridLayoutManager(mContext, 3));
        adapter = new StoreAreaDialogAdapter(mContext, mPosition);
        adapter.setOnItemClickListener(new StoreAreaDialogAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(ResultClassfiyStoreBean bean, int position) {
                onClickListener.onClick(position);
            }
        });
        adapter.addAll(mList);
        rv.setAdapter(adapter);
    }

    private void init() {
        Window window = getWindow();
        // 添加动画效果
        window.setWindowAnimations(R.style.bottom_menu_animation);
        // 宽度全屏
        WindowManager windowManager = ((Activity) mContext).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.gravity = Gravity.TOP;
        lp.width = display.getWidth();
        window.setAttributes(lp);
//        window.setWindowAnimations(R.style.Animation_Popup);
        this.setCancelable(true);
        this.setCanceledOnTouchOutside(true);
    }

    public OnClickListener onClickListener;

    public void setClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(int position);
    }

}

class StoreAreaDialogAdapter extends RecyclerView.Adapter<StoreAreaDialogAdapter.ViewHolder>{

    private int RESOURCE_ID = R.layout.item_store_area_dialog;
    private Context mContext;
    private List<ResultClassfiyStoreBean> dataList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;

    public int selectedPos = 0;

    public StoreAreaDialogAdapter(Context context){
        this.mContext = context;
    }

    public StoreAreaDialogAdapter(Context context, int position){
        this.mContext = context;
        this.selectedPos = position;
    }

    @Override
    public StoreAreaDialogAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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

    public void notifyDataSetChanged(List<ResultClassfiyStoreBean> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addAll(List<ResultClassfiyStoreBean> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void clear() {
        this.dataList.clear();
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_name;

        private OnMyItemClickListener onItemClickListener;

        ViewHolder(View v, OnMyItemClickListener onItemClickListener) {
            super(v);
            tv_name = (TextView) v.findViewById(R.id.tv_name);
            this.onItemClickListener = onItemClickListener;
        }

        private void bindItem(final ResultClassfiyStoreBean bean, final int position) {
            tv_name.setText(bean.name);
            if(selectedPos == position){
                tv_name.setSelected(true);
            }else{
                tv_name.setSelected(false);
            }
            tv_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(selectedPos != position){
                        notifyItemChanged(selectedPos);
                        selectedPos = position;
                        notifyItemChanged(selectedPos);
                        if(null != onItemClickListener){
                            onItemClickListener.onItemClick(bean, position);
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
        void onItemClick(ResultClassfiyStoreBean bean, int position);
    }

}
