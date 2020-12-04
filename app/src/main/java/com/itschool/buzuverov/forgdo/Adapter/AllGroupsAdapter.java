package com.itschool.buzuverov.forgdo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.itschool.buzuverov.forgdo.Dialog.DialogCreateGroup;
import com.itschool.buzuverov.forgdo.Model.Tasks.GroupTask;
import com.itschool.buzuverov.forgdo.R;

public class AllGroupsAdapter extends RecyclerView.Adapter<AllGroupsAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private GroupTask[] allGroup;
    private onSelect select;
    private Context context;

    public interface onSelect {
        void select(String name);
    }

    public AllGroupsAdapter(Context context, onSelect select) {
        this.inflater = LayoutInflater.from(context);
        this.select = select;
        this.context = context;
    }

    public void setData(GroupTask[] groups) {
        allGroup = groups;
    }

    @NonNull
    @Override
    public AllGroupsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_all_group, parent, false);
        return new AllGroupsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllGroupsAdapter.ViewHolder holder, int position) {
        position = holder.getAdapterPosition();
        holder.bindInfo(position);
        holder.bindClick();
    }

    @Override
    public int getItemCount() {
        return allGroup.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;

        ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.all_groups_name);
        }

        private void bindInfo(int position) {
            name.setText(allGroup[position].getName());
        }

        private void bindClick() {
            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        if (context.getString(R.string.create_new_group).equals(allGroup[pos].getName())) {
                            DialogCreateGroup createTask = new DialogCreateGroup(new DialogCreateGroup.DialogCreateGroupListener() {
                                @Override
                                public void create(String name) {
                                    select.select(name);
                                }
                            }, context);
                            createTask.show(((AppCompatActivity) context).getSupportFragmentManager(), "tag");
                        } else if (context.getString(R.string.delete_for_group).equals(allGroup[pos].getName())) {
                            select.select("null");
                        } else {
                            select.select(allGroup[pos].getName());
                        }
                    }
                }
            });
        }
    }
}