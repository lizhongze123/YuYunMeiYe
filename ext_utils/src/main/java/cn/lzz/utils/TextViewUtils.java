package cn.lzz.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.LruCache;
import android.widget.TextView;

public class TextViewUtils {

    public static void setTextOrEmpty(@NonNull TextView tv, CharSequence cs){
        if(TextUtils.isEmpty(cs)){
            tv.setText("");
        }else{
            tv.setText(cs);
        }
    }

    public static void setText(@NonNull TextView tv, CharSequence desc, CharSequence cs){
        if(TextUtils.isEmpty(cs)){
            tv.setText(desc);
        }else{
            tv.setText(desc.toString() + cs);
        }
    }

    public static void setTextOrEmpty(@NonNull TextView tv, CharSequence cs, CharSequence defaultS){
        if(TextUtils.isEmpty(cs)){
            tv.setText(defaultS);
        }else{
            tv.setText(cs);
        }
    }

    public static void setTextSynOrEmpty(@NonNull TextView tv,Object obj,LoadTextAsyTask task){
        StringLruCache lruCache = StringLruCache.getInstance(tv.getContext());
        String text = lruCache.get(obj.toString());
        if(text != null){
            tv.setText(text);
        }else{
            task.execute(obj.toString());
        }
    }

    public static class StringLruCache extends LruCache<String,String>{

        private static StringLruCache instance;

        public StringLruCache(int maxSize) {
            super(maxSize);
        }

        public static StringLruCache getInstance(Context context) {
            if(instance == null){
                synchronized (StringLruCache.class){
                    if(instance == null){
                        ActivityManager actMan = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                        int maxSize = actMan.getMemoryClass() / 16 * 1024 *1024;
                        instance = new StringLruCache(maxSize);
                    }
                }
            }
            return instance;
        }

    }

    public static abstract class LoadTextAsyTask extends AsyncTask<String,Void,String> {

        private final String tag;
        private TextView tv;

        public LoadTextAsyTask(TextView tv,String tag) {
            this.tv = tv;
            this.tag = tag;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s != null){
                StringLruCache.getInstance(tv.getContext()).put(tag,s);
                tv.setText(s);
            }
        }
    }

    public static CharSequence getIconText(Context context, int resourceId, int iconSize, CharSequence text) {
        SpannableStringBuilder ssb = new SpannableStringBuilder("*"+text);
        Drawable drawable = context.getResources().getDrawable(resourceId);
        drawable.setBounds(0,0,iconSize,iconSize);
        ImageSpan span = new ImageSpan(drawable);
        ssb.setSpan(span,0,1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return ssb;
    }
}
