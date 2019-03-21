package cn.yuyun.yymy.http.result;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * @desc
 *
 * @author
 * @date
 */

public class RecordAmountBean implements Serializable {

    @SerializedName("staff_achieve_amount")
    public double amount;

    public int num_new_mb;
    public int num_person_count;
    public double num_person_times;

    public int member_add_sum;
    public double person_times_sum;

}
