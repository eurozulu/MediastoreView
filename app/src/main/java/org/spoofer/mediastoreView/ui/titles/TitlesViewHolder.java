package org.spoofer.mediastoreView.ui.titles;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.spoofer.mediastoreView.R;
import org.spoofer.mediastoreView.model.columns.Column;

public class TitlesViewHolder extends RecyclerView.ViewHolder {

    private final TextView textView;

    public TitlesViewHolder(@NonNull View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.titles_list_item);
    }

    public void setColumn(Column column) {
        if (textView != null) {
            String name = column.getName();
            textView.setVisibility(View.VISIBLE);
            textView.setText(name);
            textView.setTooltipText(name);
            int width = column.getWidth() > 0
                    ? column.getWidth()
                    : (int) (textView.getPaint().measureText(name) * 1.5);
            column.setWidth(width);
            setLayoutWidth(textView, width);
        }
    }

    private void setLayoutWidth(View v, int width) {
        ViewGroup.LayoutParams params = v.getLayoutParams();
        params.width = width;
        v.setLayoutParams(params);
    }
}
