package org.spoofer.mediastoreView.ui.datagrid;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.spoofer.mediastoreView.model.table.Row;

/**
 * {@link DataGridViewHolder} holds a reference to a pre build 'row' of TextViews.
 * The Row is a LinearLayout containg aTextView for each column in the grid.
 */
public class DataGridViewHolder extends RecyclerView.ViewHolder {
    //private static final String LOG = DataGridViewHolder.class.getName();

    private LinearLayout rowLayout;

    public DataGridViewHolder(@NonNull View itemView) {
        super(itemView);
        rowLayout = (LinearLayout) itemView;
    }

    public void setRowData(Row rowData) {
        int childCount = rowLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            TextView textView = ((TextView) rowLayout.getChildAt(i));
            if (i >= rowData.size()) {
                textView.setVisibility(View.GONE);
                continue;
            }

            textView.setVisibility(View.VISIBLE);
            String value = rowData.get(i);
            textView.setText(value);
            textView.setTooltipText(value);
        }
    }

}
