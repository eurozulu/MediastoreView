package org.spoofer.mediastoreView.ui.titles;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.spoofer.mediastoreView.R;
import org.spoofer.mediastoreView.model.table.Column;

import java.util.List;
import java.util.stream.Collectors;

public class TitlesAdapter extends RecyclerView.Adapter<TitlesViewHolder> {

    private List<Column> columns;
    private ViewGroup viewParent;

    public void setColumns(List<Column> columns) {
        this.columns = columns.stream().filter(Column::isVisible).collect(Collectors.toList());
        notifyDataSetChanged();
    }


    public int getColumnViewWidth(int index) {
        if (viewParent == null || index >= viewParent.getChildCount()) {
            return 0;
        }
        return viewParent.getChildAt(index).getWidth();
    }

    @NonNull
    @Override
    public TitlesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (this.viewParent != parent) {
            this.viewParent = parent;
        }

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
