package cn.yuyun.yymy.http.result;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @author
 * @desc
 * @date
 */
public class ResultStock implements Serializable{


    /**
     * id : null
     * sh_id : 20
     * sh_name : 开发专用
     * group_id : null
     * product_id : 5114
     * product_name : 梁峰
     * product_type : 管理梁峰1
     * product_brand : 小票机梁峰
     * stock_number : 1
     * stock_amount : 300
     * guideprice : 300
     * purchaseprice : 300
     */

    public int id;

    @SerializedName("sh_id")
    public int shId;

    /**仓库*/
    @SerializedName("sh_name")
    public String shName;

    public String group_id;

    @SerializedName("product_id")
    public int productId;

    /**商品名称*/
    @SerializedName("product_name")
    public String productName;

    /**商品类型*/
    @SerializedName("product_type")
    public String productType;

    /**商品品牌*/
    @SerializedName("product_brand")
    public String productBrand;

    /**库存数量*/
    @SerializedName("stock_number")
    public int stockNumber;

    @SerializedName("stock_amount")
    public double stockAmount;

    @SerializedName("guideprice")
    public double guidePrice;


    @SerializedName("purchaseprice")
    public double purchasePrice;

    @SerializedName("sp_money")
    public double spMoney;
}
