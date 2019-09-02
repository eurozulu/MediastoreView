package org.spoofer.mediastoreview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.navigation.NavigationView;

import org.spoofer.mediastoreview.mediagroup.MediaGroup;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_GROUP_NAME = BuildConfig.APPLICATION_ID + ".extras.GROUP_NAME";

    private NavigationView navView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    private TableViewAdaper tableViewAdaper;
    

    private final NavigationView.OnNavigationItemSelectedListener navigationListener
            = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            int id = menuItem.getItemId();

            switch (id) {
                case R.id.menu_nav_audio:
                    tableViewAdaper.setMediaGroup(
                            MediaStore.Audio.class.getSimpleName());
                    break;

                case R.id.menu_nav_files:
                    tableViewAdaper.setMediaGroup(
                            MediaStore.Files.class.getSimpleName());
                    break;

                case R.id.menu_nav_video:
                    tableViewAdaper.setMediaGroup(
                            MediaStore.Video.class.getSimpleName());
                    break;

                case R.id.menu_nav_images:
                    tableViewAdaper.setMediaGroup(
                            MediaStore.Images.class.getSimpleName());
                    break;

                default:
                    return false;
            }
            drawerLayout.closeDrawers();
            return true;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermissions();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.activity_main);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.string.nav_drawer_open, R.string.nav_drawer_close);

        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navView = findViewById(R.id.main_navview);
        navView.setNavigationItemSelectedListener(navigationListener);

        tableViewAdaper = new TableViewAdaper(getSupportFragmentManager());
        
        ViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(tableViewAdaper);
    }

    @Override
    protected void onResume() {
        super.onResume();
        tableViewAdaper.setMediaGroup(MediaGroup.MEDIA_GROUPS[0]);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (null == tableViewAdaper)
            return;

        String groupName = null != savedInstanceState ?
                savedInstanceState.getString(EXTRA_GROUP_NAME) : null;

        if (!TextUtils.isEmpty(groupName))
            tableViewAdaper.setMediaGroup(groupName);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (null != tableViewAdaper && null != tableViewAdaper.getMediaGroup())
            outState.putString(EXTRA_GROUP_NAME, tableViewAdaper.getMediaGroup().getGroupName());
    }

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED ||

                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                            PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, 666);
            }
        }
    }

    class TableViewAdaper extends FragmentStatePagerAdapter {

        private MediaGroup mediaGroup;

        public TableViewAdaper(@NonNull FragmentManager fm) {
            super(fm, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        public MediaGroup getMediaGroup() {
            return mediaGroup;
        }

        public void setMediaGroup(MediaGroup mediaGroup) {
            this.mediaGroup = mediaGroup;
            notifyDataSetChanged();
        }

        public void setMediaGroup(String groupName) {
            int index = 0;
            for (MediaGroup group : MediaGroup.MEDIA_GROUPS) {
                if (group.getGroupName().equals(groupName))
                    break;
                index++;
            }
            setMediaGroup(index < MediaGroup.MEDIA_GROUPS.length ?
                    MediaGroup.MEDIA_GROUPS[index] : null);
        }

        @Override
        public int getCount() {
            return null != mediaGroup ? mediaGroup.getQueries().size() : 0;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return TableFragment.newInstance(mediaGroup.getQueries().get(position));
        }
    }

}
