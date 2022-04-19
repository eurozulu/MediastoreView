package org.spoofer.mediastoreView.query;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public class UpdateQuery implements Query {
    private final ContentValues values = new ContentValues();

    private Uri source;
    private String where;
    private String[] whereArgs;

    public ContentValues getValues() {
        return values;
    }

    @Override
    public Uri getSource() {
        return source;
    }

    public void setSource(Uri source) {
        this.source = source;
    }

    @Override
    public String getWhereClause() {
        return where;
    }

    @Override
    public String[] getWhereArgs() {
        return whereArgs;
    }

    public void setWhereArgs(String[] whereArgs) {
        this.whereArgs = whereArgs;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    @Override
    public Cursor execute(ContentResolver resolver) {
        resolver.update(getSource(), getValues(),
                getWhereClause(), getWhereArgs());
        return null;
    }

}
