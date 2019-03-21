package cn.yuyun.yymy.http.result;

import java.io.Serializable;

/**
 * @author
 * @desc
 * @date
 */
public class ResultReportStoreSale implements Serializable{


    /**
     * sh_name : 韶关分组
     * product_id : 28272
     * product_name : 彩华产品
     * gain_sh_name : 韶关分组
     * date : 2018-10-08 14:32:09
     * type : 减库存
     * buy_money : 39
     * staff_money : 0
     * store_money : 0
     * guideprice : 298
     * real_selling_price : 29800
     * stock_number : 10
     * store_total_amount : 0
     * buy_total_amount : 390
     * create_person_name : 彭丽平
     * operate_person_name :
     * order_id : SR1810814329
     * description : 单据:CANB_499018100814320901365中第1个减库存的商品!
     */

    private String sh_name;
    private int product_id;
    private String product_name;
    private String gain_sh_name;
    private String date;
    private String type;
    private double buy_money;
    private double staff_money;
    private double store_money;
    private double guideprice;
    private double real_selling_price;
    private int stock_number;
    private double store_total_amount;
    private double buy_total_amount;
    private String create_person_name;
    private String operate_person_name;
    private String order_id;
    private String description;

    public String getSh_name() {
        return sh_name;
    }

    public void setSh_name(String sh_name) {
        this.sh_name = sh_name;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getGain_sh_name() {
        return gain_sh_name;
    }

    public void setGain_sh_name(String gain_sh_name) {
        this.gain_sh_name = gain_sh_name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getBuy_money() {
        return buy_money;
    }

    public void setBuy_money(double buy_money) {
        this.buy_money = buy_money;
    }

    public double getStaff_money() {
        return staff_money;
    }

    public void setStaff_money(double staff_money) {
        this.staff_money = staff_money;
    }

    public double getStore_money() {
        return store_money;
    }

    public void setStore_money(double store_money) {
        this.store_money = store_money;
    }

    public double getGuideprice() {
        return guideprice;
    }

    public void setGuideprice(double guideprice) {
        this.guideprice = guideprice;
    }

    public double getReal_selling_price() {
        return real_selling_price;
    }

    public void setReal_selling_price(double real_selling_price) {
        this.real_selling_price = real_selling_price;
    }

    public int getStock_number() {
        return stock_number;
    }

    public void setStock_number(int stock_number) {
        this.stock_number = stock_number;
    }

    public double getStore_total_amount() {
        return store_total_amount;
    }

    public void setStore_total_amount(double store_total_amount) {
        this.store_total_amount = store_total_amount;
    }

    public double getBuy_total_amount() {
        return buy_total_amount;
    }

    public void setBuy_total_amount(double buy_total_amount) {
        this.buy_total_amount = buy_total_amount;
    }

    public String getCreate_person_name() {
        return create_person_name;
    }

    public void setCreate_person_name(String create_person_name) {
        this.create_person_name = create_person_name;
    }

    public String getOperate_person_name() {
        return operate_person_name;
    }

    public void setOperate_person_name(String operate_person_name) {
        this.operate_person_name = operate_person_name;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
