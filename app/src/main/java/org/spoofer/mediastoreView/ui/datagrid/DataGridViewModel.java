package org.spoofer.mediastoreView.ui.datagrid;

import android.content.Context;
import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.spoofer.mediastoreView.model.table.Column;
import org.spoofer.mediastoreView.model.table.Row;
import org.spoofer.mediastoreView.model.table.Table;

import java.util.List;

public class DataGridViewModel extends ViewModel {

    private final MutableLiveData<List<Column>> mColumns;
    private final MutableLiveData<List<Row>> mRows;

    public DataGridViewModel() {
        mColumns = new MutableLiveData<>();
        mRows = new MutableLiveData<>();
    }

    public LiveData<List<Column>> getColumns() {
        return mColumns;
    }

    public LiveData<List<Row>> getRows() {
        return mRows;
    }


    public void setSource(Context context, Uri src) {
        Table table = new Table(context);
        table.setQuery(src);
        mColumns.setValue(table.getColumns());
        mRows.setValue(table.getRows());
    }
}
