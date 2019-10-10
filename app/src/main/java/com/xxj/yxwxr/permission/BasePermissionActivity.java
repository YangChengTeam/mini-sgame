package com.xxj.yxwxr.permission;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Toast;


import com.xxj.yxwxr.view.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于API23 Android6.0以上权限校验的Activity基类
 * 校验时只校验必须权限，请求时请求全部权限
 */
public abstract class BasePermissionActivity extends BaseActivity {

    private String TAG = "GameSdkLog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 如果targetSDKVersion >= 23，就要申请好权限。如果您的App没有适配到Android6.0（即targetSDKVersion < 23），那么只需要在这里直接调用fetchSplashAD接口。
        if (Build.VERSION.SDK_INT >= 23) {
            checkAndRequestPermission();
        } else {
            onRequestPermissionSuccess();
        }
    }

    /**
     * ----------非常重要----------
     * <p>
     * Android6.0以上的权限适配简单示例：
     * <p>
     * 如果targetSDKVersion >= 23，那么必须要申请到所需要的权限，再调用广点通SDK，否则广点通SDK不会工作。
     * <p>
     * Demo代码里是一个基本的权限申请示例，请开发者根据自己的场景合理地编写这部分代码来实现权限申请。
     * 注意：下面的`checkSelfPermission`和`requestPermissions`方法都是在Android6.0的SDK中增加的API，如果您的App还没有适配到Android6.0以上，则不需要调用这些方法，直接调用广点通SDK即可。
     */
    @TargetApi(23)
    private void checkAndRequestPermission() {
        boolean obtainMust = PermissionHelpUtils.checkMustPermissions(this);
        if (obtainMust) {
            onRequestPermissionSuccess();
        } else {
            List<String> allPermissions = new ArrayList<>();
            List<String> mustPermissions = PermissionHelpUtils.getMustPermissions();
            List<String> notMustPermissions = PermissionHelpUtils.getNotMustPermissions();
            allPermissions.addAll(mustPermissions);
            allPermissions.addAll(notMustPermissions);
            // 请求所缺少的权限，在onRequestPermissionsResult中再看是否获得权限，如果获得权限就可以调用SDK，否则不要调用SDK。
            String[] requestPermissions = new String[allPermissions.size()];
            allPermissions.toArray(requestPermissions);
            requestPermissions(requestPermissions, 1024);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1024 && PermissionHelpUtils.checkMustPermissions(this)) {
            onRequestPermissionSuccess();
        } else {
            onRequestPermissionError();
            // 如果用户没有授权，那么应该说明意图，引导用户去设置里面授权。
            Toast.makeText(this, "应用缺少必要的权限！请点击\"权限\"，打开所需要的权限。", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
            finish();
        }
    }

    /**
     * 权限请求失败的回调函数
     */
    protected void onRequestPermissionError() {
    }

    /**
     * 权限请求成功的回调函数
     */
    protected abstract void onRequestPermissionSuccess();
}

