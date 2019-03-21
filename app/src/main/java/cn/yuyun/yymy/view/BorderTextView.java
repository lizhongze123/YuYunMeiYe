package cn.yuyun.yymy.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.lzz.utils.DensityUtils;
import cn.lzz.utils.LogUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.utils.CommonUtils;


/**
 * 用于作为标签显示的TextView
 * 边框默认与文字颜色一致
 */
public class BorderTextView extends TextView {

    public static final float DEFAULT_STROKE_WIDTH = 0.5f;    // 默认边框宽度, 1dp
    public static final float DEFAULT_CORNER_RADIUS = 3.0f;   // 默认圆角半径, 2dp
    public static final float DEFAULT_LR_PADDING = 6f;      // 默认左右内边距
    public static final float DEFAULT_TB_PADDING = 2f;      // 默认上下内边距

    private int strokeWidth;    // 边框线宽
    private int strokeColor;    // 边框颜色
    private int cornerRadius;   // 圆角半径
    private boolean mFollowTextColor; // 边框颜色是否跟随文字颜色

    private Paint mPaint = new Paint();     // 画边框所使用画笔对象
    private RectF mRectF;                   // 画边框要使用的矩形


    private boolean mUseRandomBackgroundColor = true;

    public BorderTextView(Context context) {
        this(context, null);
    }

    public BorderTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BorderTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // 将DIP单位默认值转为PX
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        strokeWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                DEFAULT_STROKE_WIDTH, displayMetrics);
        cornerRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                DEFAULT_CORNER_RADIUS, displayMetrics);

        // 读取属性值
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BorderTextView);
        strokeWidth = ta.getDimensionPixelSize(R.styleable.BorderTextView_strokeWidth, strokeWidth);
        cornerRadius = ta.getDimensionPixelSize(R.styleable.BorderTextView_cornerRadius, cornerRadius);
        strokeColor = ta.getColor(R.styleable.BorderTextView_strokeColor, Color.TRANSPARENT);
        mFollowTextColor = ta.getBoolean(R.styleable.BorderTextView_followTextColor, true);
        ta.recycle();

        mRectF = new RectF();

       /* int paddingLeft = (int)context.getResources().getDimension(R.dimen.x36);
        int paddingRight = (int)context.getResources().getDimension(R.dimen.x36);
        int paddingTop = (int)context.getResources().getDimension(R.dimen.y8);
        int paddingBottom = (int)context.getResources().getDimension(R.dimen.y8);*/

        // 如果使用时没有设置内边距, 设置默认边距
        int paddingLeft = getPaddingLeft() == 0 ? (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, DEFAULT_LR_PADDING, displayMetrics) : getPaddingLeft();
        int paddingRight = getPaddingRight() == 0 ? (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, DEFAULT_LR_PADDING,
                displayMetrics) : getPaddingRight();
        int paddingTop = getPaddingTop() == 0 ? (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, DEFAULT_TB_PADDING, displayMetrics) : getPaddingTop();
        int paddingBottom = getPaddingBottom() == 0 ? (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, DEFAULT_TB_PADDING,
                displayMetrics) : getPaddingBottom();
        setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);


        initPaint(context);
    }

    private void initPaint(Context context) {
        if (mUseRandomBackgroundColor) {
            mPaint.setColor(Color.parseColor(CommonUtils.getRandomColor()));
        } else {
            mPaint.setColor(context.getResources().getColor(R.color.text_gray));
        }
        /** 空心效果*/
        mPaint.setStyle(Paint.Style.STROKE);
        /** 设置画笔为无锯齿*/
        mPaint.setAntiAlias(true);
        /** 线宽*/
        mPaint.setStrokeWidth(strokeWidth);
        setTextColor(mPaint.getColor());
    }

    public void setBgColorAndTextColor(int color){
        mPaint.setColor(color);
        setTextColor(color);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {

        // 设置边框线的颜色, 如果声明为边框跟随文字颜色且当前边框颜色与文字颜色不同时重新设置边框颜色
        /*if (mFollowTextColor && strokeColor != getCurrentTextColor()) {
            strokeColor = getCurrentTextColor();
        }
        mPaint.setColor(strokeColor);*/

        // 画空心圆角矩形
        mRectF.left = mRectF.top = 0.5f * strokeWidth;
        mRectF.right = getMeasuredWidth() - strokeWidth;
        mRectF.bottom = getMeasuredHeight() - strokeWidth;
        canvas.drawRoundRect(mRectF, cornerRadius, cornerRadius, mPaint);
        //绘制背景,在绘制文字之前绘制
        super.onDraw(canvas);
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        if(selected){
            mPaint.setStyle(Paint.Style.FILL);
            setTextColor(Color.WHITE);
        }else{
            mPaint.setStyle(Paint.Style.STROKE);
            setTextColor(mPaint.getColor());
        }
    }

}
