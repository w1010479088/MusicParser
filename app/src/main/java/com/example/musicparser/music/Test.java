package com.example.musicparser.music;

public class Test {
    public static void main1(String[] args) {
        new KuGouParser().parse("tempFile/新的心跳-加密版.kge", "tempFile/新的心跳-加密版-待测.mp3", new OnParseListener() {
            @Override
            public void onStart() {
                log("解码开始...");
            }

            @Override
            public void onFinish() {
                log("解码结束!");
            }

            @Override
            public void onError(String content) {
                log(content);
            }
        });
    }

    public static void main(String[] args) {
        new NetEaseParser().parse("tempFile/倒数-加密.uc", "tempFile/倒数-解密.mp3", new OnParseListener() {
            @Override
            public void onStart() {
                log("任务开始!");
            }

            @Override
            public void onFinish() {
                log("正常解析完毕!");
            }

            @Override
            public void onError(String content) {
                log(content);
            }
        });
    }

    private static void log(String content) {
        //ignore
    }
}
