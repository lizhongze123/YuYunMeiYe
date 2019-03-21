package cn.yuyun.yymy.ui.home.unboxing;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import cn.example.takephoto.CustomHelper;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.base.BaseNoTitleActivity;
import cn.yuyun.yymy.event.NotifyEvent;
import cn.yuyun.yymy.event.RefreshPicEvent;
import cn.yuyun.yymy.event.RefreshUnboxingEvent;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestUnboxing;
import cn.yuyun.yymy.http.request.RequestUploadPic;
import cn.yuyun.yymy.http.result.ResultUnboxingLabel;
import cn.yuyun.yymy.ui.other.ViewBigImageActivity;
import cn.yuyun.yymy.utils.EvenManager;
import cn.yuyun.yymy.utils.InputLengthFilter;
import cn.yuyun.yymy.view.BorderTextView;
import cn.yuyun.yymy.view.FlowLayout;
import cn.yuyun.yymy.view.selectpic.SelectPicAdapter;
import cn.yuyun.yymy.view.selectpic.SelectPicView;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;

/**
 * @author
 * @desc
 * @date
 */
public class PublishUnboxingActivity extends BaseNoTitleActivity {

    @BindView(R.id.recyclerView)
    SelectPicView selectPicView;

    private CustomHelper customHelper;
    private List<String> compressList = new ArrayList<>();

    @BindView(R.id.et_content)
    EditText etContent;

    private LayoutInflater mInflater;

    @BindView(R.id.flowlayout)
    FlowLayout mFlowLayout;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    @BindView(R.id.title)
    TextView title;

    private List<ResultUnboxingLabel.UnboxingLabelBean> labelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_publish_unboxing);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
        customHelper = new CustomHelper(this).setCrop(false).setLimit(9);
    }

    private void initViews() {
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
                bundle.putBoolean("isShow", false);
                bundle.putStringArrayList("imageList", (ArrayList<String>) compressList);
                startActivity(ViewBigImageActivity.startViewBigImageActivity(mContext, bundle));
            }
        });
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        setLabel();
    }

    private void setLabel() {
        mFlowLayout.removeAllViewsInLayout();
        ResultUnboxingLabel.UnboxingLabelBean bean = new ResultUnboxingLabel.UnboxingLabelBean();
        bean.labelName = "+";
        labelList.add(bean);
        for (int i = 0; i < labelList.size(); i++) {

            if (i == labelList.size() - 1) {
                ImageView iv = (ImageView) mInflater.inflate(R.layout.item_unboxing_label_add4, mFlowLayout, false);
                iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, SelectUnboxingLabelActivity.class);
                        intent.putExtra(EXTRA_DATA, (Serializable) labelList);
                        startActivityForResult(intent, REQUEST_CODE);
                    }
                });
                mFlowLayout.addView(iv);
            }else{
                BorderTextView rv = (BorderTextView) mInflater.inflate(R.layout.item_unboxing_label_add2, mFlowLayout, false);
                rv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, SelectUnboxingLabelActivity.class);
                        intent.putExtra(EXTRA_DATA, (Serializable) labelList);
                        startActivityForResult(intent, REQUEST_CODE);
                    }
                });
                rv.setText(labelList.get(i).labelName);
                mFlowLayout.addView(rv);
            }


        }
        labelList.remove(labelList.size() - 1);
    }

    private void submit(String content, List<String> pictures) {
        RequestUnboxing requestBean = new RequestUnboxing();
        requestBean.content = content;
        if (null != pictures) {
            requestBean.pictures = pictures;
        }
        if(labelList.size() > 0){
            List<Integer> list = new ArrayList<>();
            for (int i = 0; i < labelList.size(); i++) {
                list.add(labelList.get(i).labelId);
            }
            requestBean.labelIdLists = list;
        }
        AppModelFactory.model().submitUnboxing(requestBean, new ProgressSubscriber<Object>(mContext) {
            @Override
            public void onNext(Object o) {
                showToast("已发布");
                EvenManager.sendEvent(new RefreshUnboxingEvent());
                onBackPressed();
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
                showToast(ex.message);
            }
        });

    }

    private void uploadPic(final String content) {
        RequestUploadPic requestUploadPic = new RequestUploadPic();
        requestUploadPic.type = RequestUploadPic.UploadPicType.UNBOXING;
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
            public void onError(ApiException ex) {
                super.onError(ex);
                showToast(ex.message);
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
        if (compressList.size() == 0) {
            selectPicView.setVisibility(View.GONE);
        } else {
            selectPicView.setVisibility(View.VISIBLE);
        }
        selectPicView.notifyDataSetChanged(compressList);
    }

    private final int REQUEST_CODE = 1111;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            labelList.clear();
            labelList = (List<ResultUnboxingLabel.UnboxingLabelBean>) data.getSerializableExtra(EXTRA_DATA);
            if (null != labelList) {
                setLabel();
            }
        }
    }

}
