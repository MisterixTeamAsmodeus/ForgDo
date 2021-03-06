package com.itschool.buzuverov.forgdo.Activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.itschool.buzuverov.forgdo.Dialog.DialogCreateGroup;
import com.itschool.buzuverov.forgdo.Model.DataBaseHelper;
import com.itschool.buzuverov.forgdo.Model.Notification.AlarmHelper;
import com.itschool.buzuverov.forgdo.Model.Tasks.Task;
import com.itschool.buzuverov.forgdo.Model.Theme.ThemeHelper;
import com.itschool.buzuverov.forgdo.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CreateTaskActivity extends AppCompatActivity {

    private EditText title, info;
    private long deadline = new Date().getTime();
    private Spinner group;
    private TextView priority_none, priority_low, priority_medium, priority_high, time_text;
    private int priority = Task.NONE;
    private boolean isNotification = false;
    private ArrayList<String> groups;
    private Calendar dateAndTime = Calendar.getInstance();
    private DateFormat dateFormat = new SimpleDateFormat("d.MM.yyyy H:m");
    private ArrayAdapter<String> adapter;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(ThemeHelper.getTheme(this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);

        userId = FirebaseAuth.getInstance().getUid();
        if (getSharedPreferences("Preferences", MODE_PRIVATE).getBoolean("AutoSynchronizations", false))
            userId = FirebaseAuth.getInstance().getUid();
        else {
            userId = null;
        }
        title = findViewById(R.id.create_task_activity_title);
        info = findViewById(R.id.create_task_activity_description);
        group = findViewById(R.id.create_task_activity_group);
        time_text = findViewById(R.id.create_task_activity_date_text);
        priority_none = findViewById(R.id.create_task_activity_priority_none);
        priority_low = findViewById(R.id.create_task_activity_priority_low);
        priority_medium = findViewById(R.id.create_task_activity_priority_medium);
        priority_high = findViewById(R.id.create_task_activity_priority_high);

        ((SwitchCompat) (findViewById(R.id.create_task_activity_notification))).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isNotification = b;
            }
        });

        findViewById(R.id.create_task_activity_date_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDeadline();
            }
        });

        ImageButton back = findViewById(R.id.create_task_activity_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        back.startAnimation(AnimationUtils.loadAnimation(this, R.anim.show_back_button));

        time_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDeadline();
            }
        });

        time_text.setText(dateFormat.format(deadline));

        groups = getAllGroups();

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, groups);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        group.setAdapter(adapter);
        String groupName = getIntent().getStringExtra("ActiveGroup");
        if (groupName != null && !groupName.equals("")) {
            group.setSelection(groups.indexOf(groupName));
        }
        group.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 1) {
                    DialogCreateGroup createTask = new DialogCreateGroup(new DialogCreateGroup.DialogCreateGroupListener() {
                        @Override
                        public void create(String name) {
                            groups.add(name);
                            adapter.notifyDataSetChanged();
                            group.setSelection(groups.size() - 1);
                        }
                    }, CreateTaskActivity.this);
                    createTask.show(getSupportFragmentManager(), "tag");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        priority_none.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (priority != Task.NONE) {
                    switch (priority) {
                        case Task.LOW:
                            priority_low.setBackgroundResource(R.drawable.text_low);
                            priority_low.setTypeface(null, Typeface.NORMAL);
                            break;
                        case Task.MEDIUM:
                            priority_medium.setBackgroundResource(R.drawable.text_medium);
                            priority_medium.setTypeface(null, Typeface.NORMAL);
                            break;
                        case Task.HIGH:
                            priority_high.setBackgroundResource(R.drawable.text_high);
                            priority_high.setTypeface(null, Typeface.NORMAL);
                            break;
                    }
                    priority = Task.NONE;
                    priority_none.setBackgroundResource(R.drawable.text_none_select);
                    priority_none.setTypeface(null, Typeface.BOLD);
                }
            }
        });

        priority_low.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (priority != Task.LOW) {
                    switch (priority) {
                        case Task.NONE:
                            priority_none.setBackgroundResource(R.drawable.text_none);
                            priority_none.setTypeface(null, Typeface.NORMAL);
                            break;
                        case Task.MEDIUM:
                            priority_medium.setBackgroundResource(R.drawable.text_medium);
                            priority_medium.setTypeface(null, Typeface.NORMAL);
                            break;
                        case Task.HIGH:
                            priority_high.setBackgroundResource(R.drawable.text_high);
                            priority_high.setTypeface(null, Typeface.NORMAL);
                            break;
                    }
                    priority = Task.LOW;
                    priority_low.setBackgroundResource(R.drawable.text_low_select);
                    priority_low.setTypeface(null, Typeface.BOLD);
                }
            }
        });

        priority_medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (priority != Task.MEDIUM) {
                    switch (priority) {
                        case Task.NONE:
                            priority_none.setBackgroundResource(R.drawable.text_none);
                            priority_none.setTypeface(null, Typeface.NORMAL);
                            break;
                        case Task.LOW:
                            priority_low.setBackgroundResource(R.drawable.text_low);
                            priority_low.setTypeface(null, Typeface.NORMAL);
                            break;
                        case Task.HIGH:
                            priority_high.setBackgroundResource(R.drawable.text_high);
                            priority_high.setTypeface(null, Typeface.NORMAL);
                            break;
                    }
                    priority = Task.MEDIUM;
                    priority_medium.setBackgroundResource(R.drawable.text_medium_select);
                    priority_medium.setTypeface(null, Typeface.BOLD);
                }
            }
        });

        priority_high.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (priority != Task.HIGH)
                    switch (priority) {
                        case Task.NONE:
                            priority_none.setBackgroundResource(R.drawable.text_none);
                            priority_none.setTypeface(null, Typeface.NORMAL);
                            break;
                        case Task.LOW:
                            priority_low.setBackgroundResource(R.drawable.text_low);
                            priority_low.setTypeface(null, Typeface.NORMAL);
                            break;
                        case Task.MEDIUM:
                            priority_medium.setBackgroundResource(R.drawable.text_medium);
                            priority_medium.setTypeface(null, Typeface.NORMAL);
                            break;
                    }
                priority = Task.HIGH;
                priority_high.setBackgroundResource(R.drawable.text_high_select);
                priority_high.setTypeface(null, Typeface.BOLD);
            }
        });
        startAnim();
    }

    private void startAnim() {
        findViewById(R.id.create_task_activity_deadline_tip).startAnimation(AnimationUtils.loadAnimation(this, R.anim.show_tip_text));
        findViewById(R.id.create_task_activity_description_tip).startAnimation(AnimationUtils.loadAnimation(this, R.anim.show_tip_text));
        findViewById(R.id.create_task_activity_group_tip).startAnimation(AnimationUtils.loadAnimation(this, R.anim.show_tip_text));
        findViewById(R.id.create_task_activity_notification_tip).startAnimation(AnimationUtils.loadAnimation(this, R.anim.show_tip_text));
        findViewById(R.id.create_task_activity_priority_tip).startAnimation(AnimationUtils.loadAnimation(this, R.anim.show_tip_text));
        findViewById(R.id.create_task_activity_title_tip).startAnimation(AnimationUtils.loadAnimation(this, R.anim.show_tip_text));
        findViewById(R.id.create_task_activity_info_title).startAnimation(AnimationUtils.loadAnimation(this, R.anim.show_title_text));
        findViewById(R.id.create_task_activity_create).startAnimation(AnimationUtils.loadAnimation(this, R.anim.show_create_button));
        title.startAnimation(AnimationUtils.loadAnimation(this, R.anim.show_params_layout));
        info.startAnimation(AnimationUtils.loadAnimation(this, R.anim.show_params_layout));
        findViewById(R.id.create_task_activity_date).startAnimation(AnimationUtils.loadAnimation(this, R.anim.show_params_layout));
        findViewById(R.id.create_task_activity_priority).startAnimation(AnimationUtils.loadAnimation(this, R.anim.show_params_layout));
        group.startAnimation(AnimationUtils.loadAnimation(this, R.anim.show_params_layout));
        findViewById(R.id.create_task_activity_notification_layout).startAnimation(AnimationUtils.loadAnimation(this, R.anim.show_params_layout));
    }

    private ArrayList<String> getAllGroups() {
        ArrayList<String> groups = new ArrayList<>();
        groups.add(getString(R.string.none));
        groups.add(getString(R.string.create_new_group));
        SQLiteDatabase database = new DataBaseHelper(this).getReadableDatabase();
        Cursor cursor = database.rawQuery("select DISTINCT " + DataBaseHelper.KEY_GROUP + " FROM " + DataBaseHelper.TABLE_TASK + " WHERE " + DataBaseHelper.KEY_GROUP + " != ?", new String[]{"null"});
        if (cursor.moveToFirst()) {
            int groupIndex = cursor.getColumnIndex(DataBaseHelper.KEY_GROUP);
            do {
                groups.add(cursor.getString(groupIndex));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return groups;
    }

    public void createTask(View view) {
        if (title.getText().toString().length() > 0) {
            long dateCreate = new Date().getTime();
            Task task = null;
            DataBaseHelper helper = new DataBaseHelper(this);
            Cursor cursor = helper.getWritableDatabase().query(DataBaseHelper.TABLE_TASK, null, null, null, null, null, null);
            if (cursor.moveToLast()) {
                task = new Task(
                        title.getText().toString(),
                        info.getText().toString(),
                        group.getSelectedItemId() != 0 ? groups.get((int) group.getSelectedItemId()) : "null",
                        dateCreate,
                        deadline,
                        isNotification,
                        false,
                        priority,
                        cursor.getInt(cursor.getColumnIndex(DataBaseHelper.KEY_ID)) + 1);
            }
            cursor.close();
            if (task != null) {
                helper.createTask(task, userId);
                if (task.isOnNotification()) {
                    AlarmHelper.createReminder(deadline, this, task.getName(), task.getInfo());
                } else {
                    AlarmHelper.cancelReminder(this, task.getName(), task.getInfo());
                }
                finish();
            } else {
                Toast.makeText(this, getString(R.string.enter_name), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, getString(R.string.enter_name), Toast.LENGTH_LONG).show();
        }
    }

    private void setDeadline() {
        new DatePickerDialog(CreateTaskActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                dateAndTime.set(Calendar.YEAR, i);
                dateAndTime.set(Calendar.MONTH, i1);
                dateAndTime.set(Calendar.DAY_OF_MONTH, i2);
                new TimePickerDialog(CreateTaskActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        dateAndTime.set(Calendar.HOUR_OF_DAY, i);
                        dateAndTime.set(Calendar.MINUTE, i1);
                        dateAndTime.set(Calendar.SECOND, 0);
                        deadline = dateAndTime.getTimeInMillis();
                        time_text.setText(dateFormat.format(deadline));
                    }
                }, dateAndTime.get(Calendar.HOUR_OF_DAY), dateAndTime.get(Calendar.MINUTE), true).show();

            }
        }, dateAndTime.get(Calendar.YEAR), dateAndTime.get(Calendar.MONTH), dateAndTime.get(Calendar.DAY_OF_MONTH)).show();
    }
}