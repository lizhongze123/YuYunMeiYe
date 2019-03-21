package cn.yuyun.yymy.ui.home.actions;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.http.ApiException;
import com.example.http.DataBean;
import com.example.http.ProgressSubscriber;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.example.takephoto.CustomHelper;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.base.BaseNoTitleActivity;
import cn.yuyun.yymy.event.NotifyEvent;
import cn.yuyun.yymy.event.RefreshPicEvent;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestAddNotice;
import cn.yuyun.yymy.http.request.RequestUploadPic;
import cn.yuyun.yymy.http.result.ResultClassfiyStoreBean;
import cn.yuyun.yymy.ui.home.leave.SelectStoreListMultiSelActivity;
import cn.yuyun.yymy.ui.home.work.SelectStoreAdapter;
import cn.yuyun.yymy.ui.home.work.SelectStoreView;
import cn.yuyun.yymy.ui.other.ViewBigImageActivity;
import cn.yuyun.yymy.utils.EvenManager;
import cn.yuyun.yymy.view.EditTextWithDel;
import cn.yuyun.yymy.view.selectpic.SelectPicAdapter;
import cn.yuyun.yymy.view.selectpic.SelectPicView;

/**
 * @author
 * @desc
 * @date
 */

public class AddNoticeActivity extends BaseNoTitleActivity{

    @BindView(R.id.et_title)
    EditText etTitle;
    @BindView(R.id.ll_title)
    LinearLayout llTitle;
    @BindView(R.id.line)
    View line;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.selectPicView)
    SelectPicView selectPicView;
    @BindView(R.id.selectStoreView)
    SelectStoreView selectStoreView;
    private List<ResultClassfiyStoreBean.OgServiceplacesRspListBean> mIntentResult;
    private CustomHelper customHelper;
    private List<String> compressList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_action);
        EvenManager.register(this);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
        customHelper = new CustomHelper(this).setCrop(false).setLimit(9);
    }

    private void initViews() {
        line.setVisibility(View.GONE);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etContent.getText().toString())) {
                    showToast("请输入内容");
                    return;
                }
                if (compressList.size() > 0) {
                    uploadPic(etContent.getText().toString());
                } else {
                    submit(etContent.getText().toString(), null);
                }
            }
        });
        selectPicView.setMax(9);
        selectPicView.isDel(true);
        selectPicView.setAdapter(new SelectPicAdapter.OnClickCallBack() {
            @Override
            public void onAdd() {
                customHelper.onClick(getTakePhoto());
            }

            @Override
            public void onDel(int pos) {
                compressList.remove(pos);
                selectPicView.notifyDataSetChanged(compressList);
            }

            @Override
            public void onBrowser(int pos) {
                Bundle bundle = new Bundle();
                bundle.putInt("code", pos);
                bundle.putBoolean("isShow", true);
                bundle.putStringArrayList("imageList", (ArrayList<String>) compressList);
                startActivity(ViewBigImageActivity.startViewBigImageActivity(mContext, bundle));
            }
        });
        selectStoreView.setAdapter(new SelectStoreAdapter.OnClickCallBack() {
            @Override
            public void onAdd() {
                startActivity(SelectStoreListMultiSelActivity.startMultiSelActivity(mContext, mIntentResult));
            }

            @Override
            public void onDel(int pos) {
                mIntentResult.remove(pos);
                selectStoreView.notifyDataSetChanged(mIntentResult);

            }
        });
        selectStoreView.setMax(Integer.MAX_VALUE);
        llTitle.setVisibility(View.GONE);
    }

    private void uploadPic(final String content) {
        RequestUploadPic requestUploadPic = new RequestUploadPic();
        requestUploadPic.type = RequestUploadPic.UploadPicType.NOTICE;
        List<File> fileList = new ArrayList<>();
        for (int i = 0; i < compressList.size(); i++) {
            File file = new File(compressList.get(i));
            fileList.add(file);
        }
        requestUploadPic.file = fileList;
        AppModelFactory.model().uploadPic(requestUploadPic, new ProgressSubscriber<DataBean<List<String>>>(mContext) {
            @Override
            public void onNext(DataBean<List<String>> o) {
                if (null != o.data) {
                    submit(content, o.data);
                }

            }

            @Override
            public void onNormalError(ApiException ex) {
                super.onNormalError(ex);
                showToast(ex.message);
            }

            @Override
            public void onNoNetWorkError() {
                super.onNoNetWorkError();
                showToast("网络异常，请检查网络");
            }

        });
    }

    private void submit(String content, List<String> pictures) {
        RequestAddNotice requestBean = new RequestAddNotice();
        requestBean.content = content;
        if(null != mIntentResult){
            List<String>list = new ArrayList<>();
            for (ResultClassfiyStoreBean.OgServiceplacesRspListBean bean : mIntentResult) {
                list.add(bean.sp_id);
            }
            requestBean.sp_List = list;
        }
        if (null != pictures) {
            requestBean.pictures = pictures;
        }
        AppModelFactory.model().submitNotice(requestBean, new ProgressSubscriber<Object>(mContext) {
            @Override
            public void onNext(Object o) {
                showToast("已发布");
                setResult(RESULT_OK);
                onBackPressed();
            }

            @Override
            public void onNormalError(ApiException ex) {
                super.onNormalError(ex);
                showToast(ex.message);
            }

            @Override
            public void onNoNetWorkError() {
                super.onNoNetWorkError();
                showToast("网络异常，请检查网络");
            }
        });
    }

    @Override
    public void takeCancel() {
        super.takeCancel();
    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
//        path = result.getImages().get(0).getCompressPath();
        showImg(result.getImages());
    }

    private void showImg(ArrayList<TImage> images) {
        for (int i = 0; i < images.size(); i++) {
            if(compressList.size() < 9){
                compressList.add(images.get(i).getCompressPath());
            }
        }
        selectPicView.notifyDataSetChanged(compressList);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNotifyEvent(RefreshPicEvent refreshPicEvent) {
        compressList.remove(refreshPicEvent.pos);
        selectPicView.notifyDataSetChanged(compressList);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNotifyEvent(NotifyEvent notifyEvent) {
        if (notifyEvent.tag == NotifyEvent.DATA) {
            mIntentResult = (List<ResultClassfiyStoreBean.OgServiceplacesRspListBean>) notifyEvent.value;
            selectStoreView.notifyDataSetChanged(mIntentResult);
        }
    }


    @Override
    protected void onDestroy() {
        EvenManager.unregister(this);
        super.onDestroy();
    }
}
