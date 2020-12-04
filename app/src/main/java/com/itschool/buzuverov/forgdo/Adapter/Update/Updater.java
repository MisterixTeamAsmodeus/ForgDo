package com.itschool.buzuverov.forgdo.Adapter.Update;

import android.util.Log;

import androidx.recyclerview.widget.DiffUtil;
import java.util.ArrayList;

public class Updater<T extends UpdaterInterface> extends DiffUtil.Callback{

    private final ArrayList<T> oldList;
    private final ArrayList<T> newList;

    public Updater(ArrayList<T> oldList, ArrayList<T> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getId() == newList.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).hashCode() == newList.get(newItemPosition).hashCode();
    }
}
