package com.hfad.scichart;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import com.scichart.charting.model.dataSeries.IOhlcDataSeries;
import com.scichart.charting.model.dataSeries.OhlcDataSeries;
import com.scichart.charting.modifiers.AxisDragModifierBase;
import com.scichart.charting.modifiers.ModifierGroup;
import com.scichart.charting.modifiers.XAxisDragModifier;
import com.scichart.charting.visuals.SciChartSurface;
import com.scichart.charting.visuals.axes.AutoRange;
import com.scichart.charting.visuals.axes.IAxis;
import com.scichart.charting.visuals.renderableSeries.FastCandlestickRenderableSeries;
import com.scichart.core.annotations.Orientation;
import com.scichart.core.framework.UpdateSuspender;
import com.scichart.extensions.builders.SciChartBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Candle_activity extends AppCompatActivity {


    // this variable is to know that fifteen minutes are done
    // 15 min = 15 * 60 s = 900 s
    int flag;
    public Random generator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candle);

        flag = -1;
        generator = new Random();
        // Create a SciChartSurface
        SciChartSurface surface = new SciChartSurface(this);
        // Get a layout declared in "activity_main.xml" by id
        LinearLayout chartLayout = findViewById(R.id.candle);
        // Add the SciChartSurface to the layout
        chartLayout.addView(surface);
        // Initialize the SciChartBuilder
        SciChartBuilder.init(this);
        // Obtain the SciChartBuilder instance
        final SciChartBuilder sciChartBuilder = SciChartBuilder.instance();


        final IAxis xAxis = sciChartBuilder.newCategoryDateAxis()
                .withAutoRangeMode(AutoRange.Once)
                .withGrowBy(0, 0.1)
                .build();
        final IAxis yAxis = sciChartBuilder.newNumericAxis()
                .withGrowBy(0d, 0.1d)
                .withAutoRangeMode(AutoRange.Always)
                .build();

        ModifierGroup chartModifiers = sciChartBuilder.newModifierGroup()
                .withPinchZoomModifier().withReceiveHandledEvents(true).build()
                .withZoomPanModifier().withReceiveHandledEvents(true).build()
                .build();

        // Create a LegendModifier and configure a chart legend
        ModifierGroup legendModifier = sciChartBuilder.newModifierGroup()
                .withLegendModifier()
                .withOrientation(Orientation.HORIZONTAL)
                .withPosition(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 10)
                .build()
                .build();

        surface.getChartModifiers().add(legendModifier);
// Add the LegendModifier to the SciChartSurface

        // Create and configure a CursorModifier
        ModifierGroup cursorModifier = sciChartBuilder.newModifierGroup()
                .withCursorModifier().withShowTooltip(true).build()
                .build();
// Add the CursorModifier to the SciChartSurface
        surface.getChartModifiers().add(cursorModifier);
        Collections.addAll(surface.getChartModifiers(), chartModifiers);
        Collections.addAll(surface.getXAxes(), xAxis);
        Collections.addAll(surface.getYAxes(), yAxis);

        // Create an XAxisDragModifier
        XAxisDragModifier xAxisDrag = new XAxisDragModifier();
        xAxisDrag.setDragMode(AxisDragModifierBase.AxisDragMode.Pan);
        surface.getChartModifiers().add(xAxisDrag);
        IOhlcDataSeries<Date, Double> dataSeries = new OhlcDataSeries<>(Date.class, Double.class);

        final FastCandlestickRenderableSeries rSeries = sciChartBuilder.newCandlestickSeries()
                .withStrokeUp(0xFF00AA00)
                .withFillUpColor(0x8800AA00)
                .withStrokeDown(0xFFFF0000)
                .withFillDownColor(0x88FF0000)
                .withDataSeries(dataSeries)
                .build();
        Collections.addAll(surface.getRenderableSeries(),
                rSeries);

        sciChartBuilder
                .newAnimator(rSeries)
                .withWaveTransformation()
                .withInterpolator(new DecelerateInterpolator())
                .withDuration(0).withStartDelay(0).start();


        Collections.addAll(surface.getChartModifiers(),
                sciChartBuilder
                        .newModifierGroupWithDefaultModifiers()
                        .build());
        Collections.addAll(surface.getChartModifiers(), chartModifiers);

        final Double[] open = {new Double(-30.0d)};
        final Double[] close = {new Double(30.6)};
        final Double[] low = {new Double(-30)};
        final Double[] high = {new Double(30.0d)};


        Double[] variations1 = {0.30, 0.15, 0.25, .005};
        Double[] variations2 = {-0.30, -0.25, -0.15, -0.005};
        List<Double[]> variations = new ArrayList<>();
        variations.add(variations1);
        variations.add(variations2);
        final double[] share = {open[0]};
        TimerTask updateDataTask = new TimerTask() {
            @Override
            public void run() {
                share[0] += variations.get(generator.nextInt(2))[generator.nextInt(4)];
                high[0] = Math.max(high[0], share[0]);
                low[0] = Math.min(low[0], share[0]);
                UpdateSuspender.using(surface, new Runnable() {
                    @Override
                    public void run() {

                        if(flag == 0){
                            dataSeries.append(new Date(),
                                    open[0], high[0], low[0], close[0]);
                            high[0] = share[0];
                            low[0] = share[0];
                            open[0] = share[0];

                        }

                        //surface.zoomExtents();
                    }
                });
                if(flag == 900){
                    flag = -1;
                }

                flag++;
                close[0] = share[0];

            }
        };
        Timer timer = new Timer();
        long delay = 1000;
        long interval = 10;
        timer.schedule(updateDataTask, delay, interval);


    }
}