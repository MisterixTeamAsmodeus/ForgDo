package com.itschool.buzuverov.forgdo.Model.Tasks;

import com.itschool.buzuverov.forgdo.Adapter.Update.UpdaterInterface;

import java.util.Objects;

public class Task implements UpdaterInterface {

    public static final int
            HIGH = 0,
            MEDIUM = 1,
            LOW = 2,
            NONE = 3;

    private String name;
    private String info;
    private String groupName;
    private long dateCreate, deadline;
    private boolean onNotificationOn, isDone;
    private int priority, id;

    public Task(String name, String info, String groupName, long dateCreate, long deadline, boolean onNotificationOn, boolean isDone, int priority, int id) {
        this.name = name;
        this.info = info;
        this.groupName = groupName;
        this.dateCreate = dateCreate;
        this.deadline = deadline;
        this.onNotificationOn = onNotificationOn;
        this.isDone = isDone;
        this.priority = priority;
        this.id = id;
    }

    public Task() {
    }

    public String getName() {
        return name;
    }

    public String getInfo() {
        return info;
    }

    public String getGroupName() {
        return groupName;
    }

    public long getDateCreate() {
        return dateCreate;
    }

    public long getDeadline() {
        return deadline;
    }

    public boolean isOnNotification() {
        return onNotificationOn;
    }

    public boolean isDone() {
        return isDone;
    }

    public int getPriority() {
        return priority;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return dateCreate == task.dateCreate &&
                deadline == task.deadline &&
                onNotificationOn == task.onNotificationOn &&
                isDone == task.isDone &&
                priority == task.priority &&
                id == task.id &&
                name.equals(task.name) &&
                info.equals(task.info) &&
                groupName.equals(task.groupName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name) + Objects.hash(info) + Objects.hash(groupName) + Objects.hash(dateCreate) + Objects.hash(deadline) + Objects.hash(onNotificationOn) + Objects.hash(isDone) + Objects.hash(priority) + Objects.hash(id);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
