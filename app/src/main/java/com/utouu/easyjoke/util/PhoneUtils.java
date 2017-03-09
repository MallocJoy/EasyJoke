package com.utouu.easyjoke.util;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;

/**
 * Created by xujixiao on 2016/11/21.17:36
 * 邮箱：1107313740@qq.com
 */

public class PhoneUtils {
    // 拍照存放本地文件名称
    public static final String IMAGE_FILE_NAME = "temp.png";
    // 裁剪后的文件名称
    public static final String IMAGE_FILE_NAME_TEMP = "temp.png";

    public static File file = new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME);
    public static Uri imageUri;

    public static File cropFile = new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME_TEMP);
    public static Uri imageCropUri = Uri.fromFile(cropFile);
    // 请求码
    public static final int CAMERA = 1;
    public static final int PICTURE = 2;
    public static final int CROP = 3;
    public static Uri cameraFileUri;
    public static String mPhotoPath;

    /**
     * 选择相机拍照
     *
     * @param activity
     */
    public static void takeCamera(Activity activity) {
        try {
            String directory = Environment.getExternalStorageDirectory().toString() + "/tailorx";
            if (directory != null) {
                File filePath = new File(directory);
                if (!filePath.exists()) {
                    filePath.mkdirs();
                }
                if (filePath != null) {
                    File file = new File(filePath, System.currentTimeMillis() + ".jpg");
                    if (file != null) {
                        cameraFileUri = Uri.fromFile(file);
                        mPhotoPath = file.getAbsolutePath();
                        if (cameraFileUri != null) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraFileUri);
                            activity.startActivityForResult(intent, CAMERA);
                        }
                    }
                }
            }
        } catch (Exception e) {
//            Tools.toast("请检查相机权限");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity.requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA);
            }
        }
    }

    /**
     * 选择图库
     *
     * @param activity
     */
    public static void selectPicture(Activity activity) {
        try {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            activity.startActivityForResult(intent, PICTURE);
        } catch (ActivityNotFoundException e) {
//            Tools.toast("您的设备没有相册支持,请安装!");
        }
    }

    /**
     * 裁剪图片方法实现，放置传输大图片内容exception方法
     */
    public static void startPhotoZoom(Activity context, Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("circleCrop", false);
        intent.putExtra("aspectY", 1);
//        intent.putExtra("scale", true);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 400);
        intent.putExtra("outputY", 400);
        //裁剪之后，保存在裁剪文件中，关键
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageCropUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("return-data", false);
        context.startActivityForResult(intent, 3);
    }

    /**
     * 裁剪图片方法实现
     * 此种调用会出现transaction too large exception
     *
     * @param uri
     */
    public static void startPhotoZoomWithException(Activity activity, Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", true);
        activity.startActivityForResult(intent, CROP);
    }

    /**
     * 保存裁剪之后的图片数据,并显示
     */
    public static Bitmap getBitmapFromUri(Activity activity, Intent data) {
        Bundle extras = data.getExtras();
        Bitmap bitmap = null;

        try {
            bitmap = BitmapFactory.decodeStream(activity.getContentResolver().openInputStream(imageCropUri));
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (extras != null) {
            // bitmap = extras.getParcelable("data");
            // Log.d("size",bitmap.getByteCount()+"");

            /*
             * ByteArrayOutputStream stream = new ByteArrayOutputStream();
             * bitmap.compress(Bitmap.CompressFormat.JPEG, 60, stream);
             * byte[] b = stream.toByteArray(); // 将图片流以字符串形式存储下来
             *
             * tp = new String(Base64Coder.encodeLines(b));
             * 这个地方大家可以写下给服务器上传图片的实现，直接把tp直接上传就可以了， 服务器处理的方法是服务器那边的事了，吼吼
             *
             * 如果下载到的服务器的数据还是以Base64Coder的形式的话，可以用以下方式转换 为我们可以用的图片类型就OK啦...吼吼
             * Bitmap dBitmap = BitmapFactory.decodeFile(tp);
             * Drawable drawable = new BitmapDrawable(dBitmap);
             */
        }
        return null;
    }

    /**
     * 添加到图库
     */
    public static void galleryAddPic(Context context, String path) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(path);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

}
