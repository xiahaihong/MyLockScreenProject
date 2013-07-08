package com.example.mylockscreen.module;

import android.content.Context;
import android.taobao.filecache.FileCache;
import android.taobao.filecache.FileDir;
import com.example.mylockscreen.controllers.Controllers;
import org.apache.http.util.EncodingUtils;

import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * Created by haihong.xiahh on 13-7-5.
 */
public class FileService {

    private static final String GROUP_STORAGE = "storage";

    private FileDir getFileCache(String group) {
        FileDir fileDir = FileCache.getInsatance(Controllers.getInstance().getApplication())
                .getFileDirInstance(group, false);
        fileDir.init(null, null);
        fileDir.enableNoSpaceClear(true);
        return fileDir;
    }

    public String readStorage(final String fileName) {
        String content = null;
        byte[] bytes = getFileCache(GROUP_STORAGE).read(fileName);
        if (bytes != null) {
            try {
                content = new String(bytes, "UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return content;
    }

    public void writeStorage(final String fileName, final String content) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ByteBuffer bytes = ByteBuffer.wrap(content.getBytes());
                getFileCache(GROUP_STORAGE).write(fileName, bytes);
            }
        }).start();
    }

    public String readAssets(String assetsFileName, Context context) {
        String result = "";
        try {
            InputStream in = context.getResources().getAssets().open(assetsFileName);
            int length = in.available();
            byte[] buffer = new byte[length];
            in.read(buffer);
            result = EncodingUtils.getString(buffer, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
