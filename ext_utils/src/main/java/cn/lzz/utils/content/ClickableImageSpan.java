package cn.lzz.utils.content;

import android.graphics.drawable.Drawable;
import android.text.style.ImageSpan;
import android.view.View;

/**
 * @author lzz
 * @desc lzz
 * @date 2018/5/9
 */
public abstract class ClickableImageSpan extends ImageSpan {
    public ClickableImageSpan(Drawable b, int verticalAlignment) {
        super(b, verticalAlignment);
    }

    public abstract void onClick(View view);
}
