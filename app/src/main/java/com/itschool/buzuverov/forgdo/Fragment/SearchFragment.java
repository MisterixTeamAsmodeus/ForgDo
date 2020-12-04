package com.itschool.buzuverov.forgdo.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itschool.buzuverov.forgdo.Adapter.TaskAdapter;
import com.itschool.buzuverov.forgdo.Model.Avatar.AvatarHelper;
import com.itschool.buzuverov.forgdo.Model.DataBaseHelper;
import com.itschool.buzuverov.forgdo.Model.Fragment.MyFragment;
import com.itschool.buzuverov.forgdo.R;

import static android.content.Context.MODE_PRIVATE;

public class SearchFragment extends MyFragment {
    private View view;
    private Context context;
    private TaskAdapter taskAdapter;
    private TextView textInfo;
    private static String textSearch = "";
    private DataBaseHelper dataBaseHelper;
    private Animation animation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search, container, false);
        dataBaseHelper = new DataBaseHelper(context);
        textInfo = view.findViewById(R.id.search_fragment_text_info);
        setDefaultInfo();
        initRecycleView();
        return view;
    }

    private void setDefaultInfo() {
        View statusLayout = view.findViewById(R.id.search_fragment_status_layout);
        TextView userName = statusLayout.findViewById(R.id.status_layout_name);
        userName.setText(String.format("%s%s.", getString(R.string.hi), context.getSharedPreferences("Preferences", MODE_PRIVATE).getString("User name", "User")));
        ImageView userAvatar = statusLayout.findViewById(R.id.status_layout_avatar);
        userAvatar.setImageResource(AvatarHelper.getSelectAvatar(context.getSharedPreferences("Preferences", MODE_PRIVATE).getInt("User avatar", R.id.select_avatar_activity_avatar_1)));
        statusLayout.startAnimation(AnimationUtils.loadAnimation(context, R.anim.show_status_layout));
        view.findViewById(R.id.constraintLayout5).startAnimation(AnimationUtils.loadAnimation(context, R.anim.show_status_layout));
    }

    private void initRecycleView() {
        animation = AnimationUtils.loadAnimation(context, R.anim.show_task_info_text);
        EditText search = view.findViewById(R.id.search_fragment_search);
        search.setText(textSearch);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textSearch = charSequence.toString();
                taskAdapter.setTextSearch(charSequence.toString());
                animTextInfo();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        RecyclerView taskList = view.findViewById(R.id.search_fragment_list_task);
        taskList.setLayoutManager(new LinearLayoutManager(context));
        taskAdapter = new TaskAdapter(context, new TaskAdapter.UpdateListener() {
            @Override
            public void updateInfo() {

            }

            @Override
            public void deleteLastShowTask() {
                animTextInfo();
            }
        });
        taskList.setAdapter(taskAdapter);
        taskAdapter.setDate(dataBaseHelper.getAllTask());
        taskAdapter.setTextSearch(search.getText().toString());
        animTextInfo();
        taskList.startAnimation(AnimationUtils.loadAnimation(context, R.anim.show_task_adapter));
    }

    @Override
    public void onDataUpdate() {
        taskAdapter.setDate(dataBaseHelper.getAllTask());
        animTextInfo();
    }

    @Override
    public void setDefault() {
        textSearch = "";
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    private void animTextInfo() {
        if (taskAdapter.getItemCount() != 0) {
            animation.cancel();
            textInfo.setVisibility(View.GONE);
        } else {
            if (textInfo.getVisibility() != View.VISIBLE){
                textInfo.setVisibility(View.VISIBLE);
                textInfo.startAnimation(animation);
            }
        }
    }
}