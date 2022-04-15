package org.spoofer.mediastoreView.ui.datagrid;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.MenuRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.spoofer.mediastoreView.R;
import org.spoofer.mediastoreView.databinding.FragmentDatagridBinding;
import org.spoofer.mediastoreView.model.DataRow;
import org.spoofer.mediastoreView.model.Source;
import org.spoofer.mediastoreView.model.Sources;
import org.spoofer.mediastoreView.model.TitleColumns;
import org.spoofer.mediastoreView.query.Query;
import org.spoofer.mediastoreView.query.SelectQuery;

import java.util.Arrays;
import java.util.List;

public class DataGridFragment extends Fragment {
    private static final String LOG = DataGridFragment.class.getName();

    public static final String ARG_GROUP_NAME = "group_name";
    public static final String ARG_SOURCE_NAME = "source_name";

    private FragmentDatagridBinding binding;

    private String groupName = "";

    private DataGridAdapter dataGridAdapter;
    private DataGridViewModel dataGridViewModel;

    private TitleColumns titleColumns;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            groupName = getArguments().getString(ARG_GROUP_NAME);
        }
        dataGridViewModel = new ViewModelProvider(this).get(DataGridViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDatagridBinding.inflate(inflater, container, false);

        titleColumns = new TitleColumns(binding.layoutDatagridTitles.layoutGridRow);
        dataGridAdapter = new DataGridAdapter(titleColumns);

        // Monitor ViewModel for changes and update adapter to reflect in GUI
        dataGridViewModel.getTitles().observe(getViewLifecycleOwner(), titleColumns::updateTitles);
        dataGridViewModel.getRows().observe(getViewLifecycleOwner(), dataGridAdapter::addRows);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.bottomNavigationHome.setOnItemSelectedListener(
                menuItem -> updateSource(menuItem.getTitle().toString()));

        RecyclerView dataGrid = binding.listGridCells;
        dataGrid.setAdapter(dataGridAdapter);
        dataGrid.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        setupBottomNavigator();

        // Select first item in bottom nav to kick off the datagrid load
        MenuItem firstMenu = binding.bottomNavigationHome.getMenu().getItem(0);
        binding.bottomNavigationHome.setSelectedItemId(firstMenu.getItemId());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // update the datagrid with the named data source.
    private boolean updateSource(String sourceName) {
        Log.d(LOG, String.format("starting updateSource with: %s", sourceName));

        // reset grid
        dataGridAdapter.reset();

        List<Source> sources = new Sources().get(groupName);
        if (sources == null) {
            Log.e(LOG, String.format("failed to update source '%s' as not known", groupName));
            return false;
        }
        int index = sourceIndex(sourceName, sources);
        if (index < 0 && !sources.isEmpty()) {
            index = 0;
        }
        Source source = index >= 0 ? sources.get(index) : null;
        if (source == null || source.getLocation() == null) {
            return false;
        }
        SelectQuery query = Query.of(Query.QueryType.SELECT);
        query.setSource(source.getLocation());

        Cursor cursor = query.execute(getContext());
        if (cursor == null) {
            return false;
        }
        setAppBarTitle(String.format("%s  -  %s", groupName, sourceName));

        Log.d(LOG, String.format("query complete with: %d results", cursor.getCount()));
        dataGridViewModel.setTitles(Arrays.asList(cursor.getColumnNames()));
        dataGridViewModel.setRows(DataRow.parseDataRows(cursor));
        Log.d(LOG, "updateSource - complete");
        return true;
    }

    private void setAppBarTitle(String title) {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }


    }

    private void setupBottomNavigator() {
        List<Source> group = new Sources().get(groupName);
        if (group == null) {
            Log.e(LOG, String.format("Failed to find bottom nav source for %s", groupName));
            return;
        }
        Menu navMenu = binding.bottomNavigationHome.getMenu();
        navMenu.clear();
        @MenuRes int menuItems = sourceIndex("Albums", group) >= 0
                ? R.menu.bottom_navigation_audio
                : R.menu.bottom_navigation;
        MenuInflater inflater = new MenuInflater(getContext());
        // Add all possible items, then remove ones not in group
        inflater.inflate(menuItems, navMenu);
        for (int i = navMenu.size() - 1; i >= 0; i--) {
            if (sourceIndex(navMenu.getItem(i).getTitle().toString(), group) >= 0) {
                continue;
            }
            navMenu.getItem(i).setVisible(false);
        }
    }

    private int sourceIndex(String name, List<Source> sources) {
        int index = 0;
        for (Source src : sources) {
            if (src.getName().equalsIgnoreCase(name)) {
                return index;
            }
            index++;
        }
        return -1;
    }


}