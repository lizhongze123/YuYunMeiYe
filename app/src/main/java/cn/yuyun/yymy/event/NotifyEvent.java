package cn.yuyun.yymy.event;

public class NotifyEvent {

    public static int LOGOUT = 1001;
    public static int RREFRESH = 1002;
    public static int DATA = 1003;
    public static int NOTIFICE = 1004;
    public static int WORK = 1005;
    public static int LOGIN = 1006;
    public static int RREFRESH_LABEL = 1007;
    public static int RREFRESH_EMAIL = 1008;
    public static int RREFRESH_EDIT_COMMUNICATION = 1009;
    public static int APPOINTMENT = 1010;
    public static int STAFF = 1011;
    public static int COLLECTION_ONE = 1012;
    public static int COLLECTION_TWO = 1013;
    public static int COLLECTION_THREE = 1014;
    public static int COLLECTION_FOUR = 1015;
    public static int RREFRESH_DOT = 1016;
    public static int RREFRESH_EDIT_ACTIONS = 1017;
    public static int FINISH_COMMISION_STAFF = 1018;
    public static int FINISH_COMMISION = 1019;
    public static int FINISH_CASHIER = 1020;
    public static int SELECT_PROJECT = 1021;

    public Object value;

    public int tag;

    public NotifyEvent(Object value, int tag) {
        this.tag = tag;
        this.value = value;
    }

    public NotifyEvent(int tag) {
        this.tag = tag;
    }

    public NotifyEvent() {
    }
}
