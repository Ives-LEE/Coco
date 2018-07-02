package idv.leeicheng.coco.recording;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import idv.leeicheng.coco.R;
import idv.leeicheng.coco.main.ItemSQLiteOpenHelper;

import static idv.leeicheng.coco.main.CalendarControl.getFormatYear;
import static idv.leeicheng.coco.main.CalendarControl.getSelect;
import static idv.leeicheng.coco.main.CalendarControl.getTodayFormatMonth;
import static idv.leeicheng.coco.main.CalendarControl.getTodayFormatYear;
import static idv.leeicheng.coco.main.CalendarControl.makeMonth;
import static idv.leeicheng.coco.main.MainActivity.tlMain;

public class MyRecordingYearFragment extends Fragment {
    RecyclerView rvYear;
    TextView tvTotalYear;
    ItemSQLiteOpenHelper itemSQLiteOpenHelper;
    List<MonthItem> months;
    String[] monthsText = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
    long yearAllSpend;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.fragment_year_recording, container, false);
        if (itemSQLiteOpenHelper == null) {
            itemSQLiteOpenHelper = new ItemSQLiteOpenHelper(getActivity());
        }
        findViews(view);
        return view;
    }

    private void findViews(View view) {
        rvYear = view.findViewById(R.id.rvYear);
        tvTotalYear = view.findViewById(R.id.tvTotalYear);
        viewsControl();
    }

    private void viewsControl() {
        setRvYear();
    }

    private void setRvYear() {
        months = new ArrayList<>();
        yearAllSpend = 0;
        months = getMonthData();
        rvYear.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rvYear.setAdapter(new YearAdapter(getActivity(), months));
        tvTotalYear.setText(String.valueOf(yearAllSpend));
    }

    private class YearAdapter extends RecyclerView.Adapter<YearAdapter.yearViewHolder> {
        Context context;
        List<MonthItem> monthItemList;

        public YearAdapter(Context context, List<MonthItem> monthItemList) {
            Collections.reverse(monthItemList);
            this.context = context;
            this.monthItemList = monthItemList;
        }

        public class yearViewHolder extends RecyclerView.ViewHolder {
            TextView tvMonth, tvMonthSpent;

            public yearViewHolder(View view) {
                super(view);
                tvMonth = view.findViewById(R.id.tvMonth);
                tvMonthSpent = view.findViewById(R.id.tvMonthSpent);
            }
        }

        @Override
        public yearViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.year_item, parent, false);
            return new yearViewHolder(view);
        }

        @Override
        public void onBindViewHolder(yearViewHolder holder, int position) {
            final MonthItem monthItem = monthItemList.get(position);
            holder.tvMonth.setText(monthItem.getMonth() + "月");
            holder.tvMonthSpent.setText(String.valueOf(monthItem.getMonthSpent()));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String month;
                    if (monthItem.getMonth() < 10){
                        month = getFormatYear()+"-0"+monthItem.getMonth();
                    } else {
                        month = getFormatYear()+"-"+monthItem.getMonth();
                    }
                    Date date = makeMonth(month);
                    getSelect().setTime(date);
                    tlMain.getTabAt(1).select();
                }
            });

        }

        @Override
        public int getItemCount() {
            return monthItemList.size();
        }
    }

    private List<MonthItem> getMonthData() {
        List<MonthItem> monthItems = new ArrayList<>();
        int thisMonth = Integer.valueOf(getTodayFormatMonth().substring(5,7));
        int titleYear = Integer.valueOf(getFormatYear());
        int thisYear = Integer.valueOf(getTodayFormatYear());
        long monthAllSpent = 0;
        for (String month : monthsText) {
            String select = getFormatYear() + "-" + month;
            List<ListItem> items = itemSQLiteOpenHelper.getAllRecording(select);
            for (ListItem item : items) {
                RecordingItem recordingItem = (RecordingItem) item;
                monthAllSpent += recordingItem.getSpent();
            }

            if (titleYear == thisYear && Integer.valueOf(month) <= thisMonth) {
                monthItems.add(new MonthItem(Integer.valueOf(month), monthAllSpent));
                yearAllSpend += monthAllSpent;
            } else if (titleYear < thisYear) {
                monthItems.add(new MonthItem(Integer.valueOf(month), monthAllSpent));
                yearAllSpend += monthAllSpent;
            }
            monthAllSpent = 0;

        }
        return monthItems;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (itemSQLiteOpenHelper != null) {
            itemSQLiteOpenHelper = null;
        }
    }
}
