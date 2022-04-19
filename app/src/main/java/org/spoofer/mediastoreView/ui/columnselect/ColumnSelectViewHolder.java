package org.spoofer.mediastoreView.ui.columnselect;

import android.view.View;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.spoofer.mediastoreView.R;
import org.spoofer.mediastoreView.model.table.Column;

public class ColumnSelectViewHolder extends RecyclerView.ViewHolder {

    private final CheckBox checkBox;

    public ColumnSelectViewHolder(@NonNull View itemView) {
        super(itemView);
        checkBox = (CheckBox) itemView.findViewById(R.id.columns_list_item);
    }

    public void setColumn(Column column) {
        if (checkBox != null) {
            checkBox.setVisibility(View.VISIBLE);
            checkBox.setChecked(column.isVisible());
        }
    }
}
