package cn.yuyun.yymy.http.result;

import java.io.Serializable;
import java.util.List;

/**
 * @author
 * @desc
 * @date
 */
public class ResultCanbeUsedAssest implements Serializable{

    public String member_id;
    public List<ResultCanbeUsedAssestBean> canbe_used_assets;
    public String canbe_used_amount;

    public static class ResultCanbeUsedAssestBean implements Serializable {


        public String member_id;
        public int asset_type;
        public int expired;
        public String dead_line;
        public String asset_id;
        public double amount_still_here;
        public int num_total;
        public int num_canbe_used;
        public String asset_name;
        public int transaction_type;
        public double transaction_price;
        public double handmake_default_buy;
        public double handmake_default_giveaway;
        public String transaction_time;
        public int status;
        public String cache_time;
        public GoodRspBean goodRsp;
        public Object packageItemsInfoRspList;
        public AssetsPaymentInfoRspBean assetsPaymentInfoRsp;

        public static class AssetsPaymentInfoRspBean implements Serializable{
            public double transaction_price;
            public double amount_realpay;
            public double storedvalue_pay;
            public double free_pay;
            public double accumulate_sv_pay;
            public double coupon_pay;
        }

        public static class GoodRspBean implements Serializable{

            public int group_id;
            public int good_id;
            public String name;
            public String item_number;
            public String specifications;
            public String unit;
            public int good_type;
            public int package_type;
            public Object times;
            public Object brand_id;
            public String brand_name;
            public Object type_id;
            public String type_name;
            public double guideprice;
            public double purchaseprice;
            public double innerstaffprice;
            public double innershopprice;
            public double handmake_default_buy;
            public double handmake_default_giveaway;
            public int achieve_statistics_type;
            public double achieve_percent;
            public double achieve_amount;
            /**1.百分比  2.固定值*/
            public int consume_statistics_type;
            public double consume_percent;
            public double consume_amount;
            public double discount;
            public Object expire;
            public double stock_safe_min;
            public double stock_safe_max;
            public String thumb_url;
            public int status;
            public String description;
            public double sold_out_numbers;
            public double wechat_appointment;
            public Object stock_numbers_in_sp;
            public List<?> goodContainsListRspList;
            public List<Integer> good_available_sp_list;
        }
    }

}
