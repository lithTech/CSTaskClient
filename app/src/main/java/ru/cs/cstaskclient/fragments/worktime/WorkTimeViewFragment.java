package ru.cs.cstaskclient.fragments.worktime;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import ru.cs.cstaskclient.R;
import ru.cs.cstaskclient.dto.SessionUser;
import ru.cs.cstaskclient.dto.WorkTime;
import ru.cs.cstaskclient.dto.WorkTimeElem;
import ru.cs.cstaskclient.repository.ApiManager;
import ru.cs.cstaskclient.repository.WorkTimeApi;
import ru.cs.cstaskclient.util.ApiCall;

/**
 * Created by lithTech on 07.12.2016.
 */

public class WorkTimeViewFragment extends Fragment implements AdapterView.OnItemClickListener {

    ListView lvWorkTime;
    View lvTotalHourHeader = null;
    WorkTimeApi workTimeApi;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_worktime, container, false);

        workTimeApi = ApiManager.getWorkTimeApi();

        lvWorkTime = (ListView) view.findViewById(R.id.lvWorkTime);

        updateData();

        lvWorkTime.setOnItemClickListener(this);

        return view;
    }

    public void updateData() {
        Call<WorkTime> tasksCall = workTimeApi.getTasks();
        tasksCall.enqueue(new ApiCall<WorkTime>(getActivity()) {
            @Override
            public void onResponse(Response<WorkTime> response) {
                long sum = 0;
                for (WorkTimeElem wt : response.body().workTimes) {
                    sum += wt.workTimeDTO.workTime;
                }

                if (lvTotalHourHeader == null) {
                    lvTotalHourHeader = LayoutInflater.from(getActivity()).inflate(R.layout.list_work_time_header, null, false);
                    lvWorkTime.addHeaderView(lvTotalHourHeader);
                }
                TextView etWorkTimeSum = (TextView) lvTotalHourHeader.findViewById(R.id.tvWorkTimeWorkTimeSum);
                etWorkTimeSum.setText(sum + " ч.");

                lvWorkTime.setAdapter(new WorkTimeListAdapter(getActivity(), response.body().workTimes));
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        final WorkTimeElem wt = (WorkTimeElem) lvWorkTime.getItemAtPosition(i);
        if (wt == null) return;
        final Context context = getActivity();
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_worktime, null);
        final NumberPicker npHour = (NumberPicker) dialogView.findViewById(R.id.npHours);
        final EditText etDescr = (EditText) dialogView.findViewById(R.id.etDescr);

        npHour.setMaxValue(8);
        npHour.setMinValue(1);
        npHour.setValue(1);
        if (wt.workTimeDTO.workTime > 0)
            npHour.setValue((int) wt.workTimeDTO.workTime);
        etDescr.setText(wt.workTimeDTO.workDescr);

        AlertDialog.Builder ad = new AlertDialog.Builder(getActivity());
        final AlertDialog dialog = ad.setView(dialogView).setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //empty for older API
            }
        }).setTitle(R.string.dialog_worktime).create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(etDescr.getText().toString())) {
                    etDescr.setError(getString(R.string.error_field_required));
                    return;
                }
                saveWorkTime(etDescr.getText().toString(), npHour.getValue(), wt);
                dialog.dismiss();
            }
        });
    }

    private void saveWorkTime(String description, int hour, WorkTimeElem wt) {
        Call<ResponseBody> call = null;
        if (wt.workTimeDTO.id == 0)
            call = workTimeApi.createWorkTime(description,
                    hour, "HOUR", SessionUser.CURRENT.id, wt.workTimeDTO.taskId);
        else
            call = workTimeApi.updateWorkTime(description, hour, "HOUR", SessionUser.CURRENT.id,
                    wt.workTimeDTO.taskId, wt.workTimeDTO.id, wt.workTimeDTO.id);
        final Activity activity = getActivity();
        call.enqueue(new ApiCall<ResponseBody>(getActivity()) {
            @Override
            public void onResponse(Response<ResponseBody> response) {
                Toast.makeText(activity,
                        activity.getString(R.string.work_time_status_done),
                        Toast.LENGTH_LONG).show();
                updateData();
            }
        });
    }
}
