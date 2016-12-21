package ru.cs.cstaskclient;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;
import ru.cs.cstaskclient.dto.Category;
import ru.cs.cstaskclient.dto.Project;
import ru.cs.cstaskclient.dto.SessionUser;
import ru.cs.cstaskclient.dto.Tag;
import ru.cs.cstaskclient.dto.Task;
import ru.cs.cstaskclient.dto.TaskCreateRequest;
import ru.cs.cstaskclient.dto.TaskStatus;
import ru.cs.cstaskclient.dto.User;
import ru.cs.cstaskclient.fragments.tasks.TaskFragment;
import ru.cs.cstaskclient.repository.ApiManager;
import ru.cs.cstaskclient.repository.ProjectApi;
import ru.cs.cstaskclient.repository.TaskApi;
import ru.cs.cstaskclient.repository.UserApi;
import ru.cs.cstaskclient.util.ApiCall;
import ru.cs.cstaskclient.util.Callback;
import ru.cs.cstaskclient.widget.EditTextDatePicker;

/**
 * Created by lithTech on 13.12.2016.
 */

public class TaskCreateActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    Toolbar toolbar;

    TaskApi taskApi;
    ProjectApi projectApi;
    UserApi userApi;

    private AutoCompleteTextView tvStatuses;
    private AutoCompleteTextView tvProjects;
    private MultiAutoCompleteTextView tvTags;
    private AutoCompleteTextView tvAssignee;
    private AutoCompleteTextView tvCategory;
    private EditTextDatePicker etStartDate;
    private EditTextDatePicker etPlannedDate;
    private EditText tvTitle;
    private EditText tvDescr;
    private EditText tvPlannedLabor;
    private Button bCreate;

    Map<String, Long> projectsMap = new HashMap<>();
    Map<String, Long> categoryMap = new HashMap<>();
    Map<String, String> userMap = new HashMap<>();
    Map<String, String> statusMap = new HashMap<>();
    Map<String, Long> tagsMap = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_dialog_task);

        Toolbar toolbar = (Toolbar) findViewById(R.id.appBar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvStatuses = (AutoCompleteTextView) findViewById(R.id.etStatus);
        tvProjects = (AutoCompleteTextView) findViewById(R.id.etProject);
        tvTags = (MultiAutoCompleteTextView) findViewById(R.id.etTags);
        tvTags.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        tvCategory = (AutoCompleteTextView) findViewById(R.id.etCategory);
        tvAssignee = (AutoCompleteTextView) findViewById(R.id.etAssignee);
        tvDescr = (EditText) findViewById(R.id.etDescr);
        tvPlannedLabor = (EditText) findViewById(R.id.etPlannedLabor);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tvTags.setShowSoftInputOnFocus(false);
            tvStatuses.setShowSoftInputOnFocus(false);
            tvProjects.setShowSoftInputOnFocus(false);
            tvAssignee.setShowSoftInputOnFocus(false);
            tvCategory.setShowSoftInputOnFocus(false);
        }

        tvProjects.setOnItemClickListener(this);

        taskApi = ApiManager.getTaskApi();
        projectApi = ApiManager.getProjectApi();
        userApi = ApiManager.getUserApi();

        etStartDate = new EditTextDatePicker(this, (EditText) findViewById(R.id.etStartDate));
        etPlannedDate = new EditTextDatePicker(this, (EditText) findViewById(R.id.etPlannedEndDate));
        tvTitle = (EditText) findViewById(R.id.etTitle);

        etStartDate.getEditText().setText(new SimpleDateFormat("dd.MM.yyyy").format(new Date()));

        bCreate = (Button) findViewById(R.id.bCreate);
        bCreate.setOnClickListener(this);

        updateReferences();
    }

    private void updateReferences() {
        Call<List<TaskStatus>> statusesCall = taskApi.getTaskStatuses();
        statusesCall.enqueue(new ApiCall<List<TaskStatus>>(this) {
             @Override
             public void onResponse(Response<List<TaskStatus>> response) {
                 String[] statuses = new String[response.body().size()];
                 List<TaskStatus> body = response.body();
                 for (int i = 0; i < body.size(); i++) {
                     TaskStatus status = body.get(i);
                     statuses[i] = status.localeName;

                     statusMap.put(status.localeName, status.name);
                 }
                 tvStatuses.setAdapter(new ArrayAdapter<String>(activity,
                         android.R.layout.simple_dropdown_item_1line,
                         statuses));
             }
        });

        Call<List<Tag>> tagCall = taskApi.getTags();
        tagCall.enqueue(new ApiCall<List<Tag>>(this) {
            @Override
            public void onResponse(Response<List<Tag>> response) {
                String[] tags = new String[response.body().size()];
                List<Tag> body = response.body();
                for (int i = 0; i < body.size(); i++) {
                    Tag tag = body.get(i);
                    tags[i] = tag.name;

                    tagsMap.put(tag.name, tag.id);
                }
                tvTags.setAdapter(new ArrayAdapter<String>(activity,
                        android.R.layout.simple_dropdown_item_1line,
                        tags));
            }
        });

        Call<List<Project>> projectCall = projectApi.getProjects("progress");
        projectCall.enqueue(new ApiCall<List<Project>>(this) {
            @Override
            public void onResponse(Response<List<Project>> response) {
                String[] projects = new String[response.body().size()];
                List<Project> body = response.body();
                for (int i = 0; i < body.size(); i++) {
                    Project project = body.get(i);
                    projects[i] = project.title;

                    projectsMap.put(project.title, project.id);
                }
                tvProjects.setAdapter(new ArrayAdapter<String>(activity,
                        android.R.layout.simple_dropdown_item_1line, projects));
            }
        });

        Call<List<User>> userCall = userApi.getUsersReference();
        userCall.enqueue(new ApiCall<List<User>>(this) {
            @Override
            public void onResponse(Response<List<User>> response) {
                String[] users = new String[response.body().size()];
                List<User> body = response.body();
                for (int i = 0; i < body.size(); i++) {
                    User user = body.get(i);
                    users[i] = user.fullName;

                    userMap.put(user.fullName, user.login);
                }
                tvAssignee.setAdapter(new ArrayAdapter<String>(activity,
                        android.R.layout.simple_dropdown_item_1line,
                        users));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String project = (String) adapterView.getItemAtPosition(i);
        Long id = projectsMap.get(project);
        if (id != null) {
            Call<List<Category>> catCall = projectApi.getCategories(id);
            catCall.enqueue(new ApiCall<List<Category>>(this) {
                @Override
                public void onResponse(Response<List<Category>> response) {
                    String[] cat = new String[response.body().size()];
                    List<Category> body = response.body();
                    for (int i = 0; i < body.size(); i++) {
                        Category c = body.get(i);
                        cat[i] = c.title;
                        categoryMap.put(c.title, c.id);
                    }
                    tvCategory.setAdapter(new ArrayAdapter<String>(activity,
                            android.R.layout.simple_dropdown_item_1line,
                            cat));
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        if (validate()) {
            final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress);
            bCreate.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);

            Map<String, Object> req = new HashMap<>();
            req.put("assigneeUserLogin", userMap.get(tvAssignee.getText().toString()));
            req.put("categoryId", categoryMap.get(tvCategory.getText().toString()));
            req.put("projectId", projectsMap.get(tvProjects.getText().toString()));
            req.put("creatorLogin", SessionUser.CURRENT.login);
            req.put("descr", tvDescr.getText().toString());
            req.put("plannedEndDate", etPlannedDate.getEditText().getText().toString());
            req.put("plannedLabor", tvPlannedLabor.getText().toString());
            req.put("startDate", etStartDate.getEditText().getText().toString());
            req.put("status", statusMap.get(tvStatuses.getText().toString()));
            req.put("title", tvTitle.getText().toString());

            String tagStr = tvTags.getText().toString();
            List<Long> tagsList = new ArrayList<>();
            if (!TextUtils.isEmpty(tagStr)) {
                String[] tags = tagStr.split(",");
                for (int i = 0; i < tags.length; i++) {
                    if (tagsMap.containsKey(tags[i].trim()))
                        tagsList.add(tagsMap.get(tags[i].trim()));
                }
            }

            Call<Task> crCall = taskApi.create(req, tagsList);
            crCall.enqueue(new ApiCall<Task>(this) {
                @Override
                public void onResponse(Response<Task> r) {
                    progressBar.setVisibility(View.GONE);
                    bCreate.setVisibility(View.VISIBLE);

                    Toast.makeText(activity,
                            activity.getString(R.string.alert_task_created)+" #" +
                                    r.body().vid, Toast.LENGTH_LONG).show();

                    showOpenTaskDialog(r.body());
                }

                @Override
                public void onFailure(Call<Task> call, Throwable t) {
                    super.onFailure(call, t);
                    progressBar.setVisibility(View.GONE);
                    bCreate.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    private void showOpenTaskDialog(final Task task) {
        final Activity thisAct = this;
        AlertDialog ad = new AlertDialog.Builder(this)
                .setCancelable(true)
                .setView(R.layout.dialog_task_open)
                .setPositiveButton(R.string.openNewTask, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent();
                        intent.putExtra(Const.ARG_TASK_ID, task.id);
                        intent.putExtra(Const.ARG_TASK_TITLE, "#" + task.vid + " " + task.title);
                        setResult(Const.RESULT_TASK_CREATED, intent);
                        thisAct.finish();
                    }
                })
                .create();
        ad.show();
    }

    private boolean validate() {
        boolean r = true;
        String error = getString(R.string.error_field_required);
        if (TextUtils.isEmpty(tvTitle.getText().toString())) {
            tvTitle.setError(error);
            r = false;
        }
        if (!categoryMap.containsKey(tvCategory.getText().toString())) {
            tvCategory.setError(error);
            r = false;
        }
        if (!projectsMap.containsKey(tvProjects.getText().toString())) {
            tvProjects.setError(error);
            r = false;
        }
        if (!statusMap.containsKey(tvStatuses.getText().toString())) {
            tvStatuses.setError(error);
            r = false;
        }
        else{
            if (statusMap.get(tvStatuses.getText().toString()) != null &&
                    statusMap.get(tvStatuses.getText().toString()).equalsIgnoreCase("2processed")) {
                if (TextUtils.isEmpty(etPlannedDate.getEditText().getText().toString())) {
                    etPlannedDate.getEditText().setError(error);
                    r = false;
                }
            }
        }
        if (!userMap.containsKey(tvAssignee.getText().toString())) {
            tvAssignee.setError(error);
            r = false;
        }
        if (TextUtils.isEmpty(etStartDate.getEditText().getText().toString())) {
            etStartDate.getEditText().setError(error);
            r = false;
        }

        return r;
    }
}
