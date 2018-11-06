package com.example.musicparser.music;

public interface IParser {

    void parse(String inputPath, String outputPath, OnParseListener listener);
}
