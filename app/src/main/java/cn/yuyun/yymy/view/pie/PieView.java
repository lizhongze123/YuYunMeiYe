package cn.yuyun.yymy.view.pie;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import cn.lzz.utils.LogUtils;


/**
 * @author lzz
 * @desc lzz
 * @date 2018/3/2
 */

public class PieView extends View {

    /**饼状图初始绘制角度*/
    private float mStartAngle = 0;
    /** 数据*/
    private ArrayList<PieData> mData;
    /** 宽高*/
    private int mWidth, mHeight;
    /** 画笔*/
    private Paint mPaint = new Paint();
    private int color_gray = 0x99999999;

    public PieView(Context context) {
        this(context, null);
    }

    public PieView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mRadius = (Math.min(mWidth, mHeight) - getPaddingLeft() - getPaddingRight()) / 2;
    }

    private int mRadius;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = measureWidth(widthMeasureSpec);
        mHeight = measureHeight(heightMeasureSpec);
        setMeasuredDimension(mWidth,mHeight);
    }

    private int measureWidth(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
                result = specSize;
                break;
            case MeasureSpec.AT_MOST:
                result = Math.min(result, specSize);
                break;
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
                default:
        }
        return result;
    }

    private int measureHeight(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
                result = specSize;
                break;
            case MeasureSpec.AT_MOST:
                result = Math.min(result, specSize);
                break;
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
                default:
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (null == mData){
            return;
        }
        // 当前起始角度
        float currentStartAngle = mStartAngle;
        // 将画布坐标原点移动到中心位置
        canvas.translate(mWidth / 2, mHeight / 2);
        // 饼状图半径
//        float r = (float) (Math.min(mWidth, mHeight) / 2);
        // 饼状图绘制区域
        for (int i = 0; i < mData.size(); i++) {
            float r2 = mRadius - (i * 0.1f * mRadius);
            RectF rect = new RectF(-r2, -r2, r2, r2);
            PieData pie = mData.get(i);
            mPaint.setColor(pie.color);
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawArc(rect, currentStartAngle, (float) pie.angle, true, mPaint);
            currentStartAngle += pie.angle;

        }
        // 内圆半径
        float r2 = (float) mRadius * 0.7f;
        // 饼状图绘制区域
        RectF rect2 = new RectF(-r2, -r2, r2, r2);
        mPaint.setColor(Color.WHITE);
        canvas.drawArc(rect2, 0, 360, true, mPaint);
    }

    /** 设置起始角度*/
    public void setStartAngle(int mStartAngle) {
        this.mStartAngle = mStartAngle;
        invalidate(); // 刷新
    }

    /** 设置数据*/
    public void setData(ArrayList<PieData> mData) {
        this.mData = mData;
        initDate(mData);
        invalidate(); // 刷新
    }

    /** 初始化数据*/
    private void initDate(ArrayList<PieData> mData) {
        // 数据有问题 直接返回
        if (null == mData || mData.size() == 0) {
            return;
        }
        float sumValue = 0;
        for (int i = 0; i < mData.size(); i++) {
            PieData pie = mData.get(i);
            //计算数值和
            sumValue += pie.value;
        }

        float sumAngle = 0;
        for (int i = 0; i < mData.size(); i++) {
            PieData pie = mData.get(i);
            // 百分比
            double percentage = pie.value / sumValue;
            // 对应的角度
            double angle = percentage * 360;
            // 记录百分比
            pie.percentage = percentage;
            // 记录角度大小
            pie.angle = angle;
            sumAngle += angle;
        }
    }
}
