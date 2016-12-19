package com.xian.utouu.adlibrary;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.File;

/**
 * Create by 李俊鹏 on 2016/12/7 9:43
 * Function：
 * Desc：
 */

public class AdView extends RelativeLayout implements View.OnClickListener {
    private ImageView customeImageView;
    private Button jumpNext;
    private Class goActivity;
    private Context context;
    private MyCountDownTimer myCountDownTimer;
    private CustomVideoView customVideoView;
    private CustomerGifView gif;
    private MyCountdownClick myClick;
    private MyAdClick myAdClick;
    private RelativeLayout rlGowhere;
    private FrameLayout flVideoView;
    private ImageView flImage;
    private boolean isShowTime;

    public AdView(Context context) {
        this(context, null);
        this.context = context;
    }

    public AdView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.context = context;
    }

    public void setMyImageClick(MyAdClick myAdClick) {
        this.myAdClick = myAdClick;
    }

    public AdView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView(context);
    }

    /**
     * @param myClick
     */
    public void setMyClick(MyCountdownClick myClick) {
        this.myClick = myClick;
    }

    /**
     * 设置将要跳转的界面
     *
     * @param goActivity 将要跳转到的类
     */
    public void goWhere(Class<?> goActivity) {
        this.goActivity = goActivity;
    }

    /**
     * 暂停视频
     */
    public void stopVideo() {
        customVideoView.pause();
    }

    /**
     * 视频重新可见并播放
     */
    public void resumeVideo() {
        customVideoView.resume();
        customVideoView.playVideo();
    }

    /**
     * 获取视频当前的位置
     *
     * @return
     */
    public int getVideogCurrentPosition() {
        return customVideoView.getCurrentPosition();
    }

    private void initView(Context context) {
        View.inflate(context, R.layout.my_view, this);
        customeImageView = (ImageView) findViewById(R.id.iv_ad);
        rlGowhere = (RelativeLayout) findViewById(R.id.rl_gowhere);
        flVideoView = (FrameLayout) findViewById(R.id.fl_vv);
        jumpNext = (Button) findViewById(R.id.bt_time);
        customVideoView = (CustomVideoView) findViewById(R.id.vv_ad);
        flImage = (ImageView) findViewById(R.id.iv_fl);
        jumpNext.setOnClickListener(this);
        gif = (CustomerGifView) findViewById(R.id.giv);
        customVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
                flImage.postDelayed(new Runnable() {
                    public void run() {
                        flImage.setVisibility(View.GONE);
                    }
                }, 600);
            }
        });
    }

    /**
     * 设置广告页停留时间
     *
     * @param millisInFuture 总共的停留时间
     * @param showTime       是否显示倒计时时间的显示
     */
    public void setTime(long millisInFuture, boolean showTime) {
        this.isShowTime = showTime;
        myCountDownTimer = new MyCountDownTimer(millisInFuture, 1000);
        myCountDownTimer.start();
        jumpNext.setVisibility(View.VISIBLE);
        rlGowhere.setOnClickListener(this);
    }

    /**
     * 取消倒计时
     */
    public void cancelCountDownTimer() {
        myCountDownTimer.cancel();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.bt_time) {
            myClick.CountdownClick();
            myCountDownTimer.cancel();
        } else if (id == R.id.rl_gowhere) {
            myAdClick.adClick();
        }
    }

    /**
     * 倒计时按钮的点击接口
     */
    public interface MyCountdownClick {
        void CountdownClick();
    }

    /**
     * 广告图片、gif、视频的点击回调
     */
    public interface MyAdClick {
        void adClick();
    }

    class MyCountDownTimer extends CountDownTimer {
        /**
         * @param millisInFuture    表示以毫秒为单位 倒计时的总数
         *                          <p>
         *                          例如 millisInFuture=1000 表示1秒
         * @param countDownInterval 表示 间隔 多少微秒 调用一次 onTick 方法
         *                          <p>
         *                          例如: countDownInterval =1000 ; 表示每1000毫秒调用一次onTick()
         */
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        public void onFinish() {
            context.startActivity(new Intent(context, goActivity));
        }

        public void onTick(long millisUntilFinished) {
            if (isShowTime) {
                jumpNext.setText("跳过" + millisUntilFinished / 1000);
            } else {
                jumpNext.setText("跳过");
            }
        }
    }

    /**
     * 设置跳转按钮的背景
     *
     * @param resId
     */
    public void setJumpButtonBackgound(int resId) {
        jumpNext.setBackgroundResource(resId);
    }

    /**
     * 设置按钮文字颜色
     *
     * @param value
     */
    public void setJumpButtonTextColor(int value) {
        jumpNext.setTextColor(value);
    }

    /**
     * 设置按钮的参数
     *
     * @param params
     */
    public void setJumpButtonParams(ViewGroup.LayoutParams params) {
        jumpNext.setLayoutParams(params);
    }

    /**
     * 播放视频
     *
     */
    public void playVideo(File file) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(file.getPath());
        Bitmap bitmap = mmr.getFrameAtTime(0);
        flImage.setImageBitmap(bitmap);
        mmr.release();
        flVideoView.setVisibility(View.VISIBLE);
        customVideoView.setVisibility(View.VISIBLE);
        customeImageView.setVisibility(GONE);
        gif.setVisibility(GONE);
        customVideoView.playVideo(file);
    }

    /**
     * 显示图片
     *
     */
    public void showImage(File file) {
        customeImageView.setVisibility(VISIBLE);
        Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
        BitmapDrawable drawable = new BitmapDrawable(bitmap);
        customeImageView.setBackground(drawable);
        flVideoView.setVisibility(GONE);
        gif.setVisibility(GONE);
    }    /**
     * 显示图片
     *
     */
    public void showImage(int resId) {
        customeImageView.setVisibility(VISIBLE);
        customeImageView.setBackgroundResource(resId);
        flVideoView.setVisibility(GONE);
        gif.setVisibility(GONE);
    }

    /**
     * 展示gif图片
     *
     */
    public void showGif(File file) {
        gif.setVisibility(VISIBLE);
        flVideoView.setVisibility(GONE);
        customeImageView.setVisibility(GONE);
        gif.showGif(file);
    }
}
