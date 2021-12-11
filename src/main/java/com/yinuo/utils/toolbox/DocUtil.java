package com.yinuo.utils.toolbox;

import de.idyl.winzipaes.AesZipFileEncrypter;
import de.idyl.winzipaes.impl.AESEncrypter;
import de.idyl.winzipaes.impl.AESEncrypterBC;
import de.idyl.winzipaes.impl.AESEncrypterJCA;
import org.apache.poi.poifs.crypt.temp.AesZipFileZipEntrySource;
import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.*;

/**
 * @author liang
 */
public class DocUtil {


    public static void createDocFromHtml(String content, File file) {
        try ( //将内容转成数组, 使用的节点流
              ByteArrayInputStream in = new ByteArrayInputStream(content.getBytes());
              FileOutputStream os = new FileOutputStream(file);
        ) {
            POIFSFileSystem poifsFileSystem = new POIFSFileSystem();
            DirectoryEntry directory = poifsFileSystem.getRoot();
            directory.createDocument("WordDocument", in);
            poifsFileSystem.writeFilesystem(os);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        final String path = "D://";
        String fileName = "a.doc";
        String password = "123456";

        File source = new File(path, fileName);
        DocUtil.createDocFromHtml("<html><h1>hello</h1></html>", source);

        File dest = new File(path, source.getName() + ".zip");
        // AesZipFileEncrypter.zip(source, dest);
        // AesZipFileEncrypter.zipAndEncrypt(source, dest, password, new AESEncrypterJCA());
        AESEncrypter aesEncrypter = new AESEncrypterBC();
        aesEncrypter.init(password, 0);
        AesZipFileEncrypter ze = new AesZipFileEncrypter(dest, aesEncrypter);
        ze.add(source.getName(), new FileInputStream(source), password);
        ze.close();
    }
}
