package com.xxj.yxwxr.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;

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
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;


public abstract class BaseActivity extends AppCompatActivity {
    public MyLoadingDialog dlg = null;
    public IWXAPI api;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            // Activity was brought to front and not created,
            // Thus finishing this will get us to the last viewed activity
            finish();
            return;
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
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
                download(productInfo);
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

}
