package cn.yuyun.yymy.ui.home.member.memberdata;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.http.ApiException;
import com.example.http.DataBean;
import com.example.http.PageInfo;
import com.example.http.ProgressSubscriber;
import com.jph.takephoto.model.TResult;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.example.takephoto.CustomHelper;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseNoTitleFragment;
import cn.yuyun.yymy.constan.Constans;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestDelMemberPicWall;
import cn.yuyun.yymy.http.request.RequestMemberAddPicWall;
import cn.yuyun.yymy.http.request.RequestUploadMemberPic;
import cn.yuyun.yymy.http.request.RequestUploadPic;
import cn.yuyun.yymy.http.result.ResultMemberBean;
import cn.yuyun.yymy.http.result.ResultPicWall;
import cn.yuyun.yymy.sp.UserfoPrefs;
import cn.yuyun.yymy.ui.other.ViewBigImageActivity;
import cn.yuyun.yymy.ui.store.member.MemberPicWallAdapter;
import cn.yuyun.yymy.view.selectpic.SelectPicAdapter;
import cn.yuyun.yymy.view.selectpic.SelectPicView;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;
import static cn.yuyun.yymy.constan.Constans.EXTRA_TYPE;

/**
 * @author 详细资料-会员照片墙Fragment
 * @desc
 * @date 2018/1/15
 */
public class MemberDataThreeFragment extends BaseNoTitleFragment {

    private ResultMemberBean memberBean;
    private String type;
    private RecyclerView recyclerView;
    private List<ResultPicWall> resultList = new ArrayList<>();
    private List<String> list = new ArrayList<>();
    private CustomHelper customHelper2;
    private MemberPicWallAdapter mAdapter;

    public static MemberDataThreeFragment newInstance(ResultMemberBean memberBean, String type) {
        MemberDataThreeFragment fragment = new MemberDataThreeFragment();
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_DATA, memberBean);
        args.putSerializable(EXTRA_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initBundle(Bundle bundle) {
        super.initBundle(bundle);
        if (getArguments() != null) {
            memberBean = (ResultMemberBean) getArguments().getSerializable(EXTRA_DATA);
            type = getArguments().getString(EXTRA_TYPE);
        }
    }

    @Override
    protected void onBindViewBefore(View root) {
        super.onBindViewBefore(root);
        mContext = getContext();
    }

    @Override
    protected int getTheLayoutId() {
        return R.layout.fragment_wall;
    }

    @Override
    protected void initViews(View root) {
        super.initViews(root);
        recyclerView = (RecyclerView) root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        mAdapter = new MemberPicWallAdapter(mContext, type);
        mAdapter.setOnItemClickListener(new MemberPicWallAdapter.OnMyItemClickListener() {
            @Override
            public void onDel(int pos) {
                if(Constans.isStoreLoginer){
                    delPicWall(resultList.get(pos).memberLifePhotoId);
                }else{
                    showToast(getString(R.string.PARTNER));
                }
            }
        });
        recyclerView.setAdapter(mAdapter);
        customHelper2 = new CustomHelper(getActivity()).setCrop(false);
    }


    public void add(){
        customHelper2.onClick(getTakePhoto());
    }

    private void getPicWall() {
        AppModelFactory.model().getPicWall(memberBean.memberId, new ProgressSubscriber<DataBean<PageInfo<ResultPicWall>>>(mContext) {

            @Override
            public void onNext(DataBean<PageInfo<ResultPicWall>> result) {
                if (null != result) {
                    if(null != result.data){
                        resultList = result.data.dataLsit;
                        list.clear();
                        for (int i = 0; i < result.data.dataLsit.size(); i++) {
                            list.add(mContext.getString(R.string.image_url_prefix) + result.data.dataLsit.get(i).thumbUrls.get(0));
                        }
                        mAdapter.notifyDataSetChanged(list);
                    }
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

            @Override
            public void onCompleted() {
                super.onCompleted();
            }
        });
    }

    private void delPicWall(int id) {
        List<Integer> idList = new ArrayList();
        idList.add(id);
        RequestDelMemberPicWall body = new RequestDelMemberPicWall();
        body.idList = idList;
        AppModelFactory.model().delPicWall(body, new ProgressSubscriber<Object>(mContext) {

            @Override
            public void onNext(Object result) {
                showToast("删除成功");
                getPicWall();
            }
            @Override
            public void onNormalError(ApiException ex) {
                super.onNormalError(ex);
                showToast("操作失败，请稍后重试");
            }

            @Override
            public void onNoNetWorkError() {
                super.onNoNetWorkError();
                showToast(mContext.getString(R.string.error_no_network));
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
            public void onNormalError(ApiException ex) {
                super.onNormalError(ex);
                showToast("操作失败，请稍后重试");
            }

            @Override
            public void onNoNetWorkError() {
                super.onNoNetWorkError();
                showToast(mContext.getString(R.string.error_no_network));
            }
        });

    }


    private void submit(List<String> pictures){
        RequestMemberAddPicWall body = new RequestMemberAddPicWall();
        body.memberId = memberBean.memberId;
        body.pictures = pictures;
        AppModelFactory.model().addMemberPicWall(body, new ProgressSubscriber<Object>(mContext) {

            @Override
            public void onNext(Object result) {
                showToast("上传成功");
                getPicWall();
            }

            @Override
            public void onNormalError(ApiException ex) {
                super.onNormalError(ex);
                showToast("操作失败，请稍后重试");
            }

            @Override
            public void onNoNetWorkError() {
                super.onNoNetWorkError();
                showToast(mContext.getString(R.string.error_no_network));
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
            }
        });
    }


    @Override
    protected void initData() {
        if (null != memberBean) {
            getPicWall();
        }
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
        requestUploadPic.type = RequestUploadPic.UploadPicType.MEMBER;
        List<File>fileList = new ArrayList<>();
        File newFile = new File(result.getImages().get(0).getCompressPath());
        fileList.add(newFile);
        requestUploadPic.file = fileList;
        uploadPic(requestUploadPic);
    }

}

