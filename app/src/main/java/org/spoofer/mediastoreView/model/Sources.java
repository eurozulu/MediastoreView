package org.spoofer.mediastoreView.model;

import android.os.Build;
import android.provider.MediaStore;

import androidx.annotation.NonNull;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Sources extends AbstractMap<String, List<Source>> {

    private final Set<Entry<String, List<Source>>> entries = buildEntries();

    @NonNull
    @Override
    public Set<Entry<String, List<Source>>> entrySet() {
        return entries;
    }

    private Set<Entry<String, List<Source>>> buildEntries() {
        Map<String, List<Source>> sources = new HashMap<>();
        sources.put("Audio", Arrays.asList(new Source[]{
                new Source("Media", MediaStore.Audio.Media.EXTERNAL_CONTENT_URI),
                new Source("Albums", MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI),
                new Source("Artists", MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI),
                new Source("Playlists", MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI),
                new Source("Genres", MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI),
        }));
        sources.put("Video", Arrays.asList(new Source[]{
                new Source("Media", MediaStore.Video.Media.EXTERNAL_CONTENT_URI),
                new Source("Thumbnails", MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI),
        }));

        sources.put("Images", Arrays.asList(new Source[]{
                new Source("Media", MediaStore.Images.Media.EXTERNAL_CONTENT_URI),
                new Source("Thumbnails", MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI),
        }));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            sources.put("Downloads", Arrays.asList(new Source[]{
                    new Source("Media", MediaStore.Downloads.EXTERNAL_CONTENT_URI),
            }));
        }

        sources.put("All Files", Arrays.asList(new Source[]{
                new Source("Files", MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY))
        }));
        return sources.entrySet();
    }
}
