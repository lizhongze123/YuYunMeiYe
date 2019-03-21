package cn.yuyun.yymy.http.result;

import java.util.List;

/**
 * @author
 * @desc
 * @date
 */
public class ResultBillManagerStorevalueDetail {


    /**
     * storedvalue_id : SD_4990180726180647072
     * member_id : df48d131-8fff-11e8-9e22-7cd30ae0f408
     * consume_time : 2018-07-26 18:06:46
     * status : 1
     * cashier_sp_id : 499
     * cashier_sp_name : 韶关分组
     * related_consumption_id : CB_49901807261806470000000839
     * related_consumption_type : 3
     * related_consumption_type_desc : 使用储值购买
     * current : 8202
     * previous : 8402
     * pay_cash : 0
     * pay_ali_pay : 0
     * pay_wechat_pay : 0
     * pay_pos : 0
     * pay_transfer : 0
     * create_time : 2018-07-26 18:06:47
     * create_person : 18be23da-8fff-11e8-9e22-7cd30ae0f408
     * description :
     * create_person_desc : 韶关
     * member_name : 韶关的顾客
     * member_mobile : 13076252076
     * member_avatar : /17/avatar/staff/20180728104141.png
     * member_card : 1013361326
     * member_in_sp_id : 499
     * member_in_sp_name : 韶关分组
     * member_level_id : 382
     * member_level_name : 高级会员卡
     * staffPersonTimesRspList : []
     * staffPreSaleServiceRecordRspList : []
     * consumptionCertificateRspList : [{"id":235,"group_id":17,"record_type":1,"record_id":"SD_4990180726180647072","autograph":"http://resource.yuyunrj.com//17/autograph/20180726181343.jpeg","comment":"","shop_score":0,"service_score":0,"good_score":0,"audio":"/17/audio/20180726181617.amr","description":"哈哈哈哈","create_person":"18be23da-8fff-11e8-9e22-7cd30ae0f408","create_time":"2018-07-26 18:16:19"}]
     * consumptionCertificateThumbwallRspList : []
     */

    public String storedvalue_id;
    public String member_id;
    public String consume_time;
    public int status;
    public int cashier_sp_id;
    public String cashier_sp_name;
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
    public String create_time;
    public String create_person;
    public String description;
    public String create_person_desc;
    public String member_name;
    public String member_mobile;
    public String member_avatar;
    public String member_card;
    public int member_in_sp_id;
    public String member_in_sp_name;
    public int member_level_id;
    public String member_level_name;
    public List<?> staffPersonTimesRspList;
    public List<?> staffPreSaleServiceRecordRspList;
    public List<ConsumptionCertificateRspListBean> consumptionCertificateRspList;
    public List<?> consumptionCertificateThumbwallRspList;

    public static class ConsumptionCertificateRspListBean {
        /**
         * id : 235
         * group_id : 17
         * record_type : 1
         * record_id : SD_4990180726180647072
         * autograph : http://resource.yuyunrj.com//17/autograph/20180726181343.jpeg
         * comment :
         * shop_score : 0
         * service_score : 0
         * good_score : 0
         * audio : /17/audio/20180726181617.amr
         * description : 哈哈哈哈
         * create_person : 18be23da-8fff-11e8-9e22-7cd30ae0f408
         * create_time : 2018-07-26 18:16:19
         */

        public int id;
        public int group_id;
        public int record_type;
        public String record_id;
        public String autograph;
        public String comment;
        public int shop_score;
        public int service_score;
        public int good_score;
        public String audio;
        public String description;
        public String create_person;
        public String create_time;
    }
}
