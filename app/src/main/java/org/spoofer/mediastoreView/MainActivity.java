package org.spoofer.mediastoreView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.spoofer.mediastoreView.databinding.ActivityMainBinding;
import org.spoofer.mediastoreView.ui.SourceSelectViewModel;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    private SourceSelectViewModel sourceSelectViewModel;

    // Receiver for permissions check
    private final ActivityResultLauncher<String[]> mPermissionResult =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), isGranted -> {
                // Gather any permission NOT granted
                List<String> failed = isGranted.entrySet().stream()
                        .filter(e -> !e.getValue())
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toList());
                if (!failed.isEmpty()) {
                    Snackbar.make(binding.getRoot(), R.string.permission_failed, Snackbar.LENGTH_LONG);
                    //checkPermissions();
                    return;
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!checkPermissions()) {
            // wait until permissions granted before making conntection
            requestPermissions();
        }

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sourceSelectViewModel = new ViewModelProvider(this).get(SourceSelectViewModel.class);
        sourceSelectViewModel.setGroupName(getString(R.string.menu_audio));
        sourceSelectViewModel.setSourceName(getString(R.string.menu_source_media));

        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.menu_show_data)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_all_files:
            case R.id.nav_downloads:
            case R.id.nav_audio:
            case R.id.nav_images:
            case R.id.nav_video:
                sourceSelectViewModel.setGroupName(item.getTitle().toString());
                navigateTo(R.id.menu_show_data);
                binding.drawerLayout.closeDrawer(Gravity.LEFT, true);
                return false;
        }
        return false;
    }

    private void navigateTo(@IdRes int target) {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        navController.popBackStack();
        navController.navigate(target, null);
    }

    private void requestPermissions() {
        mPermissionResult.launch(new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.MANAGE_EXTERNAL_STORAGE,
                Manifest.permission.WAKE_LOCK
        });
    }

    private boolean checkPermissions() {
        return (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.MANAGE_EXTERNAL_STORAGE) ==
                        PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.WAKE_LOCK) ==
                        PackageManager.PERMISSION_GRANTED);
    }
}