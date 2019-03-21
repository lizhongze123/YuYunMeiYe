package cn.yuyun.yymy.ui.other;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.http.NetworkUtils;
import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.List;

import cn.lzz.utils.ToastUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.app.MyApplication;
import cn.yuyun.yymy.event.RefreshPicEvent;
import cn.yuyun.yymy.utils.EvenManager;
import cn.yuyun.yymy.utils.RxSaveImage;
import cn.yuyun.yymy.view.bigimage.ViewPagerFixed;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;

/**
 * @author lzz
 * @desc lzz
 * @date 2018/3/17
 */

public class ViewBigImageActivity extends FragmentActivity {

    private ViewPagerFixed viewPager;
    private MyViewPagerAdapter adapter;
    /**
     * 接收传过来的uri地址
     */
    private List<String> picList;
    private int code;
    private ImageView ivDel;
    private boolean isShow = true;
    private boolean isSave = false;
    /**
     * 保存图片
     */
    private TextView tvSaveBigImage;

    public static Intent startViewBigImageActivity(Context context, Bundle bundle) {
        return new Intent(context, ViewBigImageActivity.class).putExtra(EXTRA_DATA, bundle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置状态栏为全透明
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }


        setContentView(R.layout.popup_window_media_browse);
        Bundle bundle = getIntent().getBundleExtra(EXTRA_DATA);
        code = bundle.getInt("code");
        isShow = bundle.getBoolean("isShow",true);
        isSave = bundle.getBoolean("isSave",false);
        picList = bundle.getStringArrayList("imageList");
        initViews();
    }

    private void initViews() {

        /**
         * 若是show的方式则只做显示
         * */
        ivDel = (ImageView) findViewById(R.id.iv_del);
        if (isShow) {
            ivDel.setVisibility(View.GONE);
        } else {
            ivDel.setVisibility(View.VISIBLE);
            ivDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteImage();
                }
            });
        }

        viewPager = (ViewPagerFixed) findViewById(R.id.viewPager);
        adapter = new MyViewPagerAdapter(picList);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(code);
        viewPager.addOnPageChangeListener(new OnPageChangeListener());
        tvSaveBigImage = (TextView) findViewById(R.id.tv_save_big_image);
        if(isSave){
            tvSaveBigImage.setVisibility(View.VISIBLE);
        }else{
            tvSaveBigImage.setVisibility(View.GONE);
        }

        tvSaveBigImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NetworkUtils.hasInternet(ViewBigImageActivity.this)) {
                    ToastUtils.toastInBottom(MyApplication.getInstance().getApplicationContext(), "当前网络不可用，请检查你的网络设置");
                    return;
                }
                ToastUtils.toastInCenter(MyApplication.getInstance().getApplicationContext(), "开始下载图片");
                int pos = viewPager.getCurrentItem();
                RxSaveImage.saveImageToGallery(ViewBigImageActivity.this, picList.get(pos), System.currentTimeMillis() + "");
            }
        });
    }

    /** 删除图片操作*/
    public void deleteImage() {
        if (picList.size() == 1) {
            picList.remove(0);
            EvenManager.sendEvent(new RefreshPicEvent(0));
            finish();
        } else {
            int pos = viewPager.getCurrentItem();
            picList.remove(pos);
            adapter.notifyDataSetChanged();
            EvenManager.sendEvent(new RefreshPicEvent(pos));
        }

    }

    private final class OnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }

    /**
     * ViewPager的适配器
     *
     * @author guolin
     */
    class MyViewPagerAdapter extends PagerAdapter {

        LayoutInflater inflater;
        private List<String> imageList;

        MyViewPagerAdapter(List<String> imageList) {
            inflater = getLayoutInflater();
            this.imageList = imageList;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = inflater.inflate(R.layout.layout_browse, container, false);
            final PhotoView zoomImageView = (PhotoView) view.findViewById(R.id.photo_view);
            zoomImageView.setOnPhotoTapListener(new OnPhotoTapListener() {
                @Override
                public void onPhotoTap(ImageView view, float x, float y) {
                    ViewBigImageActivity.this.finish();
                }
            });
            String imageUrl = (String) getItem(position);
            Glide.with(ViewBigImageActivity.this).load(imageUrl).listener(new RequestListener<String, GlideDrawable>() {
                @Override
                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                    zoomImageView.setScaleType(ImageView.ScaleType.CENTER);
                    ToastUtils.toastInBottom(MyApplication.getInstance().getApplicationContext(), "图片加载异常");
                    return false;
                }

                @Override
                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    zoomImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    return false;
                }
            }).error(ViewBigImageActivity.this.getResources().getDrawable(R.drawable.timeline_image_failure)).into(zoomImageView);
            container.addView(view, 0);
            return view;
        }

        /*@Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = inflater.inflate(R.layout.layout_browse, container, false);
            final PhotoView zoomImageView = (PhotoView) view.findViewById(R.id.photo_view);
            zoomImageView.setOnPhotoTapListener(new OnPhotoTapListener() {
                @Override
                public void onPhotoTap(ImageView view, float x, float y) {
                    ViewBigImageActivity.this.finish();
                }
            });
            String imageUrl = (String) getItem(position);
            RequestOptions myOptions = new RequestOptions().error(ViewBigImageActivity.this.getResources().getDrawable(R.drawable.timeline_image_failure));
            Glide.with(ViewBigImageActivity.this).load(imageUrl).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    zoomImageView.setScaleType(ImageView.ScaleType.CENTER);
                    ToastUtils.toastInBottom(ViewBigImageActivity.this, "图片加载异常");
                    return false;
                }

                //这个用于监听图片是否加载完成
                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    *//**这里应该是加载成功后图片的高*//*
//                    int height = zoomImageView.getHeight();

//                    int wHeight = getWindowManager().getDefaultDisplay().getHeight();
                   *//* if (height > wHeight) {
                        zoomImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    } else {
                        zoomImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    }*//*
                    zoomImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    return false;
                }
            }).apply(myOptions).into(zoomImageView);
            container.addView(view, 0);
            return view;
        }*/

        @Override
        public int getCount() {
            if (imageList == null || imageList.size() == 0) {
                return 0;
            }
            return imageList.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public int getItemPosition(Object object) {
            //重载getItemPosition()，返回POSITION_NONE，adapter.notifyDataSetChanged()就可以正常修改ViewPager里面item的值
            return POSITION_NONE;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }

        Object getItem(int position) {
            return imageList.get(position);
        }
    }

}
