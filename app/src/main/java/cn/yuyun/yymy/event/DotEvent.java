package cn.yuyun.yymy.event;

public class DotEvent {

   public static int NOTICE = 1114;
   public static int TRAIN = 1115;
   public static int WORK = 1116;
   public static int APPROVE = 1117;

    public Object value;

    public int tag;

    public DotEvent(Object value, int tag){
        this.tag = tag;
        this.value = value;
    }

    public DotEvent(int tag){
        this.tag = tag;
    }

    public DotEvent(){
    }
}
