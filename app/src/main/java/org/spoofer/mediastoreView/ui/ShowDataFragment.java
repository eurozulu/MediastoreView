package org.spoofer.mediastoreView.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.MenuRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.spoofer.mediastoreView.R;
import org.spoofer.mediastoreView.databinding.FragmentShowdataBinding;
import org.spoofer.mediastoreView.model.Source;
import org.spoofer.mediastoreView.model.Sources;
import org.spoofer.mediastoreView.model.table.Table;
import org.spoofer.mediastoreView.ui.datagrid.DataGridAdapter;
import org.spoofer.mediastoreView.ui.datagrid.DataGridViewModel;
import org.spoofer.mediastoreView.ui.titles.TitlesAdapter;

import java.util.List;

public class ShowDataFragment extends Fragment {
    public static final String ARG_GROUP_NAME = "group_name";
    public static final String ARG_SOURCE_NAME = "source_name";
    private static final String LOG = ShowDataFragment.class.getName();

    private FragmentShowdataBinding binding;

    private SourceSelectViewModel sourceSelectViewModel;
    private DataGridViewModel dataGridViewModel;

    private TitlesAdapter titlesAdapter;
    private DataGridAdapter dataGridAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setHasOptionsMenu(true);

        new Table(getContext()).resetAllColumns();

        titlesAdapter = new TitlesAdapter();
        dataGridAdapter = new DataGridAdapter(titlesAdapter);
        dataGridViewModel = new ViewModelProvider(this).get(DataGridViewModel.class);
        sourceSelectViewModel = new ViewModelProvider(getActivity()).get(SourceSelectViewModel.class);

        if (getArguments() != null) {
            String groupName = getArguments().getString(ARG_GROUP_NAME);
            if (!TextUtils.isEmpty(groupName)) {
                sourceSelectViewModel.setGroupName(groupName);
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentShowdataBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView columnTitles = binding.listShowDataTitles;
        columnTitles.setAdapter(titlesAdapter);
        columnTitles.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        dataGridViewModel.getColumns().observe(getViewLifecycleOwner(), titlesAdapter::setColumns);

        RecyclerView dataGrid = binding.listShowDataCells;
        dataGrid.setAdapter(dataGridAdapter);
        dataGrid.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        dataGridViewModel.getRows().observe(getViewLifecycleOwner(), dataGridAdapter::setRows);

        sourceSelectViewModel.getGroupName().observe(getViewLifecycleOwner(), this::updateGroup);
        sourceSelectViewModel.getSourceName().observe(getViewLifecycleOwner(), this::updateSource);

        binding.bottomNavigationHome.setOnItemSelectedListener(menu -> {
            sourceSelectViewModel.setSourceName(menu.getTitle().toString());
            return true;
        });

        // Select first item in bottom nav to kick off the datagrid load
        //setSelected();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.showdata_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() != R.id.menu_action_columns) {
            return super.onOptionsItemSelected(item);
        }
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
        Bundle args = new Bundle();
        args.putString(ARG_GROUP_NAME, sourceSelectViewModel.getGroupName().getValue());
        args.putString(ARG_SOURCE_NAME, sourceSelectViewModel.getSourceName().getValue());

        navController.navigate(R.id.menu_action_columns, args);
        return true;
    }

    private void updateGroup(String groupName) {
        // Update the bootom navigation with the avaialble sources within this group
        List<Source> group = new Sources().get(groupName);
        if (group == null) {
            Log.e(LOG, String.format("Failed to find any sources for %s", groupName));
            return;
        }
        Menu navMenu = binding.bottomNavigationHome.getMenu();
        navMenu.clear();
        @MenuRes int menuItems = groupName.equals("Audio")
                ? R.menu.bottom_navigation_audio
                : R.menu.bottom_navigation;
        MenuInflater inflater = new MenuInflater(getContext());
        // Add all possible items, then hide ones not in group
        inflater.inflate(menuItems, navMenu);
        for (int i = navMenu.size() - 1; i >= 0; i--) {
            MenuItem menuItem = navMenu.getItem(i);
            menuItem.setVisible(Sources.indexOf(menuItem.getTitle().toString(), group) >= 0);
        }
    }

    // update the datagrid with the named data source.
    private boolean updateSource(String sourceName) {
        Log.d(LOG, String.format("starting updateSource with: %s", sourceName));
        String groupName = sourceSelectViewModel.getGroupName().getValue();
        if (TextUtils.isEmpty(groupName)) {
            return false;
        }
        Source source = Sources.from(groupName, sourceName);
        if (source == null || source.getLocation() == null) {
            Log.e(LOG, String.format("failed to update source %s:%s as not known", groupName, sourceName));
            return false;
        }
        setAppBarTitle(String.format("%s  -  %s", groupName, sourceName));
        dataGridViewModel.setSource(getContext(), source.getLocation());
        return true;
    }

    private void setAppBarTitle(String title) {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }

    private void setSelected() {
        @IdRes int selectedId = 0;
        Menu menu = binding.bottomNavigationHome.getMenu();
        String sourceName = sourceSelectViewModel.getSourceName().getValue();
        if (!TextUtils.isEmpty(sourceName)) {
            for (int i = 0; i < menu.size(); i++) {
                MenuItem item = menu.getItem(i);
                if (sourceName.equals(item.getTitle().toString())) {
                    selectedId = item.getItemId();
                    break;
                }
            }
        }
        if (selectedId == 0) {
            selectedId = menu.getItem(0).getItemId();
        }
        binding.bottomNavigationHome.setSelectedItemId(selectedId);
    }
}