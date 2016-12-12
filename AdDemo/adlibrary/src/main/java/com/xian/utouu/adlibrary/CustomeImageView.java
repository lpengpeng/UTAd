package com.xian.utouu.adlibrary;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.ImageView;

import java.io.File;

/**
 * Create by 李俊鹏 on 2016/12/7 9:59
 * Function： 1.app广告有一张广告图。
 * 2.当app启动，先获取本地广告图。如果有则显示，如果为空则跳过广告页
 * 3.获取服务器广告图数据，传递当前app的appKey，服务器根据appKey找有没有新的广告图。有则下载。
 * Desc：
 */

public class CustomeImageView extends ImageView {
    private Context context;
    private String path;
    private static File imageFile;

    public CustomeImageView(Context context) {
        this(context, null);
        this.context = context;

    }

    public CustomeImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.context = context;
    }

    public CustomeImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    public void showImage(int resId, String downLoadUrl) {
        int type;
        String showPath = getSavePath(downLoadUrl);
        imageFile = new File(context.getCacheDir(), showPath);
        if (imageFile.exists()) {
            type = 1;
        } else {
            type = 0;
        }
        switch (type) {
            case 0://显示APP本地图片
                setBackgroundResource(resId);
                download(downLoadUrl, showPath);
                break;
            case 1://显示d缓存目录图片
                Bitmap bmpDefaultPic = BitmapFactory.decodeFile(
                        imageFile.getPath(), null);
                if (bmpDefaultPic != null) {
                    setImageBitmap(bmpDefaultPic);
                }
                break;
        }
    }

    @NonNull
    private String getSavePath(String downLoadUrl) {
        String showPath = "";
        if (downLoadUrl.endsWith(".jpg")) {
            showPath = MD5Util.md5(downLoadUrl) + ".jpg";
        } else if (downLoadUrl.endsWith(".png")) {
            showPath = MD5Util.md5(downLoadUrl) + ".png";
        }
        return showPath;
    }

    private void download(String url, String path) {
        if (url == null) {
            return;
        }
        imageFile = new File(context.getCacheDir(), path);
        HttpTool.downLoadFromUrl(url, imageFile);
    }
}
