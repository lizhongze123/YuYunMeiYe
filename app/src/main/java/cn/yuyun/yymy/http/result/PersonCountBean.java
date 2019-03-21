package cn.yuyun.yymy.http.result;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @desc
 *
 * @author
 * @date
 */

public class PersonCountBean implements Serializable {

    @SerializedName(value = "staff_person_times_amount", alternate = {"staff_handmake"})
    public double amount;
}
