package com.xxj.yxwxr;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.kk.securityhttp.domain.GoagalInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.kk.utils.FileUtil;
import com.kk.utils.LogUtil;
import com.umeng.commonsdk.UMConfigure;
import com.xxj.yxwxr.model.bean.ChannelInfo;
import com.xxj.yxwxr.model.bean.InitInfo;
import com.xxj.yxwxr.model.bean.ProductInfo;
import com.yc.adsdk.core.Config;
import com.yc.adsdk.tt.STtAdSDk;
import com.yc.adsdk.tt.config.TTConfig;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


public class SGameApplication extends Application {
    public Map<String, String> deaultParams;

    private static SGameApplication SGameApplication;
    public ChannelInfo channelInfo;
    public InitInfo initInfo;
    public ProductInfo loadingInfo;

    public static SGameApplication getSGameApplication() {
        return SGameApplication;
    }



    @Override
    public void onCreate() {
        super.onCreate();
        hideShowDialog();
        SGameApplication = this;

        hideShowDialog();


//        new Thread(()->{
//            inits();
//        }).start();
        inits();
        initTtAd();
    }


    private void inits() {
        // 初始化http
        initHttpInfo();

    }

    private void initTtAd() {
        STtAdSDk sTtAdSDk = STtAdSDk.getImpl();
        Config config = new Config();
        TTConfig ttConfig = new TTConfig();
        ttConfig.setTtAdInsterDownload("901121417");
        ttConfig.setTtAdInsterNormal("901121725");
        ttConfig.setTtAdVideoHorizontal("901121375");
        ttConfig.setTtAdVideoVertical("901121375");
        ttConfig.setTtAdVideoNative("901121709");
        ttConfig.setTtAdbanner("901121987");
        ttConfig.setTtAdbannerDownload("901121895");
        ttConfig.setTtAdbannerNative("901121423");
        ttConfig.setTtAppName("外星人游戏圈_android");
        ttConfig.setTtAdSplash("823543759");
        ttConfig.setTtAdVideoRewardHorizontal("923543786");
        ttConfig.setTtAdVideoReward("923543786");
        config.setExt(ttConfig);
        config.setAppId("5023543");

        sTtAdSDk.init(this, config);
    }

    private void initHttpInfo() {
        ApplicationInfo appinfo = getApplicationInfo();
        String sourceDir = appinfo.sourceDir;
        ZipFile zf = null;
        try {
            zf = new ZipFile(sourceDir);
            ZipEntry ze = zf.getEntry("META-INF/channelconfig.json");
            InputStream in = zf.getInputStream(ze);
            String result = FileUtil.readString(in);
            channelInfo = JSON.parseObject(result, ChannelInfo.class);
            LogUtil.msg("渠道来源->" + result);
        } catch (Exception e) {
            LogUtil.msg("apk中channelconfig.json文件不存在", LogUtil.W);
        } finally {
            if (zf != null) {
                try {
                    zf.close();
                } catch (IOException e2) {
                    // TODO Auto-generated catch block
                    e2.printStackTrace();
                }
            }
        }
        GoagalInfo.get().init(getApplicationContext());
        HttpConfig.setPublickey("-----BEGIN PUBLIC KEY-----\n" +
                "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAsbCfry4FyXGpkQ63+POA\n" +
                "DQOCWVLG1t1sHUUa8xJgC6JcewIcdgQpywlxOoCVJJwpJa9GfWA2Y6nz4wZo5dv5\n" +
                "7O7hDVBKuO1BZnRNr+t2XJgMJVVjimUfQ7xumk2NtbuUk0v7OrsMmnL9/NG0HXCO\n" +
                "QelsLJnbBIp9B9dZr0dNp9ZC+c+cpY8QQwfq7i86IrykF4zd16xHqJjioh24KV+Y\n" +
                "ZhkEqRV2QVgNPVzFen6udMc2MAJPupw99tCbM7JrKF8ojg2CqEGn95WmIX5biIDR\n" +
                "SpxU+Ovj/0DymLT5fhpYjcABlhkyRRbTCk1Uf0kLsyc/sZxskzCbeNfChlbTAZoV\n" +
                "jTqu8b86BKhxbiIHVBKwg5tkb+nFCY/6jfRxacNpa6A/nz/bYREIJlmfsq0aDZEw\n" +
                "Dbm+v5byevt/oJ2KL6o5ZtR2fF8K5J9HYXYIFmQhECJ3GqrIFpAlVRiOgefYg5Bq\n" +
                "dUJ1eHhPe5SR1f4GnRMkyld38N6xJhq40XF49838oqKnysKyszok3Mgyxq6Jp4Q3\n" +
                "t0b0GdUMCLbJ4Xho+/ZYaCDkU69J7W2OIUFRsbxQphtvvcPem2veAccYtxhGpWq0\n" +
                "LoErhjPtGkrWzbP9cmjPs4LKHGvDDG6144yMLHn496qVIpG4U8Aisj1ldKvjQ/ro\n" +
                "76OY9qY3UefnLVd3DvCN13cCAwEAAQ==\n" +
                "-----END PUBLIC KEY-----");
        //设置http默认参数
        String agent_id = "1";
        Map<String, String> params = new HashMap<>();
        if (GoagalInfo.get().channelInfo != null && GoagalInfo.get().channelInfo.agent_id != null) {
            params.put("from_id", GoagalInfo.get().channelInfo.from_id + "");
            params.put("author", GoagalInfo.get().channelInfo.author + "");
            agent_id = GoagalInfo.get().channelInfo.agent_id;
        }
        params.put("agent_id", agent_id);
        params.put("ts", System.currentTimeMillis() + "");
        params.put("device_type", "2");
        params.put("imeil", GoagalInfo.get().uuid);
        String sv = Build.MODEL.contains(Build.BRAND) ? Build.MODEL + " " + Build.VERSION.RELEASE : Build.BRAND + " " + Build.MODEL + " " + Build.VERSION.RELEASE;
        params.put("sv", sv);

        if (channelInfo != null) {
            params.put("site_id", channelInfo.getSite_id() + "");
            params.put("soft_id", channelInfo.getSoft_id() + "");
            params.put("referer_url", channelInfo.getNode_url() + "");
        }
        if (GoagalInfo.get().packageInfo != null) {
            params.put("app_version", GoagalInfo.get().packageInfo.versionCode + "");
        }

        deaultParams = params;
        HttpConfig.setDefaultParams(params);

        // 友盟统计
        UMConfigure.init(this, "5c9aeda461f5649183000975", "WebStore" + agent_id, UMConfigure.DEVICE_TYPE_PHONE, null);

    }

    private void hideShowDialog() {
        try {
            Class aClass = Class.forName("android.content.pm.PackageParser$Package");
            Constructor declaredConstructor = aClass.getDeclaredConstructor(String.class);
            declaredConstructor.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Class cls = Class.forName("android.app.ActivityThread");
            Method declaredMethod = cls.getDeclaredMethod("currentActivityThread");
            declaredMethod.setAccessible(true);
            Object activityThread = declaredMethod.invoke(null);

            try {
                Field mHiddenApiWarningShown = cls.getDeclaredField("mHiddenApiWarningShown"); // java.lang.NoSuchFieldException: mHiddenApiWarningShown
                mHiddenApiWarningShown.setAccessible(true);
                mHiddenApiWarningShown.setBoolean(activityThread, true);
            } catch (NoSuchFieldException e) {
                LogUtil.msg("获取mHiddenApiWarningShown 失败", LogUtil.W);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}


