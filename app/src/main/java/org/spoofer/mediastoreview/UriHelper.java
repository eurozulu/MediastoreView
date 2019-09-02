package org.spoofer.mediastoreview;

import android.net.Uri;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class UriHelper {

    public static Uri getParent(Uri uri) {
        if (TextUtils.isEmpty(uri.getPath()))
            return uri;

        Uri.Builder builder = new Uri.Builder()
                .scheme(uri.getScheme())
                .authority(uri.getAuthority());

        List<String> path = uri.getPathSegments();
        int size = path.size() - 1;
        int index = 0;
        while (index < size) {
            builder.appendPath(path.get(index));
            index++;
        }
        builder.fragment(uri.getFragment())
                .query(uri.getQuery());

        return builder.build();
    }

    public static Uri addQueryToUri(Uri base, String key, String value) {
        Uri.Builder builder = new Uri.Builder()
                .scheme(base.getScheme())
                .authority(base.getAuthority())
                .path(base.getPath());

        if (!TextUtils.isEmpty(base.getQuery()))
            for (String name : base.getQueryParameterNames()) {
                if (name.equals(key))
                    continue;
                builder.appendQueryParameter(name, base.getQueryParameter(name));
            }

        if (!TextUtils.isEmpty(key))
            builder.appendQueryParameter(key, value);

        builder.fragment(base.getFragment());

        return builder.build();
    }

    public static Uri removeQueryFromUri(Uri base, String key) {

        if (TextUtils.isEmpty(base.getQuery()))
            return base;

        Uri.Builder builder = new Uri.Builder()
                .scheme(base.getScheme())
                .authority(base.getAuthority())
                .path(base.getPath());

        for (String name : base.getQueryParameterNames()) {
            if (name.equals(key))
                continue;
            builder.appendQueryParameter(name, base.getQueryParameter(name));
        }

        builder.fragment(base.getFragment());

        return builder.build();
    }

    public static String getWhereFromQuery(Uri uri) {
        String where = null;

        if (!TextUtils.isEmpty(uri.getQuery())) {
            StringBuilder sb = new StringBuilder();
            for (String field : uri.getQueryParameterNames()) {
                if (sb.length() > 0)
                    sb.append(" AND ");
                sb.append(field);
                if (!field.contains("?"))
                    sb.append("=?");
            }

            where = sb.toString();
        }
        return where;
    }

    public static String[] getWhereArgsFromQuery(Uri uri) {
        String[] whereArgs = null;

        if (!TextUtils.isEmpty(uri.getQuery())) {
            List<String> values = new ArrayList<>();
            for (String field : uri.getQueryParameterNames()) {
                String value = uri.getQueryParameter(field);
                if (!TextUtils.isEmpty(value) &&
                        value.startsWith("[") && value.endsWith("]")) {
                    String[] vals = value.substring(1, value.length() - 1).split(",");
                    for (String s : vals)
                        values.add(s.trim());
                } else
                    values.add(value);
            }

            whereArgs = new String[values.size()];
            values.toArray(whereArgs);
        }
        return whereArgs;
    }


}
