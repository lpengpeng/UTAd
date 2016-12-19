package com.xian.utouu.adlibrary;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
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
     */
    public void playVideo(File file) {
        Uri videoUri = Uri.fromFile(file);
        // 设置播放路径
        setVideoURI(videoUri);
        //开始播放
        start();
        setOnErrorListener(new MediaPlayer.OnErrorListener() {

                               @Override
                               public boolean onError(MediaPlayer mp, int what, int extra) {
                                   return true;
                               }
                           }
        );
    }
}