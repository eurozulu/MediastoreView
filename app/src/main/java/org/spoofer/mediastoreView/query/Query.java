package org.spoofer.mediastoreView.query;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

public interface Query {
    static <Q extends Query> Q of(QueryType queryType) {
        switch (queryType) {
            case SELECT:
                return (Q) new SelectQuery();
            case UPDATE:
                return (Q) new UpdateQuery();
        }
        return null;
    }

    Uri getSource();

    String getWhereClause();

    String[] getWhereArgs();

    Cursor execute(ContentResolver resolver);

    enum QueryType {
        SELECT("select"),
        INSERT("insert"),
        UPDATE("update"),
        DELETE("delete"),
        ;

        private final String name;

        QueryType(String name) {
            this.name = name;
        }
    }
}
