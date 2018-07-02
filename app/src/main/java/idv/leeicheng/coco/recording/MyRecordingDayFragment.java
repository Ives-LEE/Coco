package idv.leeicheng.coco.recording;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;


import java.lang.ref.WeakReference;
import java.util.List;
import java.util.TreeSet;

import idv.leeicheng.coco.R;
import idv.leeicheng.coco.main.Common;
import idv.leeicheng.coco.main.ItemSQLiteOpenHelper;
import idv.leeicheng.coco.main.MainActivity;
import idv.leeicheng.coco.main.NumberKeyboardFragment;
import idv.leeicheng.coco.setting.ItemType;

import static idv.leeicheng.coco.main.CalendarControl.getFormatDay;
import static idv.leeicheng.coco.main.ModeControl.getIsEdit;
import static idv.leeicheng.coco.main.ModeControl.getIsInputSpent;
import static idv.leeicheng.coco.main.ModeControl.hideNumberKeyboard;
import static idv.leeicheng.coco.main.ModeControl.setIsEdit;
import static idv.leeicheng.coco.main.ModeControl.setIsInputSpent;
import static idv.leeicheng.coco.main.ModeControl.showAndHideNumberKeyboard;

public class MyRecordingDayFragment extends Fragment {
    public static EditText etInputCost;
    ImageButton ibColorInputCard, ibAddInputCard;
    AlertDialog colorDialog;
    TextView tvInputItem, tvTotal, tvNoRecording;
    ItemSQLiteOpenHelper itemSQLiteOpenHelper;
    RecyclerView rvMyRecording;
    CardView cvInputMyRecording;
    RecordingItemAdapter recordingItemAdapter;
    List<ListItem> toDayItems;

    ItemType selectType;
    long allSpend;


    TreeSet<Integer> willDeleteIds;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.fragment_day_recording, container, false);
        allSpend = 0;
        if (getIsEdit()) {
            commonMode();
            setIsEdit(false);
        }
        if (itemSQLiteOpenHelper == null) {
            itemSQLiteOpenHelper = new ItemSQLiteOpenHelper(getActivity());
        }
        if (getIsInputSpent()) setIsInputSpent(false);
        findViews(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getIsEdit()) {
            commonMode();
            setIsEdit(false);
        }
        if (getIsInputSpent()) setIsInputSpent(false);
        setAdatper();
    }

    @Override
    public void onResume() {
        super.onResume();
        hideNumberKeyboard(getChildFragmentManager());
        allSpend = 0;
        if (getIsEdit()) {
            commonMode();
            setIsEdit(false);
        }
        if (getIsInputSpent()) setIsInputSpent(false);
        tvInputItem.setText(R.string.itemType);
        etInputCost.setHint(R.string.paid);
        ibColorInputCard.setColorFilter(getResources().getColor(R.color.colorGray), android.graphics.PorterDuff.Mode.SRC_IN);
        setAdatper();
    }

    void setAdatper() {
        allSpend = 0;
        toDayItems = getAllRecordingItem();
        tvTotal.setText(String.valueOf(calculationSpend()));
        rvMyRecording.setVisibility(View.VISIBLE);
        tvNoRecording.setVisibility(View.INVISIBLE);
        if (toDayItems.size() <= 0) {
            rvMyRecording.setVisibility(View.INVISIBLE);
            tvNoRecording.setHint(R.string.noRecording);
            tvNoRecording.setVisibility(View.VISIBLE);
        }
        if (recordingItemAdapter == null) {
            recordingItemAdapter = new RecordingItemAdapter(getActivity(), toDayItems);
            rvMyRecording.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            rvMyRecording.setAdapter(recordingItemAdapter);
        } else {
            recordingItemAdapter.setRecordingItems(toDayItems);
            recordingItemAdapter.notifyDataSetChanged();
        }
    }

    private void findViews(View view) {
        ibAddInputCard = view.findViewById(R.id.ibAddInputCard);
        tvInputItem = view.findViewById(R.id.tvInputItem);
        tvNoRecording = view.findViewById(R.id.tvNoRecording);
        etInputCost = view.findViewById(R.id.etInputCost);
        rvMyRecording = view.findViewById(R.id.rvMyRecording);
        ibColorInputCard = view.findViewById(R.id.ibColorInputCard);
        tvTotal = view.findViewById(R.id.tvTotal);
        cvInputMyRecording = view.findViewById(R.id.cvInputMyRecording);
        viewsControl();

    }

    private void viewsControl() {
        tvInputItem.setText(R.string.itemType);
        etInputCost.setHint(R.string.paid);

        etInputCost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAndHideNumberKeyboard(getChildFragmentManager(),etInputCost);
            }
        });


        ibAddInputCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allSpend = 0;
                hideNumberKeyboard(getChildFragmentManager());
                if (selectType != null) {
                    String date = getFormatDay();
                    String itemName = "";
                    String cost = etInputCost.getText().toString().trim();
                    long spent = 0;
                    if (!cost.equals("")) {
                        spent = Long.valueOf(cost);
                    }
                    itemSQLiteOpenHelper.insertRecording(new RecordingItem(date, selectType.getName(), spent, itemName));
                    setAdatper();
                    ibColorInputCard.setColorFilter(getResources().getColor(R.color.colorGray), PorterDuff.Mode.SRC_IN);
                    selectType = null;
                    tvInputItem.setText(R.string.itemType);
                    etInputCost.setText(null);
                }
            }
        });

        ibAddInputCard.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), AddItemActivity.class);
                startActivity(intent);
                return false;
            }
        });

        View.OnClickListener showTypeDialog = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideNumberKeyboard(getChildFragmentManager());
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
                View dialogView = getLayoutInflater().inflate(R.layout.choose_color_dialog, null);
                RecyclerView rvColorDialog = dialogView.findViewById(R.id.rvColorDialog);
                List<ItemType> types = getTypes();
                rvColorDialog.setLayoutManager(new GridLayoutManager(getActivity(), 4, GridLayoutManager.VERTICAL, false));
                rvColorDialog.setAdapter(new ColorAdapter(getActivity(), types));
                dialogBuilder.setView(dialogView);
                colorDialog = dialogBuilder.create();
                colorDialog.show();
            }
        };
        tvInputItem.setOnClickListener(showTypeDialog);
        ibColorInputCard.setOnClickListener(showTypeDialog);
    }

    List<ListItem> getAllRecordingItem() {
        return itemSQLiteOpenHelper.getAllRecording(getFormatDay());
    }

    private class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ColorViewHolder> {
        Context context;
        List<ItemType> types;

        public ColorAdapter(Context context, List<ItemType> types) {
            this.context = context;
            this.types = types;
        }

        public class ColorViewHolder extends RecyclerView.ViewHolder {
            CardView cvColor;
            TextView tvColorItemName;

            public ColorViewHolder(View view) {
                super(view);
                cvColor = view.findViewById(R.id.cvColor);
                tvColorItemName = view.findViewById(R.id.tvColorItemName);
            }
        }

        @Override
        public ColorAdapter.ColorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = getLayoutInflater();
            View view = layoutInflater.inflate(R.layout.color_card, parent, false);

            return new ColorViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ColorAdapter.ColorViewHolder holder, int position) {
            final ItemType itemType = types.get(position);
            final int color = Color.rgb(itemType.getRed(), itemType.getGreen(), itemType.getBlue());
            holder.cvColor.setCardBackgroundColor(color);
            holder.tvColorItemName.setText(itemType.getName());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectType = itemType;
                    ibColorInputCard.setColorFilter(color, android.graphics.PorterDuff.Mode.SRC_IN);
                    tvInputItem.setText(itemType.getName());
                    colorDialog.dismiss();
                }
            });
        }

        @Override
        public int getItemCount() {
            return types.size();
        }
    }


    private class RecordingItemAdapter extends RecyclerView.Adapter {
        Context context;
        List<ListItem> recordingItems;

        public RecordingItemAdapter(Context context, List<ListItem> recordingItems) {
            this.context = context;
            this.recordingItems = recordingItems;
        }

        private class ItemViewHolder extends RecyclerView.ViewHolder {
            ImageButton ibColorItemCard;
            TextView tvItemName, tvItemSpent;

            public ItemViewHolder(View view) {
                super(view);
                ibColorItemCard = view.findViewById(R.id.ibColorItemCard);
                tvItemName = view.findViewById(R.id.tvItemName);
                tvItemSpent = view.findViewById(R.id.tvItemSpent);
            }
        }

        public void setRecordingItems(List<ListItem> recordingItems) {
            this.recordingItems = recordingItems;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = getLayoutInflater();

            View view = layoutInflater.inflate(R.layout.item_card, parent, false);
            return new ItemViewHolder(view);

        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            final ItemViewHolder itemViewHolder = new ItemViewHolder(holder.itemView);
            final RecordingItem recordingItem = (RecordingItem) recordingItems.get(position);
            String type = recordingItem.getItemType();
            final String itemName = recordingItem.getItemName();
            String spent = String.valueOf(recordingItem.getSpent());
            itemViewHolder.tvItemName.setText(type + " " + itemName);
            itemViewHolder.tvItemSpent.setText(spent);
            tvTotal.setText(String.valueOf(allSpend));
            if (!getIsEdit()) {
                int color = itemSQLiteOpenHelper.selectTypeColor(type);
                itemViewHolder.ibColorItemCard.setImageResource(R.drawable.ic_lens_black_24dp);
                itemViewHolder.ibColorItemCard.setColorFilter(color, android.graphics.PorterDuff.Mode.SRC_IN);
                itemViewHolder.ibColorItemCard.setOnClickListener(null);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("itemId", recordingItem.getId());
                        Intent intent = new Intent(getContext(), AddItemActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });

                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        setIsEdit(true);
                        editMode();
                        setAdatper();
                        return false;
                    }
                });

            } else {
                willDeleteIds = new TreeSet<>();
                itemViewHolder.itemView.setOnLongClickListener(null);
                itemViewHolder.itemView.setOnClickListener(null);
                int color = getResources().getColor(R.color.colorGray);
                itemViewHolder.ibColorItemCard.setImageResource(R.drawable.ic_check_box_outline_blank_black_24dp);
                itemViewHolder.ibColorItemCard.setColorFilter(color, android.graphics.PorterDuff.Mode.SRC_IN);

                View.OnClickListener clickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int color = getResources().getColor(R.color.colorRed);
                        itemViewHolder.ibColorItemCard.setImageResource(R.drawable.ic_check_box_black_24dp);
                        itemViewHolder.ibColorItemCard.setColorFilter(color, android.graphics.PorterDuff.Mode.SRC_IN);

                        if (recordingItem.getIsSelected()) {
                            recordingItem.setIsSelected(false);
                            color = getResources().getColor(R.color.colorGray);
                            itemViewHolder.ibColorItemCard.setImageResource(R.drawable.ic_check_box_outline_blank_black_24dp);
                            itemViewHolder.ibColorItemCard.setColorFilter(color, android.graphics.PorterDuff.Mode.SRC_IN);
                            willDeleteIds.remove(recordingItem.getId());

                        } else {
                            recordingItem.setIsSelected(true);
                            willDeleteIds.add(recordingItem.getId());

                        }
                    }
                };
                itemViewHolder.ibColorItemCard.setOnClickListener(clickListener);
                itemViewHolder.itemView.setOnClickListener(clickListener);
            }
        }

        @Override
        public int getItemCount() {
            return recordingItems.size();
        }

        @Override
        public int getItemViewType(int position) {
            return recordingItems.get(position).getType();
        }
    }

    long calculationSpend() {
        for (ListItem item : toDayItems) {
            RecordingItem recordingItem = (RecordingItem) item;
            long spend = recordingItem.getSpent();
            allSpend += spend;
        }
        return allSpend;
    }

    List<ItemType> getTypes() {
        return itemSQLiteOpenHelper.getAllTypes();
    }

    void editMode() {
        if (getIsEdit()) {
            deleteMode();
            cvInputMyRecording.setVisibility(View.GONE);

            View.OnClickListener clickRightListenerEditMode = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (getIsEdit()) {
                        showDeleteDialog();
                    }
                }
            };

            View.OnClickListener clickLeftListenerEditMode = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (getIsEdit()) {
                        setIsEdit(false);
                        commonMode();
                    }
                }
            };

            MainActivity.ibTopRight.setOnClickListener(clickRightListenerEditMode);
            MainActivity.ibTopLeft.setOnClickListener(clickLeftListenerEditMode);
        } else {
            commonMode();
        }
    }

    void commonMode() {
        resetToolbar();
        cvInputMyRecording.setVisibility(View.VISIBLE);
        MainActivity.ibTopRight.setOnClickListener(MainActivity.clickRightListener);
        MainActivity.ibTopLeft.setOnClickListener(MainActivity.clickLeftListener);
        setAdatper();
    }

    void showDeleteDialog() {
        if (willDeleteIds == null) {
            return;
        }
        int count = willDeleteIds.size();
        new AlertDialog.Builder(getActivity())
                .setMessage("確定刪除 " + count + " 個記錄嗎？")
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        for (int id : willDeleteIds) {
                            itemSQLiteOpenHelper.deleteRecordingById(id);
                        }
                        setIsEdit(false);
                        editMode();
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
    public void onDestroy() {
        super.onDestroy();
        if (itemSQLiteOpenHelper != null) {
            itemSQLiteOpenHelper.close();
        }
    }

    void deleteMode() {
        MainActivity.tbMain.setBackgroundColor(getResources().getColor(R.color.colorBlack));
        int color = getResources().getColor(R.color.colorWhite);
        MainActivity.ibTopLeft.setImageResource(R.drawable.ic_arrow_back_black_24dp);
        MainActivity.ibTopRight.setImageResource(R.drawable.ic_delete_black_24dp);
        MainActivity.ibTopLeft.setColorFilter(color, android.graphics.PorterDuff.Mode.SRC_IN);
        MainActivity.ibTopRight.setColorFilter(color, android.graphics.PorterDuff.Mode.SRC_IN);
        MainActivity.bnvMain.setVisibility(View.GONE);
        MainActivity.tlMain.setVisibility(View.GONE);

    }

    void resetToolbar() {
        MainActivity.tbMain.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        int color = getResources().getColor(R.color.colorWhite);
        MainActivity.ibTopLeft.setImageResource(R.drawable.ic_chevron_left_black_24dp);
        MainActivity.ibTopRight.setImageResource(R.drawable.ic_chevron_right_black_24dp);
        MainActivity.ibTopLeft.setColorFilter(color, android.graphics.PorterDuff.Mode.SRC_IN);
        MainActivity.ibTopRight.setColorFilter(color, android.graphics.PorterDuff.Mode.SRC_IN);
        MainActivity.bnvMain.setVisibility(View.VISIBLE);
        MainActivity.tlMain.setVisibility(View.VISIBLE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (getIsInputSpent()) setIsInputSpent(false);
    }
}
