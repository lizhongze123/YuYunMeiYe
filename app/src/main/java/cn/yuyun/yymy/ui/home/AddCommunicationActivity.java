package cn.yuyun.yymy.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import cn.yuyun.yymy.event.RefreshPicEvent;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestAddCallback;
import cn.yuyun.yymy.http.request.RequestAddCommunication;
import cn.yuyun.yymy.http.request.RequestUploadPic;
import cn.yuyun.yymy.http.result.ResultMemberBean;
import cn.yuyun.yymy.sp.LoginInfoPrefs;
import cn.yuyun.yymy.sp.UserfoPrefs;
import cn.yuyun.yymy.ui.other.ViewBigImageActivity;
import cn.yuyun.yymy.utils.EvenManager;
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

public class AddCommunicationActivity extends BaseNoTitleActivity{

    public int type;
    public static final int COMMUNICATION = 1;
    public static final int CALLBACK = 2;
    public static final int FEEDBACK = 3;

    private ResultMemberBean memberBean;

    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.selectPicView)
    SelectPicView selectPicView;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;

    private CustomHelper customHelper;
    private List<String> compressList = new ArrayList<>();

    public static Intent startFromCommunication(Context context, ResultMemberBean resultMemberBean) {
        return new Intent(context, AddCommunicationActivity.class).putExtra(EXTRA_DATA, resultMemberBean).putExtra(EXTRA_TYPE, COMMUNICATION);
    }

    public static Intent startFromCallback(Context context, ResultMemberBean resultMemberBean) {
        return new Intent(context, AddCommunicationActivity.class).putExtra(EXTRA_DATA, resultMemberBean).putExtra(EXTRA_TYPE, CALLBACK);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_communication);

    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
        customHelper = new CustomHelper(this).setCrop(false).setLimit(9);
    }

    private void initViews() {
        type = getIntent().getIntExtra(EXTRA_TYPE, 0);
        memberBean = (ResultMemberBean) getIntent().getSerializableExtra(EXTRA_DATA);
        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etContent.getText().toString())) {
                    showToast("请输入内容");
                    return;
                }
                if(compressList.size() > 0){
                    uploadPic(etContent.getText().toString());
                }else{
                    if(type == COMMUNICATION) {
                        addCommunication(etContent.getText().toString(), null);
                    }else{
                        addCallback(etContent.getText().toString(), null);
                    }
                }
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
    }

    private void uploadPic(final String content) {
        RequestUploadPic requestUploadPic = new RequestUploadPic();
        if(type == COMMUNICATION) {
            requestUploadPic.type = RequestUploadPic.UploadPicType.COMMUNICATION;
        }else{
            requestUploadPic.type = RequestUploadPic.UploadPicType.CALLBACK;
        }

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
                    if(type == COMMUNICATION) {
                        addCommunication(content, o.data);
                    }else{
                        addCallback(content, o.data);
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

    private void addCallback(String content, List<String> pictures) {
        RequestAddCallback requestBean = new RequestAddCallback();
        requestBean.content = content;
        requestBean.visTime = System.currentTimeMillis() / 1000;
        requestBean.memberId = memberBean.memberId;
        requestBean.staffId = LoginInfoPrefs.getInstance(mContext).getStaffId();
        requestBean.spId = UserfoPrefs.getInstance(mContext).getBaseonId();
        if(null != pictures){
            requestBean.pictures = pictures;
        }
        AppModelFactory.model().addCallback(requestBean ,new ProgressSubscriber<Object>(mContext) {
            @Override
            public void onNext(Object result) {
                showToast("添加成功");
                setResult(RESULT_OK);
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
                showToast("添加失败");
            }

        });
    }

    private void addCommunication(String content, List<String> pictures) {
        RequestAddCommunication requestBean = new RequestAddCommunication();
        requestBean.content = content;
        requestBean.comTime = System.currentTimeMillis() / 1000;
        requestBean.memberId = memberBean.memberId;
        requestBean.groupId = LoginInfoPrefs.getInstance(mContext).getGroupId();
        requestBean.staffId = LoginInfoPrefs.getInstance(mContext).getStaffId();
        requestBean.spName = UserfoPrefs.getInstance(mContext).getBaseonTypeDesc();
        if(null != pictures){
            requestBean.pictures = pictures;
        }
        AppModelFactory.model().addCommunication(requestBean ,new ProgressSubscriber<Object>(mContext) {
            @Override
            public void onNext(Object result) {
                showToast("添加成功");
                setResult(RESULT_OK);
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
                showToast("添加失败");
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

    @Override
    protected void onDestroy() {
        EvenManager.unregister(this);
        super.onDestroy();
    }

}
