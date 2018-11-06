package com.example.musicparser.music;

import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;

public class KuGouParser implements IParser {

    @Override
    public void parse(String inputPath, String outputPath, OnParseListener listener) {
        listener.onStart();
        int[] keys = {0xAC, 0xEC, 0xDF, 0x57};
        File input = new File(inputPath);
        if (input.exists() && input.isFile()) {
            File output = new File(outputPath);
            RandomAccessFile inputStream = null;
            FileOutputStream outputStream = null;
            try {
                inputStream = new RandomAccessFile(input, "rwd");
                inputStream.seek(1024);
                outputStream = new FileOutputStream(output);
                byte[] bytes = new byte[keys.length];
                int len;
                while ((len = inputStream.read(bytes, 0, bytes.length)) != -1) {
                    for (int i = 0; i < len; i++) {

                        int k = keys[i];
                        int kh = k >> 4;
                        int kl = k & 0xf;
                        int b = bytes[i];
                        int low = b & 0xf ^ kl;
                        int high = (b >> 4) ^ kh ^ low & 0xf;
                        bytes[i] = (byte) (high << 4 | low);
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
