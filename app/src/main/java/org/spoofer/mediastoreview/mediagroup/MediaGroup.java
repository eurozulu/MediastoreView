package org.spoofer.mediastoreview.mediagroup;

import android.net.Uri;
import android.os.Parcelable;

import java.util.List;

public interface MediaGroup {

    MediaGroup[] MEDIA_GROUPS = new MediaGroup[] {
        new AudioMediaGroup(),
        new FilesMediaGroup(),
        new ImagesMediaGroup(),
        new VideoMediaGroup()
    };

    String getGroupName();
    List<Query> getQueries();

    interface Query extends Parcelable {
        String getName();
        Uri getQuery();
    }

}
