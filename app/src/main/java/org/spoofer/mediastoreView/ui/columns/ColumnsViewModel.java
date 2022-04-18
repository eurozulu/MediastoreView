package org.spoofer.mediastoreView.ui.columns;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.spoofer.mediastoreView.model.Source;
import org.spoofer.mediastoreView.model.columns.ColumnSet;

/**
 * {@link ColumnsViewModel} represents the Set of Columns
 */
public class ColumnsViewModel extends ViewModel {

    private final MutableLiveData<Source> mSource;
    private final MutableLiveData<ColumnSet> mColumns;

    public ColumnsViewModel() {
        mSource = new MutableLiveData<>();
        mColumns = new MutableLiveData<>();
    }

    public LiveData<ColumnSet> getColumns() {
        return mColumns;
    }

    public LiveData<Source> getSource() {
        return mSource;
    }

    public void setSource(Context context, Source source) {
        mSource.setValue(source);
        mColumns.setValue(new ColumnSet(context, source.getLocation()));
    }

}