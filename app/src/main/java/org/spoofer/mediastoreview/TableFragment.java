package org.spoofer.mediastoreview;

import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.DialogCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.spoofer.mediastoreview.mediagroup.MediaGroup;

import java.util.List;

public class TableFragment extends Fragment implements
        TableAdapter.OnClickTitleListener, TableAdapter.OnClickRowListener, TableAdapter.OnLongClickTitleListener {

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
        adapter.setOnClickTitleListener(this);
        adapter.setOnLongClickTitleListener(this);
        adapter.setOnClickRowListener(this);

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
    public void onRowClick(int rowId, int column, CharSequence value) {
        List<String> row = viewModel.getRow(rowId);
        if (null == row)
            return;
        AlertDialog dialog = createDetailDialogue(row);
        if (null != dialog)
            dialog.show();
    }

    @Override
    public void onTitleClick(int column, String columnName) {
        viewModel.setQuerySortField(columnName);
    }

    @Override
    public void onTitleLongClick(TextView view, int column, String columnName) {
        List<List<String>> rows = viewModel.getRows().getValue();
        if (null == rows)
            return;

        // Get the widest cell in the column
        int maxWidth = adapter.getColumnWidth(column);
        for (List<String> row : rows) {
            Rect bounds = new Rect();
            String cell = row.get(column);
            view.getPaint().getTextBounds(cell, 0, cell.length(), bounds);
            maxWidth = Math.max(bounds.width(), maxWidth);
        }
        adapter.setColumnWidth(maxWidth, column);
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


    private AlertDialog createDetailDialogue(List<String> row) {
        StringBuilder details = new StringBuilder();
        List<String> titles = viewModel.getTitles().getValue();
        if (null == row || titles == null)
            return null;

        int count = Math.min(titles.size(), row.size());
        for (int index = 0; index < count; index++) {
            details.append(titles.get(index))
                    .append(": ")
                    .append(row.get(index))
                    .append('\n');
        }

        return new AlertDialog.Builder(getActivity())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Item details")
                .setMessage(details)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create();
    }
}
