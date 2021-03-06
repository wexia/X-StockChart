package com.example.sample.stockchart;

import android.graphics.Canvas;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.renderer.XAxisRenderer;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

/**
 * @author weixia
 * @date 2019/1/10.
 */
public class TimeSharingXAxisRenderer extends XAxisRenderer {
    private final BarLineChartBase mChart;
    private final TimeSharingXAxis mXAxis;

    public TimeSharingXAxisRenderer(ViewPortHandler viewPortHandler, TimeSharingXAxis xAxis,
                                    Transformer trans, BarLineChartBase chart) {
        super(viewPortHandler, xAxis, trans);
        mChart = chart;
        mXAxis = xAxis;
    }

    @Override
    protected void drawLabels(Canvas c, float pos, MPPointF anchor) {
        final float[] position = new float[]{0f, 0f};
        final int count = mXAxis.getXLabels().size();
        for (int i = 0; i < count; i++) {
            /*获取label对应key值，也就是x轴坐标0,60,121,182,242*/
            final int ix = mXAxis.getXLabels().keyAt(i);
            if (mXAxis.isCenterAxisLabelsEnabled()) {
                final float offset = (float) mXAxis.getXLabels().keyAt(count - 1) / (count - 1);
                position[0] = ix + offset / 2;
            } else {
                position[0] = ix;
            }
            /*在图表中的x轴转为像素，方便绘制text*/
            mTrans.pointValuesToPixel(position);
            /*x轴越界*/
            final String label = mXAxis.getXLabels().valueAt(i);
            /*文本长度*/
            final int labelWidth = Utils.calcTextWidth(mAxisLabelPaint, label);
            /*右出界*/
            if ((labelWidth / 2 + position[0]) > mViewPortHandler.contentRight()) {
                position[0] = mViewPortHandler.contentRight() - (float) labelWidth / 2;
            }
            /*左出界*/
            else if ((position[0] - labelWidth / 2) < mViewPortHandler.contentLeft()) {
                position[0] = mViewPortHandler.contentLeft() + (float) labelWidth / 2;
            }

            final float offsetBottom = Utils.convertPixelsToDp(mViewPortHandler.offsetBottom());
            final float y = pos + offsetBottom + 5;
            c.drawText(label, position[0], y, mAxisLabelPaint);
        }
    }

    /*x轴垂直线*/
    @Override
    public void renderGridLines(Canvas c) {
        if (!mXAxis.isDrawGridLinesEnabled() || !mXAxis.isEnabled()) {
            return;
        }

        setupGridPaint();

        int count = mXAxis.getXLabels().size();
        if (!mChart.isScaleXEnabled()) {
            count -= 1;
        }
        final float[] position = new float[]{0f, 0f};
        for (int i = 0; i < count; i++) {
            final int ix = mXAxis.getXLabels().keyAt(i);
            position[0] = ix;
            //优化XLabels对应的线条绘制（主用于初始化）
            if (ix == 0) {
                position[0] = Integer.MIN_VALUE;
            }
            mTrans.pointValuesToPixel(position);
            c.drawLine(position[0], mViewPortHandler.offsetTop(), position[0],
                    mViewPortHandler.contentBottom(), mGridPaint);
        }
    }
}
