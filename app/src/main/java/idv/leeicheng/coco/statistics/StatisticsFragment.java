package idv.leeicheng.coco.statistics;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;


import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import idv.leeicheng.coco.R;
import idv.leeicheng.coco.main.ItemSQLiteOpenHelper;
import idv.leeicheng.coco.main.MainActivity;
import idv.leeicheng.coco.recording.ListItem;
import idv.leeicheng.coco.recording.RecordingItem;

public class StatisticsFragment extends Fragment {
    PieChart pcStatistics;

    List<PieEntry> pieEntries;
    List<Integer> colors;
    long allSpent;

    ItemSQLiteOpenHelper itemSQLiteOpenHelper;
    TreeMap<String, Long> types;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.fragment_statistics, container, false);
        if (itemSQLiteOpenHelper == null) {
            itemSQLiteOpenHelper = new ItemSQLiteOpenHelper(getActivity());
        }
        findViews(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    void findViews(View view) {
        pcStatistics = view.findViewById(R.id.pcStatistics);
        viewsControl();
    }

    void viewsControl() {
        pieEntries = new ArrayList<>();

        allSpent = 0;
        setPie();

        pcStatistics.setRotationEnabled(true);
        pcStatistics.setUsePercentValues(true);
        pcStatistics.getDescription().setEnabled(false);
        pcStatistics.setDrawHoleEnabled(true);
        pcStatistics.animateY(1000, Easing.EasingOption.EaseInOutCirc);

        pcStatistics.setCenterText(getActivity().getResources().getString(R.string.total) + "\n" + allSpent +"元");
        pcStatistics.setCenterTextColor(R.color.colorGray);
        pcStatistics.setNoDataText(getActivity().getResources().getString(R.string.noRecording));

        pcStatistics.setEntryLabelColor(getActivity().getResources().getColor(R.color.colorBlack));

        pcStatistics.setCenterTextSize(16);
        pcStatistics.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry entry, Highlight highlight) {
                PieEntry pieEntry = (PieEntry) entry;
                String name = pieEntry.getLabel();
                long allSpent = (long) pieEntry.getValue();
                pcStatistics.setCenterText(name + "\n" + allSpent +"元");
            }

            @Override
            public void onNothingSelected() {
                String center = getActivity().getResources().getString(R.string.total) + "\n" + allSpent +"元";
                pcStatistics.setCenterText(center);
            }
        });
    }

    void setPie() {
        pieEntries = getData();
        PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
        pieDataSet.setValueFormatter(new PercentFormatter());
        pieDataSet.setYValuePosition(PieDataSet.ValuePosition.INSIDE_SLICE);
        pieDataSet.setXValuePosition(PieDataSet.ValuePosition.INSIDE_SLICE);
        pieDataSet.setValueTextSize(12);
        pieDataSet.setColors(colors);
        PieData pieData = new PieData(pieDataSet);
        pcStatistics.setData(pieData);
        pcStatistics.invalidate();
    }

    List<PieEntry> getData() {
        types = new TreeMap<>();
        List<ListItem> listItems;
        List<String> typesText = new ArrayList<>();
        List<PieEntry> pieEntries = new ArrayList<>();
        colors = new ArrayList<>();

        listItems = itemSQLiteOpenHelper.getAllRecording(MainActivity.tvTopDay.getText().toString().trim());

        for (ListItem item : listItems) {
            RecordingItem recordingItem = (RecordingItem) item;
            String type = recordingItem.getItemType();
            long spent = recordingItem.getSpent();
            if (types.get(type) != null) {
                spent += types.get(type);
                types.put(type, spent);
            } else {
                types.put(type, spent);
                typesText.add(type);
            }
        }

        for (String text : typesText) {
            float typeSpent = types.get(text);
            allSpent += typeSpent;
            colors.add(itemSQLiteOpenHelper.selectTypeColor(text));
            pieEntries.add(new PieEntry(typeSpent, text));
        }
        return pieEntries;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (itemSQLiteOpenHelper != null) {
            itemSQLiteOpenHelper.close();
        }
    }
}
