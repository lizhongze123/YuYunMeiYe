package cn.yuyun.yymy.view.bigimage;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;

import java.util.ArrayList;
import java.util.List;

import cn.yuyun.yymy.R;
import cn.yuyun.yymy.main.ViewPagerAdapter;
import cn.yuyun.yymy.utils.GlideHelper;

public class MediaBrowsePopupWindow extends PopupWindow {

    private Context context;

    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private ArrayList<View> views = new ArrayList<>();

    public MediaBrowsePopupWindow(Context context) {
        this.context = context;
        initViews();
    }

    private void initViews() {
        View view = View.inflate(context, R.layout.popup_window_media_browse, null);
        setContentView(view);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setFocusable(true);
        setBackgroundDrawable(new ColorDrawable());
        setOutsideTouchable(true);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
    }

    public void showDialog(View view, List<String> pics) {
        adapter = new ViewPagerAdapter(views);
        for(int i = 0; i < pics.size(); i++){
            View v = LayoutInflater.from(context).inflate(R.layout.layout_browse, null);
            ImageView photoView = (ImageView) v.findViewById(R.id.photo_view);
            GlideHelper.displayImage(context, pics.get(i), photoView);
            views.add(v);
        }
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new OnPageChangeListener());
        showAtLocation(view, Gravity.BOTTOM
                | Gravity.CENTER_HORIZONTAL, 0, 0);
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

}
