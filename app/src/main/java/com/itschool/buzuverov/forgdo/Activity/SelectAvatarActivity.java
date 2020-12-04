package com.itschool.buzuverov.forgdo.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;

import com.itschool.buzuverov.forgdo.Model.Avatar.Avatar;
import com.itschool.buzuverov.forgdo.Model.Avatar.AvatarHelper;
import com.itschool.buzuverov.forgdo.Model.Theme.ThemeHelper;
import com.itschool.buzuverov.forgdo.R;

public class SelectAvatarActivity extends AppCompatActivity {

    AvatarHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(ThemeHelper.getTheme(this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_avatar);

        ImageButton back = findViewById(R.id.select_avatar_activity_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        back.startAnimation(AnimationUtils.loadAnimation(this, R.anim.show_back_button));
        showAnim();
        helper = new AvatarHelper(this);
        Avatar selectAvatar = new Avatar(findViewById(helper.getSelectAvatarId()), findViewById(helper.getSelectAvatarCheckerId()));
        helper.selectAvatar(selectAvatar);
    }

    private void showAnim() {
        findViewById(R.id.select_avatar_activity_title).startAnimation(AnimationUtils.loadAnimation(this, R.anim.show_title_text));
        findViewById(R.id.select_avatar_activity_man_tip).startAnimation(AnimationUtils.loadAnimation(this, R.anim.show_avatra_layout));
        findViewById(R.id.select_avatar_activity_girl_tip).startAnimation(AnimationUtils.loadAnimation(this, R.anim.show_avatra_layout));
        findViewById(R.id.select_avatar_activity_man_layout).startAnimation(AnimationUtils.loadAnimation(this, R.anim.show_avatra_layout));
        findViewById(R.id.select_avatar_activity_girl_layout).startAnimation(AnimationUtils.loadAnimation(this, R.anim.show_avatra_layout));
    }

    public void onAvatarSelect(View view) {
        helper.selectAvatar(new Avatar(view, findViewById(helper.getSelectAvatarCheckerId(view.getId()))));
    }
}