package ru.cs.cstaskclient.fragments.tasks;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import ru.cs.cstaskclient.R;
import ru.cs.cstaskclient.dto.Task;

/**
 * Created by lithTech on 08.12.2016.
 */

public class TaskListAdapter extends ArrayAdapter<Task> {
    public TaskListAdapter(Context context, List<Task> objects) {
        super(context, R.layout.list_task_item, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_task_item, parent, false);

        Task task = getItem(position);

        TextView tvTaskTitle = (TextView) convertView.findViewById(R.id.tvTaskTitle);
        tvTaskTitle.setText(task.title);

        TextView assignee = (TextView) convertView.findViewById(R.id.tvAssignee);
        assignee.setText(task.assigneeUserFullName);
        TextView chief = (TextView) convertView.findViewById(R.id.tvChief);
        chief.setText(task.creatorFullName);
        TextView tvStartDate = (TextView) convertView.findViewById(R.id.tvStartDate);
        tvStartDate.setText(task.startDate);
        TextView tvEndDate = (TextView) convertView.findViewById(R.id.tvEndDate);
        if (!TextUtils.isEmpty(task.endDate))
            tvEndDate.setText(task.endDate);
        else
            tvEndDate.setText(task.plannedEndDate);
        TextView planLabor = (TextView) convertView.findViewById(R.id.tvPlanLabor);
        planLabor.setText(task.plannedLabor);
        TextView tvStatus = (TextView) convertView.findViewById(R.id.tvStatus);
        tvStatus.setText(task.statusLocaleName);

        TextView tvVID = (TextView) convertView.findViewById(R.id.tvVID);
        tvVID.setText(task.vid);

        tvEndDate.setVisibility(tvEndDate.getText().length() == 0 ? View.INVISIBLE : View.VISIBLE);

        tvStartDate.setVisibility(tvStartDate.getText().length() == 0? View.INVISIBLE : View.VISIBLE);

        return convertView;
    }
}
