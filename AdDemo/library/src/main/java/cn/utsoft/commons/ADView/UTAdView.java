package cn.utsoft.commons.ADView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

/**
 * Create by 李俊鹏 on 2016/12/7 9:43
 * Function：
 * Desc：
 */

public class UTAdView extends RelativeLayout implements View.OnClickListener {
    private ImageView customeImageView;
    private Button jumpNext;
    private Context context;
    private MyCountDownTimer myCountDownTimer;
    private UTVideoView customVideoView;
    private UTGifView gif;
    private MyCountdownClick myClick;
    private RelativeLayout rlGowhere;
    private FrameLayout flVideoView;
    private ImageView flImage;
    private boolean isShowTime;
    private String adUrl;
    private Class<?> goActivity;
    private MyAdClick myAdClick;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                showView(mActivity, sdpath, resource, url, time, goActivity);
            }
        }
    };//不加这个分号则不能自动添加代码
    private String url;
    private String sdpath;
    private int time;
    private Activity mActivity;
    private String resource;

    public UTAdView(Context context) {
        this(context, null);
        this.context = context;
    }

    public UTAdView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.context = context;
    }

    public UTAdView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView(context);
    }

    /**
     * 设置跳过按钮的点击事件
     *
     * @param myClick
     */
    public void setMyClick(MyCountdownClick myClick) {
        this.myClick = myClick;
    }

    /**
     * 设置跳过按钮的点击事件
     *
     * @param myAdClick
     */
    public void setMyAdClick(MyAdClick myAdClick) {
        this.myAdClick = myAdClick;
    }

    private void initView(Context context) {
        View.inflate(context, R.layout.my_view, this);
        customeImageView = (ImageView) findViewById(R.id.iv_ad);
        rlGowhere = (RelativeLayout) findViewById(R.id.rl_gowhere);
        flVideoView = (FrameLayout) findViewById(R.id.fl_vv);
        jumpNext = (Button) findViewById(R.id.bt_time);
        customVideoView = (UTVideoView) findViewById(R.id.vv_ad);
        flImage = (ImageView) findViewById(R.id.iv_fl);
        jumpNext.setOnClickListener(this);
        gif = (UTGifView) findViewById(R.id.giv);
        customVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
                flImage.postDelayed(new Runnable() {
                    public void run() {
                        flImage.setVisibility(View.GONE);
                    }
                }, 500);
            }
        });
    }

    /**
     * 设置广告页停留时间
     *
     * @param millisInFuture 总共的停留时间
     * @param showTime       是否显示倒计时时间的显示
     */
    public void setProperty(long millisInFuture, boolean showTime) {
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
        if (myCountDownTimer != null) {
            myCountDownTimer.cancel();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.bt_time) {
            myClick.CountdownClick();
            customVideoView.pause();
            myCountDownTimer.cancel();
        } else if (id == R.id.rl_gowhere) {
            customVideoView.pause();
            myAdClick.AdClick();
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(adUrl));
            context.startActivity(new Intent(context, goActivity));
            context.startActivity(i);
            myCountDownTimer.cancel();
        }
    }

    /**
     * 倒计时按钮的点击接口
     */
    public interface MyCountdownClick {
        void CountdownClick();
    }

    /**
     * 倒计时按钮的点击接口
     */
    public interface MyAdClick {
        void AdClick();
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
            myAdClick.AdClick();
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
     * 获取广告跳转按钮
     *
     * @return
     */
    public Button getJumpButton() {
        return jumpNext;
    }

    /**
     * 播放本地视频
     *
     * @param file
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
     * 显示本地图片
     *
     * @param file
     */
    public void showImage(File file) {
        customeImageView.setVisibility(VISIBLE);
        Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
        customeImageView.setImageBitmap(bitmap);
        flVideoView.setVisibility(GONE);
        gif.setVisibility(GONE);
    }

    /**
     * 显示资源文件图片
     *
     * @param resId
     */
    public void showImage(int resId) {
        customeImageView.setVisibility(VISIBLE);
        customeImageView.setBackgroundResource(resId);
        flVideoView.setVisibility(GONE);
        gif.setVisibility(GONE);
    }

    /**
     * 显示gif图片
     *
     * @param file
     */
    public void showGif(File file) {
        gif.setVisibility(VISIBLE);
        flVideoView.setVisibility(GONE);
        customeImageView.setVisibility(GONE);
        gif.showGif(file);
    }

    /**
     * @param mActivity   显示图片的activity
     * @param sdpath      SD卡的路径
     * @param downLoadUrl 下载的链接
     * @param adUrl       广告的链接
     * @param time        倒计时时间
     * @param goActivity  点击按钮后跳转到的界面
     */
    public void showView(final Activity mActivity, String sdpath, final String downLoadUrl, String adUrl, final int time, final Class<?> goActivity) {
        this.adUrl = adUrl;
        String localUrl = "";
        localUrl = getMd5String(downLoadUrl, localUrl);
        File localFile = new File(sdpath, localUrl);
        if (localFile.exists()) {
            setProperty(time, false);
            if (downLoadUrl.endsWith(".gif")) {
                showGif(localFile);//显示gif
            } else if (downLoadUrl.endsWith(".png") || downLoadUrl.endsWith(".jpg")) {
                showImage(localFile);//显示图片
            } else if (downLoadUrl.endsWith(".mp4") || downLoadUrl.endsWith(".3gp")) {
                playVideo(localFile);//显示视频
            }
        } else {
            //判断Sd卡是否存在
            if (UTSdUtils.ExistSDCard(context)) {
                File file = new File(sdpath);
                if (!file.exists()) {
                    file.mkdir();
                }
                String saveUrl = "";
                saveUrl = getMd5String(downLoadUrl, saveUrl);
                File saveFile = new File(file, saveUrl);
                //下载相应的广告
                UTHttpTool.downLoadFromUrl(downLoadUrl, saveFile, new UTHttpTool.HttpDownLoadListener() {

                    @Override
                    public void onSuccess(final File file) {
                        mActivity.runOnUiThread(new Runnable() {
                            public void run() {
                                //设置倒计时的时间和是否显示几秒。false 为不显示
                                setProperty(time, false);
                                if (downLoadUrl.endsWith(".gif")) {
                                    showGif(file);//显示gif
                                } else if (downLoadUrl.endsWith(".png") || downLoadUrl.endsWith(".jpg")) {
                                    showImage(file);//显示图片
                                } else if (downLoadUrl.endsWith(".mp4") || downLoadUrl.endsWith(".3gp")) {
                                    playVideo(file);//显示视频
                                }
                            }
                        });
                    }

                    @Override
                    public void onFailure() {
                        context.startActivity(new Intent(context, goActivity));
                        myAdClick.AdClick();
                    }
                });
            }
        }
    }


    private String getMd5String(String downLoadUrl, String saveUrl) {
        if (downLoadUrl.endsWith(".gif")) {
            saveUrl = UTMD5Util.md5(downLoadUrl) + ".gif";
        } else if (downLoadUrl.endsWith(".jpg")) {
            saveUrl = UTMD5Util.md5(downLoadUrl) + ".jpg";
        } else if (downLoadUrl.endsWith(".png")) {
            saveUrl = UTMD5Util.md5(downLoadUrl) + ".png";
        } else if (downLoadUrl.endsWith(".mp4")) {
            saveUrl = UTMD5Util.md5(downLoadUrl) + ".mp4";
        } else if (downLoadUrl.endsWith(".3gp")) {
            saveUrl = UTMD5Util.md5(downLoadUrl) + ".3gp";
        }
        return saveUrl;
    }

    /**
     * 加载广告
     *
     * @param mActivity   上下文
     * @param environment 环境
     * @param resId       初始资源图片
     * @param sdpath      内存卡路径
     * @param APP_ID      应用appId
     * @param APP_KEY     应用appKey
     * @param time        倒计时时长
     * @param goActivity  跳转到的主页面
     */
    public void initAdData(final Activity mActivity, int environment, int resId, final String sdpath, String APP_ID, String APP_KEY, final int time, final Class<?> goActivity) {
        showImage(resId);
        this.goActivity = goActivity;
        this.sdpath = sdpath;
        this.time = time;
        this.mActivity = mActivity;
        final HashMap<String, String> map = new HashMap<>();
        map.put("client_id", APP_ID);
        map.put("client_secret", APP_KEY);
        map.put("grant_type", "client_advertise");
        String adUrl = "";
        switch (environment) {
            case 0:
                adUrl = "https://api.open.dev.utouu.com/v1/advertise/get";
                break;
            case 1:
                adUrl = "https://api.open.test.utouu.com/v1/advertise/get";
                break;
            case 2:
                break;
        }
        UTHttpTool.getAdData(adUrl, map, new UtLoadAdListener() {

            @Override
            public void loadSuccess(String s) {
                try {
                    if (TextUtils.isEmpty(s)) {
                        context.startActivity(new Intent(context, goActivity));
                        myAdClick.AdClick();
                        return;
                    }
                    JSONObject jsonObject = new JSONObject(s);
                    boolean success = jsonObject.getBoolean("success");
                    if (success) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        DisplayMetrics dm = new DisplayMetrics();
                        mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
                        float width = dm.widthPixels;
                        JSONArray jsonArray = data.getJSONArray("resource");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject mObject = (JSONObject) jsonArray.get(i);
                            String mSize = mObject.getString("size");
                            if (width > 1080 && "3".equals(mSize)) {
                                resource = mObject.getString("url");
                            } else if (width >= 720 && width <= 1080 && "2".equals(mSize)) {
                                resource = mObject.getString("url");
                            } else if (width < 720 && "1".equals(mSize)) {
                                resource = mObject.getString("url");
                            }
                        }
                        url = data.getString("url");
                        Message message =
                                 new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void loadFailure(Exception e) {
                context.startActivity(new Intent(context, goActivity));
                myAdClick.AdClick();
            }
        });
    }
}
