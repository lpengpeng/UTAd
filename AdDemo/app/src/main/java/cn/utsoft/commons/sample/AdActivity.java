package cn.utsoft.commons.sample;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import cn.utsoft.commons.ADView.UTAdView;


public class AdActivity extends AppCompatActivity {
    private UTAdView myView;
    //gif的下载地址, http://photo.l99.com/bigger/00/1425373097998_utt83i.gif
    private String videoUrl = "http://www.51hfzs.cn/123.mp4"; //视频的下载地址
    private String gifUrl = "http://photocdn.sohu.com/20150808/mp26389744_1439008079309_5.gif";
    private String imageUrl = "http://4493bz.1985t.com/uploads/allimg/150127/4-15012G52133.jpg";
    // 视频和gif在SD卡中的目录
    private String sdpath = Environment.getExternalStorageDirectory() + "/adVideo";
    private String url;
    private String adUrl = "https://www.baidu.com/";
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        //获取控件
        myView = (UTAdView) findViewById(R.id.myView_main);
        //获取跳转按钮
        Button jumpButton = myView.getJumpButton();
        //设置按钮背景
        jumpButton.setBackgroundResource(R.mipmap.jump);
        //设置跳转按钮的点击事件
        myView.setMyClick(new UTAdView.MyCountdownClick() {
            @Override
            public void CountdownClick() {
                startActivity(new Intent(AdActivity.this, HomeActivity.class));
                finish();
            }
        });
        //设置广告点击后监听
        myView.setMyAdClick(new UTAdView.MyAdClick() {
            @Override
            public void AdClick() {
                myView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 1000);

            }
        });
        //加载view
        myView.initAdData(this, 0,R.mipmap.ic_launcher, sdpath, "c29zp4mKTsGOfe638kbv5Q", "U-wL7_B2S6-gY-9zsggMuQ", 5000, HomeActivity.class);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        myView.cancelCountDownTimer();
    }
}