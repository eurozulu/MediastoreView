package org.spoofer.mediastoreView.query;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class SelectQuery implements Query {
    private static final String LOG = SelectQuery.class.getName();

    private final List<String> fields = new ArrayList<>();
    private Uri source;

    private String where;
    private String[] whereArgs;

    private String soryBy;

    public List<String> getFields() {
        return fields;
    }

    public void setField(List<String> fields) {
        this.fields.clear();
        this.fields.addAll(fields);
    }

    public String getSoryBy() {
        return soryBy;
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

    @NonNull
    @Override
    public String toString() {
        CharSequence s = TextUtils.concat(QueryType.SELECT.toString(), " ",
                (!fields.isEmpty() ? fields.toString() : "*"), " FROM ", source.toString());
        if (!TextUtils.isEmpty(where)) {
            s = TextUtils.concat(s, " WHERE ", where, " (", whereArgs.toString(), ")");
        }
        return s.toString();
    }

    @Override
    public Cursor execute(ContentResolver resolver) {
        Log.d(LOG, String.format("executing query '%s'", this));
        return resolver.query(getSource(), getFields().toArray(new String[0]),
                getWhereClause(), getWhereArgs(), getSoryBy());
    }
}
