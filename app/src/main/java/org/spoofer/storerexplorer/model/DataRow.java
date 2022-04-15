package org.spoofer.storerexplorer.model;

import android.database.Cursor;
import android.util.Log;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

public class DataRow extends AbstractList<String> {
    private static final String LOG = DataRow.class.getName();

    public static final String NULL_TOKEN = "NULL";

    private static final int MAX_ROWS = 30;
    private static final int MAX_COLS = 25;

    private final List<String> values;

    public DataRow(List<String> values) {
        this.values = values;
    }

    @Override
    public String get(int index) {
        return values.get(index);
    }

    @Override
    public int size() {
        return values.size();
    }

    public static List<DataRow> parseDataRows(Cursor cursor) {
        Log.d(LOG, String.format("Parsing row %d", cursor.getCount()));
        int colCount = cursor.getColumnCount();
        List<DataRow> rows = new ArrayList<>();
        try {
            while (cursor.moveToNext()) {
                List<String> row = new ArrayList<>();
                for (int i = 0; i < colCount; i++) {
                    row.add(readCursorValue(cursor, i));
                }
                rows.add(new DataRow(row));
                if (rows.size() >= MAX_ROWS) {
                    break;
                }
            }
        } finally {
            cursor.close();
        }
        return rows;
    }

    private static String readCursorValue(Cursor cursor, int index) {
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
