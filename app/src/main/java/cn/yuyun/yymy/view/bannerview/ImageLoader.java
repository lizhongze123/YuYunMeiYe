package cn.yuyun.yymy.view.bannerview;

import android.content.Context;
import android.widget.ImageView;

/**
 * @author lzz
 * @desc lzz
 * @date 2018/4/16
 */
public abstract class ImageLoader implements ImageLoaderInterface<ImageView> {

    @Override
    public ImageView createImageView(Context context) {
        ImageView imageView = new ImageView(context);
        return imageView;
    }

}
