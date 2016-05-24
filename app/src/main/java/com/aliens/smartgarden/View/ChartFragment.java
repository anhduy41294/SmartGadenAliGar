package com.aliens.smartgarden.View;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.aliens.smartgarden.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChartFragment extends Fragment {

    View view = null;

    public ChartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_chart, container, false);
        ListView lv = (ListView) view.findViewById(R.id.listView1);

        ArrayList<BarData> list = new ArrayList<BarData>();

        // 20 items
        for (int i = 0; i < 3; i++) {
            list.add(generateData(i + 1));
        }

        ChartDataAdapter cda = new ChartDataAdapter(view.getContext(), list);
        lv.setAdapter(cda);
        return view;
    }

    private class ChartDataAdapter extends ArrayAdapter<BarData> {

        private Typeface mTf;

        public ChartDataAdapter(Context context, List<BarData> objects) {
            super(context, 0, objects);

            mTf = Typeface.createFromAsset(view.getContext().getAssets(), "OpenSans-Regular.ttf");
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            BarData data = getItem(position);

            ViewHolder holder = null;

            if (convertView == null) {

                holder = new ViewHolder();

                convertView = LayoutInflater.from(view.getContext()).inflate(
                        R.layout.list_item_barchart, null);
                holder.chart = (BarChart) convertView.findViewById(R.id.chart);

                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            // apply styling
            data.setValueTypeface(mTf);
            data.setValueTextColor(Color.BLACK);
            holder.chart.setDescription("");
            holder.chart.setDrawGridBackground(false);

            XAxis xAxis = holder.chart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setTypeface(mTf);
            xAxis.setDrawGridLines(false);

            YAxis leftAxis = holder.chart.getAxisLeft();
            leftAxis.setTypeface(mTf);
            leftAxis.setLabelCount(5, false);
            leftAxis.setSpaceTop(15f);

            YAxis rightAxis = holder.chart.getAxisRight();
            rightAxis.setTypeface(mTf);
            rightAxis.setLabelCount(5, false);
            rightAxis.setSpaceTop(15f);

            // set data
            holder.chart.setData(data);

            // do not forget to refresh the chart
//            holder.chart.invalidate();
            holder.chart.animateY(700, Easing.EasingOption.EaseInCubic);

            return convertView;
        }

        private class ViewHolder {

            BarChart chart;
        }
    }

    /**
     * generates a random ChartData object with just one DataSet
     *
     * @return
     */
    private BarData generateData(int cnt) {

        ArrayList<BarEntry> entries1 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> entries2 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> entries3 = new ArrayList<BarEntry>();

        for (int i = 0; i < 6; i++) {
            entries1.add(new BarEntry((int) (Math.random() * 70), i));
            entries2.add(new BarEntry((int) (Math.random() * 70) + 30, i));
            entries3.add(new BarEntry((int) (Math.random() * 70) + 30, i));

        }

        int[] colornek1 = {Color.rgb(255,0,0)};
        int[] colornek2 = {Color.rgb(0,191,255)};
        int[] colornek3 = {Color.rgb(255,255,0)};
        ArrayList<IBarDataSet> sets = new ArrayList<IBarDataSet>();
        BarDataSet d;
        switch (cnt){
            case 1:
                //BarDataSet d = new BarDataSet(entries, "New DataSet " + cnt);
                d = new BarDataSet(entries1, "Nhiệt độ");
                d.setBarSpacePercent(50f);
                d.setColors(colornek1);
                //d.setColors(ColorTemplate.VORDIPLOM_COLORS);
                d.setBarShadowColor(Color.rgb(203, 203, 203));

                sets.add(d);
                break;
            case 2:

                //BarDataSet d = new BarDataSet(entries, "New DataSet " + cnt);
                d = new BarDataSet(entries2, "Độ ẩm");
                d.setBarSpacePercent(50f);
                d.setColors(colornek2);
                //d.setColors(ColorTemplate.VORDIPLOM_COLORS);
                d.setBarShadowColor(Color.rgb(203, 203, 203));

                sets.add(d);
                break;
            case 3:
                //BarDataSet d = new BarDataSet(entries, "New DataSet " + cnt);
                d = new BarDataSet(entries3, "Ánh sáng");
                d.setBarSpacePercent(50f);
                d.setColors(colornek3);
                //d.setColors(ColorTemplate.VORDIPLOM_COLORS);
                d.setBarShadowColor(Color.rgb(203, 203, 203));

                sets.add(d);
                break;
        }



        BarData cd = new BarData(getMonths(), sets);
        return cd;
    }

    private ArrayList<String> getMonths() {
        Calendar c = Calendar.getInstance();
        int currentHour24 = c.get(Calendar.HOUR_OF_DAY);

        ArrayList<String> m = new ArrayList<String>();
        m.add(String.valueOf(currentHour24-10)+"g");
        m.add(String.valueOf(currentHour24-8)+"g");
        m.add(String.valueOf(currentHour24-6)+"g");
        m.add(String.valueOf(currentHour24-4)+"g");
        m.add(String.valueOf(currentHour24-2)+"g");
        m.add(String.valueOf(currentHour24)+"g");

        return m;
    }
}