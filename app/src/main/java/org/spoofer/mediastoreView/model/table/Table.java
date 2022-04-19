package org.spoofer.mediastoreView.model.table;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import org.spoofer.mediastoreView.query.Query;
import org.spoofer.mediastoreView.query.SelectQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Table {
    private static final String LOG = Table.class.getName();
    public static final String NULL_TOKEN = "-";

    private final ContentResolver contentResolver;
    private final PersistentColumns savedColumns;

    private final List<Column> columns = new ArrayList<>();

    private int maxRows = 0;

    private Uri query;

    public Table(Context context) {
        this.contentResolver = context.getContentResolver();
        this.savedColumns = new PersistentColumns(context);
    }

    public int getMaxRows() {
        return maxRows;
    }

    public void setMaxRows(int maxRows) {
        this.maxRows = maxRows;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setQuery(Uri source) {
        this.query = source;
        setColumns();
    }

    public List<String> getVisibleColumnNames() {
        return columns.stream().filter(Column::isVisible).map(Column::getName).collect(Collectors.toList());
    }

    public void clear() {
        this.query = null;
        this.columns.clear();
    }

    public void resetAllColumns() {
        savedColumns.clearSavedColumns();
    }

    private void setColumns() {
        columns.clear();
        List<Column> saved = savedColumns.getColumns(query.toString());
        for (String name : getColumnNames()) {
            // If name is known in saved, use that, otherwise create invisible column.
            int i = indexOfColumn(name, saved);
            Column col;
            if (i >= 0) {
                col = saved.get(i);
            } else {
                col = new Column(name, 0, false);
            }
            columns.add(col);
        }
    }

    public List<Row> getRows() {
        List<Row> rows = new ArrayList<>();
        // Execute source query to populate datagrid
        SelectQuery selectQuery = Query.of(Query.QueryType.SELECT);
        selectQuery.setSource(query);
        selectQuery.setField(getVisibleColumnNames());
        Cursor cursor = selectQuery.execute(contentResolver);
        if (cursor == null) {
            return null;
        }
        Log.d(LOG, String.format("Parsing row %d", cursor.getCount()));
        int colCount = cursor.getColumnCount();
        try {
            while (cursor.moveToNext()) {
                Row row = new Row();
                for (int i = 0; i < colCount; i++) {
                    row.add(readCursorValue(cursor, i));
                }
                rows.add(row);
                if (maxRows > 0 && maxRows <= rows.size()) {
                    break;
                }
            }
        } finally {
            Log.d(LOG, String.format("query complete with: %d results", rows.size()));
            cursor.close();
        }
        return rows;
    }

    private int indexOfColumn(String name, List<Column> columns) {
        int index = 0;
        for (; index < columns.size(); index++) {
            if (name.equals(columns.get(index).getName())) {
                return index;
            }
        }
        return -1;
    }

    private List<String> getColumnNames() {
        Cursor cursor = contentResolver.query(query, null, "_id < 0", null, null);
        if (cursor == null) {
            return null;
        }
        try {
            return Arrays.asList(cursor.getColumnNames());
        } finally {
            cursor.close();
        }
    }

    private String readCursorValue(Cursor cursor, int index) {
        switch (cursor.getType(index)) {
            case Cursor.FIELD_TYPE_STRING:
                return cursor.getString(index);
            case Cursor.FIELD_TYPE_INTEGER:
                return Integer.toString(cursor.getInt(index));
            case Cursor.FIELD_TYPE_FLOAT:
                return Float.toString(cursor.getFloat(index));
            case Cursor.FIELD_TYPE_BLOB:
                return new String(cursor.getBlob(index));
            case Cursor.FIELD_TYPE_NULL:
                return NULL_TOKEN;
            default:
                return "";
        }
    }
}
