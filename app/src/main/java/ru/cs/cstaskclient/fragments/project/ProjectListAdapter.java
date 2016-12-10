package ru.cs.cstaskclient.fragments.project;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import ru.cs.cstaskclient.R;
import ru.cs.cstaskclient.dto.Project;

/**
 * Created by lithTech on 07.12.2016.
 */

public class ProjectListAdapter extends ArrayAdapter<Project> {

    Context context;

    public ProjectListAdapter(Context context,List<Project> objects) {
        super(context, R.layout.list_project_item, objects);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_project_item,
                    parent, false);
        }
        Project project = getItem(position);
        TextView tvProjectTitle = (TextView) convertView.findViewById(R.id.tvProjectTitle);
        TextView tvProjectDesc = (TextView) convertView.findViewById(R.id.tvProjectDescription);
        TextView tvProjectAuthor = (TextView) convertView.findViewById(R.id.tvProjectAuthor);

        tvProjectTitle.setText(project.title);
        tvProjectDesc.setText(project.descr);
        tvProjectAuthor.setText(project.creatorFullName);

        return convertView;
    }
}
