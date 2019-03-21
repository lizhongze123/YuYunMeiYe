package cn.yuyun.yymy.main;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import cn.lzz.utils.ToastUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.app.MyApplication;
import cn.yuyun.yymy.utils.AppUtils;
import cn.yuyun.yymy.utils.GlideHelper;

/**
 *
 * @autho
 * @date
 */
public class GuideActivity extends AppCompatActivity implements View.OnClickListener{

	private ViewPager viewPager;
	private ViewPagerAdapter adapter;

	/**
	 * 底部指示器(小圆点)的图片
	 */
	private ImageView[] points;

	/**
	 * 当前选中的指针
	 */
	private int currentIndex;
	private int length;

	private RelativeLayout goLayout;
	private ImageView goComing;
	private TextView goLogin;

	private ArrayList<View> views = new ArrayList<>();

	private LinearLayout llPoint;

	/**
	 * 引导界面本地图片
	 */
	public final int[] pics = {R.drawable.guide1, R.drawable.guide2, R.drawable.guide3, R.drawable.guide4};

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			Window window = getWindow();
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
					| View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.setStatusBarColor(Color.TRANSPARENT);
		}
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);
		initViews();
		initData();
	}

	private void initViews() {
		goLayout = (RelativeLayout) findViewById(R.id.goLayout);
		goComing = (ImageView) findViewById(R.id.goComing);
		goLogin = (TextView) findViewById(R.id.goLogin);
		viewPager = (ViewPager) findViewById(R.id.viewPager);
	}

	private void initData() {
		AppUtils.clearThisUserInfo(GuideActivity.this);
		adapter = new ViewPagerAdapter(views);
		for(int i = 0; i < pics.length; i++){
			View view = LayoutInflater.from(this).inflate(R.layout.fragmemt_guide, null);
			length = pics.length;
			ImageView ivGif = (ImageView) view.findViewById(R.id.iv_gif);
			ivGif.setScaleType(ImageView.ScaleType.FIT_XY);
			GlideHelper.displayImage(GuideActivity.this, pics[i], ivGif);
			views.add(view);
		}
		viewPager.setAdapter(adapter);
		viewPager.addOnPageChangeListener(new OnPageChangeListener());
		initPoint();
	}

	/**
	 * 初始化小圆点
	 */
	private void initPoint() {
		//设定一个布局的参数
		llPoint = (LinearLayout) findViewById(R.id.point_layout);
		//生成指示器个数
		points = new ImageView[length];

		for (int i = 0; i < length; i++) {
			points[i] = (ImageView) llPoint.getChildAt(i);
			// 默认都设为灰色
			points[i].setEnabled(true);
			// 给每个小点设置监听
			points[i].setOnClickListener(this);
			// 设置位置tag，方便取出与当前位置对应
			points[i].setTag(i);
		}

		//设置当前默认的位置
		currentIndex = 0;
		//设置为白色即选中的状态
		points[currentIndex].setEnabled(false);
	}

	@Override
	public void onClick(View v) {
		int position = (Integer) v.getTag();
		setCurrentView(position);
		setCurrentDot(position);
	}

	private final class OnPageChangeListener implements ViewPager.OnPageChangeListener {


		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		}

		@Override
		public void onPageSelected(int position) {
			setCurrentDot(position);
			if (position == length - 1) {
				goLayout.setVisibility(View.VISIBLE);
				llPoint.setVisibility(View.GONE);
				goComing.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent();
						intent.setClass(GuideActivity.this, LoginActivity.class);
						startActivity(intent);
						overridePendingTransition(R.anim.screen_zoom_in, R.anim.screen_zoom_out);
						finish();
					}
				});

				goLogin.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						ToastUtils.toastInBottom(MyApplication.getInstance().getApplicationContext(), "此功能正在研发中");
					}
				});
			} else {
				goLayout.setVisibility(View.GONE);
				llPoint.setVisibility(View.VISIBLE);
			}
		}

		@Override
		public void onPageScrollStateChanged(int state) {
		}
	}

	/**
	 * 设置当前页面视图的状态
	 *
	 * @param position
	 */
	private void setCurrentView(int position) {
		if (position < 0 || position >= length) {
			return;
		}
		viewPager.setCurrentItem(position);
	}

	private void setCurrentDot(int position) {
		if (position < 0 || position >= length) {
			return;
		}
		points[position].setEnabled(false);
		points[currentIndex].setEnabled(true);
		currentIndex = position;
	}
}
