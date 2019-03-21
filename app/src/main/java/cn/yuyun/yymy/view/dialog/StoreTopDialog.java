package cn.yuyun.yymy.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.yuyun.yymy.R;
import cn.yuyun.yymy.http.result.ResultClassfiyStoreBean.OgServiceplacesRspListBean;

/**
 * @author
 * @desc
 * @date
 */
public class StoreTopDialog extends Dialog {

    private Context mContext;
    private RecyclerView rv;
    private ImageView ivBack;
    private TextView tvSelect;
    private List<OgServiceplacesRspListBean> mList;
    private StoreDialogAdapter adapter;
    private int mPosition;

    public StoreTopDialog(Context context, List<OgServiceplacesRspListBean> list) {
        super(context, R.style.tip_dialog_no_status);
        mContext = context;
        mList = list;
    }

    @Override
    public void dismiss() {
        onClickListener.onDismiss();
        super.dismiss();
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
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.dialog_staff_filter);
        init();
        initView();
    }

    private void initView() {
        rv = (RecyclerView) findViewById(R.id.rv);
        findViewById(R.id.rl_title).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        rv.setLayoutManager(new GridLayoutManager(mContext, 4));
        adapter = new StoreDialogAdapter(mContext, mPosition);
        adapter.setOnItemClickListener(new StoreDialogAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(OgServiceplacesRspListBean bean, int position) {
                tvSelect.setText(bean.sp_name);
                onClickListener.onClick(bean, position);
            }
        });
        adapter.addAll(mList);
        rv.setAdapter(adapter);
        tvSelect = (TextView) findViewById(R.id.tv_select);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void init() {
        Window window = getWindow();
        window.getDecorView().setPadding(0,0,0,0);
        // 添加动画效果
        window.setWindowAnimations(R.style.bottom_menu_animation);
        // 宽度全屏
        WindowManager windowManager = ((Activity) mContext).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.gravity = Gravity.TOP;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);
        this.setCancelable(true);
        this.setCanceledOnTouchOutside(true);
    }

    public OnClickListener onClickListener;

    public void setClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(OgServiceplacesRspListBean bean, int position);
        void onDismiss();
    }

}

class StoreDialogAdapter extends RecyclerView.Adapter<StoreDialogAdapter.ViewHolder>{

    private int RESOURCE_ID = R.layout.item_store_dialog;
    private Context mContext;
    private List<OgServiceplacesRspListBean> dataList = new ArrayList<>();
    OnMyItemClickListener onItemClickListener;

    public int selectedPos = 0;

    public StoreDialogAdapter(Context context){
        this.mContext = context;
    }

    public StoreDialogAdapter(Context context, int position){
        this.mContext = context;
        this.selectedPos = position;
    }

    @Override
    public StoreDialogAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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

    public void notifyDataSetChanged(List<OgServiceplacesRspListBean> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addAll(List<OgServiceplacesRspListBean> dataList) {
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

        private void bindItem(final OgServiceplacesRspListBean bean, final int position) {
            tv_name.setText(bean.sp_name);

            if(selectedPos == position){
                  /*  tv_name.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
                    tv_name.setBackground(mContext.getResources().getDrawable(R.drawable.store_area_bg));*/
                tv_name.setSelected(true);
            }else{
                   /* tv_name.setTextColor(mContext.getResources().getColor(R.color.gray_99));
                    tv_name.setBackground(mContext.getResources().getDrawable(R.color.white));*/
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
        void onItemClick(OgServiceplacesRspListBean bean, int position);
    }


}
