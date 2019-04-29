package com.xxj.adsdk;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Toast;

import com.qq.e.o.ads.v2.ads.splash.SplashAD;
import com.qq.e.o.ads.v2.ads.splash.SplashADListener;
import com.qq.e.o.ads.v2.error.AdError;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AdImpl  {
    private static final String TAG = "AdImpl";
    public SplashAD splashAD;
    private Activity activity;
    private ViewGroup container;
    private Class clazz;
    private Runnable callback;
    private static AdImpl adImpl;

    private AdImpl(){}

    public static AdImpl getAdImpl(Activity activity, ViewGroup container, Class clazz){
        if(adImpl == null){
            adImpl = new AdImpl();
        }
        adImpl.activity = activity;
        adImpl.container = container;
        adImpl.clazz = clazz;
        return adImpl;
    }

    public void startAd(Runnable callback){
        this.callback = callback;
        if (Build.VERSION.SDK_INT >= 23) {
            checkAndRequestPermission();
        } else {
            // 如果是Android6.0以下的机器，默认在安装时获得了所有权限，可以直接调用SDK
            fetchSplashAD(activity, container, splashADListener);
        }
    }

    /**
     * ----------非常重要----------
     * <p>
     * Android6.0以上的权限适配简单示例：
     * <p>
     * 如果targetSDKVersion >= 23，那么必须要申请到所需要的权限，再调用广点通SDK，否则广点通SDK不会工作。
     * <p>
     * Demo代码里是一个基本的权限申请示例，请开发者根据自己的场景合理地编写这部分代码来实现权限申请。 注意：下面的`checkSelfPermission`和`requestPermissions`方法都是在Android6.0的SDK中增加的API，如果您的App还没有适配到Android6.0以上，则不需要调用这些方法，直接调用广告SDK即可。
     */
    @TargetApi(Build.VERSION_CODES.M)
    private void checkAndRequestPermission() {
        List<String> lackedPermission = new ArrayList<String>();

        List<String> needCheckPermissions = getNeedCheckPermissions();
        for (String permission : needCheckPermissions) {
            if (!(activity.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED)) {
                lackedPermission.add(permission);
            }
        }

        // 权限都已经有了，那么直接调用SDK
        if (lackedPermission.size() == 0) {
            fetchSplashAD(activity, container, splashADListener);
        } else {
            // 请求所缺少的权限，在onRequestPermissionsResult中再看是否获得权限，如果获得权限就可以调用SDK，否则不要调用SDK。
            String[] requestPermissions = new String[lackedPermission.size()];
            lackedPermission.toArray(requestPermissions);
            activity.requestPermissions(requestPermissions, 1024);
        }
    }

    private boolean hasAllPermissionsGranted(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1024 && hasAllPermissionsGranted(grantResults)) {
            fetchSplashAD(activity, container, splashADListener);
        } else {
            handleNoPermissions(permissions, grantResults);
        }
    }

    /**
     * 拉取开屏广告
     *
     * @param activity    展示广告的activity
     * @param adContainer 展示广告的大容器
     * @param adListener  广告状态监听器
     */
    private void fetchSplashAD(Activity activity, ViewGroup adContainer, SplashADListener adListener) {
        splashAD = new SplashAD(activity, adContainer, "sgame", "C1121", adListener);
    }

    private  SplashADListener splashADListener = new SplashADListener() {
        @Override
        public void onADDismissed() {
            Log.i(TAG, "SplashADDismissed");
        }

        @Override
        public void onNoAD(AdError adError) {
            Log.i(TAG, "LoadSplashADFail, eCode=" + adError.getErrorCode() + " msg:" + adError.getErrorMsg());
            /* 如果加载广告失败，则直接跳转 */
            next(activity, clazz);
        }

        @Override
        public void onADPresent() {
            if(callback != null){
                callback.run();
            }
            Log.i(TAG, "SplashADPresent");
        }

        @Override
        public void onADClicked() {
            Log.i(TAG, "SplashADClicked");
        }

        @Override
        public void onADSkip() {
            Log.i(TAG, "onADClose");
            next(activity, clazz);
        }

        @Override
        public void onADTimeOver() {
            Log.i(TAG, "onADTimeOver");
            next(activity, clazz);
        }
    };

    /**
     * 获取需要适配 Android 6.0 之后的权限
     *
     * @return 返回需要适配的权限
     */
    public List<String> getNeedCheckPermissions() {
        return Arrays.asList(
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        );
    }

    /**
     * 处理缺少权限的情况
     *
     * @param permissions  #onRequestPermissionsResult(int, String[], int[]) 中的第二个参数，请求检查的权限
     * @param grantResults #onRequestPermissionsResult(int, String[], int[]) 中的第三个参数，授权的结果
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    public void handleNoPermissions(String[] permissions, int[] grantResults) {
        // 如果用户没有授权，那么应该说明意图，引导用户去设置里面授权。
        Toast.makeText(activity, "应用缺少必要的权限！请点击\"权限\"，打开所需要的权限。", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + activity.getPackageName()));
        activity.startActivity(intent);
        activity.finish();
    }

    public static boolean mDisrupt = false;

    /**
     * 处理跳转下个页面
     */
    public static void next(Activity context, Class clazz) {

        if (mDisrupt) {
            context.startActivity(new Intent(context, clazz));
            context.finish();
        } else {
            mDisrupt = true;
        }
    }

    public void onResume() {
        if (mDisrupt) {
            next(activity, clazz);
        }
        mDisrupt = true;
    }

    public void onPause() {
        mDisrupt = false;
    }

}
