package cn.yuyun.yymy.view;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.ViewDebug.CapturedViewProperty;
import android.widget.TextView;

/**
 * @ClassName ScatterTextView
 * @Description 两端分散对齐(以ems作为字数或view的宽，通过{@link #setScatterView(boolean)}}改变，默认为false),暂时不可以添加drwableLeft等图标
 * @date 2016-8-17
 */
public class ScatterTextView extends TextView {

	private CharSequence srcCS;
	private boolean isScatterView = false;

	public ScatterTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public ScatterTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ScatterTextView(Context context) {
		super(context);
		init();
	}

	private void init() {
		setSingleLine();
		srcCS = getText();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		reSetText();
		super.onDraw(canvas);
	}

	private float reSetText() {
		TextPaint paint = getPaint();
		float maxWidth = getTextLen();
		float textLen = paint.measureText(srcCS, 0, srcCS.length());
		float spaceLen = paint.measureText(" ");
		float textSpaceLen = (maxWidth - textLen)/(srcCS.length()-1);
		float num = textSpaceLen / spaceLen;
		CharSequence resultCS = addSplit(srcCS,num);
		super.setText(resultCS, BufferType.NORMAL);
		float newLen = paint.measureText(resultCS, 0, resultCS.length());
		return newLen;
	}

	/**
	 * @Title setScatterView
	 * @Description 分散对齐的标准,true为View的宽，false为以EMS为字数的总宽度
	 * @author 陈海钦
	 * @date 2016-8-17 下午3:42:54
	 * @param isScatterView
	 */
	public void setScatterView(boolean isScatterView){
		this.isScatterView = isScatterView;
	}

	private float getTextLen() {
		float newLen = 0;
		if(isScatterView){
			newLen = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
		}else{
			TextPaint paint = getPaint();
			int maxEms = getCurrentMaxEms();
			if(maxEms > 0){
				newLen = paint.measureText(getMeasureText(maxEms));
			}else{
				newLen = paint.measureText(getText().toString());
			}
		}
		return newLen;
	}

	public int getCurrentMaxEms() {
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			return super.getMaxEms();
		}else {
			int len = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
			return (int) (len / getPaint().measureText("测"));
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int maxEms = getCurrentMaxEms();
		if(maxEms > 0){
			int emsLen = (int) getPaint().measureText(getMeasureText(maxEms+1));
			int measuredWidth = getMeasuredWidth();
			if(emsLen > measuredWidth){
				int measuredHeight = getMeasuredHeight();
				int newWid = (int) getPaint().measureText(getMeasureText(maxEms)) + getPaddingLeft()+getPaddingRight();
				setMeasuredDimension(newWid, measuredHeight);
			}
		}
	}

	private String getMeasureText(int maxEms) {
		StringBuffer sb = new StringBuffer();
		for(int i = 0 ; i < maxEms ; i++){
			sb.append("测");
		}
		return sb.toString();
	}

	private CharSequence addSplit(CharSequence cs, float num) {
		if(cs.length() <= 1){
			return cs;
		}
		CharSequence result = cs.subSequence(0, 1);
		StringBuffer space = new StringBuffer();
		for(int i = 0 ; i < (int)num ; i++){
			space.append(" ");
		}
		for(int i = 1; i < cs.length(); i ++){
			result = result.toString() + space.toString()+cs.subSequence(i, i+1);
		}
		return result;
	}

	@Override
	@CapturedViewProperty
	public CharSequence getText() {
		return srcCS;
	}

	@Override
	public void setText(CharSequence text, BufferType type) {
		super.setText(text, type);
		srcCS = text;
	}
}
