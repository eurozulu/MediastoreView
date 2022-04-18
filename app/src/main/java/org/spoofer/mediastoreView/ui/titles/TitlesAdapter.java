package org.spoofer.mediastoreView.ui.titles;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.spoofer.mediastoreView.R;
import org.spoofer.mediastoreView.model.columns.Column;

import java.util.List;

public class TitlesAdapter extends RecyclerView.Adapter<TitlesViewHolder> {

    private List<Column> columns;

    public int getColumnWidth(int position) {
        if (position < 0 || position >= getItemCount()) {
            return 0;
        }
        return columns.get(position).getWidth();
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TitlesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new TitlesViewHolder(inflater.inflate(R.layout.titles_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TitlesViewHolder holder, int position) {
        if (position < 0 || position >= getItemCount()) {
            return;
        }
        holder.setColumn(columns.get(position));
    }

    @Override
    public int getItemCount() {
        return columns != null ? columns.size() : 0;
    }
}
