package cn.yuyun.yymy.ui.home.work;

import android.content.Intent;

import java.util.List;

public interface MomentsContract {

    interface View extends BaseView<Presenter>{
        void showInputMenu(int position, String aid);
        void hideInputMenu();
        void showPicDialog(int type, String string);
        void onRefreshComplete();
        void refreshListView(String time);
        void updateCommentView(int position);
        void updateGoodView(int position);
        void showBackground(String url);
    }

    interface Presenter {
            /**获取数据*/
            List<Object> getData();
            String getBackgroudMoment();
            void getCacheTime();
            void loadeData(int pageIndex);
            void setGood(int position, String aid);
            void comment(int position, String aid, String content);
            void cancelGood(int position, String gid);
            void deleteComment(int position, String cid);
            void onBarRightViewClicked();
            void deleteItem(int position, String aid);
            void startToPhoto(int type);
            void startToAlbum(int type);
            void onResult(int requestCode, int resultCode, Intent data);
            String getCurrentTime();
    }

}
