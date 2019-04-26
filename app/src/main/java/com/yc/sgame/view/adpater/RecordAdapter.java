package com.yc.sgame.view.adpater;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.sgame.R;
import com.yc.sgame.helper.GlideHelper;
import com.yc.sgame.model.bean.ProductInfo;

import java.util.List;

public class RecordAdapter extends BaseQuickAdapter<ProductInfo, BaseViewHolder> {


    public RecordAdapter(int layout, List<ProductInfo> data) {
        super(layout, data);
    }



    @Override
    protected void convert(BaseViewHolder helper, ProductInfo item) {
        helper.setText(R.id.tv_title, item.getName());
        helper.setText(R.id.tv_desc, item.getDesc());
        GlideHelper.corderBorderImageView(mContext, helper.getView(R.id.iv_logo), item.getIco(), 5, R.mipmap.default_logo);
    }
}
