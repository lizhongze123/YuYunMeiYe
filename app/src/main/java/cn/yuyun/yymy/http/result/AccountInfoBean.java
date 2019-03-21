package cn.yuyun.yymy.http.result;

import java.io.Serializable;
import java.util.List;

import cn.yuyun.yymy.bean.Sex;

/**
 * @author
 * @desc
 * @date
 */

public class AccountInfoBean implements Serializable {

    public String location;
    public StaffInfoBean staff;
    public AccountBean account;
    public GroupBean group;
    public List<RoleBean> role;
    public List<ResultClassfiyStoreBean> serviceplace;

    public static class StaffInfoBean {
        /**
         * group_id : 17
         * staff_id : b4fc7c2c-ec6e-11e7-9a86-00163e0824d9
         * staff_number : 0014165
         * staff_name : 彭丽平
         * mobile : 15011719611
         * avatar : http://resource.yuyunrj.com/17/avatar/staff/20180628152703.jpeg
         * status : 1
         * sex : 1
         * position_id : 2
         * position_name : 美容师
         * emergency_person :
         * emergency_mobile :
         * entry_time : 2018-03-19
         * baseon_type : 1
         * baseon_id : 17
         * baseon_desc : 总部
         * birth_type : 0
         * birth_year : 0
         * birth_month : 0
         * birth_day : 0
         * cross_sp : 1
         * idcard :
         * mechanic : 1
         * description :
         */

        public String group_id;
        public String staff_id;
        public String staff_number;
        public String staff_name;
        public String mobile;
        public String avatar;
        public int status;
        public Sex sex;
        public String position_id;
        public String position_name;
        public String emergency_person;
        public String emergency_mobile;
        public String entry_time;
        public String baseon_type;
        public String baseon_id;
        public String baseon_desc;
        public int birth_type;
        public int birth_year;
        public int birth_month;
        public int birth_day;
        public int cross_sp;
        public String idcard;
        public int mechanic;
        public String description;
    }

    public static class AccountBean {
        /**
         * id : 67
         * group_id : 17
         * og_type : 1
         * og_id : 17
         * username : 15011719601
         * staff_id : b4fc7c2c-ec6e-11e7-9a86-00163e0824d9
         * privilege_level : null
         * boss : 1
         */

        public int id;
        public int group_id;
        public int og_type;
        public int og_id;
        public String username;
        public String staff_id;
        public Object privilege_level;
        public int boss;
    }

    public static class GroupBean {
        /**
         * group_id : 17
         * name : 内部测试库
         * tel : 020-89705678
         * charge_man : 测试人员
         * charge_mobile : 15555555555
         * auth_sp_numbers : 500
         * thumb_url :
         * logo_url : /respath/17/group_thumb/20180713085011.png
         * qr_code : http://resource.yuyunrj.com/17/group_thumb/20180604184053.jpeg
         * description :
         * addr : 广东省广州市番禺区洛溪华荟名苑41栋
         * status : 1
         */

        public int group_id;
        public String name;
        public String tel;
        public String charge_man;
        public String charge_mobile;
        public int auth_sp_numbers;
        public String thumb_url;
        public String logo_url;
        public String qr_code;
        public String description;
        public String addr;
        public int status;
    }

    public static class RoleBean {
        /**
         * id : 1
         * group_id : 0
         * name : 系统管理员(内置)
         * description : 御韵软件系统内置角色-管理员
         * status : 1
         * create_time : 2017-09-25 18:28:32
         * create_person : 3473e7d8-19a9-43ba-b586-16630e4212a8
         */

        public int id;
        public int group_id;
        public String name;
        public String description;
        public int status;
        public String create_time;
        public String create_person;
    }

}

