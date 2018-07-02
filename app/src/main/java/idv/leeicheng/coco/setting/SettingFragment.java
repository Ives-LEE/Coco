package idv.leeicheng.coco.setting;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import idv.leeicheng.coco.R;

public class SettingFragment extends Fragment {
    RecyclerView rvSetting;
    List<String> settings;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.fragment_setting,container,false);
        setSetting();
        findViews(view);
        return view;
    }

    private void findViews(View view) {
        rvSetting = view.findViewById(R.id.rvSetting);
        viewsControl(view);
    }

    private void viewsControl(View view) {
        rvSetting.setLayoutManager(new GridLayoutManager(getActivity(),3, GridLayoutManager.VERTICAL,false));
        rvSetting.setAdapter(new SettingAdapter());
    }

    void setSetting(){
        settings = new ArrayList<>();
        settings.add(String.valueOf(R.string.typeColor));
    }


    private class SettingAdapter extends RecyclerView.Adapter<SettingAdapter.SettingItemViewHolder> {

        public class SettingItemViewHolder extends RecyclerView.ViewHolder {
            Button btnItemSetting;
            public SettingItemViewHolder(View view) {
                super(view);
                btnItemSetting = view.findViewById(R.id.btnSettingItem);
            }
        }

        @Override
        public SettingItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = getLayoutInflater();
            View view = layoutInflater.inflate(R.layout.setting_item,parent,false);
            return new SettingItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(SettingItemViewHolder holder, int position) {
            String item = settings.get(position);
            if (item.equals(String.valueOf(R.string.typeColor))){
                holder.btnItemSetting.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.ic_view_comfy_black_24dp,0,0);
                holder.btnItemSetting.setText(R.string.typeColor);
                holder.btnItemSetting.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(),EditItemTypeActivity.class);
                        startActivity(intent);
                    }
                });

            }

        }

        @Override
        public int getItemCount() {
            return settings.size();
        }

    }
}
