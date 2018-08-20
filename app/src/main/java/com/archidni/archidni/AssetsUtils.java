package com.archidni.archidni;

import android.content.Context;

import com.google.android.gms.common.util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class AssetsUtils {

    public static String readFileFromAssets(String inFile,Context context) {
        String tContents = "";

        try {
            InputStream stream = context.getAssets().open(inFile);

            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            tContents = new String(buffer);
        } catch (IOException e) {
            // Handle exceptions here
        }

        return tContents;

    }

    public static byte[] readBytesFromAssets (String inFile,Context context)
    {
        byte [] bytes = null;

        try {
            InputStream stream = context.getAssets().open(inFile);

            bytes = IOUtils.toByteArray(stream);
        } catch (IOException e) {
            // Handle exceptions here
        }

        return bytes;
    }
}
