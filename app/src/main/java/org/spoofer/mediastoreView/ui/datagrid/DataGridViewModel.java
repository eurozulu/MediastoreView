package org.spoofer.mediastoreView.ui.datagrid;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.spoofer.mediastoreView.model.DataRow;
import org.spoofer.mediastoreView.model.columns.ColumnSet;

import java.util.List;

/**
 * {@link DataGridViewModel} represents the current Gird data set.
 * A List of Titles define the number of columns in the grid and the 'names' of each column.
 * A List of DataRow's (String List) makes up the data set in the grid.
 */
public class DataGridViewModel extends ViewModel {

    private final MutableLiveData<ColumnSet> mColumns;
    private final MutableLiveData<List<DataRow>> mRows;

    public DataGridViewModel() {
        mColumns = new MutableLiveData<>();
        mRows = new MutableLiveData<>();
    }

    public LiveData<List<DataRow>> getRows() {
        return mRows;
    }

    public void setRows(List<DataRow> rows) {
        mRows.setValue(rows);
    }

    public LiveData<ColumnSet> getColumns() {
        return mColumns;
    }

    public void setColumns(ColumnSet columns) {
        mColumns.setValue(columns);
    }
}