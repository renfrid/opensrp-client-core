package org.smartregister.util;

import android.content.Context;
import android.util.Log;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by koros on 3/24/16.
 */
public class AssetHandler {

    public static final String TAG = "AssetHandler";

    public static String readFileFromAssetsFolder(String fileName, Context context) {
        String fileContents = null;
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            fileContents = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            android.util.Log.e(TAG, ex.toString(), ex);
            return null;
        }
        //Log.d("File", fileContents);
        return fileContents;
    }

    public static <T> T assetJsonToJava(Map jsonMap, Context context, String fileName, Class<T> clazz, Type type) {
        try {
            if (jsonMap == null) {
                return null;
            } else if (jsonMap.containsKey(fileName)) {
                Object o = jsonMap.get(fileName);
                if (clazz.isAssignableFrom(o.getClass())) {
                    return clazz.cast(jsonMap.get(fileName));
                } else {
                    return null;
                }
            }

            String jsonString = readFileFromAssetsFolder(fileName, context);
            if (StringUtils.isBlank(jsonString)) {
                return null;
            }
            T t;
            if (type == null)
                t = JsonFormUtils.gson.fromJson(jsonString, clazz);
            else
                t = JsonFormUtils.gson.fromJson(jsonString, type);
            jsonMap.put(fileName, t);
            return t;
        } catch (Exception e) {
            Log.e(TAG, e.toString(), e);
            return null;
        }
    }

    public static <T> T assetJsonToJava(Map jsonMap, Context context, String fileName, Class<T> clazz) {
        return assetJsonToJava(jsonMap, context, fileName, clazz, null);
    }
}
