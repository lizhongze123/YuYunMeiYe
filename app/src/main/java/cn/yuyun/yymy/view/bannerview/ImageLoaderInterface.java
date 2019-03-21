package cn.yuyun.yymy.view.bannerview;

import android.content.Context;
import android.view.View;

import java.io.Serializable;

/**
 * @author lzz
 * @desc lzz
 * @date 2018/4/16
 */
public interface ImageLoaderInterface<T extends View> extends Serializable {

    void displayImage(Context context, Object path, T imageView);

    T createImageView(Context context);
}
