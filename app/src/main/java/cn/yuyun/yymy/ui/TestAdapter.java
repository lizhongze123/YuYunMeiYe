package cn.yuyun.yymy.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.yuyun.yymy.R;

/**
 * @author lzz
 * @desc lzz
 * @date 2018/12/27
 */
public class TestAdapter  extends RecyclerView.Adapter<TestAdapter.ViewHolder> {

    private Context mContext;

    public TestAdapter(Context context) {
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_work_like_person, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {


    }

    @Override
    public int getItemCount() {
        return 10;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        View card;

        public ViewHolder(View itemView) {
            super(itemView);
            card = itemView;
        }


        @Override
        public void onClick(View v) {

        }
    }

}
