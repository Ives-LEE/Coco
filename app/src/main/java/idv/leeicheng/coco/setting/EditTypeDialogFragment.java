package idv.leeicheng.coco.setting;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;


import java.util.List;

import idv.leeicheng.coco.R;
import idv.leeicheng.coco.main.ItemSQLiteOpenHelper;

public class EditTypeDialogFragment extends DialogFragment {
    ItemSQLiteOpenHelper itemSQLiteOpenHelper;
    ItemType itemType;
    TextView tvRedValue, tvGreenValue, tvBlueValue;
    SeekBar sbRed, sbGreen, sbBlue;
    ImageView ivColorDailog;
    EditText etTypeNameDailog;
    EditItemTypeActivity.TypeAdapter typeAdapter;

    int red = 0;
    int green = 0;
    int blue = 0;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (itemSQLiteOpenHelper == null) {
            itemSQLiteOpenHelper = new ItemSQLiteOpenHelper(getActivity());
        }
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View view = layoutInflater.inflate(R.layout.edit_type_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        findViews(view);

        if (itemType != null) {
            etTypeNameDailog.setHint(itemType.getName());
            red = itemType.getRed();
            green = itemType.getGreen();
            blue = itemType.getBlue();
            sbRed.setProgress(red);
            sbGreen.setProgress(green);
            sbBlue.setProgress(blue);
            tvRedValue.setText(String.valueOf(red));
            tvGreenValue.setText(String.valueOf(green));
            tvBlueValue.setText(String.valueOf(blue));
            ivColorDailog.setColorFilter(Color.rgb(red, green, blue), android.graphics.PorterDuff.Mode.SRC_IN);
            builder.setTitle(R.string.editType)
                    .setView(view)
                    .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String newName = etTypeNameDailog.getText().toString().trim();
                            if (!newName.equals("")) {
                                itemType.setName(newName);
                            }
                            itemType.setRed(red);
                            itemType.setGreen(green);
                            itemType.setBlue(blue);
                            itemSQLiteOpenHelper.updateType(itemType);
                            dialogInterface.dismiss();
                            typeAdapter.notifyDataSetChanged();
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
        } else {
            etTypeNameDailog.setHint(getResources().getString(R.string.createType));
            builder.setTitle(R.string.createType)
                    .setView(view)
                    .setPositiveButton(R.string.create, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String name = etTypeNameDailog.getText().toString().trim();
                            if (name.equals("")) return;
                            ItemType itemType = new ItemType(name, red, green, blue);
                            itemSQLiteOpenHelper.insertType(itemType);
                            dialogInterface.dismiss();
                            typeAdapter.setTypes(getTypes());
                            typeAdapter.notifyDataSetChanged();
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
        }

        SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int value, boolean b) {
                if (seekBar.getId() == R.id.sbRed) {
                    red = value;
                    tvRedValue.setText(String.valueOf(red));
                } else if (seekBar.getId() == R.id.sbGreen) {
                    green = value;
                    tvGreenValue.setText(String.valueOf(green));
                } else if (seekBar.getId() == R.id.sbBlue) {
                    blue = value;
                    tvBlueValue.setText(String.valueOf(blue));
                }
                ivColorDailog.setColorFilter(Color.rgb(red, green, blue), android.graphics.PorterDuff.Mode.SRC_IN);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        };
        sbRed.setOnSeekBarChangeListener(onSeekBarChangeListener);
        sbGreen.setOnSeekBarChangeListener(onSeekBarChangeListener);
        sbBlue.setOnSeekBarChangeListener(onSeekBarChangeListener);

        return builder.create();
    }

    void findViews(View view) {
        etTypeNameDailog = view.findViewById(R.id.etTypeNameDailog);
        ivColorDailog = view.findViewById(R.id.ivColorDailog);
        sbRed = view.findViewById(R.id.sbRed);
        sbGreen = view.findViewById(R.id.sbGreen);
        sbBlue = view.findViewById(R.id.sbBlue);
        tvRedValue = view.findViewById(R.id.tvRedValue);
        tvGreenValue = view.findViewById(R.id.tvGreenValue);
        tvBlueValue = view.findViewById(R.id.tvBlueValue);
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }

    public void setTypeAdapter(EditItemTypeActivity.TypeAdapter typeAdapter) {
        this.typeAdapter = typeAdapter;
    }

    List<ItemType> getTypes() {
        return itemSQLiteOpenHelper.getAllTypes();
    }
}
