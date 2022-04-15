package org.spoofer.mediastoreView.query;

import android.content.ContentValues;
import android.content.Context;
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

    public void setWhere(String where) {
        this.where = where;
    }

    public void setWhereArgs(String[] whereArgs) {
        this.whereArgs = whereArgs;
    }

    @Override
    public Cursor execute(Context context) {
         context.getContentResolver().update(getSource(), getValues(),
                getWhereClause(), getWhereArgs());
         return null;
    }

}
