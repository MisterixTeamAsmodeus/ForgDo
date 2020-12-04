package com.itschool.buzuverov.forgdo.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.itschool.buzuverov.forgdo.Model.Tasks.FilterTask;
import com.itschool.buzuverov.forgdo.Model.Tasks.Task;
import com.itschool.buzuverov.forgdo.R;

import java.util.ArrayList;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.ViewHolder> {

    private ArrayList<FilterTask> filters;
    private static int indexSelect = 0;
    private LayoutInflater inflater;
    private TaskAdapter taskAdapter;
    private FilterInterface filterInterface;

    public FilterAdapter(Context context, ArrayList<FilterTask> filters, TaskAdapter.UpdateListener listener, FilterInterface filterInterface) {
        this.filterInterface = filterInterface;
        this.inflater = LayoutInflater.from(context);
        this.taskAdapter = new TaskAdapter(context, listener);
        this.filters = filters;
        filters.get(indexSelect).setSelect(true);
    }

    public void setData(ArrayList<Task> tasks) {
        taskAdapter.setDate(tasks);
    }

    public static void setDefaultIndexSelect() {
        FilterAdapter.indexSelect = 0;
    }

    public int getIndexSelect() {
        return indexSelect;
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
        return filters.size();
    }

    public void nextFilter() {
        if (indexSelect + 1 < getItemCount()){
            filters.get(indexSelect).setSelect(false);
            notifyItemChanged(indexSelect);
            indexSelect++;
            filters.get(indexSelect).setSelect(true);
            notifyItemChanged(indexSelect);
            taskAdapter.setFilterTask(filters.get(indexSelect).getFilter());
            filterInterface.onFilterChanged();
        }
    }

    public void previousFilter() {
        if (indexSelect - 1 >= 0){
            filters.get(indexSelect).setSelect(false);
            notifyItemChanged(indexSelect);
            indexSelect--;
            filters.get(indexSelect).setSelect(true);
            notifyItemChanged(indexSelect);
            taskAdapter.setFilterTask(filters.get(indexSelect).getFilter());
            filterInterface.onFilterChanged();
        }
    }

    public interface FilterInterface {
        void onFilterChanged();
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
            FilterTask filter = filters.get(position);
            name.setText(filter.getTextGroup());
            selectIcon.setVisibility(filter.isSelect() ? View.VISIBLE : View.GONE);
            name.setTypeface(null, filter.isSelect() ? Typeface.BOLD : Typeface.NORMAL);
        }

        public void bindClick() {
            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && indexSelect != position) {
                        filters.get(indexSelect).setSelect(false);
                        notifyItemChanged(indexSelect);
                        filters.get(position).setSelect(true);
                        indexSelect = position;
                        notifyItemChanged(indexSelect);
                        taskAdapter.setFilterTask(filters.get(indexSelect).getFilter());
                        filterInterface.onFilterChanged();
                    }
                }
            });
        }
    }
}