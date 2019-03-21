package cn.yuyun.yymy.view.sidebar;

import android.content.Context;  
import android.graphics.Canvas;  
import android.graphics.Color;  
import android.graphics.Paint;  
import android.graphics.Typeface;  
import android.graphics.drawable.ColorDrawable;  
import android.util.AttributeSet;  
import android.view.MotionEvent;  
import android.view.View;  
import android.widget.TextView;

import cn.yuyun.yymy.R;

public class SideBar extends View {

    /** 触摸事件*/
    private OnTouchingLetterChangedListener onTouchingLetterChangedListener;
    /**26个字母*/
    public static String[] A_Z = { "A", "B", "C", "D", "E", "F", "G", "H", "I",  
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",  
            "W", "X", "Y", "Z", "#" };
    /**选中*/
    private int choose = -1;
    private Paint paint = new Paint();  
  
    private TextView mTextDialog;  
  
    /** 
     * 为SideBar设置显示字母的TextView
     * @param mTextDialog 
     */  
    public void setTextView(TextView mTextDialog) {  
        this.mTextDialog = mTextDialog;  
    }  
  
  
    public SideBar(Context context, AttributeSet attrs, int defStyle) {  
        super(context, attrs, defStyle);  
    }  
  
    public SideBar(Context context, AttributeSet attrs) {  
        super(context, attrs);  
    }  
  
    public SideBar(Context context) {  
        super(context);  
    }  
  
    /** 
     * 重写这个方法
     */
    @Override
    protected void onDraw(Canvas canvas) {  
        super.onDraw(canvas);
        // 获取焦点改变背景颜色.
        // 获取对应高度
        int height = getHeight();
        // 获取对应宽度
        int width = getWidth();
        // 获取每一个字母的高度  (这里-2仅仅是为了好看而已)
        int singleHeight = height / A_Z.length - 2;
  
        for (int i = 0; i < A_Z.length; i++) {
            //设置字体颜色
            paint.setColor(Color.parseColor("#999999"));
            //设置字体
//            paint.setTypeface(Typeface.DEFAULT_BOLD);
            //设置抗锯齿
            paint.setAntiAlias(true);
            //设置字母字体大小
            paint.setTextSize(30);
            // 选中的状态
            if (i == choose) {
                //选中的字母改变颜色
                paint.setColor(Color.parseColor("#3399ff"));
                //设置字体为粗体
//                paint.setFakeBoldText(true);
            }
            // x坐标等于中间-字符串宽度的一半.
            float xPos = width / 2 - paint.measureText(A_Z[i]) / 2;  
            float yPos = singleHeight * i + singleHeight;
            //绘制所有的字母
            canvas.drawText(A_Z[i], xPos, yPos, paint);
            //重置画笔
            paint.reset();
        }  
  
    }  
  
    @Override  
    public boolean dispatchTouchEvent(MotionEvent event) {  
        final int action = event.getAction();
        // 点击y坐标
        final float y = event.getY();
        final int oldChoose = choose;  
        final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
        // 点击y坐标所占总高度的比例*b数组的长度就等于点击b中的个数.
        final int c = (int) (y / getHeight() * A_Z.length);
  
        switch (action) {  
        case MotionEvent.ACTION_UP:  
            setBackgroundDrawable(new ColorDrawable(0x00000000));
            choose = -1;
            invalidate();  
            if (mTextDialog != null) {  
                mTextDialog.setVisibility(View.INVISIBLE);  
            }  
            break;  
  
        default:  
            setBackgroundResource(R.drawable.sidebar_background);
            if (oldChoose != c) {
                //判断选中字母是否发生改变
                if (c >= 0 && c < A_Z.length) {  
                    if (listener != null) {  
                        listener.onTouchingLetterChanged(A_Z[c]);  
                    }  
                    if (mTextDialog != null) {  
                        mTextDialog.setText(A_Z[c]);  
                        mTextDialog.setVisibility(View.VISIBLE);  
                    }  
                    
                    choose = c;  
                    invalidate();  
                }  
            }  
  
            break;  
        }  
        return true;  
    }  
  

    public void setOnTouchingLetterChangedListener(  
            OnTouchingLetterChangedListener onTouchingLetterChangedListener) {  
        this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;  
    }  

    public interface OnTouchingLetterChangedListener {  
        void onTouchingLetterChanged(String s);
    }

}
