package org.spoofer.mediastoreView.model.columns;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * {@link PersistentColumns} represents the columns which have been saved into preferences.
 * Any column choosen by the user as visible,
 */
public class PersistentColumns {
    public static final String[] DEFAULT_COLUMN_NAMES = new String[]{
            MediaStore.MediaColumns._ID,
            MediaStore.MediaColumns.TITLE,
            MediaStore.MediaColumns.DISPLAY_NAME,
            MediaStore.MediaColumns.DATA,
            MediaStore.MediaColumns.DATE_ADDED,
            MediaStore.MediaColumns.DATE_MODIFIED,
    };
    private static final String PREFERENCE_NAME = PersistentColumns.class.getName();
    private static final String PREFERENCE_KEY = "COLUMN_NAMES";
    private final Context context;
    private final SharedPreferences preferences;

    public PersistentColumns(Context context) {
        this.context = context;
        this.preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public List<Column> getSavedColumns() {
        return preferences.getStringSet(PREFERENCE_KEY, getDefaultColumns()).stream()
                .map(this::parseNewColumn)
                .collect(Collectors.toList());
    }

    public void putSavedColumns(List<Column> columns) {
        Set<String> savedCols = mergeSavedColumns(columns).stream()
                .map(this::serialiseColumn)
                .collect(Collectors.toSet());

        preferences.edit()
                .putStringSet(PREFERENCE_KEY, savedCols)
                .apply();
    }

    /**
     * replaces any column in the given list with the saved column of the same name.
     * If a saved name exists it is replaced in the list, otherwise the given column is left in the list.
     *
     * @param columns the columns to replace
     * @return the given columns with saved column matching being replaced in the list.
     */
    public List<Column> applySavedColumns(List<Column> columns) {
        List<Column> savedCols = getSavedColumns();
        return columns.stream()
                .map(column -> {
                    int index = savedCols.indexOf(column);
                    return index >= 0
                            ? savedCols.get(index)
                            : column;
                })
                .collect(Collectors.toList());
    }

    public void clearSavedColumns() {
        preferences.edit()
                .remove(PREFERENCE_KEY)
                .apply();
    }

    // Serialse column tacks the column width on the end of its name, delimited with a colon.
    private String serialiseColumn(Column column) {
        return TextUtils.concat(column.getName(), ":",
                Integer.toString(column.getWidth())).toString();
    }

    private Column parseNewColumn(String columnName) {
        int width = 0;
        int colPos = columnName.lastIndexOf(':');
        if (colPos >= 0 && colPos < (columnName.length() - 1)) {
            try {
                width = Integer.parseInt(columnName.substring(colPos + 1));
                columnName = columnName.substring(0, colPos);
            } catch (NumberFormatException e) {
                e.printStackTrace(); // ignore and carry on regardless!
            }
        }
        return new Column(columnName, width, true);
    }

    private Set<String> getDefaultColumns() {
        return new HashSet<>(Arrays.asList(DEFAULT_COLUMN_NAMES));
    }

    /**
     * Merge the given Columns into the saved columns.
     * All visible columns in given column set are added to saved.
     * non visible columns are ignored unless already in existing saved columns, in which case they are removed.
     *
     * @param columns
     * @return
     */
    private List<Column> mergeSavedColumns(List<Column> columns) {
        List<Column> savedCols = getSavedColumns();
        for (Column col : columns) {
            int index = savedCols.indexOf(col);
            // remove any existing
            if (index >= 0) {
                savedCols.remove(index);
            }
            if (!col.isVisible()) {
                // no visible, ignore it
                continue;
            }
            savedCols.add(col);
        }
        return savedCols;
    }
}
