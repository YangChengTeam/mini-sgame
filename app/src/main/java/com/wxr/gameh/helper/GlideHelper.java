package com.wxr.gameh.helper;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.kk.utils.ScreenUtil;

/**
 * Created by zhangkai on 2017/8/8.
 */

public class GlideHelper {

    public static void circleBorderImageView(final Context context, ImageView imageView, Object url, int
            placehorder, float borderwidth, int bordercolor, int width, int height) {
        RequestOptions options = new RequestOptions();
        if (placehorder != 0) {
            options.placeholder(placehorder);
        }
        options.transform(new GlideCircleTransformation(context, borderwidth,
                bordercolor));
        if(width > 0 && height > 0) {
            options.override(width, height);
        }
        Glide.with(context).load(url).apply(options).into(imageView);

    }

    public static void corderBorderImageView(final Context context,ImageView imageView, String url, int width, int placehorder) {
        RequestOptions options = new RequestOptions();
        if (placehorder != 0) {
            options.placeholder(placehorder);
        }
        options.transform(new RoundedCornersTransformation(ScreenUtil.dip2px(context, width),0));
        Glide.with(context).load(url).apply(options).into(imageView);

    }

    public static void imageView(final Context context, ImageView imageView, String url, int
            placehorder) {
        RequestOptions options = new RequestOptions();
        if (placehorder != 0) {
            options.placeholder(placehorder);
        }
        Glide.with(context).load(url).apply(options).into(imageView);
    }

    public static void imageView(final Context context, ImageView imageView, String url, int
            placehorder, int width, int height) {
        RequestOptions options = new RequestOptions();
        if (placehorder != 0) {
            options.placeholder(placehorder);
        }
        options.override(width, height);
        Glide.with(context).load(url).apply(options).into(imageView);
    }

}
