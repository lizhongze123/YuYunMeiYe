package cn.yuyun.yymy.ui.home.analysis;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @author lzz
 * @desc lzz
 * @date 2018/1/25
 */

public class StoreBean implements Serializable{

    /**门店id*/
    @SerializedName("sp_id")
    public String spId;
    /**门店所属集团id*/
    public String group_id;
    /**门店名称id*/
    @SerializedName("sp_name")
    public String spName;
    /**负责人姓名id*/
    public String chairman;
    /**负责人联系方式id*/
    public String chairmantel;
    /**店长*/
    public String shopowner;
    /**店长联系方式*/
    public String shopowner_tel;
    /**门店地址*/
    public String addr;
    /**省*/
    public String privince;
    /**市*/
    public String city;
    /**区*/
    public String district;
    /**门店电话*/
    public String tel;
    /**门店经度*/
    public String lng;
    /**门店纬度*/
    public String lat;
    /**门店缩略图*/
    public String thumb_url;
    /**门店状态:0撤店、1正常、2停业整顿、3禁用、4删除*/
    public int status;
    /**门店类型 1直营门店 2连锁店*/
    public int type;
    /**备注*/
    public String description;
    /**创建时间*/
    public String create_time;
    /**创建人*/
    public String create_person;
    /**修改时间*/
    public String modify_time;
    /**修改人*/
    public String modify_person;

    @SerializedName("og_type")
    public int ogType = 2;
}
