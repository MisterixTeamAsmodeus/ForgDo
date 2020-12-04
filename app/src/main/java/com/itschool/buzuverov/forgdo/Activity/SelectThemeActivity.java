package com.itschool.buzuverov.forgdo.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;

import com.itschool.buzuverov.forgdo.Model.Theme.Theme;
import com.itschool.buzuverov.forgdo.Model.Theme.ThemeHelper;
import com.itschool.buzuverov.forgdo.R;

public class SelectThemeActivity extends AppCompatActivity {

    private ThemeHelper themeHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(ThemeHelper.getTheme(this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_theme);
        themeHelper = new ThemeHelper(this);
        themeHelper.selectTheme(new Theme(
                findViewById(themeHelper.getThemeImageId(getSharedPreferences("Preferences", MODE_PRIVATE).getInt("User theme", R.style.DefaultTheme))),
                findViewById(themeHelper.getThemeCheckerImageId(getSharedPreferences("Preferences", MODE_PRIVATE).getInt("User theme", R.style.DefaultTheme))),
                getSharedPreferences("Preferences", MODE_PRIVATE).getInt("User theme", R.style.DefaultTheme)));
        ImageButton back = findViewById(R.id.select_theme_activity_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectThemeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        back.startAnimation(AnimationUtils.loadAnimation(this, R.anim.show_back_button));
        showAnim();
    }

    private void showAnim() {
        findViewById(R.id.select_theme_activity_title).startAnimation(AnimationUtils.loadAnimation(this, R.anim.show_title_text));
        findViewById(R.id.select_theme_activity_theme_layout).startAnimation(AnimationUtils.loadAnimation(this, R.anim.show_theme_layout));
    }

    public void onSelectTheme(View view) {
        themeHelper.selectTheme(new Theme(view,findViewById(themeHelper.getNewThemeChecker(view.getId())),themeHelper.getNewThemeRes(view.getId())));
        Intent intent = new Intent(this, SelectThemeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SelectThemeActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}