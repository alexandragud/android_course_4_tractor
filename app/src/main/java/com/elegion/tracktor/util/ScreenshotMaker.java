package com.elegion.tracktor.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.view.View;

import androidx.core.content.ContextCompat;

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

    public static String toBase64(Bitmap bitmap, int compression) {
        return encodeToString(getBytes(bitmap, 100 - compression), DEFAULT);
    }

    private static byte[] getBytes(Bitmap bitmap, int compressionQuality) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.WEBP, compressionQuality, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static Bitmap fromBase64(String base64) {
        byte[] decodedString = Base64.decode(base64, DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    public static Bitmap fromVector(int vectorId, Context context) {
        Drawable drawable = ContextCompat.getDrawable(context, vectorId);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.draw(canvas);
        return bitmap;
    }
}
