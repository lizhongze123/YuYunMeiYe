package cn.yuyun.yymy.ui.home.member;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import cn.yuyun.yymy.ui.me.entity.MemberBean;

/**
 * @author lzz
 * @desc lzz
 * @date 2018/1/20
 */

public class Level0Item extends AbstractExpandableItem<MemberBean> implements MultiItemEntity {

    public String title;

    public Level0Item(String title){
        this.title = title;
    }

    @Override
    public int getItemType() {
        return ExpandableItemAdapter.TYPE_LEVEL_0;
    }

    @Override
    public int getLevel() {
        return 0;
    }

}
