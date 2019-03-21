package cn.yuyun.yymy.ui.home.member;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.lzz.utils.DateTimeUtils;
import cn.lzz.utils.TextViewUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.event.NotifyEvent;
import cn.yuyun.yymy.http.result.ResultCallback;
import cn.yuyun.yymy.http.result.ResultCommunication;
import cn.yuyun.yymy.ui.home.unboxing.FillContent;
import cn.yuyun.yymy.utils.EvenManager;
import cn.yuyun.yymy.utils.GlideHelper;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;
import static cn.yuyun.yymy.constan.Constans.EXTRA_TYPE;

/**
 * @author
 * @desc
 * @date
 */

public class CommunicationDetailActivity extends BaseActivity{

    @BindView(R.id.rv_image)
    RecyclerView rvImage;
    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tv_store)
    TextView tvStore;
    @BindView(R.id.tv_position)
    TextView tvPosition;
    private ResultCommunication communicationBean;
    private ResultCallback callbackBean;

    private static final int COMMUNICATION = 1;
    private static final int CALLBACK = 2;
    private int type;

    public static Intent startCommunicationDetailActivity(Context context, ResultCommunication bean) {
        return new Intent(context, CommunicationDetailActivity.class).putExtra(EXTRA_DATA, bean).putExtra(EXTRA_TYPE, COMMUNICATION);
    }

    public static Intent startCallbackDetailActivity(Context context, ResultCallback bean) {
        return new Intent(context, CommunicationDetailActivity.class).putExtra(EXTRA_DATA, bean).putExtra(EXTRA_TYPE,CALLBACK);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EvenManager.register(this);
        setContentView(R.layout.activity_communication_detail);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNotifyEvent(NotifyEvent notifyEvent) {
        if(notifyEvent.tag == NotifyEvent.RREFRESH_EDIT_COMMUNICATION){
            finish();
        }
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
    }

    private void initViews() {
        type = getIntent().getIntExtra(EXTRA_TYPE,0);
        List<String> list = new ArrayList<>();
        rvImage.setLayoutManager(new LinearLayoutManager(this));
        if(type == COMMUNICATION){
            titleBar.setTilte("沟通记录详情");
            titleBar.setRightIcon(R.drawable.icon_edit);
            titleBar.setRightOnClicker(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(EditCommunicationActivity.startEditCommunicationActivity(mContext, communicationBean));
                }
            });
            communicationBean = (ResultCommunication) getIntent().getSerializableExtra(EXTRA_DATA);
            TextViewUtils.setTextOrEmpty(tvName, communicationBean.createPersonName);
            TextViewUtils.setTextOrEmpty(tvTime, DateTimeUtils.StringToDate(communicationBean.comTime, DateTimeUtils.FORMAT_DATETIME_UI, DateTimeUtils.FORMAT_DATETIME_TWO));
            TextViewUtils.setTextOrEmpty(tvStore, "门店：" + communicationBean.spName);
            TextViewUtils.setTextOrEmpty(tvContent, communicationBean.content);
            if (!TextUtils.isEmpty(communicationBean.createPersonPosition)) {
                tvPosition.setVisibility(View.VISIBLE);
                TextViewUtils.setTextOrEmpty(tvPosition, "(" + communicationBean.createPersonPosition + ")");
            } else {
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
                    list.add(communicationBean.pictures.get(i));
                } else {
                    list.add(mContext.getString(R.string.image_url_prefix) + communicationBean.pictures.get(i));
                }
            }
            FillContent.fillComunicationImgList(list, mContext, rvImage, true, true);
        }else{
            titleBar.setTilte("回访记录详情");
            callbackBean = (ResultCallback) getIntent().getSerializableExtra(EXTRA_DATA);
            TextViewUtils.setTextOrEmpty(tvName, callbackBean.createPersonName);
            TextViewUtils.setTextOrEmpty(tvTime, DateTimeUtils.StringToDate(callbackBean.visTime, DateTimeUtils.FORMAT_DATETIME_UI, DateTimeUtils.FORMAT_DATETIME_TWO));
            TextViewUtils.setTextOrEmpty(tvContent, callbackBean.content);
            if (!TextUtils.isEmpty(callbackBean.createPersonPosition)) {
                tvPosition.setVisibility(View.VISIBLE);
                TextViewUtils.setTextOrEmpty(tvPosition, "(" + callbackBean.createPersonPosition + ")");
            } else {
                tvPosition.setVisibility(View.GONE);
            }
            if(!TextUtils.isEmpty(callbackBean.createPersonAvatar)){

                if(callbackBean.createPersonAvatar.startsWith(mContext.getString(R.string.HTTP))){
                    GlideHelper.displayImage(mContext, callbackBean.createPersonAvatar,ivAvatar);
                }else{
                    GlideHelper.displayImage(mContext, mContext.getString(R.string.image_url_prefix) + callbackBean.createPersonAvatar, ivAvatar);
                }

            }
            for (int i = 0; i < callbackBean.pictures.size(); i++) {
                if (callbackBean.pictures.get(i).startsWith(mContext.getString(R.string.HTTP))) {
                    list.add(callbackBean.pictures.get(i));
                } else {
                    list.add(mContext.getString(R.string.image_url_prefix) + callbackBean.pictures.get(i));
                }
            }
            FillContent.fillComunicationImgList(list, mContext, rvImage, true, true);
        }

    }

}
