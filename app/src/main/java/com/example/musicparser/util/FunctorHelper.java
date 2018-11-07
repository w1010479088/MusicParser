package com.example.musicparser.util;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import permissions.dispatcher.PermissionRequest;

public class FunctorHelper {

    public static void needPermission(Context context, final PermissionRequest request) {
        new AlertDialog.Builder(context)
                .setTitle("权限申请提示")
                .setMessage("熊猫美妆需要您授权一些权限才能运行某些功能,请您在接下来点击允许！")
                .setPositiveButton("知道了", (dialog, which) -> request.proceed())
                .setNegativeButton("拒绝", (dialog, which) -> request.cancel())
                .setCancelable(false)
                .show();
    }

    public static void createDialog(Context context, String title, String message, DialogInterface.OnClickListener confirmListener, DialogInterface.OnClickListener cancelListener) {
        createDialog(context, title, message, "确定", "取消", confirmListener, cancelListener);
    }

    public static void createDialog(Context context, String title, String message, String positiveTitle, String negativeTitle, DialogInterface.OnClickListener confirmListener, DialogInterface.OnClickListener cancelListener) {
        createDialog(context, true, title, message, positiveTitle, negativeTitle, confirmListener, cancelListener);
    }

    public static void createDialog(Context context, boolean cancelable, String title, String message, String positiveTitle, String negativeTitle, DialogInterface.OnClickListener confirmListener, DialogInterface.OnClickListener cancelListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setCancelable(cancelable)
                .setPositiveButton(positiveTitle, confirmListener)
                .setNegativeButton(negativeTitle, cancelListener);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
    }

    public static void createDialog(Context context, String title, String message, DialogInterface.OnClickListener confirmListener) {
        createDialog(context, title, message, "知道了", confirmListener);
    }

    public static void createDialog(Context context, String title, String message, String positiveTitle, DialogInterface.OnClickListener confirmListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(positiveTitle, confirmListener);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }
}
