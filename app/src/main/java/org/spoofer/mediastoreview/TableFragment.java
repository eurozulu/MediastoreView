package org.spoofer.mediastoreview;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.spoofer.mediastoreview.mediagroup.MediaGroup;

public class TableFragment extends Fragment implements TableAdapter.OnClickTableListener {

    private static final String DEFAULT_KEY =
            "androidx.lifecycle.ViewModelProvider.DefaultKey";

    private TableAdapter adapter;
    private RecyclerView list;
    private TextView tableTitle;
    private TextView fields;

    private TableViewModel viewModel;

    private MediaGroup.Query query;


    public TableFragment() {
        super(R.layout.fragment_table);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new TableAdapter(getContext());
        adapter.setOnClickTableListener(this);

        query = null != getArguments() ?
                (MediaGroup.Query) getArguments().getParcelable(MainActivity.EXTRA_GROUP_NAME) : null;

        String key = null != query ? query.getQuery().toString() : DEFAULT_KEY;
        viewModel = new ViewModelProvider(getActivity(), new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new TableViewModel(getContext().getContentResolver());
            }
        }).get(key, TableViewModel.class);

        if (null != query)
            viewModel.setQuery(query);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        list = view.findViewById(android.R.id.list);
        LinearLayoutManager llm = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        list.setLayoutManager(llm);
        list.setAdapter(adapter);

        ViewGroup titlePanel = view.findViewById(R.id.panel_titles);
        adapter.setTitlesView(titlePanel);

        tableTitle = view.findViewById(R.id.text_table_title);
        tableTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFields();
            }
        });
        tableTitle.setText("");
        fields = view.findViewById(R.id.text_fieldnames);
        fields.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideFields();
            }
        });
        hideFields();

        viewModel.getQuery().observe(getViewLifecycleOwner(), query -> {
            tableTitle.setText(query.getName());
        });

        viewModel.getTitles().observe(getViewLifecycleOwner(), titles -> {
            adapter.setTitles(titles);
            adapter.notifyDataSetChanged();
        });

        viewModel.getRows().observe(getViewLifecycleOwner(), rows -> {
            adapter.setRows(rows);
            adapter.notifyDataSetChanged();
        });

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onClick(int column, String columnName) {
        viewModel.setQuerySortField(columnName);
    }

    private void showFields() {
        StringBuilder sb = new StringBuilder();
        for (String title : adapter.getTitles()) {
            sb.append(title).append('\n');
        }
        fields.setText(sb.toString());
        fields.setVisibility(View.VISIBLE);
    }

    private void hideFields() {
        fields.setText("");
        fields.setVisibility(View.GONE);
    }

    public static TableFragment newInstance(MediaGroup.Query query) {
        TableFragment fragment = new TableFragment();
        Bundle arguments = new Bundle();
        arguments.putParcelable(MainActivity.EXTRA_GROUP_NAME, query);
        fragment.setArguments(arguments);
        return fragment;
    }
}
