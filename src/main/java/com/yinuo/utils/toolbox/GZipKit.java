package com.yinuo.utils.toolbox;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;

public class GZipKit {
	/**
	 * 解压tar.gz文件 
	 * tar文件只是把多个文件或文件夹打包合成一个文件，本身并没有进行压缩。gz是进行过压缩的文件。
	 * @param gzFile
	 * @param destDir
	 * @throws Exception
	 */
	public static void deGzipArchive(String gzFile, String destDir) throws Exception {
		final CompressorInputStream in = new GzipCompressorInputStream(new FileInputStream(gzFile), true);
		TarArchiveInputStream tin = new TarArchiveInputStream(in, "GBK");
		TarArchiveEntry entry = tin.getNextTarEntry();
		while (entry != null) {
			File archiveEntry = new File(destDir, entry.getName());
			archiveEntry.getParentFile().mkdirs();
			if (entry.isDirectory()) {
				archiveEntry.mkdir();
				entry = tin.getNextTarEntry();
				continue;
			}
			OutputStream out = new FileOutputStream(archiveEntry);
			IOUtils.copy(tin, out);
			out.close();
			entry = tin.getNextTarEntry();
		}
		in.close();
		tin.close();
	}
}
