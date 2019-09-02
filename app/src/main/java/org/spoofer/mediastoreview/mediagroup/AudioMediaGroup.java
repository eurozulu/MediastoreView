package org.spoofer.mediastoreview.mediagroup;

import android.provider.MediaStore;

import java.util.Arrays;
import java.util.List;

public class AudioMediaGroup implements MediaGroup {

    private static final Query[] queries = new Query[]{
            new SimpleQuery(
                    MediaStore.Audio.Playlists.class.getSimpleName(),
                    MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI),

            new SimpleQuery(
                    MediaStore.Audio.Albums.class.getSimpleName(),
                    MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI),

            new SimpleQuery(
                    MediaStore.Audio.Artists.class.getSimpleName(),
                    MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI),

            new SimpleQuery(
                    MediaStore.Audio.Genres.class.getSimpleName(),
                    MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI),

            new SimpleQuery(
                    MediaStore.Audio.Media.class.getSimpleName(),
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI),
    };

    @Override
    public String getGroupName() {
        return MediaStore.Audio.class.getSimpleName();
    }

    @Override
    public List<Query> getQueries() {
        return Arrays.asList(queries);
    }
}
