package com.xxj.yxwxr.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.xxj.yxwxr.R;
import com.xxj.yxwxr.view.widget.MyLoadingDialog;

import butterknife.ButterKnife;
import rx.subscriptions.CompositeSubscription;


public abstract class BaseFragment extends Fragment {
    protected View mRootView;
    protected Context mContext;

    public MyLoadingDialog dlg = null;

    protected CompositeSubscription mSubscriptions;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(getLayoutId(), null);
            ButterKnife.bind(this, mRootView);
            if(mRootView.findViewById(R.id.v_status_bar) != null){
                setstatusBarHeight(mRootView.findViewById(R.id.v_status_bar));
            }
            mSubscriptions = new CompositeSubscription();
            dlg = new MyLoadingDialog(this.getContext());
            initViews();
            initData();
            mContext = getActivity();
        }
        return mRootView;
    }


    public void showLoadingDialog(String message) {
        dlg.show(message);
    }

    public void dissmissLoadingDialog(){
        dlg.dismiss();
    }


    protected abstract int  getLayoutId();

    protected abstract void initViews();

    protected void initData(){}



    @Override
    public void onDestroy() {
        super.onDestroy();
        mSubscriptions.clear();
    }

    public void setstatusBarHeight(View v) {
        int statusBarHeight = 50;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        v.getLayoutParams().width = -1;
        v.getLayoutParams().height = statusBarHeight;
        v.setLayoutParams( v.getLayoutParams());
    }
}
