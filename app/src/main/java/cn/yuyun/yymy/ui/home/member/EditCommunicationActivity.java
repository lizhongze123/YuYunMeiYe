package cn.yuyun.yymy.ui.home.member;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.example.takephoto.CustomHelper;
import cn.lzz.utils.DateTimeUtils;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.event.NotifyEvent;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestAddCommunication;
import cn.yuyun.yymy.http.request.RequestEditCommunication;
import cn.yuyun.yymy.http.request.RequestUploadPic;
import cn.yuyun.yymy.http.result.ResultCallback;
import cn.yuyun.yymy.http.result.ResultCommunication;
import cn.yuyun.yymy.sp.LoginInfoPrefs;
import cn.yuyun.yymy.sp.UserfoPrefs;
import cn.yuyun.yymy.ui.home.unboxing.FillContent;
import cn.yuyun.yymy.ui.other.ViewBigImageActivity;
import cn.yuyun.yymy.utils.EvenManager;
import cn.yuyun.yymy.utils.GlideHelper;
import cn.yuyun.yymy.utils.InputLengthFilter;
import cn.yuyun.yymy.view.selectpic.SelectPicAdapter;
import cn.yuyun.yymy.view.selectpic.SelectPicView;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;
import static cn.yuyun.yymy.constan.Constans.EXTRA_TYPE;

/**
 * @author
 * @desc
 * @date
 */

public class EditCommunicationActivity extends BaseActivity{

    @BindView(R.id.selectPicView)
    SelectPicView selectPicView;
    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.tv_store)
    TextView tvStore;
    @BindView(R.id.tv_position)
    TextView tvPosition;
    private ResultCommunication communicationBean;
    private CustomHelper customHelper;
    private List<String> allList = new ArrayList<>();
    private List<String> intentList = new ArrayList<>();
    private List<String> uploadList = new ArrayList<>();

    public static Intent startEditCommunicationActivity(Context context, ResultCommunication bean) {
        return new Intent(context, EditCommunicationActivity.class).putExtra(EXTRA_DATA, bean);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_communication);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
        customHelper = new CustomHelper(this).setCrop(false).setLimit(9);
    }

    private void initViews() {
        titleBar.setTilte("沟通记录详情");
        titleBar.setTvRight("保存");
        titleBar.setRightOnClicker(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etContent.getText().toString())) {
                    showToast("请输入内容");
                    return;
                }
                if(uploadList.size() > 0){
                    uploadPic(etContent.getText().toString());
                }else{
                    if(intentList.size() == 0){
                        editCommunication(etContent.getText().toString(), null);
                    }else{
                        editCommunication(etContent.getText().toString(), intentList);
                    }
                }
            }
        });
        communicationBean = (ResultCommunication) getIntent().getSerializableExtra(EXTRA_DATA);
        TextViewUtils.setTextOrEmpty(tvName, communicationBean.createPersonName);
        TextViewUtils.setTextOrEmpty(tvTime, DateTimeUtils.StringToDate(communicationBean.comTime, DateTimeUtils.FORMAT_DATETIME_UI, DateTimeUtils.FORMAT_DATETIME_TWO));
        TextViewUtils.setTextOrEmpty(tvStore, "门店：" + communicationBean.spName);
        etContent.setText(communicationBean.content);

        if(!TextUtils.isEmpty(communicationBean.createPersonPosition)){
            tvPosition.setVisibility(View.VISIBLE);
            tvPosition.setText("(" + communicationBean.createPersonPosition + ")");
        }else{
            tvPosition.setVisibility(View.GONE);
        }

        if(!TextUtils.isEmpty(communicationBean.createPersonAvatar)){
            if(communicationBean.createPersonAvatar.startsWith(mContext.getString(R.string.HTTP))){
                GlideHelper.displayImage(mContext, communicationBean.createPersonAvatar,ivAvatar);
            }else{
                GlideHelper.displayImage(mContext, mContext.getString(R.string.image_url_prefix) + communicationBean.createPersonAvatar, ivAvatar);
            }

        }
        for (int i = 0; i < communicationBean.pictures.size(); i++) {
            if (communicationBean.pictures.get(i).startsWith(mContext.getString(R.string.HTTP))) {
                intentList.add(communicationBean.pictures.get(i));
                allList.add(communicationBean.pictures.get(i));
            } else {
                intentList.add(mContext.getString(R.string.image_url_prefix) + communicationBean.pictures.get(i));
                allList.add(mContext.getString(R.string.image_url_prefix) + communicationBean.pictures.get(i));
            }
        }

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

    private void editCommunication(String content, final List<String> pictures) {
        final RequestEditCommunication requestBean = new RequestEditCommunication();
        requestBean.content = content;
        requestBean.id = communicationBean.memberCommunicationId;
        requestBean.comTime = System.currentTimeMillis() / 1000;
        requestBean.groupId = LoginInfoPrefs.getInstance(mContext).getGroupId();
        requestBean.staffId = LoginInfoPrefs.getInstance(mContext).getStaffId();
        if(null != pictures){
            requestBean.pictures = pictures;
        }
        AppModelFactory.model().editCommunication(requestBean ,new ProgressSubscriber<Object>(mContext) {
            @Override
            public void onNext(Object result) {
                showToast("编辑成功");
                allList = pictures;
                EvenManager.sendEvent(new NotifyEvent(NotifyEvent.RREFRESH_EDIT_COMMUNICATION));
                onBackPressed();
            }

            @Override
            public void onNoNetWorkError() {
                super.onNoNetWorkError();
                showToast(getString(R.string.error_no_network));
            }

            @Override
            public void onNormalError(ApiException ex) {
                super.onNormalError(ex);
                showToast("编辑失败");
            }

        });
    }

    private void uploadPic(final String content) {
        RequestUploadPic requestUploadPic = new RequestUploadPic();
        requestUploadPic.type = RequestUploadPic.UploadPicType.COMMUNICATION;
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
                        editCommunication(content, o.data);
                    }else{
                        /*for (int i = intentList.size(); i > 0; i--) {
                            o.data.add(0, intentList.get(i-1));
                        }*/
                        for (int i = 0; i < intentList.size(); i++) {
                            o.data.add(intentList.get(i));
                        }
                        editCommunication(content, o.data);
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
                showToast("编辑失败，请重试");
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
