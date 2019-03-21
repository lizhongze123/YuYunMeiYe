package cn.yuyun.yymy.ui.home.work;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.http.ApiException;
import com.example.http.DataBean;
import com.example.http.PageInfo;
import com.example.http.ProgressSubscriber;
import com.jph.takephoto.model.TResult;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.example.takephoto.CustomHelper;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.constan.Constans;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestDelStaffPicWall;
import cn.yuyun.yymy.http.request.RequestStaffPicWall;
import cn.yuyun.yymy.http.request.RequestUploadPic;
import cn.yuyun.yymy.http.result.ResultStaffPicWall;
import cn.yuyun.yymy.http.result.StaffBean;
import cn.yuyun.yymy.sp.LoginInfoPrefs;
import cn.yuyun.yymy.ui.home.analysis.StoreBean;
import cn.yuyun.yymy.ui.other.ViewBigImageActivity;
import cn.yuyun.yymy.view.EmptyLayout;
import cn.yuyun.yymy.view.WarnningDialog;
import cn.yuyun.yymy.view.selectpic.SelectPicAdapter;
import cn.yuyun.yymy.view.selectpic.SelectPicView;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;
import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA2;

/**
 * @author
 * @desc
 * @date
 */
public class StaffPicWallActivity extends BaseActivity {

    private StaffBean staffBean;
    private StoreBean storeBean;
    @BindView(R.id.recyclerView)
    SelectPicView selectPicView;
    private List<ResultStaffPicWall> resultList = new ArrayList<>();
    private List<String> list = new ArrayList<>();
    private CustomHelper customHelper2;
    private int tempPos;
    private WarnningDialog warnningDialog;

    public static Intent startStaffPicWallActivity(Context context, StaffBean staffBean, StoreBean storeBean) {
        return new Intent(context, StaffPicWallActivity.class).putExtra(EXTRA_DATA, staffBean).putExtra(EXTRA_DATA2, storeBean);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_pic_wall);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initView();
        initData();
    }

    private void initView() {
        titleBar.setTilte("员工生活照");
        staffBean = (StaffBean) getIntent().getSerializableExtra(EXTRA_DATA);
        storeBean = (StoreBean) getIntent().getSerializableExtra(EXTRA_DATA2);
        selectPicView.setMax(Integer.SIZE);
        selectPicView.setAdapter(new SelectPicAdapter.OnClickCallBack() {
            @Override
            public void onAdd() {
                if(Constans.isStoreLoginer){
                    customHelper2.onClick(getTakePhoto());
                }else{
                    showToast(getString(R.string.PARTNER));
                }
            }

            @Override
            public void onDel(int pos) {
                if(Constans.isStoreLoginer){
                    tempPos = pos;
                    warnningDialog.show();
                }else{

                }
            }

            @Override
            public void onBrowser(int pos) {
                Bundle bundle = new Bundle();
                bundle.putInt("code", pos);
                bundle.putStringArrayList("imageList", (ArrayList<String>) list);
                startActivity(ViewBigImageActivity.startViewBigImageActivity(mContext, bundle));
            }
        });
        if(null == staffBean){
            selectPicView.editable(true);
            customHelper2 = new CustomHelper(this).setCrop(false);
        }else{
            selectPicView.editable(false);
        }
        warnningDialog = new WarnningDialog(mContext, "确定删除这张照片？");
        warnningDialog.setOnPositiveListener(new WarnningDialog.OnDialogListener() {
            @Override
            public void onPositive() {
                delPicWall(resultList.get(tempPos).employeeLifePhotoId);
                warnningDialog.dismiss();
            }

            @Override
            public void onNegative() {

            }
        });
    }

    private void getPicWall() {
        String staffId = "";
        if(null == staffBean){
            staffId = LoginInfoPrefs.getInstance(this).getStaffId();
        }else{
            staffId = staffBean.staffId;
        }
        AppModelFactory.model().getStaffPicWall(storeBean.group_id, staffId,1, Integer.MAX_VALUE, new ProgressSubscriber<DataBean<PageInfo<ResultStaffPicWall>>>(mContext) {

            @Override
            public void onNext(DataBean<PageInfo<ResultStaffPicWall>> result) {
                if (null != result) {
                    if(null != result.data){
                        resultList = result.data.dataLsit;
                        list.clear();
                        for (int i = 0; i < result.data.dataLsit.size(); i++) {
                            if(null != result.data.dataLsit.get(i).thumbUrls){
                                list.add(mContext.getString(R.string.image_url_prefix) + result.data.dataLsit.get(i).thumbUrls.get(0));
                            }
                        }
                        selectPicView.notifyDataSetChanged(list);
                        if(null != staffBean){
                            if(result.data.dataLsit.size() == 0){
                                selectPicView.setVisibility(View.GONE);
                                showEmpty(EmptyLayout.NODATA_ENABLE_CLICK);
                            }
                        }
                    }
                }
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
            }
        });
    }

    private void delPicWall(int id) {
        RequestDelStaffPicWall body = new RequestDelStaffPicWall();
        List<Integer> list = new ArrayList<>();
        list.add(id);
        body.idList = list;
        AppModelFactory.model().delStaffPicWall(body, new ProgressSubscriber<Object>(mContext) {

            @Override
            public void onNext(Object result) {
                showToast("删除成功");
                getPicWall();
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
            }
        });
    }

    private void uploadPic(RequestUploadPic body){
        AppModelFactory.model().uploadPic(body, new ProgressSubscriber<DataBean<List<String>>>(mContext) {
            @Override
            public void onNext(DataBean<List<String>> o) {
                if(null != o.data){
                    submit(o.data);
                }
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
                showToast(ex.getMessage());
            }
        });
    }

    private void submit(List<String> pictures){
        RequestStaffPicWall body = new RequestStaffPicWall();
        if(null == staffBean){
            body.staffId  = LoginInfoPrefs.getInstance(this).getStaffId();
        }else{
            body.staffId  = staffBean.staffId;
        }
        body.pictures = pictures;
        AppModelFactory.model().uploadStaffPicWall(body, new ProgressSubscriber<Object>(mContext) {

            @Override
            public void onNext(Object result) {
                showToast("上传成功");
                getPicWall();
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
            }
        });
    }

    private void initData() {
        getPicWall();
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
        RequestUploadPic requestUploadPic = new RequestUploadPic();
        requestUploadPic.type = RequestUploadPic.UploadPicType.STAFF;
        List<File>fileList = new ArrayList<>();
        File newFile = new File(result.getImages().get(0).getCompressPath());
        fileList.add(newFile);
        requestUploadPic.file = fileList;
        uploadPic(requestUploadPic);
    }

}

