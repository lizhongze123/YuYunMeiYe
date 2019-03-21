package cn.yuyun.yymy.http.result;

import java.util.List;

/**
 * @author
 * @desc
 * @date
 */
public class ResultAssetDetail {


    /**
     * member_assets_refund : [{"id":11849,"group_id":17,"member_id":"0c02b03a-f6a7-11e8-aa79-7cd30ae45cee","member_name":"方言","cashier_sp_id":623,"cashier_sp_name":"直播教学库","bill_refund_id":"CRF_6230181203110001066","consume_time":"2018-12-03 11:00:00","related_record_id":"CANB_623018120311000101534","related_asset_id":"S101812031059160908","related_asset_type":2,"related_asset_type_desc":"项目","refund_amount_realpay":0,"refund_amount_to_storedvalue":2900,"refund_times":5,"create_person":"73af82c9-316a-11e8-a35a-00163e0824d9","create_person_name":"检莹","create_time":"2018-12-03 11:00:01","status":1,"description":"","good_id":64635,"goodRsp":null,"good_name":"萍BBB","total_num":10,"canbe_used_num":null,"transaction_price":5800,"dead_line":null,"staffPreSaleServiceRecordRspList":[],"consumptionBillsRefundRspList":null}]
     * member_assets_repayment : []
     * member_assets_consume : [{"id":685322,"group_id":17,"member_id":"0c02b03a-f6a7-11e8-aa79-7cd30ae45cee","member_name":"方言","cashier_sp_id":623,"cashier_sp_name":"直播教学库","bill_consume_id":"CC_62301812031104050000000146","consume_time":"2018-12-03 11:04:05","related_record_id":"CANB_623018120311040508552","related_asset_id":"S101812031059160908","related_asset_type":2,"related_asset_type_desc":"项目","consume_num_now":1,"consume_amount_now":580,"consume_time_now":1,"serve_comment":"","customer_feedback":"","customer_rating":0,"create_person":"73af82c9-316a-11e8-a35a-00163e0824d9","create_person_name":"检莹","create_time":"2018-12-03 11:04:05","status":1,"description":"","good_id":64635,"goodRsp":null,"good_name":"萍BBB","package_type":null,"total_num":10,"canbe_used_num":null,"transaction_price":5800,"transaction_type":null,"dead_line":null,"staffPreSaleServiceRecordRspList":[{"id":488838,"staff_id":"0496c17e-cb5d-11e8-9e22-7cd30ae0f408","staff_in_og_name":"直播教学库","staff_name":"地地","sale_type":3,"commission":0,"achieve_amount":580,"handmake":0,"related_consumption_type":6,"related_consumption_id":"CC_62301812031104050000000146","create_time":"2018-12-03 11:04:05","create_person":"73af82c9-316a-11e8-a35a-00163e0824d9","status":1},{"id":-1,"staff_id":"0496c17e-cb5d-11e8-9e22-7cd30ae0f408","staff_in_og_name":"","staff_name":"","sale_type":1,"commission":-1,"achieve_amount":0,"handmake":0,"related_consumption_type":-1,"related_consumption_id":"","create_time":"","create_person":"","status":-1},{"id":-1,"staff_id":"0496c17e-cb5d-11e8-9e22-7cd30ae0f408","staff_in_og_name":"","staff_name":"","sale_type":2,"commission":-1,"achieve_amount":0,"handmake":0,"related_consumption_type":-1,"related_consumption_id":"","create_time":"","create_person":"","status":-1}],"consumptionBillsConsumeRspList":null,"assetsPaymentInfoRsp":null}]
     * member_assets_giveaway : []
     * member_assets_info : {"id":550097,"group_id":17,"asset_id":"S101812031059160908","member_id":"0c02b03a-f6a7-11e8-aa79-7cd30ae45cee","member_name":"方言","member_description":null,"member_card":"5458","member_in_sp_id":623,"member_in_sp_name":"直播教学库","service_id":64635,"service_name":"萍BBB","thumb_url":"https://yuyunrj.oss-cn-shenzhen.aliyuncs.com/system_resource/thumb/good/service/7.jpg","transaction_type":1,"total_num":10,"transaction_price":5800,"amount_already_paid":5800,"amount_already_refund":2900,"amount_already_used":580,"amount_still_here":2320,"amount_canbe_refund":2320,"amount_arrear":0,"num_consume":1,"num_refund":5,"num_frozen":0,"num_canbe_used":4,"num_canbe_refund":4,"create_time":"2018-12-03 10:59:16","consume_time":"2018-12-03 10:59:15","create_person":"73af82c9-316a-11e8-a35a-00163e0824d9","create_person_name":"检莹","modify_time":"2018-12-03 10:59:16","modify_person":"73af82c9-316a-11e8-a35a-00163e0824d9","status":1,"display_to_customer":1,"description":"","expired":1,"dead_line":""}
     * member_assets_buy : [{"id":490351,"group_id":17,"member_id":"0c02b03a-f6a7-11e8-aa79-7cd30ae45cee","member_name":"方言","cashier_sp_id":623,"cashier_sp_name":"直播教学库","bill_buy_id":"CB_62301812031059160000000769","consume_time":"2018-12-03 10:59:15","related_record_id":"CANB_623018120310591609244","related_asset_id":"S101812031059160908","related_asset_type":2,"related_asset_type_desc":"项目","amount_realpay":0,"storedvalue_pay":5800,"free_pay":0,"accumulate_sv_pay":0,"coupon_pay":0,"arrear":null,"create_person":"73af82c9-316a-11e8-a35a-00163e0824d9","create_person_name":"检莹","create_time":"2018-12-03 10:59:16","status":1,"description":"","good_id":64635,"goodRsp":null,"good_name":"萍BBB","total_num":10,"transaction_price":5800,"dead_line":null,"staffPreSaleServiceRecordRspList":[]}]
     */

    public MemberAssetsInfoBean member_assets_info;
    public List<MemberAssetsRefundBean> member_assets_refund;
    public List<MemberAssetsRepaymentBean> member_assets_repayment;
    public List<MemberAssetsConsumeBean> member_assets_consume;
    public List<MemberAssetsGiveawayBean> member_assets_giveaway;
    public List<MemberAssetsBuyBean> member_assets_buy;

    public static class MemberAssetsInfoBean {
        /**
         * id : 550097
         * group_id : 17
         * asset_id : S101812031059160908
         * member_id : 0c02b03a-f6a7-11e8-aa79-7cd30ae45cee
         * member_name : 方言
         * member_description : null
         * member_card : 5458
         * member_in_sp_id : 623
         * member_in_sp_name : 直播教学库
         * service_id : 64635
         * service_name : 萍BBB
         * thumb_url : https://yuyunrj.oss-cn-shenzhen.aliyuncs.com/system_resource/thumb/good/service/7.jpg
         * transaction_type : 1
         * total_num : 10
         * transaction_price : 5800
         * amount_already_paid : 5800
         * amount_already_refund : 2900
         * amount_already_used : 580
         * amount_still_here : 2320
         * amount_canbe_refund : 2320
         * amount_arrear : 0
         * num_consume : 1
         * num_refund : 5
         * num_frozen : 0
         * num_canbe_used : 4
         * num_canbe_refund : 4
         * create_time : 2018-12-03 10:59:16
         * consume_time : 2018-12-03 10:59:15
         * create_person : 73af82c9-316a-11e8-a35a-00163e0824d9
         * create_person_name : 检莹
         * modify_time : 2018-12-03 10:59:16
         * modify_person : 73af82c9-316a-11e8-a35a-00163e0824d9
         * status : 1
         * display_to_customer : 1
         * description :
         * expired : 1
         * dead_line :
         */

        public int id;
        public int group_id;
        public String asset_id;
        public String member_id;
        public String member_name;
        public String member_description;
        public String member_card;
        public int member_in_sp_id;
        public String member_in_sp_name;
        public int service_id;
        public String service_name;
        public String thumb_url;
        public int transaction_type;
        public double total_num;
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
        public double num_canbe_used;
        public double num_canbe_refund;
        public String create_time;
        public String consume_time;
        public String create_person;
        public String create_person_name;
        public String modify_time;
        public String modify_person;
        public int status;
        public int display_to_customer;
        public String description;
        public int expired;
        public String dead_line;
        public List<PackageItemsInfoRspListBean> packageItemsInfoRspList;

        public int package_type;

        public static class PackageItemsInfoRspListBean {

            /**
             * package_type : 3
             * package_asset_id : K101812041829040230
             * good_id : 67264
             * sub_good_id : 66669
             * total_good_numbers : 2
             * goodRsp : {"group_id":17,"good_id":66669,"name":"袪痘护理1","item_number":"","specifications":"","unit":"","good_type":2,"package_type":1,"times":null,"brand_id":3353,"brand_name":"丽仁测试","type_id":3465,"type_name":"丽仁测试","guideprice":2600,"purchaseprice":0,"innerstaffprice":0,"innershopprice":0,"handmake_default_buy":10,"handmake_default_giveaway":10,"achieve_statistics_type":1,"achieve_percent":1,"achieve_amount":0,"consume_statistics_type":1,"consume_percent":1,"consume_amount":0,"discount":0.5,"expire":null,"stock_safe_min":0,"stock_safe_max":0,"thumb_url":"https://yuyunrj.oss-cn-shenzhen.aliyuncs.com/system_resource/thumb/good/service/4.jpg","status":1,"description":"","sold_out_numbers":57,"wechat_appointment":0,"goodContainsListRspList":[],"good_available_sp_list":[921,914,906,905,904,903,902,901,900,899,894,881,864,863,854,845,844,839,838,837,824,806,780,730,729,727,725,662,660,656,648,643,631,626,624,623,619,616,597,595,594,593,591,573,571,566,565,533,530,526,525,523,518,516,512,510,509,508,505,503,501,500,499,498,497,496,495,494,493,492,491,490,489,487,484,483,482,481,480,478,477,476,475,472,471,466,465,464,454,453,449,446,444,443,442,439,437,436,435,434,433,432,430,428,427,426,422,421,420,419,418,415,413,411,409,404,400,396,395,394,392,390,389,388,379,375,374,362,345,342,341,243,241,239,237,225,209,206,199,197,195,194,193,190,189,188,179,174,173,162,156,155,153,152,150,149,148,139,132,116,104,102,100,97,96,69,70,106,108,110]}
             * canbe_used_good_numbers : 1
             */

            public int package_type;
            public String package_asset_id;
            public int good_id;
            public int sub_good_id;
            public int total_good_numbers;
            public GoodRspBean goodRsp;
            public int canbe_used_good_numbers;

            public static class GoodRspBean {
                /**
                 * group_id : 17
                 * good_id : 66669
                 * name : 袪痘护理1
                 * item_number :
                 * specifications :
                 * unit :
                 * good_type : 2
                 * package_type : 1
                 * times : null
                 * brand_id : 3353
                 * brand_name : 丽仁测试
                 * type_id : 3465
                 * type_name : 丽仁测试
                 * guideprice : 2600
                 * purchaseprice : 0
                 * innerstaffprice : 0
                 * innershopprice : 0
                 * handmake_default_buy : 10
                 * handmake_default_giveaway : 10
                 * achieve_statistics_type : 1
                 * achieve_percent : 1
                 * achieve_amount : 0
                 * consume_statistics_type : 1
                 * consume_percent : 1
                 * consume_amount : 0
                 * discount : 0.5
                 * expire : null
                 * stock_safe_min : 0
                 * stock_safe_max : 0
                 * thumb_url : https://yuyunrj.oss-cn-shenzhen.aliyuncs.com/system_resource/thumb/good/service/4.jpg
                 * status : 1
                 * description :
                 * sold_out_numbers : 57
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
                public double achieve_statistics_type;
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

    public static class MemberAssetsRepaymentBean {

        public String cashier_sp_name;
        public String create_time;
        public String consume_time;
        public String description;
        public double amount_realpay;

    }

    public static class MemberAssetsRefundBean {
        /**
         * id : 11849
         * group_id : 17
         * member_id : 0c02b03a-f6a7-11e8-aa79-7cd30ae45cee
         * member_name : 方言
         * cashier_sp_id : 623
         * cashier_sp_name : 直播教学库
         * bill_refund_id : CRF_6230181203110001066
         * consume_time : 2018-12-03 11:00:00
         * related_record_id : CANB_623018120311000101534
         * related_asset_id : S101812031059160908
         * related_asset_type : 2
         * related_asset_type_desc : 项目
         * refund_amount_realpay : 0
         * refund_amount_to_storedvalue : 2900
         * refund_times : 5
         * create_person : 73af82c9-316a-11e8-a35a-00163e0824d9
         * create_person_name : 检莹
         * create_time : 2018-12-03 11:00:01
         * status : 1
         * description :
         * good_id : 64635
         * goodRsp : null
         * good_name : 萍BBB
         * total_num : 10
         * canbe_used_num : null
         * transaction_price : 5800
         * dead_line : null
         * staffPreSaleServiceRecordRspList : []
         * consumptionBillsRefundRspList : null
         */

        public int id;
        public int group_id;
        public String member_id;
        public String member_name;
        public int cashier_sp_id;
        public String cashier_sp_name;
        public String bill_refund_id;
        public String consume_time;
        public String related_record_id;
        public String related_asset_id;
        public int related_asset_type;
        public String related_asset_type_desc;
        public double refund_amount_realpay;
        public double refund_amount_to_storedvalue;
        public int refund_times;
        public String create_person;
        public String create_person_name;
        public String create_time;
        public int status;
        public String description;
        public int good_id;
        public Object goodRsp;
        public String good_name;
        public int total_num;
        public double canbe_used_num;
        public double transaction_price;
        public Object dead_line;
        public Object consumptionBillsRefundRspList;
        public List<?> staffPreSaleServiceRecordRspList;
    }

    public static class MemberAssetsConsumeBean {
        /**
         * id : 685322
         * group_id : 17
         * member_id : 0c02b03a-f6a7-11e8-aa79-7cd30ae45cee
         * member_name : 方言
         * cashier_sp_id : 623
         * cashier_sp_name : 直播教学库
         * bill_consume_id : CC_62301812031104050000000146
         * consume_time : 2018-12-03 11:04:05
         * related_record_id : CANB_623018120311040508552
         * related_asset_id : S101812031059160908
         * related_asset_type : 2
         * related_asset_type_desc : 项目
         * consume_num_now : 1
         * consume_amount_now : 580
         * consume_time_now : 1
         * serve_comment :
         * customer_feedback :
         * customer_rating : 0
         * create_person : 73af82c9-316a-11e8-a35a-00163e0824d9
         * create_person_name : 检莹
         * create_time : 2018-12-03 11:04:05
         * status : 1
         * description :
         * good_id : 64635
         * goodRsp : null
         * good_name : 萍BBB
         * package_type : null
         * total_num : 10
         * canbe_used_num : null
         * transaction_price : 5800
         * transaction_type : null
         * dead_line : null
         * staffPreSaleServiceRecordRspList : [{"id":488838,"staff_id":"0496c17e-cb5d-11e8-9e22-7cd30ae0f408","staff_in_og_name":"直播教学库","staff_name":"地地","sale_type":3,"commission":0,"achieve_amount":580,"handmake":0,"related_consumption_type":6,"related_consumption_id":"CC_62301812031104050000000146","create_time":"2018-12-03 11:04:05","create_person":"73af82c9-316a-11e8-a35a-00163e0824d9","status":1},{"id":-1,"staff_id":"0496c17e-cb5d-11e8-9e22-7cd30ae0f408","staff_in_og_name":"","staff_name":"","sale_type":1,"commission":-1,"achieve_amount":0,"handmake":0,"related_consumption_type":-1,"related_consumption_id":"","create_time":"","create_person":"","status":-1},{"id":-1,"staff_id":"0496c17e-cb5d-11e8-9e22-7cd30ae0f408","staff_in_og_name":"","staff_name":"","sale_type":2,"commission":-1,"achieve_amount":0,"handmake":0,"related_consumption_type":-1,"related_consumption_id":"","create_time":"","create_person":"","status":-1}]
         * consumptionBillsConsumeRspList : null
         * assetsPaymentInfoRsp : null
         */

        public int id;
        public int group_id;
        public String member_id;
        public String member_name;
        public int cashier_sp_id;
        public String cashier_sp_name;
        public String bill_consume_id;
        public String consume_time;
        public String related_record_id;
        public String related_asset_id;
        public int related_asset_type;
        public String related_asset_type_desc;
        public int consume_num_now;
        public double consume_amount_now;
        public double consume_time_now;
        public String serve_comment;
        public String customer_feedback;
        public int customer_rating;
        public String create_person;
        public String create_person_name;
        public String create_time;
        public int status;
        public String description;
        public int good_id;
        public Object goodRsp;
        public String good_name;
        public Object package_type;
        public double total_num;
        public double canbe_used_num;
        public double transaction_price;
        public Object transaction_type;
        public Object dead_line;
        public List<ConsumptionBillsConsumeRspListBean> consumptionBillsConsumeRspList;
        public Object assetsPaymentInfoRsp;
        public List<StaffPreSaleServiceRecordRspListBean> staffPreSaleServiceRecordRspList;

        public static class ConsumptionBillsConsumeRspListBean {


            public int good_id;
            public int consume_times;
            public GoodRspBean goodRsp;

            public static class GoodRspBean {
                /**
                 * group_id : 17
                 * good_id : 66669
                 * name : 袪痘护理1
                 * item_number :
                 * specifications :
                 * unit :
                 * good_type : 2
                 * package_type : 1
                 * times : null
                 * brand_id : 3353
                 * brand_name : 丽仁测试
                 * type_id : 3465
                 * type_name : 丽仁测试
                 * guideprice : 2600
                 * purchaseprice : 0
                 * innerstaffprice : 0
                 * innershopprice : 0
                 * handmake_default_buy : 10
                 * handmake_default_giveaway : 10
                 * achieve_statistics_type : 1
                 * achieve_percent : 1
                 * achieve_amount : 0
                 * consume_statistics_type : 1
                 * consume_percent : 1
                 * consume_amount : 0
                 * discount : 0.5
                 * expire : null
                 * stock_safe_min : 0
                 * stock_safe_max : 0
                 * thumb_url : https://yuyunrj.oss-cn-shenzhen.aliyuncs.com/system_resource/thumb/good/service/4.jpg
                 * status : 1
                 * description :
                 * sold_out_numbers : 57
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
                public double achieve_statistics_type;
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

        public static class StaffPreSaleServiceRecordRspListBean {
            /**
             * id : 488838
             * staff_id : 0496c17e-cb5d-11e8-9e22-7cd30ae0f408
             * staff_in_og_name : 直播教学库
             * staff_name : 地地
             * sale_type : 3
             * commission : 0
             * achieve_amount : 580
             * handmake : 0
             * related_consumption_type : 6
             * related_consumption_id : CC_62301812031104050000000146
             * create_time : 2018-12-03 11:04:05
             * create_person : 73af82c9-316a-11e8-a35a-00163e0824d9
             * status : 1
             */

            public int id;
            public String staff_id;
            public String staff_in_og_name;
            public String staff_name;
            public int sale_type;
            public double commission;
            public double achieve_amount;
            public double handmake;
            public int related_consumption_type;
            public String related_consumption_id;
            public String create_time;
            public String create_person;
            public int status;
        }
    }

    public static class MemberAssetsBuyBean {
        /**
         * id : 490351
         * group_id : 17
         * member_id : 0c02b03a-f6a7-11e8-aa79-7cd30ae45cee
         * member_name : 方言
         * cashier_sp_id : 623
         * cashier_sp_name : 直播教学库
         * bill_buy_id : CB_62301812031059160000000769
         * consume_time : 2018-12-03 10:59:15
         * related_record_id : CANB_623018120310591609244
         * related_asset_id : S101812031059160908
         * related_asset_type : 2
         * related_asset_type_desc : 项目
         * amount_realpay : 0
         * storedvalue_pay : 5800
         * free_pay : 0
         * accumulate_sv_pay : 0
         * coupon_pay : 0
         * arrear : null
         * create_person : 73af82c9-316a-11e8-a35a-00163e0824d9
         * create_person_name : 检莹
         * create_time : 2018-12-03 10:59:16
         * status : 1
         * description :
         * good_id : 64635
         * goodRsp : null
         * good_name : 萍BBB
         * total_num : 10
         * transaction_price : 5800
         * dead_line : null
         * staffPreSaleServiceRecordRspList : []
         */

        public int id;
        public int group_id;
        public String member_id;
        public String member_name;
        public int cashier_sp_id;
        public String cashier_sp_name;
        public String bill_buy_id;
        public String consume_time;
        public String related_record_id;
        public String related_asset_id;
        public int related_asset_type;
        public String related_asset_type_desc;
        public double amount_realpay;
        public double storedvalue_pay;
        public double free_pay;
        public double accumulate_sv_pay;
        public double coupon_pay;
        public double arrear;
        public String create_person;
        public String create_person_name;
        public String create_time;
        public int status;
        public String description;
        public int good_id;
        public Object goodRsp;
        public String good_name;
        public double total_num;
        public double transaction_price;
        public Object dead_line;
        public List<?> staffPreSaleServiceRecordRspList;
    }

    public static class MemberAssetsGiveawayBean {

        public String description;
        public String cashier_sp_name;
        public String create_time;
        public String consume_time;

    }
}
