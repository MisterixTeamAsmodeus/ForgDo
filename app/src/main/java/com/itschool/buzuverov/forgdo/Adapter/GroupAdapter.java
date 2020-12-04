package com.itschool.buzuverov.forgdo.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.itschool.buzuverov.forgdo.Dialog.DialogCreateGroup;
import com.itschool.buzuverov.forgdo.Model.DataBaseHelper;
import com.itschool.buzuverov.forgdo.Model.Notification.AlarmHelper;
import com.itschool.buzuverov.forgdo.Model.Tasks.GroupTask;
import com.itschool.buzuverov.forgdo.Model.Tasks.Task;
import com.itschool.buzuverov.forgdo.R;

import java.util.ArrayList;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> {

    private ArrayList<GroupTask> groups;
    private Context context;
    private static int indexSelect = 0;
    private LayoutInflater inflater;
    private TaskAdapter taskAdapter;
    private GroupUpdaterInterface groupUpdaterInterface;

    public GroupAdapter(Context context, TaskAdapter taskAdapter, GroupUpdaterInterface groupUpdaterInterface) {
        this.inflater = LayoutInflater.from(context);
        this.taskAdapter = taskAdapter;
        this.context = context;
        this.groupUpdaterInterface = groupUpdaterInterface;
    }

    public void setData(ArrayList<GroupTask> allGroup) {
        this.groups = allGroup;
        if (allGroup.size() != 0) {
            if (indexSelect >= allGroup.size()) {
                indexSelect = allGroup.size() - 1;
            }
            allGroup.get(indexSelect).setSelect(true);
            taskAdapter.setDate(allGroup.get(indexSelect).getTasks());
            notifyDataSetChanged();
        }
    }

    public static void setDefaultIndexSelect() {
        GroupAdapter.indexSelect = 0;
    }

    public int getIndexSelect() {
        return indexSelect;
    }

    public void reduceIndexSelect() {
        taskAdapter.clear();
        indexSelect--;
    }

    private void rename(final int pos) {
        new DialogCreateGroup(new DialogCreateGroup.DialogCreateGroupListener() {
            @Override
            public void create(String name) {
                if (!groups.get(pos).getName().equals(name)) {
                    taskAdapter.setDate(groups.get(pos).getTasks());
                    notifyDataSetChanged();
                    taskAdapter.moveAll(name);
                    groupUpdaterInterface.updateAdapter();
                }
            }
        }, groups.get(pos).getName(), context).show(((AppCompatActivity) context).getSupportFragmentManager(), "tag");
    }

    public TaskAdapter getTaskAdapter() {
        return taskAdapter;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.group_layout, parent, false);
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
        return groups.size();
    }

    public void nextGroup() {
        if (indexSelect + 1 < getItemCount()) {
            groups.get(indexSelect).setSelect(false);
            notifyItemChanged(indexSelect);
            indexSelect++;
            groups.get(indexSelect).setSelect(true);
            notifyItemChanged(indexSelect);
            taskAdapter.setDate(groups.get(indexSelect).getTasks());
            groupUpdaterInterface.onGroupSelect();
        }
    }

    public void previousGroup() {
        if (indexSelect - 1 >= 0) {
            groups.get(indexSelect).setSelect(false);
            notifyItemChanged(indexSelect);
            indexSelect--;
            groups.get(indexSelect).setSelect(true);
            notifyItemChanged(indexSelect);
            taskAdapter.setDate(groups.get(indexSelect).getTasks());
            groupUpdaterInterface.onGroupSelect();
        }
    }

    public String getActiveGroupName() {
        return groups.get(indexSelect).getName();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private ImageView selectIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.group_layout_name);
            selectIcon = itemView.findViewById(R.id.group_layout_select_icon);
        }

        public void bindInfo(int position) {
            GroupTask groupTask = groups.get(position);
            name.setText(groupTask.getName());
            selectIcon.setVisibility(groupTask.isSelect() ? View.VISIBLE : View.GONE);
            name.setTypeface(null, groupTask.isSelect() ? Typeface.BOLD : Typeface.NORMAL);
            if (groupTask.isSelect()) {
                taskAdapter.setDate(groups.get(indexSelect).getTasks());
            }
        }

        public void bindClick() {
            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        if (indexSelect != position) {
                            groups.get(indexSelect).setSelect(false);
                            notifyItemChanged(indexSelect);
                            groups.get(position).setSelect(true);
                            indexSelect = position;
                            notifyItemChanged(indexSelect);
                            taskAdapter.setDate(groups.get(indexSelect).getTasks());
                            groupUpdaterInterface.onGroupSelect();
                        }
                    }
                }
            });

            name.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    final int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        PopupMenu menu = new PopupMenu(view.getContext(), view);
                        menu.inflate(R.menu.group_task_popup_menu);
                        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                switch (menuItem.getItemId()) {
                                    case R.id.group_task_menu_delete:
                                        notifyItemRemoved(pos);
                                        deleteAll(pos);
                                        break;
                                    case R.id.group_task_menu_edit:
                                        rename(pos);
                                        break;
                                }
                                return true;
                            }
                        });
                        menu.show();
                    }
                    return false;
                }
            });
        }

    }

    private void deleteAll(int pos) {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        ArrayList<Task> tasks = groups.get(pos).getTasks();
        String userId = FirebaseAuth.getInstance().getUid();
        for (Task t : tasks) {
            dataBaseHelper.deleteTask(t, userId);
            AlarmHelper.cancelReminder(context, t.getName(), t.getInfo());
        }
        groups.remove(pos);
        if (indexSelect >= groups.size()) {
            indexSelect = groups.size() - 1;
        }
        groupUpdaterInterface.updateAdapter();
    }

    public interface GroupUpdaterInterface {
        void onGroupSelect();

        void updateAdapter();
    }
}
