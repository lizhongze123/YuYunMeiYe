package cn.yuyun.yymy.main;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.animation.LinearInterpolator;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.lzz.utils.LogUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseNoTitleActivity;
import cn.yuyun.yymy.event.NotifyEvent;
import cn.yuyun.yymy.utils.EvenManager;
import cn.yuyun.yymy.view.NavFragment;
import cn.yuyun.yymy.view.NavigationButton;

/**
 *
 * @author
 * @date
 */

public class MainActivity extends BaseNoTitleActivity implements NavFragment.OnNavigationReselectListener{

    private NavFragment mNavBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EvenManager.register(this);
    }

 /*   @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            //横屏
            LogUtils.e("横屏");
        }else{
            //竖屏
            LogUtils.e("竖屏");
        }
    }*/

    @Override
    protected void setUpViewAndData() {
        initViews();
    }

    /*@Override
    protected void restartApp() {
        LogUtils.e("应用被回收重启----");
        startActivity(new Intent(this,TransitionActivity.class));
        finish();
    }*/

   /* @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int action = intent.getIntExtra(KEY_HOME_ACTION, ACTION_BACK_TO_HOME);
        switch (action) {
            case ACTION_RESTART_APP:
                restartApp();
                break;
                default:
        }
    }*/

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNotifyEvent(NotifyEvent notifyEvent) {
        if(notifyEvent.tag == NotifyEvent.LOGOUT){
            finish();
        }else if(notifyEvent.tag == NotifyEvent.LOGIN){
            finish();
        }
    }

    private void initViews() {
        FragmentManager manager = getSupportFragmentManager();
        mNavBar = ((NavFragment) manager.findFragmentById(R.id.fag_nav));
        mNavBar.setup(this, manager, R.id.main_container, this);
    }

    public void doDiscovery(){
        mNavBar.select();
    }

    @Override
    public void onReselect(NavigationButton navigationButton) {
//        Fragment fragment = navigationButton.getFragment();
//        if (fragment != null
//                && fragment instanceof OnTabReselectListener) {
//            OnTabReselectListener listener = (OnTabReselectListener) fragment;
//            listener.onTabReselect();
//        }
    }

    public void toggleNavTabView(boolean isShowOrHide) {
        final View view = mNavBar.getView();
        if (view == null) {
            return;
        }
        // hide
        view.setVisibility(View.VISIBLE);
        if (!isShowOrHide) {
            view.animate()
                    .translationY(view.getHeight())
                    .setDuration(180)
                    .setInterpolator(new LinearInterpolator())
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            view.setTranslationY(view.getHeight());
                            view.setVisibility(View.GONE);
                        }
                    });
        } else {
            view.animate()
                    .translationY(0)
                    .setDuration(180)
                    .setInterpolator(new LinearInterpolator())
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            // fix:bug > 点击隐藏的同时，快速点击显示
                            view.setVisibility(View.VISIBLE);
                            view.setTranslationY(0);
                        }
                    });
        }
    }

    private boolean isExit = false;

    @Override
    public void onBackPressed() {
        if(isExit){
            super.onBackPressed();
        }else{
            isExit = true;
            showToast("再次点击将退出程序");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isExit = false;
                }
            }, 2000);
        }
    }


}

