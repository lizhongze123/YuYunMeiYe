package cn.yuyun.yymy.ui.store.book;

/**
 * @author lzz
 * @desc lzz
 * @date 2018/9/26
 */
public class AppointmentBookPosBean {


    public int x;
    public int y;
    public String staffName;
    public String startTime;
    public String endTime;

    @Override
    public String toString() {
        return "AppointmentBookPosBean{" +
                "x=" + x +
                ", y=" + y +
                ", staffName='" + staffName + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }
}
