package org.spoofer.mediastoreView.ui.columnselect;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.spoofer.mediastoreView.R;
import org.spoofer.mediastoreView.model.table.Column;

import java.util.List;

public class ColumnSelectAdapter extends RecyclerView.Adapter<ColumnSelectViewHolder> {
    private static final String LOG = ColumnSelectAdapter.class.getName();

    private List<Column> columns;
    private int cleanStateHash;

    private CompoundButton.OnCheckedChangeListener checkedChangeListener;

    public ColumnSelectAdapter(CompoundButton.OnCheckedChangeListener checkedChangeListener) {
        this.checkedChangeListener = checkedChangeListener;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setChecked(String columnName, boolean isChecked) {
        final String name = cleanName(columnName);
        int index = getColumnIndex(name);
        if (index < 0) {
            Log.w(LOG, String.format("failed to recognise checkbox click with name %s", name));
            return;
        }
        columns.get(index).setVisible(isChecked);
    }

    public int getColumnIndex(String name) {
        int index = 0;
        for (Column column : columns) {
            if (name.equals(column.getName())) {
                return index;
            }
            index++;
        }
        return -1;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
        this.cleanStateHash = getColumnStateHash();
        notifyDataSetChanged();
    }

    public boolean isDirty() {
        return cleanStateHash != getColumnStateHash();
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

    @Override
    public void onViewAttachedToWindow(@NonNull ColumnSelectViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if (checkedChangeListener != null) {
            CheckBox checkBox = holder.itemView.findViewById(R.id.columns_list_item_state);
            checkBox.setOnCheckedChangeListener(checkedChangeListener);
        }
    }

    private int getColumnStateHash() {
        StringBuilder sb = new StringBuilder();
        for (Column column : columns) {
            sb.append(column.getName());
            sb.append(column.isVisible());
        }
        return sb.toString().hashCode();
    }

    private String cleanName(String name) {
        if (name.length() > 0 && name.charAt(0) == '(') {
            name = name.substring(1);
        }
        if (name.length() > 0 && name.charAt(name.length() - 1) == ')') {
            name = name.substring(0, name.length() - 1);
        }
        return name;
    }
}
