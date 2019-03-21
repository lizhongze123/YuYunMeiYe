package cn.yuyun.yymy.event;

public class RefreshUnboxingEvent {

    public static int FAVORITE = 1114;
    public static int LIKE = 1115;

    public Object value;

    public int tag;

    public RefreshUnboxingEvent(Object value, int tag){
        this.tag = tag;
        this.value = value;
    }

    public RefreshUnboxingEvent(){
    }

}
