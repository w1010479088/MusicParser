package com.example.musicparser.util;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by liuwei on 15/9/18.
 * IO工具类
 */
@SuppressWarnings("all")
public class IOUtil {
    public static final String ROOT_PATH = Environment.getExternalStorageDirectory().getPath();

    public static String getVerifiedPath(File file) {
        String path = file.getParentFile().getPath();       //不带后缀"/",导致路径出问题
        if (TextUtils.isEmpty(path)) {
            return null;
        } else {
            return path.endsWith("/") ? path : path + "/";
        }
    }

    public static void copyFile(String filePath, String destPath) {
        save(destPath, open(filePath, false), true);
    }

    public static boolean save(String path, byte[] data, boolean deleteOld) {
        FileOutputStream fos = null;
        try {
            File file = new File(path);
            createNewFile(getVerifiedPath(file), file.getName(), deleteOld);
            fos = new FileOutputStream(file, !deleteOld);
            fos.write(data);
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                //ignore
            }
        }
    }

    public static byte[] open(String path, boolean del) {
        File file = new File(path);
        if (file.exists()) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
                int len = fis.available();
                byte[] buffer = new byte[len];
                fis.read(buffer);
                return buffer;
            } catch (Exception e) {
                //ignore
                return null;
            } finally {
                if (del) {
                    delete(file);
                }
                try {
                    if (fis != null) {
                        fis.close();
                    }
                } catch (IOException ex) {
                    //ignore
                }
            }
        }
        return null;
    }

    public static void delete(String fileName) {
        File file = new File(fileName);
        delete(file);
    }

    public static void delete(File file) {
        if (file == null) return;
        if (file.exists()) {
            String pathName = file.getPath();
            if (file.isFile()) {
                file.delete();
            } else {
                deleteDir(pathName);
            }
        }
    }

    public static void deleteDir(String dirPath) {
        if (dirPath == null) {
            return;
        }
        if (!dirPath.endsWith(File.separator)) {
            dirPath = dirPath + File.separator;
        }
        File dir = new File(dirPath);
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        String[] fileNames = dir.list();
        File file;
        for (String fileName : fileNames) {
            String filePath = dirPath + fileName;
            file = new File(filePath);
            if (file.isDirectory()) {
                deleteDir(filePath);
            } else {
                file.delete();
            }
        }
        dir.delete();
    }

    public static byte[] transFormRawDataToByteArray(Context con, String filename) {
        InputStream is = null;
        try {
            is = con.getAssets().open(filename);
            if (is == null) {
                return null;
            }
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            return buffer;
        } catch (Exception e) {
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (Exception ex) {
                //ignore
            }
        }
    }

    public static byte[] transFormRawDataToByteArray(Context con, int rawId) {
        InputStream is = null;
        try {
            is = con.getResources().openRawResource(rawId);
            if (is == null) {
                return null;
            }
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            return buffer;
        } catch (Exception e) {
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (Exception ex) {
                //ignore
            }
        }
    }

    public static boolean downloadImage(String urlString, String filename, String savePath) {
        InputStream is = null;
        OutputStream os = null;
        try {
            URL url = new URL(urlString);
            URLConnection con = url.openConnection();
            con.setConnectTimeout(5 * 1000);
            is = con.getInputStream();
            int len;
            createNewFile(savePath, filename, true);
            File file = new File(savePath + filename);
            os = new FileOutputStream(file, false);
            byte[] bs = new byte[1024];
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
            return true;
        } catch (Exception ex) {
            return false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (Exception ex) {
                // ignore
            }
        }
    }

    public static String inputStream2Str(InputStream in) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder builder = new StringBuilder();
        try {
            String s;
            while ((s = reader.readLine()) != null) {
                builder.append(s);
            }
            return builder.toString();
        } catch (IOException e) {
            return null;
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (Exception ex) {
                //ignore
            }
        }
    }

    public static Uri fileToUri(File file, Context context) {
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }

    public static File createNewFile(String filePath, String fileName, boolean deleteOld) throws IOException {
        File fileDir = new File(filePath);
        createDir(fileDir, false);

        File file = new File(filePath, fileName);
        if (file.exists() && file.isFile() && deleteOld) {
            delete(file);
        }
        file.createNewFile();
        return file;
    }

    public static void createDir(String fileDirPath) {
        createDir(fileDirPath, true);
    }

    public static void createDir(String fileDirPath, boolean noMedia) {
        File fileDir = new File(fileDirPath);
        createDir(fileDir, noMedia);
    }

    public static void createDir(File fileDir) {
        createDir(fileDir, true);
    }

    public static void createDir(File fileDir, boolean noMedia) {
        if (fileDir.exists()) {
            if (fileDir.isFile()) {
                delete(fileDir);
                fileDir.mkdirs();
            }
        } else {
            fileDir.mkdirs();
        }
        if (noMedia) {
            createNoMediaFile(fileDir);
        }
    }

    private static void createNoMediaFile(File file) {
        File noMedia = new File(file, ".nomedia");
        try {
            noMedia.createNewFile();
        } catch (IOException e) {
            //ignore
        }
    }
}
