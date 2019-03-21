package cn.yuyun.yymy.ui.home.actions;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
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
import cn.lzz.utils.LogUtils;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.event.NotifyEvent;
import cn.yuyun.yymy.event.RefreshPicEvent;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestAddTrain;
import cn.yuyun.yymy.http.request.RequestEditActions;
import cn.yuyun.yymy.http.request.RequestUploadPic;
import cn.yuyun.yymy.http.result.ActionBean;
import cn.yuyun.yymy.ui.home.work.SelectStoreView;
import cn.yuyun.yymy.ui.other.ViewBigImageActivity;
import cn.yuyun.yymy.utils.EvenManager;
import cn.yuyun.yymy.view.EditTextWithDel;
import cn.yuyun.yymy.view.selectpic.SelectPicAdapter;
import cn.yuyun.yymy.view.selectpic.SelectPicView;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;

/**
 * @author
 * @desc 编辑活动
 * @date
 */

public class EditActionActivity extends BaseActivity {

    @BindView(R.id.et_title)
    EditText etTitle;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.selectPicView)
    SelectPicView selectPicView;
    @BindView(R.id.selectStoreView)
    SelectStoreView selectStoreView;
    @BindView(R.id.tv_store)
    TextView tvStore;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    private ActionBean actionBean;
    private CustomHelper customHelper;
    private List<String> allList = new ArrayList<>();
    private List<String> intentList = new ArrayList<>();
    private List<String> uploadList = new ArrayList<>();

    public static Intent startEditActionActivity(Context context, ActionBean bean) {
        return new Intent(context, EditActionActivity.class).putExtra(EXTRA_DATA, bean);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_action);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
        customHelper = new CustomHelper(this).setCrop(false).setLimit(9);
    }

    private void initViews() {
        rlTitle.setVisibility(View.GONE);
        titleBar.setTilte("编辑活动");
        titleBar.setTvRight("保存");
        titleBar.setRightOnClicker(new View.OnClickListener() {
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
                if(uploadList.size() > 0){
                    uploadPic(etTitle.getText().toString(),etContent.getText().toString());
                }else{
                    if(intentList.size() == 0){
                        submit(etTitle.getText().toString(), etContent.getText().toString(), null);
                    }else{
                        submit(etTitle.getText().toString(), etContent.getText().toString(), intentList);
                    }
                }
            }
        });
        actionBean = (ActionBean) getIntent().getSerializableExtra(EXTRA_DATA);
        etTitle.setText(actionBean.latestActivityVo.title);
        etContent.setText(actionBean.latestActivityVo.content);
        for (int i = 0; i < actionBean.latestActivityVo.pictures.size(); i++) {
            if (actionBean.latestActivityVo.pictures.get(i).startsWith(mContext.getString(R.string.HTTP))) {
                intentList.add(actionBean.latestActivityVo.pictures.get(i));
                allList.add(actionBean.latestActivityVo.pictures.get(i));
            } else {
                intentList.add(mContext.getString(R.string.image_url_prefix) + actionBean.latestActivityVo.pictures.get(i));
                allList.add(mContext.getString(R.string.image_url_prefix) + actionBean.latestActivityVo.pictures.get(i));
            }
        }
        tvStore.setVisibility(View.GONE);
        selectStoreView.setVisibility(View.GONE);
        selectPicView.setMax(9);
        selectPicView.isDel(true);
        selectPicView.setAdapter(new SelectPicAdapter.OnClickCallBack() {
            @Override
            public void onAdd() {
                customHelper.onClick(getTakePhoto());
            }

            @Override
            public void onDel(int pos) {
                allList.remove(pos);
                if(pos - intentList.size() >= 0){
                    uploadList.remove(pos - intentList.size());
                }else{
                    intentList.remove(pos);
                }
                selectPicView.notifyDataSetChanged(allList);
            }

            @Override
            public void onBrowser(int pos) {
                Bundle bundle = new Bundle();
                bundle.putInt("code", pos);
                bundle.putBoolean("isShow", true);
                bundle.putStringArrayList("imageList", (ArrayList<String>) intentList);
                startActivity(ViewBigImageActivity.startViewBigImageActivity(mContext, bundle));
            }
        });
        selectPicView.notifyDataSetChanged(allList);
    }

    private void uploadPic(final String title, final String content) {
        RequestUploadPic requestUploadPic = new RequestUploadPic();
        requestUploadPic.type = RequestUploadPic.UploadPicType.ACTION;
        List<File>fileList = new ArrayList<>();
        for (int i = 0; i < uploadList.size(); i++) {
            File file = new File(uploadList.get(i));
            fileList.add(file);
        }
        requestUploadPic.file = fileList;
        AppModelFactory.model().uploadPic(requestUploadPic, new ProgressSubscriber<DataBean<List<String>>>(mContext) {
            @Override
            public void onNext(DataBean<List<String>> o) {
                if(null != o.data){
                    if(intentList.size() == 0){
                        submit(title, content, o.data);
                    }else{
                        for (int i = 0; i < intentList.size(); i++) {
                            o.data.add(intentList.get(i));
                        }
                        submit(title, content, o.data);
                    }
                }

            }

            @Override
            public void onNoNetWorkError() {
                super.onNoNetWorkError();
                showToast(getString(R.string.error_no_network));
            }

            @Override
            public void onNormalError(ApiException ex) {
                super.onNormalError(ex);
                showToast(ex.message);
            }
        });
    }


    private void submit(String title, String content, final List<String> pictures) {
        LogUtils.e("lzz");
        RequestEditActions requestBean = new RequestEditActions();
        requestBean.content = content;
        requestBean.title = title;
        requestBean.actionId = actionBean.latestActivityVo.latestActivityId;
        if(null != pictures){
            requestBean.pictures = pictures;
        }
        AppModelFactory.model().editAction(requestBean, new ProgressSubscriber<Object>(mContext) {
            @Override
            public void onNext(Object o) {
                showToast("编辑成功");
//                allList = pictures;
                EvenManager.sendEvent(new NotifyEvent(NotifyEvent.RREFRESH_EDIT_ACTIONS));
                finish();
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
        showImg(result.getImages());
    }

    private void showImg(ArrayList<TImage> images) {
        for (int i = 0; i < images.size(); i++) {
            if(allList.size() < 9){
                allList.add(images.get(i).getCompressPath());
                uploadList.add(images.get(i).getCompressPath());
            }
        }
        selectPicView.notifyDataSetChanged(allList);
    }

}
