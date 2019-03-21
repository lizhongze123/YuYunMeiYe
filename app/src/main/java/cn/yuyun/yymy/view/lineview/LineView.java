package cn.yuyun.yymy.view.lineview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import cn.lzz.utils.DateTimeUtils;
import cn.yuyun.yymy.R;

/**
 * @author lzz
 * @desc 折线图
 * @date 2018/1/16
 */

public class LineView extends View {

    private static int DEFAULT_PURPLE = 0XFF4e57fc;
    private static int DEFAULT_RED = 0XFFff474c;
    private static int DEFAULT_GRAY = Color.GRAY;

    private Context mContext;
    /**
     * 折线最低点的高度
     */
    private int minPointHeight;
    /**
     * 折线线段长度
     */
    private int lineInterval;
    /**
     * 折线点的半径
     */
    private float pointRadius;
    /**
     * 控件的最小高度
     */
    private int minViewHeight;
    /**
     * 字体大小
     */
    private float textSize;
    /**
     * 折线坐标图四周留出来的偏移量
     */
    private int defaultPadding;

    private int backgroundColor;
    private int screenWidth;
    private int screenHeight;

    /**线画笔*/
    private Paint linePaint;
    /**文字画笔*/
    private Paint textPaint;
    private Paint descPaint;
    /**圆点画笔*/
    private Paint circlePaint;

    private int viewHeight;
    private int viewWidth;
    /**元数据*/
    private List<LineBean> data = new ArrayList<>();
    private List<LineBean> data2 = new ArrayList<>();
    /**折线拐点坐标集合*/
    private List<PointF> points = new ArrayList<>();
    private List<PointF> points2 = new ArrayList<>();

    /**元数据中的最高和最低数据*/
    private int maxData;
    private int minData;
    /**折线单位高度差*/
    private float pointGap;

    private String[] descArray = {"0", "5", "10", "15", "20", "25", "30"};

    public LineView(Context context) {
        this(context, null);
    }

    public LineView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initAttrs(attrs);
        setBackgroundColor(backgroundColor);
        initSize(context);
        initPaint(context);
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray ta = mContext.obtainStyledAttributes(attrs, R.styleable.LineView);
        minPointHeight = (int) ta.getDimension(R.styleable.LineView_min_point_height, dp2pxF(mContext, 60));
        lineInterval = (int) ta.getDimension(R.styleable.LineView_line_interval, dp2pxF(mContext, 60));
        backgroundColor = ta.getColor(R.styleable.LineView_background_color, Color.WHITE);
        ta.recycle();
    }

    /**
     * 初始化默认数据
     */
    private void initSize(Context c) {
        screenWidth = getResources().getDisplayMetrics().widthPixels;
        screenHeight = getResources().getDisplayMetrics().heightPixels;
        //默认3倍
        minViewHeight = 3 * minPointHeight;
        pointRadius = dp2pxF(c, 2.5f);
        textSize = sp2pxF(c, 10);
        //默认0.5倍
        defaultPadding = (int) (0.5 * minPointHeight);
        lineInterval = (viewWidth -  defaultPadding * 2) / 6;
    }

    private void initPaint(Context c) {
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setStrokeWidth(dp2px(c, 1));

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(textSize);
        textPaint.setColor(Color.BLACK);
        //这样即能实现文字的水平居中，到时候绘制文字时传入文字中心x的坐标即可。（而不需要向左偏移半个文本长度）
        textPaint.setTextAlign(Paint.Align.CENTER);

        descPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        descPaint.setTextSize(textSize);
        descPaint.setColor(Color.BLACK);
        //这样即能实现文字的水平居中，到时候绘制文字时传入文字中心x的坐标即可。（而不需要向左偏移半个文本长度）
        descPaint.setTextAlign(Paint.Align.CENTER);

        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setStrokeWidth(dp2pxF(c, 1));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getSize(heightMeasureSpec);

        /**
         * 本控件的高度是用户可控的，不过限制了最小高度
         * 宽度根据数量来决定，并且最小宽度为一个屏幕宽
         */

        if(heightMode == MeasureSpec.EXACTLY){
            viewHeight = Math.max(heightSize, minViewHeight);
        }else{
            viewHeight = minViewHeight;
        }

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        viewWidth = widthSize;

        setMeasuredDimension(widthSize, viewHeight);
        calculatePontGap();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initSize(getContext());
        calculatePontGap();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        if (data.isEmpty()) {
//            return;
//        }
        drawAxis(canvas);
        drawLinesAndPoints(canvas);
        drawDesc(canvas);
    }

    private void drawAxis(Canvas canvas) {
        canvas.save();
        linePaint.setColor(DEFAULT_GRAY);
        linePaint.setStrokeWidth(dp2px(getContext(), 1));
        canvas.drawLine(defaultPadding,
                viewHeight - defaultPadding,
                viewWidth - defaultPadding,
                viewHeight - defaultPadding,
                linePaint);
        float centerY = viewHeight - defaultPadding + dp2pxF(getContext(), 15);
        float centerX;
        for (int i = 0; i < descArray.length; i++) {
            String text = descArray[i];
            centerX = defaultPadding + i * lineInterval;
            Paint.FontMetrics m = descPaint.getFontMetrics();
            if(text != null && !"0".equals(text)){
                canvas.drawText(text, 0, text.length(), centerX, centerY - (m.ascent + m.descent) / 2, descPaint);
            }
        }
        canvas.restore();
    }

    /**
     * 画描述值
     *
     * @param canvas
     */
    private void drawDesc(Canvas canvas) {
        canvas.save();
        //字体放大一丢丢
        textPaint.setTextSize(1.1f * textSize);
        textPaint.setColor(DEFAULT_PURPLE);
        float centerX;
        float centerY;
        String text;
        for (int i = 0; i < points.size(); i++) {
            text = data.get(i).temperature + "";
            centerX = points.get(i).x;
            centerY = points.get(i).y - dp2pxF(getContext(), 15);
            Paint.FontMetrics metrics = textPaint.getFontMetrics();
            canvas.drawText(text, centerX, centerY - (metrics.ascent + metrics.descent/2), textPaint);
            textPaint.setTextSize(textSize);
        }

        textPaint.setColor(DEFAULT_RED);
        for (int i = 0; i < points2.size(); i++) {
            text = data2.get(i).temperature + "";
            centerX = points2.get(i).x;
            centerY = points2.get(i).y - dp2pxF(getContext(), 15);
            Paint.FontMetrics metrics = textPaint.getFontMetrics();
            canvas.drawText(text, centerX, centerY - (metrics.ascent + metrics.descent/2), textPaint);
            textPaint.setTextSize(textSize);
        }
        canvas.restore();
    }


    /**
     * 画折线和圆点
     * @param canvas
     */
    private void drawLinesAndPoints(Canvas canvas) {
        canvas.save();
        linePaint.setColor(DEFAULT_PURPLE);
        linePaint.setStrokeWidth(dp2pxF(getContext(), 1));
        linePaint.setStyle(Paint.Style.STROKE);
        //用于绘制折线
        Path linePath = new Path();
        points.clear();
        int baseHeight = defaultPadding ;
//        int baseHeight = defaultPadding + minPointHeight;
        float centerX;
        float centerY;
        for (int i = 0; i < data.size(); i++) {
            int tem = data.get(i).temperature;
            tem = tem - minData;
            centerY = (int) (viewHeight - (baseHeight + tem * pointGap));
            centerX = defaultPadding + i * lineInterval;
            points.add(new PointF(centerX, centerY));
            if (i == 0) {
                linePath.moveTo(centerX, centerY);
            } else {
                linePath.lineTo(centerX, centerY);
            }
        }
        //画出折线
        canvas.drawPath(linePath, linePaint);

        //画圆点
        float x, y;
        for (int i = 0; i < points.size(); i++) {
            x = points.get(i).x;
            y = points.get(i).y;

            //先画一个颜色为背景颜色的实心园覆盖掉折线拐角
            circlePaint.setStyle(Paint.Style.FILL_AND_STROKE);
            circlePaint.setColor(backgroundColor);
            canvas.drawCircle(x, y,
                    pointRadius + dp2pxF(getContext(), 1),
                    circlePaint);
            //再画出正常的空心园
            circlePaint.setStyle(Paint.Style.STROKE);
            circlePaint.setColor(DEFAULT_PURPLE);
            canvas.drawCircle(x, y,
                    pointRadius,
                    circlePaint);
        }

        canvas.save();
        linePaint.setColor(DEFAULT_RED);
        linePaint.setStrokeWidth(dp2pxF(getContext(), 1));
        linePaint.setStyle(Paint.Style.STROKE);
        //用于绘制折线
        Path linePath2 = new Path();
        points2.clear();
        for (int i = 0; i < data2.size(); i++) {
            int tem = data2.get(i).temperature;
            tem = tem - minData;
            centerY = (int) (viewHeight - (baseHeight + tem * pointGap));
            centerX = defaultPadding + i * lineInterval;
            points2.add(new PointF(centerX, centerY));
            if (i == 0) {
                linePath2.moveTo(centerX, centerY);
            } else {
                linePath2.lineTo(centerX, centerY);
            }
        }
        //画出折线
        canvas.drawPath(linePath2, linePaint);

        //画圆点
        for (int i = 0; i < points2.size(); i++) {
            x = points2.get(i).x;
            y = points2.get(i).y;

            //先画一个颜色为背景颜色的实心园覆盖掉折线拐角
            circlePaint.setStyle(Paint.Style.FILL_AND_STROKE);
            circlePaint.setColor(backgroundColor);
            canvas.drawCircle(x, y,
                    pointRadius + dp2pxF(getContext(), 1),
                    circlePaint);
            //再画出正常的空心园
            circlePaint.setStyle(Paint.Style.STROKE);
            circlePaint.setColor(DEFAULT_RED);
            canvas.drawCircle(x, y,
                    pointRadius,
                    circlePaint);
        }
    }

    /**
     * 计算折线单位高度差
     */
    private void calculatePontGap() {
        int lastMaxTem = -100000;
        int lastMinTem = 100000;
        for (LineBean bean : data) {
            if (bean.temperature > lastMaxTem) {
                maxData = bean.temperature;
                lastMaxTem = bean.temperature;
            }
            if (bean.temperature < lastMinTem) {
                minData = bean.temperature;
                lastMinTem = bean.temperature;
            }
        }
        for (LineBean bean : data2) {
            if (bean.temperature > lastMaxTem) {
                maxData = bean.temperature;
                lastMaxTem = bean.temperature;
            }
            if (bean.temperature < lastMinTem) {
                minData = bean.temperature;
                lastMinTem = bean.temperature;
            }
        }

        minData = 0;
        lastMinTem = 0;
        float gap = (maxData - minData) * 1.0f;
        //保证分母不为0
        gap = (gap == 0.0f ? 1.0f : gap);
//        pointGap = (viewHeight - minPointHeight - 2 * defaultPadding) / gap;
        pointGap = (viewHeight  - 2 * defaultPadding) / gap;
    }

    /**
     * 唯一公开方法，用于设置元数据
     *
     * @param data
     */
    public void setData(List<LineBean> data, List<LineBean> data2) {
        if (data == null || data.isEmpty()) {
            return;
        }
        if (data2 == null || data2.isEmpty()) {
            return;
        }
        this.data = data;
        this.data2 = data2;
        points.clear();
        points2.clear();

        requestLayout();
        invalidate();
    }

    /**工具类*/
    public static int dp2px(Context c, float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, c.getResources().getDisplayMetrics());
    }

    public static int sp2px(Context c, float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, c.getResources().getDisplayMetrics());
    }

    public static float dp2pxF(Context c, float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, c.getResources().getDisplayMetrics());
    }

    public static float sp2pxF(Context c, float sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, c.getResources().getDisplayMetrics());
    }

}
