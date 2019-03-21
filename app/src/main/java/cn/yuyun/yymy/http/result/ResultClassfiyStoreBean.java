package cn.yuyun.yymy.http.result;

import java.io.Serializable;
import java.util.List;

/**
 * @author
 * @desc
 * @date
 */
public class ResultClassfiyStoreBean implements Serializable{

    /**
     * id : 10
     * group_id : 17
     * name : 杭州大区22
     * status : 1
     * create_person : null
     * create_time : 2018-06-30 13:56:52
     * ogServiceplacesRspList : [{"sp_id":225,"group_id":17,"classify_id":10,"sp_name":"武穆祠店","chairman":"x","chairmantel":"020-88905678","shopowner":"","shopowner_tel":"","addr":"广东省洛溪街道","province":"湖南xx","city":"","district":"类似","tel":"020-88905677","lng":0,"lat":0,"thumb_url":"/17/sp/20180717211007.jpeg","status":1,"type":null,"description":"","create_time":"2018-04-28 10:48:31","create_person":"b4fc7c2c-ec6e-11e7-9a86-00163e0824d9","modify_time":"2018-07-18 19:28:13","modify_person":"b4fc7c2c-ec6e-11e7-9a86-00163e0824d9"},{"sp_id":209,"group_id":17,"classify_id":10,"sp_name":"西子湖店","chairman":"1","chairmantel":"","shopowner":"","shopowner_tel":"","addr":"","province":"杭州","city":"","district":"","tel":"020-88905678","lng":0,"lat":0,"thumb_url":"https://yuyunrj.oss-cn-shenzhen.aliyuncs.com/system_resource/thumb/sp/3.png","status":1,"type":null,"description":"","create_time":"2018-04-18 21:11:04","create_person":"b4fc7c2c-ec6e-11e7-9a86-00163e0824d9","modify_time":"2018-07-18 13:54:13","modify_person":"b4fc7c2c-ec6e-11e7-9a86-00163e0824d9"},{"sp_id":190,"group_id":17,"classify_id":10,"sp_name":"钱塘江店","chairman":"侯素元","chairmantel":"15874689071","shopowner":"","shopowner_tel":"","addr":"","province":null,"city":"","district":"","tel":"","lng":0,"lat":0,"thumb_url":"https://yuyunrj.oss-cn-shenzhen.aliyuncs.com/system_resource/thumb/sp/6.png","status":1,"type":null,"description":"","create_time":"2018-04-07 09:35:23","create_person":"b4bf4687-ec6e-11e7-9a86-00163e0824d9","modify_time":"2018-06-30 14:37:39","modify_person":"b4fc7c2c-ec6e-11e7-9a86-00163e0824d9"},{"sp_id":173,"group_id":17,"classify_id":10,"sp_name":"衡山店","chairman":"","chairmantel":"","shopowner":"","shopowner_tel":"","addr":"湖南省衡阳市","province":"湖南","city":"","district":"","tel":"01545211255","lng":0,"lat":0,"thumb_url":"https://yuyunrj.oss-cn-shenzhen.aliyuncs.com/system_resource/thumb/sp/7.png","status":1,"type":null,"description":"","create_time":"2018-03-28 11:41:19","create_person":"b4fc7c2c-ec6e-11e7-9a86-00163e0824d9","modify_time":"2018-07-18 13:54:28","modify_person":"b4fc7c2c-ec6e-11e7-9a86-00163e0824d9"},{"sp_id":162,"group_id":17,"classify_id":10,"sp_name":"旗舰店","chairman":"奥巴马","chairmantel":"","shopowner":"","shopowner_tel":"","addr":"湖南省湖南市湖南县湖南街湖南巷湖南店","province":null,"city":"","district":"","tel":"18795646499","lng":0,"lat":0,"thumb_url":"http://resource.yuyunrj.com/17/sp/20180703140226.jpeg","status":1,"type":null,"description":"","create_time":"2018-03-18 16:30:17","create_person":"b4fc7c2c-ec6e-11e7-9a86-00163e0824d9","modify_time":"2018-07-11 15:44:13","modify_person":"73af82c9-316a-11e8-a35a-00163e0824d9"},{"sp_id":156,"group_id":17,"classify_id":10,"sp_name":"断桥残雪景点店","chairman":"","chairmantel":"","shopowner":"","shopowner_tel":"","addr":"北京省北京市北京县北京街北京巷北京店","province":"杭州西湖","city":"西湖","district":"西湖十景","tel":"","lng":0,"lat":0,"thumb_url":"https://yuyunrj.oss-cn-shenzhen.aliyuncs.com/system_resource/thumb/sp/4.png","status":1,"type":null,"description":"","create_time":"2018-03-17 09:18:47","create_person":"b4fc7c2c-ec6e-11e7-9a86-00163e0824d9","modify_time":"2018-07-18 13:53:58","modify_person":"b4fc7c2c-ec6e-11e7-9a86-00163e0824d9"},{"sp_id":155,"group_id":17,"classify_id":10,"sp_name":"小雷音寺店","chairman":"","chairmantel":"","shopowner":"","shopowner_tel":"","addr":"广东省广州市广州县广州街广州巷广州店","province":"杭州","city":"","district":"","tel":"","lng":0,"lat":0,"thumb_url":"https://yuyunrj.oss-cn-shenzhen.aliyuncs.com/system_resource/thumb/sp/7.png","status":1,"type":null,"description":"","create_time":"2018-03-17 09:17:50","create_person":"b4fc7c2c-ec6e-11e7-9a86-00163e0824d9","modify_time":"2018-07-18 13:54:39","modify_person":"b4fc7c2c-ec6e-11e7-9a86-00163e0824d9"}]
     */

    public int id;
    public String group_id;
    public String name;
    public int status;
    public String create_person;
    public String create_time;
    public List<OgServiceplacesRspListBean> ogServiceplacesRspList;

    public static class OgServiceplacesRspListBean implements Serializable, Comparable{
        /**
         * sp_id : 225
         * group_id : 17
         * classify_id : 10
         * sp_name : 武穆祠店
         * chairman : x
         * chairmantel : 020-88905678
         * shopowner :
         * shopowner_tel :
         * addr : 广东省洛溪街道
         * province : 湖南xx
         * city :
         * district : 类似
         * tel : 020-88905677
         * lng : 0
         * lat : 0
         * thumb_url : /17/sp/20180717211007.jpeg
         * status : 1
         * type : null
         * description :
         * create_time : 2018-04-28 10:48:31
         * create_person : b4fc7c2c-ec6e-11e7-9a86-00163e0824d9
         * modify_time : 2018-07-18 19:28:13
         * modify_person : b4fc7c2c-ec6e-11e7-9a86-00163e0824d9
         */

        public int baseonType;
        public String sp_id;
        public String group_id;
        public int classify_id;
        public String sp_name;
        public String chairman;
        public String chairmantel;
        public String shopowner;
        public String shopowner_tel;
        public String addr;
        public String province;
        public String city;
        public String district;
        public String tel;
        public double lng;
        public double lat;
        public String thumb_url;
        public int status;
        public int type;
        public String description;
        public String create_time;
        public String create_person;
        public String modify_time;
        public String modify_person;
        public int ogType = 2;
        public boolean isChecked;

        @Override
        public int compareTo(Object obj) {
            // 按status比较大小，用于分组
            OgServiceplacesRspListBean b = (OgServiceplacesRspListBean) obj;
            return b.status - this.status;
        }
    }
}
