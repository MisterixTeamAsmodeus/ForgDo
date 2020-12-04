package com.itschool.buzuverov.forgdo.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.google.firebase.database.FirebaseDatabase;
import com.itschool.buzuverov.forgdo.Model.Tasks.Task;

import java.util.ArrayList;
import java.util.Arrays;

import static android.content.Context.MODE_PRIVATE;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "taskDataBase";
    public static final String TABLE_TASK = "task";
    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_INFO = "info";
    public static final String KEY_GROUP = "groups";
    public static final String KEY_TIME_CREATE = "timeCreate";
    public static final String KEY_TIME_COMPLETE = "timeComplete";
    public static final String KEY_DONE = "isDone";
    public static final String KEY_PRIORITY = "priority";
    public static final String KEY_NOTIFICATION = "onNotificationOn";
    private boolean isSynchronise;

    public DataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        isSynchronise = context.getSharedPreferences("Preferences", MODE_PRIVATE).getBoolean("AutoSynchronizations", false);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                "CREATE TABLE " + TABLE_TASK + "(" +
                        KEY_ID + " INTEGER PRIMARY KEY," +
                        KEY_NAME + " TEXT," +
                        KEY_INFO + " TEXT," +
                        KEY_GROUP + " TEXT," +
                        KEY_TIME_CREATE + " NUMERIC," +
                        KEY_TIME_COMPLETE + " NUMERIC," +
                        KEY_DONE + " NUMERIC," +
                        KEY_NOTIFICATION + " NUMERIC," +
                        KEY_PRIORITY + " NUMERIC" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists " + TABLE_TASK);
        onCreate(sqLiteDatabase);
    }

    public ArrayList<Task> getTaskInGroup(String groupName) {
        ArrayList<Task> tasks = new ArrayList<>();
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery("select * FROM " + DataBaseHelper.TABLE_TASK + " WHERE " + DataBaseHelper.KEY_GROUP + " = ?", new String[]{groupName});
        addTasksInArray(tasks, cursor);
        return tasks;
    }

    public void addTasksInArray(ArrayList<Task> tasks, Cursor cursor) {
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
                tasks.add(new Task(
                        cursor.getString(nameIndex),
                        cursor.getString(infoIndex),
                        cursor.getString(groupIndex),
                        cursor.getLong(timeCreateIndex),
                        cursor.getLong(timeCompleteIndex),
                        cursor.getInt(notificationIndex) == 1,
                        cursor.getInt(doneIndex) == 1,
                        cursor.getInt(priorityIndex),
                        cursor.getInt(idIndex)));
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    public void updateTask(Task task, String userId) {
        SQLiteDatabase database = getWritableDatabase();
        database.update(DataBaseHelper.TABLE_TASK, getContentValues(task), KEY_ID + " = " + task.getId(), null);
        if (userId != null && isSynchronise) {
            FirebaseDatabase.getInstance().getReference().child("User").child(userId).child("Tasks").child(String.valueOf(task.getId())).setValue(task);
        }
    }

    public void createTask(Task task, String userId) {
        SQLiteDatabase database = getWritableDatabase();
        database.insert(DataBaseHelper.TABLE_TASK, null, getContentValues(task));
        if (userId != null && isSynchronise) {
            FirebaseDatabase.getInstance().getReference().child("User").child(userId).child("Tasks").child(String.valueOf(task.getId())).setValue(task);
        }
    }

    private ContentValues getContentValues(Task task) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DataBaseHelper.KEY_NAME, task.getName());
        contentValues.put(DataBaseHelper.KEY_INFO, task.getInfo());
        contentValues.put(DataBaseHelper.KEY_GROUP, task.getGroupName());
        contentValues.put(DataBaseHelper.KEY_TIME_COMPLETE, task.getDeadline());
        contentValues.put(DataBaseHelper.KEY_TIME_CREATE, task.getDateCreate());
        contentValues.put(DataBaseHelper.KEY_NOTIFICATION, task.isOnNotification());
        contentValues.put(DataBaseHelper.KEY_DONE, task.isDone());
        contentValues.put(DataBaseHelper.KEY_PRIORITY, task.getPriority());
        contentValues.put(DataBaseHelper.KEY_ID, task.getId());
        return contentValues;
    }

    public ArrayList<Task> getAllTask() {
        ArrayList<Task> tasks = new ArrayList<>();
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery("select * FROM " + DataBaseHelper.TABLE_TASK, null);
        addTasksInArray(tasks, cursor);
        cursor.close();
        return tasks;
    }

    public String[] getAllGroupName(String groupName) {
        ArrayList<String> groups = new ArrayList<>(Arrays.asList(getAllGroupName()));
        if (!groupName.equals("null") && !groups.contains("null"))
            groups.add("null");
        return groups.toArray(new String[0]);
    }

    public void deleteTask(Task task, String userId) {
        SQLiteDatabase database = getWritableDatabase();
        database.delete(DataBaseHelper.TABLE_TASK, KEY_ID + "=" + task.getId(), null);
        if (userId != null && isSynchronise) {
            FirebaseDatabase.getInstance().getReference().child("User").child(userId).child("Tasks").child(String.valueOf(task.getId())).removeValue();
        }
    }

    public Task getTask(int id) {
        Task task = new Task();
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery("select * FROM " + DataBaseHelper.TABLE_TASK + " WHERE " + KEY_ID + " = ?", new String[]{String.valueOf(id)});
        int idIndex = cursor.getColumnIndex(DataBaseHelper.KEY_ID);
        int nameIndex = cursor.getColumnIndex(DataBaseHelper.KEY_NAME);
        int infoIndex = cursor.getColumnIndex(DataBaseHelper.KEY_INFO);
        int groupIndex = cursor.getColumnIndex(DataBaseHelper.KEY_GROUP);
        int timeCreateIndex = cursor.getColumnIndex(DataBaseHelper.KEY_TIME_CREATE);
        int timeCompleteIndex = cursor.getColumnIndex(DataBaseHelper.KEY_TIME_COMPLETE);
        int doneIndex = cursor.getColumnIndex(DataBaseHelper.KEY_DONE);
        int priorityIndex = cursor.getColumnIndex(DataBaseHelper.KEY_PRIORITY);
        int notificationIndex = cursor.getColumnIndex(DataBaseHelper.KEY_NOTIFICATION);
        if (cursor.moveToFirst()) {
            task = new Task(
                    cursor.getString(nameIndex),
                    cursor.getString(infoIndex),
                    cursor.getString(groupIndex),
                    cursor.getLong(timeCreateIndex),
                    cursor.getLong(timeCompleteIndex),
                    cursor.getInt(notificationIndex) == 1,
                    cursor.getInt(doneIndex) == 1,
                    cursor.getInt(priorityIndex),
                    cursor.getInt(idIndex));
        }
        cursor.close();
        return task;
    }

    public String[] getAllGroupName() {
        ArrayList<String> groups = new ArrayList<>();
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery("select DISTINCT " + DataBaseHelper.KEY_GROUP + " FROM " + DataBaseHelper.TABLE_TASK + " WHERE " + DataBaseHelper.KEY_GROUP + " != ?", new String[]{"null"});
        if (cursor.moveToFirst()) {
            int groupIndex = cursor.getColumnIndex(DataBaseHelper.KEY_GROUP);
            do {
                groups.add(cursor.getString(groupIndex));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return groups.toArray(new String[0]);
    }
}
