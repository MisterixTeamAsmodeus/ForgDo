package com.itschool.buzuverov.forgdo.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;

import com.itschool.buzuverov.forgdo.Fragment.GroupFragment;
import com.itschool.buzuverov.forgdo.Fragment.HomeFragment;
import com.itschool.buzuverov.forgdo.Fragment.SearchFragment;
import com.itschool.buzuverov.forgdo.Fragment.SettingFragment;
import com.itschool.buzuverov.forgdo.Model.Fragment.MyFragment;
import com.itschool.buzuverov.forgdo.Model.Theme.ThemeHelper;
import com.itschool.buzuverov.forgdo.R;

public class MainActivity extends AppCompatActivity {

    private MyFragment fragment;
    private static int selectIconIndex = 0;
    private ImageButton[] iconMenuButtons;
    private TypedValue value;
    private FragmentManager manager = getSupportFragmentManager();
    private FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(ThemeHelper.getTheme(this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.coordinatorLayout).startAnimation(AnimationUtils.loadAnimation(this, R.anim.show_bottom_bar));
        setStartIcon();
        initFab();
        setStartFragment();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fragment.onDataUpdate();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        fragment.onDataUpdate();
    }

    private void initMenuIcon() {
        iconMenuButtons = new ImageButton[4];
        iconMenuButtons[0] = findViewById(R.id.main_menu_home);
        iconMenuButtons[1] = findViewById(R.id.main_menu_group);
        iconMenuButtons[2] = findViewById(R.id.main_menu_search);
        iconMenuButtons[3] = findViewById(R.id.main_menu_profile);
    }

    private void setStartIcon() {
        value = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorAccent, value, true);
        initMenuIcon();
        iconMenuButtons[selectIconIndex].setEnabled(false);
        iconMenuButtons[selectIconIndex].setColorFilter(value.data);
    }

    private void initFab() {
        findViewById(R.id.main_floating_action_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String group = "";
                if (fragment.getClass() == GroupFragment.class){
                    group = ((GroupFragment) fragment).getActiveGroupName();
                }
                Intent intent = new Intent(MainActivity.this, CreateTaskActivity.class);
                intent.putExtra("ActiveGroup", group);
                startActivity(intent);
            }
        });
    }

    private void setStartFragment() {
        switch (selectIconIndex){
            case 0:
                fragment = new HomeFragment();
                break;
            case 1:
                fragment = new GroupFragment();
                break;
            case 2:
                fragment = new SearchFragment();
                break;
            case 3:
                fragment = new SettingFragment();
                break;
        }
        transaction = manager.beginTransaction();
        transaction.replace(R.id.main_frame_layout, fragment);
        transaction.commit();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    public void onIconMenuClick(View view) {
        if (view.getId() != iconMenuButtons[selectIconIndex].getId()) {
            iconMenuButtons[selectIconIndex].setEnabled(true);
            iconMenuButtons[selectIconIndex].setColorFilter(null);
            transaction = manager.beginTransaction();
            fragment.setDefault();
            switch (view.getId()) {  // переключение фрагментов и изменение цвета иконки
                case R.id.main_menu_home:
                    selectIconIndex = 0;
                    fragment = new HomeFragment();
                    break;
                case R.id.main_menu_group:
                    selectIconIndex = 1;
                    fragment = new GroupFragment();
                    break;
                case R.id.main_menu_search:
                    selectIconIndex = 2;
                    fragment = new SearchFragment();
                    break;

                case R.id.main_menu_profile:
                    selectIconIndex = 3;
                    fragment = new SettingFragment();
                    break;
            }
            iconMenuButtons[selectIconIndex].setEnabled(false);
            iconMenuButtons[selectIconIndex].setColorFilter(value.data);
            transaction.replace(R.id.main_frame_layout, fragment);
            transaction.commit();
        }
    }
}