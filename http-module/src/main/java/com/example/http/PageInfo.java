package com.example.http;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PageInfo<T>{

    /**
     * offset : 0
     * limit : 2147483647
     * total : 1
     * size : 15
     * pages : 1
     * current : 1
     * searchCount : true
     * openSort : true
     * orderByField : null
     * records : [{"staff_id":"b4fc7c2c-ec6e-11e7-9a86-00163e0824d9","cashier_sp_id":72,"cashier_sp_name":"贰号店","sale_type":1,"related_consumption_type":4,"consume_time":"2018-01-19 16:47:02","related_consumption_id":"CG_720180119164702082","commission":0.1,"handmake":0,"obj":{"id":621,"group_id":17,"member_id":"45d9420f-efa6-11e7-9a86-00163e0824d9","member_name":"吴小姐姐","cashier_sp_id":72,"cashier_sp_name":"贰号店","bill_giveaway_id":"CG_720180119164702082","consume_time":"2018-01-19 16:47:02","related_record_id":"CANB_72018011916470203130","related_asset_id":"D001801191647020552","related_asset_type":1,"related_asset_type_desc":"产品","create_person":null,"create_time":"2018-01-19 16:47:02","status":1,"description":"","good_id":1784,"good_name":"杯子","total_num":1,"transaction_price":null,"dead_line":"2000-01-01 00:00:00","staffPersonTimesRspList":null,"staffPreSaleServiceRecordRspList":[{"id":1568,"staff_id":"b4fc7c2c-ec6e-11e7-9a86-00163e0824d9","staff_in_og_name":"总部","staff_name":"彭丽平","sale_type":1,"commission":0.1,"handmake":0,"related_consumption_type":4,"related_consumption_id":"CG_720180119164702082","create_time":"2018-01-19 16:47:02","create_person":"b4fc7c2c-ec6e-11e7-9a86-00163e0824d9","status":1},{"id":1569,"staff_id":"b4fc7c2c-ec6e-11e7-9a86-00163e0824d9","staff_in_og_name":"总部","staff_name":"彭丽平","sale_type":2,"commission":0.1,"handmake":0,"related_consumption_type":4,"related_consumption_id":"CG_720180119164702082","create_time":"2018-01-19 16:47:02","create_person":"b4fc7c2c-ec6e-11e7-9a86-00163e0824d9","status":1}]}}]
     * condition : null
     * offsetCurrent : 0
     * asc : true
     */

    public int offset;
    public int total;
    public int size;
    public int pages;
    public int limit;
    public int current;
    public boolean searchCount;
    public boolean openSort;
    public Object orderByField;
    public Object condition;
    public int offsetCurrent;
    public boolean asc;
    @SerializedName("records")
    public List<T> dataLsit;

}
