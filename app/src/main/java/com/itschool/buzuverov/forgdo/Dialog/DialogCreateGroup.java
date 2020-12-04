package com.itschool.buzuverov.forgdo.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.itschool.buzuverov.forgdo.R;

public class DialogCreateGroup extends AppCompatDialogFragment {

    private DialogCreateGroupListener listener;
    private String names;
    private Context context;

    public DialogCreateGroup(DialogCreateGroupListener listener, Context context) {
        this.listener = listener;
        this.context = context;
        stopTransform();
    }

    public DialogCreateGroup(DialogCreateGroupListener listener, String name, Context context) {
        this.listener = listener;
        this.context = context;
        names = name;
        stopTransform();
    }

    private void stopTransform() {
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
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_create_group, null);
        final EditText name = view.findViewById(R.id.group_info_name);
        name.setText(names);
        Button accept = view.findViewById(R.id.group_dialog_accept);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                names = name.getText().toString().trim();
                if (names.length() > 0) {
                    dismiss();
                    listener.create(names);
                } else
                    Toast.makeText(context, context.getString(R.string.enter_name), Toast.LENGTH_SHORT).show();

            }
        });
        Button cancel = view.findViewById(R.id.group_dialog_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        return builder.create();
    }

    public interface DialogCreateGroupListener {
        void create(String name);
    }
}