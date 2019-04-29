package com.wxr.gameh.view.widget;

import android.content.Context;
import android.widget.TextView;


import com.wxr.gameh.R;

import butterknife.BindView;

public class MyLoadingDialog extends BaseDialog {

    @BindView(R.id.message)
    TextView messageTextView;

    public MyLoadingDialog(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_loading;
    }

    public void show(String message) {
        messageTextView.setText(message);
        this.show();
    }

    @Override
    protected void initViews() {

    }
}
