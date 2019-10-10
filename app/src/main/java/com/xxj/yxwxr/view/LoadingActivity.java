package com.xxj.yxwxr.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.kk.securityhttp.domain.ResultInfo;
import com.kk.utils.ToastUtil;
import com.kk.utils.VUiKit;
import com.xxj.yxwxr.R;
import com.xxj.yxwxr.SGameApplication;
import com.xxj.yxwxr.model.bean.InitInfo;
import com.xxj.yxwxr.model.bean.ProductInfo;
import com.xxj.yxwxr.model.engin.InitEngin;
import com.xxj.yxwxr.permission.BasePermissionActivity;
import com.yc.adsdk.core.AdCallback;
import com.yc.adsdk.core.AdType;
import com.yc.adsdk.core.Error;
import com.yc.adsdk.core.SAdSDK;


import butterknife.BindView;
import rx.Subscriber;


public class LoadingActivity extends BasePermissionActivity {


    @BindView(R.id.logo_image)
    ImageView logo_image;

    @BindView(R.id.active_image)
    FrameLayout active_image;

    @BindView(R.id.loading_iv_icon)
    ImageView loading_iv_icon;
    @BindView(R.id.loading_tv_title)
    TextView loading_tv_title;
    @BindView(R.id.loading_tv_btn)
    TextView loading_tv_btn;


    private int count = 5;
    private InitInfo initInfo;

    private ProductInfo loadingInfo;

    private boolean isGo = true;
    private int initCount = 0;
    private boolean isGoToMain = false;

    //是否强制跳转到主页面
    private boolean mForceGoMain;


    @Override
    protected boolean isFullScreen() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_loading;
    }

    @Override
    protected void initViews() {
//        BannerLayoutManager
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        logo_image.invalidate();

        setTitleName();



        /*VUiKit.postDelayed(10 * 1000, new Runnable() {  //影响广告的展示，部分广告直接在闪屏页用h5展示
            @Override
            public void run() {
                goToMain();
            }
        });*/

    }

    private void setTitleName() {
        String appName = "小游戏";
        try {
            PackageManager packageManager = getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            appName = getResources().getString(labelRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("555555555", "setTitleName: appName " + appName);
        loading_tv_title.setText(appName + "辅助外挂使用说明");
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

    }

    private Handler mHandler = new Handler();

    @Override
    protected void onRequestPermissionSuccess() {

        loading_tv_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPreferences2 = getSharedPreferences("data", Context.MODE_PRIVATE);
                boolean isNewUser = sharedPreferences2.getBoolean("isNewUser", false);
                if (!isNewUser) {
                    SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("isNewUser", true);
                    editor.commit();
                }

                if (!isNewUser) {
                    ShowNewUserAd();
                } else {
                    showAd();
                }
            }
        });
    }
    private boolean mIsShowing;

    private void showWatchVideoDialog() {
        if(mIsShowing){
            return;
        }
        mIsShowing=true;
        WatchVideoDialog watchVideoDialog = new WatchVideoDialog(LoadingActivity.this);
        watchVideoDialog.setCancelable(false);
        watchVideoDialog.show();
        watchVideoDialog.setOnClickBtnListener(new WatchVideoDialog.OnClickBtnListener() {
            @Override
            public void clickBtn(boolean isAffirm) {
                mIsShowing = false;
                if (isAffirm) {
                    showAd();
                }
            }
        });
    }

    private void ShowNewUserAd() {
        SAdSDK.getImpl().showAd(this, AdType.VIDEO_REWARD_V, new AdCallback() {
            @Override
            public void onDismissed() {
                Log.d("securityhttp", "ShowNewUserAd onDismissed: ");
                showWatchVideoDialog();
            }

            @Override
            public void onNoAd(Error error) {
                Log.d("securityhttp", "ShowNewUserAd onNoAd: getTripartiteCode " + error.getTripartiteCode() + " getMessage " + error.getMessage() + " getCode " + error.getCode());
                goToMain();
            }

            @Override
            public void onPresent() {

            }

            @Override
            public void onClick() {

            }
        });
    }


    private void showAd() {

        SAdSDK.getImpl().showAd(this, AdType.VIDEO_REWARD_V, new AdCallback() {
            @Override
            public void onDismissed() {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        goToMain();
                    }
                }, 200);
                Log.d("securityhttp", "SAdSDK onDismissed: ");

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
//                goToMain();
            }
        }, active_image);

    }


    private void switchMain(long delayTime) {
        VUiKit.postDelayed(delayTime, new Runnable() {
            @Override
            public void run() {
                goToMain();
            }
        });
    }

    private void goToMain() {
        if (!isGoToMain) {
            Intent intent = new Intent(LoadingActivity.this, MainActivity.class);
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
