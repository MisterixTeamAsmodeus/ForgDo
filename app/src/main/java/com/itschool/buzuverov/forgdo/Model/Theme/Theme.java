package com.itschool.buzuverov.forgdo.Model.Theme;

import android.view.View;

public class Theme {
    private View themeImage;
    private View selectThemeImage;
    private int themeRecourse;

    public Theme(View themeImage, View selectThemeImage, int themeRecourse) {
        this.themeImage = themeImage;
        this.selectThemeImage = selectThemeImage;
        this.themeRecourse = themeRecourse;
    }

    public void setSelect(boolean isSelect) {
        selectThemeImage.setVisibility(isSelect ? View.VISIBLE : View.GONE);
        themeImage.setEnabled(!isSelect);
    }

    public int getThemeRecourse() {
        return themeRecourse;
    }
}
