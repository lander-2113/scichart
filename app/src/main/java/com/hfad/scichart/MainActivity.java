package com.hfad.scichart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.util.DateInterval;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Surface;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hfad.scichart.data.ShareData;
import com.scichart.charting.ClipMode;
import com.scichart.charting.model.AxisCollection;
import com.scichart.charting.model.RenderableSeriesCollection;
import com.scichart.charting.model.dataSeries.IOhlcDataSeries;
import com.scichart.charting.model.dataSeries.OhlcDataSeries;
import com.scichart.charting.model.dataSeries.XyDataSeries;
import com.scichart.charting.modifiers.AxisDragModifierBase;
import com.scichart.charting.modifiers.ModifierGroup;
import com.scichart.charting.visuals.SciChartSurface;
import com.scichart.charting.visuals.annotations.HorizontalAnchorPoint;
import com.scichart.charting.visuals.annotations.TextAnnotation;
import com.scichart.charting.visuals.annotations.VerticalAnchorPoint;
import com.scichart.charting.visuals.axes.AxisAlignment;
import com.scichart.charting.visuals.axes.DateAxis;
import com.scichart.charting.visuals.axes.IAxis;
import com.scichart.charting.visuals.axes.NumericAxis;
import com.scichart.charting.visuals.pointmarkers.EllipsePointMarker;
import com.scichart.charting.visuals.renderableSeries.FastCandlestickRenderableSeries;
import com.scichart.charting.visuals.renderableSeries.FastLineRenderableSeries;
import com.scichart.charting.visuals.renderableSeries.IRenderableSeries;
import com.scichart.core.annotations.Orientation;
import com.scichart.core.framework.UpdateSuspender;
import com.scichart.core.model.DateValues;
import com.scichart.core.model.DoubleValues;
import com.scichart.core.utility.DateIntervalUtil;
import com.scichart.drawing.common.PenStyle;
import com.scichart.drawing.common.SolidPenStyle;
import com.scichart.drawing.utility.ColorUtil;
import com.scichart.extensions.builders.SciChartBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {

    // data object that store the today's open, low, high, close values.
    static ShareData shareData;
    // from android layout
    EditText eopen;
    EditText elow;
    EditText ehigh;
    EditText eclose;

    Button btn_chart;
    Button btn_update;
    String[] values;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        String key = "ks0JpEeMVmiTNnl2oRc+fVUTui7nP/e2NJYSvI5hUzO0P4eElRkqAzKxE8Ff6MdK/A1jNbngroNH4VncT4thsMbIfTcgK9V9FweOhvCTwMsdNfwRFUSJjpZhVMmXRrettjeTgudOa7oO4vdXNHZA+rJ7V+qm89ZD3oEeRZJuoh1gfvWGVXLfT+O/ARstLUTEj2bQFLcbMUu/2083DKb1HO6DetIPbK7N0oDbwOIhMr6F3g0QiORiFwxp83MA3bJPY2TIFKE5Jx4QRC9ljjharyCgku94NvaPcQRnsQ7a9LrLK4OhE2fbFUaptwNtsuRN6C36Nxw7eyxT8JTp0U+goMGuaiAWEEkZOBQdkc0cjdbIXM23Qj7nwO2cPJ4NrQ/cskO1g7bl4d7RqV6sQ85a0DPl/dSR6BtzKPX8wLUwBlgSDBOVHWHTJrL+vBgKczwLFaCa358Tn39sf34iXnpmP4sBgWVKEBrjOak7OEqNYseDp+0EGMyHsZpsEaMOMLD9IWNqnpDwYg8nuh8zJd80laCOw5wzHNVTk4rK9LnS6qGRVSfOneiZ3KssihndeyUpZGF5pRRkFA==";
        try {
            com.scichart.charting.visuals.SciChartSurface.setRuntimeLicenseKey(key);
        } catch (Exception e) {
            Log.e("SciChart", "Error when setting the license", e);
        }

        eopen = findViewById(R.id.sharevalue);
        elow = findViewById(R.id.todaylow);
        ehigh = findViewById(R.id.todayhigh);
        eclose = findViewById(R.id.closing);
        btn_chart = findViewById(R.id.button2);
        btn_update = findViewById(R.id.button);
        //btn_chart.setVisibility(View.INVISIBLE);
    }

    public void update(View view) {
        try {
            Double open = null, low = null, high = null, close = null;


            // these if checks if the values are entered or not.
            if(eopen.getText().toString().length() > 0){
                open = Double.parseDouble(eopen.getText().toString());
            }

            if(elow.getText().toString().length() > 0){
                low = Double.parseDouble(elow.getText().toString());
            }

            if(ehigh.getText().toString().length() > 0){
                high = Double.parseDouble(ehigh.getText().toString());
            }

            if(eclose.getText().toString().length() > 0){
                close = Double.parseDouble(eclose.getText().toString());
            }

            shareData = new ShareData(open, low, high, close);

        } catch(Exception e){
            Log.e("input", "invalid value entered", e);
            Toast.makeText(getApplicationContext(), "Invalid Input", Toast.LENGTH_SHORT).show();
        }
    }

    public void seeLineCharts(View view) {
        Intent intent = new Intent(getApplicationContext(), LineActivity.class);
        startActivity(intent);
    }

    public void seeCandle(View view) {
        Intent intent = new Intent(getApplicationContext(), Candle_activity.class);
        startActivity(intent);
    }

}