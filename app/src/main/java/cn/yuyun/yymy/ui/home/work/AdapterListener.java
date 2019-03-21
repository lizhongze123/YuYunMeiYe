package cn.yuyun.yymy.ui.home.work;

import cn.yuyun.yymy.http.result.ResultWork;


public interface AdapterListener {

    void onUserClicked();

    void onFavorites(ResultWork bean);

    void onLike(ResultWork bean, int position);

    void onComment(ResultWork bean, int position);

    void onItemClicked(ResultWork bean, int position);
}
