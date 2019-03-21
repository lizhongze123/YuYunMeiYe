package cn.yuyun.yymy.view.lineview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import cn.lzz.utils.LogUtils;
import cn.lzz.utils.StringFormatUtils;
import cn.yuyun.yymy.R;

import static android.view.View.MeasureSpec.AT_MOST;

/**
 * @author
 * @desc
 * @date
 */

public class BusinessLineView extends View {

    /**
     * 控件默认宽高
     */
    private static final float DEF_WIDTH = 650;
    private static final float DEF_HIGHT = 400;

    private int lineColor;

    /**
     * 数据源
     */
    List<FundMode> mFundModeList;
    /**
     * 控件宽高
     */
    int mWidth;
    int mHeight;
    /**
     * 上下左右padding
     */
    float mPaddingTop;
    float mPaddingBottom;
    float mPaddingLeft;
    float mPaddingRight;

    /**
     * Y轴对应的最大值和最小值,注意，这里存的是对象
     */
    FundMode mMinFundMode;
    FundMode mMaxFundMode;

    /**
     * X、Y轴每一个data对应的大小
     */
    float mPerX;
    float mPerY;

    Paint mLoadingPaint;
    final float mLoadingTextSize = 20;
    final String mLoadingText = "暂无数据";
    boolean mDrawLoadingPaint = true;

    /** 外围X、Y轴线文字 */
    Paint mYPaint;
    /** 外围X、Y轴线文字 */
    Paint mXPaint;
    /** y轴指示文字字体的大小 */
    float mYTextSize = 8;
    /** x轴指示文字字体的大小 */
    float mXTextSize = 8;
    /** 左侧文字距离左边线线的距离 */
    float mLeftTxtPadding = 17;
    /** 底部文字距离底部线的距离*/
    float mBottomTxtPadding = 8;

    Paint linePaint;
    Paint floatTextPaint;
    Paint floatPathPaint;

    /** X轴的间隔 */
    float xInterval;

    /**
     * 折线
     */
    Paint mBrokenPaint;
    /**
     * 单位：px    在这里面没有经过dp2px的，都是默认是px
     */
    float mBrokenStrokeWidth = 4f;

    private Context mContext;

    /**长按处理*/
    private long mPressTime;
    /**默认多长时间算长按*/
    private final long DEF_LONGPRESS_LENGTH = 100;
    private float mPressX;
    private float mPressY;
    //长按的十字线
    private Paint mLongPressPaint;
    private boolean mDrawLongPressPaint = false;
    private final float mLongPressTextSize = 20;

    public BusinessLineView(Context context) {
        this(context, null);
    }

    public BusinessLineView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BusinessLineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initAttrs(attrs);
    }

    private void initAttrs(AttributeSet attrs) {

        // 读取属性值
        TypedArray ta = mContext.obtainStyledAttributes(attrs, R.styleable.BusinessLineView);
        lineColor = ta.getColor(R.styleable.BusinessLineView_lineColor, Color.BLACK);
        ta.recycle();
        initDimen();
        initLoadingPaint();
        initInnerXPaint();
        initXYPaint();
        initFloatTextPaint();
        initBrokenPaint();
        initLongPressPaint();
    }

    private void initFloatTextPaint() {
        floatTextPaint = new Paint();
        floatTextPaint.setColor(getColor(R.color.color_fundView_xLineColor));
        floatTextPaint.setTextSize(26);
        floatTextPaint.setColor(getColor(R.color.white));
        floatPathPaint = new Paint();
        floatPathPaint.setColor(getColor(R.color.color_fundView_xLineColor));
        floatPathPaint.setAntiAlias(true);
        floatPathPaint.setColor(getColor(R.color.color_fundView_floatRectColor));
    }

    private void initDimen() {
        mYTextSize = mContext.getResources().getDimensionPixelSize(R.dimen.x20);
        mXTextSize = mContext.getResources().getDimensionPixelSize(R.dimen.x28);
        mPaddingLeft = mContext.getResources().getDimensionPixelSize(R.dimen.x120);
        mPaddingRight = mContext.getResources().getDimensionPixelSize(R.dimen.x30);
        mPaddingBottom = mContext.getResources().getDimensionPixelSize(R.dimen.y92);
        mPaddingTop= mContext.getResources().getDimensionPixelSize(R.dimen.y50);
        mLeftTxtPadding = mContext.getResources().getDimensionPixelSize(R.dimen.x8);
        mBottomTxtPadding = mContext.getResources().getDimensionPixelSize(R.dimen.y8);
        mBrokenStrokeWidth = mContext.getResources().getDimensionPixelSize(R.dimen.x1);
    }

    private void initBrokenPaint() {
        mBrokenPaint = new Paint();
        mBrokenPaint.setColor(lineColor);
        mBrokenPaint.setStyle(Paint.Style.STROKE);
        mBrokenPaint.setAntiAlias(true);
        mBrokenPaint.setStrokeWidth(convertDp2Px(mBrokenStrokeWidth));
        PathEffect pathEffect = new CornerPathEffect(30);
        mBrokenPaint.setPathEffect(pathEffect);
    }

    private void initLoadingPaint() {
        mLoadingPaint = new Paint();
        mLoadingPaint.setColor(getColor(R.color.color_fundView_yTxtColor));
        mLoadingPaint.setTextSize(mLoadingTextSize);
        mLoadingPaint.setAntiAlias(true);
    }

    private void initInnerXPaint() {
        //禁用硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        linePaint = new Paint();
        linePaint.setColor(getColor(R.color.color_fundView_xLineColor));
        linePaint.setStrokeWidth(convertDp2Px(0.5f));
        linePaint.setStyle(Paint.Style.STROKE);
    }

    private void initXYPaint() {
        mYPaint = new Paint();
        mYPaint.setColor(getColor(R.color.color_fundView_yTxtColor));
        mYPaint.setTextSize(mYTextSize);
        mYPaint.setAntiAlias(true);
        mXPaint = new Paint();
        mXPaint.setColor(getColor(R.color.color_fundView_xTxtColor));
        mXPaint.setTextSize(mXTextSize);
        Typeface font = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD);
        mXPaint.setTypeface(font);
        mXPaint.setAntiAlias(true);
    }

    private void initLongPressPaint() {
        mLongPressPaint = new Paint();
        mLongPressPaint.setColor(getColor(R.color.color_fundView_longPressLineColor));
        mLongPressPaint.setStyle(Paint.Style.FILL);
        mLongPressPaint.setAntiAlias(true);
        mLongPressPaint.setTextSize(mLongPressTextSize);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        //setMeasuredDimension 设置的是px
        if (widthSpecMode == AT_MOST && heightSpecMode == AT_MOST) {
            setMeasuredDimension((int) DEF_WIDTH, (int) DEF_HIGHT);
        } else if (widthSpecMode == AT_MOST) {
            setMeasuredDimension((int) DEF_WIDTH, heightSpecSize);
        } else if (heightSpecMode == AT_MOST) {
            setMeasuredDimension(widthSpecSize, (int) DEF_HIGHT);
        } else {
            setMeasuredDimension(widthSpecSize, heightSpecSize);
        }
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
    }

    private float mPosX;
    private float mPosY;
    private float mCurPosX ;
    private float mCurPosY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mPosX = event.getX();
                mPosY = event.getY();
              /*  if(null != onPressListener){
                    onPressListener.onPress();
                }*/
                mPressTime = event.getDownTime();
                break;
            case MotionEvent.ACTION_MOVE:
                mCurPosX = event.getX();
                mCurPosY = event.getY();

                if (event.getEventTime() - mPressTime > DEF_LONGPRESS_LENGTH) {
                    mPressX = event.getX();
                    mPressY = event.getY();
                    //处理长按后的逻辑
                    showLongPressView();
                }


                if (mCurPosY - mPosY > 0
                        && (Math.abs(mCurPosY - mPosY) > 25)) {
                    //向下滑動

                } else if (mCurPosY - mPosY < 0
                        && (Math.abs(mCurPosY - mPosY) > 25)) {
                    //向上滑动

                }else{
//                    onPressListener.onPress();
                    //左右滑动

                }
                break;
            case MotionEvent.ACTION_UP:
                //处理松手后的逻辑
                if(null != onPressListener){
//                    onPressListener.onUp();
                }
//                hiddenLongPressView();
                break;
            default:
                break;
        }
        return true;
    }

    private void showLongPressView() {
        mDrawLongPressPaint = true;
        invalidate();
    }

    public interface OnPressListener{
       void onPress();
       void onUp();
    }

    public void setOnPressListener(OnPressListener onPressListener){
        this.onPressListener = onPressListener;
    }

    OnPressListener onPressListener;

    private void hiddenLongPressView() {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                mDrawLongPressPaint = false;
                invalidate();
            }
        }, 1000);
    }

    private void drawLongPress(Canvas canvas) {
        if (!mDrawLongPressPaint) {
            return;
        }
        //获取距离最近按下的位置的model
        float pressX = mPressX;
        //循环遍历，找到距离最短的x轴的mode
        FundMode finalFundMode = mFundModeList.get(0);
        float minXLen = Integer.MAX_VALUE;
        for (int i = 0; i < mFundModeList.size(); i++) {
            FundMode currFunMode = mFundModeList.get(i);
            float abs = Math.abs(pressX - currFunMode.floatX);
            if (abs < minXLen) {
                finalFundMode = currFunMode;
                minXLen = abs;
            }
        }
        //x
        canvas.drawLine(mPaddingLeft, finalFundMode.floatY, mWidth - mPaddingRight, finalFundMode.floatY, mLongPressPaint);
        //y
        canvas.drawLine(finalFundMode.floatX, mPaddingTop, finalFundMode.floatX, mHeight - mPaddingBottom, mLongPressPaint);
      /*  if(type == TYPE_WEEK){
            drawFloatTextBox(canvas, finalFundMode.floatX, finalFundMode.floatY, finalFundMode.datetime + "(" + finalFundMode.week +")", finalFundMode.dataY);
        }else{
            drawFloatTextBox(canvas, finalFundMode.floatX, finalFundMode.floatY, finalFundMode.datetime, finalFundMode.dataY);
        }*/
        drawFloatTextBox(canvas, finalFundMode.floatX, finalFundMode.floatY, finalFundMode.datetime + "(" + finalFundMode.week +")", finalFundMode.dataY);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //默认加载loading界面
        showLoadingPaint(canvas);
        if (mFundModeList == null || mFundModeList.size() == 0) {
            return;
        }
        drawInnerXPaint(canvas);
        drawXYPaint(canvas);
        drawBrokenPaint(canvas);
        drawLongPress(canvas);
    }

    private void drawFloatTextBox(Canvas canvas, float x, float y, String textTop, float textBottom) {

//        textTop = textTop.substring(5, textTop.length());

        float dp6 = convertDp2Px(6);
        float dp120 = convertDp2Px(120);
        float dp36 = convertDp2Px(36);
        float dp38 = convertDp2Px(38);

        RectF r1 = new RectF();
        float cX = (mWidth - mPaddingRight - mPaddingLeft) / 2 + mPaddingLeft;
//        Rect rect = getTextBounds("业绩:" + StringFormatUtils.formatDecimal(textBottom), floatTextPaint);
        Rect rect = getTextBounds("业绩:" + textTop, floatTextPaint);
        if(cX > x){
            r1.left = x + dp6;
            r1.right = x + dp120  + dp6;
            r1.top = mPaddingTop + dp36 ;
            r1.bottom = mPaddingTop;
            canvas.drawRoundRect(r1, 10, 10, floatPathPaint);
            canvas.drawText("业绩:" + StringFormatUtils.formatDecimal(textBottom), x + dp6 + dp6, mPaddingTop + dp38 - rect.height(), floatTextPaint);
            canvas.drawText("日期:" + textTop, x + dp6 + dp6, mPaddingTop + rect.height() + dp6, floatTextPaint);
        }else{
            r1.left = x - dp6 - dp120;
            r1.right = x - dp6;
            r1.top = mPaddingTop + dp36;
            r1.bottom = mPaddingTop;
            canvas.drawRoundRect(r1, 10, 10, floatPathPaint);
            canvas.drawText("业绩:"  + StringFormatUtils.formatDecimal(textBottom), x - dp120, mPaddingTop + dp38 - rect.height(), floatTextPaint);
            canvas.drawText("日期:" + textTop, x - dp120, mPaddingTop + rect.height() + dp6, floatTextPaint);
        }

    }

    /**
     * 获取丈量文本的矩形
     *
     * @param text
     * @param paint
     * @return
     */
    private Rect getTextBounds(String text, Paint paint) {
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        return rect;
    }

    private void drawBrokenPaint(Canvas canvas) {
        if (sum != 0) {
            //先画第一个点
            FundMode fundMode = mFundModeList.get(0);
            Path path = new Path();
            //这里需要说明一下，x轴的起始点，其实需要加上mPerX，但是加上之后不是从起始位置开始，不好看。
            // 同理，for循环内x轴其实需要(i+1)。现在这样处理，最后会留一点空隙，其实挺好看的。
            float floatY = mHeight - mPaddingBottom - mPerY * (fundMode.dataY - mMinFundMode.dataY);
            fundMode.floatX = mPaddingLeft;
            fundMode.floatY = floatY;
            path.moveTo(mPaddingLeft, floatY);
            xInterval = (mWidth - mPaddingLeft - mPaddingRight) / (mFundModeList.size() - 1);
            for (int i = 0; i < mFundModeList.size(); i++) {
                FundMode fm = mFundModeList.get(i);
                float floatX2 = mPaddingLeft + xInterval * i;
                float floatY2 = mHeight - mPaddingBottom - mPerY * (fm.dataY - mMinFundMode.dataY);
                fm.floatX = floatX2;
                fm.floatY = floatY2;
                path.lineTo(floatX2, floatY2);

            }
            canvas.drawPath(path, mBrokenPaint);
        }else{
            Path path = new Path();
            path.moveTo(mPaddingLeft + xInterval / 2, mHeight - mPaddingBottom);
            for (int i = 0; i < mFundModeList.size(); i++) {
                float floatX2 = mPaddingLeft + xInterval * i + xInterval / 2;
                float floatY2 = mHeight - mPaddingBottom;
                path.lineTo(floatX2, floatY2);
            }
            canvas.drawPath(path, mBrokenPaint);
        }

    }



    private void drawXYPaint(Canvas canvas) {
        //先处理y轴方向文字
        drawYPaint(canvas);
        //处理x轴方向文字
        drawXPaint(canvas);
    }

    private void drawYPaint(Canvas canvas) {
        //现将最小值、最大值画好
        //draw min
        float minWigth = mYPaint.measureText((int)mMinFundMode.dataY + "") + mLeftTxtPadding;
        canvas.drawText((int)mMinFundMode.dataY + "",
                mPaddingLeft - minWigth,
                mHeight - mPaddingBottom, mYPaint);
        //draw max
        float maxWigth = mYPaint.measureText((int)mMaxFundMode.dataY + "") + mLeftTxtPadding;
        canvas.drawText((int)mMaxFundMode.dataY + "",
                mPaddingLeft - maxWigth,
                mPaddingTop, mYPaint);
        //因为横线是均分的，所以只要取到最大值最小值的差值，均分即可。
        float perYValues = (mMaxFundMode.dataY - mMinFundMode.dataY) / 5;
        float perYWidth = (mHeight - mPaddingBottom - mPaddingTop) / 5;
        //从下到上依次画
        for (int i = 1; i <= 4; i++) {
            String text = ((int)(mMinFundMode.floatY + perYValues * i)) + "";
            float width = mYPaint.measureText(text) + mLeftTxtPadding;
            canvas.drawText(text,
                    mPaddingLeft - width,
                    mHeight - mPaddingBottom - perYWidth * i, mYPaint);
        }
    }

    private void drawXPaint(Canvas canvas) {
        //x轴文字的高度
        Paint.FontMetrics metrics = mXPaint.getFontMetrics();
        float hight = mHeight - mPaddingBottom + mBottomTxtPadding + (metrics.descent - metrics.ascent);
        String textStart;
        String textEnd;
        if(type == TYPE_WEEK){
            textStart = mFundModeList.get(0).datetime.substring(5, mFundModeList.get(0).datetime.length()) + "(" + mFundModeList.get(0).week +")";
            textEnd = mFundModeList.get(mFundModeList.size()-1).datetime.substring(5, mFundModeList.get(0).datetime.length())+ "(" + mFundModeList.get(mFundModeList.size()-1).week +")";
        }else{
            textStart = mFundModeList.get(0).datetime.substring(5, mFundModeList.get(0).datetime.length());
            textEnd = mFundModeList.get(mFundModeList.size()-1).datetime.substring(5, mFundModeList.get(0).datetime.length());
        }
        textStart = textStart.replace("-", "/");
        textEnd = textEnd.replace("-", "/");
        float width = mXPaint.measureText(textStart);

        canvas.drawText(textStart, mPaddingLeft, hight, mXPaint);
        canvas.drawText(textEnd, mWidth - mPaddingRight - width, hight, mXPaint);
    }

    private void showLoadingPaint(Canvas canvas) {
        if (!mDrawLoadingPaint) {
            return;
        }
        //这里特别注意，x轴的起始点要减去文字宽度的一半
        canvas.drawText(mLoadingText, mWidth / 2 - mLoadingPaint.measureText(mLoadingText) / 2, mHeight / 2, mLoadingPaint);
    }

    /**
     * 初始化绘制虚线的画笔
     */
    private void drawInnerXPaint(Canvas canvas) {
        //画5条横轴的虚线
        //首先确定最大值和最小值的位置
        //Y
        canvas.drawLine(0 + mPaddingLeft, mPaddingTop,
                0 + mPaddingLeft, mHeight - mPaddingBottom, linePaint);
        //X
        canvas.drawLine(0 + mPaddingLeft, mHeight - mPaddingBottom,
                mWidth - mPaddingRight, mHeight - mPaddingBottom, linePaint);
    }

    private Integer[] ary =  new Integer[]{0, 2000, 4000 , 6000, 8000, 10000};
    private float sum = 0;
    public static final int TYPE_MOONTH = 0;
    public static final int TYPE_WEEK = 1;
    private int type;


    /**
     * 程序入口，设置数据
     */
    public void setDataList(List<FundMode> fundModeList, int type) {
        this.type = type;
        if (fundModeList == null || fundModeList.size() == 0) {
            return;
        }
        this.mFundModeList = fundModeList;
        sum = 0;
        for (FundMode fundMode : mFundModeList) {
            sum = sum + fundMode.dataY;
        }
        //开始获取最大值最小值；单个数据尺寸等
        mMinFundMode = mFundModeList.get(0);
        mMaxFundMode = mFundModeList.get(mFundModeList.size() - 1);
        if(sum == 0){
            //没有数据，手动设置最大和最小
            mMaxFundMode.dataY = ary[ary.length - 1];
            mMinFundMode.dataY = ary[0];
        }else{
            for (FundMode fundMode : mFundModeList) {
                if (fundMode.dataY < mMinFundMode.dataY) {
                    mMinFundMode = fundMode;
                }
                if (fundMode.dataY > mMaxFundMode.dataY) {
                    mMaxFundMode = fundMode;
                }
            }
        }
        //获取单个数据X/y轴的大小
        mPerX = (mWidth - mPaddingLeft - mPaddingRight) / mFundModeList.size();
        mPerY = ((mHeight - mPaddingTop - mPaddingBottom) / (mMaxFundMode.dataY - mMinFundMode.dataY));

        //数据过来，隐藏加载更多
        hiddenLoadingPaint();

        //刷新界面
        invalidate();
    }

    /**
     * 只需要把画笔颜色置为透明即可
     */
    private void hiddenLoadingPaint() {
        mLoadingPaint.setColor(0x00000000);
        mDrawLoadingPaint = false;
    }

    public float getFontHeight(float fontSize, Paint paint) {
        paint.setTextSize(fontSize);
        Paint.FontMetrics fm = paint.getFontMetrics();
        return (float) (Math.ceil(fm.descent - fm.top) + 2);
    }

    public String getCustomDay(int customDay, String format) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -customDay);
        String date = new SimpleDateFormat(format).format(cal.getTime());
        System.out.println(date);
        return date;
    }

    private int getColor(@ColorRes int colorId) {
        return getResources().getColor(colorId);
    }

    private float convertDp2Px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (dpValue * scale + 0.5f);
    }
}
