package ru.cs.cstaskclient;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import ru.cs.cstaskclient.dto.GridFilter;
import ru.cs.cstaskclient.dto.GridQueryRequest;
import ru.cs.cstaskclient.dto.SessionUser;
import ru.cs.cstaskclient.dto.Task;
import ru.cs.cstaskclient.fragments.assigned.AssignedTaskFragment;
import ru.cs.cstaskclient.fragments.discuss.DiscussFragment;
import ru.cs.cstaskclient.fragments.lastactivity.LastActivityFragment;
import ru.cs.cstaskclient.fragments.project.ProjectViewFragment;
import ru.cs.cstaskclient.fragments.tasks.TaskFavoritesFragment;
import ru.cs.cstaskclient.fragments.tasks.TaskFragment;
import ru.cs.cstaskclient.fragments.worktime.WorkTimeViewFragment;
import ru.cs.cstaskclient.repository.ApiManager;
import ru.cs.cstaskclient.repository.FunctionApi;
import ru.cs.cstaskclient.repository.SecurityTester;
import ru.cs.cstaskclient.repository.TaskApi;
import ru.cs.cstaskclient.util.ApiCall;
import ru.cs.cstaskclient.util.Callback;

/**
 * Created by lithTech on 06.12.2016.
 */

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    Toolbar toolbar;
    NavigationView navigationView;
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_activity_main);

        toolbar = (Toolbar) findViewById(R.id.appBar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);

        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,
                toolbar, R.string.openDrawer, R.string.closeDrawer);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        actionBarDrawerToggle.syncState();

        selectMenuItem(R.id.actLastActivity, getString(R.string.lastActivity));

        View headerView = navigationView.getHeaderView(0);
        TextView loggedMail = (TextView) headerView.findViewById(R.id.tvGlobalEmail);
        TextView loggedUserName = (TextView) headerView.findViewById(R.id.tvGlobalUserName);
        ImageView loggedAvatar = (ImageView) headerView.findViewById(R.id.imAvatar);
        loggedMail.setText(SessionUser.CURRENT.email);
        loggedUserName.setText(SessionUser.CURRENT.fullName);
        Bitmap bitmap = BitmapFactory.decodeByteArray(SessionUser.CURRENT.avatar, 0,
                SessionUser.CURRENT.avatar.length);
        loggedAvatar.setImageBitmap(bitmap);

        applyAccess(navigationView);
    }

    private void applyAccess(NavigationView navigationView) {
        final MenuItem assTasks = navigationView.getMenu().findItem(R.id.actAssignedTasks);
        final MenuItem workTime = navigationView.getMenu().findItem(R.id.actWorkTime);
        FunctionApi fApi = ApiManager.getFunctionApi();
        SecurityTester.assignedTasksAvailable(this, fApi, new Callback() {
            @Override
            public void done(Object o) {
                assTasks.setVisible((Boolean) o);
            }
        });

        SecurityTester.workTimesAvailable(this, fApi, new Callback() {
            @Override
            public void done(Object o) {
                workTime.setVisible((Boolean) o);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            FragmentManager sfm = getSupportFragmentManager();
            int count = sfm.getBackStackEntryCount();

            if (count <= 1)
            {
                drawerLayout.openDrawer(GravityCompat.START);
            }
            else
            {
                count--;
                if (count > 0 && sfm.getFragments().size() >= count) {
                    Fragment fragment = sfm.getFragments().get(count - 1);
                    setTitle(fragment.getArguments().getString("TITLE"));
                }
                sfm.popBackStack();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Const.RESULT_TASK_CREATED && data != null) {
            String title = data.getStringExtra(Const.ARG_TASK_TITLE);
            long id = data.getLongExtra(Const.ARG_TASK_ID, -1);
            DiscussFragment discussFragment = new DiscussFragment();
            discussFragment.setArguments(new Bundle());
            discussFragment.getArguments().putLong(Const.ARG_TASK_ID, id);

            loadFragment(title, discussFragment);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.actAddTask) {
            Intent intent = new Intent(this, TaskCreateActivity.class);
            startActivityForResult(intent, Const.RESULT_TASK_CREATED);
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity_toolbar, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        boolean r = selectMenuItem(item.getItemId(), item.getTitle().toString());

        item.setChecked(r);

        return r;
    }

    public boolean selectMenuItem(int id, String title) {
        Fragment fragment = null;
        Class frClass = null;
        if (id == R.id.actProjects) {
            frClass = ProjectViewFragment.class;
        }
        else if (id == R.id.actWorkTime) {
            frClass = WorkTimeViewFragment.class;
        }
        else if (id == R.id.actLastActivity) {
            frClass = LastActivityFragment.class;
        }
        else if (id == R.id.actAssignedTasks) {
            frClass = AssignedTaskFragment.class;
        }
        else if (id == R.id.actFavoriteTask) {
            frClass = TaskFavoritesFragment.class;
        }
        else if (id == R.id.actGotoTask){
            gotoTask();
            drawerLayout.closeDrawer(GravityCompat.START);
            return false;
        }
        else return false;

        try {
            fragment = (Fragment) frClass.newInstance();
        } catch (Exception e) {
            return false;
        }

        loadFragment(title, fragment);

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void gotoTask() {
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_task_goto, null, false);
        final EditText etTaskNum = (EditText) v.findViewById(R.id.etTaskNumber);
        AlertDialog.Builder ad = new AlertDialog.Builder(this).setCancelable(true)
                .setTitle(R.string.action_move_to_task)
                .setView(v)
                .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final String num = etTaskNum.getText().toString();
                        if (TextUtils.isDigitsOnly(num)) {
                            TaskApi taskApi = ApiManager.getTaskApi();
                            GridQueryRequest req = GridQueryRequest.getSimple(GridQueryRequest.class,
                                    "title", "contains", num);
                            Call<List<Task>> call = taskApi.findTasks(req);
                            call.enqueue(new ApiCall<List<Task>>(MainActivity.this) {
                                @Override
                                public void onResponse(Response<List<Task>> r) {
                                    if (r.body().isEmpty()) return;
                                    String title = r.body().get(0).title;
                                    DiscussFragment discussFragment = new DiscussFragment();
                                    Bundle args = new Bundle();
                                    args.putLong(Const.ARG_TASK_ID, Integer.valueOf(num));
                                    discussFragment.setArguments(args);
                                    loadFragment(title, discussFragment);
                                }
                            });
                        }
                    }
                });
        final AlertDialog d = ad.show();
        etTaskNum.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    d.getButton(DialogInterface.BUTTON_POSITIVE).callOnClick();
                    d.dismiss();
                }
                return true;
            }
        });
    }

    public void loadFragment(String title, Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (fragmentManager.getBackStackEntryCount() > 8)
            fragmentManager.popBackStack();

        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(title)
                .commitAllowingStateLoss();

        Bundle args = fragment.getArguments();
        if (args == null) {
            args = new Bundle();
            fragment.setArguments(args);
        }
        args.putString("TITLE", title);

        setTitle(title);
    }
}
