package cn.yuyun.yymy.view;

import android.content.Context;
import android.graphics.Rect;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.widget.TextView;

public class MarqueeTextView extends TextView {

    public MarqueeTextView(Context context) {
	super(context);
	initMarquee();
    }

    public MarqueeTextView(Context context, AttributeSet attrs) {
	super(context, attrs);
	initMarquee();
    }

    public MarqueeTextView(Context context, AttributeSet attrs, int defStyle) {
	super(context, attrs, defStyle);
	initMarquee();
    }

    private void initMarquee() {
	setSingleLine(true);
	setEllipsize(TruncateAt.MARQUEE);
	setMarqueeRepeatLimit(-1);
    }

    @Override
    public boolean isFocused() {
	return true;
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
	if (focused) {
	    super.onFocusChanged(focused, direction, previouslyFocusedRect);
	}
    }

    @Override
    public void onWindowFocusChanged(boolean focused) {
	if (focused) {
	    super.onWindowFocusChanged(focused);
	}
    }

}
