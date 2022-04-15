package org.spoofer.storerexplorer.model;

import android.net.Uri;

/**
 * Source represents a single location in the MediaStore. (A table or view)
 */
public class Source {
    private final String name;
    private final Uri location;

    public Source(String name, Uri location) {
        this.name = name;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public Uri getLocation() {
        return location;
    }
}
