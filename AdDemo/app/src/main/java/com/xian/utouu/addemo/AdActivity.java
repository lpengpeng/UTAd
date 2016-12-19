package com.xian.utouu.addemo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;

import com.xian.utouu.adlibrary.AdView;
import com.xian.utouu.adlibrary.HttpTool;
import com.xian.utouu.adlibrary.SdUtils;

import java.io.File;

public class AdActivity extends AppCompatActivity {
    private AdView myView;
    //gif的下载地址, http://photo.l99.com/bigger/00/1425373097998_utt83i.gif
    private String videoUrl = "http://www.51hfzs.cn/123.mp4"; //视频的下载地址
    private String gifUrl = "http://photocdn.sohu.com/20150808/mp26389744_1439008079309_5.gif";
    //图片的请求地址  先返回json 数据里面包含了链接
    private String imageUrl = "http://4493bz.1985t.com/uploads/allimg/150127/4-15012G52133.jpg";
    // 视频和gif在SD卡中的目录
    private String sdpath = Environment.getExternalStorageDirectory() + "/adVideo";
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        //调用接口返回url
        url = getIntent().getStringExtra("url");

        //获取控件
        myView = (AdView) findViewById(R.id.myView_main);

        //设置跳转按钮
        myView.goWhere(HomeActivity.class);

        //设置跳转按钮的背景
        myView.setJumpButtonBackgound(R.mipmap.jump);

        //设置跳转按钮的字体颜色
        myView.setJumpButtonTextColor(0xf16060);

        //设置按钮的位置的参数
        myView.setJumpButtonParams(null);

        //设置跳转按钮的点击事件
        myView.setMyClick(new AdView.MyCountdownClick() {
            @Override
            public void CountdownClick() {
                myView.cancelCountDownTimer();
                startActivity(new Intent(AdActivity.this, HomeActivity.class));
                finish();
            }
        });
        //设置默认的图片
        myView.showImage(R.mipmap.ic_launcher);
        //设置广告本身的点击事件
        myView.setMyImageClick(new AdView.MyAdClick() {
            @Override
            public void adClick() {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://www.baidu.com/"));
                startActivity(i);
                myView.cancelCountDownTimer();//取消掉倒计时的事件
            }
        });
        if (SdUtils.ExistSDCard(this)) {
            File file = new File(sdpath);
            if (!file.exists()) {
                file.mkdir();
            }
            HttpTool.downLoadFromUrl(url, file, new HttpTool.HttpDownLoadListener() {
                @Override
                public void onFinish(final File file) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            //设置倒计时的时间和是否显示几秒。false 为不显示
                            myView.setTime(5000, false);
                            if (url.endsWith(".gif")) {
                                myView.showGif(file);//显示gif
                            } else if (url.endsWith(".jpg") || url.endsWith(".png")) {
                                myView.showImage(file);//显示图片
                            } else {
                                myView.playVideo(file);//显示视频
                            }
                        }
                    });
                }

                @Override
                public void onError(Exception e) {
                    //直接跳过界面
                    startActivity(new Intent(AdActivity.this, HomeActivity.class));
                }
            });
        } else {
            //直接跳过界面
            startActivity(new Intent(AdActivity.this, HomeActivity.class));
        }
    }

    @Override
    protected void onPause() {
        //停止视频的播放
        myView.stopVideo();
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //如果点击了广告还要返回，可以在这里调用
        myView.resumeVideo();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //如果提前点击跳转按钮的时候需要取消倒计时
        myView.cancelCountDownTimer();
    }
}