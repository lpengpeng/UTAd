package com.xian.utouu.adlibrary;

import java.io.File;

/**
 * Created by YSD on 2016/12/9.
 */

public class FileUtils {

    public static void deldetFile(File saveFile, String suffix) {
        File[] listFiles = saveFile.listFiles();
        if (listFiles != null) {
            for (File files : listFiles) {
                if (files.isFile()) {
                    String path = files.getPath();
                    if (path.endsWith(suffix)) {
                        files.delete();
                    }
                }
            }
        }
    }
}
