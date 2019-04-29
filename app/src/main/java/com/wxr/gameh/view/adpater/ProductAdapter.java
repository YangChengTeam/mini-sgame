package com.wxr.gameh.view.adpater;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wxr.gameh.R;
import com.wxr.gameh.helper.GlideHelper;
import com.wxr.gameh.model.bean.ProductInfo;


import java.util.List;

public class ProductAdapter extends BaseQuickAdapter<ProductInfo, BaseViewHolder> {


    public ProductAdapter(int layout, List<ProductInfo> data) {
        super(layout, data);
    }



    @Override
    protected void convert(BaseViewHolder helper, ProductInfo item) {
        helper.setText(R.id.tv_title, item.getName());
        helper.setText(R.id.tv_desc, item.getDesc());
        helper.setText(R.id.tv_num, item.getPlay_num()+"人在玩");
        GlideHelper.imageView(mContext, helper.getView(R.id.iv_logo), item.getIco(), R.mipmap.default_logo);
        GlideHelper.corderBorderImageView(mContext, helper.getView(R.id.iv_game_bg), item.getImage(), 4, R.mipmap.bg_default_product);
    }
}
