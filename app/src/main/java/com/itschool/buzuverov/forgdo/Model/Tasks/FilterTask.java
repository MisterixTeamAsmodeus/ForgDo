package com.itschool.buzuverov.forgdo.Model.Tasks;

public class FilterTask {
    public static final int OUTSTANDING = 0;
    public static final int ALL = 1;
    public static final int RECENT = 2;
    public static final int TODAY = 3;
    public static final int COMPLETED = 4;
    private String textGroup;
    private int filter;
    private boolean isSelect;

    public FilterTask(String textGroup, int filter) {
        this.textGroup = textGroup;
        this.filter = filter;
    }

    public int getFilter() {
        return filter;
    }

    public String getTextGroup() {
        return textGroup;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
