package cn.yuyun.yymy.utils;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import cn.lzz.utils.ToastUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.app.MyApplication;
import cn.yuyun.yymy.ui.home.actions.AddNoticeActivity;
import cn.yuyun.yymy.ui.home.appointment.AddAppointmentActivity;
import cn.yuyun.yymy.ui.home.work.PublishMomentsActivity;

/**
 * @author lzz
 * @desc lzz
 * @date 2018/4/13
 */
public class PopupMenuUtil implements View.OnTouchListener{

    private static final String TAG = "PopupMenuUtil";

    public static PopupMenuUtil getInstance() {
        return MenuUtilHolder.INSTANCE;
    }

    private static class MenuUtilHolder {
        public static PopupMenuUtil INSTANCE = new PopupMenuUtil();
    }

    private View rootVew;
    private PopupWindow popupWindow;

    private RelativeLayout rlClick;
    private ImageView ivBtn;
    private LinearLayout llTest1, llTest2, llTest3, llTest4, llTest5, llTest6, llTest7, llTest8;

    /**
     * 动画执行的 属性值数组
     */
    float animatorProperty[] = null;
    /**
     * 第一排图 距离屏幕底部的距离
     */
    int top = 0;
    /**
     * 第二排图 距离屏幕底部的距离
     */
    int bottom = 0;

    /**
     * 创建 popupWindow 内容
     *
     * @param context context
     */
    private void _createView(final Context context) {
        rootVew = LayoutInflater.from(context).inflate(R.layout.popup_menu, null);
        popupWindow = new PopupWindow(rootVew,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        //设置为失去焦点 方便监听返回键的监听
        popupWindow.setFocusable(false);

        // 如果想要popupWindow 遮挡住状态栏可以加上这句代码
//        popupWindow.setClippingEnabled(false);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(false);

        if (animatorProperty == null) {
            top = dip2px(context, 310);
            bottom = dip2px(context, 210);
            animatorProperty = new float[]{bottom, 160, -50, - 10, 0};
        }

        initLayout(context);
    }

    /**
     * dp转化为px
     *
     * @param context  context
     * @param dipValue dp value
     * @return 转换之后的px值
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 初始化 view
     */
    private void initLayout(Context context) {
        rlClick = (RelativeLayout) rootVew.findViewById(R.id.pop_rl_click);
        ivBtn = (ImageView) rootVew.findViewById(R.id.close);
        llTest1 = (LinearLayout) rootVew.findViewById(R.id.ll_work);
        llTest2 = (LinearLayout) rootVew.findViewById(R.id.ll_appointment);
        llTest3 = (LinearLayout) rootVew.findViewById(R.id.ll_unboxing);
        llTest4 = (LinearLayout) rootVew.findViewById(R.id.ll_leave);
        llTest5 = (LinearLayout) rootVew.findViewById(R.id.ll_action);
        llTest6 = (LinearLayout) rootVew.findViewById(R.id.ll_notice);


        rlClick.setOnClickListener(new MViewClick(0, context));

        llTest1.setOnClickListener(new MViewClick(1, context));
        llTest2.setOnClickListener(new MViewClick(2, context));
        llTest3.setOnClickListener(new MViewClick(3, context));
        llTest4.setOnClickListener(new MViewClick(4, context));
        llTest5.setOnClickListener(new MViewClick(5, context));
        llTest6.setOnClickListener(new MViewClick(6, context));

        llTest1.setOnTouchListener(this);
        llTest2.setOnTouchListener(this);
        llTest3.setOnTouchListener(this);
        llTest4.setOnTouchListener(this);
        llTest5.setOnTouchListener(this);
        llTest6.setOnTouchListener(this);

        mButtonScaleLargeAnimation = AnimationUtils.loadAnimation(context, R.anim.button_scale_to_large);
        mButtonScaleSmallAnimation = AnimationUtils.loadAnimation(context, R.anim.button_scale_to_small);

    }
    private Animation mButtonScaleLargeAnimation;
    private Animation mButtonScaleSmallAnimation;

    @Override
    public boolean onTouch(final View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 手指按下，按钮执行放大动画
                v.startAnimation(mButtonScaleLargeAnimation);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                // 手指移开，按钮执行缩小动画
                v.startAnimation(mButtonScaleSmallAnimation);
                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // 缩小动画执行完毕后，将按钮的动画清除。这里的150毫秒是缩小动画的执行时间。
                        v.clearAnimation();
                    }
                }, 150);
                break;
            default:
        }
        return false;
    }

    /**
     * 点击事件
     */
    private class MViewClick implements View.OnClickListener {

        public int index;
        public Context context;

        public MViewClick(int index, Context context) {
            this.index = index;
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            if (index == 0) {
                //加号按钮点击之后的执行
                _rlClickAction();
            } else {
                switch (v.getId()){
                    case R.id.ll_work:
                        context.startActivity(new Intent(context, PublishMomentsActivity.class));
                        break;
                    case R.id.ll_appointment:
                        context.startActivity(new Intent(context, AddAppointmentActivity.class));
                        break;
                    case R.id.ll_unboxing:
                        ToastUtils.toastInBottom(MyApplication.getInstance().getApplicationContext(), "敬请期待");
                        break;
                    case R.id.ll_leave:

                        break;
                    case R.id.ll_action:
//                        context.startActivity(AddNoticeActivity.startAddActionActivity(context));
                        break;
                    case R.id.ll_notice:
//                        context.startActivity(AddNoticeActivity.startAddNoticeActivity(context));
                        break;
                    default:
                }
                _close();
            }
        }
    }

    /**
     * 刚打开popupWindow 执行的动画
     */
    private void _openPopupWindowAction() {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(ivBtn, "rotation", 0f, 135f);
        objectAnimator.setDuration(200);
        objectAnimator.start();

        _startAnimation(llTest1, 370, animatorProperty);
        _startAnimation(llTest2, 400, animatorProperty);
        _startAnimation(llTest3, 450, animatorProperty);
        _startAnimation(llTest4, 500, animatorProperty);

        _startAnimation(llTest5, 370, animatorProperty);
        _startAnimation(llTest6, 400, animatorProperty);
        _startAnimation(llTest7, 450, animatorProperty);
        _startAnimation(llTest8, 500, animatorProperty);
    }

    private void dismiss(){
        _rlClickAction();
    }

    /**
     * 关闭 popupWindow执行的动画
     */
    public void _rlClickAction() {
        if (ivBtn != null && rlClick != null) {

            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(ivBtn, "rotation", 135f, 0f);
            objectAnimator.setDuration(300);
            objectAnimator.start();

            _closeAnimation(llTest1, 100, top);
            _closeAnimation(llTest2, 200, top);
            _closeAnimation(llTest3, 200, top);
            _closeAnimation(llTest4, 300, top);
            _closeAnimation(llTest5, 100, bottom);
            _closeAnimation(llTest6, 200, bottom);
            _closeAnimation(llTest7, 200, bottom);
            _closeAnimation(llTest8, 300, bottom);

            rlClick.postDelayed(new Runnable() {
                @Override
                public void run() {
                    _close();
                }
            }, 350);

        }
    }


    /**
     * 弹起 popupWindow
     *
     * @param context context
     * @param parent  parent
     */
    public void _show(Context context, View parent) {
        _createView(context);
        if (popupWindow != null && !popupWindow.isShowing()) {
            popupWindow.showAtLocation(parent, Gravity.NO_GRAVITY, 0, 0);
            _openPopupWindowAction();
        }
    }

    /**
     * 关闭popupWindow
     */

    public void _close() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            popupWindow = null;
        }
    }

    /**
     * @return popupWindow 是否显示了
     */
    public boolean _isShowing() {
        if (popupWindow == null) {
            return false;
        } else {
            return popupWindow.isShowing();
        }
    }

    /**
     * 关闭 popupWindow 时的动画
     *
     * @param view     mView
     * @param duration 动画执行时长
     * @param next     平移量
     */
    private void _closeAnimation(View view, int duration, int next) {
        ObjectAnimator anim = ObjectAnimator.ofFloat(view, "translationY", 0f, next);
        anim.setDuration(duration);
        anim.start();
    }

    /**
     * 启动动画
     *
     * @param view     view
     * @param duration 执行时长
     * @param distance 执行的轨迹数组
     */
    private void _startAnimation(View view, int duration, float[] distance) {
        ObjectAnimator anim = ObjectAnimator.ofFloat(view, "translationY", distance);
        anim.setDuration(duration);
        anim.start();
    }


}
