package mang.tools.ftp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogDownloadMonitor implements FTPDownListener {
	private static final transient Logger log = LoggerFactory.getLogger(LogDownloadMonitor.class);
	
	private int downloadCount=0;
	
	@Override
	public void afterDownload(String remoteFilePath,String localDirpath) {
		log.info("下载{} ok",remoteFilePath);
		downloadCount++;
	}

	@Override
	public int getDownloadCount() {
		return downloadCount;
	}

}
