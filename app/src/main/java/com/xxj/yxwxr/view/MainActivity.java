package com.xxj.yxwxr.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.xxj.yxwxr.R;
import com.xxj.yxwxr.SGameApplication;
import com.xxj.yxwxr.view.fragment.IndexFragment;
import com.xxj.yxwxr.view.fragment.MyFragment;

import java.util.List;

import butterknife.*;


public class MainActivity extends BaseActivity implements View.OnClickListener {

    private Fragment[] fragments;
    @BindViews({R.id.iv_index_tab, R.id.iv_my_tab})
    public List<ImageView> tab_imageViews;

    @BindViews({R.id.tv_index_tab_text, R.id.tv_my_tab})
    public List<TextView> tab_textViews;

    private static MainActivity mainActivity;

    public static MainActivity getMainActivity() {
        return mainActivity;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews() {

//        checkAndRequestPermission();

        mainActivity = this;

        Fragment indexFragment = IndexFragment.newInstance();
        Fragment myFragment = MyFragment.newInstance();
        this.fragments = new Fragment[]{indexFragment, myFragment};
        if (!indexFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction().add((int) R.id.fragment_layout, indexFragment).show(indexFragment).commit();
        }
        findViewById(R.id.cl_index_tab).setOnClickListener(this);
        findViewById(R.id.cl_my_tab).setOnClickListener(this);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cl_index_tab:
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                showFragment(0);
                break;
            case R.id.cl_my_tab:
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                showFragment(1);
                break;
            default:
                break;
        }
    }

    private boolean isClick;
    private boolean isClick2;

    public void showFragment(int index) {
        FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
        if (index == 0) {
            trx.hide(this.fragments[1]);
            if (!(this.fragments[0].isAdded()) && !isClick) {
                trx.add(R.id.fragment_layout, this.fragments[0]);
            }
            isClick = true;
            trx.show(this.fragments[0]).commit();
        } else if (index == 1) {
            trx.hide(this.fragments[0]);
            if (!(this.fragments[1].isAdded()) && !isClick2) {
                trx.add(R.id.fragment_layout, this.fragments[1]);
            }
            isClick2 = true;
            trx.show(this.fragments[1]).commit();
        }
        if (index == 0) {
            this.tab_textViews.get(index).setTextColor(getResources().getColor(R.color.tab_color_selected));
            this.tab_imageViews.get(index).setImageResource(R.mipmap.index_tab_selected);
            this.tab_textViews.get(1).setTextColor(getResources().getColor(R.color.tab_color));
            this.tab_imageViews.get(1).setImageResource(R.mipmap.my_tab);
        } else if (index == 1) {
            this.tab_textViews.get(index).setTextColor(getResources().getColor(R.color.tab_color_selected));
            this.tab_imageViews.get(index).setImageResource(R.mipmap.my_tab_selected);
            this.tab_textViews.get(0).setTextColor(getResources().getColor(R.color.tab_color));
            this.tab_imageViews.get(0).setImageResource(R.mipmap.index_tab);
        }
    }

    @Override
    protected void initData() {
        super.initData();
        if (SGameApplication.getSGameApplication().loadingInfo != null) {
            nav2MiniProgram(SGameApplication.getSGameApplication().loadingInfo);
        }
    }

    private long firstTime = 0;

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4) {
            return false;
        }
        long secondTime = System.currentTimeMillis();
        if (secondTime - this.firstTime > 1500) {
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            this.firstTime = secondTime;
            return true;
        }
        finish();
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainActivity = null;
    }

}
