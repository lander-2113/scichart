package com.hfad.scichart;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;

import com.scichart.charting.model.dataSeries.XyDataSeries;
import com.scichart.charting.modifiers.AxisDragModifierBase;
import com.scichart.charting.modifiers.ModifierGroup;
import com.scichart.charting.modifiers.XAxisDragModifier;
import com.scichart.charting.visuals.SciChartSurface;
import com.scichart.charting.visuals.axes.AutoRange;
import com.scichart.charting.visuals.axes.IAxis;
import com.scichart.charting.visuals.renderableSeries.IRenderableSeries;
import com.scichart.core.annotations.Orientation;
import com.scichart.core.framework.UpdateSuspender;
import com.scichart.drawing.utility.ColorUtil;
import com.scichart.extensions.builders.SciChartBuilder;

import java.util.Collections;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class LineActivity extends AppCompatActivity {
    Random generator;
    private Double open;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line);

        generator = new Random();

        /******************************************************/
        open = MainActivity.shareData.getDailyShareValue();
        /******************************************************/


        /******************************************************/
        // Create a SciChartSurface
        SciChartSurface surface = new SciChartSurface(this);
        // Get a layout declared in "activity_main.xml" by id
        LinearLayout chartLayout = findViewById(R.id.linechart);
        // Add the SciChartSurface to the layout
        chartLayout.addView(surface);
        // Initialize the SciChartBuilder
        SciChartBuilder.init(this);
        // Obtain the SciChartBuilder instance
        final SciChartBuilder sciChartBuilder = SciChartBuilder.instance();
        /******************************************************/


        /******************************************************/
        // Create a numeric X axis
        final IAxis xAxis = sciChartBuilder.newNumericAxis()
                .withAxisTitle("X Axis Title")
                .withAutoRangeMode(AutoRange.Once)
                .withGrowBy(0, 1)
                .build();
        // Create a numeric Y axis
        final IAxis yAxis = sciChartBuilder.newNumericAxis()
                .withAxisTitle("Y Axis Title")
                .withAutoRangeMode(AutoRange.Always)
                .withGrowBy(0d, 0.1d).build();
        /******************************************************/
        // Create interactivity modifiers


        ModifierGroup chartModifiers = sciChartBuilder.newModifierGroup()
                .withPinchZoomModifier().withReceiveHandledEvents(true).build()
                .withZoomPanModifier().withReceiveHandledEvents(true).build()
                .build();
        // add the chart modifier to the chart
        Collections.addAll(surface.getChartModifiers(), chartModifiers);


        // Create a LegendModifier and configure a chart legend
        ModifierGroup legendModifier = sciChartBuilder.newModifierGroup()
                .withLegendModifier()
                .withOrientation(Orientation.HORIZONTAL)
                .withPosition(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 10)
                .build()
                .build();
        // Add the LegendModifier to the SciChartSurface
        surface.getChartModifiers().add(legendModifier);

        // Create and configure a CursorModifier
        ModifierGroup cursorModifier = sciChartBuilder.newModifierGroup()
                .withCursorModifier().withShowTooltip(true).build()
                .build();
        // Add the CursorModifier to the SciChartSurface
        surface.getChartModifiers().add(cursorModifier);

        // Create an XAxisDragModifier
        XAxisDragModifier xAxisDrag = new XAxisDragModifier();
        xAxisDrag.setDragMode(AxisDragModifierBase.AxisDragMode.Pan);
        surface.getChartModifiers().add(xAxisDrag);
/***********/

        // Add the Y axis to the YAxes collection of the surface
        Collections.addAll(surface.getYAxes(), yAxis);
        // Add the X axis to the XAxes collection of the surface
        Collections.addAll(surface.getXAxes(), xAxis);

/*******************/

        // change data accorind to these values
        Double[] variations = {0.30, 0.15, 0.25, .005, -0.30, -0.25, -0.15, -0.005};

        // this is the linedata that hold both x and y values.
        XyDataSeries lineData = sciChartBuilder.newXyDataSeries(Integer.class, Double.class).build();

        // schedule the task
        TimerTask updateDataTask = new TimerTask() {
            @Override
            public void run() {
                UpdateSuspender.using(surface, new Runnable() {
                    @Override
                    public void run() {
                        int x = lineData.getCount();
                        open += variations[generator.nextInt(variations.length)];
                        lineData.append(x, open);
                    }
                });
            }
        };
        Timer timer = new Timer();
        long delay = 1000;
        long interval = 1000;
        timer.schedule(updateDataTask, delay, interval);
        // tenderable sereis
        final IRenderableSeries lineSeries = sciChartBuilder.newLineSeries()
                .withDataSeries(lineData)
                .withStrokeStyle(ColorUtil.GreenYellow, 2f, true)
                .build();

        surface.getRenderableSeries().add(lineSeries);
    }
}