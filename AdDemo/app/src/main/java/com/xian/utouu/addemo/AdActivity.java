package com.xian.utouu.addemo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;

import com.xian.utouu.adlibrary.AdView;

public class AdActivity extends AppCompatActivity {
    AdView myView;
    private int type;
    private boolean isDownLoad = true;
    private String videoUrl = "http://www.51hfzs.cn/123.mp4";
    private String gifUrl = "http://photo.l99.com/bigger/00/1425373097998_utt83i.gif";
    private String imageUrl = "http://www.51hfzs.cn/data.json";//图片下载地址

    private Uri uri;
    private String sdpath = Environment.getExternalStorageDirectory()+ "/adVideo";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        int which = getIntent().getIntExtra("which", 0);
        type = getIntent().getIntExtra("type", 0);
       String localVideo = "android.resource://" + this.getPackageName() + "/" + R.raw.guide_1;
        uri = Uri.parse(localVideo);
        myView = (AdView) findViewById(R.id.myView_main);
        myView.setTime(5000, 1000, false);
        myView.goWhere(HomeActivity.class);
        myView.setMyClick(new AdView.MyCountdownClick() {
            @Override
            public void CountdownClick() {
                myView.cancelCountDownTimer();
                startActivity(new Intent(AdActivity.this, HomeActivity.class));
            }
        });
        myView.setMyImageClick(new AdView.MyAdClick() {
            @Override
            public void adClick() {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://www.baidu.com/"));
                startActivity(i);
                myView.cancelCountDownTimer();
            }
        });
        switch (which) {
            case 1:
                myView.showImage(isDownLoad, R.mipmap.welcome, imageUrl);
                break;
            case 2:
                myView.playVideo(isDownLoad, uri, type, videoUrl,sdpath);
                break;
            case 3:
                myView.showGif(isDownLoad, type, R.raw.hh, gifUrl,sdpath);
                break;
        }
    }


    @Override
    protected void onPause() {
        myView.stopVideo();
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        myView.resumeVideo();
//        myView.playVideo(isDownLoad, uri, type, videoUrl);
//        myView.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                finish();
//                startActivity(new Intent(AdActivity.this, HomeActivity.class));
//            }
//        },1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myView.cancelCountDownTimer();
    }
}