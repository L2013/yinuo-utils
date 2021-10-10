package com.yinuo.utils.toolbox;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * @author liang
 */
public class TarKit {
    private static final int BUFFER = 1024;

    /**
     * 如有中文charset要使用如GBK等，试过UTF8，解析中文有异常
     */
    public static void extract(File tarFile, String destDir, String charset) throws Exception {
        TarArchiveInputStream tarArchiveInputStream = new TarArchiveInputStream(new FileInputStream(tarFile), charset);
        TarArchiveEntry entry = null;
        while ((entry = tarArchiveInputStream.getNextTarEntry()) != null) {
            File destFile = new File(destDir + File.separator + entry.getName());
            if (!destFile.exists()) {
                destFile.getParentFile().mkdirs();
            }
            deArchive(tarArchiveInputStream, destFile);
        }
    }

    private static void deArchive(TarArchiveInputStream tarArchiveInputStream, File destFile)
            throws Exception {
        BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream(destFile));

        int count;
        byte[] data = new byte[BUFFER];
        while ((count = tarArchiveInputStream.read(data, 0, BUFFER)) != -1) {
            bos.write(data, 0, count);
        }

        bos.close();
    }
}
