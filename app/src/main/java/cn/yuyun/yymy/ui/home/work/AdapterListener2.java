package cn.yuyun.yymy.ui.home.work;

import cn.yuyun.yymy.http.result.ResultWork;


public interface AdapterListener2 {

    void onUnread();

    void onUserClicked();

    void onFavorites(ResultWork bean);

    void onLike(ResultWork bean, int parentPos, int childPos);

    void onComment(ResultWork bean, int parentPos, int childPos);

    void onItemClicked(ResultWork bean, int parentPos, int childPos);
}
