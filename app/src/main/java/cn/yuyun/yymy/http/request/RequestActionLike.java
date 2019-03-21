package cn.yuyun.yymy.http.request;

/**
 * @author
 * @desc
 * @date
 */
public class RequestActionLike {

    /**最新活动ID*/
    public int latestActivityId;

    /**1：新增，2：删除*/
    public int operation;

    /**1：收藏，2：点赞*/
    public int type;

}
