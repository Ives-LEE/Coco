package idv.leeicheng.coco.appwidget;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import idv.leeicheng.coco.R;
import idv.leeicheng.coco.main.ItemSQLiteOpenHelper;
import idv.leeicheng.coco.main.Operation;
import idv.leeicheng.coco.recording.RecordingItem;
import idv.leeicheng.coco.setting.ItemType;


public class QuicklyAddAcitivty extends AppCompatActivity {
    String ADDITION, SUBSTRACTION, DIVISION, TIMES;

    ImageButton ibTopLeft, ibTopRight, ibBackspace;
    Button btnOne, btnTwo, btnThree, btnFour, btnFive, btnSix, btnSeven, btnEight, btnNine, btnZero;
    Button btnClean, btnAdd, btnSub, btnTimes, btnDiv, btnEqual, btnCalculationType;
    TextView tvCalculationAreaText, tvCalculationTitle;
    String today;

    List<String> numbers;
    String answer;
    RecyclerView rvCalculationArea;
    List<ItemType> types;
    ItemSQLiteOpenHelper itemSQLiteOpenHelper;
    float height;
    float width;
    String inputNumber;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculation);

        ADDITION = getResources().getString(R.string.addition);
        SUBSTRACTION = getResources().getString(R.string.substraction);
        DIVISION = getResources().getString(R.string.division);
        TIMES = getResources().getString(R.string.times);
        numbers = new ArrayList<>();
        if (itemSQLiteOpenHelper == null) {
            itemSQLiteOpenHelper = new ItemSQLiteOpenHelper(this);
        }
        rvCalculationArea = findViewById(R.id.rvCalculationArea);
        setRvQuicklyAdd();
        findViews();
    }

    private void setRvQuicklyAdd() {
        rvCalculationArea.setVisibility(View.VISIBLE);
        rvCalculationArea.setLayoutManager(new GridLayoutManager(this, 3, GridLayoutManager.HORIZONTAL, false));
        rvCalculationArea.post(new Runnable() {
            @Override
            public void run() {
                height = rvCalculationArea.getHeight() / 3 -2;
                width = rvCalculationArea.getWidth() / 3 - 2;
                QuicklyAddAdapter quicklyAddAdapter
                        = new QuicklyAddAdapter(QuicklyAddAcitivty.this, getTypes());
                rvCalculationArea.setAdapter(quicklyAddAdapter);
            }
        });

    }

    private List<ItemType> getTypes() {
        return itemSQLiteOpenHelper.getAllTypes();
    }

    private void findViews() {
        today = getToday();
        ibTopLeft = findViewById(R.id.ibLeftCalculation);
        ibTopRight = findViewById(R.id.ibRightCalculation);
        ibBackspace = findViewById(R.id.ibBackspaceCalculation);

        tvCalculationTitle = findViewById(R.id.tvCalculationTitle);
        tvCalculationAreaText = findViewById(R.id.tvCalculationAreaText);

        btnOne = findViewById(R.id.btnOneCalculation);
        btnTwo = findViewById(R.id.btnTwoCalculation);
        btnThree = findViewById(R.id.btnThreeCalculation);
        btnFour = findViewById(R.id.btnFourCalculation);
        btnFive = findViewById(R.id.btnFiveCalculation);
        btnSix = findViewById(R.id.btnSixCalculation);
        btnSeven = findViewById(R.id.btnSevenCalculation);
        btnEight = findViewById(R.id.btnEightCalculation);
        btnNine = findViewById(R.id.btnNineCalculation);
        btnZero = findViewById(R.id.btnZeroCalculation);

        btnClean = findViewById(R.id.btnCleanCalculation);
        btnDiv = findViewById(R.id.btnDivCalculation);
        btnSub = findViewById(R.id.btnSubCalculation);
        btnTimes = findViewById(R.id.btnTimesCalculation);
        btnAdd = findViewById(R.id.btnAddationCalculation);
        btnEqual = findViewById(R.id.btnEqualCalculation);
        btnCalculationType = findViewById(R.id.btnCalculationType);
        viewsControl();
    }

    private String getToday() {
        Calendar calendar = Calendar.getInstance();
        String dayFormat = "yyyy-MM-dd E";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dayFormat);
        return simpleDateFormat.format(calendar.getTime());
    }

    private void viewsControl() {
        tvCalculationTitle.setText(today);
        btnOne.setOnClickListener(setNumberListener());
        btnTwo.setOnClickListener(setNumberListener());
        btnThree.setOnClickListener(setNumberListener());
        btnFour.setOnClickListener(setNumberListener());
        btnFive.setOnClickListener(setNumberListener());
        btnSix.setOnClickListener(setNumberListener());
        btnSeven.setOnClickListener(setNumberListener());
        btnEight.setOnClickListener(setNumberListener());
        btnNine.setOnClickListener(setNumberListener());
        btnZero.setOnClickListener(setNumberListener());
        btnAdd.setOnClickListener(setNumberListener());
        btnSub.setOnClickListener(setNumberListener());
        btnTimes.setOnClickListener(setNumberListener());
        btnDiv.setOnClickListener(setNumberListener());


        ibTopLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ibTopRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!tvCalculationAreaText.getText().equals("")) {
                    String input = tvCalculationAreaText.getText().toString().trim();
                    answer = Operation.compute(input);
                }

                String date = today;
                String itemName = "";
                String type = btnCalculationType.getText().toString().trim();
                long spent;

                if (answer == null || answer.equals("")) return;
                if (answer != null) {
                    spent = Integer.valueOf(answer);
                    itemSQLiteOpenHelper.insertRecording(new RecordingItem(date, type, spent, itemName));

                }
                finish();
            }
        });

        btnCalculationType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rvCalculationArea.setVisibility(View.VISIBLE);
            }
        });

        ibBackspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String gettvCalculationAreaText = tvCalculationAreaText.getText().toString().trim();
                if (gettvCalculationAreaText.length() > 0) {
                    String backspace = gettvCalculationAreaText.substring(0, gettvCalculationAreaText.length() - 1);
                    if (inputNumber.length() > 0){
                        inputNumber = inputNumber.substring(0,inputNumber.length()-1);
                    }
                    tvCalculationAreaText.setText(backspace);
                } else {
                    numbers.clear();
                    inputNumber = "";
                }

            }
        });

        btnClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvCalculationAreaText.setText("");
                inputNumber = "";
                numbers.clear();
                inputNumber = "";
            }
        });

        btnEqual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = tvCalculationAreaText.getText().toString().trim();
                if (input == null || input.equals("")) {
                    return;
                }
                answer = Operation.compute(input);
                tvCalculationAreaText.setText(answer);
                if (input.length() == 0){
                    inputNumber = "";
                }

            }
        });
    }

    View.OnClickListener setNumberListener() {

        View.OnClickListener clickNumberListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inputNumber != null && inputNumber.length() > 10){
                    return;
                }
                String btnText = "";
                if (view.getId() == R.id.btnOneCalculation) {
                    btnText = getResources().getString(R.string.one);
                } else if (view.getId() == R.id.btnTwoCalculation) {
                    btnText = getResources().getString(R.string.two);
                } else if (view.getId() == R.id.btnThreeCalculation) {
                    btnText = getResources().getString(R.string.three);
                } else if (view.getId() == R.id.btnFourCalculation) {
                    btnText = getResources().getString(R.string.four);
                } else if (view.getId() == R.id.btnFiveCalculation) {
                    btnText = getResources().getString(R.string.five);
                } else if (view.getId() == R.id.btnSixCalculation) {
                    btnText = getResources().getString(R.string.six);
                } else if (view.getId() == R.id.btnSevenCalculation) {
                    btnText = getResources().getString(R.string.seven);
                } else if (view.getId() == R.id.btnEightCalculation) {
                    btnText = getResources().getString(R.string.eight);
                } else if (view.getId() == R.id.btnNineCalculation) {
                    btnText = getResources().getString(R.string.nine);
                } else if (view.getId() == R.id.btnZeroCalculation) {
                    btnText = getResources().getString(R.string.zero);
                } else if (view.getId() == R.id.btnAddationCalculation) {
                    String text = tvCalculationAreaText.getText().toString().trim();
                    if (text.length() > 0) {
                        String lastChar = String.valueOf(text.charAt(text.length() - 1));
                        if (!lastChar.equals(ADDITION) && !lastChar.equals(SUBSTRACTION) && !lastChar.equals(TIMES) && !lastChar.equals(DIVISION)) {
                            btnText = ADDITION;
                        }
                    }

                } else if (view.getId() == R.id.btnSubCalculation) {
                    String text = tvCalculationAreaText.getText().toString().trim();
                    if (text.length() > 0) {
                        String lastChar = String.valueOf(text.charAt(text.length() - 1));
                        if (!lastChar.equals(ADDITION) && !lastChar.equals(SUBSTRACTION) && !lastChar.equals(TIMES) && !lastChar.equals(DIVISION)) {
                            btnText = SUBSTRACTION;
                        }
                    }
                } else if (view.getId() == R.id.btnDivCalculation) {
                    String text = tvCalculationAreaText.getText().toString().trim();
                    if (text.length() > 0) {
                        String lastChar = String.valueOf(text.charAt(text.length() - 1));
                        if (!lastChar.equals(ADDITION) && !lastChar.equals(SUBSTRACTION) && !lastChar.equals(TIMES) && !lastChar.equals(DIVISION)) {
                            btnText = DIVISION;
                        }
                    }
                } else if (view.getId() == R.id.btnTimesCalculation) {
                    String text = tvCalculationAreaText.getText().toString().trim();
                    if (text.length() > 0) {
                        String lastChar = String.valueOf(text.charAt(text.length() - 1));
                        if (!lastChar.equals(ADDITION) && !lastChar.equals(SUBSTRACTION) && !lastChar.equals(TIMES) && !lastChar.equals(DIVISION)) {
                            btnText = TIMES;
                        }
                    }
                }
                String gettvCalculationAreaText = tvCalculationAreaText.getText().toString().trim();
                gettvCalculationAreaText += btnText;
                if (view.getId() == R.id.btnAddationCalculation
                        || view.getId() == R.id.btnSubCalculation
                        || view.getId() == R.id.btnDivCalculation
                        || view.getId() == R.id.btnTimesCalculation){
                    inputNumber = "";

                } else {
                    inputNumber += btnText;
                }
                tvCalculationAreaText.setText(gettvCalculationAreaText);
            }
        };
        return clickNumberListener;
    }

    private class QuicklyAddAdapter extends RecyclerView.Adapter<QuicklyAddAdapter.ItemViewHolder> {
        Context context;
        List<ItemType> types;

        public class ItemViewHolder extends RecyclerView.ViewHolder {
            TextView tvColorItemName;
            CardView cvColor;

            public ItemViewHolder(View view) {
                super(view);
                tvColorItemName = view.findViewById(R.id.tvColorItemName);
                cvColor = view.findViewById(R.id.cvColor);
                cvColor.getLayoutParams().width = (int) width;
                cvColor.getLayoutParams().height = (int) height;
            }
        }

        public QuicklyAddAdapter(Context context, List<ItemType> types) {
            this.context = context;
            this.types = types;
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.quickly_color_item, parent, false);
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ItemViewHolder holder, int position) {
            final ItemType itemType = types.get(position);
            final int color = Color.rgb(itemType.getRed(), itemType.getGreen(), itemType.getBlue());
            holder.cvColor.setCardBackgroundColor(color);
            holder.tvColorItemName.setText(itemType.getName());


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    rvCalculationArea.setVisibility(View.GONE);
                    btnCalculationType.setText(itemType.getName());
                    btnCalculationType.setFocusable(true);
                }
            });

        }

        @Override
        public int getItemCount() {
            return types.size();
        }
    }

}
