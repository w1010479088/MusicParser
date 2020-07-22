package com.example.musicparser.music;

import android.media.MediaMetadataRetriever;

import java.util.ArrayList;
import java.util.List;

public class NameParser {
    public List<String> parse(String path, int[] keys) {
        List<String> result = new ArrayList<>();
        try {
            MediaMetadataRetriever parser = new MediaMetadataRetriever();
            parser.setDataSource(path);
            for (int key : keys) {
                result.add(parser.extractMetadata(key));
            }
        } catch (Exception ex) {
            result.add(ex.getMessage());
        }
        return result;
    }
}
