package cn.yuyun.yymy.http.request;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author
 * @desc
 * @date
 */
public class RequestCertificatePic {

    public String group_id = "";
    /**储值1  消费2*/
    public int record_type;
    public String record_id = "";
    public String create_person = "";
    public List<File> fileList = new ArrayList<>();



}
