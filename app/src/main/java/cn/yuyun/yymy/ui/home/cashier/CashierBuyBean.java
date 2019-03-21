package cn.yuyun.yymy.ui.home.cashier;

import java.io.Serializable;
import java.util.List;

import cn.yuyun.yymy.http.result.ResultListStaff;

/**
 * @author
 * @desc
 * @date
 */
public class CashierBuyBean implements Serializable{

    public String good_id;
    public int asset_type;
    public String name;
    public double guideprice;
    public String thumb_url;
    /**提成员工*/
    public List<ResultListStaff> staffList;
    /**实付*/
    public double amount_realpay;
    /**成交价格*/
    public double transaction_price;
    /**储值支付*/
    public double storedvalue_pay;
    /**欠款*/
    public double arrear_pay;
    /**总次数*/
    public int total_num = 1;

    public double achieve_percent;
    public int achieve_statistics_type;

}
