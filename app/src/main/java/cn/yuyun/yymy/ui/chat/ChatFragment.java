package cn.yuyun.yymy.ui.chat;

import android.view.View;

import com.githang.statusbar.StatusBarCompat;

import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseFragment;

/**
 * @author
 * @desc
 * @date
 */
public class ChatFragment extends BaseFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_chat;
    }

    @Override
    public void onResume() {
        StatusBarCompat.setStatusBarColor(getActivity(), getResources().getColor(R.color.title_bg));
        super.onResume();
    }

    @Override
    protected void initViews(View root) {
        super.initViews(root);
        titleBar.setTilte("");
    }

    @Override
    protected void initData() {
        super.initData();
    }


}
