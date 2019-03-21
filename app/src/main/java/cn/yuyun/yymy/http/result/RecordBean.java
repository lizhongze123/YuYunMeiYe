package cn.yuyun.yymy.http.result;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import cn.yuyun.yymy.bean.ConsumeType;

/**
 * @desc
 *
 * @author
 * @date
 */

public class RecordBean implements Serializable {


    /**
     * staff_id : b4fc7c2c-ec6e-11e7-9a86-00163e0824d9
     * cashier_sp_id : 72
     * cashier_sp_name : 贰号店
     * sale_type : 1
     * related_consumption_type : 4
     * consume_time : 2018-01-19 16:47:02
     * related_consumption_id : CG_720180119164702082
     * commission : 0.1
     * handmake : 0
     * obj : {"id":621,"group_id":17,"member_id":"45d9420f-efa6-11e7-9a86-00163e0824d9","member_name":"吴小姐姐","cashier_sp_id":72,"cashier_sp_name":"贰号店","bill_giveaway_id":"CG_720180119164702082","consume_time":"2018-01-19 16:47:02","related_record_id":"CANB_72018011916470203130","related_asset_id":"D001801191647020552","related_asset_type":1,"related_asset_type_desc":"产品","create_person":null,"create_time":"2018-01-19 16:47:02","status":1,"description":"","good_id":1784,"good_name":"杯子","total_num":1,"transaction_price":null,"dead_line":"2000-01-01 00:00:00","staffPersonTimesRspList":null,"staffPreSaleServiceRecordRspList":[{"id":1568,"staff_id":"b4fc7c2c-ec6e-11e7-9a86-00163e0824d9","staff_in_og_name":"总部","staff_name":"彭丽平","sale_type":1,"commission":0.1,"handmake":0,"related_consumption_type":4,"related_consumption_id":"CG_720180119164702082","create_time":"2018-01-19 16:47:02","create_person":"b4fc7c2c-ec6e-11e7-9a86-00163e0824d9","status":1},{"id":1569,"staff_id":"b4fc7c2c-ec6e-11e7-9a86-00163e0824d9","staff_in_og_name":"总部","staff_name":"彭丽平","sale_type":2,"commission":0.1,"handmake":0,"related_consumption_type":4,"related_consumption_id":"CG_720180119164702082","create_time":"2018-01-19 16:47:02","create_person":"b4fc7c2c-ec6e-11e7-9a86-00163e0824d9","status":1}]}
     */

    public String staff_id;
    public int cashier_sp_id;

    public double achieve_amount;

    /**消费门店名称*/
    @SerializedName("cashier_sp_name")
    public String cashierSpName;

    public int sale_type;
    /**对应的消费类型*/
    public ConsumeType related_consumption_type;

    /**消费时间*/
    @SerializedName("consume_time")
    public String consumeTime;

    /**对应的单据id*/
    public String related_consumption_id;
    public double commission;
    public double handmake;
    public ObjBean obj;

    public static class ObjBean implements Serializable{
        /**
         * id : 621
         * group_id : 17
         * member_id : 45d9420f-efa6-11e7-9a86-00163e0824d9
         * member_name : 吴小姐姐
         * cashier_sp_id : 72
         * cashier_sp_name : 贰号店
         * bill_giveaway_id : CG_720180119164702082
         * consume_time : 2018-01-19 16:47:02
         * related_record_id : CANB_72018011916470203130
         * related_asset_id : D001801191647020552
         * related_asset_type : 1
         * related_asset_type_desc : 产品
         * create_person : null
         * create_time : 2018-01-19 16:47:02
         * status : 1
         * description :
         * good_id : 1784
         * good_name : 杯子
         * total_num : 1
         * transaction_price : null
         * dead_line : 2000-01-01 00:00:00
         * staffPersonTimesRspList : null
         * staffPreSaleServiceRecordRspList : [{"id":1568,"staff_id":"b4fc7c2c-ec6e-11e7-9a86-00163e0824d9","staff_in_og_name":"总部","staff_name":"彭丽平","sale_type":1,"commission":0.1,"handmake":0,"related_consumption_type":4,"related_consumption_id":"CG_720180119164702082","create_time":"2018-01-19 16:47:02","create_person":"b4fc7c2c-ec6e-11e7-9a86-00163e0824d9","status":1},{"id":1569,"staff_id":"b4fc7c2c-ec6e-11e7-9a86-00163e0824d9","staff_in_og_name":"总部","staff_name":"彭丽平","sale_type":2,"commission":0.1,"handmake":0,"related_consumption_type":4,"related_consumption_id":"CG_720180119164702082","create_time":"2018-01-19 16:47:02","create_person":"b4fc7c2c-ec6e-11e7-9a86-00163e0824d9","status":1}]
         */

        public double pay_cash;
        public double pay_ali_pay;
        public double pay_wechat_pay;
        public double pay_pos;
        public double pay_transfer;

        public int id;
        public int group_id;
        public String member_id;
        /**会员姓名*/
        @SerializedName("member_name")
        public String memberName;

        /**实收金额*/
        @SerializedName("amount_realpay")
        public double amountRealpay;

        public int cashier_sp_id;

        /**消费门店*/
        @SerializedName("cashier_sp_name")
        public String cashierSpName;

        public String bill_giveaway_id;

        /**消费详单*/
        @SerializedName("bill_consume_id")
        public String consumeDetail;


        @SerializedName("bill_buy_id")
        public String bill_buy_id;

        public String bill_repayment_id;

        /**消费时间*/
        @SerializedName("consume_time")
        public String consumeTime;

        /**消费单据*/
        @SerializedName("related_record_id")
        public String consumeBill;


        public String related_asset_id;
        public int related_asset_type;
        public String related_asset_type_desc;
        public Object create_person;
        public String create_time;

        /**消费次数*/
        @SerializedName("consume_num_now")
        public int consumeNum;

        /**消耗价值*/
        @SerializedName("consume_amount_now")
        public double consumeAmountNow;
        /**服务时长*/
        @SerializedName("consume_time_now")
        public int serveTime;

        public String customer_feedback;
        public int customer_rating;

        public int status;
        public String description;
        public int good_id;

        /**商品名称*/
        @SerializedName("good_name")
        public String goodName;

        /**服务心得*/
        @SerializedName("serve_comment")
        public String serveComment;

        public int total_num;

        /**成交价格*/
        @SerializedName("transaction_price")
        public double transactionPrice;
        public double refund_amount_realpay;
        public double refund_amount_to_storedvalue;
        public String dead_line;
        public Object staffPersonTimesRspList;
        public List<StaffPreSaleServiceRecordRspListBean> staffPreSaleServiceRecordRspList;

        public static class StaffPreSaleServiceRecordRspListBean implements Serializable{
            /**
             * id : 1568
             * staff_id : b4fc7c2c-ec6e-11e7-9a86-00163e0824d9
             * staff_in_og_name : 总部
             * staff_name : 彭丽平
             * sale_type : 1
             * commission : 0.1
             * handmake : 0
             * related_consumption_type : 4
             * related_consumption_id : CG_720180119164702082
             * create_time : 2018-01-19 16:47:02
             * create_person : b4fc7c2c-ec6e-11e7-9a86-00163e0824d9
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
}
