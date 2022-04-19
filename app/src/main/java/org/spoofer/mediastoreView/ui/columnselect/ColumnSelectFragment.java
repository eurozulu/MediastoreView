package org.spoofer.mediastoreView.ui.columnselect;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import org.spoofer.mediastoreView.databinding.FragmentColumnsBinding;
import org.spoofer.mediastoreView.model.Source;
import org.spoofer.mediastoreView.model.Sources;
import org.spoofer.mediastoreView.model.table.Table;
import org.spoofer.mediastoreView.ui.SourceSelectViewModel;

public class ColumnSelectFragment extends Fragment {
    private static final String LOG = ColumnSelectFragment.class.getName();

    private FragmentColumnsBinding binding;
    private ColumnSelectAdapter columnSelectAdapter;

    private SourceSelectViewModel sourceSelectViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        columnSelectAdapter = new ColumnSelectAdapter();
        sourceSelectViewModel = new ViewModelProvider(getActivity()).get(SourceSelectViewModel.class);
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

        sourceSelectViewModel.getGroupName().observe(getViewLifecycleOwner(), binding.textColumnsGroupname::setText);
        sourceSelectViewModel.getSourceName().observe(getViewLifecycleOwner(), this::setSource);
    }

    private void setSource(String src) {
        binding.textColumnsSourcename.setText(src);
        Source source = Sources.from(sourceSelectViewModel.getGroupName().getValue(), src);
        if (source == null || source.getLocation() == null) {
            Log.w(LOG, String.format("Failed to set columns list with source %s", src));
            return;
        }
        Table table = new Table(getContext());
        table.setQuery(source.getLocation());
        columnSelectAdapter.setColumns(table.getColumns());
    }
}
