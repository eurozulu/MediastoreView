package org.spoofer.mediastoreView.ui.columns;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import org.spoofer.mediastoreView.databinding.FragmentColumnsBinding;
import org.spoofer.mediastoreView.model.Source;

public class ColumnsFragment extends Fragment {
    public static final String ARG_SOURCE_NAME = "source_name";

    private FragmentColumnsBinding binding;
    private ColumnsViewModel viewModel;
    private ColumnsAdapter columnsAdapter;

    private String source;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            source = getArguments().getString(ARG_SOURCE_NAME);
        }
        viewModel = new ViewModelProvider(this).get(ColumnsViewModel.class);
        columnsAdapter = new ColumnsAdapter();
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
        viewModel.getSource().observe(getViewLifecycleOwner(), this::setSourceTitle);
        viewModel.getColumns().observe(getViewLifecycleOwner(), columnsAdapter::setColumns);

    }

    private void setSourceTitle(Source src) {
        binding.textColumnsSourcename.setText(src.getName());
    }
}
