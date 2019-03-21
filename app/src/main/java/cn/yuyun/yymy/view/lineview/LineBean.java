package cn.yuyun.yymy.view.lineview;

/**
 * 折线实体类
 */

public class LineBean {

    /**温度值*/
    public int temperature;
    /**时间值*/
    public String time;

    public LineBean(int temperature,String time) {

        this.temperature = temperature;
        this.time = time;
    }

}
