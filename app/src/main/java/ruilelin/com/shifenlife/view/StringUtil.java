package ruilelin.com.shifenlife.view;

public class StringUtil {


    /**
     * 判断字符串是否为null或空
     *
     * @param string
     * @return true:为 空或null;false:不为 空或null
     */
    public static   boolean isNullOrEmpty(String string) {
        boolean flag = false;
        if (null == string || string.trim().length() == 0) {
            flag = true;
        }
        return flag;
    }
}
