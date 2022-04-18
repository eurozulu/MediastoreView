package org.spoofer.mediastoreView.model;

import java.util.AbstractList;
import java.util.List;

public class DataRow extends AbstractList<String> {
    private static final String LOG = DataRow.class.getName();

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

}
