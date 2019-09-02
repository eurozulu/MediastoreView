package org.spoofer.mediastoreview;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Result implements Iterable<List<String>> {


    private Cursor cursor;
    private List<String> titles;

    public Result(Cursor cursor) {
        this.cursor = cursor;
        if (null != cursor)
            titles = Arrays.asList(cursor.getColumnNames());
    }

    public List<String> getTitles() {
        return titles;
    }


    @Override
    public Iterator<List<String>> iterator() {
        return new Iterator<List<String>>() {

            private List<String> next;

            @Override
            public boolean hasNext() {
                if (null == next)
                    next = readNext();
                return null != next;
            }

            @Override
            public List<String> next() {
                List<String> n = hasNext() ? next : null;
                next = null;
                return n;
            }
        };
    }

    private List<String> readNext() {
        if (null == cursor || cursor.isClosed())
            return null;

        if (!cursor.moveToNext()) {
            cursor.close();
            cursor = null;
            return null;
        }

        List<String> row = new ArrayList<>();
        int count = cursor.getColumnCount();
        for (int index = 0; index < count; index++)
            row.add(cursor.getString(index));
        return row;
    }

}
