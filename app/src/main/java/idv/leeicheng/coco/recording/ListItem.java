package idv.leeicheng.coco.recording;

import java.io.Serializable;

public abstract class ListItem implements Serializable {
    public static final int TYPE_DATE = 0;
    public static final int TYPE_ITEM = 1;
    public abstract int getType();
}
