package cn.yuyun.yymy.http.result;

import java.io.Serializable;

/**
 * @author
 * @desc
 * @date
 */

public class ResultGroupBean implements Serializable{


    /**
     * group_id : 17
     * name : 内部测试库
     * tel : null
     * charge_man : 测试人员
     * charge_mobile : 15555555555
     * auth_sp_numbers : 1000
     * thumb_url : null
     * logo_url : null
     * qr_code : null
     * description : null
     * addr : null
     * status : null
     * staff_on_working : 98
     * staff_on_working_in_headquarters : 30
     */

    /**集团Id*/
    public String group_id;

    /**集团名称*/
    public String name;

    /**集团电话*/
    public String tel;

    /**集团负责人*/
    public String charge_man;

    /**负责人电话*/
    public String charge_mobile;

    /**授权门店数量*/
    public int auth_sp_numbers;

    /**集团路径*/
    public String thumb_url;

    /**logo图路径*/
    public String logo_url;

    /**二维码地址*/
    public String qr_code;

    /**集团描述*/
    public String description;


    public String addr;

    /**集团状态：-1 删除 1 正常*/
    public String status;

    /**在职员工数量*/
    public int staff_on_working;

    /**总部在职员工数量*/
    public int staff_on_working_in_headquarters;

}
