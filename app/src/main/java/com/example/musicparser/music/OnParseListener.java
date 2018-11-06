package com.example.musicparser.music;

public interface OnParseListener {
    void onStart();

    void onFinish();

    void onError(String content);
}
