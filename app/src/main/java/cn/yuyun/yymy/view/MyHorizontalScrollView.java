package cn.yuyun.yymy.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

/**
 *当滚动联动时，这时这两个列表都存在滚动监听，所以就造成了监听的递归调用（死循环），就会造成内存溢出
 *所以，解决的方法就是，当HorizontalScrollView处理水平滚动事件时，取消列表的滚动监听
 * 而ScrollView本身不支持滚动监听，所以需要重新HorizontalScrollView，向外提供滚动监听功能
 * */
public class MyHorizontalScrollView extends HorizontalScrollView {

	private ScrollViewListener scrollViewListener = null;

	public MyHorizontalScrollView(Context context) {
		super(context);
	}

	public MyHorizontalScrollView(Context context, AttributeSet attrs,
                                  int defStyle) {
		super(context, attrs, defStyle);
	}

	public MyHorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setScrollViewListener(ScrollViewListener scrollViewListener) {
		this.scrollViewListener = scrollViewListener;
	}

	public void removeScrollViewListener() {
		this.scrollViewListener = null;
	}

	@Override
	protected void onScrollChanged(int x, int y, int oldx, int oldy) {
		super.onScrollChanged(x, y, oldx, oldy);
		if (scrollViewListener != null) {
			scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
		}
	}

	public interface ScrollViewListener {
		void onScrollChanged(MyHorizontalScrollView scrollView, int x, int y, int oldx, int oldy);
	}

}
