package cn.yuyun.yymy.view.lineview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;

import cn.lzz.utils.StringFormatUtils;
import cn.lzz.utils.ToastUtils;
import cn.yuyun.yymy.R;

import static android.view.View.MeasureSpec.AT_MOST;

/**
 * @author
 * @desc 柱状图
 * @date
 */

public class HistogramView extends View {

    /**
     * 控件默认宽高
     */
    private static final float DEF_WIDTH = 650;
    private static final float DEF_HIGHT = 400;

    /**
     * 数据源
     */
    List<HistogramMode> mFundModeList;
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

    Paint mLoadingPaint;
    final float mLoadingTextSize = 20;
    final String mLoadingText = "数据加载，请稍后";
    boolean mDrawLoadingPaint = true;

    /** 外围X、Y轴线文字 */
    Paint mXYPaint;
    /** x、y轴指示文字字体的大小 */
    float mXYTextSize = 8;
    /** 左侧文字距离左边线线的距离 */
    float mLeftTxtPadding = 17;
    /** 底部文字距离底部线的距离*/
    float mBottomTxtPadding = 8;
    /** 内部X轴虚线 */
    Paint mInnerXPaint;
    float mInnerXStrokeWidth = 0.5f;

    Paint linePaint;
    Paint floatTextPaint;

    /**坐标轴原点的x坐标*/
    float xOrigin;

    /**
     * 柱形
     */
    Paint mRectPaint;

    private Context mContext;

    float perXWidth;
    float perYHeight;

    public HistogramView(Context context) {
        this(context, null);
    }

    public HistogramView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HistogramView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initAttrs();
    }

    private void initAttrs() {
        initDimen();
        initLoadingPaint();
        initInnerXPaint();
        initXYPaint();
        initBrokenPaint();
    }

    private void initDimen() {
        mXYTextSize = mContext.getResources().getDimensionPixelSize(R.dimen.x22);
        mPaddingLeft = mContext.getResources().getDimensionPixelSize(R.dimen.x25);
        mPaddingRight = mContext.getResources().getDimensionPixelSize(R.dimen.x25);
        mPaddingBottom = mContext.getResources().getDimensionPixelSize(R.dimen.y70);
        mPaddingTop= mContext.getResources().getDimensionPixelSize(R.dimen.y70);
        mLeftTxtPadding = mContext.getResources().getDimensionPixelSize(R.dimen.x8);
        mBottomTxtPadding = mContext.getResources().getDimensionPixelSize(R.dimen.y8);
    }

    private void initBrokenPaint() {
        mRectPaint = new Paint();
        mRectPaint.setColor(getColor(R.color.histogram_one));
        mRectPaint.setStyle(Paint.Style.FILL);
        mRectPaint.setAntiAlias(true);
    }

    private void initLoadingPaint() {
        mLoadingPaint = new Paint();
        mLoadingPaint.setColor(getColor(R.color.color_fundView_yTxtColor));
        mLoadingPaint.setTextSize(mLoadingTextSize);
        mLoadingPaint.setAntiAlias(true);
    }

    /**
     * 初始化绘制虚线的画笔
     */
    private void initInnerXPaint() {
        mInnerXPaint = new Paint();
        mInnerXPaint.setColor(getColor(R.color.color_fundView_xLineColor));
        mInnerXPaint.setStrokeWidth(convertDp2Px(mInnerXStrokeWidth));
        mInnerXPaint.setStyle(Paint.Style.STROKE);
        //禁用硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        PathEffect effects = new DashPathEffect(new float[]{5, 5, 5, 5}, 1);
        mInnerXPaint.setPathEffect(effects);

        linePaint = new Paint();
        linePaint.setColor(getColor(R.color.color_fundView_xLineColor));
        linePaint.setStrokeWidth(convertDp2Px(mInnerXStrokeWidth));
        linePaint.setStyle(Paint.Style.STROKE);

        floatTextPaint = new Paint();
        floatTextPaint.setColor(getColor(R.color.color_fundView_xLineColor));
    }

    private void initXYPaint() {
        mXYPaint = new Paint();
        mXYPaint.setColor(getColor(R.color.color_fundView_yTxtColor));
        mXYPaint.setTextSize(mXYTextSize);
        mXYPaint.setAntiAlias(true);

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



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //默认加载loading界面
        showLoadingPaint(canvas);
        if (mFundModeList == null || mFundModeList.size() == 0) {
            return;
        }
        drawXYPaint(canvas);
        drawInnerXPaint(canvas);
        drawRect(canvas);
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

    /**
     * @param text  绘制的文字
     * @param paint 画笔
     * @return 文字的宽度
     */
    public int getTextWidth(String text, Paint paint) {
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        int width = bounds.left + bounds.width();
        return width;
    }

    private void drawRect(Canvas canvas){
        for (int i = 0; i < mFundModeList.size(); i++) {
            RectF rect = new RectF();
            rect.left  = xOrigin + mBottomTxtPadding + perXWidth * (2 * i + 1);
            rect.right = xOrigin + mBottomTxtPadding + perXWidth * (2 * i + 1) + perXWidth;
            rect.top = (float)((mHeight - mPaddingTop - mPaddingBottom) / 2 * (1 - mFundModeList.get(i).getValue() / maxValue)) + mPaddingTop;
            rect.bottom = mHeight - mPaddingBottom - perYHeight * 2;

            if(i == 0){
                mRectPaint.setColor(getColor(R.color.histogram_one));
            }else if(i == 1){
                mRectPaint.setColor(getColor(R.color.histogram_two));
            }else if(i == 2){
                mRectPaint.setColor(getColor(R.color.histogram_three));
            }
            canvas.drawRect(rect, mRectPaint);

            Paint.FontMetrics metrics = mXYPaint.getFontMetrics();
            float hight = (metrics.descent - metrics.ascent) / 2;

            String text = StringFormatUtils.formatTwoDecimal(mFundModeList.get(i).getValue());
            float width = mXYPaint.measureText(text);
            float startX = (perXWidth - width) / 2;
            if(mFundModeList.get(i).getValue() < 0){
                canvas.drawText(text, rect.left + startX, rect.bottom  - hight, mXYPaint);
                mFundModeList.get(i).setTop(rect.top + hight);
            }else{
                canvas.drawText(text, rect.left + startX, rect.top - hight , mXYPaint);
                mFundModeList.get(i).setTop(rect.top - hight);
            }

            mFundModeList.get(i).setRight(rect.right);
            mFundModeList.get(i).setBottom(rect.bottom);
            mFundModeList.get(i).setLeft(rect.left);

        }
    }

    private void drawXYPaint(Canvas canvas) {
        //先处理y轴方向文字
        drawYPaint(canvas);
        //处理x轴方向文字
        drawXPaint(canvas);
    }

    private void drawYPaint(Canvas canvas) {
        String[] arr = getText_Y(mFundModeList);
        perYHeight = (mHeight - mPaddingBottom - mPaddingTop) / 4;
        //从下到上依次画
        xOrigin = mXYPaint.measureText(arr[0]) + mPaddingLeft;
        mXYPaint.setTextAlign(Paint.Align.RIGHT);
        for (int i = 0; i < arr.length; i++) {
            canvas.drawText(arr[i],
                    xOrigin,
                    mHeight - mPaddingBottom - perYHeight * i, mXYPaint);
        }
    }

    private void drawXPaint(Canvas canvas) {
        //x轴文字的高度
        Paint.FontMetrics metrics = mXYPaint.getFontMetrics();
        float hight = mHeight - mPaddingBottom + mBottomTxtPadding + (metrics.descent - metrics.ascent);
        perXWidth = (mWidth - xOrigin - mPaddingRight) / 7;
        String text = "实收金额";
        float width = mXYPaint.measureText(text);
        float startX = (perXWidth - width) / 2;
        mXYPaint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("实收金额", xOrigin + mBottomTxtPadding + perXWidth + startX, hight, mXYPaint);
        canvas.drawText("消耗金额", xOrigin + mBottomTxtPadding + perXWidth * 3 + startX, hight, mXYPaint);
        canvas.drawText("业绩金额", xOrigin + mBottomTxtPadding +  perXWidth * 5 + startX, hight, mXYPaint);
    }

    private void showLoadingPaint(Canvas canvas) {
        if (!mDrawLoadingPaint) {
            return;
        }
        //这里特别注意，x轴的起始点要减去文字宽度的一半
        canvas.drawText(mLoadingText, mWidth / 2 - mLoadingPaint.measureText(mLoadingText) / 2, mHeight / 2, mLoadingPaint);
    }

    /**
     * 绘制虚线
     */
    private void drawInnerXPaint(Canvas canvas) {
        //画5条横轴的虚线
        //首先确定最大值和最小值的位置
        float perHeight = (mHeight - mPaddingBottom - mPaddingTop) / 4;
        //Y
        canvas.drawLine(xOrigin + mLeftTxtPadding, mPaddingTop,
                xOrigin + mLeftTxtPadding, mHeight - mPaddingBottom, linePaint);
        //X
       /*  canvas.drawLine(0 + mPaddingLeft, mHeight - mPaddingBottom,
                mWidth - mPaddingRight, mHeight - mPaddingBottom, linePaint);*/

        //1
        canvas.drawLine(xOrigin + mLeftTxtPadding, mPaddingTop + perHeight * 0,
                mWidth - mPaddingRight, mPaddingTop + perHeight * 0, mInnerXPaint);
        //2
        canvas.drawLine(xOrigin + mLeftTxtPadding, mPaddingTop + perHeight * 1,
                0 +mWidth - mPaddingRight, mPaddingTop + perHeight * 1, mInnerXPaint);

        canvas.drawLine(xOrigin + mLeftTxtPadding, mPaddingTop + perHeight * 2,
                0 +mWidth - mPaddingRight, mPaddingTop + perHeight * 2, linePaint);

        canvas.drawLine(xOrigin + mLeftTxtPadding, mPaddingTop + perHeight * 3,
                0 +mWidth - mPaddingRight, mPaddingTop + perHeight * 3, mInnerXPaint);

        canvas.drawLine(xOrigin + mLeftTxtPadding, mPaddingTop + perHeight * 4,
                0 +mWidth - mPaddingRight, mPaddingTop + perHeight * 4, mInnerXPaint);
    }

    /**
     * 程序入口，设置数据
     */
    public void setDataList(List<HistogramMode> fundModeList) {
        if (fundModeList == null || fundModeList.size() == 0) {
            return;
        }
        this.mFundModeList = fundModeList;
        //数据过来，隐藏加载更多
        hiddenLoadingPaint();
        //刷新界面
        invalidate();
    }

    double maxValue;

    /**
     * 获取一组数据的最大值
     */
    public int getMax(List<HistogramMode> list) {
        double max = Math.abs(list.get(0).getValue());
        for(int i = 1; i < list.size(); i++) {
            if(Math.abs(list.get(i).getValue()) > max){
                max = Math.abs(list.get(i).getValue());
            }
        }
        return (int) max;
    }

    public String[] getText_Y(List<HistogramMode> list){
        String[] text_Y;
        int textY = 0;

        String numMax = getMax(list) + "";
        char[] charArray = numMax.toCharArray();
        //最大数的位数
        int dataLength = charArray.length;
        String twoNumString = "";
        if(dataLength > 2){
            for (int i = 0; i < 2; i++) {
                twoNumString += charArray[i];
            }
            int twoNum =Integer.parseInt(twoNumString);
            twoNum = twoNum + 5;

            for (int i = 0; i < dataLength - 2; i++) {
                twoNum = twoNum * 10;
            }
            textY = (int) Math.ceil(twoNum / 2);
            //将数据前两位后加上“0” 用于返回前两位的整数

            text_Y = new String[]{"-" + twoNum, "-" + textY, "0",  "" + textY,  twoNum + ""};
            maxValue = twoNum;
        }else{
            text_Y = new String[]{"-100", "-50", "0", "50" , "100"};
        }
        return text_Y;
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

    private int getColor(@ColorRes int colorId) {
        return getResources().getColor(colorId);
    }

    private float convertDp2Px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (dpValue * scale + 0.5f);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                clickAction(event);
                break;
            case MotionEvent.ACTION_MOVE:
                break;

            case MotionEvent.ACTION_UP:
                break;
                default:
        }

        return true;
    }

    private void clickAction(MotionEvent event) {
        if(mFundModeList == null){
            return;
        }
        float eventX = event.getX();
        float eventY = event.getY();
        float dp10 = convertDp2Px(10);
        for (int i = 0; i < mFundModeList.size(); i++) {
            float left = mFundModeList.get(i).getLeft();
            float right = mFundModeList.get(i).getRight();
            float bottom = mFundModeList.get(i).getBottom();
            float top = mFundModeList.get(i).getTop();
            //每个节点周围8dp都是可点击区域
            if(eventX <= right+dp10 && eventX >= left-dp10 && eventY >= top-dp10 && eventY <= bottom+dp10){
                listener.onClick(mFundModeList.get(i), i);
                return;
            }

        }
    }

    private OnChartClickListener listener;

    public interface OnChartClickListener {
        void onClick(HistogramMode bean, int pos);
    }

    public void setOnChartClickListener(OnChartClickListener listener)  {
        this.listener = listener;
    }

}
