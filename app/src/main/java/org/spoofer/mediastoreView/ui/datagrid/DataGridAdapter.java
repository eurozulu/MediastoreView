package org.spoofer.mediastoreView.ui.datagrid;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.spoofer.mediastoreView.R;
import org.spoofer.mediastoreView.model.table.Row;
import org.spoofer.mediastoreView.ui.titles.TitlesAdapter;

import java.util.ArrayList;
import java.util.List;

public class DataGridAdapter extends RecyclerView.Adapter<DataGridViewHolder> {
    private static final String LOG = DataGridAdapter.class.getName();

    private final TitlesAdapter columns;
    private final List<Row> rowData = new ArrayList<>();

    public DataGridAdapter(TitlesAdapter columns) {
        this.columns = columns;
    }

    public void setRows(@NonNull List<Row> rows) {
        Log.d(LOG, String.format("setting datagrid adapter with %d rows", rows.size()));
        rowData.clear();
        rowData.addAll(rows);
        notifyDataSetChanged();
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
        setCellWidths((ViewGroup) holder.itemView);
        holder.setRowData(rowData.get(position));
    }

    @Override
    public int getItemCount() {
        return rowData != null ? rowData.size() : 0;
    }

    public void reset() {
        int size = getItemCount();
        if (size > 0) {
            rowData.clear();
            notifyItemRangeRemoved(0, size);
        }
    }


    private void ensureContainsCells(LayoutInflater inflater, ViewGroup row, int count) {
        while (row.getChildCount() < count) {
            TextView cell = (TextView) inflater.inflate(R.layout.cell_data, row, false);
            row.addView(cell);
        }
    }

    private void setCellWidths(ViewGroup row) {
        int index = 0;
        for (; index < row.getChildCount(); index++) {
            int width = 0;
            if (index < columns.getItemCount()) {
                width = columns.getColumnViewWidth(index);
            }
            ((TextView) row.getChildAt(index)).setWidth(width);
        }
    }
}
