package cn.yuyun.yymy.http.result;

import java.util.List;

/**
 * @author
 * @desc
 * @date
 */
public class ResultLineData {


    /**
     * sp_name_in_sp_id_list : null
     * sp_id_list : [881,854,824,727,656,631,624,619,616,595,594,593,591,565,526,525,518,508,505,489,444,443,342,225,209,199,190,173,162,156,155,864,838,837,730,643,573,530,516,512,510,509,503,501,500,498,497,496,495,491,490,487,484,481,478,477,476,475,472,471,466,464,449,392,388,379,374,239,237,206,174,571,533,195,194,193,132,566,409,153,152,150,149,148,863,662,626,483,482,434,100,97,96,390,69,70,106,446,442,894,839,806,623,492,465,845,844,725,648,597,499,780,729,660,523,494,493,480,454,453,439,437,436,435,433,432,430,428,427,426,422,421,420,419,418,415,413,411,404,400,396,395,389,375,362,345,341,243,241,197,189,188,179,139,116,104,102,108,110]
     * date : 2018-11-01
     * analysisSpTurnoverSingleRsp : {"group_id":17,"total_amount":11414,"person_times":11,"amount_consume":1677,"store_performance":11544,"person_number":12,"cash_amount":485,"pos_amount":8060,"transfer_amount":40,"wechat_pay_amount":1829,"ali_pay_amount":1000,"refund_to_storedvalue":1900,"refund_realpay":0,"transaction_price":null,"service_numbers":0,"new_member_percent":null}
     */

    public Object sp_name_in_sp_id_list;
    public String date;
    public AnalysisSpTurnoverSingleRspBean analysisSpTurnoverSingleRsp;
    public List<String> sp_id_list;

    public static class AnalysisSpTurnoverSingleRspBean {
        /**
         * group_id : 17
         * total_amount : 11414
         * person_times : 11
         * amount_consume : 1677
         * store_performance : 11544
         * person_number : 12
         * cash_amount : 485
         * pos_amount : 8060
         * transfer_amount : 40
         * wechat_pay_amount : 1829
         * ali_pay_amount : 1000
         * refund_to_storedvalue : 1900
         * refund_realpay : 0
         * transaction_price : null
         * service_numbers : 0
         * new_member_percent : null
         */

        public double amount_consume;

        public int group_id;
        public double total_amount;
        public double person_times;
        public double store_performance;
        public double person_number;
        public double cash_amount;
        public double pos_amount;
        public double transfer_amount;
        public double wechat_pay_amount;
        public double ali_pay_amount;
        public double refund_to_storedvalue;
        public double refund_realpay;
        public double transaction_price;
        public double service_numbers;
        public double new_member_percent;
    }
}
