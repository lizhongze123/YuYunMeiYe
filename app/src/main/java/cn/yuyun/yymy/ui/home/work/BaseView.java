package cn.yuyun.yymy.ui.home.work;

import android.app.Activity;
import android.content.Context;

/**
 * @author lzz
 * @desc lzz
 * @date 2018/2/26
 */

public interface BaseView<T> {
    void setPresenter(T presenter);
    Context getBaseContext();
    Activity getBaseActivity();
}
