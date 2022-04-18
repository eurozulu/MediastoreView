package org.spoofer.mediastoreView.ui.datagrid;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.spoofer.mediastoreView.R;
import org.spoofer.mediastoreView.model.DataRow;
import org.spoofer.mediastoreView.ui.titles.TitlesAdapter;

import java.util.ArrayList;
import java.util.List;

public class DataGridAdapter extends RecyclerView.Adapter<DataGridViewHolder> {

    private static final String LOG = DataGridAdapter.class.getName();

    private final TitlesAdapter columns;

    private final List<DataRow> rows = new ArrayList<>();

    public DataGridAdapter(@NonNull TitlesAdapter columns) {
        this.columns = columns;
    }

    @NonNull
    @Override
    public DataGridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.cell_row, parent, false);
        ensureContainsCells(inflater, root, columns.getItemCount());
        return new DataGridViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull DataGridViewHolder holder, int position) {
        holder.setRowData(columns.getColumns(), rows.get(position));
    }

    @Override
    public int getItemCount() {
        return rows.size();
    }

    public void addRows(List<DataRow> newRows) {
        Log.d(LOG, String.format("adding %d rows", newRows.size()));
        int size = rows.size();
        this.rows.addAll(newRows);
        notifyItemRangeChanged(size, rows.size());
        Log.d(LOG, "adding rows complete");
    }

    public void addRow(List<String> row) {
        final int index = rows.size();
        rows.add(new DataRow(row));
        notifyItemInserted(index);
    }

    public void reset() {
        int size = rows.size();
        if (size > 0) {
            rows.clear();
            notifyItemRangeRemoved(0, size);
        }
    }


    private void ensureContainsCells(LayoutInflater inflater, ViewGroup row, int count) {
        while (row.getChildCount() < count) {
            TextView cell = (TextView) inflater.inflate(R.layout.cell_data, row, false);
            row.addView(cell);
        }
    }

}
