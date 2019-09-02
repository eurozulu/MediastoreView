package org.spoofer.mediastoreview.mediagroup;

import android.provider.MediaStore;

import java.util.Arrays;
import java.util.List;

public class FilesMediaGroup implements MediaGroup {

    private static final Query[] queries = new Query[]{
            new SimpleQuery(
                    MediaStore.Files.class.getSimpleName(),
                    MediaStore.Files.getContentUri("external")),
    };

    @Override
    public String getGroupName() {
        return MediaStore.Files.class.getSimpleName();
    }

    @Override
    public List<Query> getQueries() {
        return Arrays.asList(queries);
    }
}
