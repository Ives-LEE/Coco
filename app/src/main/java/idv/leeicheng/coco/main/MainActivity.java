package idv.leeicheng.coco.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import idv.leeicheng.coco.R;
import idv.leeicheng.coco.recording.MyRecordingDayFragment;
import idv.leeicheng.coco.recording.MyRecordingMonthFragment;
import idv.leeicheng.coco.recording.MyRecordingYearFragment;
import idv.leeicheng.coco.setting.SettingFragment;
import idv.leeicheng.coco.statistics.StatisticsFragment;


import static idv.leeicheng.coco.main.CalendarControl.creatCalendar;
import static idv.leeicheng.coco.main.CalendarControl.getFormatDay;
import static idv.leeicheng.coco.main.CalendarControl.getFormatMonth;
import static idv.leeicheng.coco.main.CalendarControl.getFormatYear;
import static idv.leeicheng.coco.main.CalendarControl.getSelect;
import static idv.leeicheng.coco.main.CalendarControl.getToday;
import static idv.leeicheng.coco.main.CalendarControl.nextDay;
import static idv.leeicheng.coco.main.CalendarControl.nextMonth;
import static idv.leeicheng.coco.main.CalendarControl.nextYear;
import static idv.leeicheng.coco.main.CalendarControl.previousDay;
import static idv.leeicheng.coco.main.CalendarControl.previousMonth;
import static idv.leeicheng.coco.main.CalendarControl.previousYear;
import static idv.leeicheng.coco.main.CalendarControl.setSelect;
import static idv.leeicheng.coco.main.CalendarControl.setToday;
import static idv.leeicheng.coco.main.Common.PAGE_RECORDING_DAY;
import static idv.leeicheng.coco.main.Common.PAGE_RECORDING_MONTH;
import static idv.leeicheng.coco.main.Common.PAGE_RECORDING_YEAR;
import static idv.leeicheng.coco.main.Common.PAGE_SETTING;
import static idv.leeicheng.coco.main.Common.PAGE_STATISTICS_DAY;
import static idv.leeicheng.coco.main.Common.PAGE_STATISTICS_MONTH;
import static idv.leeicheng.coco.main.Common.PAGE_STATISTICS_YEAR;
import static idv.leeicheng.coco.main.ModeControl.getIsEdit;
import static idv.leeicheng.coco.main.ModeControl.getIsInputSpent;
import static idv.leeicheng.coco.main.ModeControl.normalMode;
import static idv.leeicheng.coco.main.ModeControl.setIsInputSpent;
import static idv.leeicheng.coco.main.ModeControl.setting;

public class MainActivity extends AppCompatActivity {
    public static TabLayout tlMain;
    public static BottomNavigationView bnvMain;
    public static TextView tvTopDay;
    public static Toolbar tbMain;
    public static ImageButton ibTopRight, ibTopLeft;
    public static View.OnClickListener clickRightListener;
    public static View.OnClickListener clickLeftListener;
    public static int lastPosition;
    int nowPosition;
    Fragment selectedFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getToday() == null) {
            setSelect((Calendar) creatCalendar().clone());
            setToday((Calendar) creatCalendar().clone());
        }
        selectedFragment = null;
        nowPosition = R.id.myRecording;
        findViews();
    }

    void findViews() {

        tlMain = findViewById(R.id.tlMain);
        bnvMain = findViewById(R.id.bnvMain);
        tvTopDay = findViewById(R.id.tvTopDay);
        ibTopRight = findViewById(R.id.ibTopRight);
        ibTopLeft = findViewById(R.id.ibTopLeft);
        tbMain = findViewById(R.id.tbMain);

        tlMain.addTab(tlMain.newTab().setText(R.string.day));
        tlMain.addTab(tlMain.newTab().setText(R.string.month));
        tlMain.addTab(tlMain.newTab().setText(R.string.year));

        tvTopDay.setText(getFormatDay());
        choseFragment(PAGE_RECORDING_DAY);
        viewsControl();
    }

    void viewsControl() {
        tlMain.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int tabPosition = tab.getPosition();
                if (nowPosition == R.id.myRecording) {
                    if (tabPosition == 0) {
                        nowPosition = PAGE_RECORDING_DAY;
                        tvTopDay.setText(getFormatDay());
                    } else if (tabPosition == 1) {
                        nowPosition = PAGE_RECORDING_MONTH;
                        tvTopDay.setText(getFormatMonth());
                    } else if (tabPosition == 2) {
                        nowPosition = PAGE_RECORDING_YEAR;
                        tvTopDay.setText(getFormatYear());
                    }
                    nowPosition = R.id.myRecording;
                    lastPosition = tabPosition;
                    choseFragment(lastPosition);
                } else if (nowPosition == R.id.statistics) {
                    if (tabPosition == 0) {
                        nowPosition = PAGE_STATISTICS_DAY;
                        tvTopDay.setText(getFormatDay());
                    } else if (tabPosition == 1) {
                        nowPosition = PAGE_STATISTICS_MONTH;
                        tvTopDay.setText(getFormatMonth());
                    } else if (tabPosition == 2) {
                        nowPosition = PAGE_STATISTICS_YEAR;
                        tvTopDay.setText(getFormatYear());
                    }
                    nowPosition = R.id.statistics;
                    lastPosition = tabPosition;
                    choseFragment(lastPosition + 3);
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        bnvMain.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.myRecording:
                        choseFragment(lastPosition);
                        if (nowPosition == R.id.setting) {
                            getDate(lastPosition);
                        }
                        nowPosition = R.id.myRecording;
                        normalMode();
                        viewsControl();
                        break;
                    case R.id.statistics:
                        choseFragment(lastPosition + 3);
                        if (nowPosition == R.id.setting) {
                            getDate(lastPosition + 3);
                        }
                        nowPosition = R.id.statistics;
                        normalMode();
                        break;
                    case R.id.setting:
                        nowPosition = R.id.setting;
                        choseFragment(PAGE_SETTING);
                        setting();
                        break;
                }
                selectedFragment = null;
                return true;
            }
        });

        tvTopDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getIsEdit()) return;
                Calendar calendar = (Calendar) getToday().clone();
                if (lastPosition == PAGE_RECORDING_DAY || lastPosition + 3 == PAGE_STATISTICS_DAY) {
                    showDatePicker();
                } else if (lastPosition == PAGE_RECORDING_MONTH) {
                    setSelect(calendar);
                    tvTopDay.setText(getFormatMonth());
                    choseFragment(lastPosition);
                } else if (lastPosition + 3 == PAGE_STATISTICS_MONTH) {
                    setSelect(calendar);
                    tvTopDay.setText(getFormatMonth());
                    choseFragment(lastPosition + 3);
                } else if (lastPosition == PAGE_RECORDING_YEAR) {
                    setSelect(calendar);
                    tvTopDay.setText(getFormatYear());
                    choseFragment(lastPosition);
                } else if (lastPosition + 3 == PAGE_STATISTICS_YEAR) {
                    setSelect(calendar);
                    tvTopDay.setText(getFormatYear());
                    choseFragment(lastPosition + 3);
                }
            }
        });


        clickRightListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getIsEdit()) return;
                if (nowPosition == R.id.myRecording) {
                    choseFragment(lastPosition);
                    nextDate(lastPosition);
                } else if (nowPosition == R.id.statistics) {
                    choseFragment(lastPosition + 3);
                    nextDate(lastPosition + 3);
                }
            }
        };
        ibTopRight.setOnClickListener(clickRightListener);


        clickLeftListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getIsEdit()) return;
                if (nowPosition == R.id.myRecording) {
                    choseFragment(lastPosition);
                    previousDate(lastPosition);
                } else if (nowPosition == R.id.statistics) {
                    choseFragment(lastPosition + 3);
                    previousDate(lastPosition + 3);
                }
            }
        };
        ibTopLeft.setOnClickListener(clickLeftListener);
    }



    void showDatePicker() {
        int year, month, day;

        year = getSelect().get(Calendar.YEAR);
        month = getSelect().get(Calendar.MONTH);
        day = getSelect().get(Calendar.DAY_OF_MONTH);

        final LayoutInflater layoutInflater = getLayoutInflater();
        final View view = layoutInflater.inflate(R.layout.calendar_dialog, null);

        final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this,R.style.dialog)
                .setView(view).show();
        DatePicker dpDatePicker = view.findViewById(R.id.dpDatePicker);
        Button btnBackTodayCalendar = view.findViewById(R.id.btnBackTodayCalendar);
        Button btnCloseCalendar = view.findViewById(R.id.btnCloseCalendar);

        dpDatePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
                String formate = "yyyy-MM-dd";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formate);
                month += 1;
                String text = year + "-" + month + "-" + day;

                Date date = null;
                try {
                    date = simpleDateFormat.parse(text);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                getSelect().setTime(date);

                tvTopDay.setText(getFormatDay());
                alertDialog.dismiss();
                if (nowPosition == R.id.statistics && lastPosition + 3 == PAGE_STATISTICS_DAY) {
                    choseFragment(lastPosition + 3);
                } else if (nowPosition == R.id.myRecording && lastPosition == PAGE_RECORDING_DAY) {
                    choseFragment(lastPosition);
                    getSupportFragmentManager().beginTransaction().replace(R.id.flMain, new MyRecordingDayFragment()).commit();
                }
            }
        });


        btnBackTodayCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelect((Calendar) getToday().clone());
                tvTopDay.setText(getFormatDay());
                alertDialog.dismiss();

                if (nowPosition == R.id.statistics && lastPosition == 0) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.flMain, new StatisticsFragment()).commit();
                } else if (nowPosition == R.id.myRecording && lastPosition == 0) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.flMain, new MyRecordingDayFragment()).commit();
                }
            }
        });

        btnCloseCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }

    void getDate(int position) {
        if (position == PAGE_RECORDING_DAY || position == PAGE_STATISTICS_DAY) {
            tvTopDay.setText(getFormatDay());
        } else if (position == PAGE_RECORDING_MONTH || position == PAGE_STATISTICS_MONTH) {
            tvTopDay.setText(getFormatMonth());
        } else if (position == PAGE_RECORDING_YEAR || position == PAGE_STATISTICS_YEAR) {
            tvTopDay.setText(getFormatYear());
        }
    }

    void nextDate(int position) {
        if (position == PAGE_RECORDING_DAY || position == PAGE_STATISTICS_DAY) {
            tvTopDay.setText(nextDay());
        } else if (position == PAGE_RECORDING_MONTH || position == PAGE_STATISTICS_MONTH) {
            tvTopDay.setText(nextMonth());
        } else if (position == PAGE_RECORDING_YEAR || position == PAGE_STATISTICS_YEAR) {
            tvTopDay.setText(nextYear());
        }
    }

    void previousDate(int position) {
        if (position == PAGE_RECORDING_DAY || position == PAGE_STATISTICS_DAY) {
            tvTopDay.setText(previousDay());
        } else if (position == PAGE_RECORDING_MONTH || position == PAGE_STATISTICS_MONTH) {
            tvTopDay.setText(previousMonth());
        } else if (position == PAGE_RECORDING_YEAR || position == PAGE_STATISTICS_YEAR) {
            tvTopDay.setText(previousYear());
        }
    }

    void choseFragment(int position) {
        if (position == PAGE_RECORDING_DAY) {
            selectedFragment = new MyRecordingDayFragment();
        } else if (position == PAGE_RECORDING_MONTH) {
            selectedFragment = new MyRecordingMonthFragment();
        } else if (position == PAGE_RECORDING_YEAR) {
            selectedFragment = new MyRecordingYearFragment();
        } else if (position == PAGE_STATISTICS_DAY || position == PAGE_STATISTICS_MONTH || position == PAGE_STATISTICS_YEAR) {
            selectedFragment = new StatisticsFragment();
        } else if (position == PAGE_SETTING) {
            selectedFragment = new SettingFragment();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.flMain, selectedFragment).commit();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (getIsInputSpent()) {
            setIsInputSpent(false);
        }
    }
}
