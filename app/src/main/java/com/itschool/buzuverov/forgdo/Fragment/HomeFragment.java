package com.itschool.buzuverov.forgdo.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.itschool.buzuverov.forgdo.Adapter.FilterAdapter;
import com.itschool.buzuverov.forgdo.Adapter.TaskAdapter;
import com.itschool.buzuverov.forgdo.Model.Avatar.AvatarHelper;
import com.itschool.buzuverov.forgdo.Model.DataBaseHelper;
import com.itschool.buzuverov.forgdo.Model.Fragment.MyFragment;
import com.itschool.buzuverov.forgdo.Model.Fragment.SwipeDetector;
import com.itschool.buzuverov.forgdo.Model.Tasks.FilterTask;
import com.itschool.buzuverov.forgdo.R;
import java.util.ArrayList;
import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends MyFragment implements TaskAdapter.UpdateListener{

    private View view;
    private Context context;
    private TextView[] cardInfo;
    private FilterAdapter filterAdapter;
    private TextView textInfo;
    private DataBaseHelper dataBaseHelper;
    private Animation animation;
    private RecyclerView filterList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        setDefaultInfo();
        initFilterRecyclerView();
        initTaskRecyclerView();
        initCardInfo();
        return view;
    }

    private void setDefaultInfo() {
        animation = AnimationUtils.loadAnimation(context, R.anim.show_task_info_text);
        textInfo = view.findViewById(R.id.home_fragment_text_info);
        textInfo.setText(R.string.no_tasks);
        textInfo.setVisibility(View.GONE);

        View statusLayout = view.findViewById(R.id.home_fragment_status_layout);
        statusLayout.startAnimation(AnimationUtils.loadAnimation(context, R.anim.show_status_layout));

        TextView userName = statusLayout.findViewById(R.id.status_layout_name);
        userName.setText(String.format("%s%s.", getString(R.string.hi), context.getSharedPreferences("Preferences", MODE_PRIVATE).getString("User name", "User")));

        ImageView userAvatar = statusLayout.findViewById(R.id.status_layout_avatar);
        userAvatar.setImageResource(AvatarHelper.getSelectAvatar(context.getSharedPreferences("Preferences", MODE_PRIVATE).getInt("User avatar", R.id.select_avatar_activity_avatar_1)));
    }

    private void initTaskRecyclerView() {
        RecyclerView taskList = view.findViewById(R.id.home_fragment_list_task);
        taskList.setOnTouchListener(new SwipeDetector(context) {
            @Override
            public void onSwipeDetected(Direction direction) {
                switch (direction){
                    case RIGHT:
                        filterAdapter.nextFilter();
                        filterList.scrollToPosition(filterAdapter.getIndexSelect());
                        break;
                    case LEFT:
                        filterAdapter.previousFilter();
                        filterList.scrollToPosition(filterAdapter.getIndexSelect());
                        break;
                }
            }
        });
        taskList.setLayoutManager(new LinearLayoutManager(context));
        taskList.setAdapter(filterAdapter.getTaskAdapter());
        filterAdapter.setData(dataBaseHelper.getTaskInGroup("null"));
        taskList.startAnimation(AnimationUtils.loadAnimation(context, R.anim.show_task_adapter));
    }

    private void initFilterRecyclerView() {
        dataBaseHelper = new DataBaseHelper(context);

        filterList = view.findViewById(R.id.home_fragment_filter_list);
        filterList.startAnimation(AnimationUtils.loadAnimation(context, R.anim.show_card_info));
        filterList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        filterAdapter = new FilterAdapter(context, getAllFilters(), this, new FilterAdapter.FilterInterface() {
            @Override
            public void onFilterChanged() {
                switch (filterAdapter.getIndexSelect()) {
                    case FilterTask.ALL:
                    case FilterTask.RECENT:
                        textInfo.setText(R.string.no_tasks);
                        break;
                    case FilterTask.COMPLETED:
                        textInfo.setText(R.string.no_check_task);
                        break;
                    case FilterTask.OUTSTANDING:
                        textInfo.setText(R.string.no_not_check_task);
                        break;
                    case FilterTask.TODAY:
                        textInfo.setText(R.string.dont_create_task_today);
                        break;
                }
                animTextInfo();
            }
        });
        filterList.setAdapter(filterAdapter);
        filterList.scrollToPosition(filterAdapter.getIndexSelect());
    }

    private ArrayList<FilterTask> getAllFilters() {
        ArrayList<FilterTask> filterTasks = new ArrayList<>();
        filterTasks.add(new FilterTask(getString(R.string.outstanding), FilterTask.OUTSTANDING));
        filterTasks.add(new FilterTask(getString(R.string.all_task), FilterTask.ALL));
        filterTasks.add(new FilterTask(getString(R.string.recent), FilterTask.RECENT));
        filterTasks.add(new FilterTask(getString(R.string.today), FilterTask.TODAY));
        filterTasks.add(new FilterTask(getString(R.string.completed), FilterTask.COMPLETED));
        return filterTasks;
    }

    private void initCardInfo() {
        cardInfo = new TextView[3];
        View cards = view.findViewById(R.id.home_fragment_cards);
        cardInfo[0] = cards.findViewById(R.id.card_info_outstanding);
        cardInfo[1] = cards.findViewById(R.id.card_info_competed);
        cardInfo[2] = cards.findViewById(R.id.card_info_total);
        updateInfo();
        cards.startAnimation(AnimationUtils.loadAnimation(context, R.anim.show_card_info));
    }

    @Override
    public void onDataUpdate() {
        updateInfo();
        filterAdapter.getTaskAdapter().setDate(dataBaseHelper.getTaskInGroup("null"));
        animTextInfo();
    }

    @Override
    public void setDefault() {
        FilterAdapter.setDefaultIndexSelect();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void updateInfo() {
        cardInfo[0].setText(filterAdapter.getTaskAdapter().getNotDoneCount());
        cardInfo[1].setText(filterAdapter.getTaskAdapter().getDoneCount());
        cardInfo[2].setText(filterAdapter.getTaskAdapter().getAllTask());
    }

    @Override
    public void deleteLastShowTask() {
        animTextInfo();
    }

    private void animTextInfo() {
        if (filterAdapter.getTaskAdapter().getItemCount() == 0) {
            if (textInfo.getVisibility() != View.VISIBLE){
                textInfo.setVisibility(View.VISIBLE);
                textInfo.startAnimation(animation);
            }
        } else {
            animation.cancel();
            textInfo.setVisibility(View.GONE);
        }
    }
}