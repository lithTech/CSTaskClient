package ru.cs.cstaskclient;

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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ru.cs.cstaskclient.dto.SessionUser;
import ru.cs.cstaskclient.fragments.project.ProjectViewFragment;
import ru.cs.cstaskclient.fragments.worktime.WorkTimeViewFragment;

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

        selectMenuItem(R.id.actWorkTime, getString(R.string.workTime));

        View headerView = navigationView.getHeaderView(0);
        TextView loggedMail = (TextView) headerView.findViewById(R.id.tvGlobalEmail);
        TextView loggedUserName = (TextView) headerView.findViewById(R.id.tvGlobalUserName);
        ImageView loggedAvatar = (ImageView) headerView.findViewById(R.id.imAvatar);
        loggedMail.setText(SessionUser.CURRENT.email);
        loggedUserName.setText(SessionUser.CURRENT.fullName);
        Bitmap bitmap = BitmapFactory.decodeByteArray(SessionUser.CURRENT.avatar, 0,
                SessionUser.CURRENT.avatar.length);
        loggedAvatar.setImageBitmap(bitmap);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            FragmentManager sfm = getSupportFragmentManager();
            int count = sfm.getBackStackEntryCount();

            if (count == 0)
            {
                drawerLayout.openDrawer(GravityCompat.START);
            }
            else
            {
                sfm.popBackStack();
            }
        }
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

    public void loadFragment(String title, Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (fragmentManager.getBackStackEntryCount() > 8)
            fragmentManager.popBackStack();

        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
        setTitle(title);
    }
}
