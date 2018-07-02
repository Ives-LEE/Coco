package idv.leeicheng.coco.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import idv.leeicheng.coco.R;

public class CalculationActivitiy extends AppCompatActivity {
    String ADDITION, SUBSTRACTION, DIVISION, TIMES;

    ImageButton ibTopLeft, ibTopRight, ibBackspace;
    Button btnOne, btnTwo, btnThree, btnFour, btnFive, btnSix, btnSeven, btnEight, btnNine, btnZero;
    Button btnClean, btnAdd, btnSub, btnTimes, btnDiv, btnEqual;
    TextView tvCalculationAreaText;
    List<String> numbers;
    String answer;
    String inputNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculation);
        ADDITION = getResources().getString(R.string.addition);
        SUBSTRACTION = getResources().getString(R.string.substraction);
        DIVISION = getResources().getString(R.string.division);
        TIMES = getResources().getString(R.string.times);

        numbers = new ArrayList<>();
        findViews();
    }

    private void findViews() {
        ibTopLeft = findViewById(R.id.ibLeftCalculation);
        ibTopRight = findViewById(R.id.ibRightCalculation);
        ibBackspace = findViewById(R.id.ibBackspaceCalculation);

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
        viewsControl();
    }

    private void viewsControl() {
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
                if (answer == null) {
                    if (!tvCalculationAreaText.getText().equals("")){
                        String input = tvCalculationAreaText.getText().toString().trim();
                        answer = Operation.compute(input);
                    }
                }
                Intent intent = new Intent();
                intent.putExtra("spent",answer);
                setResult(Common.COMPUT,intent);
                finish();
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
                numbers.clear();
                inputNumber = "";

            }
        });

        btnEqual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = tvCalculationAreaText.getText().toString().trim();
                if (input == null || input.equals("") ){
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

}
