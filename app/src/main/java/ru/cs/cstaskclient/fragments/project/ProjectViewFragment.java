package ru.cs.cstaskclient.fragments.project;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.cs.cstaskclient.Const;
import ru.cs.cstaskclient.MainActivity;
import ru.cs.cstaskclient.R;
import ru.cs.cstaskclient.dto.Category;
import ru.cs.cstaskclient.dto.Project;
import ru.cs.cstaskclient.fragments.tasks.TaskFragment;
import ru.cs.cstaskclient.repository.ApiManager;
import ru.cs.cstaskclient.repository.CategoryApi;
import ru.cs.cstaskclient.repository.ProjectApi;

/**
 * Created by lithTech on 06.12.2016.
 */

public class ProjectViewFragment extends Fragment implements AdapterView.OnItemClickListener {

    ListView lvProjects;
    ListView lvCategories;
    ProjectApi projectApi;
    CategoryApi categoryApi;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_fragment_projects, container, false);

        projectApi = ApiManager.getProjectApi();
        categoryApi = ApiManager.getCategoryApi();

        lvProjects = (ListView) v.findViewById(R.id.lvProjects);
        lvCategories = (ListView) v.findViewById(R.id.lvCategories);
        updateProjectData(lvProjects);

        lvProjects.setOnItemClickListener(this);
        lvCategories.setOnItemClickListener(this);

        return v;
    }

    public void updateCategoryData(long projectId, long catId, final ListView listView, final ru.cs.cstaskclient.util.Callback callback) {
        Call<List<Category>> categories = categoryApi.getCategory(projectId, catId);
        categories.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                listView.setAdapter(new CategoryAdapter(getActivity(), response.body()));
                callback.done(response.body());
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getActivity(), R.string.error_network, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void updateProjectData(final ListView listView) {
        Call<List<Project>> projects = projectApi.getProjects("progress");
        projects.enqueue(new Callback<List<Project>>() {
            @Override
            public void onResponse(Call<List<Project>> call, Response<List<Project>> response) {
                Collections.sort(response.body(), new Comparator<Project>() {
                    @Override
                    public int compare(Project t1, Project t2) {
                        return t1.title.compareTo(t2.title);
                    }
                });
                listView.setAdapter(new ProjectListAdapter(getActivity(), response.body()));
            }

            @Override
            public void onFailure(Call<List<Project>> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getActivity(), R.string.error_network, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView == lvProjects) {
            Project project = ((Project) lvProjects.getItemAtPosition(i));
            updateCategoryData(project.id, project.id, lvCategories, new ru.cs.cstaskclient.util.Callback() {
                @Override
                public void done(Object o) {
                    lvProjects.setVisibility(View.GONE);
                    lvCategories.setVisibility(View.VISIBLE);
                }
            });
        } else if (adapterView == lvCategories) {
            Category category = (Category) lvCategories.getItemAtPosition(i);
            Fragment fragment = new TaskFragment();
            fragment.setArguments(new Bundle());
            fragment.getArguments().putLong(Const.ARG_TASK_CATEGORY_ID, category.id);
            ((MainActivity) getActivity()).loadFragment(category.title, fragment);
        }
    }


}
