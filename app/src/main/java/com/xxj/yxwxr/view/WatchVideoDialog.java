package com.xxj.yxwxr.view;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xxj.yxwxr.R;

/**
 * Created by caokun on 2019/10/10 14:52.
 */

public class WatchVideoDialog extends AlertDialog implements View.OnClickListener {
    private OnClickBtnListener onClickBtnListener;

    protected WatchVideoDialog(Context context) {
        super(context, R.style.no_border_dialog);//设置样式
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.watch_video_dialog);

        initView();
    }

    private void initView() {
        TextView btn_cancel = findViewById(R.id.watch_video_dialog_btn_cancel);
        TextView btn_affirm = findViewById(R.id.watch_video_dialog_btn_affirm);

        btn_cancel.setOnClickListener(this);
        btn_affirm.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.watch_video_dialog_btn_cancel:
                onClickBtnListener.clickBtn(false);
                dismiss();
                break;
            case R.id.watch_video_dialog_btn_affirm:
                onClickBtnListener.clickBtn(true);
                dismiss();
                break;
        }
    }

    public interface OnClickBtnListener {
        void clickBtn(boolean isAffirm);
    }

    public void setOnClickBtnListener(OnClickBtnListener onClickBtnListener) {
        this.onClickBtnListener = onClickBtnListener;
    }
}
