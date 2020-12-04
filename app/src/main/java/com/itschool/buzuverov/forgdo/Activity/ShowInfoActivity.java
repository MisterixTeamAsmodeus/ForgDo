package com.itschool.buzuverov.forgdo.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.itschool.buzuverov.forgdo.Dialog.DialogShowAllGroup;
import com.itschool.buzuverov.forgdo.Model.DataBaseHelper;
import com.itschool.buzuverov.forgdo.Model.Tasks.Task;
import com.itschool.buzuverov.forgdo.Model.Theme.ThemeHelper;
import com.itschool.buzuverov.forgdo.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ShowInfoActivity extends AppCompatActivity {

    private static int id;
    private TextView title, dateCreate, deadline, priority, info, group;
    private ImageView check;
    private ImageView notification;
    private DateFormat dateFormat = new SimpleDateFormat("d.MM.yyyy H:m");
    private Task task;
    private DataBaseHelper dataBaseHelper;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(ThemeHelper.getTheme(this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_info);
        userId = FirebaseAuth.getInstance().getUid();
        if (getSharedPreferences("Preferences", MODE_PRIVATE).getBoolean("AutoSynchronizations", false))
            userId = FirebaseAuth.getInstance().getUid();
        else {
            userId = null;
        }
        dataBaseHelper = new DataBaseHelper(this);
        title = findViewById(R.id.activity_show_info_title);
        group = findViewById(R.id.activity_show_info_group);
        notification = findViewById(R.id.activity_show_info_notification);
        dateCreate = findViewById(R.id.activity_show_info_create_time);
        deadline = findViewById(R.id.activity_show_info_deadline);
        priority = findViewById(R.id.activity_show_info_priority);
        info = findViewById(R.id.activity_show_info_info);
        check = findViewById(R.id.activity_show_info_check);
        ImageButton back = findViewById(R.id.activity_show_info_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        back.startAnimation(AnimationUtils.loadAnimation(this, R.anim.show_back_button));

        ImageButton menu = findViewById(R.id.activity_show_info_menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu menu = new PopupMenu(view.getContext(), view);
                menu.inflate(R.menu.task_popup_menu);
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.task_menu_delete:
                                dataBaseHelper.deleteTask(task, userId);
                                finish();
                                break;
                            case R.id.task_menu_edit:
                                Intent intent = new Intent(ShowInfoActivity.this, EditTaskActivity.class);
                                intent.putExtra("id", id);
                                startActivity(intent);
                                break;
                            case R.id.task_menu_move:
                                DialogShowAllGroup createTask = new DialogShowAllGroup(new DialogShowAllGroup.DialogShowAllGroupListener() {
                                    @Override
                                    public void select(String name) {
                                        group.setText(name.equals("null") ? getString(R.string.none) : name);
                                        task.setGroupName(name);
                                        dataBaseHelper.updateTask(task,userId);
                                    }
                                }, dataBaseHelper.getAllGroupName(task.getGroupName()), ShowInfoActivity.this);
                                createTask.show(getSupportFragmentManager(), "tag");
                                break;
                        }
                        return true;
                    }
                });
                menu.show();
            }
        });
        menu.startAnimation(AnimationUtils.loadAnimation(this, R.anim.show_back_button));
        id = (int) getIntent().getExtras().get("id");
        setStartInfo();
        showAnim();
    }

    private void showAnim() {
        title.startAnimation(AnimationUtils.loadAnimation(this, R.anim.show_title_text));
        check.startAnimation(AnimationUtils.loadAnimation(this, R.anim.show_params_layout));
        dateCreate.startAnimation(AnimationUtils.loadAnimation(this, R.anim.show_params_layout));
        priority.startAnimation(AnimationUtils.loadAnimation(this, R.anim.show_params_layout));
        group.startAnimation(AnimationUtils.loadAnimation(this, R.anim.show_params_layout));
        info.startAnimation(AnimationUtils.loadAnimation(this, R.anim.show_create_button));
        findViewById(R.id.activity_show_info_deadline_layout).startAnimation(AnimationUtils.loadAnimation(this, R.anim.show_params_layout));
        findViewById(R.id.activity_show_info_date_create_tip).startAnimation(AnimationUtils.loadAnimation(this, R.anim.show_tip_text));
        findViewById(R.id.activity_show_info_priority_tip).startAnimation(AnimationUtils.loadAnimation(this, R.anim.show_tip_text));
        findViewById(R.id.activity_show_info_group_tip).startAnimation(AnimationUtils.loadAnimation(this, R.anim.show_tip_text));
        findViewById(R.id.activity_show_description_group).startAnimation(AnimationUtils.loadAnimation(this, R.anim.show_tip_text));
    }

    @Override
    protected void onResume() {
        super.onResume();
        setStartInfo();
    }

    private void setStartInfo() {
        task = dataBaseHelper.getTask(id);
        title.setText(task.getName());
        info.setText(task.getInfo().length() == 0 ? getString(R.string.none) : task.getInfo());
        if (task.getGroupName().equals("null")) {
            group.setText(R.string.none);
        } else {
            group.setText(task.getGroupName());
        }
        dateCreate.setText(dateFormat.format(new Date(task.getDateCreate())));
        deadline.setText(dateFormat.format(new Date(task.getDeadline())));
        notification.setVisibility(task.isOnNotification() ? View.VISIBLE : View.GONE);
        check.setBackgroundResource(task.isDone() ? R.drawable.select_background_check : R.drawable.select_background);
        switch (task.getPriority()) {
            case Task.HIGH:
                priority.setBackgroundResource(R.drawable.text_high);
                priority.setText(R.string.high);
                break;
            case Task.MEDIUM:
                priority.setBackgroundResource(R.drawable.text_medium);
                priority.setText(R.string.medium);
                break;
            case Task.LOW:
                priority.setBackgroundResource(R.drawable.text_low);
                priority.setText(R.string.low);
                break;
            case Task.NONE:
                priority.setBackgroundResource(R.drawable.text_none);
                priority.setText(R.string.none);
                break;
        }

    }
}