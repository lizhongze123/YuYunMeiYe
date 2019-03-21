package cn.yuyun.yymy.utils;

import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author
 * @desc
 * @date
 */
public class CommonUtils {

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    public static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    public static String getRandomColor() {
        List<String> colorList = new ArrayList<String>();
        colorList.add("#FF7979");
        colorList.add("#FF9967");
        colorList.add("#FFC367");
        colorList.add("#CCD42A");
        colorList.add("#86DE3D");
        colorList.add("#4CE67C");
        colorList.add("#42EBBE");
        colorList.add("#41E0DB");
        colorList.add("#38CEE3");
        colorList.add("#4CCEFF");
        colorList.add("#4CABFF");
        colorList.add("#6AAEEA");
        colorList.add("#3390E1");
        colorList.add("#6E7AFF");
        colorList.add("#C889FF");
        colorList.add("#E57CFF");
        colorList.add("#FF7CDE");
        colorList.add("#FF7C94");
        colorList.add("#FF7C7C");
        colorList.add("#59D14E");
        colorList.add("#81F277");
        colorList.add("#1AE325");
        colorList.add("#28F833");
        colorList.add("#ABDB84");
        colorList.add("#A5BC47");
        colorList.add("#54C169");
        colorList.add("#7CAA6E");
        colorList.add("#FF9577");
        colorList.add("#E96945");
        colorList.add("#BC9847");
        colorList.add("#16E9E9");
        colorList.add("#3DD4D4");
        colorList.add("#6BC1C1");
        colorList.add("#83C0F4");
        colorList.add("#75A6D0");
        colorList.add("#569BD8");
        colorList.add("#E7C777");
        colorList.add("#BD7D6C");
        colorList.add("#BABD6C");
        colorList.add("#92B069");
        colorList.add("#D0A1C4");
        colorList.add("#BCA1D0");
        colorList.add("#8F9DDC");
        colorList.add("#CEDC8F");
        colorList.add("#8FDCB6");
        colorList.add("#23ACAC");
        colorList.add("#2F8BFF");
        colorList.add("#487EC2");
        colorList.add("#EB87E5");
        colorList.add("#D98C8C");
        colorList.add("#D5DA5F");
        colorList.add("#BABBA4");
        colorList.add("#EC9653");
        colorList.add("#E67F2F");
        colorList.add("#FFA158");
        colorList.add("#C66B67");
        colorList.add("#CDCBFF");
        colorList.add("#A19EEB");
        colorList.add("#F48383");
        colorList.add("#DE5D5D");

        return colorList.get((int)(Math.random() * colorList.size()));
    }

}
