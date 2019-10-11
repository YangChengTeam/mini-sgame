package com.xxj.yxwxr.view;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import com.xxj.yxwxr.R;
import com.yc.adsdk.core.AdCallback;
import com.yc.adsdk.core.AdType;
import com.yc.adsdk.core.Error;
import com.yc.adsdk.core.SAdSDK;


public class SplashActivity extends BaseActivity {


    //是否强制跳转到主页面
    private boolean mForceGoMain;
    private boolean isGoToMain = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initViews();
    }

    @Override
    protected boolean isFullScreen() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initViews() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showAd();
            }
        }, 700);
    }

    @Override
    protected void initData() {
        super.initData();
    }

    private void showAd() {
        SAdSDK.getImpl().showAd(this, AdType.SPLASH, new AdCallback() {
            @Override
            public void onDismissed() {
                goToMain();

            }

            @Override
            public void onNoAd(Error error) {
                Log.d("securityhttp", "SAdSDK onNoAd: getTripartiteCode " + error.getTripartiteCode() + " getMessage " + error.getMessage() + " getCode " + error.getCode());
                goToMain();
            }


            @Override
            public void onPresent() {

            }

            @Override
            public void onClick() {
                Log.d("securityhttp", "SAdSDK onClick: ");
                mForceGoMain = true;
            }
        });

    }

    private void goToMain() {
        if (!isGoToMain) {
            Intent intent = new Intent(SplashActivity.this, LoadingActivity.class);
            startActivity(intent);
            isGoToMain = true;
            finish();
        }
    }

    @Override
    protected void onResume() {
        if (mForceGoMain) {
            goToMain();
        }
        super.onResume();
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

}
