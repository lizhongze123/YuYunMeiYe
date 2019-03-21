package cn.yuyun.yymy.ui.help;

import android.view.View;

import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseFragment;

/**
 * @author
 * @desc
 * @date
 */
public class HelpFragment extends BaseFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_help;
    }


    @Override
    protected void initViews(View root) {
        super.initViews(root);
        titleBar.setTilte("发现");
    }


}
