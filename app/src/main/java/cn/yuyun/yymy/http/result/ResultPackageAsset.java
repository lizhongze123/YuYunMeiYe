package cn.yuyun.yymy.http.result;

import java.io.Serializable;
import java.util.List;

import cn.yuyun.yymy.bean.DeadLineStatus;

/**
 * @author
 * @desc
 * @date
 */

public class ResultPackageAsset implements Serializable{

    public int id;
    public String group_id;
    public String asset_id;
    public int asset_type;
    public String member_id;
    public String member_name;
    public String member_card;
    public String member_description;
    public int member_in_sp_id;
    public String member_in_sp_name;
    public int package_id;
    public int package_type;
    public String package_name;
    public String thumb_url;
    public double transaction_type;
    public int total_num;
    public double transaction_price;
    public double amount_already_paid;
    public double amount_already_refund;
    public double amount_already_used;
    public double amount_still_here;
    public double amount_canbe_refund;
    public double amount_arrear;
    public double num_consume;
    public double num_refund;
    public double num_frozen;
    public int num_canbe_used;
    public double num_canbe_refund;
    public String create_time;
    public String consume_time;
    public String create_person;
    public String create_person_name;
    public String modify_time;
    public String modify_person;
    public int status;
    public double display_to_customer;
    public String description;
    public String dead_line;
    public DeadLineStatus expired;
    public List<PackageItemsInfoRspListBean> packageItemsInfoRspList;

    public static class PackageItemsInfoRspListBean implements Serializable{
        /**
         * package_type : 1
         * package_asset_id : K101812041629470625
         * good_id : 66534
         * sub_good_id : 64632
         * total_good_numbers : 68
         * goodRsp : {"group_id":17,"good_id":64632,"name":"面部拨经勿删","item_number":"","specifications":"","unit":"","good_type":2,"package_type":1,"times":null,"brand_id":3210,"brand_name":"妙语测试","type_id":3384,"type_name":"妙语测试","guideprice":300,"purchaseprice":0,"innerstaffprice":0,"innershopprice":0,"handmake_default_buy":0,"handmake_default_giveaway":0,"achieve_statistics_type":1,"achieve_percent":1,"achieve_amount":0,"consume_statistics_type":1,"consume_percent":1,"consume_amount":0,"discount":0.8,"expire":null,"stock_safe_min":0,"stock_safe_max":0,"thumb_url":"https://yuyunrj.oss-cn-shenzhen.aliyuncs.com/system_resource/thumb/good/service/8.jpg","status":1,"description":"","sold_out_numbers":115,"wechat_appointment":0,"goodContainsListRspList":[],"good_available_sp_list":[921,914,906,905,904,903,902,901,900,899,894,881,864,863,854,845,844,839,838,837,824,806,780,730,729,727,725,662,660,656,648,643,631,626,624,623,619,616,597,595,594,593,591,573,571,566,565,533,530,526,525,523,518,516,512,510,509,508,505,503,501,500,499,498,497,496,495,494,493,492,491,490,489,487,484,483,482,481,480,478,477,476,475,472,471,466,465,464,454,453,449,446,444,443,442,439,437,436,435,434,433,432,430,428,427,426,422,421,420,419,418,415,413,411,409,404,400,396,395,394,392,390,389,388,379,375,374,362,345,342,341,243,241,239,237,225,209,206,199,197,195,194,193,190,189,188,179,174,173,162,156,155,153,152,150,149,148,139,132,116,104,102,100,97,96,69,70,106,108,110]}
         * canbe_used_good_numbers : 68
         */

        public int package_type;
        public String package_asset_id;
        public int good_id;
        public int sub_good_id;
        public double total_good_numbers;
        public GoodRspBean goodRsp;
        public double canbe_used_good_numbers;

        public static class GoodRspBean implements Serializable{
            /**
             * group_id : 17
             * good_id : 64632
             * name : 面部拨经勿删
             * item_number :
             * specifications :
             * unit :
             * good_type : 2
             * package_type : 1
             * times : null
             * brand_id : 3210
             * brand_name : 妙语测试
             * type_id : 3384
             * type_name : 妙语测试
             * guideprice : 300
             * purchaseprice : 0
             * innerstaffprice : 0
             * innershopprice : 0
             * handmake_default_buy : 0
             * handmake_default_giveaway : 0
             * achieve_statistics_type : 1
             * achieve_percent : 1
             * achieve_amount : 0
             * consume_statistics_type : 1
             * consume_percent : 1
             * consume_amount : 0
             * discount : 0.8
             * expire : null
             * stock_safe_min : 0
             * stock_safe_max : 0
             * thumb_url : https://yuyunrj.oss-cn-shenzhen.aliyuncs.com/system_resource/thumb/good/service/8.jpg
             * status : 1
             * description :
             * sold_out_numbers : 115
             * wechat_appointment : 0
             * goodContainsListRspList : []
             * good_available_sp_list : [921,914,906,905,904,903,902,901,900,899,894,881,864,863,854,845,844,839,838,837,824,806,780,730,729,727,725,662,660,656,648,643,631,626,624,623,619,616,597,595,594,593,591,573,571,566,565,533,530,526,525,523,518,516,512,510,509,508,505,503,501,500,499,498,497,496,495,494,493,492,491,490,489,487,484,483,482,481,480,478,477,476,475,472,471,466,465,464,454,453,449,446,444,443,442,439,437,436,435,434,433,432,430,428,427,426,422,421,420,419,418,415,413,411,409,404,400,396,395,394,392,390,389,388,379,375,374,362,345,342,341,243,241,239,237,225,209,206,199,197,195,194,193,190,189,188,179,174,173,162,156,155,153,152,150,149,148,139,132,116,104,102,100,97,96,69,70,106,108,110]
             */

            public int group_id;
            public int good_id;
            public String name;
            public String item_number;
            public String specifications;
            public String unit;
            public int good_type;
            public int package_type;
            public Object times;
            public int brand_id;
            public String brand_name;
            public int type_id;
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
            public double consume_statistics_type;
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
            public List<?> goodContainsListRspList;
            public List<Integer> good_available_sp_list;
        }
    }

}
