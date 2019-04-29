package com.wxr.gameh.model.engin;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.BaseEngin;
import com.wxr.gameh.constant.Config;
import com.wxr.gameh.model.bean.ProductInfo;

import java.util.HashMap;
import java.util.List;

import rx.Observable;

public class RecordEngin extends BaseEngin<ResultInfo<List<ProductInfo>>> {
    public RecordEngin(Context context) {
        super(context);
    }

    @Override
    public String getUrl() {
        return Config.RECORD_URL;
    }

    public Observable<ResultInfo<List<ProductInfo>>> getRecordInfo() {
        HashMap<String, String> params = new HashMap<>();
        params.put("app_id",  "12");
        return rxpost(new TypeReference<ResultInfo<List<ProductInfo>>>() {
        }.getType(), params, true, true, true);
    }
}
