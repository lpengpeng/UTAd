package com.xian.utouu.adlibrary;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.VideoView;

import java.io.File;

/**
 * Create by 李俊鹏 on 2016/12/1 11:44
 * Function： 自定义视频播放
 * Desc：
 */
public class CustomVideoView extends VideoView {
    private File videoFile;
    private Context context;

    private File saveVideofile;

    public CustomVideoView(Context context) {
        super(context);
        this.context = context;
    }

    public CustomVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;

    }

    public CustomVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
//        videoFile = new File(sdpath);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(View.MeasureSpec.getSize(widthMeasureSpec), View.MeasureSpec.getSize(heightMeasureSpec));
    }

    public void playVideo() {
        start();
    }

    /**
     * 播放视频
     *
     * @param uri 播放地址
     */
    public void playVideo(Uri uri, int type, String downLoadUrl,String savePath) {
        videoFile = new File(savePath);
        Uri videoUri = null;
        switch (type) {
            case 0:
                videoUri = uri;
                saveVideofile = new File(videoFile, "ad.mp4");
                break;
            case 1:
                if (!SdUtils.ExistSDCard(context)) {
                    videoUri = uri;
                } else {
                    if (!videoFile.exists()) {
                        videoUri = uri;
                        videoFile.mkdir();
                        saveVideofile = new File(videoFile, "ad.mp4");
                    } else {
                        saveVideofile = new File(videoFile, "ad.mp4");
                        if (!saveVideofile.exists()) {

                        } else {
                            videoUri = Uri.fromFile(saveVideofile);
                        }
                    }
                }
                break;
        }

        // 设置播放路径
        setVideoURI(videoUri);
        //开始播放
        start();
        if (!TextUtils.isEmpty(downLoadUrl)) {//对字符串进行判断，如果不为空的话就去下载新的视频保存到本地
            HttpTool.downLoadFromUrl(downLoadUrl, saveVideofile);
        }
        setOnErrorListener(new MediaPlayer.OnErrorListener() {

                               @Override
                               public boolean onError(MediaPlayer mp, int what, int extra) {
                                   return true;
                               }
                           }
        );
    }
}