package cn.yuyun.yymy.base;

import android.view.View;


public abstract class BaseNoTitleFragment extends BaseFragment {


    @Override
    protected int getLayoutId() {
        return getTheLayoutId();
    }

    @Override
    protected void initViews(View root) {
        super.initViews(root);
        titleBar.setVisibility(View.GONE);
    }

    protected abstract int getTheLayoutId();
}
