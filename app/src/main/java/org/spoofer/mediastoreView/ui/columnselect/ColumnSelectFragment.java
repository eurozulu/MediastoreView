package org.spoofer.mediastoreView.ui.columnselect;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.spoofer.mediastoreView.R;
import org.spoofer.mediastoreView.databinding.FragmentColumnsBinding;
import org.spoofer.mediastoreView.model.Source;
import org.spoofer.mediastoreView.model.Sources;
import org.spoofer.mediastoreView.model.table.Column;
import org.spoofer.mediastoreView.model.table.PersistentColumns;
import org.spoofer.mediastoreView.model.table.Table;
import org.spoofer.mediastoreView.ui.SourceSelectViewModel;

import java.util.List;

public class ColumnSelectFragment extends Fragment implements
        CompoundButton.OnCheckedChangeListener {
    private static final String LOG = ColumnSelectFragment.class.getName();

    private FragmentColumnsBinding binding;
    private ColumnSelectAdapter columnSelectAdapter;

    private SourceSelectViewModel sourceSelectViewModel;

    private Menu menu;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setHasOptionsMenu(true);
        columnSelectAdapter = new ColumnSelectAdapter(this);
        sourceSelectViewModel = new ViewModelProvider(getActivity()).get(SourceSelectViewModel.class);

        // Backbutton callback to capture back event (in case of isDirty)
        requireActivity().getOnBackPressedDispatcher().addCallback(this, backBtnCallback);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentColumnsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.listColumns.setAdapter(columnSelectAdapter);
        binding.listColumns.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        sourceSelectViewModel.getSourceName().observe(getViewLifecycleOwner(), this::setSource);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.columns_menu, menu);
        this.menu = menu;
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_action_columns_select_all:
                selectAll(true);
                break;

            case R.id.menu_action_columns_deselect_all:
                selectAll(false);
                break;

            case R.id.menu_action_apply:
                saveColumns();
                break;

            case R.id.menu_action_columns_reset:
                new Table(getContext()).resetAllColumns();
                // fall through to reload new columns.

            case R.id.menu_action_revert:
                setSource(sourceSelectViewModel.getSourceName().getValue());
                break;

            case android.R.id.home:
                return columnSelectAdapter.isDirty() ? handleBackButton() : false;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        columnSelectAdapter.setChecked(buttonView.getText().toString(), isChecked);
        updateMenu();
    }

    private void setSource(String src) {
        Source source = Sources.from(sourceSelectViewModel.getGroupName().getValue(), src);
        if (source == null || source.getLocation() == null) {
            Log.w(LOG, String.format("Failed to set columns list with source %s", src));
            return;
        }
        setSource(source);
    }

    private void setSource(Source source) {
        Table table = new Table(getContext());
        table.setQuery(source.getLocation());
        RecyclerView list = binding.getRoot().findViewById(R.id.list_columns);
        list.removeAllViews();
        columnSelectAdapter.setColumns(table.getColumns());
        updateMenu();
    }

    private void saveColumns() {
        Source source = Sources.from(sourceSelectViewModel.getGroupName().getValue(),
                sourceSelectViewModel.getSourceName().getValue());
        if (source == null || source.getLocation() == null) {
            Log.w(LOG, "Failed to save columns as source not set");
            return;
        }

        PersistentColumns persistentColumns = new PersistentColumns(getContext());
        persistentColumns.saveColumns(source.getLocation().toString(), columnSelectAdapter.getColumns());
        // reset source
        setSource(source);
        getActivity().onBackPressed();
    }

    private void selectAll(boolean state) {
        for (Column column : columnSelectAdapter.getColumns()) {
            column.setVisible(state);
        }
        columnSelectAdapter.notifyDataSetChanged();
    }

    private void updateMenu() {
        // initial setSource triggers prior to createOptionMenus, so ignore first call.
        if (menu == null) {
            return;
        }
        List<Column> columns = columnSelectAdapter.getColumns();
        long selectCount = columns.stream()
                .filter(Column::isVisible)
                .count();
        menu.findItem(R.id.menu_action_columns_deselect_all).setEnabled(selectCount > 0);
        menu.findItem(R.id.menu_action_columns_select_all).setEnabled(selectCount < columns.size());
        boolean isDirty = columnSelectAdapter.isDirty();
        menu.findItem(R.id.menu_action_apply).setVisible(isDirty);
        menu.findItem(R.id.menu_action_revert).setVisible(isDirty);
    }

    private final OnBackPressedCallback backBtnCallback =
            new OnBackPressedCallback(true) {
                @Override
                public void handleOnBackPressed() {
                    if (!handleBackButton()) {
                        backBtnCallback.setEnabled(false);
                        getActivity().onBackPressed();
                    }
                }
            };

    private boolean handleBackButton() {
        final boolean isDirty = columnSelectAdapter.isDirty();
        if (isDirty) {
            Log.d(LOG, "captured back command as data is dirty");
            AlertDialog alert = Dialogue.createApplyChangesDialog(
                    getActivity(), (dialog, which) -> {
                        saveColumns();
                        getActivity().onBackPressed();
                    });
            alert.show();
            // Dialog shown, return as back handled to cancel system back.
            return true;
        }
        // Data is clean, proceed with back (don't handle it, let system)
        return false;
    }
}
