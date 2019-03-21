package cn.yuyun.yymy.ui.store.report;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.yuyun.yymy.R;


public class SelectYearPopupWindow extends PopupWindow {

    private Context context;
    private OnPopItemClick popClick;
    private QueryFieldAdapter topAdapter;

    public interface OnPopItemClick {
        void OnPopItemClickListener(String item, int position);
    }

    public SelectYearPopupWindow(Context context, OnPopItemClick popClick) {
        super(context);
        this.context = context;

        this.popClick = popClick;
        List<String>list = new ArrayList<>();
        for (int i = 2018; i > 2000; i--) {
            list.add(i + "");
        }
        topAdapter = new QueryFieldAdapter(context, list);
        initView(context);
    }

    private void initView(Context context) {
        View view = View.inflate(context, R.layout.popupwindow_year, null);
        setPopContent(view);
        setContentView(view);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        int h = ((Activity)context).getWindowManager().getDefaultDisplay().getHeight();
        setHeight(h / 2);
        setFocusable(true);
        setBackgroundDrawable(new ColorDrawable());
        setOutsideTouchable(true);
    }


    public void show(View v){
        showAsDropDown(v, 0, 0);
    }

    private void setPopContent(View view) {
        ListView parentList = (ListView) view.findViewById(R.id.top_query_field_list);
        parentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                QueryField item = (QueryField) parent.getItemAtPosition(position);
                popClick.OnPopItemClickListener((String) parent.getItemAtPosition(position), position);
                dismiss();
            }
        });
        parentList.setAdapter(topAdapter);
    }

    private class QueryFieldAdapter extends ArrayAdapter<String> {

        public QueryFieldAdapter(Context context, List<String> queryFields) {
            super(context, R.layout.popupwindow_item_year, queryFields);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder mHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.popupwindow_item_year, null);
                mHolder = new ViewHolder();
                mHolder.item_name = (TextView) convertView.findViewById(R.id.item_name);
                convertView.setTag(mHolder);
            } else {
                mHolder = (ViewHolder) convertView.getTag();
            }
            setItemData(mHolder, getItem(position));
            return convertView;
        }
    }

    private class ViewHolder {
        TextView item_name;
    }

    private void setItemData(ViewHolder mHolder, String item) {
        if(item == null) {
            return;
        }
        mHolder.item_name.setText(item);
    }

}
