package org.spoofer.mediastoreView.model.table;

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
            MediaStore.MediaColumns.ALBUM,
            MediaStore.MediaColumns.ARTIST,
            MediaStore.MediaColumns.ALBUM_ARTIST,
            MediaStore.MediaColumns.DATA,
            MediaStore.MediaColumns.DATE_ADDED,
            MediaStore.MediaColumns.DATE_MODIFIED,
    };
    private static final String PREFERENCE_NAME = PersistentColumns.class.getName();
    private static final String PREFERENCE_KEY = "COLUMN_NAMES_";

    private final SharedPreferences preferences;

    public PersistentColumns(Context context) {
        this.preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public List<Column> getColumns(String key) {
        return preferences.getStringSet(PREFERENCE_KEY + key, getDefaultColumns()).stream()
                .map(this::parseNewColumn)
                .collect(Collectors.toList());
    }

    public void saveColumns(String key, List<Column> columns) {
        Set<String> savedCols = columns.stream()
                .filter(Column::isVisible)
                .map(this::serialiseColumn)
                .collect(Collectors.toSet());

        preferences.edit()
                .putStringSet(PREFERENCE_KEY + key, savedCols)
                .apply();
    }

    public void clearSavedColumns() {
        preferences.edit()
                .clear()
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

}
