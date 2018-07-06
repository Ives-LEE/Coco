package idv.leeicheng.coco.recording;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
    AlertDialog colorDialog;
    TextView tvTotal, tvNoRecording;
    ItemSQLiteOpenHelper itemSQLiteOpenHelper;
    RecyclerView rvMyRecording;
    RecordingItemAdapter recordingItemAdapter;
    List<ListItem> toDayItems;
    FloatingActionButton fabAdd;

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
        fabAdd = view.findViewById(R.id.fabAdd);
        tvNoRecording = view.findViewById(R.id.tvNoRecording);
        rvMyRecording = view.findViewById(R.id.rvMyRecording);
        tvTotal = view.findViewById(R.id.tvTotal);
        viewsControl();

    }

    private void viewsControl() {
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), AddItemActivity.class);
                startActivity(intent);
            }
        });
    }

    List<ListItem> getAllRecordingItem() {
        return itemSQLiteOpenHelper.getAllRecording(getFormatDay());
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
            fabAdd.setVisibility(View.GONE);

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
        fabAdd.setVisibility(View.VISIBLE);
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
