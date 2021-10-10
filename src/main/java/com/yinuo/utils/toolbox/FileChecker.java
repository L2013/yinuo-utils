package com.yinuo.utils.toolbox;

import java.io.File;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author liang
 */
public class FileChecker {
	private static Logger log = LoggerFactory.getLogger(FileChecker.class);

	public static void waitUntilFileReady(File f) throws Exception {
		waitUntilFileReady(f, 4 * 3600f, 10, 5);
	}


	public static void waitUntilFileReady(File f, float waitTime, int detectInterval, int targetEqTimes) throws Exception {
		long max = System.currentTimeMillis() + (long) (waitTime * 1000);
		long len = 0;
		int equalTimes = 0;
		while (true) {
			long now = System.currentTimeMillis();
			if (now > max) {
				throw new Exception("fail to wait file within " + waitTime + " seconds!");
			}
			if (!f.exists()) {
				equalTimes = 0;
				log.info("currently, file not exist..." + new Date());
			} else {
				equalTimes += (f.length() == len) ? 1 : 0;
				len = f.length();
				log.info(String.format("find file!size:%s,equal times:%d %s", f.length(), equalTimes, new Date()));
				if (equalTimes == targetEqTimes) {
					break;
				}
			}
			TimeUnit.SECONDS.sleep(detectInterval);
		}
	}
}
