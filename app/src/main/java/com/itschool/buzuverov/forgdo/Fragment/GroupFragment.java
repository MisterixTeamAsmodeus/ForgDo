package com.itschool.buzuverov.forgdo.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itschool.buzuverov.forgdo.Adapter.GroupAdapter;
import com.itschool.buzuverov.forgdo.Adapter.TaskAdapter;
import com.itschool.buzuverov.forgdo.Model.Avatar.AvatarHelper;
import com.itschool.buzuverov.forgdo.Model.DataBaseHelper;
import com.itschool.buzuverov.forgdo.Model.Fragment.MyFragment;
import com.itschool.buzuverov.forgdo.Model.Fragment.SwipeDetector;
import com.itschool.buzuverov.forgdo.Model.Tasks.GroupTask;
import com.itschool.buzuverov.forgdo.R;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class GroupFragment extends MyFragment implements GroupAdapter.GroupUpdaterInterface, TaskAdapter.UpdateListener {

    private View view;
    private TextView[] cardInfo;
    private TextView textInfo;
    private GroupAdapter groupAdapter;
    private Context context;
    private RecyclerView groupList;
    private DataBaseHelper dataBaseHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_groups, container, false);
        setDefaultInfo();
        initGroupRecyclerView();
        initTaskRecycleView();
        initCardInfo();
        return view;
    }

    private void setDefaultInfo() {
        textInfo = view.findViewById(R.id.group_fragment_text_info);

        View statusLayout = view.findViewById(R.id.groups_fragment_status_layout);
        statusLayout.startAnimation(AnimationUtils.loadAnimation(context, R.anim.show_status_layout));

        TextView userName = statusLayout.findViewById(R.id.status_layout_name);
        userName.setText(String.format("%s%s.", getString(R.string.hi), context.getSharedPreferences("Preferences", MODE_PRIVATE).getString("User name", "User")));

        ImageView userAvatar = statusLayout.findViewById(R.id.status_layout_avatar);
        userAvatar.setImageResource(AvatarHelper.getSelectAvatar(context.getSharedPreferences("Preferences", MODE_PRIVATE).getInt("User avatar", R.id.select_avatar_activity_avatar_1)));

    }

    private void initGroupRecyclerView() {
        dataBaseHelper = new DataBaseHelper(context);

        groupList = view.findViewById(R.id.group_fragment_groups_list);
        groupList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        groupAdapter = new GroupAdapter(context, new TaskAdapter(context, this), this);

        groupList.setAdapter(groupAdapter);
        groupList.scrollToPosition(groupAdapter.getIndexSelect());

        groupAdapter.setData(getAllGroup());
        groupAdapter.notifyDataSetChanged();

        if (groupAdapter.getItemCount() == 0) {
            textInfo.setVisibility(View.VISIBLE);
            textInfo.startAnimation(AnimationUtils.loadAnimation(context, R.anim.show_task_info_text));
        } else {
            textInfo.setVisibility(View.GONE);
            groupList.startAnimation(AnimationUtils.loadAnimation(context, R.anim.show_card_info));
        }
    }

    private void initTaskRecycleView() {
        RecyclerView taskList = view.findViewById(R.id.group_fragment_task_list);
        taskList.setLayoutManager(new LinearLayoutManager(context));
        taskList.setOnTouchListener(new SwipeDetector(context) {
            @Override
            public void onSwipeDetected(Direction direction) {
                switch (direction) {
                    case RIGHT:
                        groupAdapter.nextGroup();
                        groupList.scrollToPosition(groupAdapter.getIndexSelect());
                        break;
                    case LEFT:
                        groupAdapter.previousGroup();
                        groupList.scrollToPosition(groupAdapter.getIndexSelect());
                        break;
                }
            }
        });
        taskList.setAdapter(groupAdapter.getTaskAdapter());
        taskList.startAnimation(AnimationUtils.loadAnimation(context, R.anim.show_task_adapter));
    }

    private void initCardInfo() {
        cardInfo = new TextView[3];
        View cards = view.findViewById(R.id.groups_fragment_card_info);
        cardInfo[0] = cards.findViewById(R.id.card_info_outstanding);
        cardInfo[1] = cards.findViewById(R.id.card_info_competed);
        cardInfo[2] = cards.findViewById(R.id.card_info_total);
        onGroupSelect();
        cards.startAnimation(AnimationUtils.loadAnimation(context, R.anim.show_card_info));
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDataUpdate() {
        groupAdapter.setData(getAllGroup());
        if (groupAdapter.getItemCount() == 0) {
            textInfo.setVisibility(View.VISIBLE);
            textInfo.startAnimation(AnimationUtils.loadAnimation(context, R.anim.show_task_info_text));
        } else {
            textInfo.setVisibility(View.GONE);
        }
        onGroupSelect();
    }

    private ArrayList<GroupTask> getAllGroup() {
        ArrayList<GroupTask> groupTasks = new ArrayList<>();
        for (String str : dataBaseHelper.getAllGroupName()) {
            groupTasks.add(new GroupTask(str, dataBaseHelper.getTaskInGroup(str), false));
        }
        return groupTasks;
    }

    @Override
    public void setDefault() {
        GroupAdapter.setDefaultIndexSelect();
    }

    @Override
    public void onGroupSelect() {
        cardInfo[0].setText(groupAdapter.getTaskAdapter().getNotDoneCount());
        cardInfo[1].setText(groupAdapter.getTaskAdapter().getDoneCount());
        cardInfo[2].setText(groupAdapter.getTaskAdapter().getAllTask());
    }

    @Override
    public void updateAdapter() {
        onDataUpdate();
    }

    @Override
    public void updateInfo() {
        onDataUpdate();
        groupList.scrollToPosition(groupAdapter.getItemCount());
    }

    @Override
    public void deleteLastShowTask() {
        if (groupAdapter.getItemCount() == 1) {
            textInfo.setVisibility(View.VISIBLE);
            textInfo.startAnimation(AnimationUtils.loadAnimation(context, R.anim.show_task_info_text));
            groupAdapter.setData(new ArrayList<GroupTask>());
            groupAdapter.notifyDataSetChanged();
        } else {
            if (groupAdapter.getIndexSelect() == groupAdapter.getItemCount() - 1) {
                groupAdapter.reduceIndexSelect();
            }
            onDataUpdate();
        }
    }

    public String getActiveGroupName() {
        return groupAdapter.getActiveGroupName();
    }
}