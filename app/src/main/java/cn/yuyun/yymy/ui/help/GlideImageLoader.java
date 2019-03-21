package cn.yuyun.yymy.ui.help;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.youth.banner.loader.ImageLoader;

import cn.yuyun.yymy.R;

/**
 *
 */

public class GlideImageLoader extends ImageLoader{

	@Override
	public void displayImage(Context context, Object url, ImageView imageView) {
		/*RequestOptions myOptions = new RequestOptions().placeholder(context.getResources().getDrawable(R.color.loadding_img_bg)).error(context.getResources().getDrawable(R.drawable.photo_filter_image_empty));
		Glide.with(context)
				.load(url)
				.apply(myOptions)
				.into(imageView);*/
		Glide.with(context)
				.load(url)
				.placeholder(context.getResources().getDrawable(R.color.loadding_img_bg))
				.error(context.getResources().getDrawable(R.color.loadding_img_bg))
				.into(imageView);
	}
}
