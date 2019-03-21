package cn.yuyun.yymy.ui.store.report;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.lzz.utils.DateTimeUtils;
import cn.lzz.utils.ResoureUtils;
import cn.yuyun.yymy.R;


public class SelectMonthPopupWindow extends PopupWindow {

    private Context context;
    private OnPopItemClick popClick;
    private ItemAdapter itemAdapter;
    private ItemAdapterTop itemAdapterTop;

    public interface OnPopItemClick {
        void OnPopItemClickListener(String item, int position);
    }

    public SelectMonthPopupWindow(Context context, OnPopItemClick popClick) {
        super(context);
        this.context = context;

        this.popClick = popClick;
        List<String> list = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            list.add(i + "");
        }
        itemAdapter = new ItemAdapter(context, list);
        List<String> listYear = new ArrayList<>();
        for (int i = 2018; i > 2000; i--) {
            listYear.add(i + "");
        }
        itemAdapterTop = new ItemAdapterTop(context, listYear);
        initView(context);
    }

    private void initView(Context context) {
        View view = View.inflate(context, R.layout.popupwindow_month, null);
        setPopContent(view);
        setContentView(view);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        setBackgroundDrawable(new ColorDrawable());
        setOutsideTouchable(true);
    }


    public void show(View v) {
        showAsDropDown(v, 0, 0);
    }

    private void setPopContent(View view) {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        RecyclerView recyclerViewYear = (RecyclerView) view.findViewById(R.id.recyclerViewYear);
        itemAdapter.setOnItemClickListener(new ItemAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(String value, int position) {
                String year = itemAdapterTop.getItem();
                popClick.OnPopItemClickListener(year + "-" + DateTimeUtils.thanTen(Integer.valueOf(value)), position);
                dismiss();
            }
        });
        itemAdapterTop.setOnItemClickListener(new ItemAdapterTop.OnMyItemClickListener() {
            @Override
            public void onItemClick(String value, int position) {

            }
        });
        recyclerView.setLayoutManager(new GridLayoutManager(context, 7));
        recyclerView.setAdapter(itemAdapter);
        recyclerViewYear.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewYear.setAdapter(itemAdapterTop);
    }

    private static class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemHolder> {

        private int RESOURCE_ID = R.layout.popupwindow_item_month;
        private Context context;
        private List<String> list;
        OnMyItemClickListener onItemClickListener;
        /**
         * 用来记录点击的item
         */
        private int selectedPos;

        public ItemAdapter(Context context, List<String> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View rootView = LayoutInflater.from(context).inflate(RESOURCE_ID, parent, false);
            return new ItemHolder(rootView, onItemClickListener);
        }

        @Override
        public void onBindViewHolder(ItemHolder holder, int position) {
            holder.bindItem(list.get(position), position);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ItemHolder extends RecyclerView.ViewHolder {

            private ItemAdapter.OnMyItemClickListener onItemClickListener;
            private RelativeLayout rl;
            private TextView tv;

            ItemHolder(View view, ItemAdapter.OnMyItemClickListener onItemClickListener) {
                super(view);
                rl = (RelativeLayout) itemView.findViewById(R.id.rl);
                tv = (TextView) view.findViewById(R.id.item_name);
                this.onItemClickListener = onItemClickListener;
            }

            private void bindItem(final String bean, final int position) {
                if (selectedPos == position) {
                    tv.setBackground(ResoureUtils.getDrawable(context, R.drawable.month_bg_green));
                    tv.setTextColor(Color.parseColor("#ffffff"));
                } else {
                    tv.setBackground(ResoureUtils.getDrawable(context, R.drawable.month_bg_white));
                    tv.setTextColor(Color.parseColor("#1C1C1C"));
                }
                tv.setText(bean);
                rl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onItemClickListener != null) {
                            clearSelection(position);
                            notifyDataSetChanged();
                            onItemClickListener.onItemClick(bean, position);
                        }
                    }
                });
            }
        }

        public void clearSelection(int pos) {
            selectedPos = pos;
        }

        public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        public interface OnMyItemClickListener {
            void onItemClick(String value, int position);
        }

    }

    private static class ItemAdapterTop extends RecyclerView.Adapter<ItemAdapterTop.ItemHolder> {

        private int RESOURCE_ID = R.layout.popupwindow_item_month_year;
        private Context context;
        private List<String> list;
        OnMyItemClickListener onItemClickListener;
        /**
         * 用来记录点击的item
         */
        private int selectedPos;

        public ItemAdapterTop(Context context, List<String> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View rootView = LayoutInflater.from(context).inflate(RESOURCE_ID, parent, false);
            return new ItemHolder(rootView, onItemClickListener);
        }

        @Override
        public void onBindViewHolder(ItemHolder holder, int position) {
            holder.bindItem(list.get(position), position);
        }

        public String getItem(){
            return list.get(selectedPos);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ItemHolder extends RecyclerView.ViewHolder {

            private ItemAdapterTop.OnMyItemClickListener onItemClickListener;
            private RelativeLayout rl;
            private TextView tv;
            private View line;

            ItemHolder(View view, ItemAdapterTop.OnMyItemClickListener onItemClickListener) {
                super(view);
                rl = (RelativeLayout) itemView.findViewById(R.id.rl);
                tv = (TextView) view.findViewById(R.id.item_name);
                line = view.findViewById(R.id.line);
                this.onItemClickListener = onItemClickListener;
            }

            private void bindItem(final String bean, final int position) {
                if (selectedPos == position) {
                    tv.setTextColor(Color.parseColor("#1C1C1C"));
                    line.setVisibility(View.VISIBLE);
                } else {
                    tv.setTextColor(Color.parseColor("#7A7A7A"));
                    line.setVisibility(View.GONE);
                }
                tv.setText(bean);
                rl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onItemClickListener != null) {
                            clearSelection(position);
                            notifyDataSetChanged();
                            onItemClickListener.onItemClick(bean, position);
                        }
                    }
                });
            }
        }

        public void clearSelection(int pos) {
            selectedPos = pos;
        }

        public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        public interface OnMyItemClickListener {
            void onItemClick(String value, int position);
        }

    }

}
