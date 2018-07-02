package idv.leeicheng.coco.setting;

import android.graphics.Color;

public class ItemType {

    private int id;
    private String name;
    private int red;
    private int green;
    private int blue;
    private int defultColor;
    private boolean isSelected = false;


    public ItemType(String name, int defultColor) {
        this.name = name;
        this.defultColor = defultColor;
    }

    public ItemType(int id, String name, int red, int green, int blue) {
        this.id = id;
        this.name = name;
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public ItemType(String name, int red, int green, int blue) {
        this.name = name;
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRed() {
        return red;
    }

    public void setRed(int red) {
        this.red = red;
    }

    public int getGreen() {
        return green;
    }

    public void setGreen(int green) {
        this.green = green;
    }

    public int getBlue() {
        return blue;
    }

    public void setBlue(int blue) {
        this.blue = blue;
    }

    public int getDefultColor() {
        return defultColor;
    }

    public void setDefultColor(int defultColor) {
        this.defultColor = defultColor;
    }

    public int getColor(){
        return Color.rgb(red,green,blue);
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
