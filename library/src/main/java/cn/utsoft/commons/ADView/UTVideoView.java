package cn.utsoft.commons.ADView;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.VideoView;

import java.io.File;

/**
 * Create by 李俊鹏 on 2016/12/1 11:44
 * Function： 自定义视频播放
 * Desc：
 */
public class UTVideoView extends VideoView {

    public UTVideoView(Context context) {
        super(context);
    }

    public UTVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    public UTVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
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