package ru.cs.cstaskclient.fragments.discuss;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import ru.cs.cstaskclient.Const;
import ru.cs.cstaskclient.R;
import ru.cs.cstaskclient.dto.Discuss;
import ru.cs.cstaskclient.dto.GridQueryRequest;
import ru.cs.cstaskclient.dto.GridQueryResultDiscuss;
import ru.cs.cstaskclient.dto.GridQueryResultTask;
import ru.cs.cstaskclient.dto.Task;
import ru.cs.cstaskclient.fragments.tasks.TaskListAdapter;
import ru.cs.cstaskclient.repository.ApiManager;
import ru.cs.cstaskclient.repository.DiscussApi;
import ru.cs.cstaskclient.util.Callback;

import static ru.cs.cstaskclient.R.id.lvTasks;

/**
 * Created by lithTech on 09.12.2016.
 */

public class DiscussFragment extends Fragment implements AbsListView.OnScrollListener, SwipeRefreshLayout.OnRefreshListener {

    ListView lvDiscuss;
    DiscussApi discussApi;
    GridQueryRequest listDiscussRequest;
    SwipeRefreshLayout srlLoading;
    private long taskId;
    private boolean stopRefreshOnScroll = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_discuss, container, false);

        lvDiscuss = (ListView) view.findViewById(R.id.lvDiscuss);

        srlLoading = (SwipeRefreshLayout) view.findViewById(R.id.srlLoading);

        srlLoading.setOnRefreshListener(this);

        resetDiscussRequest();

        onRefresh();

        lvDiscuss.setOnScrollListener(this);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        discussApi = ApiManager.getDiscussApi();
        taskId = getArguments().getLong(Const.ARG_TASK_ID);
    }

    public void resetDiscussRequest() {
        listDiscussRequest = new GridQueryRequest();
        listDiscussRequest.page = 1;
        listDiscussRequest.take = 15;
        listDiscussRequest.pageSize = 15;
        listDiscussRequest.skip = 0;
    }

    public void updateData(GridQueryRequest request, long taskId, final Callback callback) {
        Call<GridQueryResultDiscuss> discussCall = discussApi.getDiscuss(taskId, request);
        discussCall.enqueue(new retrofit2.Callback<GridQueryResultDiscuss>() {
            @Override
            public void onResponse(Call<GridQueryResultDiscuss> call, Response<GridQueryResultDiscuss> response) {
                callback.done(response.body().data);
            }

            @Override
            public void onFailure(Call<GridQueryResultDiscuss> call, Throwable t) {
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
        listDiscussRequest.page++;
        listDiscussRequest.skip += listDiscussRequest.pageSize;
        updateData(listDiscussRequest, taskId, new ru.cs.cstaskclient.util.Callback() {
            @Override
            public void done(Object o) {
                List<Discuss> discusses = (List<Discuss>) o;
                DiscussListAdapter discussListAdapter = (DiscussListAdapter) lvDiscuss.getAdapter();
                discussListAdapter.addAll(discusses);
                discussListAdapter.notifyDataSetChanged();
                srlLoading.setRefreshing(false);

                stopRefreshOnScroll = discusses.isEmpty();
            }
        });
    }

    @Override
    public void onRefresh() {
        resetDiscussRequest();
        stopRefreshOnScroll = false;
        updateData(listDiscussRequest, taskId, new Callback() {
            @Override
            public void done(Object o) {
                lvDiscuss.setAdapter(new DiscussListAdapter(getActivity(), R.layout.list_discuss_item,
                        (List<Discuss>) o));
                srlLoading.setRefreshing(false);
            }
        });
    }
}
