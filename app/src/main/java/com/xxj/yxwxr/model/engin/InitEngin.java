package com.xxj.yxwxr.model.engin;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.BaseEngin;
import com.xxj.yxwxr.constant.Config;
import com.xxj.yxwxr.model.bean.InitInfo;

import rx.Observable;

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
