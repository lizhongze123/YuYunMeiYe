package cn.yuyun.yymy.http.result;

import java.util.List;

import cn.yuyun.yymy.bean.ConsumeType;

/**
 * @author
 * @desc
 * @date
 */
public class ResultBillManagerTypeDetail {

    /**
     * bill_repayment_info : []
     * bill_consume_info : []
     * bill_giveaway_info : [{"id":15445,"group_id":17,"member_id":"9325fa85-3967-11e8-a35a-00163e0824d9","member_name":"我没有生日","cashier_sp_id":209,"cashier_sp_name":"开发专用","bill_giveaway_id":"CG_20901806081702150000000634","consume_time":"2018-06-08 17:02:19","related_record_id":"CANB_209018060817021506460","related_asset_id":"S001806081702150782","related_asset_type":2,"related_asset_type_desc":"项目","create_person":null,"create_person_name":null,"create_time":"2018-06-08 17:02:15","status":1,"description":"","good_id":16049,"good_name":"ddss","total_num":1,"transaction_price":null,"dead_line":null,"staffPersonTimesRspList":[],"staffPreSaleServiceRecordRspList":[]},{"id":15446,"group_id":17,"member_id":"9325fa85-3967-11e8-a35a-00163e0824d9","member_name":"我没有生日","cashier_sp_id":209,"cashier_sp_name":"开发专用","bill_giveaway_id":"CG_20901806081702150000000219","consume_time":"2018-06-08 17:02:19","related_record_id":"CANB_209018060817021506460","related_asset_id":"S001806081702150824","related_asset_type":2,"related_asset_type_desc":"项目","create_person":null,"create_person_name":null,"create_time":"2018-06-08 17:02:15","status":1,"description":"","good_id":14368,"good_name":"美容服务199","total_num":1,"transaction_price":null,"dead_line":null,"staffPersonTimesRspList":[],"staffPreSaleServiceRecordRspList":[]}]
     * record_certificate_thumbwal_info : [{"id":1,"group_id":17,"thumb_url":"/respath/17/autograph/20180609093936.jpeg","record_type":1,"record_id":"CANB_209018060817021506460","create_person":"萍萍萍","create_time":"2018-06-09 09:39:31"},{"id":2,"group_id":17,"thumb_url":"/respath/17/autograph/20180609093936.png","record_type":1,"record_id":"CANB_209018060817021506460","create_person":"萍萍萍","create_time":"2018-06-09 09:39:31"}]
     * record_notify_schedule_info : {"id":2,"group_id":17,"notify_time":"2018-06-11","notify_type":1,"member_id":"9325fa85-3967-11e8-a35a-00163e0824d9","staff_id":"123","content":"厉害厉害11111","create_person":"萍萍萍","create_time":"2018-06-09 20:53:02"}
     * bill_storedvalue_info : []
     * bill_buy_info : [{"id":104679,"group_id":17,"member_id":"9325fa85-3967-11e8-a35a-00163e0824d9","member_name":"我没有生日","cashier_sp_id":209,"cashier_sp_name":"开发专用","bill_buy_id":"CB_20901806081702150000000977","consume_time":"2018-06-08 17:02:19","related_record_id":"CANB_209018060817021506460","related_asset_id":"S101806081702150183","related_asset_type":2,"related_asset_type_desc":"项目","amount_realpay":200,"storedvalue_pay":0,"free_pay":0,"accumulate_sv_pay":0,"coupon_pay":0,"create_person":"d8b0af22-ec81-11e7-9a86-00163e4878145","create_person_name":null,"create_time":"2018-06-08 17:02:15","status":1,"description":"","good_id":16266,"good_name":"falv","total_num":1,"transaction_price":200,"dead_line":null,"staffPersonTimesRspList":[],"staffPreSaleServiceRecordRspList":[]},{"id":104680,"group_id":17,"member_id":"9325fa85-3967-11e8-a35a-00163e0824d9","member_name":"我没有生日","cashier_sp_id":209,"cashier_sp_name":"开发专用","bill_buy_id":"CB_20901806081702150000000759","consume_time":"2018-06-08 17:02:19","related_record_id":"CANB_209018060817021506460","related_asset_id":"S101806081702150338","related_asset_type":2,"related_asset_type_desc":"项目","amount_realpay":300,"storedvalue_pay":0,"free_pay":0,"accumulate_sv_pay":0,"coupon_pay":0,"create_person":"d8b0af22-ec81-11e7-9a86-00163e4878145","create_person_name":null,"create_time":"2018-06-08 17:02:15","status":1,"description":"","good_id":16050,"good_name":"dgdg","total_num":1,"transaction_price":300,"dead_line":null,"staffPersonTimesRspList":[],"staffPreSaleServiceRecordRspList":[]}]
     * bill_refund_info : []
     * record_info : {"id":141134,"group_id":17,"record_id":"CANB_209018060817021506460","member_id":"9325fa85-3967-11e8-a35a-00163e0824d9","member_mobile":"250","member_card":"No.200180406","consume_type":1,"cashier_sp_id":209,"cashier_sp_name":"开发专用","consume_time":"2018-06-08 17:02:19","create_time":"2018-06-08 17:02:15","consume_type_contains_desc":"购买x2,赠送x2,","consume_amount":0,"pay_cash":0,"pay_ali_pay":0,"pay_wechat_pay":500,"pay_transfer":0,"pay_pos":0,"refund_total":0,"refund_realpay":0,"refund_storedvalue":0,"description":"","create_person":"d8b0af22-ec81-11e7-9a86-00163e4878145","modify_person":"d8b0af22-ec81-11e7-9a86-00163e4878145","modify_time":"2018-06-08 17:02:15","status":1,"member_name":"我没有生日","member_in_sp_id":70,"member_in_sp_name":"白云店","member_level_id":0,"member_level_name":"","create_person_desc":"彭丽萍33333","modify_person_desc":"彭丽萍33333"}
     * record_certificate_info : {"id":1,"group_id":17,"record_type":1,"record_id":"CANB_209018060817021506460","autograph":"","audio":"","description":"","create_person":"ppp","create_time":"2018-06-08 22:48:42"}
     */

    public RecordInfoBean record_info;
    public List<BillAllInfoBean> bill_repayment_info;
    public List<BillAllInfoBean> bill_consume_info;
    public List<BillAllInfoBean> bill_giveaway_info;
    public List<BillAllInfoBean> bill_storedvalue_info;
    public List<BillAllInfoBean> bill_buy_info;
    public List<BillAllInfoBean> bill_refund_info;

    public static class RecordInfoBean {

        public int id;
        public int group_id;
        public String record_id;
        public String member_id;
        public String pos_printer_informations;
        public String member_mobile;
        public String member_avatar;
        public String member_card;
        public int consume_type;
        public int cashier_sp_id;
        public String cashier_sp_name;
        public String consume_time;
        public String create_time;
        public String consume_type_contains_desc;
        public double consume_amount;
        public double pay_cash;
        public double pay_ali_pay;
        public double pay_wechat_pay;
        public double pay_transfer;
        public double pay_pos;
        public double refund_total;
        public double refund_realpay;
        public double refund_storedvalue;
        public String description;
        public String create_person;
        public String modify_person;
        public String modify_time;
        public int status;
        public String member_name;
        public int member_in_sp_id;
        public String member_in_sp_name;
        public int member_level_id;
        public String member_level_name;
        public String create_person_desc;
        public String modify_person_desc;
        public List<StaffPersonTimesRspListBean> staffPersonTimesRspList;
        public List<ConsumptionCertificateRspListBean> consumptionCertificateRspList;
        public List<?> consumptionCertificateThumbwallRspList;

        public static class StaffPersonTimesRspListBean {
            /**
             * id : 70685
             * cashier_sp_id : 499
             * cashier_sp_name : 韶关分组
             * staff_in_og_name : 韶关分组
             * staff_name : 韶关
             * staff_id : 18be23da-8fff-11e8-9e22-7cd30ae0f408
             * person_times : 2
             * related_consumption_type : 2
             * related_consumption_id : CANB_499018073108401009435
             * create_person : 18be23da-8fff-11e8-9e22-7cd30ae0f408
             * create_time : 2018-07-31 08:40:10
             */

            public int id;
            public int cashier_sp_id;
            public String cashier_sp_name;
            public String staff_in_og_name;
            public String staff_name;
            public String staff_id;
            public double person_times;
            public int related_consumption_type;
            public String related_consumption_id;
            public String create_person;
            public String create_time;
        }

        public static class ConsumptionCertificateRspListBean {
            /**
             * id : 273
             * group_id : 17
             * record_type : 2
             * record_id : CANB_499018073108401009435
             * autograph : /17/autograph/20180802141828.jpeg
             * comment :
             * shop_score : 0
             * service_score : 0
             * good_score : 0
             * audio :
             * description :
             * create_person : 18be23da-8fff-11e8-9e22-7cd30ae0f408
             * create_time : 2018-08-02 14:23:36
             */

            public int id;
            public int group_id;
            public int record_type;
            public String record_id;
            public String autograph;
            public String comment;
            public double shop_score;
            public double service_score;
            public double good_score;
            public String audio;
            public String description;
            public String create_person;
            public String create_time;
        }
    }

    public static class BillAllInfoBean {

        public ConsumeType consumeType;
        public int id;
        public int group_id;
        public String member_id;
        public String member_name;
        public int cashier_sp_id;
        public String cashier_sp_name;
        public String bill_giveaway_id;
        public String consume_time;
        public String related_record_id;
        public String related_asset_id;
        public int related_asset_type;
        public String related_asset_type_desc;
        public String create_person;
        public String create_person_name;
        public String create_time;
        public int status;
        public String description;
        public int good_id;
        public String good_name;
        public double total_num;
        public double transaction_price;
        public Object dead_line;
        public List<?> staffPersonTimesRspList;
        public List<StaffPreSaleServiceRecordRspListBean> staffPreSaleServiceRecordRspList;

        public String bill_buy_id;
        public double amount_realpay;
        public double storedvalue_pay;
        public double free_pay;
        public double accumulate_sv_pay;
        public double coupon_pay;

        public String storedvalue_id;
        public String related_consumption_id;
        public int related_consumption_type;
        public String related_consumption_type_desc;
        public double current;
        public double previous;
        public double pay_cash;
        public double pay_ali_pay;
        public double pay_wechat_pay;
        public double pay_pos;
        public double pay_transfer;
        public String create_person_desc;
        public String member_mobile;
        public String member_card;
        public int member_in_sp_id;
        public String member_in_sp_name;
        public int member_level_id;
        public String member_level_name;

        public String bill_consume_id;
        public int consume_num_now;
        public double consume_amount_now;
        public int consume_time_now;
        public String serve_comment;
        public String customer_feedback;
        public int customer_rating;

        public String bill_repayment_id;

        public String bill_refund_id;
        public double refund_amount_realpay;
        public double refund_amount_to_storedvalue;
        public int refund_times;

    }

    public class StaffPreSaleServiceRecordRspListBean {
        /**
         * id : 40047
         * staff_id : c942af4e-6005-11e8-b29a-6c92bf16086d
         * staff_in_og_name : 总部
         * staff_name : 重新
         * sale_type : 1
         * commission : 1
         * handmake : 0
         * related_consumption_type : 7
         * related_consumption_id : CRF_2250180601102607016
         * create_time : 2018-06-01 10:26:07
         * create_person : d8b0af22-ec81-11e7-9a86-00163e4878145
         * status : 1
         */

        public int id;
        public String staff_id;
        public String staff_in_og_name;
        public String staff_name;
        public int sale_type;
        public double commission;
        public double handmake;
        public int related_consumption_type;
        public String related_consumption_id;
        public String create_time;
        public String create_person;
        public int status;
    }
}
