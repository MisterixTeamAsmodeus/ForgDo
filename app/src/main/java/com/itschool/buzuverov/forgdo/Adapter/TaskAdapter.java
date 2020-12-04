package com.itschool.buzuverov.forgdo.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.itschool.buzuverov.forgdo.Activity.EditTaskActivity;
import com.itschool.buzuverov.forgdo.Activity.MainActivity;
import com.itschool.buzuverov.forgdo.Activity.ShowInfoActivity;
import com.itschool.buzuverov.forgdo.Adapter.Update.Updater;
import com.itschool.buzuverov.forgdo.Dialog.DialogShowAllGroup;
import com.itschool.buzuverov.forgdo.Model.DataBaseHelper;
import com.itschool.buzuverov.forgdo.Model.Notification.AlarmHelper;
import com.itschool.buzuverov.forgdo.Model.Tasks.FilterTask;
import com.itschool.buzuverov.forgdo.Model.Tasks.Task;
import com.itschool.buzuverov.forgdo.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private Context context;
    private String searchName = "";
    private ArrayList<Task> allTask;
    private ArrayList<Task> showTask;
    private LayoutInflater inflater;
    private int filterTask = FilterTask.ALL;
    private int doneCount = 0;
    private int notDoneCount = 0;
    private UpdateListener listener;
    private String userId;

    public TaskAdapter(Context context, UpdateListener listener) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.listener = listener;
        allTask = new ArrayList<>();
        showTask = new ArrayList<>();
        userId = FirebaseAuth.getInstance().getUid();
    }

    public void setDate(ArrayList<Task> date) {
        allTask.clear();
        allTask.addAll(date);
        mathTaskCount();
        ArrayList<Task> old = new ArrayList<>(showTask);
        setShow();
        DiffUtil.calculateDiff(new Updater<>(old, showTask)).dispatchUpdatesTo(this);
    }

    public void clear(){
        allTask = new ArrayList<>();
        showTask = new ArrayList<>();
        notifyDataSetChanged();
    }

    private void setShow() {
        if (searchName.trim().length() > 0) {
            showTask.clear();
            for (Task t : allTask) {
                if (t.getName().toLowerCase().contains(searchName))
                    showTask.add(t);
            }
        } else {
            switch (filterTask) {
                case FilterTask.ALL:
                    showTask.clear();
                    showTask.addAll(allTask);
                    break;
                case FilterTask.COMPLETED:
                    showTask.clear();
                    for (Task task : allTask) {
                        if (task.isDone()) {
                            showTask.add(task);
                        }
                    }
                    break;
                case FilterTask.OUTSTANDING:
                    showTask.clear();
                    for (Task task : allTask) {
                        if (!task.isDone()) {
                            showTask.add(task);
                        }
                    }
                    break;
                case FilterTask.RECENT:
                    showTask.clear();
                    int lastIndex = Math.max((allTask.size() - 1 - 10), 0);
                    for (int i = allTask.size() - 1; i >= lastIndex; i--) {
                        showTask.add(allTask.get(i));
                    }
                    Collections.reverse(showTask);
                    break;
                case FilterTask.TODAY:
                    showTask.clear();
                    DateFormat dateFormat = new SimpleDateFormat("d.MM");
                    for (Task task : allTask) {
                        if (dateFormat.format(new Date(task.getDateCreate())).equals(dateFormat.format(new Date().getTime()))) {
                            showTask.add(task);
                        }
                    }
                    break;
            }
        }
        if (getItemCount() == 0) {
            listener.deleteLastShowTask();
        }
    }

    private void mathTaskCount() {
        doneCount = 0;
        notDoneCount = 0;
        for (Task task : allTask) {
            if (task.isDone()) {
                doneCount++;
            } else {
                notDoneCount++;
            }
        }
    }

    private void deleteTask(int position) {
        AlarmHelper.cancelReminder(context, showTask.get(position).getName(), showTask.get(position).getInfo());
        new DataBaseHelper(context).deleteTask(showTask.get(position), userId);
        allTask.remove(showTask.get(position));
        setShow();
        notifyItemRemoved(position);
        mathTaskCount();
        listener.updateInfo();
    }

    public void deleteAll(){
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        for (int i = 0; i < allTask.size(); i++){
            dataBaseHelper.deleteTask(allTask.get(i),userId);
            AlarmHelper.cancelReminder(context, allTask.get(i).getName(), allTask.get(i).getInfo());
        }
        setDate(new ArrayList<Task>());
        listener.deleteLastShowTask();
    }

    public void moveAll(String name){
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        for (int i = 0; i < allTask.size(); i++){
            allTask.get(i).setGroupName(name);
            dataBaseHelper.updateTask(allTask.get(i), userId);
        }
    }

    private void showAllGroup(final int pos) {
        DialogShowAllGroup createTask = new DialogShowAllGroup(new DialogShowAllGroup.DialogShowAllGroupListener() {
            @Override
            public void select(String name) {
                if (!name.equals(showTask.get(pos).getGroupName())) {
                    showTask.get(pos).setGroupName(name);
                    new DataBaseHelper(context).updateTask(showTask.get(pos), userId);
                    allTask.remove(showTask.get(pos));
                    setShow();
                    notifyItemRemoved(pos);
                    mathTaskCount();
                    listener.updateInfo();
                }
            }
        }, new DataBaseHelper(context).getAllGroupName(showTask.get(pos).getGroupName()), context);
        createTask.show(((MainActivity) context).getSupportFragmentManager(), "tag");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.task_layout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        holder.bindClick();
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        position = holder.getAdapterPosition();
        holder.bindInfo(position);
    }

    @Override
    public int getItemCount() {
        return showTask.size();
    }

    public String getDoneCount() {
        return String.valueOf(doneCount);
    }

    public String getNotDoneCount() {
        return String.valueOf(notDoneCount);
    }

    public String getAllTask() {
        return String.valueOf(allTask.size());
    }

    public void setFilterTask(int filterTask) {
        this.filterTask = filterTask;
        ArrayList<Task> old = new ArrayList<>(showTask);
        setShow();
        DiffUtil.calculateDiff(new Updater<>(old, showTask)).dispatchUpdatesTo(this);
    }

    public void setTextSearch(String searchName) {
        this.searchName = searchName.toLowerCase();
        ArrayList<Task> old = new ArrayList<>(showTask);
        setShow();
        DiffUtil.calculateDiff(new Updater<>(old, showTask)).dispatchUpdatesTo(this);
    }

    public interface UpdateListener {
        void updateInfo();
        void deleteLastShowTask();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private CardView main;
        private TextView name, info, date, priority;
        private RelativeLayout check;
        private ImageView notification, checkIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            main = itemView.findViewById(R.id.task_layout_main);
            name = itemView.findViewById(R.id.task_layout_name);
            info = itemView.findViewById(R.id.task_layout_info);
            date = itemView.findViewById(R.id.task_layout_date);
            priority = itemView.findViewById(R.id.task_layout_priority);
            check = itemView.findViewById(R.id.task_layout_check_layout);
            checkIcon = itemView.findViewById(R.id.task_layout_check);
            notification = itemView.findViewById(R.id.task_layout_notification);
        }

        public void bindInfo(int position) {
            Task task = showTask.get(position);
            name.setText(task.getName());
            if (task.getInfo().length() > 0) {
                info.setText(task.getInfo());
                info.setVisibility(View.VISIBLE);
            } else {
                info.setVisibility(View.GONE);
            }
            notification.setVisibility(task.isOnNotification() ? View.VISIBLE : View.GONE);
            checkIcon.setBackgroundResource(task.isDone() ? R.drawable.select_background_check : R.drawable.select_background);
            DateFormat dateFormat = new SimpleDateFormat("d.MM");
            date.setText(dateFormat.format(new Date(task.getDeadline())));
            switch (task.getPriority()) {
                case Task.HIGH:
                    priority.setBackgroundResource(R.drawable.text_high);
                    priority.setText(context.getString(R.string.high));
                    break;
                case Task.MEDIUM:
                    priority.setBackgroundResource(R.drawable.text_medium);
                    priority.setText(context.getString(R.string.medium));
                    break;
                case Task.LOW:
                    priority.setBackgroundResource(R.drawable.text_low);
                    priority.setText(context.getString(R.string.low));
                    break;
                case Task.NONE:
                    priority.setBackgroundResource(R.drawable.text_none);
                    priority.setText(context.getString(R.string.none));
                    break;
            }
        }

        public void bindClick() {
            main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        Intent intent = new Intent(context, ShowInfoActivity.class);
                        intent.putExtra("id", showTask.get(pos).getId());
                        context.startActivity(intent);
                    }
                }
            });

            check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        if (showTask.get(position).isDone()) {
                            doneCount--;
                            notDoneCount++;
                        } else {
                            doneCount++;
                            notDoneCount--;
                        }
                        showTask.get(position).setDone(!showTask.get(position).isDone());
                        checkIcon.setBackgroundResource(showTask.get(position).isDone() ? R.drawable.select_background_check : R.drawable.select_background);
                        new DataBaseHelper(context).updateTask(showTask.get(position), userId);
                        listener.updateInfo();
                        if (filterTask == FilterTask.OUTSTANDING && showTask.get(position).isDone()) {
                            notifyItemRemoved(position);
                        } else if (filterTask == FilterTask.COMPLETED && !showTask.get(position).isDone()) {
                            notifyItemRemoved(position);
                        }
                        setShow();
                    }
                }
            });

            main.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    PopupMenu menu = new PopupMenu(view.getContext(), view);
                    menu.inflate(R.menu.task_popup_menu);
                    menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            int pos = getAdapterPosition();
                            if (pos != RecyclerView.NO_POSITION) {
                                switch (menuItem.getItemId()) {
                                    case R.id.task_menu_delete:
                                        deleteTask(pos);
                                        break;
                                    case R.id.task_menu_edit:
                                        Intent intent = new Intent(context, EditTaskActivity.class);
                                        intent.putExtra("id", showTask.get(pos).getId());
                                        context.startActivity(intent);
                                        break;
                                    case R.id.task_menu_move:
                                        showAllGroup(pos);
                                        break;
                                }
                            }
                            return true;
                        }
                    });
                    menu.show();
                    return false;
                }
            });
        }
    }
}
