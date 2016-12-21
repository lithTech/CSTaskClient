package ru.cs.cstaskclient.fragments.lastactivity;

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
import ru.cs.cstaskclient.dto.GridQueryRequest;
import ru.cs.cstaskclient.dto.GridQueryResultLastActivity;
import ru.cs.cstaskclient.dto.GridSortDir;
import ru.cs.cstaskclient.dto.LastActivityMessage;
import ru.cs.cstaskclient.repository.ApiManager;
import ru.cs.cstaskclient.repository.LastActivityApi;
import ru.cs.cstaskclient.util.ApiCall;
import ru.cs.cstaskclient.util.Callback;

/**
 * Created by lithTech on 09.12.2016.
 */

public class LastActivityFragment extends Fragment implements AbsListView.OnScrollListener, SwipeRefreshLayout.OnRefreshListener{

    ListView lvActivity;
    LastActivityApi lastActivityApi;
    GridQueryRequest listActivityRequest;
    SwipeRefreshLayout srlLoading;
    private boolean stopRefreshOnScroll = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_last_activity, container, false);

        lvActivity = (ListView) view.findViewById(R.id.lvActivity);
        srlLoading = (SwipeRefreshLayout) view.findViewById(R.id.srlLoading);

        srlLoading.setOnRefreshListener(this);

        resetRequest();

        onRefresh();

        lvActivity.setOnScrollListener(this);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        lastActivityApi = ApiManager.getLastActivityApi();
    }

    public void resetRequest() {
        listActivityRequest = GridQueryRequest.getSimple(GridQueryRequest.class, "created", GridSortDir.desc);;
        listActivityRequest.page = 1;
        listActivityRequest.take = 15;
        listActivityRequest.pageSize = 15;
        listActivityRequest.skip = 0;
    }

    public void updateData(GridQueryRequest request, final Callback callback) {
        Call<GridQueryResultLastActivity> call = lastActivityApi.getLastActivity(request);
        call.enqueue(new ApiCall<GridQueryResultLastActivity>(getActivity()) {
            @Override
            public void onResponse(Response<GridQueryResultLastActivity> response) {
                callback.done(response.body().data);
            }

            @Override
            public void onFailure(Call<GridQueryResultLastActivity> call, Throwable t) {
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
        listActivityRequest.page++;
        listActivityRequest.skip += listActivityRequest.pageSize;
        updateData(listActivityRequest, new Callback() {
            @Override
            public void done(Object o) {
                List<LastActivityMessage> lastActivityMessages = (List<LastActivityMessage>) o;
                LastActivityListAdapter adapter = (LastActivityListAdapter) lvActivity.getAdapter();
                adapter.addAll(lastActivityMessages);
                adapter.notifyDataSetChanged();
                srlLoading.setRefreshing(false);

                stopRefreshOnScroll = lastActivityMessages.isEmpty();
            }
        });
    }

    @Override
    public void onRefresh() {
        resetRequest();
        stopRefreshOnScroll = false;
        srlLoading.setRefreshing(true);
        updateData(listActivityRequest, new Callback() {
            @Override
            public void done(Object o) {
                lvActivity.setAdapter(new LastActivityListAdapter(getActivity(), R.layout.list_last_activity_item,
                        (List<LastActivityMessage>) o));
                srlLoading.setRefreshing(false);
            }
        });
    }

}
