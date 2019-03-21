package cn.yuyun.yymy.http.request;

/**
 * @author lzz
 * @desc lzz
 * @date 2018/7/17
 */
public class RequestTrainLike {

    /**培训资料ID*/
    public int trainInfoId;

    /**1：新增，2：删除*/
    public int operation;

    /**1：收藏，2：点赞*/
    public int type;

}
