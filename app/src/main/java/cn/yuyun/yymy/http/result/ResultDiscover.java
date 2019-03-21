package cn.yuyun.yymy.http.result;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * @author
 * @desc
 * @date
 */
public class ResultDiscover implements Serializable{


    public List<BannerBean> banner;
    public List<ContentBean> content;

    public static class BannerBean implements Serializable{
        /**
         * id : 2
         * thumb_url : https://timgsa.baidu.com/timg?image&quality=80&siz
         */

        public int id;

        @SerializedName("thumb_url")
        public String thumbUrl;
    }

    public static class ContentBean implements Serializable{
        /**
         * id : 8
         * title : 美容院有哪些留客的好方法
         * content : 人天生都爱占小便宜，人们都不太愿意拒绝给予你好处的人，美容院额外礼物可以增加客情，用在联络顾客之间的感情是非常好的。但赠送额外礼物时，不要有太强的目的性，如试用装、小赠品、配送的礼品等，不一定非要在促销的时候才去用，很多礼物也许价值并不是很大，但“额外的东西”能给顾客带来一个好心情，拉近美容院和顾客之间感情，增加彼此之间的牵绊，如有纪念意义的工艺品、自制的小礼品、有特殊用途的工具等等都是很好的。
         * appDiscoverImagesVoList : [{"id":1,"thumb_url":"http://www.ouquan.cn/d/file/jygl/guanli/2018-04-17"}]
         * create_time : 2018-04-18 14:12:27
         */

        public int id;
        public String title;
        public String content;

        @SerializedName("create_time")
        public String createTime;
        public List<AppDiscoverImagesVoListBean> appDiscoverImagesVoList;

        public static class AppDiscoverImagesVoListBean implements Serializable{
            /**
             * id : 1
             * thumb_url : http://www.ouquan.cn/d/file/jygl/guanli/2018-04-17
             */

            public int id;

            @SerializedName("thumb_url")
            public String thumbUrl;
        }
    }
}
