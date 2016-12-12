package ru.cs.cstaskclient.fragments.tasks;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.cs.cstaskclient.Const;
import ru.cs.cstaskclient.MainActivity;
import ru.cs.cstaskclient.R;
import ru.cs.cstaskclient.dto.GridQueryRequestTask;
import ru.cs.cstaskclient.dto.GridQueryResultTask;
import ru.cs.cstaskclient.dto.Task;
import ru.cs.cstaskclient.fragments.discuss.DiscussFragment;
import ru.cs.cstaskclient.repository.ApiManager;
import ru.cs.cstaskclient.repository.TaskApi;

/**
 * Created by lithTech on 08.12.2016.
 */

public class TaskFragment  extends Fragment implements AbsListView.OnScrollListener, SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener {

    TaskApi taskApi;
    long categoryId;

    GridQueryRequestTask listRequestCurrent;
    ListView lvTasks;
    android.support.v4.widget.SwipeRefreshLayout srlLoading;
    boolean stopRefreshOnScroll = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_tasklist, container, false);

        taskApi = ApiManager.getTaskApi();

        lvTasks = (ListView) view.findViewById(R.id.lvTasks);

        srlLoading = (SwipeRefreshLayout) view.findViewById(R.id.srlLoading);

        onRefresh();

        lvTasks.setOnScrollListener(this);

        lvTasks.setOnItemClickListener(this);

        srlLoading.setOnRefreshListener(this);

        stopRefreshOnScroll = false;

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listRequestCurrent = new GridQueryRequestTask();
        categoryId = getArguments().getLong(Const.ARG_TASK_CATEGORY_ID, 0);
    }

    public void resetListRequest() {
        listRequestCurrent.parentId = categoryId;
        listRequestCurrent.page = 1;
        listRequestCurrent.pageSize = 15;
        listRequestCurrent.skip = 0;
        listRequestCurrent.take = 15;
    }

    public void updateData(GridQueryRequestTask request,
                           final ru.cs.cstaskclient.util.Callback callback) {
        srlLoading.setRefreshing(true);
        Call<GridQueryResultTask> tasksCall = taskApi.getTasks(request);
        tasksCall.enqueue(new Callback<GridQueryResultTask>() {
            @Override
            public void onResponse(Call<GridQueryResultTask> call, Response<GridQueryResultTask> response) {
                srlLoading.setRefreshing(false);
                callback.done(response.body().data);
            }

            @Override
            public void onFailure(Call<GridQueryResultTask> call, Throwable t) {
                srlLoading.setRefreshing(false);
                t.printStackTrace();
                Toast.makeText(getActivity(), R.string.error_network, Toast.LENGTH_LONG);
            }
        });
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        if (stopRefreshOnScroll) return;
        if(firstVisibleItem+visibleItemCount == totalItemCount && totalItemCount!=0)
        {
            if(srlLoading.isRefreshing() == false)
            {
                srlLoading.setRefreshing(true);
                loadMoreTasks();
            }
        }
    }

    private void loadMoreTasks() {
        listRequestCurrent.page++;
        listRequestCurrent.skip += listRequestCurrent.pageSize;
        updateData(listRequestCurrent, new ru.cs.cstaskclient.util.Callback() {
            @Override
            public void done(Object o) {
                List<Task> newItems = (List<Task>) o;
                TaskListAdapter taskListAdapter = (TaskListAdapter) lvTasks.getAdapter();
                taskListAdapter.addAll(newItems);
                taskListAdapter.notifyDataSetChanged();

                stopRefreshOnScroll = newItems.isEmpty();
            }
        });
    }

    @Override
    public void onRefresh() {
        resetListRequest();
        stopRefreshOnScroll = false;
        updateData(listRequestCurrent, new ru.cs.cstaskclient.util.Callback() {
            @Override
            public void done(Object o) {
                srlLoading.setRefreshing(false);
                List<Task> taskList = (List<Task>) o;
                lvTasks.setAdapter(new TaskListAdapter(getActivity(), taskList));
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView == lvTasks) {
            Task task = (Task) lvTasks.getItemAtPosition(i);
            DiscussFragment fragment = new DiscussFragment();
            fragment.setArguments(new Bundle());
            fragment.getArguments().putLong(Const.ARG_TASK_ID, task.id);

            ((MainActivity) getActivity()).loadFragment(task.title, fragment);
        }
    }
}
