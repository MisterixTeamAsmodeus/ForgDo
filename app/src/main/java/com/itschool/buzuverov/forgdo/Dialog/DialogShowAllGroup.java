package com.itschool.buzuverov.forgdo.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itschool.buzuverov.forgdo.Adapter.AllGroupsAdapter;
import com.itschool.buzuverov.forgdo.Model.Tasks.GroupTask;
import com.itschool.buzuverov.forgdo.R;

public class DialogShowAllGroup extends AppCompatDialogFragment {

    private DialogShowAllGroupListener listener;
    private String[] names;
    private Context context;

    public DialogShowAllGroup(DialogShowAllGroupListener listener, String[] names, Context context) {
        this.listener = listener;
        this.names = names;
        this.context = context;
        int currentOrientation = ((AppCompatActivity) context).getResources().getConfiguration().orientation;
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            ((AppCompatActivity) context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE); //locks landscape
        } else {
            ((AppCompatActivity) context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT); //locks port
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_show_all_groups, null);
        RecyclerView list = view.findViewById(R.id.dialog_show_all_groups_list);
        list.setLayoutManager(new LinearLayoutManager(context));
        GroupTask[] groups = new GroupTask[names.length + 1];
        for (int i = 0; i < names.length; i++) {
            if (names[i].equals("null"))
                groups[i] = new GroupTask(getString(R.string.delete_for_group), null, false);
            else
                groups[i] = new GroupTask(names[i], null, false);
        }
        groups[groups.length - 1] = new GroupTask(getString(R.string.create_new_group), null, false);
        list.setLayoutManager(new LinearLayoutManager(context));
        AllGroupsAdapter adapter = new AllGroupsAdapter(context, new AllGroupsAdapter.onSelect() {
            @Override
            public void select(String name) {
                listener.select(name);
                dismiss();
            }
        });
        list.setAdapter(adapter);
        adapter.setData(groups);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        ((AppCompatActivity) context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
    }

    public interface DialogShowAllGroupListener {
        void select(String name);
    }
}