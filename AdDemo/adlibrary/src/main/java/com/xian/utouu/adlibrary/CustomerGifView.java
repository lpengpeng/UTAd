package com.xian.utouu.adlibrary;

import android.content.Context;
import android.util.AttributeSet;

import java.io.File;
import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * Create by 李俊鹏 on 2016/12/6 14:38
 * Function：自定义gif
 * Desc：
 */
public class CustomerGifView extends GifImageView {

    private File gifFile;
    private Context context;
    private File gifSaveFile;

    public CustomerGifView(Context context) {
        this(context, null);
        this.context = context;
    }

    public CustomerGifView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.context = context;
    }

    public CustomerGifView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }

    /**
     * 加载gif图片
     * @param type        显示哪个路径的gif
     * @param resId       app中raw文件下的gif图片
     */
    public void showGif(int type, int resId, File file) {
        switch (type) {
            case 0://从App加载
                if (resId != 0) {
                    setBackgroundResource(resId);
                }
                break;
            case 1://从sd卡加载
                try {
                    GifDrawable drawable = new GifDrawable(file.getPath());
                    setBackground(drawable);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
