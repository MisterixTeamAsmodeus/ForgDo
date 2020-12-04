package com.itschool.buzuverov.forgdo.Model.Tasks;

import java.util.ArrayList;

public class GroupTask {
    private String name;
    private ArrayList<Task> tasks;
    private boolean isSelect;
    private int id = 0;

    public GroupTask(String name, ArrayList<Task> tasks, boolean isSelect) {
        this.name = name;
        this.tasks = tasks;
        this.isSelect = isSelect;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
