package ru.cs.cstaskclient.fragments.category;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.cs.cstaskclient.Const;
import ru.cs.cstaskclient.MainActivity;
import ru.cs.cstaskclient.R;
import ru.cs.cstaskclient.dto.Category;
import ru.cs.cstaskclient.fragments.tasks.TaskFragment;
import ru.cs.cstaskclient.repository.ApiManager;
import ru.cs.cstaskclient.repository.CategoryApi;
import ru.cs.cstaskclient.repository.ProjectApi;
import ru.cs.cstaskclient.util.ApiCall;

/**
 * Created by lithTech on 12.12.2016.
 */

public class CategoryFragment extends Fragment implements AdapterView.OnItemClickListener {

    private CategoryApi categoryApi;
    private ProjectApi projectApi;
    private ListView lvCategories;
    private long projectId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_categories, container, false);

        categoryApi = ApiManager.getCategoryApi();
        projectApi = ApiManager.getProjectApi();

        lvCategories = (ListView) view.findViewById(R.id.lvCategories);

        lvCategories.setOnItemClickListener(this);

        updateCategoryData(projectId, projectId, new ru.cs.cstaskclient.util.Callback() {
            @Override
            public void done(Object o) {
                List<Category> categories = (List<Category>) o;
                lvCategories.setAdapter(new CategoryAdapter(getActivity(), categories));
            }
        });

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        projectId = getArguments().getLong(Const.ARG_PROJECT_ID);
    }

    public void updateCategoryData(long projectId, long catId, final ru.cs.cstaskclient.util.Callback callback) {
        Call<List<Category>> catCall = projectApi.getCategories(projectId);
        catCall.enqueue(new ApiCall<List<Category>>(getActivity()) {
            @Override
            public void onResponse(Response<List<Category>> response) {
                if (response.body() != null)
                    lvCategories.setAdapter(new CategoryAdapter(getActivity(), response.body()));
                callback.done(response.body());
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView == lvCategories) {
            Category category = (Category) lvCategories.getItemAtPosition(i);
            Fragment fragment = new TaskFragment();
            fragment.setArguments(new Bundle());
            fragment.getArguments().putLong(Const.ARG_TASK_CATEGORY_ID, category.id);
            ((MainActivity) getActivity()).loadFragment(category.title, fragment);
        }
    }
}
