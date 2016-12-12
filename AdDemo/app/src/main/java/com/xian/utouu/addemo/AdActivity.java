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
    private String gifUrl = "http://photocdn.sohu.com/20150808/mp26389744_1439008079309_5.gif";//gif的下载地址, http://photo.l99.com/bigger/00/1425373097998_utt83i.gif
    private String imageUrl = "http://4493bz.1985t.com/uploads/allimg/150127/4-15012G52133.jpg";//图片的请求地址  先返回json 数据里面包含了链接
    private Uri uri;
    private String sdpath = Environment.getExternalStorageDirectory() + "/adVideo"; // 视频和gif在SD卡中的目录

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        //服务器需要返回的数据为  1. 视频、图片、gif 的下载（显示）地址 2.显示哪种类型的广告

        //调用服务器的接口获取显示那种类型的广告，这里是只是做演示从上个界面传递过来的
        int which = getIntent().getIntExtra("which", 0);

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
                finish();
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

        switch (which) {
            case 1:
                // * R.mipmap.welcome   app资源文件的的id    * imageUrl  下载的链接
                myView.showImage(R.mipmap.welcome, imageUrl);
                break;
            case 2:
                // *  uri 本地raw种视频文件的地址 *  videoUrl 更新视频的地址 * sdpath   视频保存在SD卡中的路径
                    myView.playVideo( uri, videoUrl, sdpath);
                break;
            case 3:
                //  * R.raw.hh app中的资源  * gifUrl gif的下载地址 * sdpath   保存在SD卡中的路径
                    myView.showGif(R.raw.hh, gifUrl, sdpath);
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