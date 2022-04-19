package org.spoofer.mediastoreView.model.table;

import java.util.ArrayList;
import java.util.List;

public class Row  {
    private static final String LOG = Row.class.getName();

    private final List<String> cells = new ArrayList<>();

    public void add(String value) {
        cells.add(value);
    }

    public String get(int index) {
        return cells.get(index);
    }
    public int size() {
        return cells.size();
    }
}
