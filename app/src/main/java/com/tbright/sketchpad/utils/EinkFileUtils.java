package com.tbright.sketchpad.utils;

import android.content.Context;

import androidx.annotation.RawRes;

import com.tbright.sketchpad.BaseApplication;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class EinkFileUtils {
    public static void saveRawToDataPath(Context context, @RawRes int rawId, String fileNameWithExt){
        InputStream inStream = context.getResources().openRawResource(rawId);
        File file = new File(fileNameWithExt);
        FileOutputStream fileOutputStream = null;
        try {
            if(!file.exists()){
                file.createNewFile();
            }
            fileOutputStream = new FileOutputStream(file);byte[] buffer = new byte[10];
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            int len = 0;
            while ((len = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
            byte[] bs = outStream.toByteArray();
            fileOutputStream.write(bs);
            outStream.close();
            inStream.close();
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }

    }

    public static String getZipFileAbsolutePath(String taskId ,String fileName){
        return getPath(taskId,fileName,"zip");
    }
    public static String getUnZipFileAbsolutePath(String taskId ,String fileName){
        return getPath(taskId,fileName,"unzip");
    }

    public static String getPath(String taskId,String fileName,String typeName){
        String userId = "451548515946385";
        String androidFileName = BaseApplication.instance.getFilesDir().getAbsolutePath()+"/"+userId + "/"+taskId+"/"+typeName;
        File file = new File(androidFileName);
        if(!file.exists()){
            file.mkdirs();
        }
        return androidFileName + "/"+fileName;
    }
}
