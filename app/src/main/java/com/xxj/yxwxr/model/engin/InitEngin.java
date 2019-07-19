package com.xxj.yxwxr.model.engin;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.BaseEngin;
import com.xxj.yxwxr.constant.Config;
import com.xxj.yxwxr.model.bean.InitInfo;


import java.lang.reflect.Type;

import rx.Observable;
import rx.internal.util.ScalarSynchronousObservable;
import rx.internal.util.SubscriptionList;

public class InitEngin extends BaseEngin<ResultInfo<InitInfo>> {

    public InitEngin(Context context) {
        super(context);
    }

    @Override
    public String getUrl() {
        return Config.INIT_URL;

    }

    public Observable<ResultInfo<InitInfo>> init() {
        return rxpost(new TypeReference<ResultInfo<InitInfo>>() {
        }.getType(), null, true, true, true);
    }
}
