package cn.utsoft.commoms.simple;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class StartActivity extends AppCompatActivity {
    private String videoUrl = "http://www.51hfzs.cn/123.mp4"; //视频的下载地址
    private String gifUrl = "http://photocdn.sohu.com/20150808/mp26389744_1439008079309_5.gif";//gif的下载地址, http://photo.l99.com/bigger/00/1425373097998_utt83i.gif
    private String imageUrl = "http://4493bz.1985t.com/uploads/allimg/150127/4-15012G52133.jpg";//图片的请求地址  先返回json 数据里面包含了链接

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        findViewById(R.id.btn_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, AdActivity.class);
                startActivity(intent);
            }
        });

    }
}
