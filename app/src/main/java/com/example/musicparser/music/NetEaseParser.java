package com.example.musicparser.music;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class NetEaseParser implements IParser {
    private static final long KEY = 0xa3;

    public void parse(String inputPath, String outputPath, OnParseListener listener) {
        listener.onStart();
        File input = new File(inputPath);
        if (input.exists() && input.isFile()) {
            File output = new File(outputPath);
            FileInputStream inputStream = null;
            FileOutputStream outputStream = null;
            try {
                inputStream = new FileInputStream(input);
                outputStream = new FileOutputStream(output);
                byte[] bytes = new byte[1024];
                int len;
                while ((len = (inputStream.read(bytes))) != -1) {
                    for (int i = 0; i < len; i++) {
                        bytes[i] ^= KEY;
                    }
                    outputStream.write(bytes, 0, len);
                }
                listener.onFinish();
            } catch (Exception ex) {
                listener.onError(ex.getMessage());
            } finally {
                try {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                } catch (Exception ex) {
                    listener.onError(ex.getMessage());
                }
                try {
                    if (outputStream != null) {
                        outputStream.close();
                    }
                } catch (Exception ex) {
                    listener.onError(ex.getMessage());
                }
            }
        }
    }
}
