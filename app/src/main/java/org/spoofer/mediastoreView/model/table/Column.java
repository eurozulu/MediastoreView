package org.spoofer.mediastoreView.model.table;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import java.util.Locale;

public class Column {
    private final String name;
    private int width = 0;
    private boolean visible = false;

    public Column(String name) {
        this.name = name;
    }

    public Column(String name, int width, boolean visible) {
        this.name = name;
        this.width = width;
        this.visible = visible;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return toCamelCase(name.replaceAll("_", " ").trim());
    }


    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof Column)) {
            return false;
        }
        Column other = (Column) obj;
        return other.getName().equals(name);
    }

    private String toCamelCase(String s) {
        StringBuilder sb= new StringBuilder();
        for (String word : s.split("[\\W_]+")) {
            if (sb.length() > 0) {
                sb.append(" ");
            }
            if (word.length() == 0) {
                continue;
            }
            sb.append(Character.toUpperCase(word.charAt(0)));
            if (word.length() > 1) {
                sb.append(word.substring(1).toLowerCase(Locale.ROOT));
            }
        }
        return sb.toString();
    }
}
