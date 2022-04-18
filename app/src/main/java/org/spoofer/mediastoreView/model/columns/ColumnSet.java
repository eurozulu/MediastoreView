package org.spoofer.mediastoreView.model.columns;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ColumnSet {
    private final List<Column> columns;

    public ColumnSet(Context context, Uri source) {
        columns = buildColumns(context, source);
    }

    public List<Column> getColumns() {
        return columns;
    }

    public List<Column> getVisibleColumns() {
        return columns.stream()
                .filter(Column::isVisible)
                .collect(Collectors.toList());
    }

    public Column getColumn(String name) {
        for (Column column : columns) {
            if (column.getName().equals(name)) {
                return column;
            }
        }
        return null;
    }

    private List<Column> buildColumns(Context context, Uri src) {
        Cursor cursor = context.getContentResolver().query(src, null,
                MediaStore.MediaColumns._ID + " < 0", null,
                null);
        if (cursor == null) {
            return Collections.emptyList();
        }
        try {
            // build list of columns for this source
            List<Column> columns = Arrays.stream(cursor.getColumnNames())
                    .map(Column::new)
                    .collect(Collectors.toList());
            // apply any relevant saved columns to the list
            return new PersistentColumns(context).applySavedColumns(columns);
        } finally {
            cursor.close();
        }
    }


}
