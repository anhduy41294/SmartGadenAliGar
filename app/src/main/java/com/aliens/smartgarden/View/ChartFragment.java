package com.aliens.smartgarden.View;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.aliens.smartgarden.Controller.LoaderHelper;
import com.aliens.smartgarden.Global.GlobalVariable;
import com.aliens.smartgarden.Model.Profile;
import com.aliens.smartgarden.Model.RecordSituation;
import com.aliens.smartgarden.R;
import com.aliens.smartgarden.View.AllProfileView.AllProfileAdapter;
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
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChartFragment extends Fragment {

    View view = null;
    ArrayList<RecordSituation> recordSituations;

    public ChartFragment() {
        // Required empty public constructor
    }

    ProgressDialog progressDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_chart, container, false);

        progressDialog = new ProgressDialog(view.getContext());
        progressDialog.setTitle("Đang tải...");

        GetDataForChart getDataForChart = new GetDataForChart();
        getDataForChart.execute();

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
            Random r = new Random();
//            entries1.add(new BarEntry((int) (Math.random() * 7), i));
//            entries2.add(new BarEntry((int) (Math.random() * 7) + 30, i));
//            entries3.add(new BarEntry((int) (Math.random() * 7) + 30, i));
            entries1.add(new BarEntry(recordSituations.get(i).getTemperature(), i));
            entries2.add(new BarEntry(recordSituations.get(i).getHumidity(), i));
            entries3.add(new BarEntry(recordSituations.get(i).getLight(), i));

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
        m.add("1");
        m.add("2");
        m.add("3");
        m.add("4");
        m.add("5");
        m.add("6");

        return m;
    }

    /**
     * AsyncTask
     */
    public class GetDataForChart extends AsyncTask<String, Void, ArrayList<RecordSituation>> {

        public GetDataForChart() {

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected ArrayList<RecordSituation> doInBackground(String... params) {
            LoaderHelper loaderHelper = new LoaderHelper();
            recordSituations = loaderHelper.getSituationChart();
            return loaderHelper.getSituationChart();
        }

        @Override
        protected void onPostExecute(ArrayList<RecordSituation> situationChart) {
            super.onPostExecute(situationChart);
            //Log.d("aaa", String.valueOf(situationChart.size()));
            recordSituations = new ArrayList<RecordSituation>();
            recordSituations = situationChart;
            ListView lv = (ListView) view.findViewById(R.id.listView1);
            ArrayList<BarData> list = new ArrayList<BarData>();

            // 3 items
            for (int i = 0; i < 3; i++) {
                list.add(generateData(i + 1));
            }
            ChartDataAdapter cda = new ChartDataAdapter(view.getContext(), list);
            lv.setAdapter(cda);
            progressDialog.dismiss();
        }
    }
}
