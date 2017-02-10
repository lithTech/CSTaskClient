package ru.cs.cstaskclient.fragments.tasks;

import android.widget.AbsListView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import ru.cs.cstaskclient.dto.GridQueryRequestTask;
import ru.cs.cstaskclient.dto.GridQueryResultTask;
import ru.cs.cstaskclient.dto.Task;
import ru.cs.cstaskclient.util.ApiCall;
import ru.cs.cstaskclient.util.Callback;

/**
 * Created by lithTech on 08.02.2017.
 */

public class TaskFavoritesFragment extends TaskFragment {

    @Override
    public void updateData(GridQueryRequestTask request, final Callback callback) {
        srlLoading.setRefreshing(true);
        Call<List<Task>> tasksCall = taskApi.getFavoriteTasks();
        tasksCall.enqueue(new ApiCall<List<Task>>(getActivity()) {
            @Override
            public void onResponse(Response<List<Task>> response) {
                srlLoading.setRefreshing(false);
                callback.done(response.body());
            }

            @Override
            public void onFailure(Call<List<Task>> call, Throwable t) {
                super.onFailure(call, t);
                srlLoading.setRefreshing(false);
            }
        });
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        //do nothing
    }
}
