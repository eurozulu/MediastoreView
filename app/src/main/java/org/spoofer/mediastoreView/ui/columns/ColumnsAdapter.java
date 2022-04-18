package org.spoofer.mediastoreView.ui.columns;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.spoofer.mediastoreView.R;
import org.spoofer.mediastoreView.model.columns.ColumnSet;

public class ColumnsAdapter extends RecyclerView.Adapter<ColumnsViewHolder> {

    private ColumnSet columns;

    public ColumnSet getColumns() {
        return columns;
    }

    public void setColumns(ColumnSet columns) {
        this.columns = columns;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ColumnsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ColumnsViewHolder(inflater.inflate(R.layout.columns_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ColumnsViewHolder holder, int position) {
        if (position < 0 || position >= getItemCount()) {
            return;
        }
        holder.setColumn(columns.getColumns().get(position));
    }

    @Override
    public int getItemCount() {
        return columns != null ? columns.getColumns().size() : 0;
    }
}
