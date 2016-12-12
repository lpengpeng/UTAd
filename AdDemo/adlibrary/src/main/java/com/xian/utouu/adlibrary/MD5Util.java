package com.xian.utouu.adlibrary;

import java.security.MessageDigest;

/**
 * Create by 李俊鹏 on 2016/12/9 17:30
 * Function：md5加密
 * Desc：
 */
public class MD5Util {

    public static void main(String[] args) {
        System.out.print(MD5Util.md5(""));
    }

    /**
     * MD5加密工具类
     *
     * @author luheng
     * @param s
     * @return
     */
    public  static String md5(String s) {

        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

        try {
            byte[] strTemp = s.getBytes();
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }
}
