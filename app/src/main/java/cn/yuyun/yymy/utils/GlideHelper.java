package cn.yuyun.yymy.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import com.bumptech.glide.load.engine.DiskCacheStrategy;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.File;

import cn.yuyun.yymy.R;
import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;



public class GlideHelper {

	public static void displayGif(Context context, int resId, ImageView imageView) {
		/*RequestOptions myOptions = new RequestOptions()
				.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
		Glide.with(context).load(resId).apply(myOptions).into(imageView);*/
		Glide.with(context).load(resId).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);
	}

	public static void displayImage(Context context, int url, ImageView imageView) {
		/*RequestOptions myOptions = new RequestOptions().error(context.getResources().getDrawable(R.drawable.avatar_default_female));
		Glide.with(context).load(url).apply(myOptions).into(imageView);*/
		Glide.with(context).load(url).error(context.getResources().getDrawable(R.drawable.avatar_default_female)).into(imageView);
	}

	public static void displayImage(Context context, String url, ImageView imageView) {
		/*RequestOptions myOptions = new RequestOptions().error(context.getResources().getDrawable(R.drawable.avatar_default_female));
		Glide.with(context).load(url).apply(myOptions).into(imageView);*/
		Glide.with(context).load(url).error(context.getResources().getDrawable(R.drawable.avatar_default_female)).into(imageView);
	}

	public static void displayImage(Context context, String url, ImageView imageView, int defaultId) {
	/*	RequestOptions myOptions = new RequestOptions().error(context.getResources().getDrawable(defaultId));
		Glide.with(context).load(url).apply(myOptions).into(imageView);*/
		Glide.with(context).load(url).error(defaultId).dontAnimate().into(imageView);
	}

	public static void displayImage(Context context, String url, int overrideSize, ImageView imageView) {
	/*	RequestOptions myOptions = new RequestOptions().override(overrideSize, overrideSize).error(context.getResources().getDrawable(R.drawable.avatar_default_female));
		Glide.with(context).load(url).apply(myOptions).into(imageView);*/
		Glide.with(context).load(url).override(overrideSize, overrideSize).error(context.getResources().getDrawable(R.drawable.avatar_default_female)).into(imageView);
	}

	public static void displayImage(Context context, int url, int overrideSize, ImageView imageView) {
	/*	RequestOptions myOptions = new RequestOptions()
				.override(overrideSize, overrideSize).error(R.drawable.avatar_default_female);
		Glide.with(context)
				.load(url)
				.apply(myOptions)
				.into(imageView);*/
		Glide.with(context)
				.load(url)
				.override(overrideSize, overrideSize).error(R.drawable.avatar_default_female)
				.into(imageView);
	}

	public static void displayImage(Context context, File file, ImageView imageView) {
//		RequestOptions myOptions = new RequestOptions().error(context.getResources().getDrawable(R.drawable.avatar_default_male));
		Glide.with(context)
				.load(file)
				.error(R.drawable.avatar_default_male)
				.into(imageView);
	}

	public static void displayImageForDetail(Context context, String url, ImageView imageView) {
	/*	RequestOptions myOptions = new RequestOptions()
				//保持原图片的比例进行缩放，直到可以在ImageView中尺寸区域内完全显示图片。图片能够完全显示，比例保持不变，但是可能图片无法完全覆盖ImageView的区域。
				.fitCenter();
		Glide.with(context).load(url).apply(myOptions).into(imageView);*/
		Glide.with(context).load(url).into(imageView);
	}

	public static void displayOverrideImage(Context context, int ResId, int width, int height, ImageView imageView) {
	/*	RequestOptions myOptions = new RequestOptions().override(width, height);
		Glide.with(context).load(ResId).apply(myOptions).into(imageView);*/
		Glide.with(context).load(ResId).override(width, height).into(imageView);
	}

	public static void displayOverrideImage(Context context, String url, int width, int height, ImageView imageView, int defId) {
		/*RequestOptions myOptions = new RequestOptions().override(width, height).error(context.getResources().getDrawable(defId));
		Glide.with(context).load(url).apply(myOptions).into(imageView);*/
		Glide.with(context).load(url).override(width, height).error(defId).into(imageView);
	}

	public static void displayOverrideImage(Context context, String url, int width, int height, ImageView imageView) {
		/*RequestOptions myOptions = new RequestOptions().override(width, height);
		Glide.with(context).load(url).apply(myOptions).into(imageView);*/
		Glide.with(context).load(url).override(width, height).into(imageView);
	}

	public static void displayOverrideImage(Context context, String url, int defaultRes, int width, int height, ImageView imageView) {
//		RequestOptions myOptions = new RequestOptions().override(width, height).error(defaultRes);
		Glide.with(context).load(url).override(width, height).error(defaultRes).into(imageView);
	}

	public static void displayImageForSize(Context context, String url, int width, int height, ImageView imageView) {
		/*RequestOptions myOptions = new RequestOptions()
				.override(width, height);
		Glide.with(context).load(url).apply(myOptions).into(imageView);*/
		Glide.with(context).load(url).override(width, height).into(imageView);
	}

	public static void displayImageForSize(Context context, String url, int width, int height, ImageView imageView, int defId) {
		/*RequestOptions myOptions = new RequestOptions().override(width, height).error(context.getResources().getDrawable(defId));
		Glide.with(context).load(url).apply(myOptions).into(imageView);*/
		Glide.with(context).load(url).override(width, height).error(context.getResources().getDrawable(defId)).into(imageView);
	}

	public static void displayImageForSize(Context context, File file, int width, int height, ImageView imageView) {
		/*RequestOptions myOptions = new RequestOptions().override(width, height);
		Glide.with(context).load(file).apply(myOptions).into(imageView);*/
		Glide.with(context).load(file).override(width, height).into(imageView);
	}

	public static void displayCircleImage(Context context, String url, ImageView imageView) {
		/*Glide.with(context)
				.load(url)
				.apply(bitmapTransform(new CropCircleTransformation()))
				.into(imageView).onLoadFailed(context.getResources().getDrawable(R.drawable.avatar_default_male));*/
	}

	public static void displayCircleAvatar(Context context, String url, int overrideSize, ImageView imageView) {
		/*RequestOptions myOptions = new RequestOptions()
				.override(overrideSize, overrideSize).error(R.drawable.avatar_default_female);
		Glide.with(context)
				.load(url)
				.apply(bitmapTransform(new CropCircleTransformation()))
				.apply(myOptions)
				.into(imageView).onLoadFailed(context.getResources().getDrawable(R.drawable.avatar_default_female));*/
	}

	public static void displayCircleAvatar(Context context, int url, int overrideSize, ImageView imageView) {
		/*RequestOptions myOptions = new RequestOptions()
				.override(overrideSize, overrideSize).error(R.drawable.avatar_default_male);
		Glide.with(context)
				.load(url)
				.apply(bitmapTransform(new CropCircleTransformation()))
				.apply(myOptions)
				.into(imageView).onLoadFailed(context.getResources().getDrawable(R.drawable.avatar_default_female));*/
	}

	public static void displayCircleAvatar(Context context, String url, ImageView imageView) {
		/*RequestOptions myOptions = new RequestOptions()
				.override((int)context.getResources().getDimension(R.dimen.user_avatar), (int)context.getResources().getDimension(R.dimen.user_avatar)).error(context.getResources().getDrawable(R.drawable.avatar_default_female));
		Glide.with(context)
				.load(url)
				.apply(bitmapTransform(new CropCircleTransformation()))
				.apply(myOptions)
				.into(imageView);*/
	}

	public static void displayCircleAvatar(Context context, int url, ImageView imageView) {
		/*RequestOptions myOptions = new RequestOptions()
				.override((int)context.getResources().getDimension(R.dimen.user_avatar), (int)context.getResources().getDimension(R.dimen.user_avatar)).error(context.getResources().getDrawable(R.drawable.avatar_default_female));
		Glide.with(context)
				.load(url)
				.apply(bitmapTransform(new CropCircleTransformation()))
				.apply(myOptions)
				.into(imageView);*/
	}

	public static void displayAvatar(Context context, String url, ImageView imageView) {
		/*RequestOptions myOptions = new RequestOptions()
				.override((int)context.getResources().getDimension(R.dimen.user_avatar), (int)context.getResources().getDimension(R.dimen.user_avatar)).error(context.getResources().getDrawable(R.drawable.avatar_default_female));
		Glide.with(context)
				.load(url)
				.apply(myOptions)
				.into(imageView);*/
	}

	public static void displayCircleAvatar(Context context, File file, ImageView imageView) {
		/*RequestOptions myOptions = new RequestOptions()
				.override((int)context.getResources().getDimension(R.dimen.user_avatar), (int)context.getResources().getDimension(R.dimen.user_avatar));
		Glide.with(context)
				.load(file)
				.apply(bitmapTransform(new CropCircleTransformation()))
				.apply(myOptions)
				.into(imageView).onLoadFailed(context.getResources().getDrawable(R.drawable.avatar_default_female));*/
	}

	public static void displayCircleImageForSize(Context context, String url, int width, int height, ImageView imageView) {
	/*	RequestOptions myOptions = new RequestOptions()
				.override(width,height).error(context.getResources().getDrawable(R.drawable.avatar_default_female));
		Glide.with(context)
				.load(url)
				.apply(myOptions)
				.apply(bitmapTransform(new CropCircleTransformation()))
				.into(imageView);*/
	}

/*	public static void displayBlur(Context context, int url, ImageView imageView) {
		Glide.with(context).load(url)
				.apply(bitmapTransform(new BlurTransformation(15, 25)))
				.into(imageView);
	}*/

	/**
	 * 自适应宽度加载图片。保持图片的长宽比例不变，通过修改imageView的高度来完全显示图片。
	 */
/*	public static void loadIntoUseFitWidth(Context context, final String imageUrl, final ImageView imageView) {
		Glide.with(context)
				.load(imageUrl)
				.listener(new RequestListener<Drawable>() {
					@Override
					public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
						return false;
					}

					@Override
					public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
						if (imageView == null) {
							return false;
						}
						if (imageView.getScaleType() != ImageView.ScaleType.FIT_XY) {
							imageView.setScaleType(ImageView.ScaleType.FIT_XY);
						}
						ViewGroup.LayoutParams params = imageView.getLayoutParams();
						int vw = imageView.getWidth() - imageView.getPaddingLeft() - imageView.getPaddingRight();
						float scale = (float) vw / (float) resource.getIntrinsicWidth();
						int vh = Math.round(resource.getIntrinsicHeight() * scale);
						params.height = vh + imageView.getPaddingTop() + imageView.getPaddingBottom();
						imageView.setLayoutParams(params);
						return false;
					}
				})
				.into(imageView);
	}*/
}
