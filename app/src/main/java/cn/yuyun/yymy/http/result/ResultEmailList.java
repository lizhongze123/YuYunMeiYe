package cn.yuyun.yymy.http.result;

import java.io.Serializable;

/**
 * @author
 * @desc
 * @date
 */

public class ResultEmailList implements Serializable{

    /**总裁信箱ID*/
    public int presidentMailboxId;

    /**集团ID*/
    public int groupId;

    public String content;
    public String staffId;
    public String baseon_desc;

    public String staff_name;
    public String createTime;
    public String createPersonName;
    public String createPersonAvatar;
    public String createPersonPosition;

    public boolean isChecked;

}
