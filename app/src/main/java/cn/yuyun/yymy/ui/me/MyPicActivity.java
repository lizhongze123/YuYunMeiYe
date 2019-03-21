package cn.yuyun.yymy.ui.me;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.example.takephoto.CustomHelper;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.base.BaseNoTitleActivity;
import cn.yuyun.yymy.bean.Sex;
import cn.yuyun.yymy.http.result.ResultMemberBean;
import cn.yuyun.yymy.http.result.ResultPicWall;
import cn.yuyun.yymy.sp.UserfoPrefs;
import cn.yuyun.yymy.ui.other.ViewBigImageActivity;
import cn.yuyun.yymy.utils.GlideHelper;
import cn.yuyun.yymy.view.selectpic.SelectPicAdapter;
import cn.yuyun.yymy.view.selectpic.SelectPicView;
import cn.yuyun.yymy.view.transition.EasyTransition;

/**
 * @author lzz
 * @desc
 * @date
 */

public class MyPicActivity extends BaseNoTitleActivity{

    private ResultMemberBean memberBean;
    private SelectPicView selectPicView;
    private List<ResultPicWall> resultList = new ArrayList<>();
    private List<String> list = new ArrayList<>();
    private CustomHelper customHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_pic);
    }

    @Override
    protected void setUpViewAndData() {
        initViews();
    }

    private void initViews() {
        selectPicView = (SelectPicView) findViewById(R.id.recyclerView);
        selectPicView.setMax(Integer.SIZE);
        selectPicView.setAdapter(new SelectPicAdapter.OnClickCallBack() {
            @Override
            public void onAdd() {
                customHelper.onClick(getTakePhoto());
            }

            @Override
            public void onDel(int pos) {
//                delPicWall(resultList.get(pos).memberLifePhotoId);
            }

            @Override
            public void onBrowser(int pos) {
                Bundle bundle = new Bundle();
                bundle.putInt("code", pos);
                bundle.putStringArrayList("imageList", (ArrayList<String>) list);
                startActivity(ViewBigImageActivity.startViewBigImageActivity(mContext, bundle));
            }
        });
        selectPicView.editable(true);
        customHelper = new CustomHelper(this).setCrop(false);
    }

}
