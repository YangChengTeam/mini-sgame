package com.xxj.yxwxr.view.fragment;

import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.kk.securityhttp.domain.ResultInfo;
import com.xxj.yxwxr.R;
import com.xxj.yxwxr.SGameApplication;
import com.xxj.yxwxr.model.bean.InitInfo;
import com.xxj.yxwxr.model.bean.ProductInfo;
import com.xxj.yxwxr.model.engin.RecordEngin;
import com.xxj.yxwxr.view.BaseActivity;
import com.xxj.yxwxr.view.adpater.RecordAdapter;

import java.util.List;

import butterknife.BindView;
import rx.Subscriber;

public class MyFragment extends BaseFragment {


    @BindView(R.id.tv_name)
    TextView nameTextView;

    @BindView(R.id.recyclerView)
    RecyclerView mProductRecyclerView;

    @BindView(R.id.iv_logo)
    ImageView logoImageView;

    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.cl_state_view)
    ConstraintLayout stateView;

    private RecordAdapter recordAdapter;
    private RecordEngin recordEngin;

    public static Fragment newInstance() {
        return new MyFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my;
    }

    @Override
    protected void initViews() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mProductRecyclerView.setLayoutManager(linearLayoutManager);
        recordAdapter = new RecordAdapter(R.layout.item_record, null);
        mProductRecyclerView.setAdapter(recordAdapter);
        mProductRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
        recordAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ProductInfo productInfo =  recordAdapter.getData().get(position);
                ((BaseActivity)getActivity()).nav2MiniProgram(productInfo);
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
        super.initData();

        InitInfo initInfo = SGameApplication.getSGameApplication().initInfo;
        if(initInfo != null){
            nameTextView.setText("游生"+initInfo.getUserInfo().getUser_id());
        } else {
            nameTextView.setText("游生10000");
        }

        loadData();
    }

    private void loadData(){
        recordEngin = new RecordEngin(getActivity());
        recordEngin.getRecordInfo().subscribe(new Subscriber<ResultInfo<List<ProductInfo>>>() {
            @Override
            public void onCompleted() {
                dissmissLoadingDialog();
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(final ResultInfo<List<ProductInfo>> resultInfo) {
                if (resultInfo.getCode() == 1) {
                    List<ProductInfo> productInfos = resultInfo.getData();
                    if(productInfos != null && productInfos.size() > 0 ) {
                        recordAdapter.setNewData(productInfos);
                        stateView.setVisibility(View.GONE);
                    } else {
                        stateView.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }


}
