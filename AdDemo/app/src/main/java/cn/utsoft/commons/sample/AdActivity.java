package cn.utsoft.commons.sample;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import cn.utsoft.commons.ADView.UTAdView;


public class AdActivity extends AppCompatActivity {
    private UTAdView myView;
    // SD卡中的保存路径
    private String sdpath = Environment.getExternalStorageDirectory() + "/adVideo";

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