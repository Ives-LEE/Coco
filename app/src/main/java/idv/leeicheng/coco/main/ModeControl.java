package idv.leeicheng.coco.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.EditText;


import java.lang.ref.WeakReference;

import idv.leeicheng.coco.R;

import static idv.leeicheng.coco.main.Common.TAG;
import static idv.leeicheng.coco.main.MainActivity.ibTopLeft;
import static idv.leeicheng.coco.main.MainActivity.ibTopRight;
import static idv.leeicheng.coco.main.MainActivity.tlMain;
import static idv.leeicheng.coco.main.MainActivity.tvTopDay;

public class ModeControl {
    private static boolean isEditType = false;
    private static boolean isAppWidget = false;
    private static boolean isInputSpent = false;
    private static boolean isEdit = false;

    public static void normalMode(){
        ibTopLeft.setVisibility(View.VISIBLE);
        ibTopRight.setVisibility(View.VISIBLE);
        tlMain.setVisibility(View.VISIBLE);
    }

    public static void setting(){
        tvTopDay.setText(R.string.setting);
        tvTopDay.setOnClickListener(null);
        ibTopLeft.setVisibility(View.GONE);
        ibTopRight.setVisibility(View.GONE);
        tlMain.setVisibility(View.GONE);
    }



    public static boolean getIsEditType() {
        return isEditType;
    }

    public static void setIsEditType(boolean isEditType) {
        ModeControl.isEditType = isEditType;
    }

    public static boolean isIsAppWidget() {
        return isAppWidget;
    }

    public static void setIsAppWidget(boolean isAppWidget) {
        ModeControl.isAppWidget = isAppWidget;
    }

    public static boolean getIsInputSpent() {
        return isInputSpent;
    }

    public static void setIsInputSpent(boolean isInputSpent) {
        ModeControl.isInputSpent = isInputSpent;
    }

    public static boolean getIsEdit() {
        return isEdit;
    }

    public static void setIsEdit(boolean isEdit) {
        ModeControl.isEdit = isEdit;
    }

    public static void showAndHideNumberKeyboard(FragmentManager fragmentManager,EditText editText) {
        if (!isInputSpent) {
            showNumberKeyboard(fragmentManager, editText);
        } else {
            hideNumberKeyboard(fragmentManager);
        }
    }

    public static void showNumberKeyboard(FragmentManager fragmentManager, EditText editText) {
        isInputSpent = true;
        NumberKeyboardFragment fragment = new NumberKeyboardFragment();
        fragment.setWeakReferenceEditText(editText);
        fragmentManager.beginTransaction().replace(R.id.flNumberKeyboard, fragment, TAG).commit();
    }

    public static void hideNumberKeyboard(FragmentManager fragmentManager) {
        Fragment fragment = fragmentManager.findFragmentByTag(TAG);
        if (fragment != null) {
            isInputSpent = false;
            fragmentManager.beginTransaction().remove(fragment).commit();
        }
    }
}
