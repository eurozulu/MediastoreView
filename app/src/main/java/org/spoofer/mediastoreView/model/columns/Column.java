package org.spoofer.mediastoreView.model.columns;

import androidx.annotation.Nullable;

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
}
