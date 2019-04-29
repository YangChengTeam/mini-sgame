package com.wxr.gameh.wxapi;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.kk.utils.LogUtil;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.wxr.gameh.view.MainActivity;


public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        MainActivity.getMainActivity().api.handleIntent(getIntent(), this);
        super.onCreate(savedInstanceState);
        LogUtil.msg("onCreate");
    }

    @Override
    public void onReq(BaseReq baseReq) {
        LogUtil.msg("onReq");
    }

    @Override
    public void onResp(BaseResp resp) {
        LogUtil.msg("resp");
        if (resp.getType() == ConstantsAPI.COMMAND_LAUNCH_WX_MINIPROGRAM) {

        }
    }
}
