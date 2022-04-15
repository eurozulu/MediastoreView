package org.spoofer.storerexplorer.ui.datagrid;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.spoofer.storerexplorer.model.DataRow;

import java.util.List;

/**
 * {@link DataGridViewModel} represents the current Gird data set.
 * A List of Titles define the number of columns in the grid and the 'names' of each column.
 * A List of DataRow's (String List) makes up the data set in the grid.
 */
public class DataGridViewModel extends ViewModel {

    private final MutableLiveData<List<String>> mTitles;
    private final MutableLiveData<List<DataRow>> mRows;

    public DataGridViewModel() {
        mTitles = new MutableLiveData<>();
        mRows = new MutableLiveData<>();
    }

    public LiveData<List<DataRow>> getRows() {
        return mRows;
    }

    public LiveData<List<String>> getTitles() {
        return mTitles;
    }

    public void setTitles(List<String> titles) {
        mTitles.setValue(titles);
    }

    public void setRows(List<DataRow> rows) {
        mRows.setValue(rows);
    }
}