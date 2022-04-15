package org.spoofer.mediastoreView.ui.datagrid;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.spoofer.mediastoreView.R;
import org.spoofer.mediastoreView.model.DataRow;
import org.spoofer.mediastoreView.model.TitleColumns;

import java.util.ArrayList;
import java.util.List;

public class DataGridAdapter extends RecyclerView.Adapter<DataGridViewHolder> {

    private static final String LOG = DataGridAdapter.class.getName();

    private final TitleColumns titleColumns;

    private final List<DataRow> rows = new ArrayList<>();

    public DataGridAdapter(TitleColumns titleColumns) {
        this.titleColumns = titleColumns;
    }

    @NonNull
    @Override
    public DataGridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.cell_row, parent, false);
        // Add number of cells required for this data
        root.removeAllViews();
        int colCount = titleColumns.getColumnCount();
        for (int i = 0; i < colCount; i++) {
            TextView cell = (TextView) inflater.inflate(R.layout.cell_data, root, false);
            root.addView(cell);
        }
        return new DataGridViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull DataGridViewHolder holder, int position) {
        ViewGroup row = (ViewGroup) holder.itemView;
        // set column widths
        for (int i = 0; i < row.getChildCount(); i++) {
            setLayoutWidth(row.getChildAt(i), titleColumns.getColumnWidth(i));
        }
        holder.setRowData(rows.get(position));
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

    private void setLayoutWidth(View v, int width) {
        ViewGroup.LayoutParams params = v.getLayoutParams();
        params.width = width;
        v.setLayoutParams(params);
    }
}
