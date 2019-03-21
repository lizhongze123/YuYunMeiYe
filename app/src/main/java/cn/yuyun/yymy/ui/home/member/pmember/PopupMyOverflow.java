package cn.yuyun.yymy.ui.home.member.pmember;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import cn.yuyun.yymy.R;

/**
 * @author lzz
 * @desc lzz
 * @date 2018/3/23
 */

public class PopupMyOverflow extends PopupWindow implements View.OnClickListener {

    private LayoutInflater inflater;
    private Context mContext;
    private View contentView;

    public PopupMyOverflow(Context context) {
        mContext = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        initPopUpMyOverflow();
    }

    private void initPopUpMyOverflow() {
        contentView = LayoutInflater.from(mContext).inflate(R.layout.pop_pmember_classify, null);
        setContentView(contentView);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
        setFocusable(true);
        setOutsideTouchable(true);

        contentView.findViewById(R.id.tv_one).setOnClickListener(this);
        contentView.findViewById(R.id.tv_two).setOnClickListener(this);
        contentView.findViewById(R.id.tv_three).setOnClickListener(this);
        contentView.findViewById(R.id.tv_four).setOnClickListener(this);
        contentView.findViewById(R.id.rl).setOnClickListener(this);
    }

    @Override
    public void showAsDropDown(View anchor) {
        if (Build.VERSION.SDK_INT >= 24) {
            Rect rect = new Rect();
            anchor.getGlobalVisibleRect(rect);
            int h = anchor.getResources().getDisplayMetrics().heightPixels - rect.bottom;
            setHeight(h);
        }
        super.showAsDropDown(anchor);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_one:
                itemClickListener.onItemClick(30);
                break;
            case R.id.tv_two:
                itemClickListener.onItemClick(90);
                break;
            case R.id.tv_three:
                itemClickListener.onItemClick(180);
                break;
            case R.id.tv_four:
                itemClickListener.onItemClick(360);
                break;
            case R.id.rl:
                break;
                default:
        }
        dismiss();
    }

    ItemClickListener itemClickListener;

    interface ItemClickListener{
        void onItemClick(int value);
    }

    public void setOnItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }
}

