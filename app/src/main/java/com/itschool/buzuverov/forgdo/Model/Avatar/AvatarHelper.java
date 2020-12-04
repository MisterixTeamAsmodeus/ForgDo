package com.itschool.buzuverov.forgdo.Model.Avatar;

import android.content.Context;
import android.content.SharedPreferences;

import com.itschool.buzuverov.forgdo.R;

import static android.content.Context.MODE_PRIVATE;

public class AvatarHelper {
    private Avatar selectAvatar;
    private Context context;

    public AvatarHelper(Context context) {
        this.context = context;
    }

    public void selectAvatar(Avatar avatar) {
        if (selectAvatar != null) {
            selectAvatar.setSelect(false);
        }
        avatar.setSelect(true);
        selectAvatar = avatar;
        SharedPreferences.Editor preferences = context.getSharedPreferences("Preferences", MODE_PRIVATE).edit();
        preferences.putInt("User avatar", selectAvatar.getSelectAvatarId());
        preferences.apply();
    }

    public int getSelectAvatarId() {
        return context.getSharedPreferences("Preferences", MODE_PRIVATE).getInt("User avatar", R.id.select_avatar_activity_avatar_1);
    }

    public static int getSelectAvatar(int id) {
        switch (id) {
            case R.id.select_avatar_activity_avatar_2:
                return R.drawable.ic_avatar_2;
            case R.id.select_avatar_activity_avatar_3:
                return R.drawable.ic_avatar_3;
            case R.id.select_avatar_activity_avatar_4:
                return R.drawable.ic_avatar_4;
            case R.id.select_avatar_activity_avatar_5:
                return R.drawable.ic_avatar_5;
            case R.id.select_avatar_activity_avatar_6:
                return R.drawable.ic_avatar_6;
            case R.id.select_avatar_activity_avatar_7:
                return R.drawable.ic_avatar_7;
            case R.id.select_avatar_activity_avatar_8:
                return R.drawable.ic_avatar_8;
            case R.id.select_avatar_activity_avatar_9:
                return R.drawable.ic_avatar_9;
            case R.id.select_avatar_activity_avatar_10:
                return R.drawable.ic_avatar_10;
            case R.id.select_avatar_activity_avatar_11:
                return R.drawable.ic_avatar_11;
            case R.id.select_avatar_activity_avatar_12:
                return R.drawable.ic_avatar_12;
            case R.id.select_avatar_activity_avatar_13:
                return R.drawable.ic_avatar_13;
            case R.id.select_avatar_activity_avatar_14:
                return R.drawable.ic_avatar_14;
            case R.id.select_avatar_activity_avatar_15:
                return R.drawable.ic_avatar_15;
            case R.id.select_avatar_activity_avatar_16:
                return R.drawable.ic_avatar_16;
            case R.id.select_avatar_activity_avatar_17:
                return R.drawable.ic_avatar_17;
            case R.id.select_avatar_activity_avatar_18:
                return R.drawable.ic_avatar_18;
            case R.id.select_avatar_activity_avatar_19:
                return R.drawable.ic_avatar_19;
            case R.id.select_avatar_activity_avatar_20:
                return R.drawable.ic_avatar_20;
            case R.id.select_avatar_activity_avatar_21:
                return R.drawable.ic_avatar_21;
            case R.id.select_avatar_activity_avatar_22:
                return R.drawable.ic_avatar_22;
            case R.id.select_avatar_activity_avatar_23:
                return R.drawable.ic_avatar_23;
            case R.id.select_avatar_activity_avatar_24:
                return R.drawable.ic_avatar_24;
            case R.id.select_avatar_activity_avatar_25:
                return R.drawable.ic_avatar_25;
            case R.id.select_avatar_activity_avatar_26:
                return R.drawable.ic_avatar_26;
            case R.id.select_avatar_activity_avatar_27:
                return R.drawable.ic_avatar_27;
            case R.id.select_avatar_activity_avatar_28:
                return R.drawable.ic_avatar_28;
            default:
                return R.drawable.ic_avatar_1;
        }
    }

    public int getSelectAvatarCheckerId() {
        switch (context.getSharedPreferences("Preferences", MODE_PRIVATE).getInt("User avatar", R.id.select_avatar_activity_avatar_1)) {
            case R.id.select_avatar_activity_avatar_2:
                return R.id.select_avatar_activity_avatar_2_check;
            case R.id.select_avatar_activity_avatar_3:
                return R.id.select_avatar_activity_avatar_3_check;
            case R.id.select_avatar_activity_avatar_4:
                return R.id.select_avatar_activity_avatar_4_check;
            case R.id.select_avatar_activity_avatar_5:
                return R.id.select_avatar_activity_avatar_5_check;
            case R.id.select_avatar_activity_avatar_6:
                return R.id.select_avatar_activity_avatar_6_check;
            case R.id.select_avatar_activity_avatar_7:
                return R.id.select_avatar_activity_avatar_7_check;
            case R.id.select_avatar_activity_avatar_8:
                return R.id.select_avatar_activity_avatar_8_check;
            case R.id.select_avatar_activity_avatar_9:
                return R.id.select_avatar_activity_avatar_9_check;
            case R.id.select_avatar_activity_avatar_10:
                return R.id.select_avatar_activity_avatar_10_check;
            case R.id.select_avatar_activity_avatar_11:
                return R.id.select_avatar_activity_avatar_11_check;
            case R.id.select_avatar_activity_avatar_12:
                return R.id.select_avatar_activity_avatar_12_check;
            case R.id.select_avatar_activity_avatar_13:
                return R.id.select_avatar_activity_avatar_13_check;
            case R.id.select_avatar_activity_avatar_14:
                return R.id.select_avatar_activity_avatar_14_check;
            case R.id.select_avatar_activity_avatar_15:
                return R.id.select_avatar_activity_avatar_15_check;
            case R.id.select_avatar_activity_avatar_16:
                return R.id.select_avatar_activity_avatar_16_check;
            case R.id.select_avatar_activity_avatar_17:
                return R.id.select_avatar_activity_avatar_17_check;
            case R.id.select_avatar_activity_avatar_18:
                return R.id.select_avatar_activity_avatar_18_check;
            case R.id.select_avatar_activity_avatar_19:
                return R.id.select_avatar_activity_avatar_19_check;
            case R.id.select_avatar_activity_avatar_20:
                return R.id.select_avatar_activity_avatar_20_check;
            case R.id.select_avatar_activity_avatar_21:
                return R.id.select_avatar_activity_avatar_21_check;
            case R.id.select_avatar_activity_avatar_22:
                return R.id.select_avatar_activity_avatar_22_check;
            case R.id.select_avatar_activity_avatar_23:
                return R.id.select_avatar_activity_avatar_23_check;
            case R.id.select_avatar_activity_avatar_24:
                return R.id.select_avatar_activity_avatar_24_check;
            case R.id.select_avatar_activity_avatar_25:
                return R.id.select_avatar_activity_avatar_25_check;
            case R.id.select_avatar_activity_avatar_26:
                return R.id.select_avatar_activity_avatar_26_check;
            case R.id.select_avatar_activity_avatar_27:
                return R.id.select_avatar_activity_avatar_27_check;
            case R.id.select_avatar_activity_avatar_28:
                return R.id.select_avatar_activity_avatar_28_check;
            default:
                return R.id.select_avatar_activity_avatar_1_check;
        }
    }

    public int getSelectAvatarCheckerId(int id) {
        switch (id) {
            case R.id.select_avatar_activity_avatar_2:
                return R.id.select_avatar_activity_avatar_2_check;
            case R.id.select_avatar_activity_avatar_3:
                return R.id.select_avatar_activity_avatar_3_check;
            case R.id.select_avatar_activity_avatar_4:
                return R.id.select_avatar_activity_avatar_4_check;
            case R.id.select_avatar_activity_avatar_5:
                return R.id.select_avatar_activity_avatar_5_check;
            case R.id.select_avatar_activity_avatar_6:
                return R.id.select_avatar_activity_avatar_6_check;
            case R.id.select_avatar_activity_avatar_7:
                return R.id.select_avatar_activity_avatar_7_check;
            case R.id.select_avatar_activity_avatar_8:
                return R.id.select_avatar_activity_avatar_8_check;
            case R.id.select_avatar_activity_avatar_9:
                return R.id.select_avatar_activity_avatar_9_check;
            case R.id.select_avatar_activity_avatar_10:
                return R.id.select_avatar_activity_avatar_10_check;
            case R.id.select_avatar_activity_avatar_11:
                return R.id.select_avatar_activity_avatar_11_check;
            case R.id.select_avatar_activity_avatar_12:
                return R.id.select_avatar_activity_avatar_12_check;
            case R.id.select_avatar_activity_avatar_13:
                return R.id.select_avatar_activity_avatar_13_check;
            case R.id.select_avatar_activity_avatar_14:
                return R.id.select_avatar_activity_avatar_14_check;
            case R.id.select_avatar_activity_avatar_15:
                return R.id.select_avatar_activity_avatar_15_check;
            case R.id.select_avatar_activity_avatar_16:
                return R.id.select_avatar_activity_avatar_16_check;
            case R.id.select_avatar_activity_avatar_17:
                return R.id.select_avatar_activity_avatar_17_check;
            case R.id.select_avatar_activity_avatar_18:
                return R.id.select_avatar_activity_avatar_18_check;
            case R.id.select_avatar_activity_avatar_19:
                return R.id.select_avatar_activity_avatar_19_check;
            case R.id.select_avatar_activity_avatar_20:
                return R.id.select_avatar_activity_avatar_20_check;
            case R.id.select_avatar_activity_avatar_21:
                return R.id.select_avatar_activity_avatar_21_check;
            case R.id.select_avatar_activity_avatar_22:
                return R.id.select_avatar_activity_avatar_22_check;
            case R.id.select_avatar_activity_avatar_23:
                return R.id.select_avatar_activity_avatar_23_check;
            case R.id.select_avatar_activity_avatar_24:
                return R.id.select_avatar_activity_avatar_24_check;
            case R.id.select_avatar_activity_avatar_25:
                return R.id.select_avatar_activity_avatar_25_check;
            case R.id.select_avatar_activity_avatar_26:
                return R.id.select_avatar_activity_avatar_26_check;
            case R.id.select_avatar_activity_avatar_27:
                return R.id.select_avatar_activity_avatar_27_check;
            case R.id.select_avatar_activity_avatar_28:
                return R.id.select_avatar_activity_avatar_28_check;
            default:
                return R.id.select_avatar_activity_avatar_1_check;
        }
    }
}
