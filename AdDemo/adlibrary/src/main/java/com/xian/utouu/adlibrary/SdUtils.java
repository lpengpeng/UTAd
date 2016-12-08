package com.xian.utouu.adlibrary;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

/**
 * Create by 李俊鹏 on 2016/12/6 19:36
 * Function：
 * Desc：
 */

public class SdUtils {
    /**
     * 判断sd卡是否存在
     *
     * @return
     */
    public static boolean ExistSDCard(Context context) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else
            Toast.makeText(context, "请检查SD卡", Toast.LENGTH_SHORT).show();
        return false;
    }

}
