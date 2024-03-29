package org.spoofer.mediastoreView.ui.columnselect;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.spoofer.mediastoreView.R;
import org.spoofer.mediastoreView.model.table.Column;

public class ColumnSelectViewHolder extends RecyclerView.ViewHolder {
    private final TextView textView;
    private final CheckBox checkBox;

    public ColumnSelectViewHolder(@NonNull View itemView) {
        super(itemView);
        textView = (TextView) itemView.findViewById(R.id.columns_list_item_name);
        checkBox = (CheckBox) itemView.findViewById(R.id.columns_list_item_state);
    }

    public void setColumn(Column column) {
        if (checkBox != null) {
            checkBox.setChecked(column.isVisible());
            checkBox.setText(String.format("(%s)", column.getName()));
        }
        if (textView != null) {
            textView.setText(column.getDisplayName());
        }
    }
}
