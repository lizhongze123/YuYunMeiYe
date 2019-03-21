package cn.yuyun.yymy.http.result;

import java.io.Serializable;

import cn.yuyun.yymy.app.MyApplication;
import cn.yuyun.yymy.bean.MomentsActionType;
import cn.yuyun.yymy.constan.Constans;
import cn.yuyun.yymy.sp.UserfoPrefs;

/**
 * @author
 * @desc
 * @date
 */

public class ResultWorkComment implements Serializable{

    public int id;
    public int related_work_id;
    public String approve_person;
    public String approve_person_name;
    public String approve_person_avatar;
    public String approve_agreement;
    public MomentsActionType like_dislike;
    public String create_time;
    public String modify_time;

}
