package ru.cs.cstaskclient.fragments.worktime;

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
import ru.cs.cstaskclient.dto.WorkTime;
import ru.cs.cstaskclient.dto.WorkTimeElem;

/**
 * Created by lithTech on 07.12.2016.
 */

public class WorkTimeListAdapter extends ArrayAdapter<WorkTimeElem> {
    public WorkTimeListAdapter(Context context, List<WorkTimeElem> objects) {
        super(context, R.layout.list_work_time_item, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_work_time_item,
                    parent, false);

        WorkTimeElem wt = getItem(position);

        TextView tvTask = (TextView) convertView.findViewById(R.id.tvWorkTimeTask);
        TextView tvWork = (TextView) convertView.findViewById(R.id.tvWorkTimeWork);
        TextView tvTime = (TextView) convertView.findViewById(R.id.tvWorkTimeWorkTime);

        tvTask.setText(wt.link);
        if (!TextUtils.isEmpty(wt.workTimeDTO.workDescr)) {
            tvWork.setVisibility(View.VISIBLE);
            tvWork.setText(wt.workTimeDTO.workDescr);
        } else {
            tvWork.setVisibility(View.GONE);
        }
        tvTime.setText(String.valueOf(wt.workTimeDTO.workTime) + "ч");

        return convertView;
    }
}
