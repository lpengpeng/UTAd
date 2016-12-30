package cn.utsoft.commons.sample;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import cn.utsoft.commons.ADView.UTAdView;
import pub.devrel.easypermissions.EasyPermissions;


public class AdActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks{
    private UTAdView myView;
    // SD卡中的保存路径
    private String sdpath = Environment.getExternalStorageDirectory() + "/adVideo";
    private static final int REQUEST_CODE_PERMISSION_AD = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showAdView();
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

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        initView();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (requestCode == REQUEST_CODE_PERMISSION_AD) {
            Toast.makeText(this, "您拒绝了读取sd卡的相关权限!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    private void showAdView() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};
        if (!EasyPermissions.hasPermissions(this, perms)) {
            EasyPermissions.requestPermissions(this, "该应用需要读写sd卡权限", REQUEST_CODE_PERMISSION_AD, perms);
        }else {
            initView();
        }
    }
}