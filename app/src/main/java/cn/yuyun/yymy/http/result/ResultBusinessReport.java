package cn.yuyun.yymy.http.result;


import java.util.List;

/**
 * @author
 * @desc
 * @date
 */
public class ResultBusinessReport {


    /**
     * sp_name_in_sp_id_list : 豪美丽休闲养生
     * sp_id_list : [906]
     * date : 2018-11-01
     * analysisSpTurnoverSingleRsp : {"group_id":null,"total_amount":0,"person_times":0,"amount_consume":0,"store_performance":0,"person_number":0,"cash_amount":null,"pos_amount":null,"transfer_amount":null,"wechat_pay_amount":null,"ali_pay_amount":null,"refund_to_storedvalue":0,"refund_realpay":0,"transaction_price":0,"service_numbers":0,"new_member_percent":"0%"}
     */

    public String sp_name_in_sp_id_list;
    public String date;
    public AnalysisSpTurnoverSingleRspBean analysisSpTurnoverSingleRsp;
    public List<Integer> sp_id_list;

    public static class AnalysisSpTurnoverSingleRspBean {
        /**
         * group_id : null
         * total_amount : 0
         * person_times : 0
         * amount_consume : 0
         * store_performance : 0
         * person_number : 0
         * cash_amount : null
         * pos_amount : null
         * transfer_amount : null
         * wechat_pay_amount : null
         * ali_pay_amount : null
         * refund_to_storedvalue : 0
         * refund_realpay : 0
         * transaction_price : 0
         * service_numbers : 0
         * new_member_percent : 0%
         */

        public String group_id;
        public double total_amount;
        public int person_times;
        public double amount_consume;
        public double store_performance;
        public int person_number;
        public double cash_amount;
        public double pos_amount;
        public double transfer_amount;
        public double wechat_pay_amount;
        public double ali_pay_amount;
        public double refund_to_storedvalue;
        public double refund_realpay;
        public double transaction_price;
        public int service_numbers;
        public String new_member_percent;
    }
}
