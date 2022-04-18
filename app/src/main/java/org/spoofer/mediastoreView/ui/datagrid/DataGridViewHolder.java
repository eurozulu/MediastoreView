package org.spoofer.mediastoreView.ui.datagrid;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.spoofer.mediastoreView.model.columns.Column;

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

    public void setRowData(List<Column> columns, List<String> rowData) {
        int childCount = rowLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            TextView textView = ((TextView) rowLayout.getChildAt(i));
            if (i >= rowData.size() || i >= columns.size()) {
                textView.setVisibility(View.GONE);
                continue;
            }

            textView.setVisibility(View.VISIBLE);
            String value = rowData.get(i);
            textView.setText(value);
            textView.setTooltipText(value);
            setLayoutWidth(textView, columns.get(i).getWidth());
        }
    }


    private void setLayoutWidth(View v, int width) {
        ViewGroup.LayoutParams params = v.getLayoutParams();
        params.width = width;
        v.setLayoutParams(params);
    }
}
