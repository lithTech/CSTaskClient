package ru.cs.cstaskclient.fragments.discuss;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import ru.cs.cstaskclient.Const;
import ru.cs.cstaskclient.R;
import ru.cs.cstaskclient.dto.Discuss;
import ru.cs.cstaskclient.dto.GridQueryRequest;
import ru.cs.cstaskclient.dto.GridQueryResultDiscuss;
import ru.cs.cstaskclient.dto.GridQueryResultTask;
import ru.cs.cstaskclient.dto.GridSortDir;
import ru.cs.cstaskclient.dto.Task;
import ru.cs.cstaskclient.fragments.tasks.TaskListAdapter;
import ru.cs.cstaskclient.repository.ApiManager;
import ru.cs.cstaskclient.repository.DiscussApi;
import ru.cs.cstaskclient.util.ApiCall;
import ru.cs.cstaskclient.util.Callback;

import static ru.cs.cstaskclient.R.id.lvTasks;

/**
 * Created by lithTech on 09.12.2016.
 */

public class DiscussFragment extends Fragment implements AbsListView.OnScrollListener, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    ListView lvDiscuss;
    DiscussApi discussApi;
    GridQueryRequest listDiscussRequest;
    SwipeRefreshLayout srlLoading;
    private long taskId;
    private long msgId;
    private boolean stopRefreshOnScroll = false;

    View bSendMessage;
    EditText etSendMessage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_discuss, container, false);

        lvDiscuss = (ListView) view.findViewById(R.id.lvDiscuss);
        srlLoading = (SwipeRefreshLayout) view.findViewById(R.id.srlLoading);
        etSendMessage = (EditText) view.findViewById(R.id.edEnterMessage);
        bSendMessage = (View) view.findViewById(R.id.bSendMessage);

        bSendMessage.setOnClickListener(this);
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
        msgId = getArguments().getLong(Const.ARG_MSG_ID);
    }

    public void resetDiscussRequest() {
        listDiscussRequest = GridQueryRequest.getSimple(GridQueryRequest.class, "createdDate", GridSortDir.desc);
        listDiscussRequest.page = 1;
        listDiscussRequest.take = 15;
        listDiscussRequest.pageSize = 15;
        listDiscussRequest.skip = 0;
    }

    public void updateData(GridQueryRequest request, long taskId, final Callback callback) {
        Call<GridQueryResultDiscuss> discussCall = discussApi.getDiscuss(taskId, request);
        discussCall.enqueue(new ApiCall<GridQueryResultDiscuss>(getActivity()) {
            @Override
            public void onResponse(Response<GridQueryResultDiscuss> response) {
                callback.done(response.body().data);
            }

            @Override
            public void onFailure(Call<GridQueryResultDiscuss> call, Throwable t) {
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
                loadMoreTasks(null);
            }
        }
    }

    private void loadMoreTasks(@Nullable final Callback callback) {
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

                if (callback != null)
                    callback.done(discusses);
            }
        });
    }

    Callback updateToMsgId = new Callback() {
        @Override
        public void done(Object o) {
            List<Discuss> discusses = (List<Discuss>) o;
            if (discusses == null || discusses.isEmpty())
                return;
            for (Discuss discuss : discusses) {
                if (discuss.id == msgId) {
                    DiscussListAdapter adapter = ((DiscussListAdapter) lvDiscuss.getAdapter());
                    int pos = adapter.getPositionByMsgId(msgId);
                    if (pos >= 0) {
                        lvDiscuss.setSelection(pos);
                    }
                    return;
                }

            }
            loadMoreTasks(this);
        }
    };

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

                if (msgId > 0)
                    updateToMsgId.done(o);
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.bSendMessage) {
            if (etSendMessage.getText().length() > 0) {
                sendMessage(etSendMessage.getText().toString(), new Callback() {
                    @Override
                    public void done(Object o) {
                        etSendMessage.setText("");
                    }
                });
            }
        }
    }

    private void sendMessage(String s, final Callback callback) {
        RequestBody textBody = RequestBody.create(null, s.replaceAll("\n", "<br />"));
        discussApi.sendMessage(taskId, textBody).enqueue(new retrofit2.Callback<Discuss>() {
            @Override
            public void onResponse(Call<Discuss> call, Response<Discuss> response) {
                callback.done(response.body());
                DiscussListAdapter adapter = (DiscussListAdapter) lvDiscuss.getAdapter();
                adapter.insert(response.body(), 0);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Discuss> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
