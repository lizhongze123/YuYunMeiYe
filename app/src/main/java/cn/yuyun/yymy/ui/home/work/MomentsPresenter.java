package cn.yuyun.yymy.ui.home.work;

import android.content.Intent;

import java.util.List;

/**
 * @author lzz
 * @desc lzz
 * @date 2018/2/26
 */

public class MomentsPresenter implements MomentsContract.Presenter{

    @Override
    public List<Object> getData() {
        return null;
    }

    @Override
    public String getBackgroudMoment() {
        return null;
    }

    @Override
    public void getCacheTime() {

    }

    @Override
    public void loadeData(int pageIndex) {

    }

    @Override
    public void setGood(int position, String aid) {

    }

    @Override
    public void comment(int position, String aid, String content) {

    }

    @Override
    public void cancelGood(int position, String gid) {

    }

    @Override
    public void deleteComment(int position, String cid) {

    }

    @Override
    public void onBarRightViewClicked() {

    }

    @Override
    public void deleteItem(int position, String aid) {

    }

    @Override
    public void startToPhoto(int type) {

    }

    @Override
    public void startToAlbum(int type) {

    }

    @Override
    public void onResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public String getCurrentTime() {
        return null;
    }
}
