package cn.yuyun.yymy.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;

import java.util.Calendar;

import cn.yuyun.yymy.R;


public class SignTimeView extends View {

    /**view内边缘*/
    private float circular_edge;

    /**the circular radius*/
    private float radius;
    private int circular_color;
    private int circular_progress_color;
    private float circular_width;

    private Rect bounds;
    private RectF mTempBounds;

    private float mProgressTextSize, mTimeTextSize;

    private Paint publicPaint;

    public SignTimeView(Context context) {
        this(context, null);
    }

    public SignTimeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public SignTimeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        circular_edge = getResources().getDimension(R.dimen.edge);

        bounds = new Rect();
        mTempBounds = new RectF();
        publicPaint = new Paint();

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

    private void initPaint() {
        publicPaint.setStrokeCap(Paint.Cap.ROUND);
        publicPaint.setStrokeWidth(getCircularWidth());
        publicPaint.setStyle(Paint.Style.STROKE);
        publicPaint.setAntiAlias(true);
    }

/*    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int dx = 0;
        int dy = 0;
        dx += getPaddingLeft() + getPaddingRight() + getCurrentWidth();
        dy += getPaddingTop() + getPaddingBottom() + getCurrentHeight();
        final int measureWidth = resolveSizeAndState(dx, widthMeasureSpec, 0);
        final int measureHeight = resolveSizeAndState(dy, heightMeasureSpec, 0);
        setMeasuredDimension(Math.max(getSuggestedMinimumWidth(), measureWidth), Math.max(getSuggestedMinimumHeight(), measureHeight));
    }*/

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

    public void setFinish(){
        refreshThread.interrupt();
    }

    public void stopRefresh(){
        refreshThread.interrupt();
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        publicPaint.setStyle(Paint.Style.FILL);
//        publicPaint.setColor(getCircularColor());
//        final RectF arcBounds = mTempBounds;
//        arcBounds.set(bounds);
//        arcBounds.inset(circular_edge, circular_edge);
//        canvas.drawArc(arcBounds, 0, 360, true, publicPaint);
        publicPaint.setStyle(Paint.Style.STROKE);
        publicPaint.setColor(getProgressColor());
        drawText(canvas);
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

    private void drawText(Canvas canvas) {
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
        //停止刷新线程
        refreshThread.interrupt();
    }
}
