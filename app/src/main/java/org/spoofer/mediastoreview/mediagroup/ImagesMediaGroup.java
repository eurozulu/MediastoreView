package org.spoofer.mediastoreview.mediagroup;

import android.provider.MediaStore;

import java.util.Arrays;
import java.util.List;

public class ImagesMediaGroup implements MediaGroup {

    private static final Query[] queries = new Query[]{
            new SimpleQuery(
                    MediaStore.Images.Media.class.getSimpleName(),
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI),

            new SimpleQuery(
                    MediaStore.Images.Thumbnails.class.getSimpleName(),
                    MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI),
    };

    @Override
    public String getGroupName() {
        return MediaStore.Images.class.getSimpleName();
    }

    @Override
    public List<Query> getQueries() {
        return Arrays.asList(queries);
    }
}
