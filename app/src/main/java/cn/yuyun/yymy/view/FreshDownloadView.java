package cn.yuyun.yymy.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.AbsSavedState;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import java.util.Calendar;

import cn.yuyun.yymy.R;


/**
 * A download progressbar with cool animator
 * https://github.com/dudu90/FreshDownloadView
 */

public class FreshDownloadView extends View {


    /**view内边缘*/
    private float circular_edge;

    /**the circular radius*/
    private float radius;
    private int circular_color;
    private int circular_progress_color;
    private float circular_width;


    private Rect bounds;
    private RectF mTempBounds;
    private float mRealLeft;
    private boolean mPrepareAniRun = false;
    private float mRealTop;

    private float mProgressTextSize, mTimeTextSize;
    private Rect textBounds;
    private final String STR_PERCENT = "%";
    private float mMarkOklength;

    private AnimatorSet mOkAnimatorSet;
    private AnimatorSet mErrorAnimatorSet;
    private float mMarkArcAngle;
    private float mMarkOkdegree;
    private float mMarkOkstart;
    private boolean mMarkOkAniRun;
    private boolean mIfShowError;

    private boolean mIfShowMarkRun = false;
    private boolean mAttached;

    private boolean mUsing;
    private Path mDst = new Path();


    /**
     * the view's Status
     */
    public enum STATUS {
        PREPARE, DOWNLOADING, DOWNLOADED, ERROR
    }

    private enum STATUS_MARK {
        DRAW_ARC, DRAW_MARK
    }


    private Paint publicPaint;

    private Path path1;
    private Path path2;
    private Path path3;
    private PathMeasure pathMeasure1;
    private PathMeasure pathMeasure2;
    private PathMeasure pathMeasure3;


    private float mArrowStart;
    private float startingArrow;

    private STATUS status = STATUS.PREPARE;

    private STATUS_MARK status_mark;

    private float mProgress;
    private final static float START_ANGLE = -90f;
    private final static float TOTAL_ANGLE = 360f;
    private final static float MARK_START_ANGLE = 65f;
    private final static float DEGREE_END_ANGLE = 270f;


    private String mStatus = "INIT";
    private final String RIPPLE = "RIPPLE";
    private final String INIT = "INIT";

    public FreshDownloadView(Context context) {
        this(context, null);
    }

    public FreshDownloadView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public FreshDownloadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        circular_edge = getResources().getDimension(R.dimen.edge);

        bounds = new Rect();
        mTempBounds = new RectF();
        publicPaint = new Paint();
        path1 = new Path();
        path2 = new Path();
        path3 = new Path();
        pathMeasure1 = new PathMeasure();
        pathMeasure2 = new PathMeasure();
        pathMeasure3 = new PathMeasure();
        textBounds = new Rect();

        parseAttrs(context.obtainStyledAttributes(attrs, R.styleable.FreshDownloadView));
        initPaint();
    }

    private void parseAttrs(TypedArray array) {
        if (array != null) {
            try {
                setRadius(array.getDimension(R.styleable.FreshDownloadView_circular_radius, getResources().getDimension(R.dimen.default_radius)));
                setCircularColor(array.getColor(R.styleable.FreshDownloadView_circular_color, getResources().getColor(R.color.default_circular_color)));
                setProgressColor(array.getColor(R.styleable.FreshDownloadView_circular_progress_color, getResources().getColor(R.color.default_circular_progress_color)));
                setCircularWidth(array.getDimension(R.styleable.FreshDownloadView_circular_width, getResources().getDimension(R.dimen.default_circular_width)));
                setProgressTextSize(array.getDimension(R.styleable.FreshDownloadView_progress_text_size, getResources().getDimension(R.dimen.default_text_size)));
                setTimeTextSize(array.getDimension(R.styleable.FreshDownloadView_progress_time_size, getResources().getDimension(R.dimen.default_text_time_size)));
            } finally {
                array.recycle();
            }
        }


    }
    private long mRippleDuration = 30;
    private void initPaint() {
        publicPaint.setStrokeCap(Paint.Cap.ROUND);
        publicPaint.setStrokeWidth(getCircularWidth());
        publicPaint.setStyle(Paint.Style.STROKE);
        publicPaint.setAntiAlias(true);

        mRipplePaint = new Paint();
        int mRippleColor = 0xffeff0f2;
        mRipplePaint.setColor(mRippleColor);
        mRipplePaint.setAntiAlias(true);
    }
    private float mRippleWidth,mMaxRippleWidth;
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int dx = 0;
        int dy = 0;
        dx += getPaddingLeft() + getPaddingRight() + getCurrentWidth();
        dy += getPaddingTop() + getPaddingBottom() + getCurrentHeight();
        final int measureWidth = resolveSizeAndState(dx, widthMeasureSpec, 0);
        final int measureHeight = resolveSizeAndState(dy, heightMeasureSpec, 0);

        mRippleWidth = 5;
        mMaxRippleWidth = 50;
        mInitRippleWidth = mRippleWidth;
        setMeasuredDimension(Math.max(getSuggestedMinimumWidth(), measureWidth), Math.max(getSuggestedMinimumHeight(), measureHeight));
    }

    private int getCurrentHeight() {
        return (int) ((getRadius() * 2) + circular_edge * 2);
    }


    private int getCurrentWidth() {
        return (int) ((getRadius() * 2) + circular_edge * 2);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        final int top = 0 + getPaddingTop();
        final int bottom = getHeight() - getPaddingBottom();
        final int left = 0 + getPaddingLeft();
        final int right = getWidth() - getPaddingRight();
        updateBounds(top, bottom, left, right);
        initArrowPath(top, bottom, left, right, getRadius());
    }

    /**
     * update the Bounds of circular
     *
     * @param top
     * @param bottom
     * @param left
     * @param right
     */
    private void updateBounds(int top, int bottom, int left, int right) {
        bounds.set(left, top, right, bottom);
    }

    private void initArrowPath(int top, int bottom, int left, int right, float radius) {
        final float realTop = top + circular_edge;
        mRealLeft = left + circular_edge;
        mRealTop = realTop;
        startingArrow = realTop + radius * .48f;
        mArrowStart = startingArrow;
        status = STATUS.PREPARE;
        updateArrow();
    }

    /**
     * update the Arrow's Status
     */
    private void updateArrow() {
        path1.reset();
        path2.reset();
        path3.reset();
        path1.moveTo(mRealLeft + radius, mArrowStart);
        path1.lineTo(mRealLeft + radius, mArrowStart + radius);
        path2.moveTo(mRealLeft + radius, mArrowStart + radius);
        path2.lineTo((float) (mRealLeft + radius - Math.tan(Math.toRadians(40)) * radius * 0.46f), mArrowStart + radius - radius * .46f);
        path3.moveTo(mRealLeft + radius, mArrowStart + radius);
        path3.lineTo((float) (mRealLeft + radius + Math.tan(Math.toRadians(40)) * radius * 0.46f), mArrowStart + radius - radius * .46f);
        pathMeasure1.setPath(path1, false);
        pathMeasure2.setPath(path2, false);
        pathMeasure3.setPath(path3, false);

    }

   /* @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP: {
                if(isClickable()){
                    if (mStatus.equals(INIT)) {
                        startRippleAnim();
                    } else {
                        mStatus = INIT;
                        rippleAnimator.cancel();
                        rippleAlphaAnimator.cancel();
                        startRippleAnim();
                    }
                }
                break;
            }
            default:
        }
        super.onTouchEvent(event);
        return true;
    }*/

    private ValueAnimator rippleAnimator;
    private ValueAnimator rippleAlphaAnimator;
    private int mRippleAlpha;
    private float mInitRippleWidth;


    public void setFinish(){
        if (mStatus.equals(INIT)) {
            startRippleAnim();
        } else {
            mStatus = INIT;
            rippleAnimator.cancel();
            rippleAlphaAnimator.cancel();
            startRippleAnim();
        }
        refreshThread.interrupt();
    }

    public void stopRefresh(){
        refreshThread.interrupt();
    }

    private void startRippleAnim() {
        mStatus = RIPPLE;
        rippleAnimator = ValueAnimator.ofFloat(mRippleWidth, mMaxRippleWidth);
        rippleAlphaAnimator = ValueAnimator.ofInt(255, 0);
        rippleAnimator.setDuration(mRippleDuration);
        rippleAlphaAnimator.setDuration(mRippleDuration);
        rippleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mRippleWidth = (Float) animation.getAnimatedValue();
            }
        });
        rippleAlphaAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mRippleAlpha = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        rippleAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mRippleWidth = mInitRippleWidth;
                mRippleAlpha = 255;
                upDateProgress();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mRippleWidth = mInitRippleWidth;
                mRippleAlpha = 255;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        AnimatorSet animSet = new AnimatorSet();
        animSet.playTogether(rippleAnimator, rippleAlphaAnimator);
        animSet.start();
    }



    @Override
    protected void onDraw(Canvas canvas) {

        if(mStatus.equals(RIPPLE)){
            drawRipple(canvas);
        }

        publicPaint.setPathEffect(null);
        publicPaint.setStyle(Paint.Style.FILL);
        publicPaint.setColor(getCircularColor());
        final RectF arcBounds = mTempBounds;
        arcBounds.set(bounds);
        arcBounds.inset(circular_edge, circular_edge);
        canvas.drawArc(arcBounds, 0, 360, true, publicPaint);
        publicPaint.setStyle(Paint.Style.STROKE);
        switch (status) {
            case PREPARE:
                drawPrepare(canvas);
                break;
            case DOWNLOADING:
                drawDownLoading(canvas, arcBounds);
                break;
            case DOWNLOADED:
                drawDownLoaded(canvas, status_mark, arcBounds, mMarkArcAngle);
                break;
            default:
        }
    }

    private Paint mRipplePaint;
    void drawRipple(Canvas canvas) {
        mRipplePaint.setAlpha(mRippleAlpha);
        float mRippleRadius = mRippleWidth;
        final RectF arcBounds = mTempBounds;
        arcBounds.set(bounds);

        arcBounds.set(bounds.left - mRippleRadius,bounds.top -mRippleRadius,bounds.right + mRippleRadius, bounds.bottom+mRippleRadius);
        arcBounds.inset(circular_edge, circular_edge);
        canvas.drawArc(arcBounds, 0, 360, false, mRipplePaint);

    }

    /**
     * Draw the Arrow
     */
    private void drawPrepare(Canvas canvas) {
        publicPaint.setColor(getProgressColor());
        drawText(canvas, 1);
    }

    /**
     * Draw the Progress
     */
    private void drawDownLoading(Canvas canvas, RectF arcBounds) {
        final float progress_degree = mProgress;
        publicPaint.setColor(getProgressColor());

        if (progress_degree <= 0) {
            canvas.drawPoint(mRealLeft + radius, mRealTop, publicPaint);
        } else {
            canvas.drawArc(arcBounds, START_ANGLE, (progress_degree) * TOTAL_ANGLE, false, publicPaint);
        }
        drawText(canvas, progress_degree);
    }

    boolean isFlash;
    String minute;
    String hour;
    private void getTimeDegree() {
        Calendar calendar = Calendar.getInstance();
        minute = String.valueOf(calendar.get(Calendar.MINUTE));
        hour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
        if(minute.length() != 2){
            minute = "0" + minute;
        }
        if(hour.length() != 2){
            hour = "0" + hour;
        }
        if(isFlash){
            bottomText = hour + " " + minute;
        }else{
            bottomText = hour + ":" + minute;
        }
        isFlash = !isFlash;
    }

    public String getHour(){
        return hour;
    }

    public String getMinute(){
        return minute;
    }

    private void drawText(Canvas canvas, float progress_degree) {
        getTimeDegree();
        final Rect rect = bounds;
        publicPaint.setStyle(Paint.Style.FILL);
        publicPaint.setTextSize(getProgressTextSize());
        publicPaint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetricsInt fontMetrics = publicPaint.getFontMetricsInt();
        int baseline = (rect.bottom + rect.top - fontMetrics.bottom - fontMetrics.top) / 2;
        float textHeight = (publicPaint .descent() + publicPaint.ascent())/2;
        canvas.drawText(topText, rect.centerX(),  baseline + textHeight * 2, publicPaint);
        publicPaint.setTextSize(getProgressTextSize());
        canvas.drawText(bottomText, rect.centerX(),  baseline - textHeight * 3,publicPaint);
    }

    private String topText = "";
    private String bottomText = "";

    public void setTopText(String topText){
        this.topText = topText;
    }

    public void setBottomText(String bottomText){
        this.bottomText = bottomText;
        postInvalidate();
    }

    /**
     * Draw success
     */
    private void drawDownLoaded(Canvas canvas, STATUS_MARK status, RectF bounds, float angle) {
        publicPaint.setColor(getProgressColor());
        switch (status) {
            case DRAW_MARK:
                final Path dst = mDst;
                dst.reset();
                //to fix hardware speedup bug
                dst.lineTo(0, 0);
                pathMeasure1.getSegment(mMarkOkstart * mMarkOklength, (mMarkOkstart + mMarkOkdegree) * mMarkOklength, dst, true);
                canvas.drawPath(dst, publicPaint);
                break;
                default:
        }
    }



    public void upDateProgress() {
        setProgressInternal();
    }


    /**
     * get Use Status
     *
     * @return if use by some task.
     */
    public boolean using() {
        return mUsing;
    }

    synchronized void setProgressInternal() {
        showDownloadOk();
    }

    /**
     * showDownLoadOK
     */
    public void showDownloadOk() {
        status = STATUS.DOWNLOADED;
        makeOkPath();
        if (mOkAnimatorSet != null && mMarkOkAniRun) {
            return;
        }
        if (mOkAnimatorSet == null) {
            mOkAnimatorSet = getDownloadOkAnimator();
        }
        mOkAnimatorSet.start();
    }

    /**
     * make the Path to show
     */
    private void makeOkPath() {
        path1.reset();
        int w2 = getMeasuredWidth() / 2;
        int h2 = getMeasuredHeight() / 2;
        double a = Math.cos(Math.toRadians(25)) * getRadius();
        double c = Math.sin(Math.toRadians(25)) * getRadius();
        double l = Math.cos(Math.toRadians(53)) * 2 * a;
        double b = Math.sin(Math.toRadians(53)) * l;
        double m = Math.cos(Math.toRadians(53)) * l;
        path1.moveTo((float) (w2 - a), (float) (h2 - c));
        path1.lineTo((float) (w2 - a + m), (float) (h2 - c + Math.sin(Math.toRadians(53)) * l));
        path1.lineTo((float) (w2 + a), (float) (h2 - c));
        pathMeasure1.setPath(path1, false);
        mMarkOklength = pathMeasure1.getLength();
    }

    /**
     * create a new DownLoadOkAnimator
     *
     * @return a new Animatorset for DownloadOk.
     */
    private AnimatorSet getDownloadOkAnimator() {
        AnimatorSet animatorSet = new AnimatorSet();

        ValueAnimator roundAnimator = ValueAnimator.ofFloat(0f, MARK_START_ANGLE).setDuration(100);
        roundAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mMarkArcAngle = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        roundAnimator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                status_mark = STATUS_MARK.DRAW_ARC;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                status_mark = STATUS_MARK.DRAW_MARK;
            }
        });
        ValueAnimator firstAnimator = ValueAnimator.ofFloat(0f, 0.7f).setDuration(200);
        firstAnimator.setInterpolator(new AccelerateInterpolator());
        firstAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mMarkOkdegree = (float) animation.getAnimatedValue();
                invalidate();
            }
        });

        ValueAnimator secondAnimator = ValueAnimator.ofFloat(0f, 0.35f, 0.2f).setDuration(500);
        secondAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mMarkOkstart = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        secondAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                mIfShowMarkRun = false;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mIfShowMarkRun = false;
            }

            @Override
            public void onAnimationStart(Animator animation) {
                mIfShowMarkRun = true;
            }
        });
        animatorSet.play(firstAnimator).after(roundAnimator);
        animatorSet.play(secondAnimator).after(200);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                mMarkOkAniRun = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mMarkOkAniRun = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mMarkOkAniRun = false;
            }
        });
        return animatorSet;
    }


    public float getProgressTextSize() {
        return mProgressTextSize;
    }

    public void setProgressTextSize(float mProgressTextSize) {
        this.mProgressTextSize = mProgressTextSize;
    }


    public float getTimeTextSize() {
        return mTimeTextSize;
    }

    public void setTimeTextSize(float mTimeTextSize) {
        this.mTimeTextSize = mTimeTextSize;
    }



    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public int getCircularColor() {
        return circular_color;
    }

    public void setCircularColor(int circular_color) {
        this.circular_color = circular_color;
    }

    public int getProgressColor() {
        return circular_progress_color;
    }

    public void setProgressColor(int circular_progress_color) {
        this.circular_progress_color = circular_progress_color;
    }

    public float getCircularWidth() {
        return circular_width;
    }

    public void setCircularWidth(float circular_width) {
        this.circular_width = circular_width;
    }

    /**刷新时间线程*/
    private Thread refreshThread;

    /**秒针刷新的时间*/
    private float refresh_time = 1000;

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mAttached = true;
        refreshThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    SystemClock.sleep((long) refresh_time);
                    postInvalidate();
                }
            }
        });
        refreshThread.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mAttached = false;
        //停止刷新线程
        refreshThread.interrupt();
    }
}
