package cn.yuyun.yymy.view.bannerview;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import cn.yuyun.yymy.R;

/**
 * @author lzz
 * @desc lzz
 * @date 2018/4/16
 */
public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        //具体方法内容自己去选择，次方法是为了减少banner过多的依赖第三方包，所以将这个权限开放给使用者去选择
      /*  RequestOptions myOptions = new RequestOptions().placeholder(context.getResources().getDrawable(R.color.loadding_img_bg)).error(context.getResources().getDrawable(R.drawable.photo_filter_image_empty));
        Glide.with(context)
                .load(path)
                .apply(myOptions)
                .into(imageView);*/
        Glide.with(context)
                .load(path)
                .placeholder(context.getResources().getDrawable(R.color.loadding_img_bg)).error(context.getResources().getDrawable(R.drawable.photo_filter_image_empty))
                .into(imageView);
    }

    @Override
    public ImageView createImageView(Context context) {
        //圆角
        return new RoundAngleImageView(context);
    }
}
