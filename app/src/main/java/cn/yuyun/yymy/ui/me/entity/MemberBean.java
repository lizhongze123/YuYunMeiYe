package cn.yuyun.yymy.ui.me.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import cn.yuyun.yymy.bean.Sex;
import cn.yuyun.yymy.ui.home.member.ExpandableItemAdapter;

/**
 * @desc
 *
 * @author lzz
 * @date 2018/1/17
 */

public class MemberBean implements Serializable,MultiItemEntity {

    public int id;
    public String memberId;
    /**会员最近一次消费时间*/
    @SerializedName("consumption_latest_time")
    public String lastTime;
    /**集团id*/
    @SerializedName("group_id")
    public int groupId;
    /**门店Id*/
    @SerializedName("sp_id")
    public int storeId;
    /**门店名称*/
    @SerializedName("sp_name")
    public String storeName;
    /**会员级别id*/
    @SerializedName("level_id")
    public int levelId;
    /**会员级别名称*/
    @SerializedName("level_name")
    public String levelName;
    /**会员卡号*/
    @SerializedName("member_card")
    public String vipCardNum;
    /**会员手机号*/
    @SerializedName("mobile")
    public String mobile;
    /**会员姓名*/
    @SerializedName("name")
    public String memberName;
    /**会员头像*/
    @SerializedName("avatar")
    public String avatar;
    /**会员性别*/
    @SerializedName("sex")
    public Sex sex;
    /**是否跨店*/
    @SerializedName("cross_sp")
    public int crossSp;
    /**开户时间*/
    @SerializedName("cash_time")
    public String cashTime;

    public MemberBean(){}

    @Override
    public int getItemType() {
        return ExpandableItemAdapter.TYPE_PERSON;
    }
}
