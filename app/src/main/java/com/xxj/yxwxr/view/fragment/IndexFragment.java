package com.xxj.yxwxr.view.fragment;

import android.graphics.drawable.AnimationDrawable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jakewharton.rxbinding3.view.RxView;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.utils.ScreenUtil;
import com.xxj.yxwxr.R;
import com.xxj.yxwxr.model.bean.ProductInfo;
import com.xxj.yxwxr.model.engin.IndexEngin;
import com.xxj.yxwxr.view.BaseActivity;
import com.xxj.yxwxr.view.adpater.ProductAdapter;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.Subscriber;
import rx.Subscription;

public class IndexFragment extends BaseFragment {

    @BindView(R.id.scrollView)
    ScrollView scrollView;

    @BindView(R.id.recyclerView)
    RecyclerView mProductRecyclerView;

    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.tv_num)
    TextView numTextView;

    @BindView(R.id.iv_start_btn)
    ImageView logoImageView;

    @BindView(R.id.cl_top)
    ConstraintLayout topConstraintLayout;

    private List<ProductInfo> productInfos;

    private ProductAdapter productAdapter;
    private IndexEngin indexEngin;

    public static Fragment newInstance() {
        return new IndexFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_index;
    }

    @Override
    protected void initViews() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 2){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mProductRecyclerView.setLayoutManager(gridLayoutManager);
        GridItemDecoration divider = new GridItemDecoration.Builder(getActivity())
                .setVerticalSpan(R.dimen.dp_10)
                .setShowLastLine(true)
                .build();
        productAdapter = new ProductAdapter(R.layout.item_product, null);
        mProductRecyclerView.setAdapter(productAdapter);
        mProductRecyclerView.addItemDecoration(divider);

        View footerView = new View(getContext());
        footerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ScreenUtil.dip2px(getActivity(), 10)));
        productAdapter.addFooterView(footerView);

        productAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                 ProductInfo productInfo =  productAdapter.getData().get(position);
                ((BaseActivity)getActivity()).nav2(productInfo);
            }
        });

        mSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getActivity(), R.color.colorAccent));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });



        showLoadingDialog("加载中...");
    }


    @Override
    protected void initData() {
        indexEngin = new IndexEngin(getActivity());
        loadData();
    }

    private void loadData(){
        getIndexData();
    }

    private void getIndexData(){
        Subscription subscription =  indexEngin.getIndexInfo().subscribe(new Subscriber<ResultInfo<List<ProductInfo>>>() {
            @Override
            public void onCompleted() {
                dissmissLoadingDialog();
                mSwipeRefreshLayout.setRefreshing(false);
                scrollView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(final ResultInfo<List<ProductInfo>> resultInfo) {
                if(resultInfo.getCode() == 1) {
                    productInfos = resultInfo.getData();
                    productAdapter.setNewData(resultInfo.getData());
                    if(productInfos != null && productInfos.size() > 0){
                        final ProductInfo item = productInfos.get(productInfos.size() - 1);
                        numTextView.setText(item.getPlay_num()+"人在玩");
                        RxView.clicks(topConstraintLayout).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(view-> {
                            ((BaseActivity)getActivity()).nav2MiniProgram(item);
                        });
                        logoImageView.setImageResource(R.drawable.start_btn_animation);
                        AnimationDrawable frameAnimation =    (AnimationDrawable)logoImageView.getDrawable();
                        frameAnimation.setCallback(logoImageView);
                        frameAnimation.setVisible(true, true);
                        frameAnimation.start();
                    }
                }
            }
        });
        mSubscriptions.add(subscription);
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

}
