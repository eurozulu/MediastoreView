package org.spoofer.mediastoreview.mediagroup;

import android.provider.MediaStore;

import java.util.Arrays;
import java.util.List;

public class VideoMediaGroup implements MediaGroup {

    private static final Query[] queries = new Query[]{
            new SimpleQuery(
                    MediaStore.Video.Media.class.getSimpleName(),
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI),

            new SimpleQuery(
                    MediaStore.Video.Thumbnails.class.getSimpleName(),
                    MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI),
    };

    @Override
    public String getGroupName() {
        return MediaStore.Video.class.getSimpleName();
    }

    @Override
    public List<Query> getQueries() {
        return Arrays.asList(queries);
    }
}
