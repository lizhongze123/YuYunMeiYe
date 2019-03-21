package cn.yuyun.yymy.view.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ajguan.library.IRefreshHeader;
import com.ajguan.library.State;

import cn.yuyun.yymy.R;
import cn.yuyun.yymy.view.refresh.RefreshAnimView;

/**
 * @author lzz
 * @desc lzz
 * @date 2018/5/8
 */
public class RefreshHeaderView extends FrameLayout implements IRefreshHeader {

    private Animation rotate_up;
    private Animation rotate_down;
    private Animation rotate_infinite;
    private TextView textView;
    private RefreshAnimView arrowIcon;
    private View successIcon;
    private View loadingIcon;
    private RelativeLayout rl;
    private Context mContext;

    public RefreshHeaderView(Context context) {
        this(context, null);
        mContext = context;
    }

    public RefreshHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // 初始化动画
        rotate_up = AnimationUtils.loadAnimation(context, R.anim.button_scale_to_large);
        rotate_down = AnimationUtils.loadAnimation(context, R.anim.rotate_down);
        rotate_infinite = AnimationUtils.loadAnimation(context, R.anim.rotate_infinite);

        inflate(context, R.layout.refresh_header, this);

        textView = (TextView) findViewById(R.id.text);
        arrowIcon = (RefreshAnimView) findViewById(R.id.arrowIcon);
        successIcon = findViewById(R.id.successIcon);
        loadingIcon = findViewById(R.id.loadingIcon);

        rl = (RelativeLayout) findViewById(R.id.rl_refresh);
    }

    @Override
    public void reset() {
        successIcon.setVisibility(INVISIBLE);
        arrowIcon.setVisibility(VISIBLE);
        arrowIcon.clearAnimation();
        loadingIcon.setVisibility(INVISIBLE);
        loadingIcon.clearAnimation();
    }

    @Override
    public void pull() {

    }

    @Override
    public void refreshing() {
        loadingIcon.setVisibility(VISIBLE);
        loadingIcon.startAnimation(rotate_infinite);
        arrowIcon.setVisibility(INVISIBLE);
        textView.setText("等等我，我快好了");
        arrowIcon.clearAnimation();
    }

    @Override
    public void onPositionChange(float currentPos, float lastPos, float refreshPos, boolean isTouch, State state) {
        //图片放大缩小比例
        float scaleProgress = currentPos / refreshPos;
        if(scaleProgress > 1){
            scaleProgress = 1;
        }
        //设置refreshAnimView的缩放进度
        arrowIcon.setCurrentProgress(scaleProgress);
        arrowIcon.invalidate();
        textView.setAlpha(scaleProgress);
    }

    @Override
    public void complete() {
        loadingIcon.setVisibility(INVISIBLE);
        loadingIcon.clearAnimation();
        successIcon.setVisibility(VISIBLE);
    }

    public void setBg(){
        rl.setBackground(mContext.getResources().getDrawable(R.drawable.ornaments));
    }
}
