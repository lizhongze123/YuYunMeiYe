package cn.yuyun.yymy.ui.home.train;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import cn.example.takephoto.CustomHelper;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.base.BaseNoTitleActivity;
import cn.yuyun.yymy.event.NotifyEvent;
import cn.yuyun.yymy.event.RefreshPicEvent;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestAddTrain;
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
 * @desc 添加培训资料
 * @date
 */

public class AddTrainActivity extends BaseNoTitleActivity {

    @BindView(R.id.et_title)
    EditText etTitle;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.selectPicView)
    SelectPicView selectPicView;
    @BindView(R.id.selectStoreView)
    SelectStoreView selectStoreView;

    private final int REQUEST_CODE = 1114;
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
        title.setText("发布培训");
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etTitle.getText().toString())) {
                    showToast("请输入标题");
                    return;
                }
                if (TextUtils.isEmpty(etContent.getText().toString())) {
                    showToast("请输入内容");
                    return;
                }
                if(compressList.size() > 0){
                    uploadPic(etTitle.getText().toString(), etContent.getText().toString());
                }else{
                    submit(etTitle.getText().toString(), etContent.getText().toString(), null);
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
    }

    private void uploadPic(final String title, final String content) {
        RequestUploadPic requestUploadPic = new RequestUploadPic();
        requestUploadPic.type = RequestUploadPic.UploadPicType.TRAIN;
        List<File>fileList = new ArrayList<>();
        for (int i = 0; i < compressList.size(); i++) {
            File file = new File(compressList.get(i));
            fileList.add(file);
        }
        requestUploadPic.file = fileList;
        AppModelFactory.model().uploadPic(requestUploadPic, new ProgressSubscriber<DataBean<List<String>>>(mContext) {
            @Override
            public void onNext(DataBean<List<String>> o) {
                if(null != o.data){
                    submit(title, content, o.data);
                }

            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
                showToast(ex.getMessage());
            }
        });
    }


    private void submit(String title, String content, List<String> pictures) {
        RequestAddTrain requestBean = new RequestAddTrain();
        requestBean.content = content;
        requestBean.title = title;
        if(null != pictures){
            requestBean.pictures = pictures;
        }
        if(null != mIntentResult){
            List<String>list = new ArrayList<>();
            for (ResultClassfiyStoreBean.OgServiceplacesRspListBean bean : mIntentResult) {
                list.add(bean.sp_id);
            }
            requestBean.sp_List = list;
        }
        AppModelFactory.model().submitTrain(requestBean, new ProgressSubscriber<Object>(mContext) {
            @Override
            public void onNext(Object o) {
                showToast("已发布");
                setResult(RESULT_OK);
                onBackPressed();
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
                showToast("提交失败");
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
