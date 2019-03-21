package cn.yuyun.yymy.view.lineview;

import java.io.Serializable;

import cn.yuyun.yymy.utils.RegxUtils;

/**
 * @author lzz
 * @desc
 * @date 2017/12/4
 */

public class FundMode implements Serializable {

    //x轴原始时间数据，ms
    public String datetime;
    public float dataY;
    public String originDataY;
    //在自定义view:FundView中的位置坐标
    public float floatX;
    public float floatY;
    public String week;

    public FundMode(String timestamp, String week, String actual) {
        this.week = week;
        this.datetime = timestamp;
        this.originDataY = actual;
        //提取后的Y周的值
        this.dataY = RegxUtils.getPureDouble(originDataY);
        this.floatY = RegxUtils.getPureDouble(originDataY);
    }

}