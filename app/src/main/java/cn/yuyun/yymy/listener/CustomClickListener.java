package cn.yuyun.yymy.listener;

import android.view.View;

/**
 * @author
 * @desc
 * @date
 */
public abstract class CustomClickListener implements View.OnClickListener {

    private long mLastClickTime;
    private long timeInterval = 1300L;

    public CustomClickListener() {

    }

    public CustomClickListener(long interval) {
        this.timeInterval = interval;
    }

    @Override
    public void onClick(View v) {
        long nowTime = System.currentTimeMillis();
        if (nowTime - mLastClickTime > timeInterval) {
            // 单次点击事件
            onSingleClick();
            mLastClickTime = nowTime;
        } else {
            // 快速点击事件
            onFastClick();
        }
    }

    protected abstract void onSingleClick();
    protected abstract void onFastClick();
}
