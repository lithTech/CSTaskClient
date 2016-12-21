package ru.cs.cstaskclient.fragments.assigned;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.cs.cstaskclient.Const;
import ru.cs.cstaskclient.MainActivity;
import ru.cs.cstaskclient.R;
import ru.cs.cstaskclient.dto.GridQueryRequest;
import ru.cs.cstaskclient.dto.GridQueryRequestAssignedTask;
import ru.cs.cstaskclient.dto.GridQueryRequestTask;
import ru.cs.cstaskclient.dto.GridQueryResultTask;
import ru.cs.cstaskclient.dto.GridSort;
import ru.cs.cstaskclient.dto.GridSortDir;
import ru.cs.cstaskclient.dto.Project;
import ru.cs.cstaskclient.dto.Tag;
import ru.cs.cstaskclient.dto.Task;
import ru.cs.cstaskclient.dto.TaskStatus;
import ru.cs.cstaskclient.dto.User;
import ru.cs.cstaskclient.fragments.discuss.DiscussFragment;
import ru.cs.cstaskclient.fragments.tasks.TaskListAdapter;
import ru.cs.cstaskclient.repository.ApiManager;
import ru.cs.cstaskclient.repository.TaskApi;
import ru.cs.cstaskclient.repository.UserApi;
import ru.cs.cstaskclient.util.ApiCall;
import ru.cs.cstaskclient.widget.InstantAutoComplete;

/**
 * Created by lithTech on 08.12.2016.
 */

public class AssignedTaskFragment extends Fragment implements AbsListView.OnScrollListener, SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener {

    TaskApi taskApi;

    GridQueryRequestAssignedTask listRequestCurrent;
    ListView lvTasks;
    InstantAutoComplete acAssignee;
    SwipeRefreshLayout srlLoading;
    boolean stopRefreshOnScroll = false;
    private UserApi userApi;
    Map<String, Long> userMap = new HashMap<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_assigned_tasks, container, false);

        taskApi = ApiManager.getTaskApi();
        userApi = ApiManager.getUserApi();

        lvTasks = (ListView) view.findViewById(R.id.lvTasks);

        srlLoading = (SwipeRefreshLayout) view.findViewById(R.id.srlLoading);

        acAssignee = (InstantAutoComplete) view.findViewById(R.id.etAssignee);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            acAssignee.setShowSoftInputOnFocus(false);
        }
        acAssignee.setOnItemClickListener(this);
        acAssignee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acAssignee.showDropDown();
            }
        });

        updateReferences();

        resetListRequest();

        onRefresh();

        lvTasks.setOnScrollListener(this);

        lvTasks.setOnItemClickListener(this);

        srlLoading.setOnRefreshListener(this);

        stopRefreshOnScroll = false;

        return view;
    }

    private void updateReferences() {
        Call<List<User>> userCall = userApi.getUsersReference();
        userCall.enqueue(new ApiCall<List<User>>(getActivity()) {
            @Override
            public void onResponse(Response<List<User>> response) {
                String[] users = new String[response.body().size()];
                List<User> body = response.body();
                for (int i = 0; i < body.size(); i++) {
                    User user = body.get(i);
                    users[i] = user.fullName;

                    userMap.put(user.fullName, user.id);
                }
                acAssignee.setAdapter(new ArrayAdapter<String>(activity,
                        android.R.layout.simple_dropdown_item_1line,
                        users));
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void resetListRequest() {
        listRequestCurrent = GridQueryRequest.getSimple(GridQueryRequestAssignedTask.class, "statusLocaleName",
                GridSortDir.asc);
        listRequestCurrent.sort.add(new GridSort("modifiedDate", GridSortDir.desc));
        listRequestCurrent.setUserId(null);
        listRequestCurrent.page = 1;
        listRequestCurrent.pageSize = 100;
        listRequestCurrent.skip = 0;
        listRequestCurrent.take = 100;
    }

    public void updateData(GridQueryRequestAssignedTask request,
                           final ru.cs.cstaskclient.util.Callback callback) {
        srlLoading.setRefreshing(true);
        Call<GridQueryResultTask> tasksCall = taskApi.getAssignedTasks(request);
        tasksCall.enqueue(new ApiCall<GridQueryResultTask>(getActivity()) {
            @Override
            public void onResponse(Response<GridQueryResultTask> r) {
                srlLoading.setRefreshing(false);
                callback.done(r.body().data);
            }

            @Override
            public void onFailure(Call<GridQueryResultTask> call, Throwable t) {
                super.onFailure(call, t);
                srlLoading.setRefreshing(false);
            }
        });
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
//        if (stopRefreshOnScroll) return;
//        if(firstVisibleItem+visibleItemCount == totalItemCount && totalItemCount!=0)
//        {
//            if(srlLoading.isRefreshing() == false)
//            {
//                srlLoading.setRefreshing(true);
//            }
//        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView == lvTasks) {
            Task task = (Task) lvTasks.getItemAtPosition(i);
            DiscussFragment fragment = new DiscussFragment();
            fragment.setArguments(new Bundle());
            fragment.getArguments().putLong(Const.ARG_TASK_ID, task.id);

            ((MainActivity) getActivity()).loadFragment("#"+task.vid+" "+task.title, fragment);
        }
        else{
            String userName = (String) acAssignee.getAdapter().getItem(i);
            Long id = userMap.get(userName);
            listRequestCurrent.setUserId(id);
            onRefresh();
        }
    }

    @Override
    public void onRefresh() {
        updateData(listRequestCurrent, new ru.cs.cstaskclient.util.Callback() {
            @Override
            public void done(Object o) {
                lvTasks.setAdapter(new TaskListAdapter(getActivity(), (List<Task>) o));
            }
        });
    }
}
