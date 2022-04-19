package org.spoofer.mediastoreView.ui.columnselect;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.spoofer.mediastoreView.R;
import org.spoofer.mediastoreView.model.table.Column;

import java.util.List;

public class ColumnSelectAdapter extends RecyclerView.Adapter<ColumnSelectViewHolder> {

    private List<Column> columns;

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ColumnSelectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ColumnSelectViewHolder(inflater.inflate(R.layout.columns_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ColumnSelectViewHolder holder, int position) {
        holder.setColumn(columns.get(position));
    }

    @Override
    public int getItemCount() {
        return columns != null ? columns.size() : 0;
    }
}
