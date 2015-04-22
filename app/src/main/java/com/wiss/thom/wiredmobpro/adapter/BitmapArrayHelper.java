package com.wiss.thom.wiredmobpro.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

/**
 * Created by Thomas on 17.04.2015.
 */
public class BitmapArrayHelper {


    public static Bitmap BytesToBitmap(byte[] blob){
        return BitmapFactory.decodeByteArray(blob, 0, blob.length);
    }

    public static byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        return stream.toByteArray();
    }
}
