package com.xxj.yxwxr.view;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.kk.securityhttp.domain.ResultInfo;
import com.kk.utils.LogUtil;
import com.kk.utils.ToastUtil;
import com.kk.utils.VUiKit;
import com.qq.e.ads.splash.SplashAD;
import com.qq.e.ads.splash.SplashADListener;
import com.qq.e.comm.util.AdError;
import com.xxj.yxwxr.R;
import com.xxj.yxwxr.SGameApplication;
import com.xxj.yxwxr.model.bean.InitInfo;
import com.xxj.yxwxr.model.bean.ProductInfo;
import com.xxj.yxwxr.model.engin.InitEngin;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.Subscriber;


public class LoadingActivity extends BaseActivity {


    @BindView(R.id.logo_image)
    ImageView logo_image;


    private int count = 5;
    private InitInfo initInfo;

    private ProductInfo loadingInfo;

    private boolean isGo = true;
    private int initCount = 0;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_loading;
    }

    @Override
    protected void initViews() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    @Override
    protected void initData() {
        new InitEngin(this).init().subscribe(new Subscriber<ResultInfo<InitInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(final ResultInfo<InitInfo> resultInfo) {
                if (resultInfo.getCode() == 1) {
                    initInfo = resultInfo.getData();

                    if (initInfo == null) {
                        if (++initCount < 3) {
                            initData();
                        } else {
                            ToastUtil.toast2(LoadingActivity.this, "初始化失败");
                        }
                        return;
                    }
                    SGameApplication sGameApplication = SGameApplication.getSGameApplication();
                    sGameApplication.initInfo = initInfo;
                }
            }
        });


        if (checkAndRequestPermission()) {
            showAd();
        }
    }


    private void showAd() {
        splashAD = new SplashAD(this, findViewById(R.id.active_image), "1108883294", "7000163086068345", new SplashADListener() {
            @Override
            public void onADDismissed() {
                switchMain(0);
            }

            @Override
            public void onNoAD(AdError adError) {
                LogUtil.msg("onNoAD" + adError.getErrorCode());
                if (isGo) {
                    VUiKit.postDelayed(2000, new Runnable() {
                        @Override
                        public void run() {
                            isGo = true;
                            showAd();
                        }
                    });
                }
                isGo = false;
            }

            @Override
            public void onADPresent() {
                LogUtil.msg("onADPresent");
                switchMain(5000);
            }

            @Override
            public void onADClicked() {
                switchMain(0);
            }

            @Override
            public void onADTick(long l) {
            }

            @Override
            public void onADExposure() {
            }
        });
    }


    private void switchMain(long delayTime) {
        VUiKit.postDelayed(delayTime, new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(LoadingActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    //防止用户返回键退出 APP
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                || keyCode == KeyEvent.KEYCODE_HOME) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    SplashAD splashAD;

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
    @TargetApi(Build.VERSION_CODES.M)
    private boolean checkAndRequestPermission() {
        List<String> lackedPermission = new ArrayList<String>();

        if (!(checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED)) {
            lackedPermission.add(Manifest.permission.READ_PHONE_STATE);
        }

        if (!(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            lackedPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            lackedPermission.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        // 权限都已经有了，那么直接调用SDK
        if (lackedPermission.size() == 0) {
            return true;
        } else {
            // 请求所缺少的权限，在onRequestPermissionsResult中再看是否获得权限，如果获得权限就可以调用SDK，否则不要调用SDK。
            String[] requestPermissions = new String[lackedPermission.size()];
            lackedPermission.toArray(requestPermissions);
            requestPermissions(requestPermissions, 1024);
            return false;
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (hasAllPermissionsGranted(grantResults)) {
            showAd();
        } else {
            // 如果用户没有授权，那么应该说明意图，引导用户去设置里面授权。
            Toast.makeText(this, "应用缺少必要的权限！请点击\"权限\"，打开所需要的权限。", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            this.startActivity(intent);
            finish();
        }
    }
}
