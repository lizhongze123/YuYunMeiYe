package cn.yuyun.yymy.view.sidebar;

import java.util.Comparator;

import cn.yuyun.yymy.http.result.BaseComparatorBean;
import cn.yuyun.yymy.http.result.StaffBean;

public class PinyinComparator implements Comparator<BaseComparatorBean> {

    @Override
    public int compare(BaseComparatorBean lhs, BaseComparatorBean rhs) {
        return sort(lhs, rhs);
    }

    private int sort(BaseComparatorBean lhs, BaseComparatorBean rhs) {
        // 获取ascii值
        int lhs_ascii = lhs.getFirstPinYin().toUpperCase().charAt(0);
        int rhs_ascii = rhs.getFirstPinYin().toUpperCase().charAt(0);
//		// 判断若不是字母，则排在字母之后

        if(lhs_ascii == rhs_ascii){
            return 0;
        } else if (lhs_ascii < 65 || lhs_ascii > 90) {
            return 1;
        } else if (rhs_ascii < 65 || rhs_ascii > 90) {
            return -1;
        } else {
            int r = (lhs.getPinYin().toUpperCase()).compareTo((rhs.getPinYin().toUpperCase()));
            return r;
        }
    }

}
