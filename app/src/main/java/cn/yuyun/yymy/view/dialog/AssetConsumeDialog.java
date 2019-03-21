package cn.yuyun.yymy.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import java.util.List;

import cn.yuyun.yymy.R;
import cn.yuyun.yymy.http.result.ResultAssetDetail;
import cn.yuyun.yymy.ui.home.member.asset.AssetConsumeDialogAdapter;


public class AssetConsumeDialog {

    private Dialog mDialog;
    private RecyclerView recyclerView;
    private AssetConsumeDialogAdapter adapter;
    private List<ResultAssetDetail.MemberAssetsInfoBean.PackageItemsInfoRspListBean> dataList;

    public AssetConsumeDialog(Context context) {

        mDialog = new Dialog(context, R.style.tip_dialog);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_asset_consume, null);
        adapter = new AssetConsumeDialogAdapter(context);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
        mDialog.setCancelable(true);
        mDialog.setContentView(view);
    }

    public void setData(List<ResultAssetDetail.MemberAssetsInfoBean.PackageItemsInfoRspListBean> dataList) {
        this.dataList = dataList;
        adapter.addAll(dataList);
    }

    public List<ResultAssetDetail.MemberAssetsInfoBean.PackageItemsInfoRspListBean> getData(){
        return dataList;
    }

    public void show() {
        mDialog.show();
    }

    public void cancel() {
        mDialog.cancel();
    }

    public boolean isShowing() {
        return mDialog.isShowing();
    }


}

