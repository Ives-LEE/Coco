package idv.leeicheng.coco.recording;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import idv.leeicheng.coco.R;
import idv.leeicheng.coco.main.CalculationActivitiy;
import idv.leeicheng.coco.main.Common;
import idv.leeicheng.coco.main.ItemSQLiteOpenHelper;
import idv.leeicheng.coco.setting.ItemType;

import static idv.leeicheng.coco.main.CalendarControl.getFormatDay;
import static idv.leeicheng.coco.main.CalendarControl.getSelect;
import static idv.leeicheng.coco.main.ModeControl.getIsInputSpent;
import static idv.leeicheng.coco.main.ModeControl.hideNumberKeyboard;
import static idv.leeicheng.coco.main.ModeControl.showAndHideNumberKeyboard;


public class AddItemActivity extends AppCompatActivity {

    ImageButton back;
    EditText etDateAdd, etSpentAdd, etItemAdd;
    Button btnAdd, btnNextItem, btnItemTypeAdd,btnRightAdd;
    AlertDialog typeDialog;
    ItemSQLiteOpenHelper itemSQLiteOpenHelper;
    TextView tvAddTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        if (itemSQLiteOpenHelper == null) {
            itemSQLiteOpenHelper = new ItemSQLiteOpenHelper(this);
        }
        findViews();
    }

    void findViews() {
        back = findViewById(R.id.ibLeftAdd);
        etDateAdd = findViewById(R.id.etDateAdd);
        etSpentAdd = findViewById(R.id.etSpentAdd);
        etItemAdd = findViewById(R.id.etItemAdd);
        btnItemTypeAdd = findViewById(R.id.btnItemTypeAdd);
        btnAdd = findViewById(R.id.btnAdd);
        btnNextItem = findViewById(R.id.btnNextAdd);
        tvAddTitle = findViewById(R.id.tvAddTitle);
        btnRightAdd = findViewById(R.id.btnRightAdd);
        viewsControl();
    }

    void viewsControl() {
        int itemId = -1;
        Intent intent = getIntent();

        if (intent.getExtras() != null) {
            Bundle bundle = intent.getExtras();
            tvAddTitle.setText(getResources().getString(R.string.editRecording));
            itemId = bundle.getInt("itemId");
        }

        if (itemId != -1) {
            //update
            final RecordingItem recording = (RecordingItem) getRecording(itemId);
            etDateAdd.setText(recording.getDate());
            etSpentAdd.setText(String.valueOf(recording.getSpent()));
            etItemAdd.setText(recording.getItemName());
            int color = getItemTypeColor(recording.getItemType());
            setButtonItemTypeColor(color);
            btnItemTypeAdd.setText(recording.getItemType());

            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //...
                    hideKeyboard();
                    updateRecording(recording.getId());
                    finish();
                }
            });

            btnNextItem.setText(R.string.cancel);
            btnNextItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

        } else {
            etDateAdd.setText(getFormatDay());

            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hideKeyboard();
                    insertRecording();
                    finish();
                }
            });

            btnNextItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hideKeyboard();
                    insertRecording();
                    finish();
                    Intent intent = new Intent(getApplicationContext(), AddItemActivity.class);
                    startActivity(intent);
                }
            });
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        etDateAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                showDatePicker();
            }
        });

        btnRightAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),CalculationActivitiy.class);
                startActivityForResult(intent, Common.COMPUT);
            }
        });


        btnItemTypeAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                showType();
            }
        });


    }

    ListItem getRecording(int id) {
        return itemSQLiteOpenHelper.getRecordingItem(id);
    }

    void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    void updateRecording(int id) {
        String date = etDateAdd.getText().toString().trim();
        String type = btnItemTypeAdd.getText().toString().trim();
        long spent = 0;
        if (!etSpentAdd.getText().toString().trim().equals("")) {
            spent = Long.valueOf(etSpentAdd.getText().toString().trim());
        }
        String itemName = etItemAdd.getText().toString().trim();

        RecordingItem recordingItem = new RecordingItem(id, date, type, spent, itemName, null);
        itemSQLiteOpenHelper.updateRecording(recordingItem);

    }

    void insertRecording() {
        String date = etDateAdd.getText().toString().trim();
        String type = btnItemTypeAdd.getText().toString().trim();
        long spent = 0;
        if (!etSpentAdd.getText().toString().trim().equals("")) {
            spent = Long.valueOf(etSpentAdd.getText().toString().trim());
        }
        String itemName = etItemAdd.getText().toString().trim();

        RecordingItem recordingItem = new RecordingItem(date, type, spent, itemName, null);
        itemSQLiteOpenHelper.insertRecording(recordingItem);
    }

    void showDatePicker() {
        int year, month, day;
        year = getSelect().get(Calendar.YEAR);
        month = getSelect().get(Calendar.MONTH);
        day = getSelect().get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                String formate = "yyyy-MM-dd";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formate);
                month += 1;
                String text = year + "-" + month + "-" + day;
                Date date = null;
                String dateText = "";
                try {
                    date = simpleDateFormat.parse(text);
                    simpleDateFormat = new SimpleDateFormat(formate + " E");
                    dateText = simpleDateFormat.format(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                etDateAdd.setText(dateText);
            }
        }, year, month, day).show();
    }

    void showType() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.choose_type_dialog, null);
        RecyclerView rvTypeDialog = dialogView.findViewById(R.id.rvTypeDialog);
        rvTypeDialog.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        List<ItemType> types = getTypes();
        rvTypeDialog.setAdapter(new TypeAdapter(this, types));
        dialogBuilder.setView(dialogView);
        typeDialog = dialogBuilder.create();
        typeDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) return;
        if (requestCode == Common.COMPUT){
            String spent =  data.getStringExtra("spent");
            etSpentAdd.setText(spent);
        }
    }

    private class TypeAdapter extends RecyclerView.Adapter<TypeAdapter.TypeItemViewHolder> {

        Context context;
        List<ItemType> types;

        public TypeAdapter(Context context, List<ItemType> types) {
            this.context = context;
            this.types = types;
        }

        public class TypeItemViewHolder extends RecyclerView.ViewHolder {
            ImageView ivColorType;
            TextView tvNameType;

            public TypeItemViewHolder(View view) {
                super(view);
                ivColorType = view.findViewById(R.id.ivColorType);
                tvNameType = view.findViewById(R.id.tvNameType);
            }
        }

        @Override
        public TypeItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = getLayoutInflater();
            View view = layoutInflater.inflate(R.layout.type_item, parent, false);
            return new TypeItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(TypeItemViewHolder holder, int position) {
            final ItemType itemType = types.get(position);
            final int color = Color.rgb(itemType.getRed(), itemType.getGreen(), itemType.getBlue());
            holder.ivColorType.setColorFilter(color, android.graphics.PorterDuff.Mode.SRC_IN);
            holder.tvNameType.setText(itemType.getName());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setButtonItemTypeColor(color);
                    btnItemTypeAdd.setText(itemType.getName());
                    typeDialog.dismiss();
                }
            });
        }

        @Override
        public int getItemCount() {
            return types.size();
        }
    }

    List<ItemType> getTypes() {
        return itemSQLiteOpenHelper.getAllTypes();
    }

    void setButtonItemTypeColor(int color) {
        Resources resources = getResources();
        Drawable drawable = resources.getDrawable(R.drawable.ic_lens_black_24dp);
        drawable.setColorFilter(color, android.graphics.PorterDuff.Mode.SRC_IN);
        btnItemTypeAdd.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
    }

    int getItemTypeColor(String typeName) {
        return itemSQLiteOpenHelper.selectTypeColor(typeName);
    }

}
