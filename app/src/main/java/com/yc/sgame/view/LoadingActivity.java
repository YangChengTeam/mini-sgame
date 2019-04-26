package com.yc.sgame.view;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.jakewharton.rxbinding3.view.RxView;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.kk.utils.LogUtil;
import com.kk.utils.ToastUtil;
import com.yc.sgame.R;
import com.yc.sgame.SGameApplication;
import com.yc.sgame.helper.GlideHelper;
import com.yc.sgame.model.bean.InitInfo;
import com.yc.sgame.model.bean.ProductInfo;
import com.yc.sgame.model.engin.InitEngin;


import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;



public class LoadingActivity extends BaseActivity {

    @BindView(R.id.active_image)
    ImageView active_image;

    @BindView(R.id.logo_image)
    ImageView logo_image;

    @BindView(R.id.skip_view)
    TextView skip_view;

    private int count = 5;
    private InitInfo initInfo;

    private ProductInfo loadingInfo;

    private boolean isGo;
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
        skip_view.setVisibility(View.VISIBLE);
        new InitEngin(this).init().subscribe(new Subscriber<ResultInfo<InitInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(final ResultInfo<InitInfo> resultInfo) {
                if(resultInfo.getCode() == 1) {
                    initInfo =  resultInfo.getData();

                    if(initInfo == null) {
                        if(++initCount < 3) {
                            initData();
                        } else {
                            ToastUtil.toast2(LoadingActivity.this, "初始化失败");
                        }
                        return;
                    }

                    SGameApplication sGameApplication =  SGameApplication.getSGameApplication();
                    sGameApplication.initInfo = initInfo;
                    loadingInfo = initInfo.getInit_img();

                    GlideHelper.imageView(getBaseContext(), active_image, loadingInfo.getImage(), 0);
                    logo_image.setVisibility(View.GONE);
                }
            }
        });

        RxView.clicks(skip_view).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(view-> {
            nav2MainActivity();
        });

        RxView.clicks(active_image).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(view-> {
            if(initInfo == null) return;
            SGameApplication.getSGameApplication().loadingInfo = loadingInfo;
            nav2MainActivity();
        });

        Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(count)
                .observeOn(AndroidSchedulers.mainThread())
                .map(aLong -> count-aLong)
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {
                        nav2MainActivity();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Long aLong) {
                        skip_view.setText("跳过(" + (aLong) + "s)");
                    }
                });
    }


    public void nav2MainActivity(){
        if(!isGo) {
            isGo = true;
            startActivity(new Intent(LoadingActivity.this, MainActivity.class));
            finish();
        }
    }



    /**
     * 开屏页一定要禁止用户对返回按钮的控制，否则将可能导致用户手动退出了App而广告无法正常曝光和计费
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
