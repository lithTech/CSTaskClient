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
import ru.cs.cstaskclient.fragments.category.CategoryAdapter;
import ru.cs.cstaskclient.fragments.category.CategoryFragment;
import ru.cs.cstaskclient.fragments.tasks.TaskFragment;
import ru.cs.cstaskclient.repository.ApiManager;
import ru.cs.cstaskclient.repository.CategoryApi;
import ru.cs.cstaskclient.repository.ProjectApi;
import ru.cs.cstaskclient.util.ApiCall;

/**
 * Created by lithTech on 06.12.2016.
 */

public class ProjectViewFragment extends Fragment implements AdapterView.OnItemClickListener {

    ListView lvProjects;
    ProjectApi projectApi;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_fragment_projects, container, false);

        projectApi = ApiManager.getProjectApi();

        lvProjects = (ListView) v.findViewById(R.id.lvProjects);
        updateProjectData(lvProjects);

        lvProjects.setOnItemClickListener(this);

        return v;
    }

    public void updateProjectData(final ListView listView) {
        Call<List<Project>> projects = projectApi.getProjects("progress");
        projects.enqueue(new ApiCall<List<Project>>(getActivity()) {
            @Override
            public void onResponse(Response<List<Project>> response) {
                Collections.sort(response.body(), new Comparator<Project>() {
                    @Override
                    public int compare(Project t1, Project t2) {
                        return t1.title.compareTo(t2.title);
                    }
                });
                listView.setAdapter(new ProjectListAdapter(getActivity(), response.body()));
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView == lvProjects) {
            Project project = ((Project) lvProjects.getItemAtPosition(i));

            Fragment fragment = new CategoryFragment();
            fragment.setArguments(new Bundle());
            fragment.getArguments().putLong(Const.ARG_PROJECT_ID, project.id);
            ((MainActivity) getActivity()).loadFragment(project.title, fragment);

        }
    }


}
