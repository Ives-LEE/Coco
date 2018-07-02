package idv.leeicheng.coco.main;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.lang.ref.WeakReference;

import idv.leeicheng.coco.R;
import idv.leeicheng.coco.recording.MyRecordingDayFragment;

import static idv.leeicheng.coco.main.ModeControl.setIsInputSpent;


public class NumberKeyboardFragment extends Fragment {
    private Button btnOne, btnTwo, btnThree, btnFour, btnFive;
    private Button btnSix, btnSeven, btnEight, btnNine, btnZero, btnCalculation;
    private ImageButton ibBackspace;
    private String DONE,CALCULATION;
    private WeakReference<EditText> weakReferenceEditText;
    private EditText editText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.fragment_number_keyboard, container, false);
        DONE = getResources().getString(R.string.done);
        CALCULATION = getResources().getString(R.string.calculation);
        findviews(view);
        return view;
    }

    private void findviews(View view) {
        btnOne = view.findViewById(R.id.btnOne);
        btnTwo = view.findViewById(R.id.btnTwo);
        btnThree = view.findViewById(R.id.btnThree);
        btnFour = view.findViewById(R.id.btnFour);
        btnFive = view.findViewById(R.id.btnFive);
        btnSix = view.findViewById(R.id.btnSix);
        btnSeven = view.findViewById(R.id.btnSeven);
        btnEight = view.findViewById(R.id.btnEight);
        btnNine = view.findViewById(R.id.btnNine);
        btnZero = view.findViewById(R.id.btnZero);
        btnCalculation = view.findViewById(R.id.btnCalculation);
        ibBackspace = view.findViewById(R.id.ibBackspace);
        editText = weakReferenceEditText.get();
        viewsControl();
    }

    private void viewsControl() {
        if (!editText.getText().toString().equals("")){
            btnCalculation.setText(DONE);
        } else {
            btnCalculation.setText(CALCULATION);
        }


        View.OnClickListener clickNubmerListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String btnText = "";
                if (view.getId() == R.id.btnOne){
                    btnText = "1";
                } else if (view.getId() == R.id.btnTwo){
                    btnText = "2";
                } else if (view.getId() == R.id.btnThree){
                    btnText = "3";
                } else if (view.getId() == R.id.btnFour){
                    btnText = "4";
                } else if (view.getId() == R.id.btnFive){
                    btnText = "5";
                } else if (view.getId() == R.id.btnSix) {
                    btnText = "6";
                }else if (view.getId() == R.id.btnSeven) {
                    btnText = "7";
                }else if (view.getId() == R.id.btnEight) {
                    btnText = "8";
                }else if (view.getId() == R.id.btnNine) {
                    btnText = "9";
                }else if (view.getId() == R.id.btnZero) {
                    btnText = "0";
                }
                btnCalculation.setText(DONE);
                String text = editText.getText().toString().trim();
                text += btnText;
                editText.setText(text);

            }
        };

        btnOne.setOnClickListener(clickNubmerListener);
        btnTwo.setOnClickListener(clickNubmerListener);
        btnThree.setOnClickListener(clickNubmerListener);
        btnFour.setOnClickListener(clickNubmerListener);
        btnFive.setOnClickListener(clickNubmerListener);
        btnSix.setOnClickListener(clickNubmerListener);
        btnSeven.setOnClickListener(clickNubmerListener);
        btnEight.setOnClickListener(clickNubmerListener);
        btnNine.setOnClickListener(clickNubmerListener);
        btnZero.setOnClickListener(clickNubmerListener);


        ibBackspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = editText.getText().toString().trim();
                if (text.length() > 0){
                    String backspace = text.substring(0,text.length()-1);
                    editText.setText(backspace);
                } else {
                    setIsInputSpent(false);
                    getFragmentManager().beginTransaction().remove(NumberKeyboardFragment.this).commit();
                }
            }
        });

        btnCalculation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btnCalculation.getText().toString().trim().equals(DONE)){
                    setIsInputSpent(false);
                    getFragmentManager().beginTransaction().remove(NumberKeyboardFragment.this).commit();
                } else {
                    Intent intent = new Intent(getActivity(),CalculationActivitiy.class);
                    startActivityForResult(intent,Common.COMPUT);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) return;
        if (requestCode == Common.COMPUT){
            String spent =  data.getStringExtra("spent");
            editText.setText(spent);
        }
    }

    public WeakReference<EditText> getWeakReferenceEditText() {
        return weakReferenceEditText;
    }

    public void setWeakReferenceEditText(EditText editText) {
        this.weakReferenceEditText = new WeakReference<>(editText);
    }
}
