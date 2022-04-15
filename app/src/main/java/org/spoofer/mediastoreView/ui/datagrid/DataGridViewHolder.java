package org.spoofer.mediastoreView.ui.datagrid;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

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

    public void setRowData(List<String> rowData) {
        int childCount = rowLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (i >= rowData.size()) {
                break;
            }
            TextView textView = ((TextView) rowLayout.getChildAt(i));
            textView.setText(rowData.get(i));
            textView.setTooltipText(rowData.get(i));
        }
    }

}
