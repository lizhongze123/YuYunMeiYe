package cn.yuyun.yymy.view.kcalendar;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.yuyun.yymy.R;

/**
 * Created by lzz on 2017/1/9.
 */

public class KCalendarView extends LinearLayout {

	private Context mContext;
	private String date = null;// 设置默认选中的日期  格式为 “2014-04-05” 标准DATE格式

	private TextView popupwindow_calendar_month;
	private List<String> listDate = new ArrayList<>();
	private int DEFAULT_RESOURCE_ID = R.drawable.app_calendar_date_focused_green;
	private KCalendar calendar;

	private String productID;

	public KCalendarView(Context context) {
		this(context, null);
	}

	public KCalendarView(Context context, @Nullable AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public KCalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	private void init(Context context) {
		LayoutInflater.from(context).inflate(R.layout.layout_calendar_view, this);
		calendar = (KCalendar) findViewById(R.id.popupwindow_calendar);
		popupwindow_calendar_month = (TextView) findViewById(R.id.popupwindow_calendar_month);
		popupwindow_calendar_month.setText(calendar.getCalendarYear() + "年" + calendar.getCalendarMonth() + "月");
		if (null != date) {
			int years = Integer.parseInt(date.substring(0, date.indexOf("-")));
			int month = Integer.parseInt(date.substring(date.indexOf("-") + 1, date.lastIndexOf("-")));
			popupwindow_calendar_month.setText(years + "年" + month + "月");
			calendar.showCalendar(years, month);
		}
		if(listDate.size() > 0){
			calendar.setCalendarDaysBgColor(listDate, DEFAULT_RESOURCE_ID);
		}

		//设置标记列表
		List<String> list = new ArrayList<>();
		calendar.addMarks(list, 0);
		setListeners();
	}

	private CalendarClickListener listener;
	public interface CalendarClickListener{
		void onClick(String dateFormat);
	}
	public void setCalendarClickListener(CalendarClickListener listener){
		this.listener = listener;
	}

	private void setListeners() {
		//监听所选中的日期
		calendar.setOnCalendarClickListener(new KCalendar.OnCalendarClickListener() {

			@Override
			public void onCalendarClick(int row, int col, String dateFormat) {
				int month = Integer.parseInt(dateFormat.substring(dateFormat.indexOf("-") + 1, dateFormat.lastIndexOf("-")));
				calendar.removeAllBgColor();
				calendar.setCalendarDayBgColor(dateFormat, R.drawable.app_calendar_date_focused_green);
				//跨年跳转
				if (calendar.getCalendarMonth() - month == 1
						|| calendar.getCalendarMonth() - month == -11) {
					calendar.lastMonth();
				//跨年跳转
				} else if (month - calendar.getCalendarMonth() == 1
						|| month - calendar.getCalendarMonth() == -11) {
					calendar.nextMonth();

				} else {
					//最后返回给全局 date
					date = dateFormat;
				}

				listener.onClick(dateFormat);
			}

		});

		//监听当前月份
		calendar.setOnCalendarDateChangedListener(new KCalendar.OnCalendarDateChangedListener() {
			@Override
			public void onCalendarDateChanged(int year, int month) {
				popupwindow_calendar_month.setText(year + "年" + month + "月");
			}
		});

		calendar.setOnFlingListener(new KCalendar.OnFlingListener() {
			@Override
			public void onFlingNext() {
				calendar.nextMonth();
			}

			@Override
			public void onFlingLast() {
				calendar.lastMonth();
			}
		});

		//上月监听按钮
		RelativeLayout popupwindow_calendar_last_month = (RelativeLayout) findViewById(R.id.popupwindow_calendar_last_month);
		popupwindow_calendar_last_month.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				calendar.lastMonth();
			}

		});

		//下月监听按钮
		RelativeLayout popupwindow_calendar_next_month = (RelativeLayout)findViewById(R.id.popupwindow_calendar_next_month);
		popupwindow_calendar_next_month
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						calendar.nextMonth();
					}
				});

	}

}
