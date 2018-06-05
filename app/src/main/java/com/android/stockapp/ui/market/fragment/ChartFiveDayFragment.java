package com.android.stockapp.ui.market.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.stockapp.R;
import com.android.stockapp.common.data.Constant;
import com.android.stockapp.ui.base.BaseFragment;
import com.android.stockapp.ui.market.activity.StockDetailLandActivity;
import com.github.mikephil.charting.stockChart.CoupleChartGestureListener;
import com.github.mikephil.charting.stockChart.data.KTimeData;
import com.github.mikephil.charting.stockChart.view.FiveDayView;
import com.github.mikephil.charting.stockChart.view.OneDayView;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 分时页
 */
public class ChartFiveDayFragment extends BaseFragment {

    @BindView(R.id.chart)
    FiveDayView chart;
    Unbinder unbinder;

    private boolean land;//是否横屏
    private KTimeData kTimeData = new KTimeData();
    private JSONObject object;

    public static ChartFiveDayFragment newInstance(boolean land) {
        ChartFiveDayFragment fragment = new ChartFiveDayFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("landscape", land);
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public int setLayoutId() {
        return R.layout.fragment_five_day;
    }

    @Override
    public void initBase(View view) {

        chart.initChart(land);

        //测试数据
        try {
            object = new JSONObject(Constant.FiveTIMEDATA);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        chart.setMaxCount(302);
        SparseArray<String> xLabels = new SparseArray<>();
        xLabels.put(0, "1日");
        xLabels.put(60, "2日");
        xLabels.put(121, "3日");
        xLabels.put(182, "4日");
        xLabels.put(243, "5日");
        xLabels.put(301, "");
        chart.setXLabels(xLabels);


        kTimeData.parseTimeData(object);
        chart.setDataToChart(kTimeData);

        //非横屏页单击转横屏页
        if (!land) {
            chart.getGestureListenerLine().setCoupleClick(new CoupleChartGestureListener.CoupleClick() {
                @Override
                public void singleClickListener() {
                    Intent intent = new Intent(getActivity(), StockDetailLandActivity.class);
                    getActivity().startActivity(intent);
                }
            });
            chart.getGestureListenerBar().setCoupleClick(new CoupleChartGestureListener.CoupleClick() {
                @Override
                public void singleClickListener() {
                    Intent intent = new Intent(getActivity(), StockDetailLandActivity.class);
                    getActivity().startActivity(intent);
                }
            });
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        land = getArguments().getBoolean("landscape");
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}