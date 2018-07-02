package idv.leeicheng.coco.recording;

import java.io.Serializable;

public class RecordingItem extends ListItem implements Serializable {

    private int id;
    private String date;
    private String itemType;
    private long spent;
    private String itemName;
    private byte[] image;
    private boolean isSelected = false;

    public RecordingItem(String date, String itemType, long spent, String itemName) {
        this.date = date;
        this.itemType = itemType;
        this.spent = spent;
        this.itemName = itemName;
    }

    public RecordingItem(String date, String itemType, long spent, String itemName, byte[] image) {
        this.date = date;
        this.itemType = itemType;
        this.spent = spent;
        this.itemName = itemName;
        this.image = image;
    }

    public RecordingItem(int id, String date, String itemType, long spent, String itemName, byte[] image) {
        this.id = id;
        this.date = date;
        this.itemType = itemType;
        this.spent = spent;
        this.itemName = itemName;
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public long getSpent() {
        return spent;
    }

    public void setSpent(long spent) {
        this.spent = spent;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int getType() {
        return TYPE_ITEM;
    }


    public boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
