package cn.lzz.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;


public class ResoureUtils {

    public static Resources getResoure(Context context) {
        return context.getResources();
    }

    public static Drawable getDrawable(Context context, int resId){
        return getResoure(context).getDrawable(resId);
    }

    public static int getColor(Context context, int resid) {
        return getResoure(context).getColor(resid);
    }

    public static int getDimension(Context context, int resid) {
        return (int) getResoure(context).getDimension(resid);
    }

}