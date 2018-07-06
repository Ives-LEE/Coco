package idv.leeicheng.coco.setting;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;



import java.util.List;
import java.util.TreeSet;

import idv.leeicheng.coco.R;
import idv.leeicheng.coco.main.ItemSQLiteOpenHelper;

import static idv.leeicheng.coco.main.ModeControl.getIsEditType;
import static idv.leeicheng.coco.main.ModeControl.setIsEditType;

public class EditItemTypeActivity extends AppCompatActivity {
    RecyclerView rvType;
    ImageButton ibLeftType, ibRightType;
    ItemSQLiteOpenHelper typeSQLiteOpenHelper;
    TypeAdapter typeAdapter;
    Toolbar tbEditType;
    TreeSet<Integer> willDeletes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item_type);
        if (typeSQLiteOpenHelper == null) {
            typeSQLiteOpenHelper = new ItemSQLiteOpenHelper(this);
        }
        if (getIsEditType()) {
            setIsEditType(false);
            normalTypeMode();
        }
        willDeletes = new TreeSet<>();
        findViews();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (getIsEditType()) {
            normalTypeMode();
            setIsEditType(false);
        }
        setAdapter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getIsEditType()) {
            normalTypeMode();
            setIsEditType(false);
        }
        setAdapter();
    }

    void setAdapter() {
        List<ItemType> types = getTypes();
        if (types.size() < 0) return;
        if (typeAdapter == null) {
            typeAdapter = new TypeAdapter(this, types);
            rvType.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            rvType.setAdapter(typeAdapter);
        } else {
            typeAdapter.setTypes(types);
            typeAdapter.notifyDataSetChanged();
        }
    }

    void findViews() {
        tbEditType = findViewById(R.id.tbEditType);
        rvType = findViewById(R.id.rvType);
        ibLeftType = findViewById(R.id.ibLeftType);
        ibRightType = findViewById(R.id.ibRightType);
        viewsControl();
    }

    void viewsControl() {
        ibLeftType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getIsEditType()) {
                    normalTypeMode();
                    willDeletes.clear();
                    setIsEditType(false);
                } else {
                    finish();
                }
            }
        });

        ibRightType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getIsEditType()) {
                    showDeleteDialog();
                } else {
                    EditTypeDialogFragment editTypeDialogFragment = new EditTypeDialogFragment();
                    editTypeDialogFragment.setItemType(null);
                    editTypeDialogFragment.setTypeAdapter(typeAdapter);
                    editTypeDialogFragment.show(getSupportFragmentManager(), "addDialog");
                }

            }
        });
    }

    public class TypeAdapter extends RecyclerView.Adapter<TypeAdapter.TypeItemViewHolder> {
        Context context;
        List<ItemType> types;

        public TypeAdapter(Context context, List<ItemType> types) {
            this.context = context;
            this.types = types;
        }

        public void setTypes(List<ItemType> types) {
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
        public void onBindViewHolder(final TypeItemViewHolder holder, int position) {
            final ItemType itemType = types.get(position);
            itemType.setSelected(false);
            if (getIsEditType()) {
                int color = getResources().getColor(R.color.colorGray);
                holder.tvNameType.setText(itemType.getName());
                holder.ivColorType.setImageResource(R.drawable.ic_check_box_outline_blank_black_24dp);
                holder.ivColorType.setColorFilter(color, android.graphics.PorterDuff.Mode.SRC_IN);
                holder.itemView.setOnLongClickListener(null);
                View.OnClickListener onClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int color = getResources().getColor(R.color.colorRed);
                        holder.ivColorType.setImageResource(R.drawable.ic_check_box_black_24dp);
                        holder.ivColorType.setColorFilter(color, android.graphics.PorterDuff.Mode.SRC_IN);
                        if (itemType.isSelected()) {
                            itemType.setSelected(false);
                            color = getResources().getColor(R.color.colorGray);
                            holder.ivColorType.setImageResource(R.drawable.ic_check_box_outline_blank_black_24dp);
                            holder.ivColorType.setColorFilter(color, android.graphics.PorterDuff.Mode.SRC_IN);
                            willDeletes.remove(itemType.getId());

                        } else {
                            itemType.setSelected(true);
                            willDeletes.add(itemType.getId());
                        }
                    }
                };
                holder.itemView.setOnClickListener(onClickListener);
                holder.ivColorType.setOnClickListener(onClickListener);
            } else {
                holder.ivColorType.setImageResource(R.drawable.ic_lens_black_24dp);
                holder.ivColorType.setColorFilter(Color.rgb(itemType.getRed(), itemType.getGreen(), itemType.getBlue()), android.graphics.PorterDuff.Mode.SRC_IN);
                holder.tvNameType.setText(itemType.getName());

                View.OnClickListener clickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EditTypeDialogFragment editTypeDialogFragment = new EditTypeDialogFragment();
                        editTypeDialogFragment.setItemType(itemType);
                        editTypeDialogFragment.setTypeAdapter(typeAdapter);
                        editTypeDialogFragment.show(getSupportFragmentManager(), "dialog");
                    }
                };

                View.OnLongClickListener longClickListener = new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        if (getIsEditType()) {
                            setIsEditType(false);
                            normalTypeMode();
                        } else {
                            setIsEditType(true);
                            deleteTypeMode();
                        }
                        return false;
                    }
                };

                holder.itemView.setOnClickListener(clickListener);
                holder.itemView.setOnLongClickListener(longClickListener);
                holder.ivColorType.setOnClickListener(clickListener);
                holder.ivColorType.setOnLongClickListener(longClickListener);
            }
        }

        @Override
        public int getItemCount() {
            return types.size();
        }

    }

    List<ItemType> getTypes() {
        return typeSQLiteOpenHelper.getAllTypes();
    }

    public void normalTypeMode() {
        tbEditType.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        ibLeftType.setImageResource(R.drawable.ic_arrow_back_black_24dp);
        ibRightType.setImageResource(R.drawable.ic_add_black_24dp);
        willDeletes.clear();
        setAdapter();
    }

    public void deleteTypeMode() {
        tbEditType.setBackgroundColor(getResources().getColor(R.color.colorBlack));
        ibLeftType.setImageResource(R.drawable.ic_close_black_24dp);
        ibRightType.setImageResource(R.drawable.ic_delete_black_24dp);
        setAdapter();
    }

    void showDeleteDialog() {
        if (willDeletes == null) {
            return;
        }
        int count = willDeletes.size();
        new AlertDialog.Builder(this)
                .setMessage("確定刪除 " + count + " 個記錄嗎？")
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        for (int id : willDeletes) {
                            typeSQLiteOpenHelper.deleteTypeById(id);
                        }
                        setIsEditType(false);
                        normalTypeMode();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (getIsEditType()) setIsEditType(false);
    }
}
