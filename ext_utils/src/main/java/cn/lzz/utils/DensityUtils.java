package cn.lzz.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.util.List;

/**
 *@desc  尺寸工具类
 *@author
 *@date
 */
public class DensityUtils {

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 设置某个View的margin
	 *
	 * @param view   需要设置的view
	 * @param isDp   需要设置的数值是否为DP
	 * @param left   左边距
	 * @param right  右边距
	 * @param top    上边距
	 * @param bottom 下边距
	 * @return
	 */
	public static ViewGroup.LayoutParams setViewMargin(Context context, View view, boolean isDp, int left, int right, int top, int bottom) {
		if (view == null) {
			return null;
		}

		int leftPx = left;
		int rightPx = right;
		int topPx = top;
		int bottomPx = bottom;
		ViewGroup.LayoutParams params = view.getLayoutParams();
		ViewGroup.MarginLayoutParams marginParams = null;
		//获取view的margin设置参数
		if (params instanceof ViewGroup.MarginLayoutParams) {
			marginParams = (ViewGroup.MarginLayoutParams) params;
		} else {
			//不存在时创建一个新的参数
			marginParams = new ViewGroup.MarginLayoutParams(params);
		}

		//根据DP与PX转换计算值
		if (isDp) {
			leftPx = dip2px(context,left);
			rightPx = dip2px(context,right);
			topPx = dip2px(context,top);
			bottomPx = dip2px(context,bottom);
		}
		//设置margin
		marginParams.setMargins(leftPx, topPx, rightPx, bottomPx);
		view.setLayoutParams(marginParams);
		view.requestLayout();
		return marginParams;
	}

	public static <T> boolean isEmpty(List<T> list) {
		if (list == null || list.size() == 0) {
			return true;
		}
		return false;
	}

	/**
	 * 获得屏幕高度,单位是px
	 *
	 * @param context
	 * @return
	 */
	public static int getScreenWidth(Context context) {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		return outMetrics.widthPixels;
	}

	/**
	 * 获得屏幕宽度，单位是px
	 *
	 * @param context
	 * @return
	 */
	public static int getScreenHeight(Context context) {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		return outMetrics.heightPixels;
	}

	/**
	 * 获得状态栏的高度
	 *
	 * @param context
	 * @return
	 */
	public static int getStatusHeight(Context context) {

		int statusHeight = -1;
		try {
			Class<?> clazz = Class.forName("com.android.internal.R$dimen");
			Object object = clazz.newInstance();
			int height = Integer.parseInt(clazz.getField("status_bar_height")
					.get(object).toString());
			statusHeight = context.getResources().getDimensionPixelSize(height);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return statusHeight;
	}
}
