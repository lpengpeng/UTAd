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
    private boolean isDownLoad = true;//是否需要去更新数据
    private String videoUrl = "http://www.51hfzs.cn/123.mp4"; //视频的下载地址
    private String gifUrl = "http://photo.l99.com/bigger/00/1425373097998_utt83i.gif";//gif的下载地址
    private String imageUrl = "http://www.51hfzs.cn/data.json";//图片下载地址
    private Uri uri;
    private String sdpath = Environment.getExternalStorageDirectory() + "/adVideo"; // 视频和gif在SD卡中的目录

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        //服务器需要返回的数据为 1.是否需要更新数据的标记 isDownLoad  2. 视频、图片、gif 的下载地址 3.SD卡的保存目录  4.显示哪种类型的广告 ，5 显示APP资源目录还是Sd卡中的标记

        //调用服务器的接口获取显示那种类型的广告
        int which = getIntent().getIntExtra("which", 0);

        //调用服务器获取该类型下的App中还是SD卡中的
        type = getIntent().getIntExtra("type", 0);

        //获取本地视频的路径
        String localVideo = "android.resource://" + this.getPackageName() + "/" + R.raw.guide_1;
        uri = Uri.parse(localVideo);

        //获取控件
        myView = (AdView) findViewById(R.id.myView_main);

        //设置倒计时的时间和是否显示几秒。false 为不显示
        myView.setTime(5000, false);

        //设置跳转按钮
        myView.goWhere(HomeActivity.class);

        //设置跳转按钮的背景
        myView.setJumpButtonBackgound(R.mipmap.jump);

        //设置跳转按钮的字体颜色
//        myView.setJumpButtonTextColor(0xf16060);

        //设置按钮的位置的参数
//        myView.setJumpButtonParams(null);

        //设置跳转按钮的点击事件
        myView.setMyClick(new AdView.MyCountdownClick() {
            @Override
            public void CountdownClick() {
                myView.cancelCountDownTimer();
                startActivity(new Intent(AdActivity.this, HomeActivity.class));
            }
        });

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

        //根据服务器的返回数据跳转显示对应的广告，同时传入是否更新数据的标记(isDownLoad),并传入下载链接（imageUrl）
        switch (which) {
            case 1:
                // @param isDownload  是否需要下载新的数据
                // @param R.mipmap.welcome   app资源文件的的id
                // @param imageUrl 下载的链接
                //该图片保存在应用缓存文件中 不用传路径
                myView.showImage(isDownLoad, R.mipmap.welcome, imageUrl);
                break;
            case 2:
                //显示视频
                // @param isDownload 是否去更新数据
                // @param uri  本地raw种视频文件的地址
                // @param type  显示app或者SD卡的标记
                // @param videoUrl 更新视频的地址
                // @param sdpath 视频保存在SD卡中的路径
                myView.playVideo(isDownLoad, uri, type, videoUrl, sdpath);
                break;
            case 3:
                //显示gif
                // @param isDownload 是否去更新数据
                // @param type 显示的gif类型
                // @param R.raw.hh app中raw文件下的资源
                // @param gifUrl gif的下载地址
                // @param sdpath 保存在SD卡中的路径
                myView.showGif(isDownLoad, type, R.raw.hh, gifUrl, sdpath);
                break;
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

        /*********************下面为一种情况***********************/
// 停留一秒钟然后跳到主页
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
        //如果提前点击跳转按钮的时候需要取消倒计时
        myView.cancelCountDownTimer();
    }
}