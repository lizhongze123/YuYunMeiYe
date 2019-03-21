package cn.yuyun.yymy.view.bannerview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import cn.lzz.utils.ResoureUtils;
import cn.yuyun.yymy.R;

/**
 * @author lzz
 * @desc lzz
 * @date 2018/4/16
 */
public class Banner extends FrameLayout {

    private ViewPager vp;
    private List<?> list = new ArrayList<>();
    private Handler handler = new Handler();
    private BannerPagerAdapter adapter;
    private GlideImageLoader imageLoader;
    private Context context;

    public Banner(Context context) {
        this(context, null);
    }

    public Banner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Banner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        initView(context, attrs);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView(Context context, AttributeSet attrs) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_banner, this, true);
        vp = (ViewPager) view.findViewById(R.id.vp);
        vp.setOffscreenPageLimit(3);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        ViewGroup.LayoutParams params = vp.getLayoutParams();
        // 宽度设置成屏幕宽度的86%，这里根据自己喜好设置
        params.width = (int) (metrics.widthPixels * 0.76);
        vp.setLayoutParams(params);


        //setPageMargin表示设置图片之间的间距
        vp.setPageMargin(getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin));
//        vp.setPageTransformer(true, new MyGallyPageTransformer());
        adapter = new BannerPagerAdapter();
        vp.setAdapter(adapter);
        vp.setCurrentItem(2000);
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            vp.setCurrentItem(adapter.getCount() - 2, false);
                        }
                    }, 250);
                } else if (position == adapter.getCount() - 1) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            vp.setCurrentItem(1, false);
                        }
                    }, 250);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //state: 0空闲，1是滑行中，2加载完毕
                if (state == 1 && isRunning) {
                    // 当处于滑动中并且正在自动播放，则停止滑动
                    // 也就是自动播放的时候用手指去滑动，会停止播放
                    stopAutoPlay();
                } else if (state == 0 && !isRunning) {
                    // 当ViewPager处于空闲状态并且没有在自动播放的时候，才开始自动播放
                    // 也就是当手指离开屏幕时，再次启动自动播放
                    autoPlay();
                }
            }
        });

        // 这里处理触摸两端滑动无效果的问题
        LinearLayout parentLayout = (LinearLayout) findViewById(R.id.ll_parent);
        parentLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return vp.dispatchTouchEvent(motionEvent);
            }
        });
    }

    public void stopAutoPlay() {
        isRunning = false;
        handler.removeCallbacks(runnable);
    }

    private PlayRunnable runnable = new PlayRunnable();
    /** 是否是自动播放状态*/
    private boolean isRunning;

    public void autoPlay() {
        isRunning = true;
        handler.postDelayed(runnable, 3000);
    }

    class PlayRunnable implements Runnable {
        @Override
        public void run() {
            vp.setCurrentItem(vp.getCurrentItem() + 1);
            autoPlay();
        }
    }

    public void setImageList(List<?> imagesUrl) {
        imageLoader = new GlideImageLoader();
        if (imagesUrl == null || imagesUrl.size() <= 0) {
            return;
        }
        for (int i = 0; i < imagesUrl.size(); i++) {
            ImageView imageView = null;
            if (imageLoader != null) {
                imageView = imageLoader.createImageView(context);
            }
            if (imageView == null) {
                imageView = new ImageView(context);
            }
            setScaleType(imageView);
            imageView.setBackgroundColor(ResoureUtils.getColor(context, R.color.loadding_img_bg));
            imageViews.add(imageView);
            if (imageLoader != null){
                imageLoader.displayImage(context, imagesUrl.get(i), imageView);
            }

        }
        adapter.notifyDataSetChanged();
        vp.setCurrentItem(1);
        autoPlay();
    }

    private void setScaleType(View imageView) {
        if (imageView instanceof ImageView) {
            ImageView view = ((ImageView) imageView);
            view.setScaleType(ImageView.ScaleType.FIT_XY);
        }
    }


    private List<View> imageViews = new ArrayList<>();
    class BannerPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imageViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View view = imageViews.get(position);
            container.addView(view);
            /*if (bannerListener != null) {
                view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e(tag, "你正在使用旧版点击事件接口，下标是从1开始，" +
                                "为了体验请更换为setOnBannerListener，下标从0开始计算");
                        bannerListener.OnBannerClick(position);
                    }
                });
            }
            if (listener != null) {
                view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.OnBannerClick(toRealPosition(position));
                    }
                });
            }*/
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }

}
