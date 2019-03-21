package cn.yuyun.yymy.http.request;

import java.io.Serializable;
import java.util.List;

/**
 * @author lzz
 * @desc lzz
 * @date 2018/4/19
 */
public class RequestUnboxing implements Serializable{

    public String content;
    public List<String> pictures;
    public List<Integer> labelIdLists;

}
