package cn.yuyun.yymy.ui.store.report;

import com.example.http.PageInfo;

import java.util.List;

import cn.yuyun.yymy.http.result.ResultStoreAnalysis;

/**
 * @author lzz
 * @desc lzz
 * @date 2018/11/16
 */
public class ResultStoreAnalysisOut {

    public AmountBean amount;
    public PageInfo<ResultStoreAnalysis> detail;

    public static class AmountBean {
        /**
         * canbe_consume : 149178.21
         * storedvalue : 1980
         * arrears : 10747
         */

        public double canbe_consume;
        public double storedvalue;
        public double arrears;
    }


}
