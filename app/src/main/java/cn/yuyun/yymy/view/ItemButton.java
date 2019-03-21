package cn.yuyun.yymy.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.RequiresApi;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import cn.yuyun.yymy.R;

public class ItemButton extends FrameLayout {

    private Fragment mFragment = null;
    private ImageView mIconView;
    private TextView mTitleView;
    private TextView mDot;
    private String mTag;

    public ItemButton(Context context) {
        super(context);
        init();
    }

    public ItemButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ItemButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ItemButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.layout_item_button, this, true);

        mIconView = (ImageView) findViewById(R.id.nav_iv_icon);
        mTitleView = (TextView) findViewById(R.id.nav_tv_title);
        mDot = (TextView) findViewById(R.id.nav_tv_dot);
    }

    public void setSelected2(boolean selected) {
        super.setSelected(selected);
        mIconView.setSelected(selected);
        mTitleView.setSelected(selected);
    }

    public void showRedDot(int count, boolean isDot) {
        if(isDot){
            mDot.setVisibility(count > 0 ? VISIBLE : GONE);
            mDot.setText("");
        }else{
            mDot.setVisibility(count > 0 ? VISIBLE : GONE);
            mDot.setText(String.valueOf(count));
        }
    }

    public void init(@DrawableRes int resId, @StringRes int strId) {
        mIconView.setImageResource(resId);
        mTitleView.setText(strId);
    }

    public void init(@DrawableRes int resId, String strId) {
        mIconView.setImageResource(resId);
        if(!TextUtils.isEmpty(strId)){
            mTitleView.setText(strId);
        }
    }

    public Fragment getFragment() {
        return mFragment;
    }

    public void setFragment(Fragment fragment) {
        this.mFragment = fragment;
    }

    @Override
    public String getTag() {
        return mTag;
    }

}
