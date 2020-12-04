package com.itschool.buzuverov.forgdo.Model.Avatar;

import android.view.View;

public class Avatar {
    private View selectIconCheck;
    private View selectIcon;

    public Avatar(View selectIcon, View selectIconCheck) {
        this.selectIcon = selectIcon;
        this.selectIconCheck = selectIconCheck;
    }

    public void setSelect(boolean isSelect) {
        selectIconCheck.setVisibility(isSelect ? View.VISIBLE : View.GONE);
        selectIcon.setEnabled(!isSelect);
    }

    public int getSelectAvatarId() {
        return selectIcon.getId();
    }
}
