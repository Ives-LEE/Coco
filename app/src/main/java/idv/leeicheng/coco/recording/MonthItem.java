package idv.leeicheng.coco.recording;

public class MonthItem extends ListItem{

    private int month;
    private long monthSpent;

    public MonthItem(int month, long monthSpent) {
        this.month = month;
        this.monthSpent = monthSpent;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public long getMonthSpent() {
        return monthSpent;
    }

    public void setMonthSpent(long monthSpent) {
        this.monthSpent = monthSpent;
    }

    @Override
    public int getType() {
        return 0;
    }
}
