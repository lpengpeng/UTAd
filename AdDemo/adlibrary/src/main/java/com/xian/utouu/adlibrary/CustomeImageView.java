package com.xian.utouu.adlibrary;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

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
    private static final String WELCOME_IMAGE_NAME = "welcome_image.jpg";
    private static File imageFile;
    private String sourceUrl;
    public void setSource(String sourceUrl) {
        this.sourceUrl = sourceUrl;
        loadRemoteImageInfo();
    }

    public CustomeImageView(Context context) {
        this(context, null);
        this.context = context;
        imageFile = new File(context.getCacheDir(), WELCOME_IMAGE_NAME);
    }

    public CustomeImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.context = context;
        imageFile = new File(context.getCacheDir(), WELCOME_IMAGE_NAME);
    }

    public CustomeImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
        this.context = context;
        imageFile = new File(context.getCacheDir(), WELCOME_IMAGE_NAME);
    }

    private void initAttrs(AttributeSet attrs) {

    }

    public void init(int resId) {
        loadLocalImage(resId);
    }

    /**
     * 必须初始化时调
     */
    private void loadLocalImage(int resId) {
        if (imageFile == null || !imageFile.exists()) {
            setBackgroundResource(resId);
            return;
        }
        Bitmap bmpDefaultPic = BitmapFactory.decodeFile(
                imageFile.getPath(), null);
        if (bmpDefaultPic != null) {
            setImageBitmap(bmpDefaultPic);
        }
    }

    private void loadRemoteImageInfo() {
        if (TextUtils.isEmpty(sourceUrl)) {
            return;
        }
        HttpTool.sendGetRequest(sourceUrl, new HttpTool.HttpCallbackListener() {
            @Override
            public void onFinish(String reslut) {
                try {
                    JSONObject jsonObject = new JSONObject(reslut);
                    String image = jsonObject.getString("image");
                    download(image);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    private void download(String url) {
        if (url == null) {
            return;
        }
        imageFile = new File(context.getCacheDir(), WELCOME_IMAGE_NAME);
        HttpTool.downLoadFromUrl(url, imageFile);
    }
}
