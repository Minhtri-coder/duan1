package com.example.myapplication;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class sodo extends AppCompatActivity {
    BarChart bar_chart;
    PieChart pie_chart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sodo);
        bar_chart= findViewById(R.id.bar_chart);
        pie_chart = findViewById(R.id.pie_chart);

        ArrayList<BarEntry> barEntries = new ArrayList<>();
        ArrayList<PieEntry> pieEntries= new ArrayList<>();

        for(int i =1;i<10;i++){
            float vlue= (float) (i * 10.0);;
            BarEntry barEntry= new BarEntry(i,vlue);
            PieEntry pieEntry = new PieEntry(vlue, String.valueOf(i));
            barEntries.add(barEntry);
            pieEntries.add(pieEntry);
        }
        BarDataSet barDataSet = new BarDataSet(barEntries, "Employees");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        // hien data
        barDataSet.setDrawValues(false);
        // set data
        bar_chart.setData(new BarData(barDataSet));
        // set animation
        bar_chart.animateY(5000);
        // ghi chu va mau
        bar_chart.getDescription().setText("Nhin cc");
        // set pie data
        PieDataSet pieDataSet= new PieDataSet(pieEntries,"Student");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        // hien data
        pieDataSet.setDrawValues(false);
        // set data
        pie_chart.setData(new PieData(pieDataSet));
        // set animation
        pie_chart.animateXY(5000,5000);
        // set ghi chu
        pie_chart.getDescription().setEnabled(false);
    }
}