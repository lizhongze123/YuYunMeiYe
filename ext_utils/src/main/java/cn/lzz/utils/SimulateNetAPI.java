package cn.lzz.utils;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

/**
 * @author lzz
 * @desc 从assets中获取数据
 * @date 2017/12/4
 */

public class SimulateNetAPI {
    /**
     * 获取去最原始的数据信息
     *
     * @return json data
     */
    public static String getOriginalFundData(Context context) {
        InputStream input = null;
        try {
            input = context.getAssets().open("list.json");
            String json = convertStreamToString(input);
            return json;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * input 流转换为字符串
     *
     * @param is
     * @return
     */
    private static String convertStreamToString(InputStream is) {
        String s = null;
        try {
            Scanner scanner = new Scanner(is, "UTF-8").useDelimiter("\\A");
            if (scanner.hasNext()) {
                s = scanner.next();
            }
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s;
    }
}
