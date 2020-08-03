package com.elegion.tracktor.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.View;

import java.io.ByteArrayOutputStream;

import static android.util.Base64.DEFAULT;
import static android.util.Base64.encodeToString;


public class ScreenshotMaker {

    public static Bitmap makeScreenshot(View view) {
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        return bitmap;
    }

    public static String toBase64(Bitmap bitmap) {
        return encodeToString(getBytes(bitmap), DEFAULT);
    }

    private static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static Bitmap fromBase64(String base64) {
        byte[] decodedString = Base64.decode(base64, DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }
}
