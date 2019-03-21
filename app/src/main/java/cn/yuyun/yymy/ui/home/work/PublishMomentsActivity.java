package cn.yuyun.yymy.ui.home.work;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.example.http.ApiException;
import com.example.http.DataBean;
import com.example.http.ProgressSubscriber;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.example.takephoto.CustomHelper;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.base.BaseNoTitleActivity;
import cn.yuyun.yymy.event.NotifyEvent;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestSubmitWork;
import cn.yuyun.yymy.http.request.RequestUploadPic;
import cn.yuyun.yymy.http.result.ResultListStaff;
import cn.yuyun.yymy.sp.ApprovePrefs;
import cn.yuyun.yymy.sp.LoginInfoPrefs;
import cn.yuyun.yymy.sp.UserfoPrefs;
import cn.yuyun.yymy.ui.home.leave.SelectStoreListActivity;
import cn.yuyun.yymy.ui.other.ViewBigImageActivity;
import cn.yuyun.yymy.utils.EvenManager;
import cn.yuyun.yymy.utils.InputLengthFilter;
import cn.yuyun.yymy.view.WarnningDialog;
import cn.yuyun.yymy.view.selectpic.SelectPicAdapter;
import cn.yuyun.yymy.view.selectpic.SelectPicView;

/**
 * @author
 * @desc
 * @date
 */

public class PublishMomentsActivity extends BaseNoTitleActivity {

    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.selectPicView)
    SelectPicView selectPicView;
    @BindView(R.id.selectPeopleView)
    SelectPeopleView selectPeopleView;
    @BindView(R.id.checkbox)
    CheckBox checkBox;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    private WarnningDialog warnningDialog;

    private OptionsPickerView<String> typePickerView;

    private RequestSubmitWork requestSubmitWork;

    private CustomHelper customHelper;
    private List<String> picList = new ArrayList<>();

    private List<SelectBean> dataList = new ArrayList<>();

    private List<ResultListStaff> intentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_moments);
        EvenManager.register(this);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
    }

    private void initViews() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataCheck()) {
                    //如果有图片先上传图片
                    if (picList.size() > 0) {
                        submitPic();
                    } else {
                        submitNormal(null);
                    }

                }
            }
        });
        final ArrayList<String> mDatas = new ArrayList<>();
        mDatas.add("日报");
        mDatas.add("周报");
        mDatas.add("月报");
        typePickerView = new OptionsPickerBuilder(mContext, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                tvType.setText(mDatas.get(options1));
                if(options1 == 0){
                    requestSubmitWork.type = 1;
                }else if(options1 == 1){
                    requestSubmitWork.type = 2;
                }else{
                    requestSubmitWork.type = 3;
                }
            }
        }).build();
        typePickerView.setPicker(mDatas);
        tvType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typePickerView.show();
            }
        });
        requestSubmitWork = new RequestSubmitWork();
        selectPeopleView.setAdapter(new SelectPeopleAdapter.OnClickCallBack() {
            @Override
            public void onAdd() {
                startActivity(SelectStoreListActivity.startTypeSelectStaff(mContext, intentList));
            }

            @Override
            public void onDel(int pos) {
                intentList.remove(pos);
                dataList.remove(pos);
                selectPeopleView.notifyDataSetChanged(dataList);

            }
        });
        selectPeopleView.setMax(Integer.MAX_VALUE);
        //取出记住的审批人
        String json1 = ApprovePrefs.getInstance(mContext).getJson1();
        if (!TextUtils.isEmpty(json1)) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<ResultListStaff>>() {
            }.getType();
            intentList = gson.fromJson(json1, type);
        }

        for (int i = 0; i < intentList.size(); i++) {
            SelectBean bean = new SelectBean();
            if (!TextUtils.isEmpty(intentList.get(i).avatar)) {
                if (intentList.get(i).avatar.startsWith(getString(R.string.HTTP))) {
                    bean.pic_url = intentList.get(i).avatar;
                } else {
                    bean.pic_url = getString(R.string.image_url_prefix) + intentList.get(i).avatar;
                }
            }
            if (null != intentList.get(i).sex) {
                bean.type = intentList.get(i).sex.value;
            }
            bean.name = intentList.get(i).staff_name;
            dataList.add(bean);
        }
        selectPeopleView.addAll(dataList);

        customHelper = new CustomHelper(this).setCrop(false).setLimit(9);
        selectPicView.setMax(9);
        selectPicView.setAdapter(new SelectPicAdapter.OnClickCallBack() {
            @Override
            public void onAdd() {
                customHelper.onClick(getTakePhoto());
            }

            @Override
            public void onDel(int pos) {
                picList.remove(pos);
                selectPicView.notifyDataSetChanged(picList);
            }

            @Override
            public void onBrowser(int pos) {
                Bundle bundle = new Bundle();
                bundle.putInt("code", pos);
                bundle.putBoolean("isShow", false);
                bundle.putStringArrayList("imageList", (ArrayList<String>) picList);
                startActivity(ViewBigImageActivity.startViewBigImageActivity(mContext, bundle));
            }
        });
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isCheck) {
                if (isCheck) {
                    requestSubmitWork.selectAllPeople = 1;
                    selectPeopleView.setOff(true);
                } else {
                    requestSubmitWork.selectAllPeople = 0;
                    selectPeopleView.setOff(false);
                }
            }
        });
        warnningDialog = new WarnningDialog(mContext, "内容还没有提交，是否继续退出？");
        warnningDialog.setOnPositiveListener(new WarnningDialog.OnDialogListener() {
            @Override
            public void onPositive() {
                finish();
            }

            @Override
            public void onNegative() {

            }
        });
    }

    private void submitPic() {
        RequestUploadPic requestUploadPic = new RequestUploadPic();
        requestUploadPic.type = RequestUploadPic.UploadPicType.ACTION;
        List<File> fileList = new ArrayList<>();
        for (int i = 0; i < picList.size(); i++) {
            File file = new File(picList.get(i));
            fileList.add(file);
        }
        requestUploadPic.file = fileList;
        AppModelFactory.model().uploadPic(requestUploadPic, new ProgressSubscriber<DataBean<List<String>>>(mContext) {
            @Override
            public void onNext(DataBean<List<String>> o) {
                if (null != o.data) {
                    submitNormal(o.data);
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
                showToast(mContext.getString(R.string.error_no_network));
            }
        });

    }

    private void submitNormal(List<String> pictures) {
        requestSubmitWork.content = etContent.getText().toString();
        if (null != pictures) {
            requestSubmitWork.picture = pictures;
        }

        if(checkBox.isChecked()){

        }else{
            //选择的审批人
            List<RequestSubmitWork.ApprovePeopleBean> personList = new ArrayList<>();
            for (int i = 0; i < intentList.size(); i++) {
                RequestSubmitWork.ApprovePeopleBean a = new RequestSubmitWork.ApprovePeopleBean();
                a.approveStaffId = intentList.get(i).staff_id;
                personList.add(a);
            }
            requestSubmitWork.approvePeopleBeanList = personList;
        }

        requestSubmitWork.staffId = LoginInfoPrefs.getInstance(mContext).getStaffId();
        requestSubmitWork.baseonType = UserfoPrefs.getInstance(mContext).getBaseonType();
        requestSubmitWork.baseonId = UserfoPrefs.getInstance(mContext).getBaseonId();
        AppModelFactory.model().submitWork(requestSubmitWork, new ProgressSubscriber<Object>(mContext) {
            @Override
            public void onNext(Object o) {
                showToast("已提交");
                EvenManager.sendEvent(new NotifyEvent(NotifyEvent.WORK));
                //记住审批人
                Gson gson = new Gson();
                String json = gson.toJson(intentList);
                ApprovePrefs.getInstance(mContext).saveInfo(json);
                finish();
            }

            @Override
            public void onNormalError(ApiException ex) {
                super.onNormalError(ex);
                showToast("提交失败,请重试");
            }

            @Override
            public void onNoNetWorkError() {
                super.onNoNetWorkError();
                showToast(mContext.getString(R.string.error_no_network));
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNotifyEvent(NotifyEvent notifyEvent) {
        if (notifyEvent.tag == NotifyEvent.DATA) {
            intentList = (List<ResultListStaff>) notifyEvent.value;
            dataList.clear();
            for (int i = 0; i < intentList.size(); i++) {
                SelectBean selectBean = new SelectBean();
                ResultListStaff bean = intentList.get(i);
                if (!TextUtils.isEmpty(intentList.get(i).avatar)) {
                    if (bean.avatar.startsWith(getString(R.string.HTTP))) {
                        selectBean.pic_url = intentList.get(i).avatar;
                    } else {
                        selectBean.pic_url = getString(R.string.image_url_prefix) + intentList.get(i).avatar;
                    }
                }
                if (null != bean.sex) {
                    selectBean.type = bean.sex.value;
                }
                selectBean.name = bean.staff_name;
                dataList.add(selectBean);
            }
            selectPeopleView.notifyDataSetChanged(dataList);
        }
    }

    private boolean dataCheck() {
        if (TextUtils.isEmpty(etContent.getText().toString())) {
            showToast("请输入工作汇报");
            return false;
        }

        if(checkBox.isChecked()){
            //选中了
            return true;
        }else{
            if(intentList.size() == 0){
                showToast("请选择审批人");
                return false;
            }
        }

        return true;
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
            if (picList.size() < 9) {
                picList.add(images.get(i).getCompressPath());
            }
        }
        selectPicView.notifyDataSetChanged(picList);
    }

    @Override
    public void onBackPressed() {
        if(!TextUtils.isEmpty(etContent.getText().toString())){
            if(null != warnningDialog){
                warnningDialog.show();
            }
        }else{
            if(null != warnningDialog && warnningDialog.isShowing()){
                warnningDialog.dismiss();
            }
            super.onBackPressed();
        }
    }
}
