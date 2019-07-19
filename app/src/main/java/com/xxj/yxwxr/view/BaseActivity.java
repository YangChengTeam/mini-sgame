package com.xxj.yxwxr.view;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.kk.securityhttp.domain.GoagalInfo;
import com.kk.utils.LogUtil;
import com.kk.utils.PathUtil;
import com.kk.utils.security.Md5;
import com.liulishuo.okdownload.DownloadListener;
import com.liulishuo.okdownload.DownloadTask;
import com.liulishuo.okdownload.OkDownload;
import com.liulishuo.okdownload.StatusUtil;
import com.liulishuo.okdownload.core.breakpoint.BreakpointInfo;
import com.liulishuo.okdownload.core.cause.EndCause;
import com.liulishuo.okdownload.core.cause.ResumeFailedCause;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;
import com.xxj.yxwxr.R;
import com.xxj.yxwxr.model.bean.ProductInfo;
import com.xxj.yxwxr.model.engin.ClickEngin;
import com.xxj.yxwxr.view.widget.MyLoadingDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;


public abstract class BaseActivity extends AppCompatActivity {
    public MyLoadingDialog dlg = null;
    public IWXAPI api;
    private ProductInfo downloadProductInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            // Activity was brought to front and not created,
            // Thus finishing this will get us to the last viewed activity
            finish();
            return;
        }

        if (isFullScreen()) {
            //无title
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            //全屏
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            if (getSupportActionBar() != null) {
                getSupportActionBar().hide();
            }
        }
        setContentView(getLayoutId());



        ButterKnife.bind(this);
        if (findViewById(R.id.v_status_bar) != null) {
            setstatusBarHeight(findViewById(R.id.v_status_bar));
        }
        dlg = new MyLoadingDialog(this);
        setTranslucentStatus();
        initViews();
        initData();

    }

    protected boolean isFullScreen() {
        return false;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

    }

    public void showLoadingDialog(String message) {
        dlg.show(message);
    }

    public void dissmissLoadingDialog() {
        dlg.dismiss();
    }

    protected void setTranslucentStatus() {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(67108864);
            window.getDecorView().setSystemUiVisibility(1280);
            window.addFlags(Integer.MIN_VALUE);
            window.setStatusBarColor(0);
        } else if (Build.VERSION.SDK_INT >= 19) {
            getWindow().addFlags(67108864);
        }
    }

    protected abstract int getLayoutId();

    protected abstract void initViews();

    protected void initData() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void setstatusBarHeight(View v) {
        int statusBarHeight = 50;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        v.getLayoutParams().width = -1;
        v.getLayoutParams().height = statusBarHeight;
        v.setLayoutParams(v.getLayoutParams());
    }

    public void nav2(ProductInfo productInfo) {
        if (productInfo != null) {
            if (productInfo.getType().equals("2")) {
                nav2H5(productInfo);
            } else if (productInfo.getType().equals("3")) {
                if (Build.VERSION.SDK_INT >= 23) {
                    this.downloadProductInfo = productInfo;
                    if (checkAndRequestPermission()) {
                        download(productInfo);
                    }
                } else {
                    download(productInfo);
                }
            } else {
                nav2MiniProgram(productInfo);
            }
        }
    }

    public void nav2H5(ProductInfo productInfo) {
        Intent intent = new Intent(this, WebActivity.class);
        intent.putExtra("title", productInfo.getName());
        intent.putExtra("url", productInfo.getGame_jump_path());
        intent.putExtra("id", productInfo.getId());
        startActivity(intent);
    }

    private ProgressDialog progressDialog;

    public void download(ProductInfo productInfo) {

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle(getString(R.string.app_name));
        progressDialog.setMessage("正在下载" + productInfo.getName());
        progressDialog.setIcon(R.mipmap.ic_launcher);
        progressDialog.setCanceledOnTouchOutside(false);

        String path = PathUtil.createDir(this, "/download");
        File file = new File(path + "/" + Md5.md5(productInfo.getName()) + ".apk");

        DownloadTask.Builder builder = new DownloadTask.Builder(productInfo.getGame_jump_path(), file);
        DownloadTask task = builder.build();
        task.enqueue(new DownloadListener() {
            @Override
            public void taskStart(@NonNull DownloadTask task) {

            }

            @Override
            public void connectTrialStart(@NonNull DownloadTask task, @NonNull Map<String, List<String>> requestHeaderFields) {

            }

            @Override
            public void connectTrialEnd(@NonNull DownloadTask task, int responseCode, @NonNull Map<String, List<String>> responseHeaderFields) {

            }

            @Override
            public void downloadFromBeginning(@NonNull DownloadTask task, @NonNull BreakpointInfo info, @NonNull ResumeFailedCause cause) {
                progressDialog.show();
            }

            @Override
            public void downloadFromBreakpoint(@NonNull DownloadTask task, @NonNull BreakpointInfo info) {

            }

            @Override
            public void connectStart(@NonNull DownloadTask task, int blockIndex, @NonNull Map<String, List<String>> requestHeaderFields) {

            }

            @Override
            public void connectEnd(@NonNull DownloadTask task, int blockIndex, int responseCode, @NonNull Map<String, List<String>> responseHeaderFields) {

            }

            @Override
            public void fetchStart(@NonNull DownloadTask task, int blockIndex, long contentLength) {

            }

            @Override
            public void fetchProgress(@NonNull DownloadTask task, int blockIndex, long increaseBytes) {
                progressDialog.setProgress((int) (((float) StatusUtil.getCurrentInfo(task).getTotalOffset() / StatusUtil.getCurrentInfo(task).getTotalLength()) * 100));
            }

            @Override
            public void fetchEnd(@NonNull DownloadTask task, int blockIndex, long contentLength) {
                progressDialog.dismiss();
            }

            @Override
            public void taskEnd(@NonNull DownloadTask task, @NonNull EndCause cause, @Nullable Exception realCause) {
                Uri apkUri = getUriFromFile(BaseActivity.this, file);
                Intent installIntent = new Intent(Intent.ACTION_VIEW);
                installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                installIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                installIntent.setDataAndType(apkUri, "application/vnd.android.package-archive");
                startActivity(installIntent);
            }
        });
    }

    public void nav2MiniProgram(ProductInfo productInfo) {

        String appId = "wx83c6906045f5c5f8";
        api = WXAPIFactory.createWXAPI(this, appId, true);
        if (!api.isWXAppInstalled()) {
            productInfo.setWx_open_status("2");
            new ClickEngin(this).click(productInfo).subscribe();
            return;
        }
        String path = productInfo.getJump_path();
        if (!TextUtils.isEmpty(path)) {
            String prefix = "?";
            if (path.contains("?")) {
                prefix = "&";
            }
            path += prefix + "game_id=" + productInfo.getGame_id() + "&imeil=" + GoagalInfo.get().uuid + "&xcx_id=" + productInfo.getXcx_id();
        }

        WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
        req.userName = productInfo.getOrigin_id();
        req.path = path;
        req.miniprogramType = productInfo.getRelease_status();

        productInfo.setPath(path);

        if (api.sendReq(req)) {
            productInfo.setWx_open_status("1");
            new ClickEngin(this).click(productInfo).subscribe();
        } else {
            productInfo.setWx_open_status("3");
            new ClickEngin(this).click(productInfo).subscribe();
        }
    }

    protected Uri getUriFromFile(Context context, File file) {
        Uri uri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = getUriFromFileForN(context, file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }

    protected Uri getUriFromFileForN(Context context, File file) {
        Uri fileUri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", file);
        return fileUri;
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
    @TargetApi(Build.VERSION_CODES.M)
    private boolean checkAndRequestPermission() {
        List<String> lackedPermission = new ArrayList<String>();

        if (!(checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED)) {
            lackedPermission.add(Manifest.permission.READ_PHONE_STATE);
        }

        if (!(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            lackedPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        /*if (!(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            lackedPermission.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }*/

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
            if (downloadProductInfo != null) {
                download(downloadProductInfo);
            }
        } else {
            // 如果用户没有授权，那么应该说明意图，引导用户去设置里面授权。
            Toast.makeText(this, "应用缺少必要的权限！请点击\"权限\"，打开所需要的权限。", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            this.startActivity(intent);
//            finish();
        }
    }


}
