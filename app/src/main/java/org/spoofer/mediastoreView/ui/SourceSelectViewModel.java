package org.spoofer.mediastoreView.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SourceSelectViewModel extends ViewModel {
    private final MutableLiveData<String> mGroupName;
    private final MutableLiveData<String> mSourceName;

    public SourceSelectViewModel() {
        mGroupName = new MutableLiveData<>();
        mSourceName = new MutableLiveData<>();
    }

    public LiveData<String> getGroupName() {
        return mGroupName;
    }

    public LiveData<String> getSourceName() {
        return mSourceName;
    }

    public void setGroupName(String groupName) {
        mGroupName.setValue(groupName);
    }

    public void setSourceName(String sourceName) {
        mSourceName.setValue(sourceName);
    }
}