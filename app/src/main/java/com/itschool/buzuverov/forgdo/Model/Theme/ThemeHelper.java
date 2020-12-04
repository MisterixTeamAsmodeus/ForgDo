package com.itschool.buzuverov.forgdo.Model.Theme;

import android.content.Context;
import android.content.SharedPreferences;

import com.itschool.buzuverov.forgdo.R;

import static android.content.Context.MODE_PRIVATE;

public class ThemeHelper {
    private Theme selectTheme;
    private Context context;

    public ThemeHelper(Context context) {
        this.context = context;
    }

    public void selectTheme(Theme theme) {
        if (selectTheme != null) {
            selectTheme.setSelect(false);
        }
        theme.setSelect(true);
        selectTheme = theme;
        SharedPreferences.Editor preferences = context.getSharedPreferences("Preferences", MODE_PRIVATE).edit();
        preferences.putInt("User theme", selectTheme.getThemeRecourse());
        preferences.apply();
    }

    public static int getTheme(Context context) {
        return context.getSharedPreferences("Preferences", MODE_PRIVATE).getInt("User theme", R.style.DefaultTheme);
    }

    public int getNewThemeChecker(int idClickedView) {
        switch (idClickedView) {
            default:
                return R.id.select_theme_activity_theme_defualt_check;
            case R.id.select_theme_activity_theme_brown:
                return R.id.select_theme_activity_theme_brown_check;
            case R.id.select_theme_activity_theme_purple:
                return R.id.select_theme_activity_theme_purple_check;
            case R.id.select_theme_activity_theme_metal:
                return R.id.select_theme_activity_theme_metal_check;
            case R.id.select_theme_activity_theme_orange:
                return R.id.select_theme_activity_theme_orange_check;
            case R.id.select_theme_activity_theme_teal:
                return R.id.select_theme_activity_theme_teal_check;
        }
    }

    public int getThemeImageId(int themeRes) {
        switch (themeRes) {
            default:
                return R.id.select_theme_activity_theme_defualt;
            case R.style.BrownTheme:
                return R.id.select_theme_activity_theme_brown;
            case R.style.PurpleTheme:
                return R.id.select_theme_activity_theme_purple;
            case R.style.DarkTheme:
                return R.id.select_theme_activity_theme_metal;
            case R.style.AmberTheme:
                return R.id.select_theme_activity_theme_orange;
            case R.style.TealTheme:
                return R.id.select_theme_activity_theme_teal;
        }
    }

    public int getThemeCheckerImageId(int themeRes) {
        switch (themeRes) {
            default:
                return R.id.select_theme_activity_theme_defualt_check;
            case R.style.BrownTheme:
                return R.id.select_theme_activity_theme_brown_check;
            case R.style.PurpleTheme:
                return R.id.select_theme_activity_theme_purple_check;
            case R.style.DarkTheme:
                return R.id.select_theme_activity_theme_metal_check;
            case R.style.AmberTheme:
                return R.id.select_theme_activity_theme_orange_check;
            case R.style.TealTheme:
                return R.id.select_theme_activity_theme_teal_check;
        }
    }

    public int getNewThemeRes(int idClickedView) {
        switch (idClickedView) {
            default:
                return R.style.DefaultTheme;
            case R.id.select_theme_activity_theme_brown:
                return R.style.BrownTheme;
            case R.id.select_theme_activity_theme_purple:
                return R.style.PurpleTheme;
            case R.id.select_theme_activity_theme_metal:
                return R.style.DarkTheme;
            case R.id.select_theme_activity_theme_orange:
                return R.style.AmberTheme;
            case R.id.select_theme_activity_theme_teal:
                return R.style.TealTheme;
        }
    }
}