package com.xxj.yxwxr.model.engin;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.BaseEngin;
import com.xxj.yxwxr.constant.Config;
import com.xxj.yxwxr.model.bean.ProductInfo;

import java.util.HashMap;

import rx.Observable;

public class ClickEngin extends BaseEngin<ResultInfo<Void>> {

    public ClickEngin(Context context) {
        super(context);
    }

    @Override
    public String getUrl() {
        return Config.JUMP_NUM_LOG;
    }

    public Observable<ResultInfo<Void>> click(ProductInfo productInfo){
        HashMap<String, String> params = new HashMap<>();
        params.put("game_id", productInfo.getGame_id());
        params.put("wx_open_status", productInfo.getWx_open_status());
        params.put("app_id",  "12");
        params.put("type",  "1");
        params.put("path", productInfo.getPath());
        return rxpost(new TypeReference<ResultInfo<Void>>() {
        }.getType(), params, true, true, true);
    }
}
