package cn.yuyun.yymy.view.rv;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import cn.yuyun.yymy.R;

/**
 * @author lzz
 * @desc lzz
 * @date 2018/10/11
 */
public class SimpleRefreshHeadView extends AbRefreshHeadView {
   /* private ImageView mIvAnim;
    private TextView mTvTip;*/

    public SimpleRefreshHeadView(Context context) {
        super(context);
    }

    @Override
    protected View onCreateView(Context context) {
        return LayoutInflater.from(context).inflate(R.layout.view_refresh_head, null);
    }

    @Override
    protected int onCreateRefreshLimitHeight() {
        return getScreenHeight() / 10;
    }

    @Override
    protected void initView() {
//        mIvAnim = (ImageView) findViewFromId(R.id.iv_refreshing);
//        mTvTip = (TextView) findViewFromId(R.id.tv_tip);
    }

    /**
     * 获取屏幕高度
     */
    private int getScreenHeight() {
        WindowManager wm = (WindowManager) getContext().getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getHeight();
    }


    private void rotationAnimator(float rotation) {
       /* ValueAnimator animator = ValueAnimator.ofFloat(mIvAnim.getRotation(), rotation);
        animator.setDuration(200).start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mIvAnim.setRotation((Float) animation.getAnimatedValue());
            }
        });
        animator.start();*/
    }

    @Override
    protected void onPullingDown() {
       /* mIvAnim.setVisibility(View.VISIBLE);
        mTvTip.setText("下拉刷新");
       mIvAnim.setImageResource(R.mipmap.ic_launcher);
        rotationAnimator(0f);*/
    }

    @Override
    protected void onReleaseState() {
     /*   mIvAnim.setVisibility(View.VISIBLE);
        mTvTip.setText("释放立即刷新");
        mIvAnim.setImageResource(R.mipmap.ic_launcher);
        rotationAnimator(180f);*/
    }

    @Override
    protected void onRefreshing() {
//        mTvTip.setText("正在刷新");
//        mIvAnim.setImageResource(R.mipmap.ic_launcher);
//        mIvAnim.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.rotate));
    }

    @Override
    protected void onResultSuccess() {
//        mIvAnim.clearAnimation();
//        mTvTip.setText(mContext.getString(R.string.refresh_success));
//        mIvAnim.setVisibility(View.GONE);
    }

    @Override
    protected void onResultFail() {
//        mIvAnim.clearAnimation();
//        mTvTip.setText(mContext.getString(R.string.refresh_fail));
//        mIvAnim.setVisibility(View.GONE);
    }
}
