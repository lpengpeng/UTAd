package com.xian.utouu.adlibrary;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

    public CustomeImageView(Context context) {
        this(context, null);
    }

    public CustomeImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomeImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void showImage(File file) {
                Bitmap bmpDefaultPic = BitmapFactory.decodeFile(
                        file.getPath(), null);
                if (bmpDefaultPic != null) {
                    setImageBitmap(bmpDefaultPic);
                }
    }
}
