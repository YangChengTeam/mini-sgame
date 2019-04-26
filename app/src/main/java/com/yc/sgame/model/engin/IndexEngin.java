package com.yc.sgame.model.engin;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.BaseEngin;
import com.yc.sgame.constant.Config;
import com.yc.sgame.model.bean.ProductInfo;

import java.util.List;

import rx.Observable;

public class IndexEngin extends BaseEngin<ResultInfo<List<ProductInfo>>> {
    public IndexEngin(Context context) {
        super(context);
    }

    @Override
    public String getUrl() {
        return Config.INDEX_URL;
    }

    public Observable<ResultInfo<List<ProductInfo>>> getIndexInfo() {
        return rxpost(new TypeReference<ResultInfo<List<ProductInfo>>>() {
        }.getType(), null, true, true, true);
    }
}
