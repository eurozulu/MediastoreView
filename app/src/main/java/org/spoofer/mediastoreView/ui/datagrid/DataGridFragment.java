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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.spoofer.mediastoreView.R;
import org.spoofer.mediastoreView.databinding.FragmentDatagridBinding;
import org.spoofer.mediastoreView.model.DataRow;
import org.spoofer.mediastoreView.model.Source;
import org.spoofer.mediastoreView.model.Sources;
import org.spoofer.mediastoreView.model.columns.Column;
import org.spoofer.mediastoreView.model.columns.ColumnSet;
import org.spoofer.mediastoreView.query.Query;
import org.spoofer.mediastoreView.query.SelectQuery;
import org.spoofer.mediastoreView.ui.columns.ColumnsFragment;
import org.spoofer.mediastoreView.ui.titles.TitlesAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DataGridFragment extends Fragment {
    public static final String NULL_TOKEN = "-";
    public static final String ARG_GROUP_NAME = "group_name";
    private static final String LOG = DataGridFragment.class.getName();
    private static final int MAX_ROWS = 30;
    private FragmentDatagridBinding binding;

    private String groupName = "";
    private String sourceName;

    private DataGridViewModel dataGridViewModel;
    private TitlesAdapter titlesAdapter;
    private DataGridAdapter dataGridAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setHasOptionsMenu(true);

        if (getArguments() != null) {
            groupName = getArguments().getString(ARG_GROUP_NAME);
        }
        dataGridViewModel = new ViewModelProvider(this).get(DataGridViewModel.class);
        titlesAdapter = new TitlesAdapter();
        dataGridAdapter = new DataGridAdapter(titlesAdapter);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDatagridBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView columnTitles = binding.listGridTitles;
        columnTitles.setAdapter(titlesAdapter);
        columnTitles.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));

        RecyclerView dataGrid = binding.listGridCells;
        dataGrid.setAdapter(dataGridAdapter);
        dataGrid.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        // Monitor ViewModel for changes and update adapter to reflect in GUI
        dataGridViewModel.getColumns().observe(getViewLifecycleOwner(), cols -> titlesAdapter.setColumns(cols.getVisibleColumns()));
        dataGridViewModel.getRows().observe(getViewLifecycleOwner(), dataGridAdapter::addRows);

        setupBottomNavigator();
        binding.bottomNavigationHome.setOnItemSelectedListener(this::updateSource);

        // Select first item in bottom nav to kick off the datagrid load
        MenuItem firstMenu = binding.bottomNavigationHome.getMenu().getItem(0);
        binding.bottomNavigationHome.setSelectedItemId(firstMenu.getItemId());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.datagrid_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() != R.id.menu_action_columns) {
            return super.onOptionsItemSelected(item);
        }
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
        Bundle args = new Bundle();
        args.putString(ARG_GROUP_NAME, groupName);
        args.putString(ColumnsFragment.ARG_SOURCE_NAME, sourceName);

        navController.navigate(R.id.menu_action_columns, args);
        return true;
    }

    // update the datagrid with the named data source.
    private boolean updateSource(MenuItem selectedMenu) {
        String sourceName = selectedMenu.getTitle().toString();
        Log.d(LOG, String.format("starting updateSource with: %s", sourceName));

        Source source = Sources.from(groupName, sourceName);
        if (source == null || source.getLocation() == null) {
            Log.e(LOG, String.format("failed to update source %s:%s as not known", groupName, sourceName));
            return false;
        }

        this.sourceName = sourceName;
        setAppBarTitle(String.format("%s  -  %s", groupName, sourceName));

        // Update column titles
        ColumnSet columns = new ColumnSet(getContext(), source.getLocation());
        dataGridViewModel.setColumns(columns);
        Log.d(LOG, String.format("set source with %d columns", columns.getColumns().size()));

        dataGridAdapter.reset();
        // Execute source query to populate datagrid
        SelectQuery query = Query.of(Query.QueryType.SELECT);
        query.setSource(source.getLocation());
        query.setField(columns.getVisibleColumns().stream()
                .map(Column::getName)
                .collect(Collectors.toList()));
        Cursor cursor = query.execute(getContext());
        if (cursor == null) {
            return false;
        }
        Log.d(LOG, String.format("query complete with: %d results", cursor.getCount()));
        dataGridViewModel.setRows(this.parseDataRows(cursor));
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

    private List<DataRow> parseDataRows(Cursor cursor) {
        Log.d(LOG, String.format("Parsing row %d", cursor.getCount()));
        int colCount = cursor.getColumnCount();
        List<DataRow> rows = new ArrayList<>();
        try {
            while (cursor.moveToNext()) {
                List<String> row = new ArrayList<>();
                for (int i = 0; i < colCount; i++) {
                    row.add(readCursorValue(cursor, i));
                }
                rows.add(new DataRow(row));
                if (rows.size() >= MAX_ROWS) {
                    break;
                }
            }
        } finally {
            cursor.close();
        }
        return rows;
    }

    private String readCursorValue(Cursor cursor, int index) {
        switch (cursor.getType(index)) {
            case Cursor.FIELD_TYPE_STRING:
                return cursor.getString(index);
            case Cursor.FIELD_TYPE_INTEGER:
                return Integer.toString(cursor.getInt(index));
            case Cursor.FIELD_TYPE_FLOAT:
                return Float.toString(cursor.getFloat(index));
            case Cursor.FIELD_TYPE_BLOB:
                return new String(cursor.getBlob(index));
            case Cursor.FIELD_TYPE_NULL:
                return NULL_TOKEN;
            default:
                return "";
        }
    }
}