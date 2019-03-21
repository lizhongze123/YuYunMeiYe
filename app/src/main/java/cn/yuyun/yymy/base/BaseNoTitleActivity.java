package cn.yuyun.yymy.base;

import android.view.View;


public abstract class BaseNoTitleActivity extends BaseActivity {

    @Override
    protected void init() {
        super.init();
        titleBar.setVisibility(View.GONE);
        titleBar.setLineIsVisible(View.GONE);
    }
}
