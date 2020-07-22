package com.example.musicparser;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.TextView;

import com.example.musicparser.music.NetEaseParser;
import com.example.musicparser.music.OnParseListener;
import com.example.musicparser.util.IOUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends AppCompatActivity {
    public static final String PERMISSION_READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    public static final String PERMISSION_WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private static final String INPUT_PATH = Environment.getExternalStorageDirectory().getPath() + "/netease/cloudmusic/Cache/Music1/";
    private static final String OUTPUT_PATH = Environment.getExternalStorageDirectory().getPath() + "/网易云音乐解码/";
    private static final String SAVE_TIP = "解码之后的文件放在:\"网易云音乐解码\"的文件夹里.\n\n";
    private StringBuilder mLog = new StringBuilder(SAVE_TIP);
    private static final int MIN_SIZE = 2 * 1024 * 1024;
    private static final String END_FIX = ".uc!";
    private boolean mPermissionChecked;
    private boolean mPermissionGranted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_1).setOnClickListener(v -> run());
        findViewById(R.id.btn_2).setOnClickListener(v -> delCache());
        findViewById(R.id.btn_3).setOnClickListener(v -> clearLog());
        findViewById(R.id.btn_4).setOnClickListener(v -> openDir());
    }

    private void run() {
        if (!mPermissionGranted) return;
        List<String> paths = listFile(false);
        if (paths.isEmpty()) {
            log("未发现可解码的缓存文件!");
        } else {
            File outFile = new File(OUTPUT_PATH);
            IOUtil.createDir(outFile);
            for (int i = 0; i < paths.size(); i++) {
                parseFile(paths.get(i), createPath(i));
            }
        }
    }

    private void delCache() {
        if (!mPermissionGranted) return;
        List<String> paths = listFile(true);
        if (paths.isEmpty()) {
            log("未发现可删除文件!");
        } else {
            for (String path : paths) {
                IOUtil.delete(new File(path));
                log("删除:" + path);
            }
            log("缓存删除完毕!");
        }
    }

    private void clearLog() {
        mLog.delete(SAVE_TIP.length(), mLog.length());
        log("");
    }

    private List<String> listFile(boolean allFile) {
        List<String> list = new ArrayList<>();
        File dir = new File(INPUT_PATH);
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null && files.length > 0) {
                for (File file : files) {
                    if (allFile) {
                        list.add(file.getAbsolutePath());
                    } else {
                        if (needFile(file)) {
                            list.add(file.getAbsolutePath());
                        }
                    }
                }
            }
        }
        return list;
    }

    private void parseFile(String input, final String output) {
        new NetEaseParser().parse(input, output, new OnParseListener() {
            @Override
            public void onStart() {
                log("开始解码:" + input);
            }

            @Override
            public void onFinish() {
                log("解码结束:" + input);
            }

            @Override
            public void onError(String content) {
                log("解码失败:" + content);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void log(String content) {
        mLog.append(content);
        if (!TextUtils.isEmpty(content)) {
            mLog.append("\n");
        }
        TextView statusView = findViewById(R.id.log);
        statusView.setText(mLog.toString());
    }

    private boolean needFile(File file) {
        return file != null && file.isFile() && file.getName().endsWith(END_FIX) && file.length() >= MIN_SIZE;
    }

    private void openDir() {
        File file = new File(OUTPUT_PATH);
        if (!file.exists() || !file.isDirectory()) {
            return;
        }
        try {
            Uri uri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", file);
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(uri, "*/*");
            startActivity(intent);
        } catch (Exception e) {
            log(e.getMessage());
        }
    }

    @SuppressLint("SimpleDateFormat")
    private String createPath(int index) {
        Date date = new Date();
        SimpleDateFormat formator = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-");
        return OUTPUT_PATH + formator.format(date) + index + ".mp3";
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!mPermissionChecked) {
            checkPermission();
        }
        mPermissionChecked = true;
    }

    private void checkPermission() {
        MainActivityPermissionsDispatcher.permissionForStorageWithCheck(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    private void sendResult(boolean granted) {
        mPermissionGranted = granted;
    }

    //读写存储
    @NeedsPermission({PERMISSION_READ_EXTERNAL_STORAGE, PERMISSION_WRITE_EXTERNAL_STORAGE})
    public void permissionForStorage() {
        sendResult(true);
    }

    @OnPermissionDenied({PERMISSION_READ_EXTERNAL_STORAGE, PERMISSION_WRITE_EXTERNAL_STORAGE})
    public void showDeniedForStorage() {
        sendResult(false);
    }

    @OnShowRationale({PERMISSION_READ_EXTERNAL_STORAGE, PERMISSION_WRITE_EXTERNAL_STORAGE})
    public void showRationalForStorage(final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setTitle("权限申请提示")
                .setMessage("解码音乐之后需要存储音乐副本,请您同意存储权限的申请.")
                .setPositiveButton("知道了", (dialog, which) -> request.proceed())
                .setNegativeButton("拒绝", (dialog, which) -> request.cancel())
                .setCancelable(false)
                .show();
    }
}
