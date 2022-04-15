package org.spoofer.mediastoreView.model;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import org.spoofer.mediastoreView.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TitleColumns {
    private final Map<String, Integer> fixedWidths = new HashMap<>();
    private final ViewGroup titleRow;
    private int[]setWidths = new int[0];

    public TitleColumns(ViewGroup titleRow) {
        this.titleRow = titleRow;
    }

    public int getColumnCount() {
        return titleRow.getChildCount();
    }

    public int getColumnWidth(int index) {
        return index < 0 || index >= setWidths.length
                ? 0 : setWidths[index];
    }
    public void reset() {
        titleRow.removeAllViews();
        setWidths = new int[0];
    }

    public void updateTitles(List<String> titles) {
        LayoutInflater inflater = LayoutInflater.from(titleRow.getContext());
        reset();

        int colCount = titles.size();
        setWidths = new int[colCount];
        for (int i = 0; i < colCount; i++) {
            String txt = titles.get(i);
            TextView cell = (TextView) inflater.inflate(R.layout.cell_title, titleRow, false);
            ViewGroup.LayoutParams params = cell.getLayoutParams();
            // Get col width by name (presaved width or WRAP_CONTENT)
            params.width = getColumnWidth(txt);
            if (params.width == 0) {
                params.width = (int)(cell.getPaint().measureText(txt) * 1.2);
            }
            cell.setLayoutParams(params);
            cell.setText(txt);
            cell.setTooltipText(txt);
            titleRow.addView(cell);
            setWidths[i] = params.width;
        }
    }

    public int getColumnWidth(String name) {
        Integer width = fixedWidths.get(name);
        return width != null ? width : 0;
    }

}
