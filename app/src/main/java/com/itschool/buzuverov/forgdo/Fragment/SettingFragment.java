package com.itschool.buzuverov.forgdo.Fragment;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itschool.buzuverov.forgdo.Activity.LoginActivity;
import com.itschool.buzuverov.forgdo.Activity.MainActivity;
import com.itschool.buzuverov.forgdo.Activity.SelectAvatarActivity;
import com.itschool.buzuverov.forgdo.Activity.SelectThemeActivity;
import com.itschool.buzuverov.forgdo.Model.Notification.AlarmHelper;
import com.itschool.buzuverov.forgdo.Model.Avatar.AvatarHelper;
import com.itschool.buzuverov.forgdo.Model.DataBaseHelper;
import com.itschool.buzuverov.forgdo.Model.Fragment.MyFragment;
import com.itschool.buzuverov.forgdo.Model.Tasks.Task;
import com.itschool.buzuverov.forgdo.R;

import static android.content.Context.MODE_PRIVATE;

public class SettingFragment extends MyFragment {

    private View view;
    private Context context;
    private FirebaseAuth auth;
    private LinearLayout synchronize;
    private LinearLayout recover;
    private CheckBox autoSynchronization;
    private TextView signText, userName;
    private ImageView userAvatar;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_setting, container, false);
        init();
        return view;
    }

    private void init() {
        progressDialog = new ProgressDialog(context);
        auth = FirebaseAuth.getInstance();
        View statusLayout = view.findViewById(R.id.setting_fragment_status_layout);
        statusLayout.startAnimation(AnimationUtils.loadAnimation(context, R.anim.show_status_layout));
        userName = statusLayout.findViewById(R.id.status_layout_name);
        userAvatar = statusLayout.findViewById(R.id.status_layout_avatar);
        userAvatar.setImageResource(AvatarHelper.getSelectAvatar(context.getSharedPreferences("Preferences", MODE_PRIVATE).getInt("User avatar", R.id.select_avatar_activity_avatar_1)));
        String name = getString(R.string.hi) + context.getSharedPreferences("Preferences", MODE_PRIVATE).getString("User name", "User") + ".";
        userName.setText(name);
        EditText userNameEdit = view.findViewById(R.id.setting_fragment_name);
        userNameEdit.setText(context.getSharedPreferences("Preferences", MODE_PRIVATE).getString("User name", "User"));
        userNameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() != 0) {
                    String name = getString(R.string.hi) + charSequence + ".";
                    userName.setText(name);
                    SharedPreferences.Editor preferences = context.getSharedPreferences("Preferences", MODE_PRIVATE).edit();
                    preferences.putString("User name", charSequence.toString());
                    preferences.apply();
                } else {
                    userName.setText(R.string.hi_default);
                    SharedPreferences.Editor preferences = context.getSharedPreferences("Preferences", MODE_PRIVATE).edit();
                    preferences.putString("User name", getString(R.string.user));
                    preferences.apply();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        synchronize = view.findViewById(R.id.setting_fragment_synchronization);
        recover = view.findViewById(R.id.setting_fragment_recover);
        LinearLayout sign = view.findViewById(R.id.setting_fragment_sign_in);
        signText = view.findViewById(R.id.setting_fragment_sign_in_text);
        view.findViewById(R.id.setting_fragment_theme).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, SelectThemeActivity.class));
                ((MainActivity)context).finish();
            }
        });
        view.findViewById(R.id.setting_fragment_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, SelectAvatarActivity.class));
            }
        });
        autoSynchronization = view.findViewById(R.id.setting_fragment_auto_synchronization);

        recover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (auth.getUid() != null){
                    int currentOrientation = getResources().getConfiguration().orientation;
                    if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                        ((AppCompatActivity) context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE); //locks landscape
                    } else {
                        ((AppCompatActivity) context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT); //locks port
                    }
                    progressDialog.setTitle(getString(R.string.task_recover));
                    progressDialog.setMessage(getString(R.string.task_message));
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    FirebaseDatabase.getInstance().getReference().child("User").child(auth.getUid()).child("Tasks").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists() && snapshot.hasChildren()) {
                                SQLiteDatabase database = new DataBaseHelper(context).getWritableDatabase();
                                database.delete(DataBaseHelper.TABLE_TASK, null, null);
                                for (DataSnapshot data : snapshot.getChildren()) {
                                    Task task = data.getValue(Task.class);
                                    ContentValues contentValues = new ContentValues();
                                    contentValues.put(DataBaseHelper.KEY_ID, task.getId());
                                    contentValues.put(DataBaseHelper.KEY_NAME, task.getName());
                                    contentValues.put(DataBaseHelper.KEY_INFO, task.getInfo());
                                    contentValues.put(DataBaseHelper.KEY_GROUP, task.getGroupName());
                                    contentValues.put(DataBaseHelper.KEY_TIME_CREATE, task.getDateCreate());
                                    contentValues.put(DataBaseHelper.KEY_TIME_COMPLETE, task.getDeadline());
                                    contentValues.put(DataBaseHelper.KEY_NOTIFICATION, task.isOnNotification());
                                    contentValues.put(DataBaseHelper.KEY_DONE, task.isDone());
                                    contentValues.put(DataBaseHelper.KEY_PRIORITY, task.getPriority());
                                    database.insert(DataBaseHelper.TABLE_TASK, null, contentValues);
                                    if (task.isOnNotification()) {
                                        AlarmHelper.createReminder(task.getDeadline(), context, task.getName(), task.getInfo());
                                    } else {
                                        AlarmHelper.cancelReminder(context, task.getName(), task.getInfo());
                                    }
                                }
                                progressDialog.dismiss();
                                ((AppCompatActivity) context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                                Toast.makeText(context, R.string.completed, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

            }
        });

        synchronize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                synchronizationsData();
            }
        });

        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (auth.getUid() == null) {
                    Intent intent = new Intent(context, LoginActivity.class);
                    startActivity(intent);
                } else {
                    auth.signOut();
                    SharedPreferences.Editor preferences = context.getSharedPreferences("Preferences", MODE_PRIVATE).edit();
                    preferences.putBoolean("AutoSynchronizations", false);
                    preferences.apply();
                    autoSynchronization.setEnabled(false);
                    autoSynchronization.setChecked(false);
                    recover.setEnabled(false);
                    synchronize.setEnabled(false);
                    signText.setText(R.string.sign_in);
                }
            }
        });

        if (auth.getUid() == null) {
            autoSynchronization.setEnabled(false);
            recover.setEnabled(false);
            synchronize.setEnabled(false);
            signText.setText(R.string.sign_in);
        } else {
            autoSynchronization.setChecked(context.getSharedPreferences("Preferences", MODE_PRIVATE).getBoolean("AutoSynchronizations", false));
            autoSynchronization.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        synchronizationsData();
                    }
                    SharedPreferences.Editor preferences = context.getSharedPreferences("Preferences", MODE_PRIVATE).edit();
                    preferences.putBoolean("AutoSynchronizations", b);
                    preferences.apply();
                }
            });
            autoSynchronization.setEnabled(true);
            recover.setEnabled(true);
            synchronize.setEnabled(true);
            signText.setText(R.string.sign_out);
        }

        startAnim();
    }

    private void startAnim() {
        view.findViewById(R.id.setting_fragment_account_tip).startAnimation(AnimationUtils.loadAnimation(context, R.anim.show_tip_text));
        view.findViewById(R.id.setting_fragment_decoration_tip).startAnimation(AnimationUtils.loadAnimation(context, R.anim.show_tip_text));
        view.findViewById(R.id.setting_fragment_account_button).startAnimation(AnimationUtils.loadAnimation(context, R.anim.show_params_layout));
        view.findViewById(R.id.setting_fragment_decoration_button).startAnimation(AnimationUtils.loadAnimation(context, R.anim.show_params_layout));
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDataUpdate() {
        userAvatar.setImageResource(AvatarHelper.getSelectAvatar(context.getSharedPreferences("Preferences", MODE_PRIVATE).getInt("User avatar", R.id.select_avatar_activity_avatar_1)));
        auth = FirebaseAuth.getInstance();
        if (auth.getUid() == null) {
            autoSynchronization.setEnabled(false);
            recover.setEnabled(false);
            synchronize.setEnabled(false);
            signText.setText(R.string.sign_in);
        } else {
            autoSynchronization.setEnabled(true);
            recover.setEnabled(true);
            synchronize.setEnabled(true);
            signText.setText(R.string.sign_out);
        }
    }

    @Override
    public void setDefault() {

    }

    private void synchronizationsData() {
        if (auth.getUid() != null){
            int currentOrientation = getResources().getConfiguration().orientation;
            if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                ((AppCompatActivity) context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE); //locks landscape
            } else {
                ((AppCompatActivity) context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT); //locks port
            }
            progressDialog.setTitle(getString(R.string.dialog_title));
            progressDialog.setMessage(getString(R.string.task_message));
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            FirebaseDatabase.getInstance().getReference().child("User").child(auth.getUid()).child("Tasks").removeValue(new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                    DataBaseHelper helper = new DataBaseHelper(context);
                    SQLiteDatabase database = helper.getReadableDatabase();
                    Cursor cursor = database.rawQuery("select * FROM " + DataBaseHelper.TABLE_TASK, null);
                    if (cursor.moveToFirst()) {
                        int idIndex = cursor.getColumnIndex(DataBaseHelper.KEY_ID);
                        int nameIndex = cursor.getColumnIndex(DataBaseHelper.KEY_NAME);
                        int infoIndex = cursor.getColumnIndex(DataBaseHelper.KEY_INFO);
                        int groupIndex = cursor.getColumnIndex(DataBaseHelper.KEY_GROUP);
                        int timeCreateIndex = cursor.getColumnIndex(DataBaseHelper.KEY_TIME_CREATE);
                        int timeCompleteIndex = cursor.getColumnIndex(DataBaseHelper.KEY_TIME_COMPLETE);
                        int doneIndex = cursor.getColumnIndex(DataBaseHelper.KEY_DONE);
                        int priorityIndex = cursor.getColumnIndex(DataBaseHelper.KEY_PRIORITY);
                        int notificationIndex = cursor.getColumnIndex(DataBaseHelper.KEY_NOTIFICATION);
                        do {
                            Task task = new Task(
                                    cursor.getString(nameIndex),
                                    cursor.getString(infoIndex),
                                    cursor.getString(groupIndex),
                                    cursor.getLong(timeCreateIndex),
                                    cursor.getLong(timeCompleteIndex),
                                    cursor.getInt(notificationIndex) == 1,
                                    cursor.getInt(doneIndex) == 1,
                                    cursor.getInt(priorityIndex),
                                    cursor.getInt(idIndex));
                            FirebaseDatabase.getInstance().getReference().child("User").child(auth.getUid()).child("Tasks").child(String.valueOf(task.getId())).setValue(task);
                        } while (cursor.moveToNext());
                    }
                    cursor.close();
                    Toast.makeText(context, R.string.completed, Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    ((AppCompatActivity) context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                }
            });
        }
    }
}