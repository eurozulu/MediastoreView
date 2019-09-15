package org.spoofer.mediastoreview;

import android.content.ContentResolver;
import android.net.Uri;
import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.spoofer.mediastoreview.mediagroup.MediaGroup;
import org.spoofer.mediastoreview.mediagroup.SimpleQuery;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TableViewModel extends ViewModel {

    public static final String PARAM_ORDER_BY = "order_by";

    private final MutableLiveData<List<String>> titles = new MutableLiveData<>();
    private final MutableLiveData<List<List<String>>> rows = new MutableLiveData<>();

    private final MutableLiveData<MediaGroup.Query> query = new MutableLiveData<>();

    private final ContentResolver contentResolver;

    public TableViewModel(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    public void setQuerySortField(String fieldName) {
        MediaGroup.Query query = this.query.getValue();
        if (query == null)
            return;
        Uri sortedQuery = UriHelper.addQueryToUri(query.getQuery(), PARAM_ORDER_BY, fieldName);
        setQuery(new SimpleQuery(query.getName(), sortedQuery));
    }

    public void setQuery(MediaGroup.Query query) {
        if (null == query)
            return;
        MediaGroup.Query current = this.query.getValue();

        /*
        if (null != current && current.getQuery().compareTo(
                query.getQuery()) == 0) // nochange
            return;
        */

        this.query.setValue(query);
        openQuery(query.getQuery());
    }

    public LiveData<MediaGroup.Query> getQuery() {
        return query;
    }

    public LiveData<List<List<String>>> getRows() {
        return rows;
    }

    public List<String> getRow(int id) {
        List<List<String>> rows = this.rows.getValue();
        if (null == rows)
            return null;

        List<String> foundRow = null;

        for (List<String> row : rows) {
            if (row.size() > 0) {
                try {
                    int _id = Integer.parseInt(row.get(0));
                    if (_id == id) {
                        foundRow = row;
                        break;
                    }
                } catch (NumberFormatException e) {

                }
            }
        }
        return foundRow;
    }


    public LiveData<List<String>> getTitles() {
        return titles;
    }


    private void openQuery(Uri query) {

        String orderBy = query.getQueryParameter(PARAM_ORDER_BY);
        if (!TextUtils.isEmpty(orderBy))
            query = UriHelper.removeQueryFromUri(query, PARAM_ORDER_BY);

        String where = UriHelper.getWhereFromQuery(query);
        String[] whereArgs = UriHelper.getWhereArgsFromQuery(query);

        Result result = new Result(contentResolver.query(query,
                null,
                where, whereArgs,
                orderBy));

        titles.setValue(result.getTitles());
        rows.setValue(readRows(result));
    }

    private List<List<String>> readRows(Result result) {
        List<List<String>> rows = new ArrayList<>();
        Iterator<List<String>> iterator = result.iterator();
        while (iterator.hasNext())
            rows.add(iterator.next());
        return rows;
    }
}
