package com.invoice.port.hnhangxin.invoice.util;

import org.apache.log4j.Logger;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Created by TT on 2018-03-16.
 */
public class GZipUtils {
    public static final int BUFFER = 1024;
    public static final String EXT = ".gz";
    private static final Logger log = Logger.getLogger(GZipUtils.class);

    public static byte[] compress(byte[] data) {
        byte[] output = (byte[]) null;
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(data);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            compress(bais, baos);
            output = baos.toByteArray();

            baos.flush();
            baos.close();

            bais.close();
        } catch (Exception e) {
            log.error("未知：" + e);
        }
        return output;
    }

    public static void compress(File file)
            throws Exception {
        compress(file, true);
    }

    public static void compress(File file, boolean delete)
            throws Exception {
        FileInputStream fis = new FileInputStream(file);
        FileOutputStream fos = new FileOutputStream(file.getPath() + ".gz");

        compress(fis, fos);

        fis.close();
        fos.flush();
        fos.close();

        if (delete) {
            boolean fal = file.delete();
            if (log.isDebugEnabled())
                log.debug("文件删除" + fal);
        }
    }

    public static void compress(InputStream is, OutputStream os)
            throws Exception {
        GZIPOutputStream gos = new GZIPOutputStream(os);

        byte[] data = new byte[1024];
        int count;
        while ((count = is.read(data, 0, 1024)) != -1) {
            gos.write(data, 0, count);
        }

        gos.finish();

        gos.flush();
        gos.close();
    }

    public static void compress(String path)
            throws Exception {
        compress(path, true);
    }

    public static void compress(String path, boolean delete)
            throws Exception {
        File file = new File(path);
        compress(file, delete);
    }

    public static byte[] decompress(byte[] data)
            throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        decompress(bais, baos);

        data = baos.toByteArray();

        baos.flush();
        baos.close();

        bais.close();

        return data;
    }

    public static void decompress(InputStream is, OutputStream os)
            throws Exception {
        GZIPInputStream gis = new GZIPInputStream(is);

        byte[] data = new byte[1024];
        int count;
        while ((count = gis.read(data, 0, 1024)) != -1) {
            os.write(data, 0, count);
        }

        gis.close();
    }
}
