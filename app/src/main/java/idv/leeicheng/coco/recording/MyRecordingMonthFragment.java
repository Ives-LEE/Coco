package idv.leeicheng.coco.recording;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import idv.leeicheng.coco.R;
import idv.leeicheng.coco.main.ItemSQLiteOpenHelper;

import static idv.leeicheng.coco.main.CalendarControl.getFormatMonth;
import static idv.leeicheng.coco.main.CalendarControl.getSelect;
import static idv.leeicheng.coco.main.CalendarControl.makeDay;
import static idv.leeicheng.coco.main.MainActivity.tlMain;


public class MyRecordingMonthFragment extends Fragment {
    RecyclerView rvMonth;
    GridLayoutManager gridLayoutManager;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    ItemSQLiteOpenHelper itemSQLiteOpenHelper;
    float width;
    float height;

    int firstDay;
    Date date;
    String MON, TUE, WED, THU, FRI, SAT, SUN;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.fragment_month_recording, container, false);
        if (itemSQLiteOpenHelper == null) {
            itemSQLiteOpenHelper = new ItemSQLiteOpenHelper(getActivity());
        }

        MON = getResources().getString(R.string.MON);
        TUE = getResources().getString(R.string.TUE);
        WED = getResources().getString(R.string.WED);
        THU = getResources().getString(R.string.THU);
        FRI = getResources().getString(R.string.FRI);
        SAT = getResources().getString(R.string.SAT);
        SUN = getResources().getString(R.string.SUN);
        calendar = (Calendar) getSelect().clone();
        findViews(view);
        return view;
    }

    private void findViews(View view) {
        rvMonth = view.findViewById(R.id.rvMonth);
        setRvMonth();
    }


    void setRvMonth() {
        getWindow();
        gridLayoutManager = new GridLayoutManager(getActivity(), 7) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        getMonthDate();
        rvMonth.setLayoutManager(gridLayoutManager);
        rvMonth.setAdapter(new CalendarAdapter(getActivity()));
    }

    void getWindow() {
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        width = metrics.widthPixels / 7 - 2;
        height = width;
    }

    void getMonthDate() {
        String dateFormat = "E";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        String weekDay = simpleDateFormat.format(calendar.getTime());

        if (weekDay.equals(MON)) {
            firstDay = 1;
            calendar.add(Calendar.DATE, -1);
        } else if (weekDay.equals(TUE)) {
            firstDay = 2;
            calendar.add(Calendar.DATE, -2);
        } else if (weekDay.equals(WED)) {
            firstDay = 3;
            calendar.add(Calendar.DATE, -3);
        } else if (weekDay.equals(THU)) {
            firstDay = 4;
            calendar.add(Calendar.DATE, -4);
        } else if (weekDay.equals(FRI)) {
            firstDay = 5;
            calendar.add(Calendar.DATE, -5);
        } else if (weekDay.equals(SAT)) {
            firstDay = 6;
            calendar.add(Calendar.DATE, -6);
        } else if (weekDay.equals(SUN)) {
            firstDay = 0;
        }

    }

    private class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.DayViewHolder> {

        Context context;

        public CalendarAdapter(Context context) {
            this.context = context;
        }

        public class DayViewHolder extends RecyclerView.ViewHolder {
            TextView tvDateMonth, tvSpentMonth;
            CardView cvMonthCard;

            public DayViewHolder(View view) {
                super(view);
                tvDateMonth = view.findViewById(R.id.tvDateMonth);
                tvSpentMonth = view.findViewById(R.id.tvSpentMonth);
                cvMonthCard = view.findViewById(R.id.cvMonthCard);
                cvMonthCard.getLayoutParams().height = (int) height;
                cvMonthCard.getLayoutParams().width = (int) width;

            }
        }

        @Override
        public DayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.month_item, parent, false);
            return new DayViewHolder(view);
        }

        @Override
        public void onBindViewHolder(DayViewHolder holder, int position) {

            String monthFormat = "yyyy-MM-dd E";
            String title = getFormatMonth();
            simpleDateFormat = new SimpleDateFormat(monthFormat);
            String date = simpleDateFormat.format(calendar.getTime());
            String month = date.substring(0, 7);
            String show;

            if (month.equals(title)) {
                if (firstDay <= position) {
                    String day = date.substring(8, 10);

                    if (day.equals("01")) {
                        if (Integer.valueOf(month.substring(4, 7)) < 10) {
                            month = month.substring(6);
                        }

                        if (Integer.valueOf(day) < 10) {
                            day = day.substring(1);
                        }
                        show = month + "/" + day;
                    } else {
                        if (Integer.valueOf(day) < 10) {
                            day = day.substring(1);
                        }
                        show = day;
                    }
                    holder.tvDateMonth.setText(show);


                    String spend = getDayData(date);

                    if (spend != null && !spend.equals("") && !spend.equals("0")) {
                        holder.tvSpentMonth.setText(spend);
                    }

                    if (spend.length() > 5) {
                        holder.tvSpentMonth.setTextSize(8);
                    }

                    final String d = date;
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String day = d;
                            Date date = makeDay(day);
                            getSelect().setTime(date);
                            tlMain.getTabAt(0).select();
                        }
                    });
                }
            }

            calendar.add(Calendar.DATE, 1);


            if (position % 7 == 0) {
                holder.tvDateMonth.setTextColor(getResources().getColor(R.color.colorRed));
            }

        }

        @Override
        public int getItemCount() {
            return 42;
        }

        String getDayData(String day) {
            long spend = 0;
            List<ListItem> items = itemSQLiteOpenHelper.getAllRecording(day);
            for (ListItem item : items) {
                RecordingItem recordingItem = (RecordingItem) item;
                spend += recordingItem.getSpent();
            }
            return String.valueOf(spend);
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (itemSQLiteOpenHelper != null) {
            itemSQLiteOpenHelper = null;
        }
    }
}
